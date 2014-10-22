package org.semanticweb.ontop.protege4.dialogs;

/*
 * #%L
 * ontop-protege4
 * %%
 * Copyright (C) 2009 - 2013 KRDB Research Centre. Free University of Bozen Bolzano.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.semanticweb.ontop.model.OBDADataFactory;
import org.semanticweb.ontop.model.OBDADataSource;
import org.semanticweb.ontop.model.impl.OBDADataFactoryImpl;
import org.semanticweb.ontop.model.impl.RDBMSourceParameterConstants;

/*
 * SetParametersDialog.java
 *
 * Created on 27-set-2010, 9.54.38
 */

/**
 *
 * @author obda
 */
public class SetParametersDialog extends javax.swing.JDialog {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -395702104656733013L;
	private SQLOBDAModel apic = null;
	
    /** Creates new form SetParametersDialog */
    public SetParametersDialog(java.awt.Frame parent, boolean modal, SQLOBDAModel apic) {
        super(parent, modal);
        this.apic = apic;
        initComponents();
        addListeners();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Source Parameters"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("JDBC URL:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jLabel3, gridBagConstraints);

        jTextField1.setPreferredSize(new java.awt.Dimension(150, 20));
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jTextField1, gridBagConstraints);

        jLabel4.setText("DBName:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jLabel4, gridBagConstraints);

        jTextField2.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jTextField2, gridBagConstraints);

        jLabel5.setText("Username:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jLabel5, gridBagConstraints);

        jTextField3.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jTextField3, gridBagConstraints);

        jLabel6.setText("Password:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jLabel6, gridBagConstraints);

        jTextField4.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jTextField4, gridBagConstraints);

        jLabel7.setText("JDBC Driver:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jLabel7, gridBagConstraints);

        jTextField5.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 6, 6);
        jPanel1.add(jTextField5, gridBagConstraints);

        jButton1.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jLabel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

 //GEN-LAST:event_jTextField1ActionPerformed
    
    private void addListeners(){
    	jButton1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				performOKButtonAction();
			}
		});
    }
    
    private void performOKButtonAction(){
    	String url = jTextField1.getText();
    	String dbname = jTextField2.getText();
    	String usr = jTextField3.getText();
    	String pwd = jTextField4.getText();
    	String driver = jTextField5.getText();
    	
    	if(url.length()<1 || dbname.length()<1 || usr.length()<1 || pwd.length()<1 || driver.length()<1){
    		JOptionPane.showMessageDialog(this, "Please fill out all fields.");
    	}else{
    	
    		OBDADataSource aux = getOldAboxDump();
    		if(aux != null){
    			aux.setParameter(RDBMSourceParameterConstants.DATABASE_DRIVER, driver);
    			aux.setParameter(RDBMSourceParameterConstants.DATABASE_USERNAME, usr);
    			aux.setParameter(RDBMSourceParameterConstants.DATABASE_PASSWORD, pwd);
    			aux.setParameter(RDBMSourceParameterConstants.DATABASE_URL, url);
//    			aux.setParameter(RDBMSsourceParameterConstants.ONTOLOGY_URI, apic.getCurrentOntologyURI().toString());
    			aux.setParameter(RDBMSourceParameterConstants.USE_DATASOURCE_FOR_ABOXDUMP, "true");
    			apic.updateSource(aux.getSourceID(), aux);
    		}else{
	    		OBDADataFactory fac = OBDADataFactoryImpl.getInstance();
		    	OBDADataSource ds = fac.getDataSource(URI.create("ABOXDUMP"));
		    	ds.setParameter(RDBMSourceParameterConstants.DATABASE_DRIVER, driver);
		    	ds.setParameter(RDBMSourceParameterConstants.DATABASE_USERNAME, usr);
		    	ds.setParameter(RDBMSourceParameterConstants.DATABASE_PASSWORD, pwd);
		    	ds.setParameter(RDBMSourceParameterConstants.DATABASE_URL, url);
//		    	ds.setParameter(RDBMSsourceParameterConstants.ONTOLOGY_URI, apic.getCurrentOntologyURI().toString());
		    	ds.setParameter(RDBMSourceParameterConstants.USE_DATASOURCE_FOR_ABOXDUMP, "true");
		    	apic.addSource(ds);
    		}
	    
	    	this.setVisible(false);
    	}
    }
    
    private OBDADataSource getOldAboxDump(){
    	
    	OBDADataSource dump = null;
    	
    	Iterator<OBDADataSource> it = apic.getSources().iterator();
    	while(it.hasNext()){
    		OBDADataSource aux = it.next();
    		String s = aux.getParameter(RDBMSourceParameterConstants.USE_DATASOURCE_FOR_ABOXDUMP);
    		if(s != null && s.equals("true")){
    			dump = aux;
    			break;
    		}
    	}
    	
    	return dump;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

}