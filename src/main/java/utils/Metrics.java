package utils;

import java.util.ArrayList;

public class Metrics {
    public static final String GodClass = "GodClass";
    public static final String DataClass = "DataClass";
    public static final String BrainClass = "BrainClass";
    public static final String BrainMethod = "BrainMethod";
    public static final String FeatureEnvy = "FeatureEnvy";
    public static final String PromiscuousController = "PromiscuousController";
    public static final String BrainController = "BrainController";
    public static final String MeddlingService = "MeddlingService";
    public static final String LaboriousRepositoryMethod = "LaboriousRepositoryMethod";
    public static final String BrainRepository = "BrainRepository";
    public static final String FatRepository = "FatRepository";
    public static final String AllIdentity = "ALL";
    public static final String AllMVC = "ALLMVC";

    public static final String GOD_CLASS = "GOD_CLASS";
    public static final String DATA_CLASS = "DATA_CLASS";
    public static final String BRAIN_CLASS = "BRAIN_CLASS";
    public static final String BRAIN_METHOD = "BRAIN_METHOD";
    public static final String FEATURE_ENVY = "FEATURE_ENVY";
    public static final String PROMISCUOUS_CONTROLLER = "PROMISCUOUS_CONTROLLER";
    public static final String BRAIN_CONTROLLER = "BRAIN_CONTROLLER";
    public static final String MEDDLING_SERVICE = "MEDDLING_SERVICE";
    public static final String LABORIOUS_REPOSITORY_METHOD = "LABORIOUS_REPOSITORY_METHOD";
    public static final String BRAIN_REPOSITORY = "BRAIN_REPOSITORY";
    public static final String FAT_REPOSITORY = "FAT_REPOSITORY";

    public static final ArrayList<Number> GodClassThresholds = new ArrayList<>(){
        {
            add(5);
            add(47);
            add(33);
        }
    };
    public static final ArrayList<Number> DataClassThresholds = new ArrayList<>(){
        {
            add(33);
            add(3);
            add(31);
            add(5);
            add(47);
        }
    };
    public static final ArrayList<Number> BrainClassThresholds = new ArrayList<>(){
        {
            add(175);
            add(44);
            add(50);
            add(58);
            add(4);
            add(3);
            add(7);
        }
    };
    public static final ArrayList<Number> BrainMethodThresholds = new ArrayList<>(){
        {
            add(58);
            add(4);
            add(3);
            add(7);
        }
    };
    public static final ArrayList<Number> FeatureEnvyThresholds = new ArrayList<>(){
        {
            add(2);
            add(33);
            add(2);
        }
    };
    public static final ArrayList<Number> PromiscuousControllerThresholds = new ArrayList<>(){
        {
            add(10);
            add(3);
        }
    };
    public static final ArrayList<Number> BrainControllerThresholds = new ArrayList<>(){
        {
            add(55);
        }
    };
    public static final ArrayList<Number> MeddlingServiceThresholds = new ArrayList<>(){
        {
            add(0);
        }
    };
    public static final ArrayList<Number>BrainRepositoryThresholds = new ArrayList<>(){
        {
            add(24);
            add(29);
        }
    };
    public static final ArrayList<Number> LaboriousRepositoryMethodThresholds = new ArrayList<>(){
        {
            add(1);
            add(1);
        }
    };
    public static final ArrayList<Number> FatRepositoryThresholds = new ArrayList<>(){
        {
            add(1);
        }
    };
}
