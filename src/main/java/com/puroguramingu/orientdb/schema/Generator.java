package com.puroguramingu.orientdb.schema;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.puroguramingu.orientdb.schema.com.puroguramingu.orientdb.schema.annotations.DocumentField;
import com.puroguramingu.orientdb.schema.com.puroguramingu.orientdb.schema.annotations.Entity;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

public class Generator {

  public static void main(String[] args) {

    ODatabaseDocumentTx db = new ODatabaseDocumentTx("remote:localhost/petshop");
    db.create();
    OSchema schema = db.getMetadata().getSchema();

    Reflections reflections = new Reflections();
    Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
    // TODO: make this into a DAG and sort it topologically
    for (Class entity : entities) {
      if (Modifier.isAbstract(entity.getModifiers())) {
        schema.createAbstractClass(entity.getSimpleName().toLowerCase());
      } else {
        schema.createClass(entity.getSimpleName().toLowerCase());
      }


      Set<Field> fields = ReflectionUtils.getAllFields(entity, ReflectionUtils.withAnnotation(DocumentField.class));
    }

  }
}
