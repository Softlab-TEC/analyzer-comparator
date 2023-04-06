package dataAccess;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SpringLintExecutor implements IExecutor {
    @Override
    public void execute(String targetPath, String outputPath, String metric) throws IOException, ExecutionException, InterruptedException, TimeoutException {
    }
}
