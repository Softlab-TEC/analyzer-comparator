package Configurations;

public class InputPath {
    private String inputPath;
    private Boolean isFolder;
    private Boolean isGASTfile;
    private Boolean isFromMemory;
    private Boolean useThreads;

    public InputPath(String inputPath, Boolean isFolder, Boolean isGASTfile, Boolean isFromMemory, Boolean useThreads) {
        this.inputPath = inputPath;
        this.isFolder = isFolder;
        this.isGASTfile = isGASTfile;
        this.isFromMemory = isFromMemory;
        this.useThreads = useThreads;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Boolean isFolder) {
        this.isFolder = isFolder;
    }

    public Boolean getIsGASTfile() {
        return isGASTfile;
    }

    public void setIsGASTfile(Boolean isGASTfile) {
        this.isGASTfile = isGASTfile;
    }

    public Boolean getIsFromMemory() {
        return isFromMemory;
    }

    public void setIsFromMemory(Boolean isFromMemory) {
        this.isFromMemory = isFromMemory;
    }

    public Boolean getUseThreads() {
        return useThreads;
    }

    public void setUseThreads(Boolean useThreads) {
        this.useThreads = useThreads;
    }
}
