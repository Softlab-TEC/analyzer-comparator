package dataAccess;
import dao.mvc.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.AnalyzerConstants;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpringLintResultsReader {
    String language;

    public static String readHTMLFile(File filePath) {
        StringBuilder content = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void readResult(String inputPath, String language) throws IOException {
        this.language = language;
        int numberOfFilesProcessed = 0;
        File directory = new File(inputPath);
        if (directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".html")) {
                        numberOfFilesProcessed ++;
                        String htmlContent = readHTMLFile(file);
                        Document document = Jsoup.parse(htmlContent);

                        // Find all script elements in the HTML document
                        Elements eles = document.select("script");
                        for (Element script : eles) {
                            // Extract the JavaScript code
                            String javascriptCode = script.data();

                            // Check if the code contains the variable "smells"
                            if (javascriptCode.contains("var smells")) {
                                // Parse the JSON data stored in "smells"
                                int startIndex = javascriptCode.indexOf("var smells");
                                int endIndex = javascriptCode.indexOf(";", startIndex);
                                String jsonVariable = javascriptCode.substring(startIndex, endIndex);

                                // Extract the JSON data and do further processing
                                String jsonString = jsonVariable.split("=")[1].trim();
                                JSONArray array = new JSONArray(jsonString);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    JSONArray smells = object.getJSONArray("smells");
                                    for (var smell : smells) {
                                        handleSmells((JSONObject) smell, object);
                                    }
                                }
                                System.out.println("Successfully file: " + file.getName() + " processed. File processed number: " + numberOfFilesProcessed );
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleSmells(JSONObject smell, JSONObject metadata){
        switch ((String) smell.get("name")) {
            case "Promiscuous Controller":
                savePromiscuousController(smell, metadata);
                break;
            case "Smart Controller":
                saveBrainController(smell, metadata);
                break;
            case "Meddling service":
                saveMeddlingService(smell, metadata);
                break;
            case "Smart Repository":
                saveBrainRepository(smell, metadata);
                break;
            case "Laborious Repository Method":
                saveLaboriousRepositoryMethod(smell, metadata);
                break;
            case "Fat Repository":
                saveFatRepository(smell, metadata);
                break;
            default:
                break;
        }
    }

    private void savePromiscuousController(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((String)smell.get("description"));
        int nor = -1;
        if(matcher.find()){
            nor = Integer.parseInt(matcher.group());
        }
        int nsd = -1;
        if(matcher.find()){
            nsd = Integer.parseInt(matcher.group());
        }
        PromiscuousControllerDAO promiscuousController = new PromiscuousControllerDAO(className, className, nor, nsd, AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        promiscuousController.save();
    }

    private void saveBrainController(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        int nfrc = Integer.parseInt(((String)smell.get("description")).split("=")[1]);
        BrainControllerDAO brainController = new BrainControllerDAO(className, className, nfrc, AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        brainController.save();
    }

    private void saveMeddlingService(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        int persistenceDependencies = -1;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((String)smell.get("description"));
        if(matcher.find()){
            persistenceDependencies = Integer.parseInt(matcher.group());
        }
        MeddlingServiceDAO meddlingService = new MeddlingServiceDAO(className, className, persistenceDependencies, AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        meddlingService.save();
    }

    private void saveBrainRepository(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((String)smell.get("description"));
        int sqlComplexity = -1;
        if(matcher.find()){
            sqlComplexity = Integer.parseInt(matcher.group());
        }
        int cyclo = -1;
        if(matcher.find()){
            cyclo = Integer.parseInt(matcher.group());
        }
        BrainRepositoryDAO brainRepository = new BrainRepositoryDAO(className, className, cyclo, sqlComplexity ,AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        brainRepository.save();
    }

    private void saveLaboriousRepositoryMethod(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String methodName =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((String)smell.get("description"));
        int quantityPersistenceActions = -1;
        if(matcher.find()){
            quantityPersistenceActions = Integer.parseInt(matcher.group());
        }
        LaboriousRepositoryMethodDAO laboriousRepositoryMethod = new LaboriousRepositoryMethodDAO(className, className, methodName, quantityPersistenceActions,AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        laboriousRepositoryMethod.save();
    }

    private void saveFatRepository(JSONObject smell, JSONObject metadata){
        String projectName = (String) metadata.get("file");
        String className =  (String) metadata.get("name");
        String path = (String) metadata.get("file");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher((String)smell.get("description"));
        int entities = -1;
        if(matcher.find()){
            entities = Integer.parseInt(matcher.group());
        }
        FatRepositoryDAO fatRepository = new FatRepositoryDAO(className, className, entities, AnalyzerConstants.SPRINGLINT, projectName, path, language, new java.util.Date());
        fatRepository.save();
    }
}
