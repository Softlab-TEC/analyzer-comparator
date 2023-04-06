package ui.identity.dataClass;

import dao.identity.DataClassDAO;
import dataAccess.PMDExecutor;
import dataAccess.PMDResultsReader;
import metrics.codesmells.dataClass.OutputDataClass;
import ui.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
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
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM DATACLASS ORDER BY classname, analyzerid, insertdate";
        ArrayList<DataClassDAO> t = new ArrayList<>();
        DataClassDAO dc = new DataClassDAO();
        switch (analyzerId){
            case 0: t = dc.getDataClasses(sql); break;
            case 1: t = dc.getDataClassesOnlyAnalyzer(); break;
            case 2: t = dc.getDataClassesOnlyPMD(); break;
        }
        TableModel tableModel = new DataClassTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
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
                ArrayList<ClassDataCodeSmell> dataClassesFound = odc.getDataClassResults();
                ArrayList<DataClassDAO> classes = new ArrayList<>();

                for (var dataC : dataClassesFound){
                    DataClassDAO dataClass = new DataClassDAO(
                            dataC.getName(),
                            dataC.getWOC(),
                            dataC.getNOPA(),
                            dataC.getNOAM(),
                            dataC.getWMC(),
                            AnalyzerConstants.ANALYZER,
                            dataC.getPath(),//Todo: In the analyzer get the path of the class
                            dataC.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) dataClass.save();
                    classes.add(dataClass);
                }
                TableModel tableModel = new DataClassTableModel(classes);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + odc.toString());
        }
    }

    @Override
    public void ReadPMFileAndSaveResults(){
        PMDResultsReader reader = new PMDResultsReader();
        try {
            reader.readDataClass(executorOutputPathName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
