/*
 * Copyright (c) 2012 Patrick Meyer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.itemanalysis.jmetrik.gui;

import com.itemanalysis.jmetrik.selector.MultipleSelectionPanel;
import com.itemanalysis.jmetrik.sql.DataTableName;
import com.itemanalysis.jmetrik.sql.DatabaseName;
import com.itemanalysis.jmetrik.workspace.SubsetVariableCommand;
import com.itemanalysis.jmetrik.workspace.VariableChangeEvent;
import com.itemanalysis.jmetrik.workspace.VariableChangeListener;
import com.itemanalysis.psychometrics.data.VariableAttributes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class SubsetVariablesDialog extends JDialog implements VariableChangeListener {

    // Variables declaration - do not modify
    private MultipleSelectionPanel selectionPanel;
    private JTextField newTableNameTextField;
    private JLabel tableLabel;
    private ArrayList<VariableAttributes> variables;
    private SubsetVariableCommand command;
    private DatabaseName dbName;
    private DataTableName tableName;
    private boolean canRun = false;
    // End of variables declaration

    /** Creates new form SubsetVariablesDialog */
    public SubsetVariablesDialog(JFrame parent, DatabaseName dbName, DataTableName tableName, ArrayList<VariableAttributes> variables) {
        super(parent, "Subset Variables", true);
        this.dbName = dbName;
        this.tableName = tableName;
        this.variables = variables;
        initComponents();

        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        tableLabel = new JLabel();
        newTableNameTextField = new JTextField();
        selectionPanel = new MultipleSelectionPanel();
//        VariableType filterType = new VariableType(VariableType.NO_FILTER, VariableType.NO_FILTER);
//        selectionPanel.addUnselectedFilterType(filterType);
//        selectionPanel.addSelectedFilterType(filterType);
        selectionPanel.setVariables(variables);

        selectionPanel.showButton4(false);
        JButton b1 = selectionPanel.getButton1();
        b1.setText("OK");
        b1.addActionListener(new OkActionListener());

        JButton b2 = selectionPanel.getButton2();
        b2.setText("Cancel");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JButton b3 = selectionPanel.getButton3();
        b3.setText("Clear");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionPanel.reset();
                newTableNameTextField.setText("");
                canRun = false;
            }
        });


        tableLabel.setText("New Table Name:");

        newTableNameTextField.setToolTipText("New table name");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(selectionPanel, 414, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(5,5,5)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(tableLabel)
                                                        .addComponent(newTableNameTextField, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(selectionPanel, 272, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(10,10,10)
                                .addComponent(tableLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(newTableNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    public boolean canRun(){
        return canRun;
    }

    public SubsetVariableCommand getCommand(){
        return command;
    }

    public void variableChanged(VariableChangeEvent e){
        selectionPanel.variableChanged(e);
    }


    class OkActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            command = new SubsetVariableCommand();
            command.getPairedOptionList("data").addValue("db", dbName.getName());
            command.getPairedOptionList("data").addValue("table", tableName.getTableName());

            boolean ready = true;

            if(!selectionPanel.hasSelection()){
                JOptionPane.showMessageDialog(SubsetVariablesDialog.this,
                        "You must select one or more variables.",
                        "No Variables Selected",
                        JOptionPane.ERROR_MESSAGE);
                ready = false;
            }

            if("".equals(newTableNameTextField.getText().trim())){
                JOptionPane.showMessageDialog(SubsetVariablesDialog.this,
                        "You must provide a name for the new table.",
                        "No Table Name Provided",
                        JOptionPane.ERROR_MESSAGE);
                ready = false;
            }

            if(ready){
                command.getFreeOption("newtable").add(newTableNameTextField.getText().trim());
                command.getSelectAllOption("options").setSelected("display", true);

                VariableAttributes[] vars = selectionPanel.getSelectedVariables();
                for(VariableAttributes v : vars){
                    command.getFreeOptionList("variables").addValue(v.getName().toString());
                }
                canRun = true;
                setVisible(false);
            }
        }

    }


}
