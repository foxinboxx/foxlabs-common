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

import java.util.function.Supplier;

import org.junit.Test;
import org.junit.Assert;

/**
 * Tests for the {@link Predicates} class.
 *
 * @author Fox Mulder
 */
public class PredicatesTest {

  /**
   * The reference to an original object for {@code ==} comparisons in tests.
   */
  private Object reference;

  /**
   * Tests the following methods:
   * <ul>
   *  <li>{@link Predicates#requireNonNull(Object)}</li>
   *  <li>{@link Predicates#requireNonNull(Object, Object)}</li>
   * </ul>
   */
  @Test
  public void testRequireNonNull() {
    // Predicates.requireNonNull(Object)
    Assert.assertTrue(Predicates.requireNonNull(reference = new Object()) == reference);
    Assert.assertEquals(null,
        Assert.assertThrows(NullPointerException.class,
            () -> Predicates.requireNonNull(null)).getMessage());
    // Predicates.requireNonNull(Object, Object)
    Assert.assertTrue(Predicates.requireNonNull(reference = new Object(), null) == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(NullPointerException.class,
            () -> Predicates.requireNonNull(null, "TEST")).getMessage());
    Assert.assertEquals("10",
        Assert.assertThrows(NullPointerException.class,
            () -> Predicates.requireNonNull(null, Integer.valueOf(10))).getMessage());
    Assert.assertEquals("TEST",
        Assert.assertThrows(NullPointerException.class,
            () -> Predicates.requireNonNull(null, (Supplier<String>) () -> "TEST")).getMessage());
  }

  /**
   * Tests the following methods:
   * <ul>
   *  <li>{@link Predicates#require(Object, Predicate)}</li>
   *  <li>{@link Predicates#require(Object, Predicate, Object)}</li>
   *  <li>{@link Predicates#require(Object, Predicate, Supplier)}</li>
   * </ul>
   */
  @Test
  public void testRequire() {
    // Predicates.require(Object, Predicate)
    Assert.assertTrue(Predicates.require(reference = new Object(), (o) -> true) == reference);
    Assert.assertEquals("null",
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Predicates.require(null, (o) -> false)).getMessage());
    // Predicates.require(Object, Predicate, Object)
    Assert.assertTrue(Predicates.require(reference = new Object(), (o) -> true, "TEST") == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Predicates.require(null, (o) -> false, "TEST")).getMessage());
    Assert.assertEquals("TEST",
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Predicates.require(null, (o) -> false, Objects.message(() -> "TEST"))).getMessage());
    // Predicates.require(Object, Predicate, Supplier)
    Assert.assertTrue(Predicates.require(reference = new Object(), (o) -> true, RuntimeException::new) == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(RuntimeException.class,
            () -> Predicates.require(null, (o) -> false, () -> new RuntimeException("TEST"))).getMessage());
  }

}
