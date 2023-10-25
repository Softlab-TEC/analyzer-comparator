package utils;

public class AnalyzerConstants {
    public static final int ANALYZER = 1;
    public static final int PMD = 2;
    public static final int SPRINGLINT = 3;
    public static final int JSPIRIT = 4;

    public static String getAnalyzerName(int id){
        switch (id){
            case 1: return "ANALYZER";
            case 2: return "PMD";
            case 3: return "SPRINGLINT";
            case 4: return "JSPIRIT";
        }
        return "";
    }
}
