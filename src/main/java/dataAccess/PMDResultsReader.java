package dataAccess;

import dao.identity.DataClassDAO;
import dao.identity.GodClassDAO;
import utils.AnalyzerConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PMDResultsReader {
    String st;

    public void readGodClass(String sourcePath, String language) throws IOException {
        GodClassDAO godClass;
        int wmc;
        int atfd;
        float tcc;
        File file  =  new File(sourcePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        while((st = br.readLine()) != null){
            System.out.println(st);
            if(isNotAnErrorOrMessageLine()){
                var data = st.split("\\s+");
                var metrics = getMetricsArray("GodClass:");
                if(data.length > 1 && metrics.length > 1) {
                    String path = data[0];
                    String className = getClassName(path);
                    wmc = Integer.parseInt(getNumericMetric(metrics[4]));
                    atfd = Integer.parseInt(getNumericMetric(metrics[5]));
                    tcc = Float.parseFloat(getNumericMetric(metrics[6]));
                    godClass = new GodClassDAO(className, className, wmc, atfd, tcc, AnalyzerConstants.PMD, path, path, language, new java.util.Date());
                    godClass.save();
                }
            }
        }
    }

    public void readDataClass(String sourcePath, String language) throws IOException {
        DataClassDAO dataClass;
        float woc;
        int nopa;
        int noam;
        int wmc;
        File file  =  new File(sourcePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        while((st = br.readLine()) != null){
            System.out.println(st);
            if(isNotAnErrorOrMessageLine()){
                var data = st.split("\\s+");
                var metrics = getMetricsArray("DataClass:");
                if(data.length > 1 && metrics.length > 1){
                    String path = data[0];
                    String className = getClassName(path);
                    woc =  Float.parseFloat(getNumericMetric(metrics[11]));
                    nopa = Integer.parseInt(getNumericMetric(metrics[12]));
                    noam = Integer.parseInt(getNumericMetric(metrics[13]));
                    wmc =  Integer.parseInt(getNumericMetric(metrics[14]));
                    dataClass = new DataClassDAO(className, className, woc, nopa, noam, wmc, AnalyzerConstants.PMD, path, path, language, new java.util.Date());
                    dataClass.save();
                }
            }
        }
    }

    public String getClassName(String data){
        Pattern className = Pattern.compile("\\/(?:.(?!\\/))+$");
        Pattern onlyName = Pattern.compile("[\\w]+");
        Matcher matcher = className.matcher(data);
        if(matcher.find()){
            matcher = onlyName.matcher(matcher.group(0));
        }
        return matcher.find() ? matcher.group(0) : "";}

    private String getNumericMetric(String data){
        Pattern numericPattern =  Pattern.compile("[0-9]+([.][0-9]*)?|[.][0-9]+");
        Matcher matcher = numericPattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "0";
    }

    private String[] getMetricsArray(String divisorName){
        String[] metricsSection = st.split(divisorName);
        String[] metrics = metricsSection[1].split("\\s+");
        return metrics;
    }

    private boolean isNotAnErrorOrMessageLine(){
        return !st.contains("PMDException")
                && !st.contains("ParseException")
                && !st.contains("Was expecting:")
                && !st.contains("<IDENTIFIER>")
                && !st.contains("Was expecting one of:");
    }
}
