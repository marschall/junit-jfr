package com.github.marschall.junit.jfr;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.StackTrace;

public class JfrExtension implements
  BeforeAllCallback, AfterAllCallback,
  BeforeEachCallback, AfterEachCallback,
  BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final String TEST_JFR_EVENT = "test jfr event";

  private static final String EACH_JFR_EVENT = "each jfr event";

  private static final String ALL_JFR_EVENT = "all jfr event";

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    var event = new TestExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    this.getStore(context).put(TEST_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    var event = this.getEvent(TEST_JFR_EVENT, TestExecutionEvent.class, context);
    event.end();
    event.commit();
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    var event = new TestWithEachExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    this.getStore(context).put(EACH_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterEach(ExtensionContext context) {
    var event = this.getEvent(EACH_JFR_EVENT, TestWithEachExecutionEvent.class, context);
    event.end();
    event.commit();
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    var event = new TestWithAllExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    this.getStore(context).put(ALL_JFR_EVENT, event);
    event.begin();

  }

  @Override
  public void afterAll(ExtensionContext context) {
    var event = this.getEvent(ALL_JFR_EVENT, TestWithAllExecutionEvent.class, context);
    event.end();
    event.commit();
  }

  private <T extends Event> T getEvent(String key, Class<T> eventClass, ExtensionContext context) {
    return this.getStore(context).remove(key, eventClass);
  }

  private Store getStore(ExtensionContext context) {
    return context.getStore(Namespace.create(this.getClass(), context.getUniqueId()));
  }

  // defining the properties in the superclass does not seem to work :-(

  @Category("JUnit")
  @StackTrace(false)
  @Label("Test execution")
  @Description("test execution without @BeforeEach and @AfterEach methods")
  static class TestExecutionEvent extends Event {

    @Label("Unique ID")
    @Description("The unique ID of the test or container")
    private String uniqueId;

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;

    String getUniqueId() {
      return this.uniqueId;
    }

    void setUniqueId(String operationName) {
      this.uniqueId = operationName;
    }

    String getDisplayName() {
      return this.displayName;
    }

    void setDisplayName(String query) {
      this.displayName = query;
    }

    String getTestMethod() {
      return this.testMethod;
    }

    void setTestMethod(String query) {
      this.testMethod = query;
    }

  }

  @Category("JUnit")
  @StackTrace(false)
  @Label("Test Execution with *Each")
  @Description("test execution with @BeforeEach and @AfterEach methods")
  static class TestWithEachExecutionEvent extends Event {

    @Label("Unique ID")
    @Description("The unique ID of the test or container")
    private String uniqueId;

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;

    String getUniqueId() {
      return this.uniqueId;
    }

    void setUniqueId(String operationName) {
      this.uniqueId = operationName;
    }

    String getDisplayName() {
      return this.displayName;
    }

    void setDisplayName(String query) {
      this.displayName = query;
    }

    String getTestMethod() {
      return this.testMethod;
    }

    void setTestMethod(String query) {
      this.testMethod = query;
    }

  }


  @Category("JUnit")
  @StackTrace(false)
  @Label("Test Execution with *All")
  @Description("test execution with @BeforeAll and @AfterAll methods")
  static class TestWithAllExecutionEvent extends Event {

    @Label("Unique ID")
    @Description("The unique ID of the test or container")
    private String uniqueId;

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;

    String getUniqueId() {
      return this.uniqueId;
    }

    void setUniqueId(String operationName) {
      this.uniqueId = operationName;
    }

    String getDisplayName() {
      return this.displayName;
    }

    void setDisplayName(String query) {
      this.displayName = query;
    }

    String getTestMethod() {
      return this.testMethod;
    }

    void setTestMethod(String query) {
      this.testMethod = query;
    }

  }

}
