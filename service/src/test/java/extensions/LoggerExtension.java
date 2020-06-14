package extensions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class LoggerExtension implements TestInstancePostProcessor {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Logger logger = LogManager.getLogger(testInstance.getClass());
        testInstance
                .getClass()
                .getMethod("setLogger", Logger.class)
                .invoke(testInstance, logger);
    }
}
