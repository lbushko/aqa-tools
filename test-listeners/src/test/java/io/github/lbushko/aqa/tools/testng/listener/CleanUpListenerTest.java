package io.github.lbushko.aqa.tools.testng.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CleanUpListenerTest {

    @CleanUp
    private final TestResource testResource = new TestResource();

    @Test
    void verifyCleanUpListenerTest() {
        testResource.createEntity();
    }

    private static class TestResource implements TestAutomationCloseable {

        private final List<Integer> createdIds = new ArrayList<>();

        private void createEntity() {
            createdIds.add(1);
        }

        @Override
        public void cleanUp() {
            log.info("Cleaning test resources...");
            createdIds.forEach(System.out::println);
            createdIds.clear();
        }
    }
}
