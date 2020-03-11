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
    final Object sampleObject = new Object();
    assertSame(sampleObject, requireNonNull(sampleObject));
    assertEquals(null, assertThrows(NullPointerException.class,
        () -> requireNonNull(null)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireNonNull(Object, String)} method.
   */
  @Test
  public void test_requireNonNull_message() {
    final Object sampleObject = new Object();
    assertSame(sampleObject, requireNonNull(sampleObject, "object cannot be null"));
    assertEquals("object cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null, "object cannot be null")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireNonNull(Object, ExceptionProvider)} method.
   */
  @Test
  public void test_requireNonNull_exception() {
    final Object sampleObject = new Object();
    assertSame(sampleObject, requireNonNull(sampleObject, ExceptionProvider.ofNPE()));
    assertEquals(null, assertThrows(NullPointerException.class,
        () -> requireNonNull(null, ExceptionProvider.ofNPE())).getMessage());
    assertEquals("object cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null, ExceptionProvider.ofNPE("object cannot be null"))).getMessage());
    assertEquals("object cannot be null: sample", assertThrows(NullPointerException.class,
        () -> requireNonNull(null, ExceptionProvider.ofNPE("object cannot be null", "sample"))).getMessage());
    assertEquals("object cannot be null", assertThrows(IllegalStateException.class,
        () -> requireNonNull(null, (o) -> new IllegalStateException("object cannot be null"))).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate)} method.
   */
  @Test
  public void test_require() {
    final Integer sampleNumber = Integer.valueOf(10);
    assertSame(sampleNumber, require(sampleNumber, (n) -> true));
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(sampleNumber, (n) -> false)).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, String)} method.
   */
  @Test
  public void test_require_message() {
    final Integer sampleNumber = Integer.valueOf(10);
    assertSame(sampleNumber, require(sampleNumber, (n) -> true, "number cannot be illegal"));
    assertEquals("number cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(sampleNumber, (n) -> false, "number cannot be illegal")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(Object, Predicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_exception() {
    final Integer sampleNumber = Integer.valueOf(10);
    assertSame(sampleNumber, require(sampleNumber, (n) -> true, ExceptionProvider.ofIAE()));
    assertEquals("10", assertThrows(IllegalArgumentException.class,
        () -> require(sampleNumber, (o) -> false, ExceptionProvider.ofIAE())).getMessage());
    assertEquals("number cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(sampleNumber, (n) -> false,
            ExceptionProvider.ofIAE("number cannot be illegal"))).getMessage());
    assertEquals("number cannot be illegal: sample = 10", assertThrows(IllegalArgumentException.class,
        () -> require(sampleNumber, (n) -> false,
            ExceptionProvider.ofIAE("number cannot be illegal", "sample"))).getMessage());
    assertEquals("number cannot be illegal: 10", assertThrows(IllegalStateException.class,
        () -> require(sampleNumber, (n) -> false,
            (n) -> new IllegalStateException("number cannot be illegal: " + n))).getMessage());
  }

}
