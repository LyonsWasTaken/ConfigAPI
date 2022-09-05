package dev.lyons.configapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE}) // goes over types for people who want to use reflections to register configs, goes over fields for saving things
public @interface Config {
	String value() default "";
}
