package it.unibz.inf.ontop.sql.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import it.unibz.inf.ontop.model.Function;
import it.unibz.inf.ontop.model.OBDADataFactory;
import it.unibz.inf.ontop.model.Variable;
import it.unibz.inf.ontop.model.impl.OBDADataFactoryImpl;
import it.unibz.inf.ontop.sql.QualifiedAttributeID;
import it.unibz.inf.ontop.sql.QuotedID;
import it.unibz.inf.ontop.sql.RelationID;
import it.unibz.inf.ontop.sql.parser.exceptions.InvalidSelectQueryException;
import it.unibz.inf.ontop.utils.ImmutableCollectors;

import java.util.Map;
import java.util.Set;

import static java.util.function.Function.identity;

/**
 * Created by Roman Kontchakov on 01/11/2016.
 *
 */
public class RelationalExpression {
    private ImmutableList<Function> atoms;
    private ImmutableMap<QualifiedAttributeID, Variable> attributes;
    private ImmutableMap<QuotedID, ImmutableSet<RelationID>> attributeOccurrences;

    private static final OBDADataFactory FACTORY = OBDADataFactoryImpl.getInstance();

    /**
     * constructs a relation expression
     *
     * @param atoms                is an {@link ImmutableList}<{@link Function}>
     * @param attributes           is an {@link ImmutableMap}<{@link QualifiedAttributeID}, {@link Variable}>
     * @param attributeOccurrences is an {@link ImmutableMap}<{@link QuotedID}, {@link ImmutableSet}<{@link RelationID}>>
     */
    public RelationalExpression(ImmutableList<Function> atoms,
                                ImmutableMap<QualifiedAttributeID, Variable> attributes,
                                ImmutableMap<QuotedID, ImmutableSet<RelationID>> attributeOccurrences) {
        this.atoms = atoms;
        this.attributes = attributes;
        this.attributeOccurrences = attributeOccurrences;
    }

    /**
     * Checks if the attributeOccurrences contains the {@link QuotedID} attribute
     *
     * @param attribute is a  {@link QuotedID}
     * @return true if attributeOccurrences contains the {@link QuotedID} attribute otherwise false
     */

    private boolean isAbsent(QuotedID attribute) {
        ImmutableSet<RelationID> occurrences = attributeOccurrences.get(attribute);
        return (occurrences == null) || occurrences.isEmpty();
    }

    private boolean isAmbiguous(QuotedID attribute) {
        ImmutableSet<RelationID> occurrences = attributeOccurrences.get(attribute);
        return (occurrences != null) && occurrences.size() > 1;
    }

    private boolean isUnique(QuotedID attribute) {
        ImmutableSet<RelationID> occurrences = attributeOccurrences.get(attribute);
        return (occurrences != null) && occurrences.size() == 1;
    }

    public ImmutableList<Function> getAtoms() {
        return atoms;
    }

    public ImmutableMap<QualifiedAttributeID, Variable> getAttributes() {
        return attributes;
    }

    /**
     * CROSS JOIN of two relations (also denoted by , in SQL)
     *
     * @param re1 is a {@link RelationalExpression}
     * @param re2 is a {@link RelationalExpression}
     * @return a {@link RelationalExpression}
     */
    public static RelationalExpression crossJoin(RelationalExpression re1, RelationalExpression re2) {
        return joinOn(re1, re2, BooleanExpressionParser.empty());
    }


    /**
     * @param re1 is a {@link RelationalExpression}
     * @param re2 is a {@link RelationalExpression}
     * @param getAtomOnExpression is a {@link BooleanExpressionParser}
     * @return a {@link RelationalExpression}
     */
    public static RelationalExpression joinOn(RelationalExpression re1, RelationalExpression re2,
                                       BooleanExpressionParser getAtomOnExpression) {

        // TODO: better exception?
        if (!relationAliasesConsistent(re1.attributes, re2.attributes))
            throw new InvalidSelectQueryException("Relation alias occurs in both arguments of the join", null);

        ImmutableMap<QualifiedAttributeID, Variable> attributes = ImmutableMap.<QualifiedAttributeID, Variable>builder()
                .putAll(re1.filterAttributes(id ->
                        (id.getRelation() != null) || re2.isAbsent(id.getAttribute())))

                .putAll(re2.filterAttributes(id ->
                        (id.getRelation() != null) || re1.isAbsent(id.getAttribute())))

                .build();

        ImmutableList<Function> atoms = ImmutableList.<Function>builder()
                .addAll(re1.atoms)
                .addAll(re2.atoms)
                .addAll(getAtomOnExpression.apply(attributes))
                .build();

        ImmutableMap<QuotedID, ImmutableSet<RelationID>> attributeOccurrences =
                getAttributeOccurrences(re1, re2, id -> attributeOccurrencesUnion(id, re1, re2));

        return new RelationalExpression(atoms, attributes, attributeOccurrences);
    }

    /**
     * NATURAL JOIN of two relations
     *
     * @param re1 is a {@link RelationalExpression)
     * @param re2 is a {@link RelationalExpression)
     * @return a {@link RelationalExpression}
     */
    public static RelationalExpression naturalJoin(RelationalExpression re1, RelationalExpression re2) {

        // TODO: better exception?
        if (!relationAliasesConsistent(re1.attributes, re2.attributes))
            throw new InvalidSelectQueryException("Relation alias occurs in both arguments of the join", null);

        ImmutableSet<QuotedID> shared = re1.attributeOccurrences.keySet().stream()
                .filter(id -> !re1.isAbsent(id) && !re2.isAbsent(id))
                .collect(ImmutableCollectors.toSet());

        // TODO: better exception? more informative error message?
        if (shared.stream().anyMatch(id -> re1.isAmbiguous(id) || re2.isAmbiguous(id)))
            throw new UnsupportedOperationException("common ambiguous attribute in select");

        return internalJoinUsing(re1, re2, shared);
    }

    /**
     * JOIN USING of two relations
     *
     * @param re1 is a {@link RelationalExpression}
     * @param re2 is a {@link RelationalExpression}
     * @param using is a {@link ImmutableSet}<{@link QuotedID}>
     * @return a {@link RelationalExpression)
     */
    public static RelationalExpression joinUsing(RelationalExpression re1, RelationalExpression re2,
                                          ImmutableSet<QuotedID> using) {

        // TODO: better exception?
        if (!relationAliasesConsistent(re1.attributes, re2.attributes))
            throw new InvalidSelectQueryException("Relation alias occurs in both arguments of the join", null);

        if (using.stream().anyMatch(id -> !re1.isUnique(id) || !re2.isUnique(id)))
            throw new UnsupportedOperationException("ambiguous column attributes in using statement");

        return RelationalExpression.internalJoinUsing(re1, re2, using);
    }

    /**
     * JOIN USING of two relations
     *
     * @param re1 is a {@link RelationalExpression}
     * @param re2 is a {@link RelationalExpression}
     * @param using is a {@link Set}<{@link QuotedID}>
     * @return a {@link RelationalExpression}
     */
    private static RelationalExpression internalJoinUsing(RelationalExpression re1, RelationalExpression re2,
                                                          ImmutableSet<QuotedID> using) {

        ImmutableMap<QualifiedAttributeID, Variable> attributes = ImmutableMap.<QualifiedAttributeID, Variable>builder()
                .putAll(re1.filterAttributes(id ->
                        (id.getRelation() != null && !using.contains(id.getAttribute()))
                                || (id.getRelation() == null && re2.isAbsent(id.getAttribute()))
                                || (id.getRelation() == null && using.contains(id.getAttribute()))))

                .putAll(re2.filterAttributes(id ->
                        (id.getRelation() != null && !using.contains(id.getAttribute()))
                                || (id.getRelation() == null && re1.isAbsent(id.getAttribute()))))

                .build();

        ImmutableList<Function> atoms = ImmutableList.<Function>builder()
                .addAll(re1.atoms)
                .addAll(re2.atoms)
                .addAll(using.stream()
                        .map(id -> new QualifiedAttributeID(null, id))
                        .map(id -> {
                            // TODO: this will be removed later, when OBDA factory will start checking non-nulls
                            Variable v1 = re1.attributes.get(id);
                            Variable v2 = re2.attributes.get(id);
                            if (v1 == null || v2 == null)
                                throw new UnsupportedOperationException("Not found the related atom variable of " + id.toString());
                            return FACTORY.getFunctionEQ(v1, v2);
                        })
                        .iterator())
                .build();

        ImmutableMap<QuotedID, ImmutableSet<RelationID>> attributeOccurrences =
                getAttributeOccurrences(re1, re2,
                        id -> using.contains(id)
                                ? re1.attributeOccurrences.get(id)
                                : attributeOccurrencesUnion(id, re1, re2));

        return new RelationalExpression(atoms, attributes, attributeOccurrences);
    }

    /**
     *
     * @param atoms
     * @param unqualifiedAttributes
     * @param alias
     * @return
     */

    public static RelationalExpression create(ImmutableList<Function> atoms, ImmutableMap<QuotedID, Variable> unqualifiedAttributes, RelationID alias) {

        ImmutableMap<QualifiedAttributeID, Variable> attributes = ImmutableMap.<QualifiedAttributeID, Variable>builder()
                .putAll(unqualifiedAttributes.entrySet().stream()
                        .collect(ImmutableCollectors.toMap(
                                e -> new QualifiedAttributeID(alias, e.getKey()),
                                Map.Entry::getValue)))
                .putAll(unqualifiedAttributes.entrySet().stream()
                        .collect(ImmutableCollectors.toMap(
                                e -> new QualifiedAttributeID(null, e.getKey()),
                                Map.Entry::getValue)))
                .build();

        ImmutableMap<QuotedID, ImmutableSet<RelationID>> attributeOccurrences =
                unqualifiedAttributes.keySet().stream()
                        .collect(ImmutableCollectors.toMap(identity(), id -> ImmutableSet.of(alias)));

        return new RelationalExpression(atoms, attributes, attributeOccurrences);
    }

    /**
     *
     * @param re
     * @param alias
     * @return
     */

    public static RelationalExpression alias(RelationalExpression re, RelationID alias) {

        ImmutableMap<QuotedID, Variable> unqualifiedAttributes =
                re.attributes.entrySet().stream()
                        .filter(e -> e.getKey().getRelation() == null)
                        .collect(ImmutableCollectors.toMap(
                                e -> e.getKey().getAttribute(), Map.Entry::getValue));

        return create(re.atoms, unqualifiedAttributes, alias);
    }


    /**
     * treats null values as empty sets
     *
     * @param id is a {@link QuotedID}
     * @param re1 a {@link RelationalExpression}
     * @param re2 a {@link RelationalExpression}
     * @return the union of occurrences of id in e1 and e2
     */

    private static ImmutableSet<RelationID> attributeOccurrencesUnion(QuotedID id,
                                                                      RelationalExpression re1, RelationalExpression re2) {

        ImmutableSet<RelationID> s1 = re1.attributeOccurrences.get(id);
        ImmutableSet<RelationID> s2 = re2.attributeOccurrences.get(id);

        if (s1 == null)
            return s2;

        if (s2 == null)
            return s1;

        return ImmutableSet.<RelationID>builder().addAll(s1).addAll(s2).build();
    }

    private ImmutableMap<QualifiedAttributeID, Variable> filterAttributes(java.util.function.Predicate<QualifiedAttributeID> condition) {

        return attributes.entrySet().stream()
                .filter(e -> condition.test(e.getKey()))
                .collect(ImmutableCollectors.toMap());
    }


    private static ImmutableMap<QuotedID, ImmutableSet<RelationID>> getAttributeOccurrences(RelationalExpression re1,
                                                                                            RelationalExpression re2,
                                                                                            java.util.function.Function<QuotedID, ImmutableSet<RelationID>>
                                                                                                    collector) {

        ImmutableSet<QuotedID> keys = ImmutableSet.<QuotedID>builder()
                .addAll(re1.attributeOccurrences.keySet())
                .addAll(re2.attributeOccurrences.keySet())
                .build();

        return keys.stream()
                .collect(ImmutableCollectors.toMap(identity(), collector));
    }


    /**
     * return false if a relation alias occurs in both arguments of the join.
     *
     * @param attributes1 is an {@link ImmutableMap}<{@link QualifiedAttributeID}, {@link Variable}>
     * @param attributes2 is an {@link ImmutableMap}<{@link QualifiedAttributeID}, {@link Variable}>
     */
    private static boolean relationAliasesConsistent(ImmutableMap<QualifiedAttributeID, Variable> attributes1,
                                                     ImmutableMap<QualifiedAttributeID, Variable> attributes2) {
        // the first one is mutable
        ImmutableSet<RelationID> alias1 = attributes1.keySet().stream()
                .filter(id -> id.getRelation() != null)
                .map(QualifiedAttributeID::getRelation).collect(ImmutableCollectors.toSet());

        ImmutableSet<RelationID> alias2 = attributes2.keySet().stream()
                .filter(id -> id.getRelation() != null)
                .map(QualifiedAttributeID::getRelation).collect(ImmutableCollectors.toSet());

        return !alias1.stream().anyMatch(alias2::contains);
    }


    @Override
    public String toString() {
        return "RelationalExpression : " + atoms + "\n" + attributes + "\n" + attributeOccurrences;
    }


}