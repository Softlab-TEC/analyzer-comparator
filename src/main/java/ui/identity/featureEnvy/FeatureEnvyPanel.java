package ui.identity.featureEnvy;

import dao.identity.FeatureEnvyDAO;
import dao.mvc.Persistence;
import dataAccess.PMDExecutor;
import ui.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.featureEnvy.OutputFeatureEnvy;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.ComparativeTable;
import ui.TableModel;
import utils.Metrics;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
        resultFileName = "PMDFeatureEnvyOutput.txt";
        metricName = Metrics.FeatureEnvy;
        executor = new PMDExecutor();
    }

    protected void initialize() {
        atfdField = initField(atfdField);
        laaField = initField(laaField);
        fdpField = initField(fdpField);

        atfdLabel = placeElementInPanel(atfdLabel, atfdField, "ATFD:", 0);
        laaLabel = placeElementInPanel(laaLabel, laaField, "LAA:", 1);
        fdpLabel = placeElementInPanel(fdpLabel, fdpField, "FDP:", 2);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM FEATUREENVY ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<FeatureEnvyDAO> t;
        FeatureEnvyDAO fe = new FeatureEnvyDAO();
        switch (analyzerId){
            case 0: t = fe.getFeatureEnvies(sql); break;
            default: t = fe.getFeatureEnviesOnlyAnalyzer(analyzerId, language, projectName, false); break;
        }
        TableModel tableModel = new FeatureEnvyTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Feature Envy");
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(atfdField.getText(), atfdLabel.getText())
                && isAValidNumericInput(laaField.getText(), laaLabel.getText())
                && isAValidNumericInput(fdpField.getText(), fdpLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(laaField.getText()));
            inputParameters.add(Float.parseFloat(fdpField.getText()));
            configureJsonForAnalyzer();
            OutputFeatureEnvy ofe = (OutputFeatureEnvy) callAnalyzer("FEATURE_ENVY", inputParameters);
            if (ofe != null && !ofe.getFeatureEnvyResults().isEmpty()) {
                ArrayList<FeatureEnvyDAO> envyMethods = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(ofe, envyMethods);
                TableModel tableModel = new FeatureEnvyTableModel(envyMethods);
                ComparativeTable ct = new ComparativeTable(tableModel, "Feature Envy");
                ct.drawTable();
            }
            System.out.println("Success" + ofe.toString());
        }
    }

}
