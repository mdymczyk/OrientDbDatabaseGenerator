package com.puroguramingu.orientdb.generator.annotations;

import com.puroguramingu.orientdb.generator.None;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
  Class<?> parent() default None.class;
  boolean isAbstract() default false;
  String name() default "";
}
