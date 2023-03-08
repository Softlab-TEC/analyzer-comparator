package ui.brainClass;

import dao.BrainClassDAO;
import metrics.codesmells.brainClass.OutputBrainClass;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BrainClassPanel  extends CodeSmellGeneralPanel {
    private static JTextField locField;
    private static JTextField wmcField;
    private static JTextField tccField;

    private static JLabel locLabel;
    private static JLabel wmcLabel;
    private static JLabel tccLabel;

    public BrainClassPanel(){
        super();
        initialize();
    }

    public void initialize(){
        locField = initField(locField);
        wmcField = initField(wmcField);
        tccField = initField(tccField);

        placeElementInPanel(locLabel, locField, "LOC", 1);
        placeElementInPanel(wmcLabel, wmcField, "WMC", 2);
        placeElementInPanel(tccLabel, tccField, "TCC", 3);
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
        if(isAValidNumericInput(locField.getText(), locLabel.getText())
                && isAValidNumericInput(wmcField.getText(), wmcLabel.getText())
                && isAValidNumericInput(tccField.getText(), tccLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(locField.getText()));
            inputParameters.add(Integer.parseInt(wmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));

            configureJsonForAnalyzer();
            OutputBrainClass ogc = (OutputBrainClass) callAnalyzer("BRAIN_CLASS", inputParameters);
            if (ogc != null && !ogc.getBrainClassResult().isEmpty()) {
                ArrayList<ClassDataCodeSmell> brainClassesFound = ogc.getBrainClassResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<BrainClassDAO> classes = new ArrayList<>();
                for (var brain : brainClassesFound){
                    BrainClassDAO godClass = new BrainClassDAO(
                            brain.getName(),
                            brain.getLOC(),
                            brain.getWMC(),
                            brain.getTCC(),
                            brain.getNumberOfBrainMethods(),
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
