package ui.identity.featureEnvy;

import dao.identity.FeatureEnvyDAO;
import dataAccess.PMDExecutor;
import ui.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MethodDataCodeSmell;
import metrics.codesmells.featureEnvy.OutputFeatureEnvy;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.ComparativeTable;
import ui.TableModel;
import utils.AnalyzerConstants;
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
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM FEATUREENVY ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<FeatureEnvyDAO> t = new ArrayList<>();
        FeatureEnvyDAO fe = new FeatureEnvyDAO();
        switch (analyzerId){
            case 0: t = fe.getFeatureEnvies(sql); break;
            case 1: t = fe.getFeatureEnviesOnlyAnalyzer(); break;
            case 2: t = fe.getFeatureEnviesOnlyPMD(); break;
        }
        TableModel tableModel = new FeatureEnvyTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
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
                ArrayList<MethodDataCodeSmell> classesWithFeatureEnviesFound = ofe.getFeatureEnvyResults();
                ArrayList<FeatureEnvyDAO> methods = new ArrayList<>();
                for (var fe : classesWithFeatureEnviesFound){
                    FeatureEnvyDAO featureEnvy = new FeatureEnvyDAO(
                            fe.getClassName(),
                            fe.getMainClassName(),
                            fe.getName(),
                            fe.getPossibleAccessors().size(),
                            fe.getLAA(),
                            fe.getFDP(),
                            AnalyzerConstants.ANALYZER,
                            fe.getFile(),//Todo: In the analyzer get the path of the class
                            fe.getFile(),
                            new java.util.Date()
                    );
                    if(saveInDB && fe.isSmell()) featureEnvy.save();
                    methods.add(featureEnvy);
                }
                TableModel tableModel = new FeatureEnvyTableModel(methods);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + ofe.toString());
        }
    }

}
