package UI;

import Configurations.Configuration;
import Configurations.InputPath;
import DAO.GodClassDAO;
import DataAccess.PMDExecutor;
import DataAccess.PMDResultsReader;
import com.google.gson.Gson;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.codesmells.godClass.detector.GodClass;
import metrics.codesmells.godClass.domain.ClassDataGodClass;
import metrics.exceptions.UnsupportedMetricException;
import metrics.run.ConfigurationReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private static JToggleButton isFolderToggle;
    private static JToggleButton isGastFileToggle;
    private static JToggleButton isFromMemoryToggle;
    private static JToggleButton useThreadsToggle;
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
        isFolderToggle = new JToggleButton("Is Folder");
        isGastFileToggle = new JToggleButton("Is GAST file");
        isFromMemoryToggle = new JToggleButton("Is from memory");
        useThreadsToggle = new JToggleButton("Use threads");


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

        constraints.gridy = 4;
        constraints.gridx = 0;
        atfdLabel = new JLabel("ATFD:");
        add(atfdLabel, constraints);
        constraints.gridx = 1;
        add(atfdField,  constraints);

        constraints.gridy = 5;
        constraints.gridx = 0;
        wmcLabel = new JLabel("WMC:");
        add(wmcLabel, constraints);
        constraints.gridx = 1;
        add(wmcField, constraints);

        constraints.gridy = 6;
        constraints.gridx = 0;
        tccLabel = new JLabel("TCC:");
        add(tccLabel, constraints);
        constraints.gridx = 1;
        add(tccField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 7;
        JButton btn = new JButton("Execute");
        btn.addActionListener(new BtnListener());
        add(btn, constraints);
    }

    private class BtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //input: "/Users/williamcasasola/Documents/TEC/Detector/Qualitas_Corpus/QualitasCorpus-20130901r/Systems/hibernate/hibernate-4.2.2/src/hibernate-release-4.2.2.Final/project/hibernate-core/src/main/java/org/hibernate/cfg/HbmBinder.java"
            //Output: /Users/williamcasasola/Documents/TEC/Detector/test/
                try {
                    PMDExecutor pmdExecutor = new PMDExecutor();
                    pmdExecutor.executePMD(inputPathField.getText(), outputPathField.getText());
                    ReadPMFileAndSaveResults();
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
        ArrayList<InputPath> ip = new ArrayList<InputPath>();
        ip.add(new InputPath(
                inputPathField.getText(),
                isFolderToggle.isSelected(),
                isGastFileToggle.isSelected(),
                isFromMemoryToggle.isSelected(),
                useThreadsToggle.isSelected())
        );

        if(isAValidNumericInput(atfdField.getText(), "ATFD")
                && isAValidNumericInput(wmcField.getText(), "WMC")
                && isAValidNumericInput(tccField.getText(), "TCC")) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(wmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));
            Configuration config = new Configuration("GOD_CLASS", outputPathField.getText(), inputParameters, ip);
            JSONParser jsonParser = new JSONParser();
            Gson gson = new Gson();
            String configString = "{\"metrics\": [" + gson.toJson(config) + "]}";
            JSONObject configJSON = (JSONObject) jsonParser.parse(configString);
            ConfigurationReader cr = new ConfigurationReader();
            OutputGodClass ogc = (OutputGodClass) cr.processJSONConfigReturningOutput(configJSON);
            if(ogc != null && !ogc.getGodClassesResult().isEmpty()) {
                ArrayList<ClassDataGodClass> godClassFound = ogc.getGodClassesResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                for (var god : godClassFound){
                    GodClassDAO godClass = new GodClassDAO(
                            god.getName(),
                            god.getWMC(),
                            god.getPossibleAccessors(),
                            god.getTCC(),
                            1,
                            god.getName(),//Todo: In the analyzer get the path of the class
                            god.getName(),
                            new java.util.Date()
                            );
                    godClass.save();
                }
            }
            System.out.println("Success" + ogc.toString());
        }
    }

    public void ReadPMFileAndSaveResults(){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readGodClass(outputPathField.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
