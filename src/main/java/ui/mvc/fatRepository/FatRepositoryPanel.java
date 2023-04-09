package ui.mvc.fatRepository;

import dao.mvc.FatRepositoryDAO;
import dataAccess.SpringLintExecutor;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.mvc.fatRepository.OutputFatRepository;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import utils.AnalyzerConstants;
import utils.Metrics;

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
        resultFileName = "SpringLintFatRepositoryOutput.txt";
        metricName = Metrics.FatRepository;
        executor = new SpringLintExecutor();
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
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(entitiesField.getText(), entitiesLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(entitiesField.getText()));

            configureJsonForAnalyzer();
            OutputFatRepository ofr = (OutputFatRepository) callAnalyzer("FAT_REPOSITORY", inputParameters);
            if (ofr != null && !ofr.getFatRepositoryResults().isEmpty()) {
                ArrayList<MVCDataCodeSmell> fatRepositoriesFound = ofr.getFatRepositoryResults();
                ArrayList<FatRepositoryDAO> repositories = new ArrayList<>();
                for (var fat : fatRepositoriesFound){
                    FatRepositoryDAO repository = new FatRepositoryDAO(
                            fat.getName(),
                            fat.getMainClassName(),
                            fat.getNumberOfBrainMethods(),
                            AnalyzerConstants.ANALYZER,
                            fat.getPath(),//Todo: In the analyzer get the path of the class
                            fat.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB && fat.isSmell()) repository.save();
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
