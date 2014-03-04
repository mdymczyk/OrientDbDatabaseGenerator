package com.puroguramingu.orientdb.generator;

import com.beust.jcommander.JCommander;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.puroguramingu.orientdb.generator.annotations.*;
import com.puroguramingu.orientdb.generator.sort.TopologicalSorter;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Generator {

  public Map<Class<?>, OClass> dbClasses = new HashMap<>();

  public static void main(String[] args) {
    CmdLineArgs parsedArgs = new CmdLineArgs();
    new JCommander(parsedArgs, args);
    Generator generator = new Generator();
    generator.createDb(parsedArgs);
  }

  private void createDb(CmdLineArgs parsedArgs) {
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

  private void addSchema(ODatabaseDocumentTx db) {
    OSchema schema = db.getMetadata().getSchema();

    Reflections reflections = new Reflections("");
    Set<Class<?>> entities = TopologicalSorter.sort(reflections.getTypesAnnotatedWith(Entity.class));

    for (Class entity : entities) {
      dbClasses.put(entity, createDbClass(schema, entity));
    }

    for (Map.Entry<Class<?>, OClass> entry : dbClasses.entrySet()) {
      addDbClassProperties(entry.getKey(), entry.getValue());
    }
  }

  private OClass createDbClass(OSchema schema, Class entity) {
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

    return klazz;
  }

  private void addDbClassProperties(Class entity, OClass oEntity) {
    Set<Field> fields = new HashSet<>(ReflectionUtils.getAllFields(entity, ReflectionUtils.withAnnotation(BasicField.class)));
    fields.addAll(ReflectionUtils.getAllFields(entity, ReflectionUtils.withAnnotation(EmbeddedField.class)));
    fields.addAll(ReflectionUtils.getAllFields(entity, ReflectionUtils.withAnnotation(LinkField.class)));
    for (Field field : fields) {
      OProperty property;
      if (field.getAnnotation(EmbeddedField.class) != null) {
        property = oEntity.createProperty(field.getName(), OType.EMBEDDED, dbClasses.get(field.getAnnotation(EmbeddedField.class).target()));
      } else if (field.getAnnotation(LinkField.class) != null) {
        property = oEntity.createProperty(field.getName(), OType.LINK, dbClasses.get(field.getAnnotation(LinkField.class).target()));
      } else {
        property = oEntity.createProperty(field.getName(), field.getAnnotation(BasicField.class).fieldType());
      }

      FieldProperties docFieldMetadata = field.getAnnotation(FieldProperties.class);
      if (null != docFieldMetadata) {
        if (!docFieldMetadata.fieldName().isEmpty()) {
          property.setName(docFieldMetadata.fieldName());
        }

        if (!docFieldMetadata.min().isEmpty()) {
          property.setMin(docFieldMetadata.min());
        }
        if (!docFieldMetadata.max().isEmpty()) {
          property.setMax(docFieldMetadata.max());
        }

        property.setMandatory(docFieldMetadata.mandatory());
        property.setNotNull(docFieldMetadata.notnull());
        property.setReadonly(docFieldMetadata.readonly());

        if (docFieldMetadata.unique()) {
          oEntity.createIndex(field.getName() + "Idx", OClass.INDEX_TYPE.UNIQUE, field.getName());
        }
      }
    }
  }

  private String getLowerCaseClassName(Class entity, Entity entityAnnotation) {
    return entityAnnotation.name().isEmpty()
        ? entity.getSimpleName().toLowerCase()
        : entityAnnotation.name();
  }
}
