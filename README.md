JUnit JFR
=========

A JUnit extension that generates JFR events.

Requires Java 11.

![Flight Recording of a JUnit Test](https://github.com/marschall/junit-jfr/blob/master/src/main/javadoc/screenshot.png)

Event Types
-----------

The extension supports the following event types in the "JUnit" category.

<dl>
<dt>@BeforeAll</dt>
<dd>Contains the execution of all @BeforeAll methods.</dd>
<dt>@BeforeEach</dt>
<dd>Contains the execution of all @BeforeEach methods.</dd>
<dt>@Test</dt>
<dd>Contains the execution of all @Test methods.</dd>
<dt>@AfterEach</dt>
<dd>Contains the execution of all @AfterEach methods.</dd>
<dt>@AfterAll</dt>
<dd>Contains the execution of all @AfterAll methods.</dd>
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
