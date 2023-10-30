package ui.identity.brainMethod;

import dao.identity.BrainMethodDAO;
import dao.mvc.Persistence;
import dataAccess.PMDExecutor;
import metrics.codesmells.brainMethod.OutputBrainMethod;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import utils.Metrics;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrainMethodPanel extends CodeSmellGeneralPanel {
    private static JTextField locField;
    private static JTextField cycloField;
    private static JTextField maxNestingField;
    private static JTextField noavField;

    private static JLabel locLabel;
    private static JLabel cycloLabel;
    private static JLabel maxNestingLabel;
    private static JLabel noavLabel;

    public BrainMethodPanel(){
        super();
        initialize();
        resultFileName = "PMDBrainMethodOutput.txt";
        super.metricName = Metrics.BrainMethod;
        executor = new PMDExecutor();
    }

    public void initialize(){
        locField = initField(locField);
        cycloField = initField(cycloField);
        maxNestingField = initField(cycloField);
        noavField = initField(noavField);

        locLabel = placeElementInPanel(locLabel, locField, "LOC", 0);
        cycloLabel = placeElementInPanel(cycloLabel, cycloField, "Cyclo", 1);
        maxNestingLabel = placeElementInPanel(maxNestingLabel, maxNestingField, "Max Nesting", 2);
        noavLabel = placeElementInPanel(noavLabel, noavField, "NOAV", 3);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINMETHOD ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<BrainMethodDAO> t ;
        BrainMethodDAO bm = new BrainMethodDAO();
        switch (analyzerId){
            case 0: t = bm.getBrainMethods(sql); break;
            default: t = bm.getBrainMethodsOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new BrainMethodTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Method");
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults(String projectName) throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if (isAValidNumericInput(locField.getText(), locLabel.getText())
                && isAValidNumericInput(cycloField.getText(), cycloLabel.getText())
                && isAValidNumericInput(maxNestingField.getText(), maxNestingLabel.getText())
                && isAValidNumericInput(noavField.getText(), noavLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Float.parseFloat(locField.getText()));
            inputParameters.add(Integer.parseInt(cycloField.getText()));
            inputParameters.add(Integer.parseInt(maxNestingField.getText()));
            inputParameters.add(Integer.parseInt(noavField.getText()));
            configureJsonForAnalyzer();
            OutputBrainMethod obm = (OutputBrainMethod) callAnalyzer("BRAIN_METHOD", inputParameters);
            if (obm != null && !obm.getBrainMethodResults().isEmpty()) {
                ArrayList<BrainMethodDAO> brainMethods = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language, projectName);
                persistence.saveResults(obm, brainMethods);
                TableModel tableModel = new BrainMethodTableModel(brainMethods);

                ComparativeTable ct = new ComparativeTable(tableModel, "Brain Method");
                ct.drawTable();
            }
            System.out.println("Success" + obm.toString());
        }
    }
}
