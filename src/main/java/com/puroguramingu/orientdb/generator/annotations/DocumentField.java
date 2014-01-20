package com.puroguramingu.orientdb.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DocumentField {
  String fieldName() default "";
  String fieldType() default "";
  String min() default "";
  String max() default "";
  boolean strict() default true;
  boolean required() default true;

}
