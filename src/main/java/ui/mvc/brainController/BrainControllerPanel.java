package ui.mvc.brainController;

import dao.mvc.BraomRepositoryDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.brainController.OutputBrainController;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import utils.AnalyzerConstants;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrainControllerPanel extends CodeSmellGeneralPanel {
    private static JTextField nfrcField;

    private static JLabel nfrcLabel;

    public BrainControllerPanel() {
        super();
        initialize();
    }

    public void initialize() {
        nfrcField = initField(nfrcField);

        nfrcLabel = placeElementInPanel(nfrcLabel, nfrcField, "NFRC", 1);
    }

    @Override
    public void showResultsTable(int analyzerId) {
        String sql = "SELECT * FROM BRAINCONTROLLER ORDER BY classname, analyzerid, insertdate";
        ArrayList<BraomRepositoryDAO> t = new ArrayList<>();
        BraomRepositoryDAO bc = new BraomRepositoryDAO();
        switch (analyzerId) {
            case 0:
                t = bc.getBrainControllers(sql);
                break;
            case 1:
                t = bc.getBrainControllersOnlyAnalyzer();
                break;
            case 2:
                t = bc.getBrainControllersOnlySpringLint();
                break;
        }
        ui.TableModel tableModel = new BrainControllerTableModel(t);
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
        if (isAValidNumericInput(nfrcField.getText(), nfrcLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(nfrcField.getText()));

            configureJsonForAnalyzer();
            OutputBrainController obc = (OutputBrainController) callAnalyzer("BRAIN_CONTROLLER", inputParameters);
            if (obc != null && !obc.getBrainControllerResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> brainControllersFound = obc.getBrainControllerResults();
                ArrayList<BraomRepositoryDAO> controllers = new ArrayList<>();
                for (var brain : brainControllersFound) {
                    BraomRepositoryDAO brainController = new BraomRepositoryDAO(
                            brain.getName(),
                            brain.getNFRFC(),
                            AnalyzerConstants.ANALYZER,
                            brain.getPath(),//Todo: In the analyzer get the path of the class
                            brain.getPath(),
                            new java.util.Date()
                    );

                    if (saveInDB) brainController.save();
                    controllers.add(brainController);
                }
                javax.swing.table.TableModel tableModel = new BrainControllerTableModel(controllers);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + obc.toString());
        }
    }
}