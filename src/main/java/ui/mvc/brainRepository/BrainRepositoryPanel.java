package ui.mvc.brainRepository;

import dao.mvc.BrainRepositoryDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.brainRepository.OutputBrainRepository;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import ui.mvc.brainController.BrainControllerTableModel;
import utils.AnalyzerConstants;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BrainRepositoryPanel extends CodeSmellGeneralPanel {
    private static JTextField cycloField;
    private static JTextField sqlComplexityField;

    private static JLabel cycloLabel;
    private static JLabel sqlComplexityLabel;

    public BrainRepositoryPanel(){
        super();
        initialize();
    }

    public void initialize(){
        cycloField = initField(cycloField);
        sqlComplexityField = initField(sqlComplexityField);

        cycloLabel = placeElementInPanel(cycloLabel, cycloField, "CYCLO", 1);
        sqlComplexityLabel = placeElementInPanel(sqlComplexityLabel, sqlComplexityField, "SQL Complexity", 2);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM BRAINREPOSITORY ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainRepositoryDAO> t = new ArrayList<>();
        BrainRepositoryDAO bc = new BrainRepositoryDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainRepositories(sql); break;
            case 1: t = bc.getBrainRepositoriesOnlyAnalyzer(); break;
            case 2: t = bc.getBrainRepositoriesOnlySpringLint(); break;
        }
        ui.TableModel tableModel = new BrainRepositoryTableModel(t);
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
        if(isAValidNumericInput(cycloField.getText(), cycloLabel.getText())
                && isAValidNumericInput(sqlComplexityField.getText(), sqlComplexityLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(cycloField.getText()));
            inputParameters.add(Integer.parseInt(sqlComplexityField.getText()));
            configureJsonForAnalyzer();
            OutputBrainRepository ogc = (OutputBrainRepository) callAnalyzer("BRAIN_REPOSITORY", inputParameters);
            if (ogc != null && !ogc.getBrainRepositoryResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> brainClassesFound = ogc.getBrainRepositoryResults();
                ArrayList<BrainRepositoryDAO> repositories = new ArrayList<>();
                for (var repository : brainClassesFound){
                    BrainRepositoryDAO brain = new BrainRepositoryDAO(
                            repository.getName(),
                            repository.getWMC(),
                            repository.getMaxSQLComplexity(),
                            AnalyzerConstants.ANALYZER,
                            repository.getPath(),//Todo: In the analyzer get the path of the class
                            repository.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) brain.save();
                    repositories.add(brain);
                }
                TableModel tableModel = new BrainRepositoryTableModel(repositories);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
