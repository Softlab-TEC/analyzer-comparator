package ui;

import Configurations.Configuration;
import Configurations.InputPath;
import com.google.gson.Gson;
import dataAccess.IExecutor;
import gastmappers.Language;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.exceptions.UnsupportedMetricException;
import metrics.run.ConfigurationReader;
import org.antlr.v4.runtime.misc.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CodeSmellGeneralPanel {
    protected GridBagConstraints constraints;
    protected Language language;
    protected boolean onlyAnalyzer;
    protected boolean isFolder;
    protected boolean isGastFile;
    protected boolean isFromMemory;
    protected boolean useThreads;
    protected boolean saveInDB;
    protected boolean returnMetrics;
    protected String inputPath;
    protected String outputPath;
    protected static JPanel panel;
    protected ArrayList<InputPath> ip;
    protected String errorMessage;
    protected IExecutor executor;
    protected String metricName;
    protected String resultFileName;
    protected String executorOutputPathName;

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

    public Language getLanguage() { return language; }

    public void setLanguage(Language language) { this.language = language; }

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

    public void execute(){
        ///Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src
        //input: "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main/java/org/hibernate/cfg/HbmBinder.java"
        //Output: /Users/williamcasasola/Documents/TEC/Detector/test/
        try {
            if(!onlyAnalyzer){
                executorOutputPathName = outputPath.isEmpty() ?
                    createFolder(resultFileName) : outputPath +  resultFileName;
                executor.execute(inputPath, executorOutputPathName, metricName);
                if (saveInDB) ReadPMFileAndSaveResults();
            }
            initAnalyzerAndSaveResults();
        } catch (ParseException ex) {
            ex.printStackTrace();
            errorMessage = ex.getMessage();
        } catch (UnsupportedMetricException unsupportedMetricException) {
            unsupportedMetricException.printStackTrace();
            errorMessage = unsupportedMetricException.getMessage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            errorMessage = throwables.getMessage();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            errorMessage = ioException.getMessage();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            errorMessage = classNotFoundException.getMessage();
        } catch (UnsupportedLanguageException unsupportedLanguageException) {
            unsupportedLanguageException.printStackTrace();
            errorMessage = unsupportedLanguageException.getMessage();
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
            errorMessage = executionException.getMessage();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            errorMessage = interruptedException.getMessage();
        } catch (TimeoutException timeoutException) {
            timeoutException.printStackTrace();
            errorMessage = timeoutException.getMessage();
        }
        catch (Exception ex){
            errorMessage = ex.getMessage();
        }
        if(errorMessage != null && !errorMessage.isEmpty()){
            JOptionPane.showMessageDialog(panel, errorMessage, "Error Found", JOptionPane.ERROR_MESSAGE);
            errorMessage = "";
        }
        System.out.println("No errors");
    }

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

    public ArrayList<Output> callAnalyzerAll(ArrayList<Pair<String, ArrayList<Number>>> inputParameters) throws UnsupportedMetricException, SQLException, ParseException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        JSONParser jsonParser = new JSONParser();
        Gson gson = new Gson();
        ConfigurationReader cr = new ConfigurationReader();
        StringBuilder configString = new StringBuilder("{\"metrics\": [");
        for(Pair<String, ArrayList<Number>> thresholds : inputParameters){
            Configuration config = new Configuration(language, thresholds.a, outputPath, thresholds.b, returnMetrics, ip);
             configString.append(gson.toJson(config)) ;
        }
        configString.append("]}");
        JSONObject configJSON = (JSONObject) jsonParser.parse(configString.toString());
        return cr.processJSONConfigReturningOutputs(configJSON);
    }

    public Output callAnalyzer(String codeSmellName, ArrayList<Number> inputParameters) throws UnsupportedMetricException, SQLException, ParseException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        Configuration config = new Configuration(language, codeSmellName, outputPath, inputParameters, returnMetrics, ip);
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
        input.setPreferredSize(new Dimension(50, 20));
        return input;
    }

    protected JLabel placeElementInPanel(JLabel label, JTextField field, String name, int y){
        constraints.gridy = y;
        constraints.gridx = 0;
        label = new JLabel(name);
        panel.add(label, constraints);
        constraints.gridx = 1;
        panel.add(field, constraints);
        return label;
    }

    protected String createFolder(String fileName){
        try {
            File directory = new File("Results");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory, fileName);
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

    public void ReadPMFileAndSaveResults(){}

}

