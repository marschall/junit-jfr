JUnit JFR
=========

A JUnit extension that generates JFR events.

Requires Java 11.

![Flight Recording of a JUnit Test](https://github.com/marschall/junit-jfr/blob/master/src/main/javadoc/screenshot.png)

Usage
-----

* Add `@JfrProfiled` to your unit test class, see [JfrExtensionTest](https://github.com/marschall/junit-jfr/blob/master/src/test/java/com/github/marschall/junit/jfr/JfrExtensionTest.java) for and example.
* Generate a flight recording from your unit tests, eg using
  ```
  -XX:StartFlightRecording:filename=recording.jfr
  -XX:FlightRecorderOptions:stackdepth=128
  ```
