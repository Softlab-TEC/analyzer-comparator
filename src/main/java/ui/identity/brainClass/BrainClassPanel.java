package ui.identity.brainClass;

import dao.identity.BrainClassDAO;
import dao.mvc.Persistence;
import dataAccess.PMDExecutor;
import metrics.codesmells.brainClass.OutputBrainClass;
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
        resultFileName = "PMDBrainClassOutput.txt";
        metricName = Metrics.BrainClass;
        executor = new PMDExecutor();
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
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainClassDAO> t = new ArrayList<>();
        BrainClassDAO bc = new BrainClassDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainClasses(sql); break;
            case 1: t = bc.getBrainClassesOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = bc.getBrainClassesOnlyPMD(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new BrainClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
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
                ArrayList<BrainClassDAO> brainClasses = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(obc, brainClasses);
                TableModel tableModel = new BrainClassTableModel(brainClasses);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + obc.toString());
        }
    }
}
