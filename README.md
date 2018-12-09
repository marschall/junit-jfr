JUnit JFR
=========

A JUnit extension that generates JFR events.

Requires Java 11.

![Flight Recording of a JUnit Test](https://github.com/marschall/junit-jfr/blob/master/src/main/javadoc/Screenshot%20from%202018-12-09%2019-36-55.png)


```
-XX:StartFlightRecording:filename=recording.jfr
-XX:FlightRecorderOptions:stackdepth=128
```
