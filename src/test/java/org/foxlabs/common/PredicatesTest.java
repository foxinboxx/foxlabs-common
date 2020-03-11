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

import java.util.function.Predicate;
import java.util.function.IntPredicate;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Predicates.*;

/**
 * Tests for methods of the {@link Predicates} class.
 *
 * @author Fox Mulder
 */
public class PredicatesTest {

  // Simple checks

  // Object

  /**
   * Tests the {@link Predicates#requireNonNull(Object)} method.
   */
  @Test
  public void test_requireNonNull() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, requireNonNull(validSample));
    // NPE: null
    assertEquals(null, assertThrows(NullPointerException.class,
        () -> requireNonNull(null)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireNonNull(Object, String)} method.
   */
  @Test
  public void test_requireNonNull_message() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, requireNonNull(validSample, "object cannot be null"));
    // NPE: message
    assertEquals("object cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            "object cannot be null")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireNonNull(Object, ExceptionProvider)} method.
   */
  @Test
  public void test_requireNonNull_exception() {
    // OK: object
    final Object validObject = new Object();
    assertSame(validObject, requireNonNull(validObject, ExceptionProvider.ofNPE()));
    // NPE: null
    assertEquals(null, assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            ExceptionProvider.ofNPE())).getMessage());
    // NPE: message
    assertEquals("object cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            ExceptionProvider.ofNPE("object cannot be null"))).getMessage());
    // NPE: message: identifier
    assertEquals("object cannot be null: sample", assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            ExceptionProvider.ofNPE("object cannot be null", "sample"))).getMessage());
    // ISE: message
    assertEquals("object cannot be null", assertThrows(IllegalStateException.class,
        () -> requireNonNull(null,
            (o) -> new IllegalStateException("object cannot be null"))).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate)} method.
   */
  @Test
  public void test_require() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, require(validSample, (o) -> true));
    // IAE: object
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, String)} method.
   */
  @Test
  public void test_require_message() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, require(validSample, (o) -> true, "object cannot be illegal"));
    // IAE: message: object
    assertEquals("object cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            "object cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_exception() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, require(validSample, (o) -> true, ExceptionProvider.ofIAE()));
    // IAE: object
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: object
    assertEquals("object cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            ExceptionProvider.ofIAE("object cannot be illegal"))).getMessage());
    // IAE: message: identifier = object
    assertEquals("object cannot be illegal: sample = 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            ExceptionProvider.ofIAE("object cannot be illegal", "sample"))).getMessage());
    // ISE: message
    assertEquals("object cannot be illegal", assertThrows(IllegalStateException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            (n) -> new IllegalStateException("object cannot be illegal"))).getMessage());
  }

  // int

  /**
   * Tests the {@link Predicates#require(int, IntPredicate)} method.
   */
  @Test
  public void test_require_int() {
    // OK: int
    assertEquals(10, require(10, (int n) -> true));
    // IAE: int
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(int, IntPredicate, String)} method.
   */
  @Test
  public void test_require_int_message() {
    // OK: int
    assertEquals(10, require(10, (int n) -> true, "object cannot be illegal"));
    // IAE: message: int
    assertEquals("int cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            "int cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(int, IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_int_exception() {
    // OK: int
    assertEquals(10, require(10, (int n) -> true, ExceptionProvider.ofIAE()));
    // IAE: int
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: int
    assertEquals("long cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal"))).getMessage());
    // IAE: message: identifier = int
    assertEquals("long cannot be illegal: sample = 10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal", "sample"))).getMessage());
    // ISE: message
    assertEquals("long cannot be illegal", assertThrows(IllegalStateException.class,
        () -> require(10, (int n) -> false,
            (n) -> new IllegalStateException("long cannot be illegal"))).getMessage());
  }

  // long

  /**
   * Tests the {@link Predicates#require(long, LongPredicate)} method.
   */
  @Test
  public void test_require_long() {
    // OK: long
    assertEquals(10L, require(10L, (long n) -> true));
    // IAE: long
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(long, LongPredicate, String)} method.
   */
  @Test
  public void test_require_long_message() {
    // OK: long
    assertEquals(10L, require(10L, (long n) -> true, "long cannot be illegal"));
    // IAE: message: long
    assertEquals("long cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            "long cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(long, LongPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_long_exception() {
    // OK: long
    assertEquals(10L, require(10L, (long n) -> true, ExceptionProvider.ofIAE()));
    // IAE: long
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: long
    assertEquals("long cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal"))).getMessage());
    // IAE: message: identifier = long
    assertEquals("long cannot be illegal: sample = 10", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal", "sample"))).getMessage());
    // ISE: message
    assertEquals("long cannot be illegal", assertThrows(IllegalStateException.class,
        () -> require(10L, (long n) -> false,
            (n) -> new IllegalStateException("long cannot be illegal"))).getMessage());
  }

  // double

  /**
   * Tests the {@link Predicates#require(double, DoublePredicate)} method.
   */
  @Test
  public void test_require_double() {
    // OK: double
    assertEquals(10., require(10., (double n) -> true), .0);
    // NPE: double
    assertEquals("10.0", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(double, DoublePredicate, String)} method.
   */
  @Test
  public void test_require_double_message() {
    // OK: double
    assertEquals(10., require(10., (double n) -> true, "double cannot be illegal"), .0);
    // NPE: message: double
    assertEquals("double cannot be illegal: 10.0", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            "double cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(double, DoublePredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_double_exception() {
    // OK: double
    assertEquals(10., require(10., (double n) -> true, ExceptionProvider.ofIAE()), .0);
    // NPE: double
    assertEquals("10.0", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // NPE: message: double
    assertEquals("double cannot be illegal: 10.0", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            ExceptionProvider.ofIAE("double cannot be illegal"))).getMessage());
    // NPE: message: identifier = double
    assertEquals("double cannot be illegal: sample = 10.0", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            ExceptionProvider.ofIAE("double cannot be illegal", "sample"))).getMessage());
    // ISE: message
    assertEquals("double cannot be illegal", assertThrows(IllegalStateException.class,
        () -> require(10., (double n) -> false,
            (n) -> new IllegalStateException("double cannot be illegal"))).getMessage());
  }

  // Array checks

  // Object[]

  /**
   * Tests the {@link Predicates#requireAllNonNull(Object[])} method.
   */
  @Test
  public void test_requireAllNonNull() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAllNonNull(emptySample));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAllNonNull(validSample));
    // NPE: [index]
    assertEquals("[1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"})).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAllNonNull(Object[], String)} method.
   */
  @Test
  public void test_requireAllNonNull_message() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample, "elements cannot be null"));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAllNonNull(emptySample, "elements cannot be null"));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAllNonNull(validSample, "elements cannot be null"));
    // NPE: message: [index]
    assertEquals("elements cannot be null: [1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            "elements cannot be null")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAllNonNull(Object[], ExceptionProvider)} method.
   */
  @Test
  public void test_requireAllNonNull_exception() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample, ExceptionProvider.OfSequence.ofNPE()));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAllNonNull(emptySample, ExceptionProvider.OfSequence.ofNPE()));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAllNonNull(validSample, ExceptionProvider.OfSequence.ofNPE()));
    // NPE: [index]
    assertEquals("[1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            ExceptionProvider.OfSequence.ofNPE())).getMessage());
    // NPE: message: [index]
    assertEquals("elements cannot be null: [1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            ExceptionProvider.OfSequence.ofNPE("elements cannot be null"))).getMessage());
    // NPE: message: identifier[index]
    assertEquals("elements cannot be null: sample[1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            ExceptionProvider.OfSequence.ofNPE("elements cannot be null", "sample"))).getMessage());
    // ISE: message
    assertEquals("elements cannot be null", assertThrows(IllegalStateException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            (a, i, e) -> new IllegalStateException("elements cannot be null"))).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Object[], Predicate)} method.
   */
  @Test
  public void test_requireAll() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAll(emptySample, (o) -> true));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAll(validSample, (o) -> true));
    // IAE: [index]
    assertEquals("[2] = three", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Object[], Predicate, String)} method.
   */
  @Test
  public void test_requireAll_message() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true, "elements cannot be invalid"));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAll(emptySample, (o) -> true, "elements cannot be invalid"));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAll(validSample, (o) -> true, "elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("elements cannot be invalid: [2] = three", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            "elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Object[], Predicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_exception() {
    // OK: null
    final Object[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final Object[] emptySample = new Object[0];
    assertSame(emptySample, requireAll(emptySample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final Object[] validSample = new Object[]{"one", "two", "three"};
    assertSame(validSample, requireAll(validSample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = three", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("elements cannot be invalid: [2] = three", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE("elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("elements cannot be invalid: sample[2] = three", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE("elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            (a, i, e) -> new IllegalStateException("elements cannot be invalid"))).getMessage());
  }

  // byte[]

  /**
   * Tests the {@link Predicates#requireAll(byte[], IntPredicate)} method.
   */
  @Test
  public void test_requireAll_byte() {
    // OK: null
    final byte[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final byte[] emptySample = new byte[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final byte[] validSample = new byte[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(byte[], IntPredicate, String)} method.
   */
  @Test
  public void test_requireAll_byte_message() {
    // OK: null
    final byte[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "byte elements cannot be invalid"));
    // OK: []
    final byte[] emptySample = new byte[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "byte elements cannot be invalid"));
    // OK: [...]
    final byte[] validSample = new byte[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, "byte elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("byte elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            "byte elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(byte[], IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_byte_exception() {
    // OK: null
    final byte[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final byte[] emptySample = new byte[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final byte[] validSample = new byte[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("byte elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("byte elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("byte elements cannot be invalid: sample[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("byte elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("byte elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            (a, i, e) -> new IllegalStateException("byte elements cannot be invalid"))).getMessage());
  }

  // short[]

  /**
   * Tests the {@link Predicates#requireAll(short[], IntPredicate)} method.
   */
  @Test
  public void test_requireAll_short() {
    // OK: null
    final short[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final short[] emptySample = new short[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final short[] validSample = new short[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(short[], IntPredicate, String)} method.
   */
  @Test
  public void test_requireAll_short_message() {
    // OK: null
    final short[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "short elements cannot be invalid"));
    // OK: []
    final short[] emptySample = new short[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "short elements cannot be invalid"));
    // OK: [...]
    final short[] validSample = new short[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, "short elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("short elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            "short elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(short[], IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_short_exception() {
    // OK: null
    final short[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final short[] emptySample = new short[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final short[] validSample = new short[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("short elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("short elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("short elements cannot be invalid: sample[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("short elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("short elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            (a, i, e) -> new IllegalStateException("short elements cannot be invalid"))).getMessage());
  }

  // int[]

  /**
   * Tests the {@link Predicates#requireAll(int[], IntPredicate)} method.
   */
  @Test
  public void test_requireAll_int() {
    // OK: null
    final int[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final int[] emptySample = new int[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final int[] validSample = new int[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(int[], IntPredicate, String)} method.
   */
  @Test
  public void test_requireAll_int_message() {
    // OK: null
    final int[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "int elements cannot be invalid"));
    // OK: []
    final int[] emptySample = new int[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "int elements cannot be invalid"));
    // OK: [...]
    final int[] validSample = new int[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, "int elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("int elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            "int elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(int[], IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_int_exception() {
    // OK: null
    final int[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final int[] emptySample = new int[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final int[] validSample = new int[]{1, 2, 3};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("int elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("int elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("int elements cannot be invalid: sample[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("int elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("int elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            (a, i, e) -> new IllegalStateException("int elements cannot be invalid"))).getMessage());
  }

  // long[]

  /**
   * Tests the {@link Predicates#requireAll(long[], LongPredicate)} method.
   */
  @Test
  public void test_requireAll_long() {
    // OK: null
    final long[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final long[] emptySample = new long[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final long[] validSample = new long[]{1L, 2L, 3L};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(long[], LongPredicate, String)} method.
   */
  @Test
  public void test_requireAll_long_message() {
    // OK: null
    final long[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "long elements cannot be invalid"));
    // OK: []
    final long[] emptySample = new long[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "long elements cannot be invalid"));
    // OK: [...]
    final long[] validSample = new long[]{1L, 2L, 3L};
    assertSame(validSample, requireAll(validSample, (n) -> true, "long elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("long elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            "long elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(long[], LongPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_long_exception() {
    // OK: null
    final long[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final long[] emptySample = new long[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final long[] validSample = new long[]{1L, 2L, 3L};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("long elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            ExceptionProvider.OfSequence.ofIAE("long elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("long elements cannot be invalid: sample[2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            ExceptionProvider.OfSequence.ofIAE("long elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("long elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            (a, i, e) -> new IllegalStateException("long elements cannot be invalid"))).getMessage());
  }

  // float[]

  /**
   * Tests the {@link Predicates#requireAll(float[], DoublePredicate)} method.
   */
  @Test
  public void test_requireAll_float() {
    // OK: null
    final float[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final float[] emptySample = new float[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final float[] validSample = new float[]{1.f, 2.f, 3.f};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3L}, (n) -> n < 3.f)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(float[], DoublePredicate, String)} method.
   */
  @Test
  public void test_requireAll_float_message() {
    // OK: null
    final float[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "float elements cannot be invalid"));
    // OK: []
    final float[] emptySample = new float[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "float elements cannot be invalid"));
    // OK: [...]
    final float[] validSample = new float[]{1.f, 2.f, 3.f};
    assertSame(validSample, requireAll(validSample, (n) -> true, "float elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("float elements cannot be invalid: [2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            "float elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(float[], DoublePredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_float_exception() {
    // OK: null
    final float[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final float[] emptySample = new float[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final float[] validSample = new float[]{1.f, 2.f, 3.f};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("float elements cannot be invalid: [2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            ExceptionProvider.OfSequence.ofIAE("float elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("float elements cannot be invalid: sample[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            ExceptionProvider.OfSequence.ofIAE("float elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("float elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            (a, i, e) -> new IllegalStateException("float elements cannot be invalid"))).getMessage());
  }

  // double[]

  /**
   * Tests the {@link Predicates#requireAll(double[], DoublePredicate)} method.
   */
  @Test
  public void test_requireAll_double() {
    // OK: null
    final double[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true));
    // OK: []
    final double[] emptySample = new double[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true));
    // OK: [...]
    final double[] validSample = new double[]{1., 2., 3.};
    assertSame(validSample, requireAll(validSample, (n) -> true));
    // IAE: [index]
    assertEquals("[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3L}, (n) -> n < 3.)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(double[], DoublePredicate, String)} method.
   */
  @Test
  public void test_requireAll_double_message() {
    // OK: null
    final double[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, "double elements cannot be invalid"));
    // OK: []
    final double[] emptySample = new double[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, "double elements cannot be invalid"));
    // OK: [...]
    final double[] validSample = new double[]{1., 2., 3.};
    assertSame(validSample, requireAll(validSample, (n) -> true, "double elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("double elements cannot be invalid: [2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            "double elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(double[], DoublePredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_double_exception() {
    // OK: null
    final double[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final double[] emptySample = new double[0];
    assertSame(emptySample, requireAll(emptySample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final double[] validSample = new double[]{1., 2., 3.};
    assertSame(validSample, requireAll(validSample, (n) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("double elements cannot be invalid: [2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            ExceptionProvider.OfSequence.ofIAE("double elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("double elements cannot be invalid: sample[2] = 3.0", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            ExceptionProvider.OfSequence.ofIAE("double elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("double elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            (a, i, e) -> new IllegalStateException("double elements cannot be invalid"))).getMessage());
  }

  // char[]

  /**
   * Tests the {@link Predicates#requireAll(char[], IntPredicate)} method.
   */
  @Test
  public void test_requireAll_char() {
    // OK: null
    final char[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (c) -> true));
    // OK: []
    final char[] emptySample = new char[0];
    assertSame(emptySample, requireAll(emptySample, (c) -> true));
    // OK: [...]
    final char[] validSample = new char[]{'a', 'b', 'c'};
    assertSame(validSample, requireAll(validSample, (c) -> true));
    // IAE: [index]
    assertEquals("[2] = c", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c')).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(char[], IntPredicate, String)} method.
   */
  @Test
  public void test_requireAll_char_message() {
    // OK: null
    final char[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (c) -> true, "char elements cannot be invalid"));
    // OK: []
    final char[] emptySample = new char[0];
    assertSame(emptySample, requireAll(emptySample, (c) -> true, "char elements cannot be invalid"));
    // OK: [...]
    final char[] validSample = new char[]{'a', 'b', 'c'};
    assertSame(validSample, requireAll(validSample, (c) -> true, "char elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("char elements cannot be invalid: [2] = c", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            "char elements cannot be invalid")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(char[], IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_char_exception() {
    // OK: null
    final char[] nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (c) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final char[] emptySample = new char[0];
    assertSame(emptySample, requireAll(emptySample, (c) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final char[] validSample = new char[]{'a', 'b', 'c'};
    assertSame(validSample, requireAll(validSample, (c) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("[2] = c", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("char elements cannot be invalid: [2] = c", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            ExceptionProvider.OfSequence.ofIAE("char elements cannot be invalid"))).getMessage());
    // IAE: message: identifier[index] = element
    assertEquals("char elements cannot be invalid: sample[2] = c", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            ExceptionProvider.OfSequence.ofIAE("char elements cannot be invalid", "sample"))).getMessage());
    // ISE: message
    assertEquals("char elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            (a, i, e) -> new IllegalStateException("char elements cannot be invalid"))).getMessage());
  }

}
