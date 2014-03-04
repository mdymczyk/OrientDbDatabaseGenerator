package com.puroguramingu.orientdb.generator;

import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.serialization.OSerializableStream;
import com.puroguramingu.orientdb.generator.annotations.BasicField;
import com.puroguramingu.orientdb.generator.annotations.Entity;
import com.puroguramingu.orientdb.generator.annotations.FieldProperties;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FieldedClass {

  @BasicField(fieldType = OType.BYTE)
  private byte byteField;

  @BasicField(fieldType = OType.SHORT)
  private short shortField;

  @BasicField(fieldType = OType.INTEGER)
  @FieldProperties(min = "1", max = "10", mandatory = false, readonly = true)
  private int intField;

  @BasicField(fieldType = OType.LONG)
  private long longField;

  @BasicField(fieldType = OType.FLOAT)
  private float floatField;

  @BasicField(fieldType = OType.DOUBLE)
  private double doubleField;

  @BasicField(fieldType = OType.STRING)
  @FieldProperties(unique = true)
  private String stringField;

  @BasicField(fieldType = OType.DATE)
  private Date dateField;

  @BasicField(fieldType = OType.DATETIME)
  private Date datetimeField;

  @BasicField(fieldType = OType.BINARY)
  private byte[] binaryField;

  @BasicField(fieldType = OType.CUSTOM)
  private OSerializableStream customField;

  @BasicField(fieldType = OType.DECIMAL)
  private BigDecimal decimalField;
}
