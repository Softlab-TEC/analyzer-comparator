package ui;
import gastmappers.Language;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ConfigurationMenuPanel extends JPanel {

    private static JTextField inputPathField;
    private static JTextField inputProjectNameField;
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
    private static JLabel inputProjectNameLabel;
    private static JLabel languageLabel;
    private static JLabel outputPathLabel;

    CodeSmellGeneralPanel codeSmellGeneralPanel;

    public ConfigurationMenuPanel(CodeSmellGeneralPanel codeSmellDetector){
        this.codeSmellGeneralPanel = codeSmellDetector;
        String smellName = codeSmellGeneralPanel.isDrawMetricsPanel() ?
                codeSmellGeneralPanel.getCodeSmellName() : "All";
        TitledBorder resultsBorder = BorderFactory.createTitledBorder("Configurations-"+ smellName);
        setBorder(resultsBorder);
        codeSmellGeneralPanel.setLanguage(Language.JAVA);
        setLayout(new GridBagLayout());
        initialize();
    }

    private void initialize(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 10, 0,10);
        Dimension labelSize = new Dimension(150, 30);


        languageLabel = new JLabel("Language:");
        languageLabel.setSize(labelSize);
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(languageLabel, constraints);

        inputPathLabel = new JLabel("Input Path:");
        inputPathLabel.setSize(labelSize);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(inputPathLabel, constraints);

        inputProjectNameLabel = new JLabel("Project Name:");
        inputProjectNameLabel.setSize(labelSize);
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(inputProjectNameLabel, constraints);

        outputPathLabel = new JLabel("Output Path:");
        outputPathLabel.setSize(labelSize);
        constraints.gridy = 3;
        add(outputPathLabel, constraints);

        inputProjectNameField = new JTextField();
        inputProjectNameField.setPreferredSize(new Dimension(300,30));
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
        Dimension preferredSize = new Dimension(150, 30);
        isFolderToggle = new Checkbox("Folder");
        isGastFileToggle = new Checkbox("GAST file");
        isFromMemoryToggle = new Checkbox("Memory");
        useThreadsToggle = new Checkbox("Use threads");
        saveInDBToggle = new Checkbox("Save results in DB");
        onlyAnalyzerToggle = new Checkbox("Run only Analyzer");
        returnMetricsToggle = new Checkbox("Return metrics");

        isFolderToggle.setPreferredSize(preferredSize);
        isGastFileToggle.setPreferredSize(preferredSize);
        isFromMemoryToggle.setPreferredSize(preferredSize);
        useThreadsToggle.setPreferredSize(preferredSize);
        saveInDBToggle.setPreferredSize(preferredSize);
        onlyAnalyzerToggle.setPreferredSize(preferredSize);
        returnMetricsToggle.setPreferredSize(preferredSize);


        String[] languagesString = { Language.JAVA.toString(), Language.CSHARP.toString() };

        JComboBox langList = new JComboBox(languagesString);
        langList.addActionListener(new ComboListener());

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(langList, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(inputPathField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        add(inputProjectNameField, constraints);

        constraints.gridy = 3;
        add(outputPathField, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        add(isFolderToggle, constraints);
        constraints.gridx = 1;
        add(isGastFileToggle, constraints);
        constraints.gridy = 5;
        constraints.gridx = 0;
        add(useThreadsToggle, constraints);
        constraints.gridx = 1;
        add(isFromMemoryToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        add(saveInDBToggle, constraints);

        constraints.gridx = 1;
        add(onlyAnalyzerToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        add(returnMetricsToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 8;
        if(codeSmellGeneralPanel.isDrawMetricsPanel()){
            add(codeSmellGeneralPanel.getPanel(),  constraints);
        } else {
            constraints.gridy = 7;
        }

        constraints.gridx = 1;
        JButton btn = new JButton("Execute");
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.addActionListener(new BtnListenerExecutor());
        add(btn, constraints);
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


    public class ComboListener implements ActionListener {
            public void actionPerformed (ActionEvent e){
                JComboBox cb = (JComboBox) e.getSource();
                String language = (String) cb.getSelectedItem();
                codeSmellGeneralPanel.setLanguage(Language.valueOf(language));
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
            codeSmellGeneralPanel.setInputProjectName(inputProjectNameField.getText());
            codeSmellGeneralPanel.setOutputPath(outputPathField.getText());
            codeSmellGeneralPanel.execute();
        }
    }

}
