package ui.dataClass;

import dao.DataClassDAO;
import dao.GodClassDAO;
import metrics.codesmells.dataClass.OutputDataClass;
import ui.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;
import ui.ComparativeTable;
import ui.TableModel;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataClassPanel extends CodeSmellGeneralPanel{
    private static JTextField wocField;
    private static JTextField fewPublicField;
    private static JTextField highWmcField;
    private static JTextField manyPublicField;
    private static JTextField veryHighWmcField;

    private static JLabel wocLabel;
    private static JLabel fewPublicLabel;
    private static JLabel highWmcLabel;
    private static JLabel manyPublicLabel;
    private static JLabel veryHighWmcLabel;

    public DataClassPanel(){
        super();
        initialize();
    }

    protected void initialize() {
        wocField = initField(wocField);
        fewPublicField = initField(fewPublicField);
        highWmcField = initField(highWmcField);
        manyPublicField = initField(manyPublicField);
        veryHighWmcField = initField(veryHighWmcField);

        placeElementInPanel(wocLabel, wocField, "WOC:", 0);
        placeElementInPanel(fewPublicLabel, fewPublicField, "Public Data (Few):", 1);
        placeElementInPanel(highWmcLabel, highWmcField, "WMC (High):", 2);
        placeElementInPanel(manyPublicLabel, manyPublicField, "Public Data (Many):", 3);
        placeElementInPanel(veryHighWmcLabel, veryHighWmcField, "WMC (Very High):", 4);
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
        if(isAValidNumericInput(wocField.getText(), wocLabel.getText())
                && isAValidNumericInput(fewPublicField.getText(), fewPublicLabel.getText())
                && isAValidNumericInput(highWmcField.getText(), highWmcLabel.getText())
                && isAValidNumericInput(manyPublicField.getText(), manyPublicLabel.getText())
                && isAValidNumericInput(veryHighWmcField.getText(), veryHighWmcLabel.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Float.parseFloat(wocField.getText()));
            inputParameters.add(Integer.parseInt(fewPublicField.getText()));
            inputParameters.add(Integer.parseInt(highWmcField.getText()));
            inputParameters.add(Integer.parseInt(manyPublicField.getText()));
            inputParameters.add(Integer.parseInt(veryHighWmcField.getText()));
            configureJsonForAnalyzer();
            OutputDataClass ogc = (OutputDataClass) callAnalyzer("DATA_CLASS", inputParameters);
            if (ogc != null && !ogc.getDataClassResults().isEmpty()) {
                ArrayList<ClassDataCodeSmell> dataClassesFound = ogc.getDataClassResults().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<DataClassDAO> classes = new ArrayList<>();

                for (var dataC : dataClassesFound){
                    DataClassDAO dataClass = new DataClassDAO(
                            dataC.getName(),
                            0,//Todo dataC.getWOC(),
                            dataC.getNOPA(),
                            dataC.getNOAM(),
                            dataC.getWMC(),
                            1,
                            dataC.getPath(),//Todo: In the analyzer get the path of the class
                            dataC.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) dataClass.save();
                    classes.add(dataClass);
                }
                TableModel tableModel = new DataClassTableModel(classes);

                ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
