package com.github.marschall.junit.jfr;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Meta annotation that registers a JUnit extension that causes a JUnit test to generate Flight Recorder events.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@ExtendWith(JfrExtension.class)
public @interface JfrProfiled {

}
