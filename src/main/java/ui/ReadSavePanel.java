package ui;

import dataAccess.JSpIRITResultsReader;
import gastmappers.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ReadSavePanel {
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

    public ReadSavePanel(){
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

        inputPathLabel = new JLabel("Input Path:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(inputPathLabel, constraints);


        inputPathField = new JTextField();
        inputPathField.setPreferredSize(new Dimension(300,30));

        String[] languagesString = { Language.JAVA.toString(), Language.CSHARP.toString() };
        String[] toolsString = { "JSpIRIT", "SpringLint" };

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
            try {
                if(selectedTool.equals("JSpIRIT")){
                    JSpIRITResultsReader jspiritReader = new JSpIRITResultsReader();
                    jspiritReader.readResult(inputPath, language);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                errorMessage = ex.getMessage();
            }
        }
    }
}
