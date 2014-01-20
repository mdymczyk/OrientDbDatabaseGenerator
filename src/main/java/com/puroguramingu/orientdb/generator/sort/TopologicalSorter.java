package com.puroguramingu.orientdb.generator.sort;

import com.puroguramingu.orientdb.generator.None;
import com.puroguramingu.orientdb.generator.annotations.Entity;

import java.util.LinkedHashSet;
import java.util.Set;

public class TopologicalSorter {
  public static LinkedHashSet<Class<?>> sort(Set<Class<?>> typesAnnotatedWith) {
    if(null == typesAnnotatedWith) {
      throw new IllegalArgumentException("Set to be sorted cannot be null.");
    }

    LinkedHashSet<Class<?>> sorted = new LinkedHashSet<>();
    for (Class<?> type : typesAnnotatedWith) {
      Class<?> parent = getParent(type);
      if (!parent.equals(None.class)) {
        if (!sorted.contains(parent)) {
          addParent(parent, sorted);
        }
      }
      if (!sorted.contains(type)) {
        sorted.add(type);
      }
    }
    return sorted;
  }

  private static Class<?> getParent(Class<?> type) {
    Entity entityAnnotation = type.getAnnotation(Entity.class);
    return entityAnnotation.parent();
  }

  private static void addParent(Class<?> parent, LinkedHashSet<Class<?>> sorted) {
    Class<?> parentsParent = getParent(parent);
    if (!parentsParent.equals(None.class)) {
      addParent(parentsParent, sorted);
    }
    sorted.add(parent);
  }
}
