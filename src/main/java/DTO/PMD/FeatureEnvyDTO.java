package DTO.PMD;

import java.util.Date;

public class FeatureEnvyDTO extends CodeSmellDTO{
    private int atfd;
    private float laa;
    private int fdp;
    private String methodName;

    public FeatureEnvyDTO(int id, String className, String methodName, int atfd, float laa, int fdp, int analyzerId, String projectName, String path, Date insertDate) {
        super(id, className, analyzerId, projectName, path, insertDate);
        this.atfd = atfd;
        this.laa = laa;
        this.fdp = fdp;
        this.methodName = methodName;
    }

    public FeatureEnvyDTO(){
        super();
    }

    public int getAtfd() {
        return atfd;
    }

    public void setAtfd(int atfd) {
        this.atfd = atfd;
    }

    public float getLaa() {
        return laa;
    }

    public void setLaa(float laa) {
        this.laa = laa;
    }

    public int getFdp() {
        return fdp;
    }

    public void setFdp(int fdp) {
        this.fdp = fdp;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
