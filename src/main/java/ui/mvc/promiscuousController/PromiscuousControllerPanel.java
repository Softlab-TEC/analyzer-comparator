package ui.mvc.promiscuousController;

import dao.identity.BrainClassDAO;
import dao.mvc.PromiscuousControllerDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.brainClass.OutputBrainClass;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.promiscuousController.OutputPromiscuousController;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import ui.identity.brainClass.BrainClassTableModel;
import utils.AnalyzerConstants;

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
                t = pc.gettPromiscuousControllersOnlyAnalyzer();
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
    public void execute() {
        try {
            if (!onlyAnalyzer) {
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
        if (isAValidNumericInput(norField.getText(), norLabel.getText())
                && isAValidNumericInput(nsdField.getText(), nsdLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(norField.getText()));
            inputParameters.add(Integer.parseInt(nsdField.getText()));

            configureJsonForAnalyzer();
            OutputPromiscuousController opc = (OutputPromiscuousController) callAnalyzer("PROMISCUOUS_CONTROLLER", inputParameters);
            if (opc != null && !opc.getPromiscuousControllerResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> promiscuousControllersFound = opc.getPromiscuousControllerResults();
                ArrayList<PromiscuousControllerDAO> controllers = new ArrayList<>();
                for (var controller : promiscuousControllersFound) {
                    PromiscuousControllerDAO godClass = new PromiscuousControllerDAO(
                            controller.getName(),
                            controller.getNumberOfRoutes(),
                            controller.getNumberOfServices(),
                            AnalyzerConstants.ANALYZER,
                            controller.getPath(),//Todo: In the analyzer get the path of the class
                            controller.getPath(),
                            new java.util.Date()
                    );

                    if (saveInDB) godClass.save();
                    controllers.add(godClass);
                }
                TableModel tableModel = new PromiscuousControllerTableModel(controllers);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + opc.toString());
        }
    }
}
