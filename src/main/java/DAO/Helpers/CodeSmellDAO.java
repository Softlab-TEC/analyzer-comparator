package DAO.Helpers;

import DAO.Connect;

import java.util.Date;

public class CodeSmellDAO {
    protected int id;
    protected String className;
    protected int analyzerId;
    protected String projectName;
    protected String path;
    protected Date insertDate;
    protected Connect connection;

    public CodeSmellDAO(){}

    public CodeSmellDAO(int id,
                        String className,
                        int analyzerId,
                        String projectName,
                        String path,
                        Date insertDate) {
        this.id = id;
        this.className = className;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.insertDate = insertDate;
    }

    public CodeSmellDAO(String className,
                        int analyzerId,
                        String projectName,
                        String path,
                        Date insertDate) {
        this.className = className;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
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

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

}
