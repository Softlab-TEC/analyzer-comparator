package DTO.PMD;

import java.util.Date;

public class DataClassDTO extends CodeSmellDTO{
    private float woc;
    private int nopa;
    private int noam;
    private int wmc;

    public DataClassDTO(int id,
                        String className,
                        float woc,
                        int wmc,
                        int nopa,
                        int noam,
                        int analyzerId,
                        String projectName,
                        String path,
                        Date insertDate) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.woc = woc;
        this.nopa = nopa;
        this.noam = noam;
        this.wmc = wmc;
    }

    public DataClassDTO(){
        super();
    }

    public float getWoc() {
        return woc;
    }

    public void setWoc(float woc) {
        this.woc = woc;
    }

    public int getNopa() {
        return nopa;
    }

    public void setNopa(int nopa) {
        this.nopa = nopa;
    }

    public int getNoam() {
        return noam;
    }

    public void setNoam(int noam) {
        this.noam = noam;
    }

    public int getWmc() {
        return wmc;
    }

    public void setWmc(int wmc) {
        this.wmc = wmc;
    }
}
