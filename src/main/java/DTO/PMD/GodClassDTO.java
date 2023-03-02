package DTO.PMD;

import java.util.Date;

public class GodClassDTO extends CodeSmellDTO {
    private int wmc;
    private int atfd;
    private float tcc;

    public GodClassDTO(int id, String className, int wmc, int atfd, float tcc, int analyzerId, String projectName, String path, Date insertDate) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.wmc = wmc;
        this.atfd = atfd;
        this.tcc = tcc;
    }

    public GodClassDTO(){
        super();
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

}
