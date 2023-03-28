package ui.mvc.meddlingService;

import dao.identity.BrainClassDAO;
import dao.mvc.MeddlingServiceDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.brainClass.OutputBrainClass;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.meddlingService.OutputMeddlingService;
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

public class MeddlingServicePanel extends CodeSmellGeneralPanel {
    private static JTextField persistenceDependenciesField;

    private static JLabel persistenceDependenciesLabel;

    public MeddlingServicePanel(){
        super();
        initialize();
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
    public void execute() {
        try {
            if(!onlyAnalyzer) {
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
        if(isAValidNumericInput(persistenceDependenciesField.getText(), persistenceDependenciesLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(persistenceDependenciesField.getText()));

            configureJsonForAnalyzer();
            OutputMeddlingService oms = (OutputMeddlingService) callAnalyzer("MEDDLING_SERVICE", inputParameters);
            if (oms != null && !oms.getMeddlingServiceResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> meddlingServicesFound = oms.getMeddlingServiceResults();
                ArrayList<MeddlingServiceDAO> services = new ArrayList<>();
                for (var service : meddlingServicesFound){
                    MeddlingServiceDAO meddlingService = new MeddlingServiceDAO(
                            service.getName(),
                            service.getNumberPersistenceDependencies(),
                            AnalyzerConstants.ANALYZER,
                            service.getPath(),//Todo: In the analyzer get the path of the class
                            service.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) meddlingService.save();
                    services.add(meddlingService);
                }
                TableModel tableModel = new MeddlingServiceTableModel(services);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + oms.toString());
        }
    }
}
