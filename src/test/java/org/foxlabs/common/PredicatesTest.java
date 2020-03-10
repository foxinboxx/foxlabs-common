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
import org.junit.Assert;

import static org.foxlabs.common.Predicates.*;

/**
 * Tests for the {@link Predicates} class.
 *
 * @author Fox Mulder
 */
public class PredicatesTest {

  /**
   * Tests the following methods:
   * <ul>
   *   <li>{@link Predicates#requireNonNull(Object)}</li>
   *   <li>{@link Predicates#requireNonNull(Object, String)}</li>
   *   <li>{@link Predicates#requireNonNull(Object, ExceptionProvider)}</li>
   * </ul>
   */
  @Test
  public void testRequireNonNull() {
    final Object reference = new Object();
    // requireNonNull(Object)
    Assert.assertTrue(requireNonNull(reference) == reference);
    Assert.assertEquals(null, Assert.assertThrows(NullPointerException.class,
        () -> requireNonNull(null)).getMessage());
    // requireNonNull(Object, String)
    Assert.assertTrue(requireNonNull(reference, "TEST") == reference);
    Assert.assertEquals("TEST", Assert.assertThrows(NullPointerException.class,
        () -> requireNonNull(null, "TEST")).getMessage());
    // requireNonNull(Object, ExceptionProvider)
    Assert.assertTrue(requireNonNull(reference, ExceptionProvider.ofNPE()) == reference);
    Assert.assertEquals("TEST", Assert.assertThrows(NullPointerException.class,
            () -> requireNonNull(null, ExceptionProvider.ofNPE("TEST"))).getMessage());
  }

  /**
   * Tests the following methods:
   * <ul>
   *   <li>{@link Predicates#require(Object, Predicate)}</li>
   *   <li>{@link Predicates#require(Object, Predicate, String)}</li>
   *   <li>{@link Predicates#require(Object, Predicate, ExceptionProvider)}</li>
   * </ul>
   */
  @Test
  public void testRequire() {
    final Object reference = new Object();
    // require(Object, Predicate)
    Assert.assertTrue(require(reference, (o) -> true) == reference);
    Assert.assertEquals("null", Assert.assertThrows(IllegalArgumentException.class,
        () -> require(null, (o) -> false)).getMessage());
    // require(Object, Predicate, String)
    Assert.assertTrue(require(reference, (o) -> true, "test") == reference);
    Assert.assertEquals("test: null", Assert.assertThrows(IllegalArgumentException.class,
        () -> require(null, (o) -> false, "test")).getMessage());
    // require(Object, Predicate, ExceptionProvider)
    Assert.assertTrue(require(reference, (o) -> true, ExceptionProvider.ofIAE()) == reference);
    Assert.assertEquals("test: object = null", Assert.assertThrows(IllegalArgumentException.class,
        () -> require(null, (o) -> false, ExceptionProvider.ofIAE("test", "object"))).getMessage());
  }

}
