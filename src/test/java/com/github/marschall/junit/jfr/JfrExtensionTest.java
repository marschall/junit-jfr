package com.github.marschall.junit.jfr;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@JfrProfiled
@DisplayName("JFR Demo Test")
class JfrExtensionTest {

  @BeforeAll
  static void beforeAll1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeAll(1)");
    demoEvent.commit();
  }

  @BeforeAll
  static void beforeAll2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeAll(2)");
    demoEvent.commit();
  }

  @BeforeEach
  void beforeEach1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeEach(1)");
    demoEvent.commit();
  }

  @BeforeEach
  void beforeEach2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeEach(1)");
    demoEvent.commit();
  }

  @Test
  @DisplayName("TEST 1")
  void test1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("test1");
    demoEvent.commit();
  }

  @Test
  @DisplayName("TEST II")
  void test2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("test2");
    demoEvent.commit();
  }


  @AfterEach
  void afterEach1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterEach(1)");
    demoEvent.commit();
  }

  @AfterEach
  void afterEach2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterEach(2)");
    demoEvent.commit();
  }

  @AfterAll
  static void afterAll1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterAll(1)");
    demoEvent.commit();
  }

  @AfterAll
  static void afterAll2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterAll(2)");
    demoEvent.commit();
  }

  @Category("Test")
  @Label("Demo Event")
  static class DemoEvent extends Event {

    @Label("Name")
    private String name;

    String getName() {
      return name;
    }

    void setName(String name) {
      this.name = name;
    }

  }

}
