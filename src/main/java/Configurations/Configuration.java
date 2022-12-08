package Configurations;

import java.util.ArrayList;

public class Configuration {
    private String language;
    private String metricToApply;
    private String outputPath;
    private ArrayList<Number> inputParameters;
    private ArrayList<InputPath> inputPaths;

    public Configuration(String language, String metricToApply, String outputPath, ArrayList<Number> inputParameters, ArrayList<InputPath> inputPaths) {
        this.language = language;
        this.metricToApply = metricToApply;
        this.outputPath = outputPath;
        this.inputParameters = inputParameters;
        this.inputPaths = inputPaths;
    }

    public Configuration(String metricToApply, String outputPath, ArrayList<Number> inputParameters, ArrayList<InputPath> inputPaths){
        language = "JAVA";
        this.metricToApply = metricToApply;
        this.outputPath = outputPath;
        this.inputParameters = inputParameters;
        this.inputPaths = inputPaths;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMetricToApply() {
        return metricToApply;
    }

    public void setMetricToApply(String metricToApply) {
        this.metricToApply = metricToApply;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public ArrayList<Number> getInputParameters() {
        return inputParameters;
    }

    public void setInputParameters(ArrayList<Number> inputParameters) {
        this.inputParameters = inputParameters;
    }

}
