package ui.mvc.promiscuousController;

import dao.mvc.Persistence;
import dao.mvc.PromiscuousControllerDAO;
import dataAccess.SpringLintExecutor;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.mvc.promiscuousController.OutputPromiscuousController;
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

public class PromiscuousControllerPanel extends CodeSmellGeneralPanel {
    private static JTextField norField;
    private static JTextField nsdField;

    private static JLabel norLabel;
    private static JLabel nsdLabel;

    public PromiscuousControllerPanel() {
        super();
        initialize();
        resultFileName = "SpringLintPromiscuousControllerOutput.txt";
        metricName = Metrics.PromiscuousController;
        executor = new SpringLintExecutor();
    }

    public void initialize() {
        norField = initField(norField);
        nsdField = initField(nsdField);

        norLabel = placeElementInPanel(norLabel, norField, "NOR", 1);
        nsdLabel = placeElementInPanel(nsdLabel, nsdField, "NSD", 2);
    }

    @Override
    public void showResultsTable(int analyzerId) {
        String sql = "SELECT * FROM PROMISCUOUSCONTROLLER ORDER BY classname, analyzerid, insertdate";
        ArrayList<PromiscuousControllerDAO> t = new ArrayList<>();
        PromiscuousControllerDAO pc = new PromiscuousControllerDAO();
        switch (analyzerId) {
            case 0:
                t = pc.getPromiscuousControllers(sql);
                break;
            case 1:
                t = pc.getPromiscuousControllersOnlyAnalyzer();
                break;
            case 2:
                t = pc.getPromiscuousControllersOnlySpringLint();
                break;
        }
        ui.TableModel tableModel = new PromiscuousControllerTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel);
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if (isAValidNumericInput(norField.getText(), norLabel.getText())
                && isAValidNumericInput(nsdField.getText(), nsdLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(norField.getText()));
            inputParameters.add(Integer.parseInt(nsdField.getText()));

            configureJsonForAnalyzer();
            OutputPromiscuousController opc = (OutputPromiscuousController) callAnalyzer("PROMISCUOUS_CONTROLLER", inputParameters);
            if (opc != null && !opc.getPromiscuousControllerResults().isEmpty()) {
                ArrayList<PromiscuousControllerDAO> controllers = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(opc, controllers);
                TableModel tableModel = new PromiscuousControllerTableModel(controllers);
                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + opc.toString());
        }
    }


}
