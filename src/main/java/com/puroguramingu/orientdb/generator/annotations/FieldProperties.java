package com.puroguramingu.orientdb.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldProperties {
  String fieldName() default "";
  String min() default "";
  String max() default "";
  boolean mandatory() default false;
  boolean notnull() default false;
  boolean readonly() default false;
  boolean unique() default false;
}
