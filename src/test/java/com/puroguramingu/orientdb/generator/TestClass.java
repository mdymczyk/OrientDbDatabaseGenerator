package com.puroguramingu.orientdb.generator;

import com.puroguramingu.orientdb.generator.annotations.EmbeddedField;
import com.puroguramingu.orientdb.generator.annotations.Entity;

import java.util.List;
import java.util.Set;

@Entity(parent = ParentClass.class)
public class TestClass {

  @EmbeddedField(target = TestClass2.class)
  private TestClass2 member;

  @EmbeddedField(target = TestClass2.class)
  private List<TestClass2> memberList;

  @EmbeddedField(target = TestClass2.class)
  private Set<TestClass2> memberSet;
}
