package ui.mvc.laboriousRepositoryMethod;

import dao.mvc.LaboriousRepositoryMethodDAO;
import dao.mvc.Persistence;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.mvc.laboriousRepositoryMethod.OutputLaboriousRepositoryMethod;
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

public class LaboriousRepositoryMethodPanel extends CodeSmellGeneralPanel {
    private static JTextField persistenceActionsField;
    private static JLabel persistenceActionsLabel;

    public LaboriousRepositoryMethodPanel(){
        super();
        initialize();
        resultFileName = "SpringLintLaboriousRepositoryMethodOutput.txt";
        metricName = Metrics.LaboriousRepositoryMethod;
    }

    public void initialize(){
        persistenceActionsField = initField(persistenceActionsField);
        persistenceActionsLabel = placeElementInPanel(persistenceActionsLabel, persistenceActionsField, "Persistence", 1);
    }

    @Override
    public void showResultsTable(int analyzerId, String language, String projectName){
        String sql = "SELECT * FROM LABORIOUSREPOSITORYMETHOD ORDER BY classname, analyzerid, insertdate";
        ArrayList<LaboriousRepositoryMethodDAO> t;
        LaboriousRepositoryMethodDAO lrm = new LaboriousRepositoryMethodDAO();
        switch (analyzerId){
            case 0: t = lrm.getLaboriousRepositoryMethods(sql); break;
            default: t = lrm.getLaboriousRepoMethodOnlyAnalyzer(analyzerId, language, projectName); break;
        }
        ui.TableModel tableModel = new LaboriousRepositoryMethodTableModel(t);
        ComparativeTable ct = new ComparativeTable(tableModel, "Laborious Repository Method");
        ct.drawTable();
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        if(isAValidNumericInput(persistenceActionsField.getText(), persistenceActionsLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Integer.parseInt(persistenceActionsField.getText()));
            configureJsonForAnalyzer();
            OutputLaboriousRepositoryMethod olrm = (OutputLaboriousRepositoryMethod) callAnalyzer("LABORIOUS_REPOSITORY_METHOD", inputParameters);
            if (olrm != null && !olrm.getLabRepResults().isEmpty()) {
                ArrayList<LaboriousRepositoryMethodDAO> methods = new ArrayList<>();
                Persistence persistence = new Persistence(saveInDB, language);
                persistence.saveResults(olrm, methods);
                TableModel tableModel = new LaboriousRepositoryMethodTableModel(methods);

                ComparativeTable ct = new ComparativeTable(tableModel, "Laborious Repository Method");
                ct.drawTable();

            }
            System.out.println("Success" + olrm.toString());
        }
    }


}
