package com.chipcollector.test.util;

import com.sun.javafx.application.PlatformImpl;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.CountDownLatch;

/**
 * Runner used to run JUnit tests on the JavaFx-Thread. Used as a parameter for the {@link RunWith} annotation.
 */
@SuppressWarnings("restriction")
public class JavaFXJunitRunner extends BlockJUnit4ClassRunner {
    /**
     * Creates a test runner, that initializes the JavaFx runtime.
     *
     * @param klass The class under test.
     * @throws InitializationError if the test class is malformed.
     */
    public JavaFXJunitRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        try {
            setupJavaFX();
        } catch (final InterruptedException e) {
            throw new InitializationError("Could not initialize the JavaFx platform.");
        }
    }

    private static void setupJavaFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        // initializes JavaFX environment
        PlatformImpl.startup(() ->
        {
      /* No need to do anything here */
        });

        latch.countDown();

        latch.await();
    }
}
