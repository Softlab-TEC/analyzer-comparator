package ui.mvc.brainRepository;

import dao.mvc.BrainRepositoryDAO;
import dao.mvc.Persistence;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.mvc.brainRepository.OutputBrainRepository;
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

public class BrainRepositoryPanel extends CodeSmellGeneralPanel {
    private static JTextField cycloField;
    private static JTextField sqlComplexityField;

    private static JLabel cycloLabel;
    private static JLabel sqlComplexityLabel;

    public BrainRepositoryPanel(){
        super();
        initialize();
        resultFileName = "SpringLintPBrainRepositoryOutput.txt";
        metricName = Metrics.BrainRepository;
    }

    public void initialize(){
        cycloField = initField(cycloField);
        sqlComplexityField = initField(sqlComplexityField);

        cycloLabel = placeElementInPanel(cycloLabel, cycloField, "CYCLO", 1);
        sqlComplexityLabel = placeElementInPanel(sqlComplexityLabel, sqlComplexityField, "SQL Complexity", 2);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM BRAINREPOSITORY ORDER BY classname, analyzerid, insertdate";
        ArrayList<BrainRepositoryDAO> t;
        BrainRepositoryDAO bc = new BrainRepositoryDAO();
        switch (analyzerId){
            case 0: t = bc.getBrainRepositories(sql); break;
            default: t = bc.getBrainRepositoriesOnlyAnalyzer(analyzerId, language, projectName, false); break;
        }
        ui.TableModel tableModel = new BrainRepositoryTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Brain Repository");
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults(String projectName) throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(cycloField.getText(), cycloLabel.getText())
                && isAValidNumericInput(sqlComplexityField.getText(), sqlComplexityLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(cycloField.getText()));
            inputParameters.add(Integer.parseInt(sqlComplexityField.getText()));
            configureJsonForAnalyzer();
            OutputBrainRepository ogc = (OutputBrainRepository) callAnalyzer("BRAIN_REPOSITORY", inputParameters);
            if (ogc != null && !ogc.getBrainRepositoryResults().isEmpty()) {
                ArrayList<BrainRepositoryDAO> repositories = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language, projectName);
                persistence.saveResults(ogc, repositories);
                TableModel tableModel = new BrainRepositoryTableModel(repositories);
                ComparativeTable ct = new ComparativeTable(tableModel, "Brain Repository");
                ct.drawTable();

            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
