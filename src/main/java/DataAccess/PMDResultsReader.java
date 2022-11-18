package DataAccess;

import DAO.GodClassDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PMDResultsReader {
    File file = new File(getClass().getResource("/output.txt").getPath());
    String st;

    public void readGodClass() throws IOException {
        GodClassDAO godClass;
        int wmc;
        int atfd;
        float tcc;
        BufferedReader br = new BufferedReader(new FileReader(file));

        while((st = br.readLine()) != null){
            System.out.println(st);
            if(!st.contains("PMDException")){
                var data = st.split("\\s+");
                String path = data[0];
                String className = getClassName(path);
                wmc =  Integer.parseInt(getNumericMetric(data[5]));
                atfd = Integer.parseInt(getNumericMetric(data[6]));
                tcc =  Float.parseFloat(getNumericMetric(data[7]));
                godClass = new GodClassDAO(className, wmc, atfd, tcc, 2, path, path, new java.util.Date());
                godClass.save();
            }
        }

    }

    public String getClassName(String data){
        Pattern className = Pattern.compile("\\/(?:.(?!\\/))+$");
        Matcher matcher = className.matcher(data);
        return matcher.find() ? matcher.group(0) : "";}

    private String getNumericMetric(String data){
        Pattern numericPattern =  Pattern.compile("[0-9]+([.][0-9]*)?|[.][0-9]+");
        Matcher matcher = numericPattern.matcher(data);
        return matcher.find() ? matcher.group(0) : "0";
    }
}
