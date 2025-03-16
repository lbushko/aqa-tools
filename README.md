# AQA Tools

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)][license]

Tools for building Advanced Test Automation Frameworks

- Utils
- TestNg Listeners
- JUnit Extensions
- Annotations (@CleanUp)
- Gradle Plugins
- Integration with Reporting

## Table of Contents

- [Table of Contents](#table-of-contents)
- [Release notes](#release-notes)
  - [0.1.0](#010)
- [Modules](#modules)
- [Prerequisites](#prerequisites)
- [Adding dependencies](#adding-dependencies)
- [Usage](#usage)
- [Contributing](#contributing)

## Release notes

### 0.1.0

- **CleanUpListener**: Custom TestNg Listener to automatically invoke clean-up method for test resorces

## Modules

- **test-listener**

## Prerequisites

- Java 21+
- Gradle 8.1+

## Adding dependencies

## Usage

### TestNg [CleanUpListener](https://github.com/lbushko/aqa-tools/blob/main/test-listeners/src/main/java/io/github/lbushko/aqa/tools/testng/listener/CleanUpListener.java) 

1. Configure TestNg to use CleanUpListener
```groovy
test {
    systemProperties System.properties
    useTestNG { TestNGOptions options ->
    options.listeners << "com.lbushko.aqa.tools.testng.listener.CleanUpListener"
    }
}
```
2. Implement TestAutomationCloseable interface

```java
public class TestResource implements TestAutomationCloseable {

    @Override
    public void cleanUp() {
        //TODO add clean-up login
    }
}
```
3. Use @CleanUp Annotation in Test Class to automatically invoke clean-up method @AfterClass

```java
public class TestClass {

    @CleanUp
    private final TestResource testResource = new TestResource();

    @Test
    void testMethod() {}
}
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

[license]: https://www.apache.org/licenses/LICENSE-2.0