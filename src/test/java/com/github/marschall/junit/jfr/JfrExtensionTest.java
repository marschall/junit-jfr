package com.github.marschall.junit.jfr;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(JfrExtension.class)
class JfrExtensionTest {

  @BeforeAll
  static void beforeAll() {

  }

  @AfterAll
  static void afterAll() {

  }

  @BeforeEach
  void beforeEach() {

  }

  @AfterEach
  void afterEach() {

  }

  @Test
  void test1() {
    assertNotEquals("1", "2");
  }

  @Test
  void test2() {
    assertNotEquals("2", "3");
  }

}
