package DTO.PMD;

import java.util.Date;

public class GodClassDTO {
    private int id;
    private String className;
    private int wmc;
    private int atfd;
    private float tcc;
    private int analyzerId;
    private String projectName;
    private String path;
    private Date insertDate;

    public GodClassDTO(int id, String className, int wmc, int atfd, float tcc, int analyzerId, String projectName, String path, Date insertDate) {
        this.id = id;
        this.className = className;
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
        this.analyzerId = analyzerId;
        this.projectName = projectName;
        this.path = path;
        this.insertDate = insertDate;
    }

    public GodClassDTO(){}

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

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public int getAtfd() {
        return atfd;
    }

    public void setAtfd(int atfd) {
        this.atfd = atfd;
    }

    public float getTcc() { return tcc; }

    public void setTcc(float tcc) {
        this.tcc = tcc;
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
