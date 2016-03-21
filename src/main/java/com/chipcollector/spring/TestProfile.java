package com.chipcollector.spring;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Profile("test")
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TestProfile {
}
