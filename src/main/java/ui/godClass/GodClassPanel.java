package ui.godClass;

import dao.GodClassDAO;
import dataAccess.PMDExecutor;
import dataAccess.PMDResultsReader;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class GodClassPanel extends CodeSmellGeneralPanel {
    private static final String ADULT_RESPONSE = "Hello mature!";
    private static final String INVALID_AGE = "Invalid age!";

    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 100;

    private static JTextField wmcField;
    private static JTextField tccField;
    private static JTextField atfdField;

    private static JLabel wmcLabel;
    private static JLabel tccLabel;
    private static JLabel atfdLabel;

    public GodClassPanel() {
        super();
        initialize();
    }

    public static JTextField getWmcField() {
        return wmcField;
    }

    public void setWmcField(JTextField wmcField) {
        this.wmcField = wmcField;
    }

    public JTextField getTccField() {
        return tccField;
    }

    public void setTccField(JTextField tccField) {
        this.tccField = tccField;
    }

    public JTextField getAtfdField() {
        return atfdField;
    }

    public void setAtfdField(JTextField atfdField) {
        this.atfdField = atfdField;
    }

    public JLabel getWmcLabel() {
        return wmcLabel;
    }

    public void setWmcLabel(JLabel wmcLabel) {
        this.wmcLabel = wmcLabel;
    }

    public  JLabel getTccLabel() {
        return tccLabel;
    }

    public void setTccLabel(JLabel tccLabel) {
        this.tccLabel = tccLabel;
    }

    public JLabel getAtfdLabel() {
        return atfdLabel;
    }

    public void setAtfdLabel(JLabel atfdLabel) {
        this.atfdLabel = atfdLabel;
    }

    protected void initialize(){
        wmcField = initField(wmcField);
        tccField = initField(tccField);
        atfdField = initField(atfdField);

        placeElementInPanel(atfdLabel, atfdField, "ATFD:", 0);
        placeElementInPanel(wmcLabel, wmcField, "WMC:", 1);
        placeElementInPanel(tccLabel, tccField, "TCC::", 2);
    }

    private String createFolder(){
        try {
            File directory = new File("Results");
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory, "PMDoutput.txt");
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

    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM GODCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<GodClassDAO> t = new ArrayList<>();
        GodClassDAO gcd = new GodClassDAO();
        switch (analyzerId){
            case 0: t = gcd.getGodClasses(sql); break;
            case 1: t = gcd.getGodClassesOnlyAnalyzer(); break;
            case 2: t = gcd.getGodClassesOnlyPMD(); break;
        }
        TableModel tableModel = new GodClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    @Override
    public void execute() {
        //input: "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main/java/org/hibernate/cfg/HbmBinder.java"
        //Output: /Users/williamcasasola/Documents/TEC/Detector/test/
        try {
            if(!onlyAnalyzer) {
                String path = outputPath.isEmpty() ?  createFolder() : outputPath +  "PMDoutput.txt";
                PMDExecutor pmdExecutor = new PMDExecutor();
                pmdExecutor.executePMD(inputPath, path);
                if (saveInDB) ReadPMFileAndSaveResults();
            }
            initAnalyzerAndSaveResults();
        } catch (ParseException ex) {
            ex.printStackTrace();
            System.out.println("Error 1");
        } catch (UnsupportedMetricException unsupportedMetricException) {
            unsupportedMetricException.printStackTrace();
            System.out.println("Error 2");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Error 3");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Error 4");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            System.out.println("Error 5");
        } catch (UnsupportedLanguageException unsupportedLanguageException) {
            unsupportedLanguageException.printStackTrace();
            System.out.println("Error 6");
        } catch (ExecutionException executionException) {
            executionException.printStackTrace();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (TimeoutException timeoutException) {
            timeoutException.printStackTrace();
        }
        System.out.println("No errors");
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(atfdField.getText(), atfdLabel.getText())
                && isAValidNumericInput(wmcField.getText(), wmcLabel.getText())
                && isAValidNumericInput(tccField.getText(), tccLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(wmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));
            configureJsonForAnalyzer();
            OutputGodClass ogc = (OutputGodClass) callAnalyzer("GOD_CLASS", inputParameters);
            if(ogc != null && !ogc.getGodClassResults().isEmpty()) {
                ArrayList<ClassDataCodeSmell> godClassFound = ogc.getGodClassResults().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<GodClassDAO> classes = new ArrayList<>();
                for (var god : godClassFound){
                    GodClassDAO godClass = new GodClassDAO(
                            god.getName(),
                            god.getWMC(),
                            god.getPossibleAccessors(),
                            god.getTCC(),
                            1,
                            god.getPath(),//Todo: In the analyzer get the path of the class
                            god.getPath(),
                            new java.util.Date()
                    );
                    if(saveInDB) godClass.save();
                    classes.add(godClass);
                }
                TableModel tableModel = new GodClassTableModel(classes);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + ogc.toString());
        }
    }

    public void ReadPMFileAndSaveResults(){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readGodClass(outputPath.isEmpty() ? "/Users/williamcasasola/Documents/TEC/Detector/test/" : outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
