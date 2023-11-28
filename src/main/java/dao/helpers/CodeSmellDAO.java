package dao.helpers;

import dao.Connect;

import java.util.Date;

public class CodeSmellDAO {
    protected int id;
    protected String className;
    protected String  mainClassName;
    protected int analyzerId;
    protected String projectName;
    protected String path;
    protected String language;
    protected Date insertDate;
    protected Connect connection;

    public CodeSmellDAO(){}

    public CodeSmellDAO(int id,
                        String className,
                        String mainClassName,
                        int analyzerId,
                        String projectName,
                        String path,
                        String language,
                        Date insertDate) {
        this.id = id;
        this.className = className;
        this.mainClassName = mainClassName;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.language = language;
        this.insertDate = insertDate;
    }

    public CodeSmellDAO(String className,
                        String mainClassName,
                        int analyzerId,
                        String projectName,
                        String path,
                        String language,
                        Date insertDate) {
        this.className = className;
        this.mainClassName = mainClassName;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.language = language;
        this.insertDate = insertDate;
    }

    public CodeSmellDAO(String className, int analyzerId, Date insertDate){}

    public Connect getConnection() {
        return connection;
    }

    public void setConnection(Connect connection) {
        this.connection = connection;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMainClassName() { return mainClassName; }

    public void setMainClassName(String mainClassName) { this.mainClassName = mainClassName; }

    public int getAnalyzerId() {
        return analyzerId;
    }

    public void setAnalyzerId(int analyzerId) {
        this.analyzerId = analyzerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLanguage(String language) { this.language = language; }

    public String getLanguage() {return language; }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    protected String getOnlyOneAnalyzerResult(String tableName, int analyzerId, String language, String projectName,
                                              boolean avoidTestFiles){
        String avoidTestsLeft = avoidTestFiles ? " AND t1.classname NOT LIKE '%test%' " : "";
        String avoidTestsRight = avoidTestFiles ? " AND t2.classname NOT LIKE '%test%' " : "";
        String query = "SELECT t1.* FROM " + tableName + " t1 LEFT JOIN " + tableName + " t2 ON (t1.classname = t2.classname OR " +
                "t1.mainclassname = t2.mainclassname) AND t2.analyzerid <> " + analyzerId + " AND t2.projectName " +
                "= '" + projectName + "' AND t2.language = '"+ language + "'" + avoidTestsRight +
                " WHERE t1.analyzerid = " + analyzerId + " AND t2.analyzerid IS NULL AND t1.projectName = '"
                + projectName + "' AND t1.language = '" + language + "'" + avoidTestsLeft;
        return query;
    }

}
