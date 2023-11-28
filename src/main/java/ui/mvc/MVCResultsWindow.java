package ui.mvc;

import dao.mvc.*;
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
    private static Checkbox avoidTestFile;

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


        avoidTestFile = new Checkbox("Exclude Test files");

        constraints.gridx = 0;
        constraints.gridy = 3;

        panel.add(avoidTestFile, constraints);


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
        constraints.gridy = 4;
        JButton btn = new JButton("Execute");
        btn.setBackground(Color.GRAY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
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
                    case "SpringLint":
                        showResults(AnalyzerConstants.SPRINGLINT, language, inputPath);
                        break;
                    case "Analyzer":
                        showResults(AnalyzerConstants.ANALYZER, language, inputPath);
                        break;
                        default: showResults(0, language, inputPath);
                }
        }
    }

    private void showResults(int analyzerId, String language, String projectName){
        showPromiscuousResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
        showBrainControllerResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
        showMeddlingResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
        showLaboriousResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
        showBrainRepositoryResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
        showFatRepositoryResultsTable(analyzerId, language, projectName, avoidTestFile.getState());
    }

    public void showPromiscuousResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles) {
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM PROMISCUOUSCONTROLLER WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests + " ORDER BY classname, analyzerid, insertdate";
        ArrayList<PromiscuousControllerDAO> t;
        PromiscuousControllerDAO pc = new PromiscuousControllerDAO();
        switch (analyzerId) {
            case 0:
                t = pc.getPromiscuousControllers(sql);
                break;
            default:
                t = pc.getPromiscuousControllersOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles);
                break;
        }
        ui.TableModel tableModel = new PromiscuousControllerTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Promiscuous Controller " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

    public void showBrainControllerResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles) {
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM BRAINCONTROLLER WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests + " ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainControllerDAO> t;
        BrainControllerDAO bc = new BrainControllerDAO();
        switch (analyzerId) {
            case 0:
                t = bc.getBrainControllers(sql);
                break;
            default:
                t = bc.getBrainControllersOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles);
                break;
        }
        ui.TableModel tableModel = new BrainControllerTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Controller " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

    public void showMeddlingResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles){
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM MEDDLINGSERVICE WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests + " ORDER BY classname, analyzerid, insertdate";
        ArrayList<MeddlingServiceDAO> t;
        MeddlingServiceDAO ms = new MeddlingServiceDAO();
        switch (analyzerId){
            case 0: t = ms.getMeddlingServices(sql); break;
            default: t = ms.getMeddlingServicesOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles); break;
        }
        ui.TableModel tableModel = new MeddlingServiceTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Meddling Service " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

    public void showLaboriousResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles){
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM LABORIOUSREPOSITORYMETHOD WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests + " ORDER BY classname, analyzerid, insertdate";
        ArrayList<LaboriousRepositoryMethodDAO> t = new ArrayList<>();
        LaboriousRepositoryMethodDAO lrm = new LaboriousRepositoryMethodDAO();
        switch (analyzerId){
            case 0: t = lrm.getLaboriousRepositoryMethods(sql); break;
            default: t = lrm.getLaboriousRepoMethodOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles); break;
        }
        ui.TableModel tableModel = new LaboriousRepositoryMethodTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Laborious Repository Method " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

    public void showFatRepositoryResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles){
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM FATREPOSITORY WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests + " ORDER BY classname, analyzerid, insertdate";
        ArrayList<FatRepositoryDAO> t;
        FatRepositoryDAO fr = new FatRepositoryDAO();
        switch (analyzerId){
            case 0: t = fr.geFatRepositories(sql); break;
            default: t = fr.getFatRepositoriesOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles); break;
        }
        ui.TableModel tableModel = new FatRepositoryTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Fat Repository " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

    public void showBrainRepositoryResultsTable(int analyzerId, String language, String projectName, boolean avoidTestFiles){
        String avoidTests = avoidTestFiles ? " AND classname NOT LIKE '%test%' " : "";
        String sql = "SELECT * FROM BRAINREPOSITORY WHERE projectname = '" +  projectName + "' AND language = '" + language +
                "' " + avoidTests +" ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainRepositoryDAO> t;
        BrainRepositoryDAO bc = new BrainRepositoryDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainRepositories(sql); break;
            default: t = bc.getBrainRepositoriesOnlyAnalyzer(analyzerId, language, projectName, avoidTestFiles); break;
        }
        ui.TableModel tableModel = new BrainRepositoryTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Repository " + AnalyzerConstants.getAnalyzerName(analyzerId));
        ct.drawTable();
    }

}
