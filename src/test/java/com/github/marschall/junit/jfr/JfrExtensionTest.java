package com.github.marschall.junit.jfr;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.marschall.junit.jfr.JfrExtension.AfterAllExecutionEvent;
import com.github.marschall.junit.jfr.JfrExtension.AfterEachExecutionEvent;
import com.github.marschall.junit.jfr.JfrExtension.BeforeAllExecutionEvent;
import com.github.marschall.junit.jfr.JfrExtension.BeforeEachExecutionEvent;
import com.github.marschall.junit.jfr.JfrExtension.TestExecutionEvent;

import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.Label;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

@JfrProfiled
@DisplayName("JFR Demo Test")
class JfrExtensionTest {

  static final Path RECORDING_LOCATION = Path.of("target", MethodHandles.lookup().lookupClass().getSimpleName() + ".jfr");


  private static volatile Recording recording;

  @BeforeAll
  static void startRecording() throws IOException {
    recording = new Recording();
    recording.enable(BeforeAllExecutionEvent.class);
    recording.enable(BeforeEachExecutionEvent.class);
    recording.enable(TestExecutionEvent.class);
    recording.enable(AfterEachExecutionEvent.class);
    recording.enable(AfterAllExecutionEvent.class);
    recording.setMaxSize(1L * 1024L * 1024L);
    recording.setToDisk(true);
    recording.setDestination(RECORDING_LOCATION);
    recording.start();
  }

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

  @AfterAll
  static void stopRecording() throws IOException {
    recording.close();
    Set<String> eventNames = new HashSet<>();
    try (RecordingFile recordingFile = new RecordingFile(RECORDING_LOCATION)) {
      while (recordingFile.hasMoreEvents()) {
        RecordedEvent event = recordingFile.readEvent();
        EventType eventType = event.getEventType();
        eventNames.add(eventType.getName());
      }
    }
    assertFalse(eventNames.isEmpty());
    assertTrue(eventNames.contains(BeforeAllExecutionEvent.class.getName()));
    assertTrue(eventNames.contains(BeforeEachExecutionEvent.class.getName()));
    assertTrue(eventNames.contains(TestExecutionEvent.class.getName()));
    assertTrue(eventNames.contains(AfterEachExecutionEvent.class.getName()));
//    assertTrue(eventNames.contains(AfterAllExecutionEvent.class.getName()));
  }

  @Category("Test")
  @Label("Demo Event")
  static class DemoEvent extends Event {

    @Label("Name")
    private String name;

    String getName() {
      return this.name;
    }

    void setName(String name) {
      this.name = name;
    }

  }

}
