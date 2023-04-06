package ui.mvc.laboriousRepositoryMethod;

import dao.mvc.LaboriousRepositoryMethodDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MethodDataCodeSmell;
import metrics.codesmells.mvc.laboriousRepositoryMethod.OutputLaboriousRepositoryMethod;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import utils.AnalyzerConstants;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LaboriousRepositoryMethodPanel extends CodeSmellGeneralPanel {
    private static JTextField persistenceActionsField;

    private static JLabel persistenceActionsLabel;

    public LaboriousRepositoryMethodPanel(){
        super();
        initialize();
    }

    public void initialize(){
        persistenceActionsField = initField(persistenceActionsField);

        persistenceActionsLabel = placeElementInPanel(persistenceActionsLabel, persistenceActionsField, "Persistence", 1);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM LABORIOUSREPOSITORYMETHOD ORDER BY classname, analyzerid, insertdate";
        ArrayList<LaboriousRepositoryMethodDAO> t = new ArrayList<>();
        LaboriousRepositoryMethodDAO lrm = new LaboriousRepositoryMethodDAO();
        switch (analyzerId){
            case 0: t = lrm.getLaboriousRepositoryMethods(sql); break;
            case 1: t = lrm.getLaboriousRepoMethodOnlyAnalyzer(); break;
            case 2: t = lrm.getLaboriousRepoMethodOnlySpringLint(); break;
        }
        ui.TableModel tableModel = new LaboriousRepositoryMethodTableModel(t);
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
        if(isAValidNumericInput(persistenceActionsField.getText(), persistenceActionsLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(persistenceActionsField.getText()));

            configureJsonForAnalyzer();
            OutputLaboriousRepositoryMethod olrm = (OutputLaboriousRepositoryMethod) callAnalyzer("LABORIOUS_REPOSITORY_METHOD", inputParameters);
            if (olrm != null && !olrm.getLabRepResults().isEmpty()) {
                ArrayList<MethodDataCodeSmell> laboriousRepoMethodsFound = olrm.getLabRepResults();
                ArrayList<LaboriousRepositoryMethodDAO> methods = new ArrayList<>();
                for (var repository : laboriousRepoMethodsFound){
                    LaboriousRepositoryMethodDAO labRepoMethod = new LaboriousRepositoryMethodDAO(
                            repository.getClassName(),
                            repository.getName(),
                            repository.getNumberPersistenceActions(),
                            AnalyzerConstants.ANALYZER,
                            repository.getFile(),//Todo: In the analyzer get the path of the class
                            repository.getFile(),
                            new java.util.Date()
                    );

                    if(saveInDB) labRepoMethod.save();
                    methods.add(labRepoMethod);
                }
                TableModel tableModel = new LaboriousRepositoryMethodTableModel(methods);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + olrm.toString());
        }
    }
}
