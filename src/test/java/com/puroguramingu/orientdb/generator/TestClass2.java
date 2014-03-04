package com.puroguramingu.orientdb.generator;

import com.puroguramingu.orientdb.generator.annotations.Entity;
import com.puroguramingu.orientdb.generator.annotations.LinkField;

import java.util.List;
import java.util.Set;

@Entity(name = "myclass")
public class TestClass2 {
  @LinkField(target = TestClass.class)
  private TestClass member;

  @LinkField(target = TestClass.class)
  private List<TestClass> memberList;

  @LinkField(target = TestClass.class)
  private Set<TestClass> memberSet;
}
