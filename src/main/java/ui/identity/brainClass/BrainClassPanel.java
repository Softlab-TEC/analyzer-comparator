package ui.identity.brainClass;

import dao.identity.BrainClassDAO;
import metrics.codesmells.brainClass.OutputBrainClass;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import utils.AnalyzerConstants;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrainClassPanel extends CodeSmellGeneralPanel {
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

        locLabel = placeElementInPanel(locLabel, locField, "LOC", 1);
        wmcLabel = placeElementInPanel(wmcLabel, wmcField, "WMC", 2);
        tccLabel = placeElementInPanel(tccLabel, tccField, "TCC", 3);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM BRAINCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainClassDAO> t = new ArrayList<>();
        BrainClassDAO bc = new BrainClassDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainClasses(sql); break;
            case 1: t = bc.getBrainClassesOnlyAnalyzer(); break;
            case 2: t = bc.getBrainClassesOnlyPMD(); break;
        }
        ui.TableModel tableModel = new BrainClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
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
            OutputBrainClass obc = (OutputBrainClass) callAnalyzer("BRAIN_CLASS", inputParameters);
            if (obc != null && !obc.getBrainClassResult().isEmpty()) {
                ArrayList<ClassDataCodeSmell> brainClassesFound = obc.getBrainClassResult();
                ArrayList<BrainClassDAO> classes = new ArrayList<>();
                for (var brain : brainClassesFound){
                    BrainClassDAO brainClass = new BrainClassDAO(
                            brain.getName(),
                            brain.getLOC(),
                            brain.getWMC(),
                            brain.getTCC(),
                            brain.getNumberOfBrainMethods(),
                            AnalyzerConstants.ANALYZER,
                            brain.getPath(),//Todo: In the analyzer get the path of the class
                            brain.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) brainClass.save();
                    classes.add(brainClass);
                }
                TableModel tableModel = new BrainClassTableModel(classes);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + obc.toString());
        }
    }
}
