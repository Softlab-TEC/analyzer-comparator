package ui.mvc.fatRepository;

import dao.mvc.FatRepositoryDAO;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.fatRepository.OutputFatRepository;
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

public class FatRepositoryPanel extends CodeSmellGeneralPanel {
    private static JTextField entitiesField;

    private static JLabel entitiesLabel;

    public FatRepositoryPanel(){
        super();
        initialize();
    }

    public void initialize(){
        entitiesField = initField(entitiesField);

        entitiesLabel = placeElementInPanel(entitiesLabel, entitiesField, "# Entities", 1);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM FATREPOSITORY ORDER BY classname, analyzerid, insertdate";
        ArrayList<FatRepositoryDAO> t = new ArrayList<>();
        FatRepositoryDAO fr = new FatRepositoryDAO();
        switch (analyzerId){
            case 0: t = fr.geFatRepositories(sql); break;
            case 1: t = fr.getFatRepositoriesOnlyAnalyzer(); break;
            case 2: t = fr.getFatRepositoriesOnlySpringLint(); break;
        }
        ui.TableModel tableModel = new FatRepositoryTableModel(t);
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
        if(isAValidNumericInput(entitiesField.getText(), entitiesLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(entitiesField.getText()));

            configureJsonForAnalyzer();
            OutputFatRepository ofr = (OutputFatRepository) callAnalyzer("BRAIN_CLASS", inputParameters);
            if (ofr != null && !ofr.getFatRepositoryResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> fatRepositoriesFound = ofr.getFatRepositoryResults();
                ArrayList<FatRepositoryDAO> repositories = new ArrayList<>();
                for (var fat : fatRepositoriesFound){
                    FatRepositoryDAO repository = new FatRepositoryDAO(
                            fat.getName(),
                            fat.getNumberEntities(""),
                            fat.getNumberOfBrainMethods(),
                            AnalyzerConstants.ANALYZER,
                            fat.getPath(),//Todo: In the analyzer get the path of the class
                            fat.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) repository.save();
                    repositories.add(repository);
                }
                TableModel tableModel = new FatRepositoryTableModel(repositories);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();

            }
            System.out.println("Success" + ofr.toString());
        }
    }
}
