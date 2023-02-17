package UI;

import Configurations.Configuration;
import Configurations.InputPath;
import DAO.GodClassDAO;
import DataAccess.PMDExecutor;
import DataAccess.PMDResultsReader;
import UI.GodClass.ComparativeTable;
import UI.GodClass.GodClassTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigurationMenuPanel extends JPanel {

    private static JTextField inputPathField;
    private static JTextField outputPathField;
    private static JTextField wmcField;
    private static JTextField tccField;
    private static JTextField atfdField;
    private static Checkbox isFolderToggle;
    private static Checkbox isGastFileToggle;
    private static Checkbox isFromMemoryToggle;
    private static Checkbox useThreadsToggle;
    private static Checkbox saveInDBToggle;
    private static Checkbox onlyAnalyzerToggle;
    private static Checkbox returnMetricsToggle;
    private static JLabel inputPathLabel;
    private static JLabel outputPathLabel;

    CodeSmellGeneralPanel codeSmellGeneralPanel;

    public ConfigurationMenuPanel(CodeSmellGeneralPanel codeSmellDetector){
        this.codeSmellGeneralPanel = codeSmellDetector;
        setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        inputPathLabel = new JLabel("Input Path:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(inputPathLabel, constraints);

        outputPathLabel = new JLabel("Output Path:");
        constraints.gridy = 1;
        add(outputPathLabel, constraints);

        inputPathField = new JTextField();
        inputPathField.setPreferredSize(new Dimension(300,30));
        outputPathField = new JTextField();
        outputPathField.setPreferredSize(new Dimension(300,30));
        wmcField = new JTextField();
        wmcField.setPreferredSize(new Dimension(30, 30));
        tccField = new JTextField();
        tccField.setPreferredSize(new Dimension(30, 30));
        atfdField = new JTextField();
        atfdField.setPreferredSize(new Dimension(30, 30));
        isFolderToggle = new Checkbox("Folder");
        isGastFileToggle = new Checkbox("GAST file");
        isFromMemoryToggle = new Checkbox("Memory");
        useThreadsToggle = new Checkbox("Use threads");
        saveInDBToggle = new Checkbox("Save results in DB");
        onlyAnalyzerToggle = new Checkbox("Run only Analyzer");
        returnMetricsToggle = new Checkbox("Return metrics");

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(inputPathField, constraints);

        constraints.gridy = 1;
        add(outputPathField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        add(isFolderToggle, constraints);
        constraints.gridx = 1;
        add(isGastFileToggle, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        add(useThreadsToggle, constraints);
        constraints.gridx = 1;
        add(isFromMemoryToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(saveInDBToggle, constraints);

        constraints.gridx = 1;
        add(onlyAnalyzerToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        add(returnMetricsToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        add(codeSmellGeneralPanel.getPanel(),  constraints);


        constraints.gridx = 1;
        constraints.gridy = 9;
        JButton btn = new JButton("Execute");
        btn.addActionListener(new BtnListenerExecutor());
        add(btn, constraints);

        JButton btnOnlyAnalyzer = new JButton("Only Analyzer");
        btnOnlyAnalyzer.addActionListener(new BtnListenerShowResultsTable(1));
        constraints.gridy = 10;
        constraints.gridx = 0;
        add(btnOnlyAnalyzer, constraints);

        JButton btnOnlyPMD = new JButton("Only PMD");
        btnOnlyPMD.addActionListener(new BtnListenerShowResultsTable(2));
        constraints.gridx = 1;
        add(btnOnlyPMD, constraints);

        JButton btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(new BtnListenerShowResultsTable(0));
        constraints.gridx = 0;
        constraints.gridy = 11;
        add(btnShowAll, constraints);
    }

    private String createFolder(){
        try {
            File directory = new File("Results");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory, "PMDoutput.txt");
            if (file.createNewFile()) {
                System.out.println("File created in Results folder: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "";
        }
    }

    private class BtnListenerShowResultsTable implements ActionListener {
        private int analyzerId;
        public BtnListenerShowResultsTable(int analyzerId){
            this.analyzerId = analyzerId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            codeSmellGeneralPanel.showResultsTable(analyzerId);
        }
    }

    private class BtnListenerExecutor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //input: "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main/java/org/hibernate/cfg/HbmBinder.java"
            //Output: /Users/williamcasasola/Documents/TEC/Detector/test/
            codeSmellGeneralPanel.setOnlyAnalyzer(onlyAnalyzerToggle.getState());
            codeSmellGeneralPanel.setFolder(isFolderToggle.getState());
            codeSmellGeneralPanel.setFromMemory(isFromMemoryToggle.getState());
            codeSmellGeneralPanel.setGastFile(isGastFileToggle.getState());
            codeSmellGeneralPanel.setReturnMetrics(returnMetricsToggle.getState());
            codeSmellGeneralPanel.setUseThreads(useThreadsToggle.getState());
            codeSmellGeneralPanel.setSaveInDB(saveInDBToggle.getState());
            codeSmellGeneralPanel.setInputPath(inputPathField.getText());
            codeSmellGeneralPanel.setOutputPath(outputPathField.getText());
            codeSmellGeneralPanel.execute();
        }
    }

}
