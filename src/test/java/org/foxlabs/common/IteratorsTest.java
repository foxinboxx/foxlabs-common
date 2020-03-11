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

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Iterators.*;

/**
 * Tests for methods of the {@link Iterators} class.
 *
 * @author Fox Mulder
 */
public class IteratorsTest {

  // Array iterators

  /**
   * Tests the {@link Iterators#toIterable(Object...)} method.
   */
  @Test
  public void test_toIterable() {
    assertThrows(NullPointerException.class, () -> toIterable((String[]) null));
    assertSame(Collections.emptyList(), toIterable());
    final Iterator<String> itr = toIterable("one", "two", "three").iterator();
    assertEquals("one", itr.next());
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertFalse(itr.hasNext());
    assertThrows(NoSuchElementException.class, () -> itr.next());
  }

  /**
   * Tests the {@link Iterators#toIterator(Object...)} method.
   */
  @Test
  public void test_toIterator() {
    assertThrows(NullPointerException.class, () -> toIterator((String[]) null));
    assertSame(Collections.emptyIterator(), toIterator());
    final Iterator<String> itr = toIterator("one", "two", "three");
    assertEquals("one", itr.next());
    assertEquals("two", itr.next());
    assertEquals("three", itr.next());
    assertFalse(itr.hasNext());
    assertThrows(NoSuchElementException.class, () -> itr.next());
  }

  // Mapping iterators

  /**
   * Tests the {@link Iterators#withMapper(Function, Iterable)} method.
   */
  @Test
  public void test_withMapper_iterable() {
    assertThrows(NullPointerException.class, () -> withMapper(null, (Iterable<?>) null));
    assertThrows(NullPointerException.class, () -> withMapper(Function.identity(), (Iterable<?>) null));
    final List<String> sampleIterable = Arrays.asList("one", "two", "three");
    final Iterator<String> itr = withMapper((s) -> s.toUpperCase(), sampleIterable).iterator();
    assertEquals("ONE", itr.next());
    assertEquals("TWO", itr.next());
    assertEquals("THREE", itr.next());
    assertFalse(itr.hasNext());
    assertThrows(NoSuchElementException.class, () -> itr.next());
  }

  /**
   * Tests the {@link Iterators#withMapper(Function, Iterator)} method.
   */
  @Test
  public void test_withMapper_iterator() {
    assertThrows(NullPointerException.class, () -> withMapper(null, (Iterator<?>) null));
    assertThrows(NullPointerException.class, () -> withMapper(Function.identity(), (Iterator<?>) null));
    final List<String> sampleIterable = Arrays.asList("one", "two", "three");
    final Iterator<String> itr = withMapper((s) -> s.toUpperCase(), sampleIterable.iterator());
    assertEquals("ONE", itr.next());
    assertEquals("TWO", itr.next());
    assertEquals("THREE", itr.next());
    assertFalse(itr.hasNext());
    assertThrows(NoSuchElementException.class, () -> itr.next());
  }

}
