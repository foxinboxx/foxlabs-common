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
    assertSame(validSample, require(validSample, (n) -> true));
    // IAE: object
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (n) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, String)} method.
   */
  @Test
  public void test_require_message() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, require(validSample, (n) -> true, "object cannot be illegal"));
    // IAE: message: object
    assertEquals("object cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (n) -> false,
            "object cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_exception() {
    // OK: object
    final Object validSample = new Object();
    assertSame(validSample, require(validSample, (n) -> true, ExceptionProvider.ofIAE()));
    // IAE: object
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: object
    assertEquals("object cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (n) -> false,
            ExceptionProvider.ofIAE("object cannot be illegal"))).getMessage());
    // IAE: message: identifier = object
    assertEquals("object cannot be illegal: sample = 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (n) -> false,
            ExceptionProvider.ofIAE("object cannot be illegal", "sample"))).getMessage());
    // ISE: message
    assertEquals("object cannot be illegal", assertThrows(IllegalStateException.class,
        () -> require(Integer.valueOf(10), (n) -> false,
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
        () -> requireAllNonNull(new String[]{"one", null, "three"})).getMessage());
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

}
