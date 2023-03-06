package UI.BrainClass;

import DAO.BrainClassDAO;
import UI.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BrainClassPanel  extends CodeSmellGeneralPanel {
    //More than one Brain Method and very large
    private static JTextField veryHighLocField;

    private static JLabel veryHighLocLabel;

    //Only one Brain Method but extremely large and complex
    private static JTextField veryHigh2LocField;
    private static JTextField veryHigh2WmcField;

    private static JLabel veryHigh2LocLabel;
    private static JLabel veryHigh2WmcLabel;

    //Very complex and not cohesive
    private static JTextField veryHighWmcField;
    private static JTextField tccField;

    private static JLabel veryHighWmcLabel;
    private static JLabel tccLabel;

    public BrainClassPanel(){
        super();
        initialize();
    }

    public void initialize(){
        veryHighLocField = initField(veryHighLocField);
        veryHigh2LocField = initField(veryHigh2LocField);
        veryHigh2WmcField = initField(veryHigh2WmcField);
        veryHighWmcField = initField(veryHighWmcField);
        tccField = initField(tccField);

        placeElementInPanel(veryHighLocLabel, veryHighLocField, "LOC (Very High)", 0);
        placeElementInPanel(veryHigh2LocLabel, veryHigh2LocField, "LOC (2x Very High)", 1);
        placeElementInPanel(veryHigh2WmcLabel, veryHigh2WmcField, "WMC (2x Very High)", 2);
        placeElementInPanel(veryHighWmcLabel, veryHighWmcField, "WMC (Very High)", 3);
        placeElementInPanel(tccLabel, tccField, "TCC", 4);
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
        if(isAValidNumericInput(veryHighLocField.getText(), veryHighLocLabel.getText())
                && isAValidNumericInput(veryHigh2LocField.getText(), veryHigh2LocLabel.getText())
                && isAValidNumericInput(veryHigh2WmcField.getText(), veryHigh2WmcLabel.getText())
                && isAValidNumericInput(veryHighWmcField.getText(), veryHighWmcLabel.getText())
                && isAValidNumericInput(tccField.getText(), tccLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(veryHighLocField.getText()));
            inputParameters.add(Integer.parseInt(veryHigh2LocField.getText()));
            inputParameters.add(Integer.parseInt(veryHigh2WmcField.getText()));
            inputParameters.add(Integer.parseInt(veryHighWmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));

            configureJsonForAnalyzer();
            OutputGodClass ogc = (OutputGodClass) callAnalyzer("BRAIN_CLASS", inputParameters);
            if (ogc != null && !ogc.getGodClassesResult().isEmpty()) {
                ArrayList<ClassDataCodeSmell> brainClassesFound = ogc.getGodClassesResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<BrainClassDAO> classes = new ArrayList<>();
                for (var brain : brainClassesFound){
                    BrainClassDAO godClass = new BrainClassDAO(
                            brain.getName(),
                            brain.getLOC(),
                            brain.getWMC(),
                            brain.getTCC(),
                            1,//todo brain.getNumberBrainMethods(),
                            1,
                            brain.getPath(),//Todo: In the analyzer get the path of the class
                            brain.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) godClass.save();
                    classes.add(godClass);
                }
                TableModel tableModel = new BrainClassTableModel(classes);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
