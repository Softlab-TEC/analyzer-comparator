package ui.mvc.meddlingService;

import dao.mvc.MeddlingServiceDAO;
import dao.mvc.Persistence;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.mvc.meddlingService.OutputMeddlingService;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import utils.Metrics;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MeddlingServicePanel extends CodeSmellGeneralPanel {
    private static JTextField persistenceDependenciesField;
    private static JLabel persistenceDependenciesLabel;

    public MeddlingServicePanel(){
        super();
        initialize();
        resultFileName = "SpringLintMeddlingServiceOutput.txt";
        metricName = Metrics.MeddlingService;
    }

    public void initialize(){
        persistenceDependenciesField = initField(persistenceDependenciesField);
        persistenceDependenciesLabel = placeElementInPanel(persistenceDependenciesLabel, persistenceDependenciesField, "# Persistence", 1);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM MEDDLINGSERVICE ORDER BY classname, analyzerid, insertdate";
        ArrayList<MeddlingServiceDAO> t = new ArrayList<>();
        MeddlingServiceDAO ms = new MeddlingServiceDAO();
        switch (analyzerId){
            case 0: t = ms.getMeddlingServices(sql); break;
            case 1: t = ms.getMeddlingServicesOnlyAnalyzer(); break;
            case 2: t = ms.getMeddlingServiceOnlySpringLint(); break;
        }
        ui.TableModel tableModel = new MeddlingServiceTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(persistenceDependenciesField.getText(), persistenceDependenciesLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(persistenceDependenciesField.getText()));

            configureJsonForAnalyzer();
            OutputMeddlingService oms = (OutputMeddlingService) callAnalyzer("MEDDLING_SERVICE", inputParameters);
            if (oms != null && !oms.getMeddlingServiceResults().isEmpty()) {
                ArrayList<MeddlingServiceDAO> services = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(oms, services);
                TableModel tableModel = new MeddlingServiceTableModel(services);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + oms.toString());
        }
    }
}
