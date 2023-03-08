package dataAccess;

import jodd.io.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class PMDExecutor {

    public PMDExecutor(){}

    public void executePMD(String targetPath, String outputPath) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        StringBuilder command = new StringBuilder();
        command.append("alias pmd=\"$HOME/pmd-bin-6.48.0/bin/run.sh pmd\" \n");
        command.append("pmd -d "+ targetPath + " -R category/java/design.xml/GodClass > "+ outputPath);

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", command.toString());
        } else {
            builder.command("sh", "-c", command.toString());
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream());
        Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        future.get(10, TimeUnit.SECONDS);
    }
}
