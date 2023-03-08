package ui;

import Configurations.Configuration;
import Configurations.InputPath;
import com.google.gson.Gson;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.exceptions.UnsupportedMetricException;
import metrics.run.ConfigurationReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CodeSmellGeneralPanel {
    protected GridBagConstraints constraints;
    protected boolean onlyAnalyzer;
    protected boolean isFolder;
    protected boolean isGastFile;
    protected boolean isFromMemory;
    protected boolean useThreads;
    protected boolean saveInDB;
    protected boolean returnMetrics;
    protected  String inputPath;
    protected  String outputPath;
    protected static JPanel panel;
    protected ArrayList<InputPath> ip;

    public CodeSmellGeneralPanel(){
        panel = new JPanel(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
    }

    public static JPanel getPanel() {
        return panel;
    }

    public static void setPanel(JPanel panel) {
        CodeSmellGeneralPanel.panel = panel;
    }

    public boolean isOnlyAnalyzer() {
        return onlyAnalyzer;
    }

    public void setOnlyAnalyzer(boolean onlyAnalyzer) {
        this.onlyAnalyzer = onlyAnalyzer;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public boolean isGastFile() {
        return isGastFile;
    }

    public void setGastFile(boolean gastFile) {
        isGastFile = gastFile;
    }

    public boolean isFromMemory() {
        return isFromMemory;
    }

    public void setFromMemory(boolean fromMemory) {
        isFromMemory = fromMemory;
    }

    public boolean isUseThreads() {
        return useThreads;
    }

    public void setUseThreads(boolean useThreads) {
        this.useThreads = useThreads;
    }

    public boolean isSaveInDB() {
        return saveInDB;
    }

    public void setSaveInDB(boolean saveInDB) {
        this.saveInDB = saveInDB;
    }

    public boolean isReturnMetrics() {
        return returnMetrics;
    }

    public void setReturnMetrics(boolean returnMetrics) {
        this.returnMetrics = returnMetrics;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void execute(){}

    protected void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException{

    }

    public void showResultsTable(int analyzerId){}

    public void configureJsonForAnalyzer(){
        ip = new ArrayList<>();
        ip.add(new InputPath(
                        inputPath,
                        isFolder,
                        isGastFile,
                        isFromMemory,
                        useThreads
                )
        );
    }

    public Output callAnalyzer(String codeSmellName, ArrayList<Number> inputParameters) throws UnsupportedMetricException, SQLException, ParseException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        Configuration config = new Configuration(codeSmellName, outputPath, inputParameters, returnMetrics, ip);
        JSONParser jsonParser = new JSONParser();
        Gson gson = new Gson();
        String configString = "{\"metrics\": [" + gson.toJson(config) + "]}";
        JSONObject configJSON = (JSONObject) jsonParser.parse(configString);
        ConfigurationReader cr = new ConfigurationReader();
        return cr.processJSONConfigReturningOutput(configJSON);
    }

    //region helpers
    protected boolean isAValidNumericInput(String field, String variableName) {
        try {
            float floatValue = Float.parseFloat(field);
            return !Float.isNaN(floatValue);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(panel, variableName + " is not a valid number");
            return false;
        }
    }

    protected JTextField initField(JTextField input){
        input = new JTextField();
        input.setPreferredSize(new Dimension(30, 30));
        return input;
    }

    protected void placeElementInPanel(JLabel label, JTextField field, String name, int y){
        constraints.gridy = y;
        constraints.gridx = 0;
        label = new JLabel(name);
        panel.add(label, constraints);
        constraints.gridx = 1;
        panel.add(field, constraints);
    }

}

