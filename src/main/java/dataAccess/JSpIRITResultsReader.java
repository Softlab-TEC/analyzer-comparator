package dataAccess;

import dao.identity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import utils.AnalyzerConstants;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSpIRITResultsReader {
    String language;
    public void readResult(String inputPath, String language) throws IOException {
        this.language = language;
        int numberOfFilesProcessed = 0;
        File directory = new File(inputPath);

        if (directory.isDirectory()) {
            // List all files in the directory
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".csv")) {
                        numberOfFilesProcessed ++;
                        try {
                            FileReader fileReader = new FileReader(file);
                            CSVParser csvParser = CSVFormat.EXCEL.withDelimiter(';').withHeader().parse(fileReader);
                            // Access rows based on column name
                            for (CSVRecord record : csvParser) {
                                switch (record.get("Smell").trim()) {
                                    case "God Class":
                                        saveGodClass(record);
                                        break;
                                    case "Data Class":
                                        saveDataClass(record);
                                        break;
                                    case "Brain Class":
                                        saveBrainClass(record);
                                        break;
                                    case "Brain Method":
                                        saveBrainMethod(record);
                                        break;
                                    case "Feature Envy":
                                        saveFeatureEnvy(record);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            System.out.println("Successfully file: " + file.getName() + " processed. File processed number: " + numberOfFilesProcessed );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
        private void saveGodClass(CSVRecord smellLine){
        String projectName = smellLine.get("Project").trim();
        String className =  smellLine.get("Location").trim();
        String path =  smellLine.get("Path").trim();
        GodClassDAO godClass = new GodClassDAO(className, className, AnalyzerConstants.JSPIRIT, projectName, path, language, new java.util.Date());
        godClass.save();
    }

    private void saveDataClass(CSVRecord smellLine){
        String projectName = smellLine.get("Project").trim();
        String className =  smellLine.get("Location").trim();
        String path =  smellLine.get("Path").trim();
        DataClassDAO dataClass = new DataClassDAO(className, className, AnalyzerConstants.JSPIRIT, projectName, path, language, new java.util.Date());
        dataClass.save();
    }

    private void saveBrainClass(CSVRecord smellLine){
        String projectName = smellLine.get("Project").trim();
        String className =  smellLine.get("Location").trim();
        String path =  smellLine.get("Path").trim();
        BrainClassDAO brainClass = new BrainClassDAO(className, className, AnalyzerConstants.JSPIRIT, projectName, path, language, new java.util.Date());
        brainClass.save();
    }

    private void saveBrainMethod(CSVRecord smellLine){
        String projectName = smellLine.get("Project").trim();
        String[] location =  smellLine.get("Location").split("\\.");
        String className = location[0].trim();
        String methodName = location[1].trim();
        String path =  smellLine.get("Path").trim();
        BrainMethodDAO brainMethod = new BrainMethodDAO(className, className, methodName ,AnalyzerConstants.JSPIRIT, projectName, path, language, new java.util.Date());
        brainMethod.save();
    }

    private void saveFeatureEnvy(CSVRecord smellLine){
        String projectName = smellLine.get("Project").trim();
        String[] location =  smellLine.get("Location").split("\\.");
        String className = location[0].trim();
        String methodName = location[1].trim();
        String path =  smellLine.get("Path").trim();
        FeatureEnvyDAO featureEnvy = new FeatureEnvyDAO(className, className, methodName, AnalyzerConstants.JSPIRIT, projectName, path, language, new java.util.Date());
        featureEnvy.save();
    }
}
