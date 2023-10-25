package ui.mvc;

import dao.mvc.*;
import dataAccess.JSpIRITResultsReader;
import dataAccess.SpringLintResultsReader;
import gastmappers.Language;
import ui.ComparativeTable;
import ui.mvc.brainController.BrainControllerTableModel;
import ui.mvc.brainRepository.BrainRepositoryTableModel;
import ui.mvc.fatRepository.FatRepositoryTableModel;
import ui.mvc.laboriousRepositoryMethod.LaboriousRepositoryMethodTableModel;
import ui.mvc.meddlingService.MeddlingServiceTableModel;
import ui.mvc.promiscuousController.PromiscuousControllerTableModel;
import utils.AnalyzerConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class MVCResultsWindow {
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

    public MVCResultsWindow(){
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
        String[] toolsString = { "Analyzer", "SpringLint", "All" };

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
                    case "JSpIRIT":
                        showResults(AnalyzerConstants.JSPIRIT, language, inputPath);
                        break;
                    case "Analyzer":
                        showResults(AnalyzerConstants.ANALYZER, language, inputPath);
                        default: showResults(0, language, inputPath);
                }
        }
    }

    private void showResults(int analyzerId, String language, String projectName){
        showPromiscuousResultsTable(analyzerId, language, projectName);
        showBrainControllerResultsTable(analyzerId, language, projectName);
        showMeddlingResultsTable(analyzerId, language, projectName);
        showLaboriousResultsTable(analyzerId, language, projectName);
        showBrainRepositoryResultsTable(analyzerId, language, projectName);
        showFatRepositoryResultsTable(analyzerId, language, projectName);
    }

    public void showPromiscuousResultsTable(int analyzerId, String language, String projectName) {
        String sql = "SELECT * FROM PROMISCUOUSCONTROLLER ORDER BY classname, analyzerid, insertdate";
        ArrayList<PromiscuousControllerDAO> t = new ArrayList<>();
        PromiscuousControllerDAO pc = new PromiscuousControllerDAO();
        switch (analyzerId) {
            case 0:
                t = pc.getPromiscuousControllers(sql);
                break;
            case 1:
                t = pc.getPromiscuousControllersOnlyAnalyzer(analyzerId, language, projectName);
                break;
            case 2:
                t = pc.getPromiscuousControllersOnlySpringLint(analyzerId, language, projectName);
                break;
        }
        ui.TableModel tableModel = new PromiscuousControllerTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    public void showBrainControllerResultsTable(int analyzerId, String language, String projectName) {
        String sql = "SELECT * FROM BRAINCONTROLLER ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainControllerDAO> t = new ArrayList<>();
        BrainControllerDAO bc = new BrainControllerDAO();
        switch (analyzerId) {
            case 0:
                t = bc.getBrainControllers(sql);
                break;
            case 1:
                t = bc.getBrainControllersOnlyAnalyzer(analyzerId, language, projectName);
                break;
            case 2:
                t = bc.getBrainControllersOnlySpringLint(analyzerId, language, projectName);
                break;
        }
        ui.TableModel tableModel = new BrainControllerTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    public void showMeddlingResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM MEDDLINGSERVICE ORDER BY classname, analyzerid, insertdate";
        ArrayList<MeddlingServiceDAO> t = new ArrayList<>();
        MeddlingServiceDAO ms = new MeddlingServiceDAO();
        switch (analyzerId){
            case 0: t = ms.getMeddlingServices(sql); break;
            case 1: t = ms.getMeddlingServicesOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = ms.getMeddlingServiceOnlySpringLint(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new MeddlingServiceTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    public void showLaboriousResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM LABORIOUSREPOSITORYMETHOD ORDER BY classname, analyzerid, insertdate";
        ArrayList<LaboriousRepositoryMethodDAO> t = new ArrayList<>();
        LaboriousRepositoryMethodDAO lrm = new LaboriousRepositoryMethodDAO();
        switch (analyzerId){
            case 0: t = lrm.getLaboriousRepositoryMethods(sql); break;
            case 1: t = lrm.getLaboriousRepoMethodOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = lrm.getLaboriousRepoMethodOnlySpringLint(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new LaboriousRepositoryMethodTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    public void showFatRepositoryResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM FATREPOSITORY ORDER BY classname, analyzerid, insertdate";
        ArrayList<FatRepositoryDAO> t = new ArrayList<>();
        FatRepositoryDAO fr = new FatRepositoryDAO();
        switch (analyzerId){
            case 0: t = fr.geFatRepositories(sql); break;
            case 1: t = fr.getFatRepositoriesOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = fr.getFatRepositoriesOnlySpringLint(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new FatRepositoryTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    public void showBrainRepositoryResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINREPOSITORY ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainRepositoryDAO> t = new ArrayList<>();
        BrainRepositoryDAO bc = new BrainRepositoryDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainRepositories(sql); break;
            case 1: t = bc.getBrainRepositoriesOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = bc.getBrainRepositoriesOnlySpringLint(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new BrainRepositoryTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

}
