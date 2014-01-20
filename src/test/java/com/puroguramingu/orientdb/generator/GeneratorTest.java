package com.puroguramingu.orientdb.generator;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import org.assertj.core.api.Assertions;
import org.junit.After;
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

  private ODatabaseDocumentTx openTestDb() {
    ODatabaseDocumentTx db = new ODatabaseDocumentTx("local:database");
    db.open("admin", "admin");
    return db;
  }

}