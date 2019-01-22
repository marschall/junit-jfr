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
  
  private static final String BEFORE_ALL_JFR_EVENT = "before all jfr event";
  
  private static final String BEFORE_EACH_JFR_EVENT = "before each jfr event";

  private static final String TEST_JFR_EVENT = "test jfr event";

  private static final String AFTER_EACH_JFR_EVENT = "after each jfr event";
  
  private static final String AFTER_ALL_JFR_EVENT = "after all jfr event";

  @Override
  public void beforeAll(ExtensionContext context) {
    var event = new BeforeAllExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    storeEvent(context, BEFORE_ALL_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    stop(context.getParent().get(), BEFORE_ALL_JFR_EVENT);
    
    var event = new BeforeEachExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    storeEvent(context, BEFORE_EACH_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    stop(context, BEFORE_EACH_JFR_EVENT);
    
    var event = new TestExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    this.getStore(context).put(TEST_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    stop(context, TEST_JFR_EVENT);
    
    var event = new AfterEachExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    storeEvent(context, AFTER_EACH_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterEach(ExtensionContext context) {
    stop(context, AFTER_EACH_JFR_EVENT);
    
    var event = new AfterAllExecutionEvent();
    event.setUniqueId(context.getUniqueId());
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(method.toString()));
    storeEvent(context.getParent().get(), AFTER_ALL_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    // runs affter @AfterAll
    stop(context, AFTER_ALL_JFR_EVENT);
  }
  
  private void stop(ExtensionContext context, String key) {
    var event = this.getStore(context).remove(key, Event.class);
    if (event != null) {
      event.end();
      event.commit();
    }
  }

  private Store getStore(ExtensionContext context) {
    return context.getStore(Namespace.create(this.getClass(), context.getUniqueId()));
  }
  
  private void storeEvent(ExtensionContext context, String key, Event event) {
    this.getStore(context).put(key, event);
  }

  // defining the properties in the superclass does not seem to work :-(

  @Category("JUnit")
  @StackTrace(false)
  @Label("@Test")
  @Description("execution of a test without @BeforeEach and @AfterEach methods")
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
  @Label("@BeforeEach")
  @Description("execution of all @BeforeEach methods")
  static class BeforeEachExecutionEvent extends Event {

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
  @Label("@AfterEach")
  @Description("execution of all @AfterEach methods")
  static class AfterEachExecutionEvent extends Event {

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
  @Label("@BeforeAll")
  @Description("execution of all @BeforeAll methods")
  static class BeforeAllExecutionEvent extends Event {

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
  @Label("@AfterAll")
  @Description("execution of all @AfterAll methods")
  static class AfterAllExecutionEvent extends Event {

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
