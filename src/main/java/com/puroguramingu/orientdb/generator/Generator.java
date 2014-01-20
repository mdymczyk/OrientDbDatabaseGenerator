package com.puroguramingu.orientdb.generator;

import com.beust.jcommander.JCommander;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.puroguramingu.orientdb.generator.annotations.DocumentField;
import com.puroguramingu.orientdb.generator.annotations.Entity;
import com.puroguramingu.orientdb.generator.sort.TopologicalSorter;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Set;

public class Generator {

  public static void main(String[] args) {
    CmdLineArgs parsedArgs = new CmdLineArgs();
    new JCommander(parsedArgs, args);
    createDb(parsedArgs);
  }

  private static void createDb(CmdLineArgs parsedArgs) {
    try (ODatabaseDocumentTx db = new ODatabaseDocumentTx("local:" + parsedArgs.getDbName())) {
      if (db.exists() && parsedArgs.isDelete()) {
        db.open("admin", "admin");
        db.drop();
      } else if (db.exists() && !parsedArgs.isDelete()) {
        return;
      }

      db.create();
      addSchema(db);
    }
  }

  private static void addSchema(ODatabaseDocumentTx db) {
    OSchema schema = db.getMetadata().getSchema();

    Reflections reflections = new Reflections("");
    Set<Class<?>> entities = TopologicalSorter.sort(reflections.getTypesAnnotatedWith(Entity.class));

    for (Class entity : entities) {
      createDbClass(schema, entity);
    }

  }

  private static void createDbClass(OSchema schema, Class entity) {
    Entity entityAnnotation = (Entity) entity.getAnnotation(Entity.class);

    OClass klazz;
    if (entityAnnotation.isAbstract()) {
      klazz = schema.createAbstractClass(getLowerCaseClassName(entity, entityAnnotation));
    } else {
      klazz = schema.createClass(getLowerCaseClassName(entity, entityAnnotation));
    }

    if (!entityAnnotation.parent().isInstance(Object.class)) {
      klazz.setSuperClass(schema.getClass(getLowerCaseClassName(entityAnnotation.parent(), entityAnnotation)));
    }

    addDbClassProperties(entity, klazz);

  }

  private static void addDbClassProperties(Class entity, OClass oEntity) {
    Set<Field> fields = ReflectionUtils.getAllFields(entity, ReflectionUtils.withAnnotation(DocumentField.class));
    for(Field field : fields) {
      oEntity.createProperty(field.getName(), getOType(field.getType()));
    }
  }

  private static OType getOType(Class<?> type) {
    return null;
  }

  private static String getLowerCaseClassName(Class entity, Entity entityAnnotation) {
    return entityAnnotation.name().isEmpty()
              ? entity.getSimpleName().toLowerCase()
              : entityAnnotation.name();
  }
}
