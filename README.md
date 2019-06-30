JUnit JFR [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/junit-jfr/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/junit-jfr) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/junit-jfr.svg)](https://www.javadoc.io/doc/com.github.marschall/junit-jfr)
=========

A JUnit extension that generates JFR events.

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>junit-jfr</artifactId>
  <version>0.1.0</version>
  <scope>test</scope>
</dependency>
```

Requires Java 11 based on OpenJDK.

![Flight Recording of a JUnit Test](https://raw.githubusercontent.com/marschall/junit-jfr/master/src/main/javadoc/screenshot.png)

Event Types
-----------

The extension supports the following event types in the "JUnit" category.

<dl>
<dt>@BeforeAll</dt>
<dd>Contains the execution of all <code>@BeforeAll</code> methods.</dd>
<dt>@BeforeEach</dt>
<dd>Contains the execution of all <code>@BeforeEach</code> methods.</dd>
<dt>@Test</dt>
<dd>Contains the execution of all <code>@Test</code> methods.</dd>
<dt>@AfterEach</dt>
<dd>Contains the execution of all <code>@AfterEach</code> methods.</dd>
<dt>@AfterAll</dt>
<dd>Contains the execution of all <code>@AfterAll</code> methods.</dd>
</dl>

Every event type may also cover some extension methods.

Usage
-----

* Add `@JfrProfiled` to your unit test class, see [JfrExtensionTest](https://github.com/marschall/junit-jfr/blob/master/src/test/java/com/github/marschall/junit/jfr/JfrExtensionTest.java) for and example.
* Generate a flight recording from your unit tests, eg using
```
-XX:StartFlightRecording:filename=recording.jfr
-XX:FlightRecorderOptions:stackdepth=128
```

```java
@JfrProfiled
class ProfiledTests {

  @Test
  void testMethod() {
    // implementation
  }

}
```
