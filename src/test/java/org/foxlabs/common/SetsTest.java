/*
 * Copyright (C) 2020 FoxLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.common;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Sets.*;

/**
 * Tests for methods of the {@link Sets} class.
 *
 * @author Fox Mulder
 */
public class SetsTest {

  // Array operations

  /**
   * Tests the {@link Sets#toHashSet(Object...)} method.
   */
  @Test
  public void test_toHashSet() {
    assertThrows(NullPointerException.class, () -> toHashSet((String[]) null));
    assertNotSame(Collections.emptySet(), toHashSet());
    final Set<String> sampleSet = new HashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final Set<String> actualSet = toHashSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    actualSet.add("four");
    assertTrue(actualSet.contains("one"));
    assertTrue(actualSet.contains("two"));
    assertTrue(actualSet.contains("three"));
    assertTrue(actualSet.contains("four"));
  }

  /**
   * Tests the {@link Sets#toImmutableHashSet(Object...)} method.
   */
  @Test
  public void test_toImmutableHashSet() {
    assertThrows(NullPointerException.class, () -> toImmutableHashSet((String[]) null));
    assertSame(Collections.emptySet(), toImmutableHashSet());
    final Set<String> sampleSet = new HashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final Set<String> actualSet = toImmutableHashSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    assertThrows(UnsupportedOperationException.class, () -> actualSet.add("four"));
    assertTrue(actualSet.contains("one"));
    assertTrue(actualSet.contains("two"));
    assertTrue(actualSet.contains("three"));
    assertFalse(actualSet.contains("four"));
  }

  /**
   * Tests the {@link Sets#toLinkedHashSet(Object...)} method.
   */
  @Test
  public void test_toLinkedHashSet() {
    assertThrows(NullPointerException.class, () -> toLinkedHashSet((String[]) null));
    assertNotSame(Collections.emptySet(), toLinkedHashSet());
    final Set<String> sampleSet = new LinkedHashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final Set<String> actualSet = toLinkedHashSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    actualSet.add("four");
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("one", itr.next());
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertEquals("four", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#toImmutableLinkedHashSet(Object...)} method.
   */
  @Test
  public void test_toImmutableLinkedHashSet() {
    assertThrows(NullPointerException.class, () -> toImmutableLinkedHashSet((String[]) null));
    assertSame(Collections.emptySet(), toImmutableLinkedHashSet());
    final Set<String> sampleSet = new LinkedHashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final Set<String> actualSet = toImmutableLinkedHashSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    assertThrows(UnsupportedOperationException.class, () -> actualSet.add("four"));
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("one", itr.next());
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#toTreeSet(Object...)} method.
   */
  @Test
  public void test_toTreeSet() {
    assertThrows(NullPointerException.class, () -> toTreeSet((String[]) null));
    assertNotSame(Collections.emptySet(), toTreeSet());
    final SortedSet<String> sampleSet = new TreeSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final SortedSet<String> actualSet = toTreeSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    actualSet.add("four");
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("four", itr.next());
    assertEquals("one", itr.next());
    assertEquals("three", itr.next());
    assertEquals("two", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#toImmutableTreeSet(Object...)} method.
   */
  @Test
  public void test_toImmutableTreeSet() {
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet((String[]) null));
    assertSame(Collections.emptySortedSet(), toImmutableTreeSet());
    final SortedSet<String> sampleSet = new TreeSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final SortedSet<String> actualSet = toImmutableTreeSet("one", "two", "three");
    assertEquals(sampleSet, actualSet);
    assertThrows(UnsupportedOperationException.class, () -> actualSet.add("four"));
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("one", itr.next());
    assertEquals("three", itr.next());
    assertEquals("two", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#toTreeSet(Comparator, Object...)} method.
   */
  @Test
  public void test_toTreeSet_comparator() {
    assertThrows(NullPointerException.class, () -> toTreeSet((Comparator<?>) null));
    final Comparator<String> comparator = (s1, s2) -> -s1.compareTo(s2);
    assertThrows(NullPointerException.class, () -> toTreeSet(comparator, (String[]) null));
    assertNotSame(Collections.emptySortedSet(), toTreeSet(comparator));
    final SortedSet<String> sampleSet = new TreeSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final SortedSet<String> actualSet = toTreeSet(comparator, "one", "two", "three");
    assertEquals(sampleSet, actualSet);
    actualSet.add("four");
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertEquals("one", itr.next());
    assertEquals("four", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#toImmutableTreeSet(Comparator, Object...)} method.
   */
  @Test
  public void test_toImmutableTreeSet_comparator() {
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet((Comparator<?>) null));
    final Comparator<String> comparator = (s1, s2) -> -s1.compareTo(s2);
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet(comparator, (String[]) null));
    assertSame(Collections.emptySortedSet(), toImmutableTreeSet(comparator));
    final SortedSet<String> sampleSet = new TreeSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    final SortedSet<String> actualSet = toImmutableTreeSet(comparator, "one", "two", "three");
    assertEquals(sampleSet, actualSet);
    assertThrows(UnsupportedOperationException.class, () -> actualSet.add("four"));
    final Iterator<String> itr = actualSet.iterator();
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertEquals("one", itr.next());
    assertFalse(itr.hasNext());
  }

  /**
   * Tests the {@link Sets#addAll(Set, Object...)} method.
   */
  @Test
  public void test_addAll() {
    assertThrows(NullPointerException.class, () -> addAll((Set<?>) null));
    final Set<String> actualSet = new HashSet<>();
    assertThrows(NullPointerException.class, () -> addAll(actualSet, (String[]) null));
    final Set<String> sampleSet = new LinkedHashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");
    assertSame(actualSet, addAll(actualSet, "one", "two", "three"));
    assertEquals(sampleSet, actualSet);
    assertSame(actualSet, addAll(actualSet, "four"));
    assertTrue(actualSet.contains("one"));
    assertTrue(actualSet.contains("two"));
    assertTrue(actualSet.contains("three"));
    assertTrue(actualSet.contains("four"));
  }

}
