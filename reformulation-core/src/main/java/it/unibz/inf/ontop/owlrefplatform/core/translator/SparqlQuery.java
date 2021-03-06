package it.unibz.inf.ontop.owlrefplatform.core.translator;

import it.unibz.inf.ontop.model.DatalogProgram;

import java.util.List;

/**
 * Created by roman on 27/03/2016.
 */
public class SparqlQuery {

    private final DatalogProgram program;
    private final List<String> signature;

    public SparqlQuery(DatalogProgram program, List<String> signature) {
        this.program = program;
        this.signature = signature;
    }

    public DatalogProgram getProgram() {
        return program;
    }

    public List<String> getSignature() {
        return signature;
    }

    @Override
    public String toString() {
        return signature.toString() + "\n" + program.toString();
    }
}
