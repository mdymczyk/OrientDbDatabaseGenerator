package com.puroguramingu.orientdb.generator.sort;

import com.puroguramingu.orientdb.generator.ParentClass;
import com.puroguramingu.orientdb.generator.ParentsParentClass;
import com.puroguramingu.orientdb.generator.TestClass;
import com.puroguramingu.orientdb.generator.annotations.Entity;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class TopologicalSorterTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionOnInvalidInput() {
    // given

    // when
    TopologicalSorter.sort(null);

    // then
  }

  @Test
  public void shouldReturnEmptySet() {
    // given

    // when
    LinkedHashSet<Class<?>> sorted = TopologicalSorter.sort(new HashSet<Class<?>>());

    // then
    Assertions.assertThat(sorted).isNotNull().hasSize(0);
  }

  @Test
  public void shouldSortInput() {
    // given
    Reflections reflections = new Reflections("");
    Set<Class<?>> entities = TopologicalSorter.sort(reflections.getTypesAnnotatedWith(Entity.class));

    // when
    LinkedHashSet<Class<?>> sorted = TopologicalSorter.sort(entities);
    Iterator<Class<?>> order = new LinkedHashSet<Class<?>>() {
      {
        this.add(ParentsParentClass.class);
        this.add(ParentClass.class);
        this.add(TestClass.class);
      }
    }.iterator();

    // then
    Assertions.assertThat(sorted).isNotNull().hasSize(6);
    Iterator<Class<?>> iterator = sorted.iterator();
    Class<?> currentOrdered = order.next();
    while (iterator.hasNext()) {
      if (iterator.next().equals(currentOrdered) && order.hasNext()) {
        currentOrdered = order.next();
      }
    }
    Assertions.assertThat(order.hasNext()).isFalse();
  }
}
