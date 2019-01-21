package com.github.marschall.junit.jfr;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@ExtendWith(JfrExtension.class)
class JfrExtensionTest {

  @BeforeAll
  static void beforeAll() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeAll");
    demoEvent.commit();
  }

  @AfterAll
  static void afterAll() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterAll");
    demoEvent.commit();
  }

  @BeforeEach
  void beforeEach() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@BeforeEach");
    demoEvent.commit();
  }

  @AfterEach
  void afterEach() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("@AfterEach");
    demoEvent.commit();
  }

  @Test
  void test1() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("test1");
    demoEvent.commit();
  }

  @Test
  void test2() {
    DemoEvent demoEvent = new DemoEvent();
    demoEvent.setName("test2");
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
