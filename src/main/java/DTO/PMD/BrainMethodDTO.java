package DTO.PMD;

import java.util.Date;

public class BrainMethodDTO extends CodeSmellDTO {
    private int loc;
    private int cyclo;
    private int maxNestig;
    private int noav;

    public BrainMethodDTO(int id,
                          String className,
                          int loc,
                          int cyclo,
                          int maxNestig,
                          int noav,
                          int analyzerId,
                          String projectName,
                          String path,
                          Date insertDate
    ){
        super(id, className, analyzerId, projectName, path, insertDate);
        this.loc = loc;
        this.cyclo = cyclo;
        this.maxNestig = maxNestig;
        this.noav = noav;
    }

    public BrainMethodDTO(){
        super();
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public int getCyclo() {
        return cyclo;
    }

    public void setCyclo(int cyclo) {
        this.cyclo = cyclo;
    }

    public int getMaxNestig() {
        return maxNestig;
    }

    public void setMaxNestig(int maxNestig) {
        this.maxNestig = maxNestig;
    }

    public int getNoav() {
        return noav;
    }

    public void setNoav(int noav) {
        this.noav = noav;
    }
}
