package ui.identity;

import dao.mvc.Persistence;
import dataAccess.SpringLintExecutor;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.codesmells.brainClass.OutputBrainClass;
import metrics.codesmells.brainMethod.OutputBrainMethod;
import metrics.codesmells.dataClass.OutputDataClass;
import metrics.codesmells.featureEnvy.OutputFeatureEnvy;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.exceptions.UnsupportedMetricException;
import org.antlr.v4.runtime.misc.Pair;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import utils.Metrics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllIdentityPanel extends CodeSmellGeneralPanel {
    public AllIdentityPanel(){
        super();
        resultFileName = "AllIdentity.txt";
        metricName = Metrics.AllIdentity;
        executor = new SpringLintExecutor();
    }

    private ArrayList<Pair<String, ArrayList<Number>>> getMVCParameters(){
        ArrayList<Pair<String, ArrayList<Number>>> allThresholds = new ArrayList<>();
        allThresholds.add(new Pair(Metrics.GOD_CLASS, Metrics.GodClassThresholds));
        allThresholds.add(new Pair(Metrics.DATA_CLASS, Metrics.DataClassThresholds));
        allThresholds.add(new Pair(Metrics.BRAIN_CLASS, Metrics.BrainClassThresholds));
        allThresholds.add(new Pair(Metrics.BRAIN_METHOD, Metrics.BrainMethodThresholds));
        allThresholds.add(new Pair(Metrics.FEATURE_ENVY, Metrics.FeatureEnvyThresholds));
        return allThresholds;
    }

    @Override
    public void initAnalyzerAndSaveResults() throws ParseException, UnsupportedMetricException, SQLException, IOException, ClassNotFoundException, UnsupportedLanguageException {
        configureJsonForAnalyzer();
        ArrayList<Output> outputs =  callAnalyzerAll(getMVCParameters());
        if (outputs != null && !outputs.isEmpty()) {
            saveResults(outputs);
        }
        System.out.println("Success " + outputs.size());
    }

    private void saveResults(ArrayList<Output> outputs){
        Persistence persistence = new Persistence(saveInDB, language);
        for (Output output: outputs) {
            if (output instanceof OutputGodClass){
                persistence.saveResults((OutputGodClass)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputDataClass){
                persistence.saveResults((OutputDataClass)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputBrainClass){
                persistence.saveResults((OutputBrainClass)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputBrainMethod){
                persistence.saveResults((OutputBrainMethod)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputFeatureEnvy){
                persistence.saveResults((OutputFeatureEnvy)output, new ArrayList<>());
                continue;
            }
        }
    }
}
