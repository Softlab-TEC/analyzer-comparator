package UI.DataClass;

import DAO.GodClassDAO;
import UI.CodeSmellGeneralPanel;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.json.simple.parser.ParseException;

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
                && isAValidNumericInput(highWmcField.getText(), highWmcField.getText())
                && isAValidNumericInput(manyPublicLabel.getText(), manyPublicField.getText())
                && isAValidNumericInput(veryHighWmcField.getText(), veryHighWmcField.getText())) {
            ArrayList<Number> inputParameters = new ArrayList<>();
            inputParameters.add(Float.parseFloat(wocField.getText()));
            inputParameters.add(Integer.parseInt(fewPublicField.getText()));
            inputParameters.add(Integer.parseInt(highWmcField.getText()));
            inputParameters.add(Integer.parseInt(manyPublicField.getText()));
            inputParameters.add(Integer.parseInt(veryHighWmcLabel.getText()));
            configureJsonForAnalyzer();
            OutputGodClass ogc = (OutputGodClass) callAnalyzer("DATA_CLASS", inputParameters);
            if (ogc != null && !ogc.getGodClassesResult().isEmpty()) {
                ArrayList<ClassDataCodeSmell> featureEnvyFound = ogc.getGodClassesResult().stream().map(g -> g.getClassData().second).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<GodClassDAO> classes = new ArrayList<>();
                /*
                for (var god : godClassFound){
                    GodClassDAO godClass = new GodClassDAO(
                            god.getName(),
                            god.getWMC(),
                            god.getPossibleAccessors(),
                            god.getTCC(),
                            1,
                            god.getPath(),//Todo: In the analyzer get the path of the class
                            god.getPath(),
                            new java.util.Date()
                    );

                    if(saveInDB) godClass.save();
                    classes.add(godClass);
                }
                TableModel tableModel = new GodClassTableModel(classes);

                UI.GodClass.ComparativeTable ct = new ComparativeTable(tableModel);
                ct.drawTable();
                */
            }
            System.out.println("Success" + ogc.toString());
        }
    }
}
