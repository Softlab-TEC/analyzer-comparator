package ui.identity;

import dao.identity.*;
import dataAccess.JSpIRITResultsReader;
import dataAccess.SpringLintResultsReader;
import gastmappers.Language;
import ui.ComparativeTable;
import ui.TableModel;
import ui.identity.brainClass.BrainClassTableModel;
import ui.identity.brainMethod.BrainMethodTableModel;
import ui.identity.dataClass.DataClassTableModel;
import ui.identity.featureEnvy.FeatureEnvyTableModel;
import ui.identity.godClass.GodClassTableModel;
import utils.AnalyzerConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class IdentityResultsWindow {

    protected GridBagConstraints constraints;
    protected String inputPath;
    protected String language;
    protected String selectedTool;
    protected static JPanel panel;
    protected String errorMessage;

    private static JTextField inputPathField;
    private static JLabel inputPathLabel;
    private static JLabel languageLabel;
    private static JLabel toolLabel;

    public IdentityResultsWindow(){
        panel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        initialize();
    }

    public static JPanel getPanel() {
        return panel;
    }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }

    public void setSelectedTool(String selectedTool) {
        this.selectedTool = selectedTool;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    private void initialize(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        languageLabel = new JLabel("Tool:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(languageLabel, constraints);

        languageLabel = new JLabel("Language:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(languageLabel, constraints);

        inputPathLabel = new JLabel("System name:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(inputPathLabel, constraints);


        inputPathField = new JTextField();
        inputPathField.setPreferredSize(new Dimension(300,30));

        String[] languagesString = { Language.JAVA.toString(), Language.CSHARP.toString() };
        String[] toolsString = { "Analyzer", "JSpIRIT", "PMD", "All" };

        selectedTool = toolsString[0];
        language = languagesString[0];

        JComboBox langList = new JComboBox(languagesString);
        langList.addActionListener(new ComboLanguageListener());

        JComboBox toolList = new JComboBox(toolsString);
        toolList.addActionListener(new ComboToolListener());

        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(toolList, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(langList, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(inputPathField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        JButton btn = new JButton("Execute");
        btn.setBackground(Color.GRAY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.addActionListener(new BtnListenerExecutor());
        panel.add(btn, constraints);
    }

    private class ComboLanguageListener implements ActionListener {
        public void actionPerformed (ActionEvent e){
            JComboBox cb = (JComboBox) e.getSource();
            String language = (String) cb.getSelectedItem();
            setLanguage(language);
        }
    }

    private class ComboToolListener implements ActionListener {
        public void actionPerformed (ActionEvent e){
            JComboBox cb = (JComboBox) e.getSource();
            String tool = (String) cb.getSelectedItem();
            setSelectedTool(tool);
        }
    }

    private class BtnListenerExecutor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String inputPath = inputPathField.getText();
            switch (selectedTool){
                case "PMD":
                    showResults(AnalyzerConstants.PMD, language, inputPath);
                    break;
                case "JSpIRIT":
                    showResults(AnalyzerConstants.JSPIRIT, language, inputPath);
                    break;
                case "Analyzer":
                    showResults(AnalyzerConstants.ANALYZER, language, inputPath);
                    break;
                default: showResults(0, language, inputPath);
            }
        }
    }

    private void showResults(int analyzerId, String language, String projectName){
        showGodClassResultsTable(analyzerId, language, projectName);
        showDataClassResultsTable(analyzerId, language, projectName);
        showFeatureEnvyResultsTable(analyzerId, language, projectName);
        showBrainMethodResultsTable(analyzerId, language, projectName);
        showBrainClassResultsTable(analyzerId, language, projectName);
    }

    public void showGodClassResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM GODCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<GodClassDAO> t;
        GodClassDAO gcd = new GodClassDAO();
        switch (analyzerId){
            case 0: t = gcd.getGodClasses(sql); break;
            default: t = gcd.getGodClassesOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        javax.swing.table.TableModel tableModel = new GodClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "God Class");
        ct.drawTable();
    }

    public void showDataClassResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM DATACLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<DataClassDAO> t;
        DataClassDAO dc = new DataClassDAO();
        switch (analyzerId){
            case 0: t = dc.getDataClasses(sql); break;
            default: t = dc.getDataClassesOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        TableModel tableModel = new DataClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Data Class");
        ct.drawTable();
    }

    public void showFeatureEnvyResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM FEATUREENVY ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<FeatureEnvyDAO> t;
        FeatureEnvyDAO fe = new FeatureEnvyDAO();
        switch (analyzerId){
            case 0: t = fe.getFeatureEnvies(sql); break;
            default: t = fe.getFeatureEnviesOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        TableModel tableModel = new FeatureEnvyTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Feature Envy");
        ct.drawTable();
    }

    public void showBrainMethodResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINMETHOD ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<BrainMethodDAO> t;
        BrainMethodDAO bm = new BrainMethodDAO();
        switch (analyzerId){
            case 0: t = bm.getBrainMethods(sql); break;
            default: t = bm.getBrainMethodsOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new BrainMethodTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Method");
        ct.drawTable();
    }

    public void showBrainClassResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainClassDAO> t;
        BrainClassDAO bc = new BrainClassDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainClasses(sql); break;
            default: t = bc.getBrainClassesOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new BrainClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Class");
        ct.drawTable();
    }
}
