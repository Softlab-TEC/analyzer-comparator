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
            if(project.getKey().isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Project name is empty and it's required" );
                return;
            }
            boolean isNotGoingToBeDuplicated = verifyIfExistInDB && !alreadyRan.stream().anyMatch(r -> r.equals(project.getKey()));
            if(!verifyIfExistInDB  || isNotGoingToBeDuplicated){
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
        return cr.processJSONConfigReturningOutputs(configJSON).get(0);
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
        projectsToProcess = new HashMap<>();
        if(inputPath.isEmpty()) {
            projectsToProcess.put("ant", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/ant/ant-1.8.4/src/apache-ant-1.8.4");
            projectsToProcess.put("antlr", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/antlr/antlr-4.0/src");
            projectsToProcess.put("aoi", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/aoi/aoi-2.8.1/src/AoIsrc281");
            projectsToProcess.put("argo", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/argouml/argouml-0.34/src/argouml-java/src/org");
            projectsToProcess.put("argo.argouml", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/argouml/argouml-0.34/src/argouml/src");
            projectsToProcess.put("aspectj", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/aspectj/aspectj-1.6.9/src/org");
            projectsToProcess.put("axion", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/axion/axion-1.0-M2/src/axion-1.0-M2/src/org/axiondb");
            projectsToProcess.put("azureus", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/azureus/azureus-4.8.1.2/src");
            projectsToProcess.put("batik", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/batik/batik-1.7/src/batik-1.7");
            projectsToProcess.put("c_jdbc", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/c_jdbc/c_jdbc-2.0.2/src/c-jdbc-2.0.2-src/src");
            projectsToProcess.put("castor.annttask", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/castor/castor-1.3.1/src/castor-1.3.1/anttask");
            projectsToProcess.put("castor.core", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/castor/castor-1.3.1/src/castor-1.3.1/core");
            projectsToProcess.put("castor.cpaptf", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/castor/castor-1.3.1/src/castor-1.3.1/cpaptf");
            projectsToProcess.put("castor.diff", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/castor/castor-1.3.1/src/castor-1.3.1/diff");
            projectsToProcess.put("castor.schema", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/castor/castor-1.3.1/src/castor-1.3.1/schema");
            projectsToProcess.put("cayenne", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/cayenne/cayenne-3.0.1/src/cayenne-3.0.1-src");
            projectsToProcess.put("checkstyle", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/checkstyle/checkstyle-5.1/src/checkstyle-src-5.1/src");
            projectsToProcess.put("cobertura", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/cobertura/cobertura-1.9.4.1/src/cobertura-1.9.4.1/src/net/sourceforge/cobertura");
            projectsToProcess.put("collections", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/collections/collections-3.2.1/src/commons-collections-3.2.1-src/src");
            projectsToProcess.put("colt", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/colt/colt-1.2.0/src/colt/src");
            projectsToProcess.put("columnba", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/columba/columba-1.0/src/columba-1.0-src/src");
            projectsToProcess.put("compiere", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/compiere/compiere-330/src/release_330");
            //projectsToProcess.put("derby", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/derby/derby-10.6.1.0/src/db-derby-10.6.1.0-src/java");
            projectsToProcess.put("displayTag.examples", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/displaytag/displaytag-1.2/src/displaytag-1.2/displaytag-examples");
            projectsToProcess.put("displayTag.displayTag", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/displaytag/displaytag-1.2/src/displaytag-1.2/displaytag");
            projectsToProcess.put("displayTag.displayTag-export-poi", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/displaytag/displaytag-1.2/src/displaytag-1.2/displaytag-export-poi");
            projectsToProcess.put("displayTag.displayTag-portlet", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/displaytag/displaytag-1.2/src/displaytag-1.2/displaytag-portlet");
            projectsToProcess.put("drawswf", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/drawswf/drawswf-1.2.9/src/drawswf-src-1.2.9");
            projectsToProcess.put("drjava", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/drjava/drjava-stable-20100913-r5387/src/drjava-stable-20100913-r5387/src/src");
            // projectsToProcess.put("eclipse_SDK", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/eclipse_SDK/eclipse_SDK-4.3/src/src");
            projectsToProcess.put("emma", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/emma/emma-2.0.5312/src/emma-2.0.5312");
            projectsToProcess.put("exoportal", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/exoportal/exoportal-v1.0.2/src/exo");
            projectsToProcess.put("findbugs", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/findbugs/findbugs-1.3.9/src/findbugs-1.3.9/src");
            projectsToProcess.put("fitjava", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/fitjava/fitjava-1.1/src/source");
            projectsToProcess.put("fitlibraryforfitness", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/fitlibraryforfitnesse/fitlibraryforfitnesse-20100806/src");
            projectsToProcess.put("freecol", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/freecol/freecol-0.10.7/src/freecol/src/net/sf/freecol");
            projectsToProcess.put("freecs", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/freecs/freecs-1.3.20100406/src/freecs-1.3.20100406/src/freecs");
            projectsToProcess.put("freemind", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/freemind/freemind-0.9.0/src/freemind/freemind");
            projectsToProcess.put("galleon", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/galleon/galleon-2.3.0/src/src/org/lnicholls/galleon");
            projectsToProcess.put("gantproject", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/ganttproject/ganttproject-2.0.9/src/ganttproject-2.0.9-src/ganttproject/src");
            //projectsToProcess.put("gt2", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/gt2/gt2-2.7-M3/src/geotools-2.7-M3");
            projectsToProcess.put("hadoop", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hadoop/hadoop-1.0.0/src/hadoop-1.0.0/src");
            projectsToProcess.put("heritrix", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/heritrix/heritrix-1.14.4/src/heritrix-1.14.4/src");
            projectsToProcess.put("hibernate.tutorial.annotations", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/documentation/src/main/docbook/quickstart/tutorials/annotations");
            projectsToProcess.put("hibernate.tuttorial.basic", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/documentation/src/main/docbook/quickstart/tutorials/basic");
            projectsToProcess.put("hibernate.tutorial.entityManager", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/documentation/src/main/docbook/quickstart/tutorials/entitymanager");
            projectsToProcess.put("hibernate.tutorial.envers", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/documentation/src/main/docbook/quickstart/tutorials/envers");
            projectsToProcess.put("hibernate.tutorial.osgi", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/documentation/src/main/docbook/quickstart/tutorials/osgi");
            projectsToProcess.put("hibernate.hibernate-c3po", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-c3p0");
            projectsToProcess.put("hibernate.hibernate-envers", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-envers");
            projectsToProcess.put("hibernate.hibernate-core", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main");
            projectsToProcess.put("hibernate.hibernate-ehcache", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-ehcache");
            projectsToProcess.put("hibernate.hibernate-entityManager", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-entitymanager");
            projectsToProcess.put("hibernate.hibernate-infinispan", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-infinispan");
            projectsToProcess.put("hsqldb", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hsqldb/hsqldb-2.0.0/src/hsqldb-2.0.0/hsqldb/src/org");
            projectsToProcess.put("htmlunit", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/htmlunit/htmlunit-2.8/src/htmlunit-2.8/src");
            projectsToProcess.put("informa", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/informa/informa-0.7.0-alpha2/src/informa-0.7.0-alpha2/src/de/nava/informa");
            projectsToProcess.put("ireport.jasperreports-components", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/ireport/ireport-3.7.5/src/iReport-3.7.5-src/jasperreports-components");
            projectsToProcess.put("itext", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/itext/itext-5.0.3/src/src/core/com/itextpdf/text");
            projectsToProcess.put("ivatagroupware", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/ivatagroupware/ivatagroupware-0.11.3/src/ivatagroupware-src-0.11.3/core/src");
            projectsToProcess.put("jag", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jag/jag-6.1/src/jag-6.1/src");
            projectsToProcess.put("james", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/james/james-2.2.0/src/src");
            projectsToProcess.put("jasml", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jasml/jasml-0.10/src/src/com/jasml");
            projectsToProcess.put("jasperreports", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jasperreports/jasperreports-3.7.3/src/jasperreports-3.7.4/src");
            projectsToProcess.put("javacc", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/javacc/javacc-5.0/src/javacc/src");
            projectsToProcess.put("jboss.adminclient", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/adminclient");
            projectsToProcess.put("jboss.aspects", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/aspects");
            projectsToProcess.put("jboss.cluster", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/cluster");
            projectsToProcess.put("jboss.console", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/console");
            projectsToProcess.put("jboss.deployment", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/deployment");
            projectsToProcess.put("jboss.ejb3", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/ejb3");
            projectsToProcess.put("jboss.embedded", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/embedded");
            projectsToProcess.put("jboss.hibernate-int", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/hibernate-int");
            projectsToProcess.put("jboss.j2se", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/j2se");
            projectsToProcess.put("jboss.jmx", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/jmx");
            projectsToProcess.put("jboss.management", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/management");
            projectsToProcess.put("jboss.mbeans", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/mbeans");
            projectsToProcess.put("jboss.jmx-remoting", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/jmx-remoting");
            projectsToProcess.put("jboss.security", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/security");
            projectsToProcess.put("jboss.system", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/system");
            projectsToProcess.put("jboss.webservices", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jboss/jboss-5.1.0/src/jboss-5.1.0.GA-src/webservices");
            projectsToProcess.put("jchempaint", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jchempaint/jchempaint-3.0.1/src/org.openscience.cdk.jchempaint30/src/main/org/openscience");
            projectsToProcess.put("jedit", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jedit/jedit-4.3.2/src/jEdit");
            projectsToProcess.put("jena", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jena/jena-2.6.3/src");
            projectsToProcess.put("jext", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jext/jext-5.0/src/jext-src-5.0/src");
            projectsToProcess.put("jFin_DateMath", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jFin_DateMath/jFin_DateMath-R1.0.1/src/src");
            projectsToProcess.put("jfreechart", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jfreechart/jfreechart-1.0.13/src/jfreechart-1.0.13/source/org/jfree");
            projectsToProcess.put("jgraph", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jgraph/jgraph-5.13.0.0/src/src");
            projectsToProcess.put("jgraphpad", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jgraphpad/jgraphpad-5.10.0.2/src/jgraphpad-5.10.0.2-src/src/org");
            projectsToProcess.put("jgrapht", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jgrapht/jgrapht-0.8.1/src/jgrapht-0.8.1/src/org/jgrapht");
            projectsToProcess.put("jgroups", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jgroups/jgroups-2.10.0/src/JGroups-2.10.0.GA.src/src/org/jgroups");
            projectsToProcess.put("jmoney", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jmoney/jmoney-0.4.4/src/source/net/sf/jmoney");
            projectsToProcess.put("joggplayer", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/joggplayer/joggplayer-1.1.4s/src/src");
            projectsToProcess.put("jparse", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jparse/jparse-0.96/src/jparse-0.96/jparse");
            projectsToProcess.put("jpf.source", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jpf/jpf-1.5.1/src/source/org/java/plugin");
            projectsToProcess.put("jpf.source-tools", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jpf/jpf-1.5.1/src/source-tools/org/java");
            projectsToProcess.put("jpf.source-boot", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jpf/jpf-1.5.1/src/source-boot/org/java/plugin/boot");
            projectsToProcess.put("jrat", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/jrat/jrat-0.6/src/shiftone-jrat-0.6/src/org/shiftone/jrat");
            projectsToProcess.put("junit", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/junit/junit-4.11/src");
            projectsToProcess.put("jhotdraw.Source/jhotdraw7", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jhotdraw/jhotdraw-7.5.1/src/JHotDraw, 7.5.1/Source/jhotdraw7");
            projectsToProcess.put("jmeter", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jmeter/jmeter-2.9/src/apache-jmeter-2.9/src");
            projectsToProcess.put("jrefactory", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jrefactory/jrefactory-2.9.19/src/jrefactory-2.9.19-source/src");
            projectsToProcess.put("jruby", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jruby/jruby-1.5.2/src/jruby-1.5.2/src");
            projectsToProcess.put("jspwiki", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jspwiki/jspwiki-2.8.4/src/JSPWiki-2.8.4-src/src");
            projectsToProcess.put("jstock", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jstock/jstock-1.0.7c/src/jstock/src");
            projectsToProcess.put("jsXe", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jsXe/jsXe-04_beta/src/jsXe-0_4_99_0-src/src/net/sourceforge/jsxe");
            projectsToProcess.put("jtopen", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/jtopen/jtopen-7.1/src/jtopen_7_1_source");
            projectsToProcess.put("log4j", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/log4j/log4j-1.2.16/src/apache-log4j-1.2.16/src");
            projectsToProcess.put("lucene", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/lucene/lucene-4.3.0/src/lucene-4.3.0");
            projectsToProcess.put("marauroa", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/marauroa/marauroa-3.8.1/");
            projectsToProcess.put("maven.maven-artifact", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/maven/maven-3.0/src/apache-maven-3.0/maven-artifact");
            projectsToProcess.put("maven.maven-embedder", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/maven/maven-3.0/src/apache-maven-3.0/maven-embedder");
            projectsToProcess.put("mvnforum", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/mvnforum/mvnforum-1.2.2-ga/src/mvnforum-1.2.2-mvnad-1.0.1-src-20100817");
            projectsToProcess.put("myfaces_core.api", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/myfaces_core/myfaces_core-2.0.2/src/myfaces-core-2.0.2-src/src/myfaces-core-project-2.0.2/api");
            projectsToProcess.put("myfaces_core.implee6", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/myfaces_core/myfaces_core-2.0.2/src/myfaces-core-2.0.2-src/src/myfaces-core-project-2.0.2/implee6");
            projectsToProcess.put("myfaces_core.impl", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/myfaces_core/myfaces_core-2.0.2/src/myfaces-core-2.0.2-src/src/myfaces-core-project-2.0.2/impl");
            projectsToProcess.put("nakedobjects.applib", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/core/applib");
            projectsToProcess.put("nakedobjects.dnd-viewer", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/dnd-viewer");
            projectsToProcess.put("nakedobjects.dom", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/claims/dom");
            projectsToProcess.put("nakedobjects.embedded", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/headless/embedded");
            projectsToProcess.put("nakedobjects.expenses_dom", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/expenses/dom");
            projectsToProcess.put("nakedobjects.expenses_fixture", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/expenses/fixture");
            projectsToProcess.put("nakedobjects.fixture", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/claims/fixture");
            projectsToProcess.put("nakedobjects.html-viewer", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/html-viewer");
            projectsToProcess.put("nakedobjects.junit", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/headless/junit");
            projectsToProcess.put("nakedobjects.metamodel", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/core/metamodel");
            projectsToProcess.put("nakedobjects.orders", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/orders");
            projectsToProcess.put("nakedobjects.remoting", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/core/remoting");
            projectsToProcess.put("nakedobjects.runtime", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/core/runtime");
            projectsToProcess.put("nakedobjects.service", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/distribution/examples/claims/service");
            projectsToProcess.put("nakedobjects.viewer", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/headless/viewer");
            projectsToProcess.put("nakedobjects.webapp", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/core/webapp");
            projectsToProcess.put("nakedobjects.xml-persistor", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nakedobjects/nakedobjects-4.0.0/src/nakedobjects-4.0.0/plugins/xml-persistor");
            projectsToProcess.put("nekohtml", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/nekohtml/nekohtml-1.9.14/src/nekohtml-1.9.14/src");
            projectsToProcess.put("netbeans.mavenscrumtoys", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/netbeans/netbeans-6.9.1/src/netbeans-6.9.1-201007282301-src/maven.samples/samples_src/MavenScrumToys");
            projectsToProcess.put("netbeans", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/netbeans/netbeans-6.9.1/src/netbeans");
            projectsToProcess.put("openjms", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/openjms/openjms-0.7.7-beta-1/src/openjms-0.7.7-beta-1");
            projectsToProcess.put("oscache", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/oscache/oscache-2.4.1/src/oscache-2.4.1-full/src");
            projectsToProcess.put("pmd", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/pmd/pmd-4.2.5/src/pmd-4.2.5/src");
            projectsToProcess.put("poi", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/poi/poi-3.6/src/poi-3.6/src");
            projectsToProcess.put("proguard", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/proguard/proguard-4.5.1/src/proguard4.5.1/src/proguard");
            projectsToProcess.put("quartz.examples", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quartz/quartz-1.8.3/src/quartz-1.8.3/examples");
            projectsToProcess.put("quartz", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quartz/quartz-1.8.3/src/quartz-1.8.3/quartz");
            projectsToProcess.put("quartz.quartz-jboss", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quartz/quartz-1.8.3/src/quartz-1.8.3/quartz-jboss");
            projectsToProcess.put("quartz.quartz-oracle", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quartz/quartz-1.8.3/src/quartz-1.8.3/quartz-oracle");
            projectsToProcess.put("quickserver", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quickserver/quickserver-1.4.7/src/QuickServer1.4.7/src/main");
            projectsToProcess.put("quilt", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/quilt/quilt-0.6-a-5/src/quilt-0.6-a-5/src/java/org/quilt");
            projectsToProcess.put("roller", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/roller/roller-4.0.1/src/apache-roller-src-4.0.1");
            projectsToProcess.put("rssowl", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/rssowl/rssowl-2.0.5/src/rssowl-2.0.5.source/org.rssowl.core");
            projectsToProcess.put("sablecc", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/sablecc/sablecc-3.2/src/sablecc-3.2");
            projectsToProcess.put("sandmark", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/sandmark/sandmark-3.4/src/smark3");
            projectsToProcess.put("springframework.org.entityframework.expression", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/springframework/springframework-3.0.5/src/spring-framework-3.0.5.RELEASE/projects/org.springframework.expression");
            projectsToProcess.put("struts.codebehind", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/codebehind");
            projectsToProcess.put("struts.blank", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/apps/blank");
            projectsToProcess.put("struts.config-browser", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/config-browser");
            projectsToProcess.put("struts.convention", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/convention");
            projectsToProcess.put("struts.core", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/core");
            projectsToProcess.put("struts.dojo", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/dojo");
            projectsToProcess.put("struts.embeddedjsp", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/embeddedjsp");
            projectsToProcess.put("struts.jasperreports", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/jasperreports");
            projectsToProcess.put("struts.javatemplates", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/javatemplates");
            projectsToProcess.put("struts.jfreechart", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/jfreechart");
            projectsToProcess.put("struts.jsf", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/jsf");
            projectsToProcess.put("struts.json", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/json");
            projectsToProcess.put("struts.junit", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/junit");
            projectsToProcess.put("struts.mailreader", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/apps/mailreader");
            projectsToProcess.put("struts.osgi", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/osgi");
            projectsToProcess.put("struts.oval", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/oval");
            projectsToProcess.put("struts.plexus", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/plexus");
            projectsToProcess.put("struts.plugins_portlet", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/portlet");
            projectsToProcess.put("struts.portlet", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/apps/portlet");
            projectsToProcess.put("struts.rest", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/rest");
            projectsToProcess.put("struts.rest-showcase", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/apps/rest-showcase");
            projectsToProcess.put("struts.showcase", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/apps/showcase");
            projectsToProcess.put("struts.sitegraph", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/sitegraph");
            projectsToProcess.put("struts.sitemesh", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/sitemesh");
            projectsToProcess.put("struts.spring", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/spring");
            projectsToProcess.put("struts.struts1", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/struts1");
            projectsToProcess.put("struts.tiles", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/plugins/tiles");
            projectsToProcess.put("struts.xwork-core", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/struts/struts-2.2.1/src/struts-2.2.1/src/xwork-core");
            projectsToProcess.put("sunflow", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/sunflow/sunflow-0.07.2/src/sunflow");
            projectsToProcess.put("tapestry", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/tapestry/tapestry-5.1.0.5/src/tapestry-src-5.1.0.5/tapestry-core");
            projectsToProcess.put("tomcat", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/tomcat/tomcat-7.0.2/src/apache-tomcat-7.0.2-src/java");
            projectsToProcess.put("trove", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/trove/trove-2.1.0/src/trove-2.1.0");
            projectsToProcess.put("velocity", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/velocity/velocity-1.6.4/src/velocity-1.6.4/src");
            projectsToProcess.put("wct", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/wct/wct-1.5.2/src/wct-package-src-v1.5.2");
            projectsToProcess.put("webmail", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/webmail/webmail-0.7.10/src/jwebmail-0.7/src");
            projectsToProcess.put("xalan", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/xalan/xalan-2.7.1/src/xalan-j_2_7_1/src");
            projectsToProcess.put("xerces", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/xerces/xerces-2.10.0/src/xerces-2_10_0/src");
            projectsToProcess.put("xmojo", "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r-2/Systems/xmojo/xmojo-5.0.0/src/XMOJO");

            //No from Qualitas Corpus
            projectsToProcess.put("SweetHome3D-6.6.4-src", "/Users/williamcasasola/Documents/TEC/Detector/SweetHome3D-6.6.4-src");
            projectsToProcess.put("spring-petclinic", "/Users/williamcasasola/Documents/TEC/Detector/spring-petclinic");
            projectsToProcess.put("elasticsearch.lang-expression", "/Users/williamcasasola/Documents/TEC/Detector/elasticsearch/modules/lang-expression/src/main");
            projectsToProcess.put("elasticsearch.transport-netty4", "/Users/williamcasasola/Documents/TEC/Detector/elasticsearch/modules/transport-netty4/src/main");
            projectsToProcess.put("vraptor-core", "/Users/williamcasasola/Documents/TEC/Detector/SpringSamples/vraptor-master");
            //toDo:List of projects by default
        }else {
            projectsToProcess.put(inputProjectName, inputPath);//Todo capture name of the project
        }
    }


}

