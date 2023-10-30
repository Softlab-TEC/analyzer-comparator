package ui.identity.dataClass;

import dao.identity.DataClassDAO;
import dao.mvc.Persistence;
import dataAccess.PMDExecutor;
import dataAccess.PMDResultsReader;
import metrics.codesmells.dataClass.OutputDataClass;
import ui.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.ComparativeTable;
import ui.TableModel;
import utils.Metrics;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataClassPanel extends CodeSmellGeneralPanel{
    private static JTextField wocField;
    private static JTextField fewPublicField;
    private static JTextField highWmcField;
    private static JTextField manyPublicField;
    private static JTextField veryHighWmcField;

    private static JLabel wocLabel;
    private static JLabel fewPublicLabel;
    private static JLabel highWmcLabel;
    private static JLabel manyPublicLabel;
    private static JLabel veryHighWmcLabel;

    public DataClassPanel(){
        super();
        initialize();
        resultFileName = "PMDDataClassOutput.txt";
        metricName = Metrics.DataClass;
        executor = new PMDExecutor();
    }

    protected void initialize() {
        wocField = initField(wocField);
        fewPublicField = initField(fewPublicField);
        highWmcField = initField(highWmcField);
        manyPublicField = initField(manyPublicField);
        veryHighWmcField = initField(veryHighWmcField);

        wocLabel = placeElementInPanel(wocLabel, wocField, "WOC:", 0);
        fewPublicLabel = placeElementInPanel(fewPublicLabel, fewPublicField, "Public Data (Few):", 1);
        highWmcLabel = placeElementInPanel(highWmcLabel, highWmcField, "WMC (High):", 2);
        manyPublicLabel = placeElementInPanel(manyPublicLabel, manyPublicField, "Public Data (Many):", 3);
        veryHighWmcLabel = placeElementInPanel(veryHighWmcLabel, veryHighWmcField, "WMC (Very High):", 4);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM DATACLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<DataClassDAO> t = new ArrayList<>();
        DataClassDAO dc = new DataClassDAO();
        switch (analyzerId){
            case 0: t = dc.getDataClasses(sql); break;
            default: t = dc.getDataClassesOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        TableModel tableModel = new DataClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Data Class");
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults(String projectName) throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(wocField.getText(), wocLabel.getText())
                && isAValidNumericInput(fewPublicField.getText(), fewPublicLabel.getText())
                && isAValidNumericInput(highWmcField.getText(), highWmcLabel.getText())
                && isAValidNumericInput(manyPublicField.getText(), manyPublicLabel.getText())
                && isAValidNumericInput(veryHighWmcField.getText(), veryHighWmcLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Float.parseFloat(wocField.getText()));
            inputParameters.add(Integer.parseInt(fewPublicField.getText()));
            inputParameters.add(Integer.parseInt(highWmcField.getText()));
            inputParameters.add(Integer.parseInt(manyPublicField.getText()));
            inputParameters.add(Integer.parseInt(veryHighWmcField.getText()));
            configureJsonForAnalyzer();
            OutputDataClass odc = (OutputDataClass) callAnalyzer("DATA_CLASS", inputParameters);
            if (odc != null && !odc.getDataClassResults().isEmpty()) {
                ArrayList<DataClassDAO> dataClasses = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language, projectName);
                persistence.saveResults(odc, dataClasses);
                TableModel tableModel = new DataClassTableModel(dataClasses);
                ComparativeTable ct = new ComparativeTable(tableModel, "Data Class");
                ct.drawTable();
            }
            System.out.println("Success" + odc.toString());
        }
    }

    @Override
    public void ReadPMFileAndSaveResults(String projectName){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readDataClass(executorOutputPathName, language.toString(), projectName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
