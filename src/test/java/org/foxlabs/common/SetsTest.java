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
 * Tests for the {@link Predicates} class.
 *
 * @author Fox Mulder
 */
public class SetsTest {

  /**
   * Tests the following methods:
   * <ul>
   *   <li>{@link Sets#toHashSet(Object...)}</li>
   *   <li>{@link Sets#toImmutableHashSet(Object...)}</li>
   * </ul>
   */
  @Test
  public void testToHashSet() {
    final Set<String> expectedSet = new HashSet<>();
    Collections.addAll(expectedSet, "one", "two", "three");

    // toHashSet(Object...)
    assertThrows(NullPointerException.class, () -> toHashSet((String[]) null));
    assertNotSame(Collections.emptySet(), toHashSet());
    final Set<String> actualSet1 = toHashSet("one", "two", "three");
    assertEquals(expectedSet, actualSet1);
    actualSet1.add("four");
    assertTrue(actualSet1.contains("one"));
    assertTrue(actualSet1.contains("two"));
    assertTrue(actualSet1.contains("three"));
    assertTrue(actualSet1.contains("four"));

    // toImmutableHashSet(Object...)
    assertThrows(NullPointerException.class, () -> toImmutableHashSet((String[]) null));
    assertSame(Collections.emptySet(), toImmutableHashSet());
    final Set<String> actualSet2 = toImmutableHashSet("one", "two", "three");
    assertEquals(expectedSet, actualSet2);
    assertThrows(UnsupportedOperationException.class, () -> actualSet2.add("four"));
    assertTrue(actualSet2.contains("one"));
    assertTrue(actualSet2.contains("two"));
    assertTrue(actualSet2.contains("three"));
  }

  /**
   * Tests the following methods:
   * <ul>
   *   <li>{@link Sets#toLinkedHashSet(Object...)}</li>
   *   <li>{@link Sets#toImmutableLinkedHashSet(Object...)}</li>
   * </ul>
   */
  @Test
  public void testToLinkedHashSet() {
    final Set<String> expectedSet = new LinkedHashSet<>();
    Collections.addAll(expectedSet, "one", "two", "three");

    // toLinkedHashSet(Object...)
    assertThrows(NullPointerException.class, () -> toLinkedHashSet((String[]) null));
    assertNotSame(Collections.emptySet(), toLinkedHashSet());
    final Set<String> actualSet1 = toLinkedHashSet("one", "two", "three");
    assertEquals(expectedSet, actualSet1);
    actualSet1.add("four");
    final Iterator<String> itr1 = actualSet1.iterator();
    assertEquals("one", itr1.next());
    assertEquals("two", itr1.next());
    assertEquals("three", itr1.next());
    assertEquals("four", itr1.next());

    // toImmutableLinkedHashSet(Object...)
    assertThrows(NullPointerException.class, () -> toImmutableLinkedHashSet((String[]) null));
    assertSame(Collections.emptySet(), toImmutableLinkedHashSet());
    final Set<String> actualSet2 = toImmutableLinkedHashSet("one", "two", "three");
    assertEquals(expectedSet, actualSet2);
    assertThrows(UnsupportedOperationException.class, () -> actualSet2.add("four"));
    final Iterator<String> itr2 = actualSet2.iterator();
    assertEquals("one", itr2.next());
    assertEquals("two", itr2.next());
    assertEquals("three", itr2.next());
  }

  /**
   * Tests the following methods:
   * <ul>
   *   <li>{@link Sets#toTreeSet(Object...)}</li>
   *   <li>{@link Sets#toImmutableTreeSet(Object...)}</li>
   *   <li>{@link Sets#toTreeSet(Comparator, Object...)}</li>
   *   <li>{@link Sets#toImmutableTreeSet(Comparator, Object...)}</li>
   * </ul>
   */
  @Test
  public void testToTreeSet() {
    final SortedSet<String> expectedSet = new TreeSet<>();
    Collections.addAll(expectedSet, "one", "two", "three");
    final Comparator<String> comparator = (s1, s2) -> -s1.compareTo(s2);

    // toTreeSet(Object...)
    assertThrows(NullPointerException.class, () -> toTreeSet((String[]) null));
    assertNotSame(Collections.emptySet(), toTreeSet());
    final SortedSet<String> actualSet1 = toTreeSet("one", "two", "three");
    assertEquals(expectedSet, actualSet1);
    actualSet1.add("four");
    final Iterator<String> itr1 = actualSet1.iterator();
    assertEquals("four", itr1.next());
    assertEquals("one", itr1.next());
    assertEquals("three", itr1.next());
    assertEquals("two", itr1.next());

    // toImmutableTreeSet(Object...)
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet((String[]) null));
    assertSame(Collections.emptySortedSet(), toImmutableTreeSet());
    final SortedSet<String> actualSet2 = toImmutableTreeSet("one", "two", "three");
    assertEquals(expectedSet, actualSet2);
    assertThrows(UnsupportedOperationException.class, () -> actualSet2.add("four"));
    final Iterator<String> itr2 = actualSet2.iterator();
    assertEquals("one", itr2.next());
    assertEquals("three", itr2.next());
    assertEquals("two", itr2.next());

    // toTreeSet(Comparator, Object...)
    assertThrows(NullPointerException.class, () -> toTreeSet((Comparator<?>) null));
    assertThrows(NullPointerException.class, () -> toTreeSet(comparator, (String[]) null));
    assertNotSame(Collections.emptySortedSet(), toTreeSet(comparator));
    final SortedSet<String> actualSet3 = toTreeSet(comparator, "one", "two", "three");
    assertEquals(expectedSet, actualSet3);
    actualSet3.add("four");
    final Iterator<String> itr3 = actualSet3.iterator();
    assertEquals("two", itr3.next());
    assertEquals("three", itr3.next());
    assertEquals("one", itr3.next());
    assertEquals("four", itr3.next());

    // toImmutableTreeSet(Comparator, Object...)
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet((Comparator<?>) null));
    assertThrows(NullPointerException.class, () -> toImmutableTreeSet(comparator, (String[]) null));
    assertSame(Collections.emptySortedSet(), toImmutableTreeSet(comparator));
    final SortedSet<String> actualSet4 = toImmutableTreeSet(comparator, "one", "two", "three");
    assertEquals(expectedSet, actualSet4);
    assertThrows(UnsupportedOperationException.class, () -> actualSet4.add("four"));
    final Iterator<String> itr4 = actualSet4.iterator();
    assertEquals("two", itr4.next());
    assertEquals("three", itr4.next());
    assertEquals("one", itr4.next());
  }

  /**
   * Tests the {@link Sets#addAll(Set, Object...)} method.
   */
  @Test
  public void testAddAll() {
    final Set<String> sampleSet = new LinkedHashSet<>();
    Collections.addAll(sampleSet, "one", "two", "three");

    // addAll(Set, Object...)
    assertThrows(NullPointerException.class, () -> addAll((Set<?>) null));
    final Set<String> actualSet = new HashSet<>();
    assertThrows(NullPointerException.class, () -> addAll(actualSet, (String[]) null));
    assertSame(actualSet, addAll(actualSet, "one", "two", "three"));
    assertEquals(sampleSet, actualSet);
    assertSame(actualSet, addAll(actualSet, "four"));
    assertTrue(actualSet.contains("one"));
    assertTrue(actualSet.contains("two"));
    assertTrue(actualSet.contains("three"));
    assertTrue(actualSet.contains("four"));
  }

}
