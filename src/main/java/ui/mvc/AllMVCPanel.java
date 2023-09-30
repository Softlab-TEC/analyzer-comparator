package ui.mvc;

import dao.mvc.Persistence;
import dataAccess.SpringLintExecutor;
import gastmappers.exceptions.UnsupportedLanguageException;
import metrics.Output;
import metrics.codesmells.mvc.brainController.OutputBrainController;
import metrics.codesmells.mvc.brainRepository.OutputBrainRepository;
import metrics.codesmells.mvc.fatRepository.OutputFatRepository;
import metrics.codesmells.mvc.laboriousRepositoryMethod.OutputLaboriousRepositoryMethod;
import metrics.codesmells.mvc.meddlingService.OutputMeddlingService;
import metrics.codesmells.mvc.promiscuousController.OutputPromiscuousController;
import metrics.exceptions.UnsupportedMetricException;
import org.antlr.v4.runtime.misc.Pair;
import org.json.simple.parser.ParseException;
import ui.CodeSmellGeneralPanel;
import utils.Metrics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllMVCPanel extends CodeSmellGeneralPanel {
    public AllMVCPanel(){
        super();
        resultFileName = "AllMVC.txt";
        metricName = Metrics.AllMVC;
        executor = new SpringLintExecutor();
    }

    @Override
    public void showResultsTable(int analyzerId){

    }

    private ArrayList<Pair<String, ArrayList<Number>>> getMVCParameters(){
        ArrayList<Pair<String, ArrayList<Number>>> allThresholds = new ArrayList<>();
        allThresholds.add(new Pair(Metrics.PROMISCUOUS_CONTROLLER, Metrics.PromiscuousControllerThresholds));
        allThresholds.add(new Pair(Metrics.BRAIN_CONTROLLER, Metrics.BrainControllerThresholds));
        allThresholds.add(new Pair(Metrics.MEDDLING_SERVICE, Metrics.MeddlingServiceThresholds));
        allThresholds.add(new Pair(Metrics.LABORIOUS_REPOSITORY_METHOD, Metrics.LaboriousRepositoryMethodThresholds));
        allThresholds.add(new Pair(Metrics.BRAIN_REPOSITORY, Metrics.BrainRepositoryThresholds));
        allThresholds.add(new Pair(Metrics.FAT_REPOSITORY, Metrics.FatRepositoryThresholds));
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
            if (output instanceof OutputPromiscuousController){
               persistence.saveResults((OutputPromiscuousController)output, new ArrayList<>());
               continue;
            }
            if (output instanceof OutputBrainController){
                persistence.saveResults((OutputBrainController)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputMeddlingService){
                persistence.saveResults((OutputMeddlingService)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputBrainRepository){
                persistence.saveResults((OutputBrainRepository)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputLaboriousRepositoryMethod){
                persistence.saveResults((OutputLaboriousRepositoryMethod)output, new ArrayList<>());
                continue;
            }
            if (output instanceof OutputFatRepository){
                persistence.saveResults((OutputFatRepository)output, new ArrayList<>());
                continue;
            }
        }
    }
}
