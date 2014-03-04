package com.puroguramingu.orientdb.generator;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.core.metadata.schema.OType;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

public class GeneratorTest {

  @After
  public void cleanUp() {
    new ODatabaseDocumentTx("local:database").open("admin", "admin").drop();
  }

  @Test
  public void shouldCreateADb() {
    // GIVEN
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("local:database");
    Assertions.assertThat(db.exists()).isFalse();

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    Assertions.assertThat(db.exists()).isTrue();
  }

  @Test
  public void shouldRecreateAnExistingDb() {
    // GIVEN
    Generator.main(new String[]{"-dbName", "database"});
    ODatabaseDocumentTx db = openTestDb();
    db.getMetadata().getSchema().createClass("test");
    Assertions.assertThat(db.exists()).isTrue();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("test")).isNotNull();
    db.close();

    // WHEN
    Generator.main(new String[]{"-dbName", "database", "-delete"});

    // THEN
    Assertions.assertThat(db.exists()).isTrue();
    db = new ODatabaseDocumentTx("local:database");
    db.open("admin", "admin");
    Assertions.assertThat(db.exists()).isTrue();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("test")).isNull();
  }

  @Test
  public void shouldCreateDbClass() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("testclass")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("testclass").isAbstract()).isFalse();
  }

  @Test
  public void shouldCreateDbClassWithCustomName() {
    // given

    // when
    Generator.main(new String[]{"-dbName", "database"});

    // then
    ODatabaseDocumentTx db = openTestDb();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("myclass")).isNotNull();

  }


  @Test
  public void shouldCreateDbAbstractClass() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("testabstractclass")).isNotNull();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("testabstractclass").isAbstract()).isTrue();
  }

  @Test
  public void shouldAddDbSubclass() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    OClass testClass = db.getMetadata().getSchema().getClass("testclass");
    Assertions.assertThat(testClass).isNotNull();
    Assertions.assertThat(testClass.isAbstract()).isFalse();
    Assertions.assertThat(testClass.isSubClassOf("parentclass")).isTrue();
    Assertions.assertThat(db.getMetadata().getSchema().getClass("parentclass").isSuperClassOf(testClass)).isTrue();
  }

  @Test
  public void shouldCreateDbClassWithBasicFields() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    OClass testClass = db.getMetadata().getSchema().getClass("fieldedclass");
    Assertions.assertThat(testClass).isNotNull();
    Assertions.assertThat(testClass.properties()).hasSize(12);
    Assertions.assertThat(testClass.getProperty("byteField").getType()).isEqualTo(OType.BYTE);
    Assertions.assertThat(testClass.getProperty("decimalField").getType()).isEqualTo(OType.DECIMAL);
    Assertions.assertThat(testClass.getProperty("customField").getType()).isEqualTo(OType.CUSTOM);
    Assertions.assertThat(testClass.getProperty("binaryField").getType()).isEqualTo(OType.BINARY);
    Assertions.assertThat(testClass.getProperty("datetimeField").getType()).isEqualTo(OType.DATETIME);
    Assertions.assertThat(testClass.getProperty("dateField").getType()).isEqualTo(OType.DATE);
    Assertions.assertThat(testClass.getProperty("stringField").getType()).isEqualTo(OType.STRING);
    Assertions.assertThat(testClass.getProperty("doubleField").getType()).isEqualTo(OType.DOUBLE);
    Assertions.assertThat(testClass.getProperty("floatField").getType()).isEqualTo(OType.FLOAT);
    Assertions.assertThat(testClass.getProperty("longField").getType()).isEqualTo(OType.LONG);
    Assertions.assertThat(testClass.getProperty("intField").getType()).isEqualTo(OType.INTEGER);
    Assertions.assertThat(testClass.getProperty("shortField").getType()).isEqualTo(OType.SHORT);
  }

  @Test
  public void shouldCreateDbClassWithBasicFieldsAndRestrictions() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    OClass testClass = db.getMetadata().getSchema().getClass("fieldedclass");
    Assertions.assertThat(testClass).isNotNull();
    Assertions.assertThat(testClass.properties()).hasSize(12);
    Assertions.assertThat(testClass.getProperty("byteField").getType()).isEqualTo(OType.BYTE);
    Assertions.assertThat(testClass.getProperty("decimalField").getType()).isEqualTo(OType.DECIMAL);
    Assertions.assertThat(testClass.getProperty("customField").getType()).isEqualTo(OType.CUSTOM);
    Assertions.assertThat(testClass.getProperty("binaryField").getType()).isEqualTo(OType.BINARY);
    Assertions.assertThat(testClass.getProperty("datetimeField").getType()).isEqualTo(OType.DATETIME);
    Assertions.assertThat(testClass.getProperty("dateField").getType()).isEqualTo(OType.DATE);
    Assertions.assertThat(testClass.getProperty("stringField").getType()).isEqualTo(OType.STRING);
    Assertions.assertThat(testClass.getProperty("doubleField").getType()).isEqualTo(OType.DOUBLE);
    Assertions.assertThat(testClass.getProperty("floatField").getType()).isEqualTo(OType.FLOAT);
    Assertions.assertThat(testClass.getProperty("longField").getType()).isEqualTo(OType.LONG);
    Assertions.assertThat(testClass.getProperty("intField").getType()).isEqualTo(OType.INTEGER);
    Assertions.assertThat(testClass.getProperty("intField").getMin()).isEqualTo("1");
    Assertions.assertThat(testClass.getProperty("intField").getMax()).isEqualTo("10");
    Assertions.assertThat(testClass.getProperty("intField").isReadonly()).isEqualTo(true);
    Assertions.assertThat(testClass.getProperty("intField").isNotNull()).isEqualTo(true);
    Assertions.assertThat(testClass.getProperty("intField").isMandatory()).isEqualTo(false);
    Assertions.assertThat(testClass.getProperty("shortField").getType()).isEqualTo(OType.SHORT);
  }

  @Test
  public void shouldCreateUniqueIndex() {
    // GIVEN

    // WHEN
    Generator.main(new String[]{"-dbName", "database"});

    // THEN
    ODatabaseDocumentTx db = openTestDb();
    OSchema schema = db.getMetadata().getSchema();
    OClass testClass = schema.getClass("fieldedclass");
    Assertions.assertThat(testClass).isNotNull();
    Assertions.assertThat(testClass.properties()).hasSize(12);
    Assertions.assertThat(testClass.getProperty("stringField").getType()).isEqualTo(OType.STRING);
    Assertions.assertThat(testClass.getProperty("stringField").getAllIndexes()).isNotNull().isNotEmpty().hasSize(1);
    Assertions.assertThat(testClass.getProperty("stringField").getAllIndexes().iterator().next().getType()).isEqualTo(OClass.INDEX_TYPE.UNIQUE.toString());
  }

  private ODatabaseDocumentTx openTestDb() {
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("local:database");
    db.open("admin", "admin");
    return db;
  }

}