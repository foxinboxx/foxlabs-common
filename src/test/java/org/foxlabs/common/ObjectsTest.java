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
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.Assert;

/**
 * Tests for the {@link Objects} class.
 *
 * @author Fox Mulder
 */
public class ObjectsTest {

  /**
   * The reference to an original object for {@code ==} comparisons in tests.
   */
  private Object reference;

  /**
   * The reference to a {@link Number} for cast operations in tests.
   */
  @SuppressWarnings("unused")
  private Number number;

  /**
   * Tests the following methods:
   * <ul>
   *  <li>{@link Objects#require(Object)}</li>
   *  <li>{@link Objects#require(Object, String)}</li>
   *  <li>{@link Objects#require(Object, Predicate)}</li>
   *  <li>{@link Objects#require(Object, Predicate, String)}</li>
   *  <li>{@link Objects#require(Object, Predicate, Supplier)}</li>
   * </ul>
   */
  @Test
  public void testRequire() {
    // Objects.require(Object)
    Assert.assertTrue(Objects.require(reference = new Object()) == reference);
    Assert.assertEquals(null,
        Assert.assertThrows(NullPointerException.class,
            () -> Objects.require(null)).getMessage());
    // Objects.require(Object, String)
    Assert.assertTrue(Objects.require(reference = new Object(), "TEST") == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(NullPointerException.class,
            () -> Objects.require(null, "TEST")).getMessage());
    Assert.assertEquals("TEST",
        Assert.assertThrows(NullPointerException.class,
            () -> Objects.require(null, Objects.message(() -> "TEST"))).getMessage());
    // Objects.require(Object, Predicate)
    Assert.assertTrue(Objects.require(reference = new Object(), (o) -> true) == reference);
    Assert.assertEquals(null,
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Objects.require(null, (o) -> false)).getMessage());
    // Objects.require(Object, Predicate, String)
    Assert.assertTrue(Objects.require(reference = new Object(), (o) -> true, "TEST") == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Objects.require(null, (o) -> false, "TEST")).getMessage());
    Assert.assertEquals("TEST",
        Assert.assertThrows(IllegalArgumentException.class,
            () -> Objects.require(null, (o) -> false, Objects.message(() -> "TEST"))).getMessage());
    // Objects.require(Object, Predicate, Supplier)
    Assert.assertTrue(Objects.require(reference = new Object(), (o) -> true, RuntimeException::new) == reference);
    Assert.assertEquals("TEST",
        Assert.assertThrows(RuntimeException.class,
            () -> Objects.require(null, (o) -> false, () -> new RuntimeException("TEST"))).getMessage());
  }

  /**
   * Tests the {@link Objects#cast(Object)} method.
   */
  @Test
  public void testCast() {
    Assert.assertTrue((number = Objects.cast(reference = Integer.valueOf(1))) == reference);
    Assert.assertThrows(ClassCastException.class, () -> number = Objects.cast(""));
  }

  /**
   * Tests the {@link Objects#message(Supplier)} method.
   */
  @Test
  public void testMessage() {
    Assert.assertEquals("TEST", Objects.message(() -> "TEST").toString());
  }

}
