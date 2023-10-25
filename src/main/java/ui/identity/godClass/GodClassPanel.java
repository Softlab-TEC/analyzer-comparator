package ui.identity.godClass;

import dao.identity.GodClassDAO;
import dao.mvc.Persistence;
import dataAccess.PMDExecutor;
import dataAccess.PMDResultsReader;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import utils.Metrics;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GodClassPanel extends CodeSmellGeneralPanel {
    private static final String ADULT_RESPONSE = "Hello mature!";
    private static final String INVALID_AGE = "Invalid age!";

    private static final int MIN_AGE = 0;
    private static final int MAX_AGE = 100;

    private static JTextField wmcField;
    private static JTextField tccField;
    private static JTextField atfdField;

    private static JLabel wmcLabel;
    private static JLabel tccLabel;
    private static JLabel atfdLabel;

    public GodClassPanel() {
        super();
        initialize();
        resultFileName = "PMDGodClassOutput.txt";
        metricName = Metrics.GodClass;
        executor = new PMDExecutor();
    }

    public static JTextField getWmcField() {
        return wmcField;
    }

    public void setWmcField(JTextField wmcField) {
        this.wmcField = wmcField;
    }

    public JTextField getTccField() {
        return tccField;
    }

    public void setTccField(JTextField tccField) {
        this.tccField = tccField;
    }

    public JTextField getAtfdField() {
        return atfdField;
    }

    public void setAtfdField(JTextField atfdField) {
        this.atfdField = atfdField;
    }

    public JLabel getWmcLabel() {
        return wmcLabel;
    }

    public void setWmcLabel(JLabel wmcLabel) {
        this.wmcLabel = wmcLabel;
    }

    public  JLabel getTccLabel() {
        return tccLabel;
    }

    public void setTccLabel(JLabel tccLabel) {
        this.tccLabel = tccLabel;
    }

    public JLabel getAtfdLabel() {
        return atfdLabel;
    }

    public void setAtfdLabel(JLabel atfdLabel) {
        this.atfdLabel = atfdLabel;
    }

    protected void initialize(){
        wmcField = initField(wmcField);
        tccField = initField(tccField);
        atfdField = initField(atfdField);

        atfdLabel = placeElementInPanel(atfdLabel, atfdField, "ATFD:", 0);
        wmcLabel = placeElementInPanel(wmcLabel, wmcField, "WMC:", 1);
        tccLabel =placeElementInPanel(tccLabel, tccField, "TCC::", 2);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM GODCLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<GodClassDAO> t = new ArrayList<>();
        GodClassDAO gcd = new GodClassDAO();
        switch (analyzerId){
            case 0: t = gcd.getGodClasses(sql); break;
            case 1: t = gcd.getGodClassesOnlyAnalyzer(analyzerId, language, projectName); break;
            case 2: t = gcd.getGodClassesOnlyPMD(analyzerId, language, projectName); break;
        }
        TableModel tableModel = new GodClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(atfdField.getText(), atfdLabel.getText())
                && isAValidNumericInput(wmcField.getText(), wmcLabel.getText())
                && isAValidNumericInput(tccField.getText(), tccLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(atfdField.getText()));
            inputParameters.add(Float.parseFloat(wmcField.getText()));
            inputParameters.add(Float.parseFloat(tccField.getText()));
            configureJsonForAnalyzer();
            OutputGodClass ogc = (OutputGodClass) callAnalyzer("GOD_CLASS", inputParameters);
            if(ogc != null && !ogc.getGodClassResults().isEmpty()) {
                ArrayList<GodClassDAO> godClasses = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(ogc, godClasses);
                TableModel tableModel = new GodClassTableModel(godClasses);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + ogc.toString());
        }
    }

    @Override
    public void ReadPMFileAndSaveResults(){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readGodClass(executorOutputPathName, language.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
