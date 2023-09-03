package Configurations;

import gastmappers.Language;

import java.util.ArrayList;

public class Configuration {
    private Language language;
    private String metricToApply;
    private String outputPath;
    private ArrayList<Number> inputParameters;
    private ArrayList<InputPath> inputPaths;
    private  Boolean returnMetrics;


    public Configuration(Language language, String metricToApply, String outputPath, ArrayList<Number> inputParameters, Boolean returnMetrics, ArrayList<InputPath> inputPaths) {
        this.language = language;
        this.metricToApply = metricToApply;
        this.outputPath = outputPath;
        this.inputParameters = inputParameters;
        this.returnMetrics = returnMetrics;
        this.inputPaths = inputPaths;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
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
