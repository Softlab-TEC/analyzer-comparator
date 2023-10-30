package dao.mvc;

import dao.helpers.CodeSmellDAO;
import dao.helpers.GetCodeSmellHelper;
import dao.identity.*;
import gastmappers.Language;
import metrics.codesmells.brainClass.OutputBrainClass;
import metrics.codesmells.brainMethod.OutputBrainMethod;
import metrics.codesmells.dataClass.OutputDataClass;
import metrics.codesmells.domain.ClassDataCodeSmell;
import metrics.codesmells.domain.MVCDataCodeSmell;
import metrics.codesmells.domain.MethodDataCodeSmell;
import metrics.codesmells.featureEnvy.OutputFeatureEnvy;
import metrics.codesmells.godClass.OutputGodClass;
import metrics.codesmells.mvc.brainController.OutputBrainController;
import metrics.codesmells.mvc.brainRepository.OutputBrainRepository;
import metrics.codesmells.mvc.fatRepository.OutputFatRepository;
import metrics.codesmells.mvc.laboriousRepositoryMethod.OutputLaboriousRepositoryMethod;
import metrics.codesmells.mvc.meddlingService.OutputMeddlingService;
import metrics.codesmells.mvc.promiscuousController.OutputPromiscuousController;
import utils.AnalyzerConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Persistence extends CodeSmellDAO {
    private boolean saveInDB;
    private  Language language;
    private String projectName;

    public Persistence(boolean saveInDB, Language language, String projectName){
        this.saveInDB = saveInDB;
        this.language = language;
        this.projectName = projectName;
    }

    public void saveResults(OutputGodClass ogc, ArrayList<GodClassDAO> godClasses) {
        ArrayList<ClassDataCodeSmell> godClassesFound = ogc.getGodClassResults();
        for (var god : godClassesFound){
            GodClassDAO godClass = new GodClassDAO(
                god.getName(),
                god.getMainClassName(),
                god.getWMC(),
                god.calculateATFD(),
                god.getTCC(),
                AnalyzerConstants.ANALYZER,
                !projectName.isEmpty() ? projectName : god.getPath(),//Todo: In the analyzer get the path of the class
                god.getPath(),
                language.toString(),
                new java.util.Date()
            );
            if(saveInDB && god.isSmell()) godClass.save();
            godClasses.add(godClass);
        }
    }

    public void saveResults(OutputDataClass odc, ArrayList<DataClassDAO> dataClasses) {
        ArrayList<ClassDataCodeSmell> dataClassesFound = odc.getDataClassResults();
        for (var dataC : dataClassesFound){
            DataClassDAO dataClass = new DataClassDAO(
                dataC.getName(),
                dataC.getMainClassName(),
                dataC.getWOC(),
                dataC.getNOPA(),
                dataC.getNOAM(),
                dataC.getWMC(),
                AnalyzerConstants.ANALYZER,
                    !projectName.isEmpty() ? projectName : dataC.getPath(),//Todo: In the analyzer get the path of the class
                dataC.getPath(),
                language.toString(),
                new java.util.Date()
            );
            if(saveInDB && dataC.isSmell()) dataClass.save();
            dataClasses.add(dataClass);
        }
    }

    public void saveResults(OutputBrainClass obc, ArrayList<BrainClassDAO> brainClasses) {
        ArrayList<ClassDataCodeSmell> brainClassesFound = obc.getBrainClassResult();
        for (var brain : brainClassesFound){
            BrainClassDAO brainClass = new BrainClassDAO(
                brain.getName(),
                brain.getMainClassName(),
                brain.getLOC(),
                brain.getWMC(),
                brain.getTCC(),
                brain.getNumberOfBrainMethods(),
                AnalyzerConstants.ANALYZER,
                    !projectName.isEmpty() ? projectName : brain.getPath(),//Todo: In the analyzer get the path of the class
                brain.getPath(),
                language.toString(),
                new java.util.Date()
            );
            if(saveInDB  && brain.isSmell()) brainClass.save();
            brainClasses.add(brainClass);
        }
    }

    public void saveResults(OutputBrainMethod obm, ArrayList<BrainMethodDAO> brainMethods) {
        ArrayList<MethodDataCodeSmell> brainMethodsFound = obm.getBrainMethodResults();
        for (var brain : brainMethodsFound){
            BrainMethodDAO brainMethod = new BrainMethodDAO(
                brain.getClassName(),
                brain.getMainClassName(),
                brain.getName() ,
                brain.getLOC(),
                brain.getCycloMatic(),
                brain.getMaxNesting(),
                brain.calculatePossibleAccessors().size(),
                AnalyzerConstants.ANALYZER,
                    !projectName.isEmpty() ? projectName : brain.getFile(),//Todo: In the analyzer get the path of the class
                brain.getFile(),
                language.toString(),
                new java.util.Date()
            );

            if(saveInDB && brain.isSmell()) brainMethod.save();
            brainMethods.add(brainMethod);
        }
    }

    public void saveResults(OutputFeatureEnvy ofe, ArrayList<FeatureEnvyDAO> envyMethods) {
        ArrayList<MethodDataCodeSmell> classesWithFeatureEnviesFound = ofe.getFeatureEnvyResults();
        for (var fe : classesWithFeatureEnviesFound){
            FeatureEnvyDAO featureEnvy = new FeatureEnvyDAO(
                fe.getClassName(),
                fe.getMainClassName(),
                fe.getName(),
                fe.calculatePossibleAccessors().size(),
                fe.calculateLAA(),
                fe.getFDP(),
                AnalyzerConstants.ANALYZER,
                    !projectName.isEmpty() ? projectName :fe.getFile(),//Todo: In the analyzer get the path of the class
                fe.getFile(),
                language.toString(),
                new java.util.Date()
            );
            if(saveInDB && fe.isSmell()) featureEnvy.save();
            envyMethods.add(featureEnvy);
        }
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
                    !projectName.isEmpty() ? projectName : controller.getPath(),//Todo: In the analyzer get the path of the class
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
                    !projectName.isEmpty() ? projectName : service.getPath(),//Todo: In the analyzer get the path of the class
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
                    !projectName.isEmpty() ? projectName : repository.getFile(),//Todo: In the analyzer get the path of the class
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
                    !projectName.isEmpty() ? projectName : fat.getPath(),//Todo: In the analyzer get the path of the class
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
                    !projectName.isEmpty() ? projectName : repository.getPath(),//Todo: In the analyzer get the path of the class
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
                    !projectName.isEmpty() ? projectName : brain.getPath(),//Todo: In the analyzer get the path of the class
                brain.getPath(),
                language.toString(),
                new java.util.Date()
            );
            if (saveInDB && brain.isSmell()) brainController.save();
            controllers.add(brainController);
        }
    }

    public ArrayList<String> getProjectsRan(){
        String query = "SELECT DISTINCT projectname FROM GODCLASS g WHERE analyzerid = 1" +
                " UNION SELECT DISTINCT projectname FROM DATACLASS d WHERE d.analyzerid = 1" +
                " UNION SELECT DISTINCT projectname FROM FEATUREENVY f WHERE f.analyzerid = 1" +
                " UNION SELECT DISTINCT projectname  FROM BRAINCLASS bc WHERE bc.analyzerid = 1 " +
                " UNION SELECT DISTINCT projectname  FROM BRAINMETHOD bm WHERE bm.analyzerid = 1";
        ArrayList<String> projects = new ArrayList<>();
        try (Connection conn = connection.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            GetCodeSmellHelper.preparedStatementByBasicProps(this, pstmt);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                projects.add(rs.getString("projectname"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return projects;
    }
}
