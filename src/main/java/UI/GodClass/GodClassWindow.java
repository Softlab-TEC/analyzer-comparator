package UI.GodClass;

import Configurations.Configuration;
import Configurations.InputPath;
import DAO.GodClassDAO;
import DataAccess.PMDExecutor;
import DataAccess.PMDResultsReader;
import com.google.gson.Gson;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.codesmells.godClass.domain.ClassDataGodClass;
import metrics.exceptions.UnsupportedMetricException;
import metrics.run.ConfigurationReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class GodClassWindow extends JPanel {
    //private static final String YOUNG_RESPONSE = "Hi youngster!";
    private static final String ADULT_RESPONSE = "Hello mature!";
    private static final String INVALID_AGE = "Invalid age!";

    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 100;

    private static JTextField inputPathField;
    private static JTextField outputPathField;
    private static JTextField wmcField;
    private static JTextField tccField;
    private static JTextField atfdField;
    private static Checkbox isFolderToggle;
    private static Checkbox isGastFileToggle;
    private static Checkbox isFromMemoryToggle;
    private static Checkbox useThreadsToggle;
    private static Checkbox saveInDBToggle;
    private static Checkbox onlyAnalyzerToggle;
    private static Checkbox returnMetricsToggle;
    private static JLabel inputPathLabel;
    private static JLabel outputPathLabel;
    private static JLabel wmcLabel;
    private static JLabel tccLabel;
    private static JLabel atfdLabel;

    public GodClassWindow() {
        setLayout(new BorderLayout());
        setLayout(new GridBagLayout());
        initilialize();
    }

    private void initilialize(){
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        inputPathLabel = new JLabel("Input Path:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(inputPathLabel, constraints);

        outputPathLabel = new JLabel("Output Path:");
        constraints.gridy = 1;
        add(outputPathLabel, constraints);



        inputPathField = new JTextField();
        inputPathField.setPreferredSize(new Dimension(300,30));
        outputPathField = new JTextField();
        outputPathField.setPreferredSize(new Dimension(300,30));
        wmcField = new JTextField();
        wmcField.setPreferredSize(new Dimension(30, 30));
        tccField = new JTextField();
        tccField.setPreferredSize(new Dimension(30, 30));
        atfdField = new JTextField();
        atfdField.setPreferredSize(new Dimension(30, 30));
        isFolderToggle = new Checkbox("Folder");
        isGastFileToggle = new Checkbox("GAST file");
        isFromMemoryToggle = new Checkbox("Memory");
        useThreadsToggle = new Checkbox("Use threads");
        saveInDBToggle = new Checkbox("Save results in DB");
        onlyAnalyzerToggle = new Checkbox("Run only Analyzer");
        returnMetricsToggle = new Checkbox("Return metrics");

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(inputPathField, constraints);

        constraints.gridy = 1;
        add(outputPathField, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        add(isFolderToggle, constraints);
        constraints.gridx = 1;
        add(isGastFileToggle, constraints);
        constraints.gridy = 3;
        constraints.gridx = 0;
        add(useThreadsToggle, constraints);
        constraints.gridx = 1;
        add(isFromMemoryToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        add(saveInDBToggle, constraints);

        constraints.gridx = 1;
        add(onlyAnalyzerToggle, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        add(returnMetricsToggle, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        atfdLabel = new JLabel("ATFD:");
        add(atfdLabel, constraints);
        constraints.gridx = 1;
        add(atfdField,  constraints);

        constraints.gridy = 7;
        constraints.gridx = 0;
        wmcLabel = new JLabel("WMC:");
        add(wmcLabel, constraints);
        constraints.gridx = 1;
        add(wmcField, constraints);

        constraints.gridy = 8;
        constraints.gridx = 0;
        tccLabel = new JLabel("TCC:");
        add(tccLabel, constraints);
        constraints.gridx = 1;
        add(tccField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 9;
        JButton btn = new JButton("Execute");
        btn.addActionListener(new BtnListener());
        add(btn, constraints);

        JButton btnOnlyAnalyzer = new JButton("Only Analyzer");
        btnOnlyAnalyzer.addActionListener(new BtnListenerTest(1));
        constraints.gridy = 10;
        constraints.gridx = 0;
        add(btnOnlyAnalyzer, constraints);

        JButton btnOnlyPMD = new JButton("Only PMD");
        btnOnlyPMD.addActionListener(new BtnListenerTest(2));
        constraints.gridx = 1;
        add(btnOnlyPMD, constraints);

        JButton btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(new BtnListenerTest(0));
        constraints.gridx = 0;
        constraints.gridy = 11;
        add(btnShowAll, constraints);
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


    private class BtnListenerTest implements  ActionListener {

        private int analyzerId;
        public BtnListenerTest(int analyzerId){
            this.analyzerId = analyzerId;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
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
    }

    private class BtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //input: "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main/java/org/hibernate/cfg/HbmBinder.java"
            //Output: /Users/williamcasasola/Documents/TEC/Detector/test/
                try {
                    if(!onlyAnalyzerToggle.getState()) {
                        String path = outputPathField.getText().isEmpty() ?  createFolder() : outputPathField.getText() +  "PMDoutput.txt";
                        PMDExecutor pmdExecutor = new PMDExecutor();
                        pmdExecutor.executePMD(inputPathField.getText(), path);
                        if (saveInDBToggle.getState()) ReadPMFileAndSaveResults();
                    }
                    InitAnalyzerAndSaveResults();
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
        }


    private boolean isAValidNumericInput(String field, String variableName) {
        try {
            float floatValue = Float.parseFloat(field);
            return !Float.isNaN(floatValue);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(this, variableName + " is not a valid number");
            return false;
        }
    }

    public void InitAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        ArrayList<InputPath> ip = new ArrayList<>();
        ip.add(new InputPath(
                inputPathField.getText(),
                isFolderToggle.getState(),
                isGastFileToggle.getState(),
                isFromMemoryToggle.getState(),
                useThreadsToggle.getState()
                )
        );

        if(isAValidNumericInput(atfdField.getText(), "ATFD")
                && isAValidNumericInput(wmcField.getText(), "WMC")
                && isAValidNumericInput(tccField.getText(), "TCC")) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(wmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));
            Configuration config = new Configuration("GOD_CLASS", outputPathField.getText(), inputParameters, returnMetricsToggle.getState(), ip);
            JSONParser jsonParser = new JSONParser();
            Gson gson = new Gson();
            String configString = "{\"metrics\": [" + gson.toJson(config) + "]}";
            JSONObject configJSON = (JSONObject) jsonParser.parse(configString);
            ConfigurationReader cr = new ConfigurationReader();
            OutputGodClass ogc = (OutputGodClass) cr.processJSONConfigReturningOutput(configJSON);
            if(ogc != null && !ogc.getGodClassesResult().isEmpty()) {
                ArrayList<ClassDataGodClass> godClassFound = ogc.getGodClassesResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
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
                    if(saveInDBToggle.getState()) godClass.save();
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
            reader.readGodClass(outputPathField.getText().isEmpty() ? "/Users/williamcasasola/Documents/TEC/Detector/test/" : outputPathField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
