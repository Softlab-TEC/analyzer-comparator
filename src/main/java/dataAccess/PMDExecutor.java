package dataAccess;

import jodd.io.StreamGobbler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class PMDExecutor implements IExecutor {

    public PMDExecutor(){}

    public void execute(String targetPath, String outputPath, String metric) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        StringBuilder command = new StringBuilder();
        command.append("alias pmd=\"$HOME/pmd-bin-7.0.0-rc4/bin/pmd\" \n");
        command.append("pmd check -d "+ targetPath + " -R category/java/design.xml/"+ metric + " > " + outputPath);

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
