package dao.mvc;

import gastmappers.Language;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.domain.MethodDataCodeSmell;
import metrics.codesmells.mvc.brainController.OutputBrainController;
import metrics.codesmells.mvc.brainRepository.OutputBrainRepository;
import metrics.codesmells.mvc.fatRepository.OutputFatRepository;
import metrics.codesmells.mvc.laboriousRepositoryMethod.OutputLaboriousRepositoryMethod;
import metrics.codesmells.mvc.meddlingService.OutputMeddlingService;
import metrics.codesmells.mvc.promiscuousController.OutputPromiscuousController;
import utils.AnalyzerConstants;

import java.util.ArrayList;

public class Persistence {
    private boolean saveInDB;
    private  Language language;

    public Persistence(boolean saveInDB, Language language){
        this.saveInDB = saveInDB;
        this.language = language;
    }

    public void saveResults(OutputPromiscuousController opc, ArrayList<PromiscuousControllerDAO> controllers){
        ArrayList<MVCDataCodeSmell> promiscuousControllersFound = opc.getPromiscuousControllerResults();
        for (var controller : promiscuousControllersFound) {
            PromiscuousControllerDAO godClass = new PromiscuousControllerDAO(
                    controller.getName(),
                    controller.getMainClassName(),
                    controller.getNumberOfRoutes(),
                    controller.getNumberOfServices(),
                    AnalyzerConstants.ANALYZER,
                    controller.getPath(),//Todo: In the analyzer get the path of the class
                    controller.getPath(),
                    language.toString(),
                    new java.util.Date()
            );

            if (saveInDB && controller.isSmell()) godClass.save();
            controllers.add(godClass);
        }
    }


    public void saveResults(OutputMeddlingService oms, ArrayList<MeddlingServiceDAO> services) {
        ArrayList<MVCDataCodeSmell> meddlingServicesFound = oms.getMeddlingServiceResults();
        for (var service : meddlingServicesFound) {
            MeddlingServiceDAO meddlingService = new MeddlingServiceDAO(
                    service.getName(),
                    service.getMainClassName(),
                    service.getNumberPersistenceDependencies(language),
                    AnalyzerConstants.ANALYZER,
                    service.getPath(),//Todo: In the analyzer get the path of the class
                    service.getPath(),
                    language.toString(),
                    new java.util.Date()
            );

            if (saveInDB && service.isSmell()) meddlingService.save();
            services.add(meddlingService);
        }
    }

    public void saveResults(OutputLaboriousRepositoryMethod olrm, ArrayList<LaboriousRepositoryMethodDAO> methods) {
        ArrayList<MethodDataCodeSmell> laboriousRepoMethodsFound = olrm.getLabRepResults();
        for (var repository : laboriousRepoMethodsFound){
            LaboriousRepositoryMethodDAO labRepoMethod = new LaboriousRepositoryMethodDAO(
                    repository.getClassName(),
                    repository.getMainClassName(),
                    repository.getName(),
                    repository.getNumberPersistenceActions(),
                    AnalyzerConstants.ANALYZER,
                    repository.getFile(),//Todo: In the analyzer get the path of the class
                    repository.getFile(),
                    language.toString(),
                    new java.util.Date()
            );

            if(saveInDB && repository.isSmell()) labRepoMethod.save();
            methods.add(labRepoMethod);
        }
    }

    public void saveResults(OutputFatRepository ofr, ArrayList<FatRepositoryDAO> repositories) {
        ArrayList<MVCDataCodeSmell> fatRepositoriesFound = ofr.getFatRepositoryResults();
        for (var fat : fatRepositoriesFound){
            FatRepositoryDAO repository = new FatRepositoryDAO(
                    fat.getName(),
                    fat.getMainClassName(),
                    fat.getNumberEntities(),
                    AnalyzerConstants.ANALYZER,
                    fat.getPath(),//Todo: In the analyzer get the path of the class
                    fat.getPath(),
                    language.toString(),
                    new java.util.Date()
            );

            if(saveInDB && fat.isSmell()) repository.save();
            repositories.add(repository);
        }
    }

    public void saveResults(OutputBrainRepository ogc, ArrayList<BrainRepositoryDAO> repositories) {
        ArrayList<MVCDataCodeSmell> brainClassesFound = ogc.getBrainRepositoryResults();
        for (var repository : brainClassesFound){
            BrainRepositoryDAO brain = new BrainRepositoryDAO(
                    repository.getName(),
                    repository.getMainClassName(),
                    repository.getWMC(),
                    repository.getMaxSQLComplexity(),
                    AnalyzerConstants.ANALYZER,
                    repository.getPath(),//Todo: In the analyzer get the path of the class
                    repository.getPath(),
                    language.toString(),
                    new java.util.Date()
            );

            if(saveInDB && repository.isSmell()) brain.save();
            repositories.add(brain);
        }
    }

    public void saveResults(OutputBrainController opc, ArrayList<BrainControllerDAO> controllers) {
        ArrayList<MVCDataCodeSmell> brainControllersFound = opc.getBrainControllerResults();
        for (var brain : brainControllersFound) {
            BrainControllerDAO brainController = new BrainControllerDAO(
                    brain.getName(),
                    brain.getMainClassName(),
                    brain.getRFC().getAsInt(),
                    AnalyzerConstants.ANALYZER,
                    brain.getPath(),//Todo: In the analyzer get the path of the class
                    brain.getPath(),
                    language.toString(),
                    new java.util.Date()
            );

            if (saveInDB && brain.isSmell()) brainController.save();
            controllers.add(brainController);
        }
    }
}
