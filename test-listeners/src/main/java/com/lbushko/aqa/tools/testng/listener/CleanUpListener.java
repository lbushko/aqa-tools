package com.lbushko.aqa.tools.testng.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IClassListener;
import org.testng.ITestClass;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CleanUpListener implements IClassListener {

    @SuppressWarnings("deprecation")
    @Override
    public void onAfterClass(ITestClass testResult) {
        Object[] testInstance = testResult.getInstances(false);

        List<TestAutomationCloseable> testResources = Arrays.stream(testInstance)
                .map(this::getDeclaredTestResources)
                .flatMap(Collection::stream)
                .toList();

        List<Throwable> caughtThrowables = new ArrayList<>();

        for (TestAutomationCloseable resource : testResources) {
            try {
                log.info("CleanUp test resource {} started", resource.getClass());
                resource.cleanUp();
                log.info("CleanUp test resource {} finished", resource.getClass());
            } catch (Throwable t) {
                log.info("CleanUp test resource {} failed", resource.getClass());
                caughtThrowables.add(t);
            }
        }

        if (!caughtThrowables.isEmpty()) buildAndLogAggregatedExceptionMsg(caughtThrowables);
    }

    private List<TestAutomationCloseable> getDeclaredTestResources(final Object testInstance) {
        return Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CleanUp.class))
                .map(field -> {
                    if (!TestAutomationCloseable.class.isAssignableFrom(field.getType())) {
                        throw new CleanupException(field);
                    }
                    try {
                        field.setAccessible(true);
                        return (TestAutomationCloseable) field.get(testInstance);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private void buildAndLogAggregatedExceptionMsg(List<Throwable> throwables) {
        String aggregatedMsg = throwables.stream()
                .map(Throwable::getMessage)
                .collect(Collectors.joining("%n"));
        log.error(String.format("Failed to CleanUp resource: %s", aggregatedMsg));
    }

    public static class CleanupException extends RuntimeException {

        private static final String ERROR_MSG = "%s %s field with @CleanUp annotation does not implement " +
                "TestAutomationCloseable";

        public CleanupException(Field field) {
            super(String.format(ERROR_MSG, field.getType(), field.getName()));
            log.error(String.format(ERROR_MSG, field.getType(), field.getName()));
        }
    }
}
