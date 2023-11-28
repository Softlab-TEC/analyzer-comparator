package ui;

import Configurations.Configuration;
import Configurations.InputPath;
import com.google.gson.Gson;
import dao.mvc.Persistence;
import dataAccess.IExecutor;
import dataAccess.PMDExecutor;
import gastmappers.Language;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.exceptions.UnsupportedMetricException;
import metrics.run.ConfigurationReader;
import org.antlr.v4.runtime.misc.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ui.identity.dataClass.DataClassPanel;
import ui.identity.godClass.GodClassPanel;
import utils.Metrics;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
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
    protected String inputProjectName;
    protected String inputPath;
    protected String outputPath;
    protected static JPanel panel;
    protected ArrayList<InputPath> ip;
    protected String errorMessage;
    protected IExecutor executor;
    protected String metricName;
    protected String resultFileName;
    protected String executorOutputPathName;

    protected Map<String, String> projectsToProcess = new HashMap<>();
    protected ArrayList<String> alreadyRan = new ArrayList<>();
    protected boolean verifyIfExistInDB = false;


    public CodeSmellGeneralPanel(){
        executor = new PMDExecutor();
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

    public void setInputProjectName(String inputProjectName) {
        this.inputProjectName = inputProjectName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getExecutorOutputPathName() {
        return executorOutputPathName;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setExecutorOutputPathName(String executorOutputPathName) {
        this.executorOutputPathName = executorOutputPathName;
    }

    protected void runExecutor(Map.Entry<String, String> project){
        if(metricName.equals("ALL")) {
            GodClassPanel gc = new GodClassPanel();
            gc.setInputPath(project.getValue());
            gc.setSaveInDB(saveInDB);
            gc.setLanguage(language);
            gc.setExecutorOutputPathName(executorOutputPathName);
            gc.setMetricName(Metrics.GodClass);
            gc.runExecutor(project);
            DataClassPanel dc = new DataClassPanel();
            dc.setInputPath(project.getValue());
            dc.setSaveInDB(saveInDB);
            dc.setLanguage(language);
            dc.setExecutorOutputPathName(executorOutputPathName);
            dc.setMetricName(Metrics.DataClass);
            dc.runExecutor(project);
        }
        else {
            try {
                executor.execute(inputPath, executorOutputPathName, metricName);
            } catch (ExecutionException executionException) {
                executionException.printStackTrace();
                errorMessage = executionException.getMessage();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                errorMessage = interruptedException.getMessage();
            } catch (TimeoutException timeoutException) {
                timeoutException.printStackTrace();
                errorMessage = timeoutException.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            if (errorMessage != null && !errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(panel, errorMessage, "Error Found", JOptionPane.ERROR_MESSAGE);
                errorMessage = "";
            }
            if (saveInDB) ReadPMFileAndSaveResults(project.getKey());
            }
    }

    public void execute(){
        initProjectsToProcess();
        Persistence p = new Persistence(saveInDB, language, "");
        if(verifyIfExistInDB) alreadyRan = p.getProjectsRan();
        for (var project : projectsToProcess.entrySet() ) {
            if(verifyIfExistInDB && !alreadyRan.stream().anyMatch(r -> r.equals(project.getKey()))){
                inputPath = project.getValue();
                try {
                    if (!onlyAnalyzer) {
                        executorOutputPathName = outputPath.isEmpty() ?
                                createFolder(resultFileName) : outputPath + resultFileName;
                        runExecutor(project);
                    }
                    initAnalyzerAndSaveResults(project.getKey());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    errorMessage = ex.getMessage();
                } catch (UnsupportedMetricException unsupportedMetricException) {
                    unsupportedMetricException.printStackTrace();
                    errorMessage = unsupportedMetricException.getMessage();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    errorMessage = throwables.getMessage();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                    errorMessage = classNotFoundException.getMessage();
                } catch (UnsupportedLanguageException unsupportedLanguageException) {
                    unsupportedLanguageException.printStackTrace();
                    errorMessage = unsupportedLanguageException.getMessage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    errorMessage = ioException.getMessage();
                } catch (Exception ex) {
                    errorMessage = ex.getMessage();
                }
                if (errorMessage != null && !errorMessage.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, errorMessage, "Error Found", JOptionPane.ERROR_MESSAGE);
                    errorMessage = "";
                }
                System.out.println("No errors");
            }
        }
    }

    protected void initAnalyzerAndSaveResults(String projectName) throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException{

    }

    public void showResultsTable(int analyzerId, String language, String projectName){}

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
             configString.append(gson.toJson(config) + ",") ;
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
            System.out.println("PMD output path: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "";
        }
    }

    public void ReadPMFileAndSaveResults(String projectName){}

    private void initProjectsToProcess(){
        if(inputPath.isEmpty()) {
            //toDo:List of projects by default
        }else {
            projectsToProcess.put(inputProjectName, inputPath);//Todo capture name of the project
        }
    }


}

