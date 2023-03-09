package ui.brainMethod;

import dao.BrainMethodDAO;
import dao.DataClassDAO;
import metrics.codesmells.brainMethod.OutputBrainMethod;
import metrics.codesmells.domain.MethodDataCodeSmell;
import ui.CodeSmellGeneralPanel;
import ui.ComparativeTable;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.dataClass.DataClassTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class BrainMethodPanel extends CodeSmellGeneralPanel {
    private static JTextField locField;
    private static JTextField cycloField;
    private static JTextField maxNestingField;
    private static JTextField noavField;

    private static JLabel locLabel;
    private static JLabel cycloLabel;
    private static JLabel maxNestingLabel;
    private static JLabel noavLabel;

    public BrainMethodPanel(){
        super();
        initialize();
    }

    public void initialize(){
        locField = initField(locField);
        cycloField = initField(cycloField);
        maxNestingField = initField(cycloField);
        noavField = initField(noavField);

        placeElementInPanel(locLabel, locField, "LOC", 0);
        placeElementInPanel(cycloLabel, cycloField, "Cyclo", 1);
        placeElementInPanel(maxNestingLabel, maxNestingField, "Max Nesting", 2);
        placeElementInPanel(noavLabel, noavField, "NOAV", 3);
    }

    @Override
    public void showResultsTable(int analyzerId){
        String sql = "SELECT * FROM BRAINMETHOD ORDER BY classname, methodname, analyzerid, insertdate";
        ArrayList<BrainMethodDAO> t = new ArrayList<>();
        BrainMethodDAO bm = new BrainMethodDAO();
        switch (analyzerId){
            case 0: t = bm.getBrainMethods(sql); break;
            case 1: t = bm.getBrainMethodsOnlyAnalyzer(); break;
            case 2: t = bm.getBrainMethodsOnlyPMD(); break;
        }
        ui.TableModel tableModel = new BrainMethodTableModel(t);
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
        if (isAValidNumericInput(locField.getText(), locLabel.getText())
                && isAValidNumericInput(cycloField.getText(), cycloLabel.getText())
                && isAValidNumericInput(maxNestingField.getText(), maxNestingLabel.getText())
                && isAValidNumericInput(noavField.getText(), noavLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Float.parseFloat(locField.getText()));
            inputParameters.add(Integer.parseInt(cycloField.getText()));
            inputParameters.add(Integer.parseInt(maxNestingField.getText()));
            inputParameters.add(Integer.parseInt(noavField.getText()));

            configureJsonForAnalyzer();
            OutputBrainMethod ogc = (OutputBrainMethod) callAnalyzer("BRAIN_METHOD", inputParameters);
            if (ogc != null && !ogc.getBrainMethodResults().isEmpty()) {
                ArrayList<MethodDataCodeSmell> brainMethodsFound = ogc.getBrainMethodResults().stream().map(b -> b.getMethodData()).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<BrainMethodDAO> methods = new ArrayList<>();
                for (var brain : brainMethodsFound){
                    BrainMethodDAO brainMethod = new BrainMethodDAO(
                            brain.getName(),
                            ""  ,
                            brain.getLOC(),
                            brain.getCycloMatic(),
                            brain.getMaxNesting(),
                            brain.getPossibleAccessors(false).size(),
                            1,
                            brain.getFile(),//Todo: In the analyzer get the path of the class
                            brain.getFile(),
                            new java.util.Date()
                    );

                    if(saveInDB) brainMethod.save();
                    methods.add(brainMethod);
                }
                TableModel tableModel = new BrainMethodTableModel(methods);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
