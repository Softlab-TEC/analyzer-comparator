package UI.FeatureEnvy;

import DAO.GodClassDAO;
import UI.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FeatureEnvyPanel extends CodeSmellGeneralPanel {
    private static JTextField atfdField;
    private static JTextField laaField;
    private static JTextField fdpField;

    private static  JLabel atfdLabel;
    private static  JLabel laaLabel;
    private static  JLabel fdpLabel;

    public FeatureEnvyPanel(){
        super();
        initialize();
    }

    protected void initialize() {
        atfdField = initField(atfdField);
        laaField = initField(laaField);
        fdpField = initField(fdpField);

        placeElementInPanel(atfdLabel, atfdField, "ATFD:", 0);
        placeElementInPanel(laaLabel, laaField, "LAA:", 1);
        placeElementInPanel(fdpLabel, fdpField, "FDP:", 2);
    }

    @Override
    public void execute() {
        try {
            if(!onlyAnalyzer) {
                JOptionPane.showMessageDialog(panel,
                        "Feature not implemented yet! Running only Analyzer",
                        "Not implemented",
                        JOptionPane.WARNING_MESSAGE);
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
        }
        System.out.println("No errors");
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(atfdField.getText(), atfdLabel.getText())
                && isAValidNumericInput(laaField.getText(), laaLabel.getText())
                && isAValidNumericInput(fdpLabel.getText(), fdpLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(laaField.getText()));
            inputParameters.add(Float.parseFloat(fdpField.getText()));
            configureJsonForAnalyzer();
            OutputGodClass ogc = (OutputGodClass) callAnalyzer("FEATURE_ENVY", inputParameters);
            if (ogc != null && !ogc.getGodClassesResult().isEmpty()) {
                ArrayList<ClassDataCodeSmell> featureEnvyFound = ogc.getGodClassesResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<GodClassDAO> classes = new ArrayList<>();
                /*
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

                UI.GodClass.ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
                */
            }
            System.out.println("Success" + ogc.toString());
        }
    }

}
