package io.github.lbushko.aqa.tools.testng.listener;

/**
 * Test resources that should be cleaned need to implement this interface TestAutomationCloseable
 */
public interface TestAutomationCloseable {
    /**
     * Test resources that should be cleaned need to implement this cleanUp method
     */
    void cleanUp();
}
