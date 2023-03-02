package DTO.PMD;

import java.util.Date;

public class BrainClassDTO extends CodeSmellDTO{
    private int loc;
    private int wmc;
    private float tcc;

    public BrainClassDTO(int id, String className, int loc, int wmc, float tcc, int analyzerId, String projectName, String path, Date insertDate) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.wmc = wmc;
        this.tcc = tcc;
    }

    public BrainClassDTO(){
        super();
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }

    public float getTcc() {
        return tcc;
    }

    public void setTcc(float tcc) {
        this.tcc = tcc;
    }
}
