package com.github.marschall.junit.jfr;

import java.lang.reflect.Method;

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

/**
 * JUnit extension that causes a JUnit test to generate Flight Recorder events.
 */
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
    event.setDisplayName(context.getDisplayName());
    context.getTestClass().ifPresent(clazz -> event.setTestClass(clazz));
    storeEvent(context, BEFORE_ALL_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    stop(context.getParent().get(), BEFORE_ALL_JFR_EVENT);
    
    var event = new BeforeEachExecutionEvent();
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(getMethodName(method)));
    context.getTestClass().ifPresent(clazz -> event.setTestClass(clazz));
    storeEvent(context, BEFORE_EACH_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    stop(context, BEFORE_EACH_JFR_EVENT);
    
    var event = new TestExecutionEvent();
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(getMethodName(method)));
    context.getTestClass().ifPresent(clazz -> event.setTestClass(clazz));
    this.getStore(context).put(TEST_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    stop(context, TEST_JFR_EVENT);
    
    var event = new AfterEachExecutionEvent();
    event.setDisplayName(context.getDisplayName());
    context.getTestMethod().ifPresent(method -> event.setTestMethod(getMethodName(method)));
    context.getTestClass().ifPresent(clazz -> event.setTestClass(clazz));
    storeEvent(context, AFTER_EACH_JFR_EVENT, event);
    event.begin();
  }

  @Override
  public void afterEach(ExtensionContext context) {
    stop(context, AFTER_EACH_JFR_EVENT);
    
    var event = new AfterAllExecutionEvent();
    event.setDisplayName(context.getDisplayName());
    context.getTestClass().ifPresent(clazz -> event.setTestClass(clazz));
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
  
  private static String getMethodName(Method method) {
    boolean isVoid = method.getReturnType() == Void.TYPE;
    boolean hasParameters = method.getParameterCount() == 0;
    StringBuilder buffer = new StringBuilder();
    if (!isVoid) {
      buffer.append(method.getReturnType().getSimpleName());
      buffer.append(' ');
    }
    buffer.append(method.getName());
    if (hasParameters) {
      buffer.append('(');
      boolean first = true;
      for (Class<?> parameterType : method.getParameterTypes()) {
        if (!first) {
          buffer.append(", ");
        }
        buffer.append(parameterType.getSimpleName());
        first = false;
      }
      buffer.append(')');
    } else {
      buffer.append("()");
    }
    return buffer.toString();
  }

  // defining the properties in the superclass does not seem to work :-(
  


  @Category("JUnit")
  @StackTrace(false)
  @Label("@BeforeAll")
  @Description("execution of all @BeforeAll methods")
  static class BeforeAllExecutionEvent extends Event {

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;
    
    @Label("Test Class")
    @Description("The class associated with the test, if available")
    private Class<?> testClass;

    String getDisplayName() {
      return this.displayName;
    }

    void setDisplayName(String query) {
      this.displayName = query;
    }

    Class<?> getTestClass() {
      return testClass;
    }

    void setTestClass(Class<?> testClass) {
      this.testClass = testClass;
    }

  }

  @Category("JUnit")
  @StackTrace(false)
  @Label("@BeforeEach")
  @Description("execution of all @BeforeEach methods")
  static class BeforeEachExecutionEvent extends Event {

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;
    
    @Label("Test Class")
    @Description("The class associated with the test, if available")
    private Class<?> testClass;

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

    Class<?> getTestClass() {
      return testClass;
    }

    void setTestClass(Class<?> testClass) {
      this.testClass = testClass;
    }

  }

  @Category("JUnit")
  @StackTrace(false)
  @Label("@Test")
  @Description("execution of a test without @BeforeEach and @AfterEach methods")
  static class TestExecutionEvent extends Event {

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;
    
    @Label("Test Class")
    @Description("The class associated with the test, if available")
    private Class<?> testClass;

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

    Class<?> getTestClass() {
      return testClass;
    }

    void setTestClass(Class<?> testClass) {
      this.testClass = testClass;
    }

  }

  @Category("JUnit")
  @StackTrace(false)
  @Label("@AfterEach")
  @Description("execution of all @AfterEach methods")
  static class AfterEachExecutionEvent extends Event {

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;

    @Label("Test Method")
    @Description("The method associated with the test, if available")
    private String testMethod;
    
    @Label("Test Class")
    @Description("The class associated with the test, if available")
    private Class<?> testClass;

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

    Class<?> getTestClass() {
      return testClass;
    }

    void setTestClass(Class<?> testClass) {
      this.testClass = testClass;
    }

  }

  @Category("JUnit")
  @StackTrace(false)
  @Label("@AfterAll")
  @Description("execution of all @AfterAll methods")
  static class AfterAllExecutionEvent extends Event {

    @Label("Display Name")
    @Description("The display name for the test or container")
    private String displayName;
    
    @Label("Test Class")
    @Description("The class associated with the test, if available")
    private Class<?> testClass;

    String getDisplayName() {
      return this.displayName;
    }

    void setDisplayName(String query) {
      this.displayName = query;
    }

    Class<?> getTestClass() {
      return testClass;
    }

    void setTestClass(Class<?> testClass) {
      this.testClass = testClass;
    }

  }

}
