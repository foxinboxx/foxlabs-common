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

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;
import java.util.regex.Pattern;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Predicates.*;

/**
 * Tests for methods and constants of the {@link Predicates} class.
 *
 * @author Fox Mulder
 */
public class PredicatesTest {

  // Predicates

  /**
   * Tests predicates constants.
   */
  @Test
  public void test__predicates() {
    // Object predicates
    assertTrue(OBJECT_NULL.test(null));
    assertFalse(OBJECT_NULL.test(new Object()));
    assertFalse(OBJECT_NON_NULL.test(null));
    assertTrue(OBJECT_NON_NULL.test(new Object()));
    // Primitive int predicates
    assertTrue(INT_EQ_ZERO.test(0));
    assertFalse(INT_EQ_ZERO.test(1));
    assertFalse(INT_EQ_ZERO.test(-1));
    assertFalse(INT_NE_ZERO.test(0));
    assertTrue(INT_NE_ZERO.test(1));
    assertTrue(INT_NE_ZERO.test(-1));
    assertFalse(INT_POSITIVE.test(0));
    assertFalse(INT_POSITIVE.test(-1));
    assertTrue(INT_POSITIVE.test(1));
    assertTrue(INT_POSITIVE_OR_ZERO.test(0));
    assertFalse(INT_POSITIVE_OR_ZERO.test(-1));
    assertTrue(INT_POSITIVE_OR_ZERO.test(1));
    assertFalse(INT_NEGATIVE.test(0));
    assertTrue(INT_NEGATIVE.test(-1));
    assertFalse(INT_NEGATIVE.test(1));
    assertTrue(INT_NEGATIVE_OR_ZERO.test(0));
    assertTrue(INT_NEGATIVE_OR_ZERO.test(-1));
    assertFalse(INT_NEGATIVE_OR_ZERO.test(1));
    // Integer predicates
    assertFalse(OBJECT_INT_EQ_ZERO.test(null));
    assertTrue(OBJECT_INT_EQ_ZERO.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_EQ_ZERO.test(Integer.valueOf(1)));
    assertFalse(OBJECT_INT_EQ_ZERO.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_EQ_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_INT_EQ_ZERO_OR_NULL.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_EQ_ZERO_OR_NULL.test(Integer.valueOf(1)));
    assertFalse(OBJECT_INT_EQ_ZERO_OR_NULL.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_NE_ZERO.test(null));
    assertFalse(OBJECT_INT_NE_ZERO.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NE_ZERO.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_NE_ZERO.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_NE_ZERO_OR_NULL.test(null));
    assertFalse(OBJECT_INT_NE_ZERO_OR_NULL.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NE_ZERO_OR_NULL.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_NE_ZERO_OR_NULL.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_POSITIVE.test(null));
    assertFalse(OBJECT_INT_POSITIVE.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_POSITIVE.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_POSITIVE.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_POSITIVE_OR_NULL.test(null));
    assertFalse(OBJECT_INT_POSITIVE_OR_NULL.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_POSITIVE_OR_NULL.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_POSITIVE_OR_NULL.test(Integer.valueOf(1)));
    assertFalse(OBJECT_INT_POSITIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_INT_POSITIVE_OR_ZERO.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_POSITIVE_OR_ZERO.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_POSITIVE_OR_ZERO.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_POSITIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_INT_POSITIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(0)));
    assertFalse(OBJECT_INT_POSITIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(-1)));
    assertTrue(OBJECT_INT_POSITIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(1)));
    assertFalse(OBJECT_INT_NEGATIVE.test(null));
    assertFalse(OBJECT_INT_NEGATIVE.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NEGATIVE.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_NEGATIVE.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_NEGATIVE_OR_NULL.test(null));
    assertFalse(OBJECT_INT_NEGATIVE_OR_NULL.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NEGATIVE_OR_NULL.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_NEGATIVE_OR_NULL.test(Integer.valueOf(1)));
    assertFalse(OBJECT_INT_NEGATIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_INT_NEGATIVE_OR_ZERO.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NEGATIVE_OR_ZERO.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_NEGATIVE_OR_ZERO.test(Integer.valueOf(1)));
    assertTrue(OBJECT_INT_NEGATIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_INT_NEGATIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(0)));
    assertTrue(OBJECT_INT_NEGATIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(-1)));
    assertFalse(OBJECT_INT_NEGATIVE_OR_ZERO_OR_NULL.test(Integer.valueOf(1)));
    // Primitive long predicates
    assertTrue(LONG_EQ_ZERO.test(0L));
    assertFalse(LONG_EQ_ZERO.test(1L));
    assertFalse(LONG_EQ_ZERO.test(-1L));
    assertFalse(LONG_NE_ZERO.test(0L));
    assertTrue(LONG_NE_ZERO.test(1L));
    assertTrue(LONG_NE_ZERO.test(-1L));
    assertFalse(LONG_POSITIVE.test(0L));
    assertFalse(LONG_POSITIVE.test(-1L));
    assertTrue(LONG_POSITIVE.test(1L));
    assertTrue(LONG_POSITIVE_OR_ZERO.test(0L));
    assertFalse(LONG_POSITIVE_OR_ZERO.test(-1L));
    assertTrue(LONG_POSITIVE_OR_ZERO.test(1L));
    assertFalse(LONG_NEGATIVE.test(0L));
    assertTrue(LONG_NEGATIVE.test(-1L));
    assertFalse(LONG_NEGATIVE.test(1L));
    assertTrue(LONG_NEGATIVE_OR_ZERO.test(0L));
    assertTrue(LONG_NEGATIVE_OR_ZERO.test(-1L));
    assertFalse(LONG_NEGATIVE_OR_ZERO.test(1L));
    // Long predicates
    assertFalse(OBJECT_LONG_EQ_ZERO.test(null));
    assertTrue(OBJECT_LONG_EQ_ZERO.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_EQ_ZERO.test(Long.valueOf(1L)));
    assertFalse(OBJECT_LONG_EQ_ZERO.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_EQ_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_LONG_EQ_ZERO_OR_NULL.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_EQ_ZERO_OR_NULL.test(Long.valueOf(1L)));
    assertFalse(OBJECT_LONG_EQ_ZERO_OR_NULL.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_NE_ZERO.test(null));
    assertFalse(OBJECT_LONG_NE_ZERO.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NE_ZERO.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_NE_ZERO.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_NE_ZERO_OR_NULL.test(null));
    assertFalse(OBJECT_LONG_NE_ZERO_OR_NULL.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NE_ZERO_OR_NULL.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_NE_ZERO_OR_NULL.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_POSITIVE.test(null));
    assertFalse(OBJECT_LONG_POSITIVE.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_POSITIVE.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_POSITIVE.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_POSITIVE_OR_NULL.test(null));
    assertFalse(OBJECT_LONG_POSITIVE_OR_NULL.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_POSITIVE_OR_NULL.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_POSITIVE_OR_NULL.test(Long.valueOf(1L)));
    assertFalse(OBJECT_LONG_POSITIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_LONG_POSITIVE_OR_ZERO.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_POSITIVE_OR_ZERO.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_POSITIVE_OR_ZERO.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_POSITIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_LONG_POSITIVE_OR_ZERO_OR_NULL.test(Long.valueOf(0L)));
    assertFalse(OBJECT_LONG_POSITIVE_OR_ZERO_OR_NULL.test(Long.valueOf(-1L)));
    assertTrue(OBJECT_LONG_POSITIVE_OR_ZERO_OR_NULL.test(Long.valueOf(1L)));
    assertFalse(OBJECT_LONG_NEGATIVE.test(null));
    assertFalse(OBJECT_LONG_NEGATIVE.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NEGATIVE.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_NEGATIVE.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_NULL.test(null));
    assertFalse(OBJECT_LONG_NEGATIVE_OR_NULL.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_NULL.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_NEGATIVE_OR_NULL.test(Long.valueOf(1L)));
    assertFalse(OBJECT_LONG_NEGATIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_ZERO.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_ZERO.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_NEGATIVE_OR_ZERO.test(Long.valueOf(1L)));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_ZERO_OR_NULL.test(Long.valueOf(0L)));
    assertTrue(OBJECT_LONG_NEGATIVE_OR_ZERO_OR_NULL.test(Long.valueOf(-1L)));
    assertFalse(OBJECT_LONG_NEGATIVE_OR_ZERO_OR_NULL.test(Long.valueOf(1L)));
    // Float predicates
    assertFalse(OBJECT_FLOAT_EQ_ZERO.test(null));
    assertTrue(OBJECT_FLOAT_EQ_ZERO.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_EQ_ZERO.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_EQ_ZERO.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_EQ_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_FLOAT_EQ_ZERO_OR_NULL.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_EQ_ZERO_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_EQ_ZERO_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NE_ZERO.test(null));
    assertFalse(OBJECT_FLOAT_NE_ZERO.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NE_ZERO.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_NE_ZERO.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_NE_ZERO_OR_NULL.test(null));
    assertFalse(OBJECT_FLOAT_NE_ZERO_OR_NULL.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NE_ZERO_OR_NULL.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_NE_ZERO_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_POSITIVE.test(null));
    assertFalse(OBJECT_FLOAT_POSITIVE.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_POSITIVE.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_NULL.test(null));
    assertFalse(OBJECT_FLOAT_POSITIVE_OR_NULL.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_POSITIVE_OR_NULL.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_POSITIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_ZERO.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_POSITIVE_OR_ZERO.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_ZERO.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_ZERO_OR_NULL.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_POSITIVE_OR_ZERO_OR_NULL.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_POSITIVE_OR_ZERO_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE.test(null));
    assertFalse(OBJECT_FLOAT_NEGATIVE.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_NULL.test(null));
    assertFalse(OBJECT_FLOAT_NEGATIVE_OR_NULL.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_ZERO.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_ZERO.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE_OR_ZERO.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_ZERO_OR_NULL.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_NEGATIVE_OR_ZERO_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NEGATIVE_OR_ZERO_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_NAN.test(null));
    assertTrue(OBJECT_FLOAT_NAN.test(Float.valueOf(Float.NaN)));
    assertFalse(OBJECT_FLOAT_NAN.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_NAN.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NAN.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_NAN.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_NAN.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_FLOAT_NAN_OR_NULL.test(null));
    assertTrue(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(Float.NaN)));
    assertFalse(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_NAN_OR_NULL.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_FINITE.test(null));
    assertFalse(OBJECT_FLOAT_FINITE.test(Float.valueOf(Float.NaN)));
    assertTrue(OBJECT_FLOAT_FINITE.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_FINITE.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_FINITE.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_FINITE.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_FINITE.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_FLOAT_FINITE_OR_NULL.test(null));
    assertFalse(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(Float.NaN)));
    assertTrue(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(.0f)));
    assertTrue(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(-1.f)));
    assertTrue(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(1.f)));
    assertFalse(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_FINITE_OR_NULL.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertFalse(OBJECT_FLOAT_INFINITE.test(null));
    assertFalse(OBJECT_FLOAT_INFINITE.test(Float.valueOf(Float.NaN)));
    assertFalse(OBJECT_FLOAT_INFINITE.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_INFINITE.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_INFINITE.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_INFINITE.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertTrue(OBJECT_FLOAT_INFINITE.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_FLOAT_INFINITE_OR_NULL.test(null));
    assertFalse(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(Float.NaN)));
    assertFalse(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(.0f)));
    assertFalse(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(-1.f)));
    assertFalse(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(1.f)));
    assertTrue(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(Float.POSITIVE_INFINITY)));
    assertTrue(OBJECT_FLOAT_INFINITE_OR_NULL.test(Float.valueOf(Float.NEGATIVE_INFINITY)));
    // Primitive double predicates
    assertTrue(DOUBLE_EQ_ZERO.test(.0d));
    assertFalse(DOUBLE_EQ_ZERO.test(1.d));
    assertFalse(DOUBLE_EQ_ZERO.test(-1.d));
    assertFalse(DOUBLE_NE_ZERO.test(.0d));
    assertTrue(DOUBLE_NE_ZERO.test(1.d));
    assertTrue(DOUBLE_NE_ZERO.test(-1.d));
    assertFalse(DOUBLE_POSITIVE.test(.0d));
    assertFalse(DOUBLE_POSITIVE.test(-1.d));
    assertTrue(DOUBLE_POSITIVE.test(1.d));
    assertTrue(DOUBLE_POSITIVE_OR_ZERO.test(.0d));
    assertFalse(DOUBLE_POSITIVE_OR_ZERO.test(-1.d));
    assertTrue(DOUBLE_POSITIVE_OR_ZERO.test(1.d));
    assertFalse(DOUBLE_NEGATIVE.test(.0d));
    assertTrue(DOUBLE_NEGATIVE.test(-1.d));
    assertFalse(DOUBLE_NEGATIVE.test(1.d));
    assertTrue(DOUBLE_NEGATIVE_OR_ZERO.test(.0d));
    assertTrue(DOUBLE_NEGATIVE_OR_ZERO.test(-1.d));
    assertFalse(DOUBLE_NEGATIVE_OR_ZERO.test(1.d));
    assertTrue(DOUBLE_NAN.test(Float.NaN));
    assertFalse(DOUBLE_NAN.test(.0d));
    assertFalse(DOUBLE_NAN.test(-1.d));
    assertFalse(DOUBLE_NAN.test(1.d));
    assertFalse(DOUBLE_NAN.test(Float.POSITIVE_INFINITY));
    assertFalse(DOUBLE_NAN.test(Float.NEGATIVE_INFINITY));
    assertFalse(DOUBLE_FINITE.test(Float.NaN));
    assertTrue(DOUBLE_FINITE.test(.0d));
    assertTrue(DOUBLE_FINITE.test(-1.d));
    assertTrue(DOUBLE_FINITE.test(1.d));
    assertFalse(DOUBLE_FINITE.test(Float.POSITIVE_INFINITY));
    assertFalse(DOUBLE_FINITE.test(Float.NEGATIVE_INFINITY));
    assertFalse(DOUBLE_INFINITE.test(Float.NaN));
    assertFalse(DOUBLE_INFINITE.test(.0d));
    assertFalse(DOUBLE_INFINITE.test(-1.d));
    assertFalse(DOUBLE_INFINITE.test(1.d));
    assertTrue(DOUBLE_INFINITE.test(Float.POSITIVE_INFINITY));
    assertTrue(DOUBLE_INFINITE.test(Float.NEGATIVE_INFINITY));
    // Double predicates
    assertFalse(OBJECT_DOUBLE_EQ_ZERO.test(null));
    assertTrue(OBJECT_DOUBLE_EQ_ZERO.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_EQ_ZERO.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_EQ_ZERO.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_EQ_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_DOUBLE_EQ_ZERO_OR_NULL.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_EQ_ZERO_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_EQ_ZERO_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NE_ZERO.test(null));
    assertFalse(OBJECT_DOUBLE_NE_ZERO.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NE_ZERO.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_NE_ZERO.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_NE_ZERO_OR_NULL.test(null));
    assertFalse(OBJECT_DOUBLE_NE_ZERO_OR_NULL.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NE_ZERO_OR_NULL.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_NE_ZERO_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE.test(null));
    assertFalse(OBJECT_DOUBLE_POSITIVE.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_NULL.test(null));
    assertFalse(OBJECT_DOUBLE_POSITIVE_OR_NULL.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE_OR_NULL.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_ZERO.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE_OR_ZERO.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_ZERO.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_ZERO_OR_NULL.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_POSITIVE_OR_ZERO_OR_NULL.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_POSITIVE_OR_ZERO_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE.test(null));
    assertFalse(OBJECT_DOUBLE_NEGATIVE.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_NULL.test(null));
    assertFalse(OBJECT_DOUBLE_NEGATIVE_OR_NULL.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE_OR_ZERO.test(null));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_ZERO.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_ZERO.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE_OR_ZERO.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_ZERO_OR_NULL.test(null));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_ZERO_OR_NULL.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_NEGATIVE_OR_ZERO_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NEGATIVE_OR_ZERO_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_NAN.test(null));
    assertTrue(OBJECT_DOUBLE_NAN.test(Double.valueOf(Double.NaN)));
    assertFalse(OBJECT_DOUBLE_NAN.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_NAN.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NAN.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_NAN.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_NAN.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_DOUBLE_NAN_OR_NULL.test(null));
    assertTrue(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(Double.NaN)));
    assertFalse(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_NAN_OR_NULL.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_FINITE.test(null));
    assertFalse(OBJECT_DOUBLE_FINITE.test(Double.valueOf(Double.NaN)));
    assertTrue(OBJECT_DOUBLE_FINITE.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_FINITE.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_FINITE.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_FINITE.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_FINITE.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_DOUBLE_FINITE_OR_NULL.test(null));
    assertFalse(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(Double.NaN)));
    assertTrue(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(.0d)));
    assertTrue(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(-1.d)));
    assertTrue(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(1.d)));
    assertFalse(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_FINITE_OR_NULL.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertFalse(OBJECT_DOUBLE_INFINITE.test(null));
    assertFalse(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(Double.NaN)));
    assertFalse(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertTrue(OBJECT_DOUBLE_INFINITE.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    assertTrue(OBJECT_DOUBLE_INFINITE_OR_NULL.test(null));
    assertFalse(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(Double.NaN)));
    assertFalse(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(.0d)));
    assertFalse(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(-1.d)));
    assertFalse(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(1.d)));
    assertTrue(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(Double.POSITIVE_INFINITY)));
    assertTrue(OBJECT_DOUBLE_INFINITE_OR_NULL.test(Double.valueOf(Double.NEGATIVE_INFINITY)));
    // Array predicates
    assertTrue(BYTE_ARRAY_NON_EMPTY.test(null));
    assertFalse(BYTE_ARRAY_NON_EMPTY.test(new byte[0]));
    assertTrue(BYTE_ARRAY_NON_EMPTY.test(new byte[1]));
    assertFalse(BYTE_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(BYTE_ARRAY_NON_EMPTY_OR_NULL.test(new byte[0]));
    assertTrue(BYTE_ARRAY_NON_EMPTY_OR_NULL.test(new byte[1]));
    assertTrue(SHORT_ARRAY_NON_EMPTY.test(null));
    assertFalse(SHORT_ARRAY_NON_EMPTY.test(new short[0]));
    assertTrue(SHORT_ARRAY_NON_EMPTY.test(new short[1]));
    assertFalse(SHORT_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(SHORT_ARRAY_NON_EMPTY_OR_NULL.test(new short[0]));
    assertTrue(SHORT_ARRAY_NON_EMPTY_OR_NULL.test(new short[1]));
    assertTrue(INT_ARRAY_NON_EMPTY.test(null));
    assertFalse(INT_ARRAY_NON_EMPTY.test(new int[0]));
    assertTrue(INT_ARRAY_NON_EMPTY.test(new int[1]));
    assertFalse(INT_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(INT_ARRAY_NON_EMPTY_OR_NULL.test(new int[0]));
    assertTrue(INT_ARRAY_NON_EMPTY_OR_NULL.test(new int[1]));
    assertTrue(LONG_ARRAY_NON_EMPTY.test(null));
    assertFalse(LONG_ARRAY_NON_EMPTY.test(new long[0]));
    assertTrue(LONG_ARRAY_NON_EMPTY.test(new long[1]));
    assertFalse(LONG_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(LONG_ARRAY_NON_EMPTY_OR_NULL.test(new long[0]));
    assertTrue(LONG_ARRAY_NON_EMPTY_OR_NULL.test(new long[1]));
    assertTrue(FLOAT_ARRAY_NON_EMPTY.test(null));
    assertFalse(FLOAT_ARRAY_NON_EMPTY.test(new float[0]));
    assertTrue(FLOAT_ARRAY_NON_EMPTY.test(new float[1]));
    assertFalse(FLOAT_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(FLOAT_ARRAY_NON_EMPTY_OR_NULL.test(new float[0]));
    assertTrue(FLOAT_ARRAY_NON_EMPTY_OR_NULL.test(new float[1]));
    assertTrue(DOUBLE_ARRAY_NON_EMPTY.test(null));
    assertFalse(DOUBLE_ARRAY_NON_EMPTY.test(new double[0]));
    assertTrue(DOUBLE_ARRAY_NON_EMPTY.test(new double[1]));
    assertFalse(DOUBLE_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(DOUBLE_ARRAY_NON_EMPTY_OR_NULL.test(new double[0]));
    assertTrue(DOUBLE_ARRAY_NON_EMPTY_OR_NULL.test(new double[1]));
    assertTrue(CHAR_ARRAY_NON_EMPTY.test(null));
    assertFalse(CHAR_ARRAY_NON_EMPTY.test(new char[0]));
    assertTrue(CHAR_ARRAY_NON_EMPTY.test(new char[1]));
    assertFalse(CHAR_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(CHAR_ARRAY_NON_EMPTY_OR_NULL.test(new char[0]));
    assertTrue(CHAR_ARRAY_NON_EMPTY_OR_NULL.test(new char[1]));
    assertTrue(BOOLEAN_ARRAY_NON_EMPTY.test(null));
    assertFalse(BOOLEAN_ARRAY_NON_EMPTY.test(new boolean[0]));
    assertTrue(BOOLEAN_ARRAY_NON_EMPTY.test(new boolean[1]));
    assertFalse(BOOLEAN_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(BOOLEAN_ARRAY_NON_EMPTY_OR_NULL.test(new boolean[0]));
    assertTrue(BOOLEAN_ARRAY_NON_EMPTY_OR_NULL.test(new boolean[1]));
    assertTrue(OBJECT_ARRAY_NON_EMPTY.test(null));
    assertFalse(OBJECT_ARRAY_NON_EMPTY.test(new Object[0]));
    assertTrue(OBJECT_ARRAY_NON_EMPTY.test(new Object[1]));
    assertFalse(OBJECT_ARRAY_NON_EMPTY_OR_NULL.test(null));
    assertFalse(OBJECT_ARRAY_NON_EMPTY_OR_NULL.test(new Object[0]));
    assertTrue(OBJECT_ARRAY_NON_EMPTY_OR_NULL.test(new Object[1]));
    // Collection and map predicates
    assertTrue(COLLECTION_NON_EMPTY.test(null));
    assertFalse(COLLECTION_NON_EMPTY.test(Collections.emptySet()));
    assertTrue(COLLECTION_NON_EMPTY.test(Collections.singleton(new Object())));
    assertFalse(COLLECTION_NON_EMPTY_OR_NULL.test(null));
    assertFalse(COLLECTION_NON_EMPTY_OR_NULL.test(Collections.emptySet()));
    assertTrue(COLLECTION_NON_EMPTY_OR_NULL.test(Collections.singleton(new Object())));
    assertTrue(MAP_NON_EMPTY.test(null));
    assertFalse(MAP_NON_EMPTY.test(Collections.emptyMap()));
    assertTrue(MAP_NON_EMPTY.test(Collections.singletonMap(new Object(), new Object())));
    assertFalse(MAP_NON_EMPTY_OR_NULL.test(null));
    assertFalse(MAP_NON_EMPTY_OR_NULL.test(Collections.emptyMap()));
    assertTrue(MAP_NON_EMPTY_OR_NULL.test(Collections.singletonMap(new Object(), new Object())));
    // Index predicates
    assertTrue(checkByteArrayIndex(0).test(null));
    assertFalse(checkByteArrayIndex(0).test(new byte[0]));
    assertTrue(checkByteArrayIndex(0).test(new byte[1]));
    assertFalse(checkByteArrayIndex(-1).test(new byte[1]));
    assertFalse(checkByteArrayIndex(1).test(new byte[1]));
    assertTrue(checkByteArrayIndex(1).test(new byte[2]));
    assertFalse(checkByteArrayIndex(3).test(new byte[2]));
    assertTrue(checkShortArrayIndex(0).test(null));
    assertFalse(checkShortArrayIndex(0).test(new short[0]));
    assertTrue(checkShortArrayIndex(0).test(new short[1]));
    assertFalse(checkShortArrayIndex(-1).test(new short[1]));
    assertFalse(checkShortArrayIndex(1).test(new short[1]));
    assertTrue(checkShortArrayIndex(1).test(new short[2]));
    assertFalse(checkShortArrayIndex(3).test(new short[2]));
    assertTrue(checkIntArrayIndex(0).test(null));
    assertFalse(checkIntArrayIndex(0).test(new int[0]));
    assertTrue(checkIntArrayIndex(0).test(new int[1]));
    assertFalse(checkIntArrayIndex(-1).test(new int[1]));
    assertFalse(checkIntArrayIndex(1).test(new int[1]));
    assertTrue(checkIntArrayIndex(1).test(new int[2]));
    assertFalse(checkIntArrayIndex(3).test(new int[2]));
    assertTrue(checkLongArrayIndex(0).test(null));
    assertFalse(checkLongArrayIndex(0).test(new long[0]));
    assertTrue(checkLongArrayIndex(0).test(new long[1]));
    assertFalse(checkLongArrayIndex(-1).test(new long[1]));
    assertFalse(checkLongArrayIndex(1).test(new long[1]));
    assertTrue(checkLongArrayIndex(1).test(new long[2]));
    assertFalse(checkLongArrayIndex(3).test(new long[2]));
    assertTrue(checkFloatArrayIndex(0).test(null));
    assertFalse(checkFloatArrayIndex(0).test(new float[0]));
    assertTrue(checkFloatArrayIndex(0).test(new float[1]));
    assertFalse(checkFloatArrayIndex(-1).test(new float[1]));
    assertFalse(checkFloatArrayIndex(1).test(new float[1]));
    assertTrue(checkFloatArrayIndex(1).test(new float[2]));
    assertFalse(checkFloatArrayIndex(3).test(new float[2]));
    assertTrue(checkDoubleArrayIndex(0).test(null));
    assertFalse(checkDoubleArrayIndex(0).test(new double[0]));
    assertTrue(checkDoubleArrayIndex(0).test(new double[1]));
    assertFalse(checkDoubleArrayIndex(-1).test(new double[1]));
    assertFalse(checkDoubleArrayIndex(1).test(new double[1]));
    assertTrue(checkDoubleArrayIndex(1).test(new double[2]));
    assertFalse(checkDoubleArrayIndex(3).test(new double[2]));
    assertTrue(checkCharArrayIndex(0).test(null));
    assertFalse(checkCharArrayIndex(0).test(new char[0]));
    assertTrue(checkCharArrayIndex(0).test(new char[1]));
    assertFalse(checkCharArrayIndex(-1).test(new char[1]));
    assertFalse(checkCharArrayIndex(1).test(new char[1]));
    assertTrue(checkCharArrayIndex(1).test(new char[2]));
    assertFalse(checkCharArrayIndex(3).test(new char[2]));
    assertTrue(checkBooleanArrayIndex(0).test(null));
    assertFalse(checkBooleanArrayIndex(0).test(new boolean[0]));
    assertTrue(checkBooleanArrayIndex(0).test(new boolean[1]));
    assertFalse(checkBooleanArrayIndex(-1).test(new boolean[1]));
    assertFalse(checkBooleanArrayIndex(1).test(new boolean[1]));
    assertTrue(checkBooleanArrayIndex(1).test(new boolean[2]));
    assertFalse(checkBooleanArrayIndex(3).test(new boolean[2]));
    assertTrue(checkObjectArrayIndex(0).test(null));
    assertFalse(checkObjectArrayIndex(0).test(new Object[0]));
    assertTrue(checkObjectArrayIndex(0).test(new Object[1]));
    assertFalse(checkObjectArrayIndex(-1).test(new Object[1]));
    assertFalse(checkObjectArrayIndex(1).test(new Object[1]));
    assertTrue(checkObjectArrayIndex(1).test(new Object[2]));
    assertFalse(checkObjectArrayIndex(3).test(new Object[2]));
    assertTrue(checkCharSequenceIndex(0).test(null));
    assertFalse(checkCharSequenceIndex(0).test(""));
    assertTrue(checkCharSequenceIndex(0).test("a"));
    assertFalse(checkCharSequenceIndex(-1).test("a"));
    assertFalse(checkCharSequenceIndex(1).test("a"));
    assertTrue(checkCharSequenceIndex(1).test("ab"));
    assertFalse(checkCharSequenceIndex(3).test("ab"));
    // Range predicates
    assertTrue(checkByteArrayRange(0).test(null));
    assertTrue(checkByteArrayRange(0).test(new byte[0]));
    assertTrue(checkByteArrayRange(0).test(new byte[1]));
    assertFalse(checkByteArrayRange(-1).test(new byte[1]));
    assertTrue(checkByteArrayRange(1).test(new byte[1]));
    assertTrue(checkByteArrayRange(1).test(new byte[2]));
    assertFalse(checkByteArrayRange(3).test(new byte[2]));
    assertTrue(checkByteArrayRange(0, 0).test(null));
    assertTrue(checkByteArrayRange(0, 0).test(new byte[0]));
    assertTrue(checkByteArrayRange(0, 1).test(new byte[1]));
    assertFalse(checkByteArrayRange(-1, 1).test(new byte[1]));
    assertTrue(checkByteArrayRange(1, 1).test(new byte[1]));
    assertTrue(checkByteArrayRange(1, 2).test(new byte[2]));
    assertFalse(checkByteArrayRange(1, 3).test(new byte[2]));
    assertFalse(checkByteArrayRange(2, 1).test(new byte[2]));
    assertTrue(checkShortArrayRange(0).test(null));
    assertTrue(checkShortArrayRange(0).test(new short[0]));
    assertTrue(checkShortArrayRange(0).test(new short[1]));
    assertFalse(checkShortArrayRange(-1).test(new short[1]));
    assertTrue(checkShortArrayRange(1).test(new short[1]));
    assertTrue(checkShortArrayRange(1).test(new short[2]));
    assertFalse(checkShortArrayRange(3).test(new short[2]));
    assertTrue(checkShortArrayRange(0, 0).test(null));
    assertTrue(checkShortArrayRange(0, 0).test(new short[0]));
    assertTrue(checkShortArrayRange(0, 1).test(new short[1]));
    assertFalse(checkShortArrayRange(-1, 1).test(new short[1]));
    assertTrue(checkShortArrayRange(1, 1).test(new short[1]));
    assertTrue(checkShortArrayRange(1, 2).test(new short[2]));
    assertFalse(checkShortArrayRange(1, 3).test(new short[2]));
    assertFalse(checkShortArrayRange(2, 1).test(new short[2]));
    assertTrue(checkIntArrayRange(0).test(null));
    assertTrue(checkIntArrayRange(0).test(new int[0]));
    assertTrue(checkIntArrayRange(0).test(new int[1]));
    assertFalse(checkIntArrayRange(-1).test(new int[1]));
    assertTrue(checkIntArrayRange(1).test(new int[1]));
    assertTrue(checkIntArrayRange(1).test(new int[2]));
    assertFalse(checkIntArrayRange(3).test(new int[2]));
    assertTrue(checkIntArrayRange(0, 0).test(null));
    assertTrue(checkIntArrayRange(0, 0).test(new int[0]));
    assertTrue(checkIntArrayRange(0, 1).test(new int[1]));
    assertFalse(checkIntArrayRange(-1, 1).test(new int[1]));
    assertTrue(checkIntArrayRange(1, 1).test(new int[1]));
    assertTrue(checkIntArrayRange(1, 2).test(new int[2]));
    assertFalse(checkIntArrayRange(1, 3).test(new int[2]));
    assertFalse(checkIntArrayRange(2, 1).test(new int[2]));
    assertTrue(checkLongArrayRange(0).test(null));
    assertTrue(checkLongArrayRange(0).test(new long[0]));
    assertTrue(checkLongArrayRange(0).test(new long[1]));
    assertFalse(checkLongArrayRange(-1).test(new long[1]));
    assertTrue(checkLongArrayRange(1).test(new long[1]));
    assertTrue(checkLongArrayRange(1).test(new long[2]));
    assertFalse(checkLongArrayRange(3).test(new long[2]));
    assertTrue(checkLongArrayRange(0, 0).test(null));
    assertTrue(checkLongArrayRange(0, 0).test(new long[0]));
    assertTrue(checkLongArrayRange(0, 1).test(new long[1]));
    assertFalse(checkLongArrayRange(-1, 1).test(new long[1]));
    assertTrue(checkLongArrayRange(1, 1).test(new long[1]));
    assertTrue(checkLongArrayRange(1, 2).test(new long[2]));
    assertFalse(checkLongArrayRange(1, 3).test(new long[2]));
    assertFalse(checkLongArrayRange(2, 1).test(new long[2]));
    assertTrue(checkFloatArrayRange(0).test(null));
    assertTrue(checkFloatArrayRange(0).test(new float[0]));
    assertTrue(checkFloatArrayRange(0).test(new float[1]));
    assertFalse(checkFloatArrayRange(-1).test(new float[1]));
    assertTrue(checkFloatArrayRange(1).test(new float[1]));
    assertTrue(checkFloatArrayRange(1).test(new float[2]));
    assertFalse(checkFloatArrayRange(3).test(new float[2]));
    assertTrue(checkFloatArrayRange(0, 0).test(null));
    assertTrue(checkFloatArrayRange(0, 0).test(new float[0]));
    assertTrue(checkFloatArrayRange(0, 1).test(new float[1]));
    assertFalse(checkFloatArrayRange(-1, 1).test(new float[1]));
    assertTrue(checkFloatArrayRange(1, 1).test(new float[1]));
    assertTrue(checkFloatArrayRange(1, 2).test(new float[2]));
    assertFalse(checkFloatArrayRange(1, 3).test(new float[2]));
    assertFalse(checkFloatArrayRange(2, 1).test(new float[2]));
    assertTrue(checkDoubleArrayRange(0).test(null));
    assertTrue(checkDoubleArrayRange(0).test(new double[0]));
    assertTrue(checkDoubleArrayRange(0).test(new double[1]));
    assertFalse(checkDoubleArrayRange(-1).test(new double[1]));
    assertTrue(checkDoubleArrayRange(1).test(new double[1]));
    assertTrue(checkDoubleArrayRange(1).test(new double[2]));
    assertFalse(checkDoubleArrayRange(3).test(new double[2]));
    assertTrue(checkDoubleArrayRange(0, 0).test(null));
    assertTrue(checkDoubleArrayRange(0, 0).test(new double[0]));
    assertTrue(checkDoubleArrayRange(0, 1).test(new double[1]));
    assertFalse(checkDoubleArrayRange(-1, 1).test(new double[1]));
    assertTrue(checkDoubleArrayRange(1, 1).test(new double[1]));
    assertTrue(checkDoubleArrayRange(1, 2).test(new double[2]));
    assertFalse(checkDoubleArrayRange(1, 3).test(new double[2]));
    assertFalse(checkDoubleArrayRange(2, 1).test(new double[2]));
    assertTrue(checkCharArrayRange(0).test(null));
    assertTrue(checkCharArrayRange(0).test(new char[0]));
    assertTrue(checkCharArrayRange(0).test(new char[1]));
    assertFalse(checkCharArrayRange(-1).test(new char[1]));
    assertTrue(checkCharArrayRange(1).test(new char[1]));
    assertTrue(checkCharArrayRange(1).test(new char[2]));
    assertFalse(checkCharArrayRange(3).test(new char[2]));
    assertTrue(checkCharArrayRange(0, 0).test(null));
    assertTrue(checkCharArrayRange(0, 0).test(new char[0]));
    assertTrue(checkCharArrayRange(0, 1).test(new char[1]));
    assertFalse(checkCharArrayRange(-1, 1).test(new char[1]));
    assertTrue(checkCharArrayRange(1, 1).test(new char[1]));
    assertTrue(checkCharArrayRange(1, 2).test(new char[2]));
    assertFalse(checkCharArrayRange(1, 3).test(new char[2]));
    assertFalse(checkCharArrayRange(2, 1).test(new char[2]));
    assertTrue(checkBooleanArrayRange(0).test(null));
    assertTrue(checkBooleanArrayRange(0).test(new boolean[0]));
    assertTrue(checkBooleanArrayRange(0).test(new boolean[1]));
    assertFalse(checkBooleanArrayRange(-1).test(new boolean[1]));
    assertTrue(checkBooleanArrayRange(1).test(new boolean[1]));
    assertTrue(checkBooleanArrayRange(1).test(new boolean[2]));
    assertFalse(checkBooleanArrayRange(3).test(new boolean[2]));
    assertTrue(checkBooleanArrayRange(0, 0).test(null));
    assertTrue(checkBooleanArrayRange(0, 0).test(new boolean[0]));
    assertTrue(checkBooleanArrayRange(0, 1).test(new boolean[1]));
    assertFalse(checkBooleanArrayRange(-1, 1).test(new boolean[1]));
    assertTrue(checkBooleanArrayRange(1, 1).test(new boolean[1]));
    assertTrue(checkBooleanArrayRange(1, 2).test(new boolean[2]));
    assertFalse(checkBooleanArrayRange(1, 3).test(new boolean[2]));
    assertFalse(checkBooleanArrayRange(2, 1).test(new boolean[2]));
    assertTrue(checkObjectArrayRange(0).test(null));
    assertTrue(checkObjectArrayRange(0).test(new Object[0]));
    assertTrue(checkObjectArrayRange(0).test(new Object[1]));
    assertFalse(checkObjectArrayRange(-1).test(new Object[1]));
    assertTrue(checkObjectArrayRange(1).test(new Object[1]));
    assertTrue(checkObjectArrayRange(1).test(new Object[2]));
    assertFalse(checkObjectArrayRange(3).test(new Object[2]));
    assertTrue(checkObjectArrayRange(0, 0).test(null));
    assertTrue(checkObjectArrayRange(0, 0).test(new Object[0]));
    assertTrue(checkObjectArrayRange(0, 1).test(new Object[1]));
    assertFalse(checkObjectArrayRange(-1, 1).test(new Object[1]));
    assertTrue(checkObjectArrayRange(1, 1).test(new Object[1]));
    assertTrue(checkObjectArrayRange(1, 2).test(new Object[2]));
    assertFalse(checkObjectArrayRange(1, 3).test(new Object[2]));
    assertFalse(checkObjectArrayRange(2, 1).test(new Object[2]));
    assertTrue(checkCharSequenceRange(0).test(null));
    assertTrue(checkCharSequenceRange(0).test(""));
    assertTrue(checkCharSequenceRange(0).test("a"));
    assertFalse(checkCharSequenceRange(-1).test("a"));
    assertTrue(checkCharSequenceRange(1).test("a"));
    assertTrue(checkCharSequenceRange(1).test("ab"));
    assertFalse(checkCharSequenceRange(3).test("ab"));
    assertTrue(checkCharSequenceRange(0, 0).test(null));
    assertTrue(checkCharSequenceRange(0, 0).test(""));
    assertTrue(checkCharSequenceRange(0, 1).test("a"));
    assertFalse(checkCharSequenceRange(-1, 1).test("a"));
    assertTrue(checkCharSequenceRange(1, 1).test("a"));
    assertTrue(checkCharSequenceRange(1, 2).test("ab"));
    assertFalse(checkCharSequenceRange(1, 3).test("ab"));
    assertFalse(checkCharSequenceRange(2, 1).test("ab"));
    // Miscellaneous predicates
    assertTrue(match(Pattern.compile("")).test(null));
    assertFalse(match(Pattern.compile(".*abc.*")).test("123"));
    assertTrue(match(Pattern.compile(".*abc.*")).test("abc"));
    assertTrue(match(Pattern.compile(".*abc.*")).test("123abc456"));
  }

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
    assertEquals("Cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            ExceptionProvider.ofNPE())).getMessage());
    // NPE: message
    assertEquals("object cannot be null", assertThrows(NullPointerException.class,
        () -> requireNonNull(null,
            ExceptionProvider.ofNPE("object cannot be null"))).getMessage());
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
    assertEquals("Invalid argument: 10", assertThrows(IllegalArgumentException.class,
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
            "object cannot be illegal: %s")).getMessage());
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
    assertEquals("Invalid argument: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: object
    assertEquals("object cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(Integer.valueOf(10), (o) -> false,
            ExceptionProvider.ofIAE("object cannot be illegal: %s"))).getMessage());
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
    assertEquals("Invalid argument: 10", assertThrows(IllegalArgumentException.class,
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
            "int cannot be illegal: %s")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(int, IntPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_int_exception() {
    // OK: int
    assertEquals(10, require(10, (int n) -> true, ExceptionProvider.ofIAE()));
    // IAE: int
    assertEquals("Invalid argument: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: int
    assertEquals("long cannot be illegal: 10", assertThrows(IllegalArgumentException.class,
        () -> require(10, (int n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal: %s"))).getMessage());
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
    assertEquals("Invalid argument: 10L", assertThrows(IllegalArgumentException.class,
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
    assertEquals("long cannot be illegal: 10L", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            "long cannot be illegal: %s")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(long, LongPredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_long_exception() {
    // OK: long
    assertEquals(10L, require(10L, (long n) -> true, ExceptionProvider.ofIAE()));
    // IAE: long
    assertEquals("Invalid argument: 10L", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // IAE: message: long
    assertEquals("long cannot be illegal: 10L", assertThrows(IllegalArgumentException.class,
        () -> require(10L, (long n) -> false,
            ExceptionProvider.ofIAE("long cannot be illegal: %s"))).getMessage());
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
    assertEquals("Invalid argument: 10.0d", assertThrows(IllegalArgumentException.class,
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
    assertEquals("double cannot be illegal: 10.0d", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            "double cannot be illegal: %s")).getMessage());
  }

  /**
   * Tests the {@link Predicates#require(double, DoublePredicate, ExceptionProvider)} method.
   */
  @Test
  public void test_require_double_exception() {
    // OK: double
    assertEquals(10., require(10., (double n) -> true, ExceptionProvider.ofIAE()), .0);
    // NPE: double
    assertEquals("Invalid argument: 10.0d", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            ExceptionProvider.ofIAE())).getMessage());
    // NPE: message: double
    assertEquals("double cannot be illegal: 10.0d", assertThrows(IllegalArgumentException.class,
        () -> require(10., (double n) -> false,
            ExceptionProvider.ofIAE("double cannot be illegal: %s"))).getMessage());
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
    assertEquals("Element [1] cannot be null", assertThrows(NullPointerException.class,
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
            "elements cannot be null: [%d]")).getMessage());
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
    assertEquals("Element [1] cannot be null", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            ExceptionProvider.OfSequence.ofNPE())).getMessage());
    // NPE: message: [index]
    assertEquals("elements cannot be null: [1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(new Object[]{"one", null, "three"},
            ExceptionProvider.OfSequence.ofNPE("elements cannot be null: [%d]"))).getMessage());
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
    assertEquals("Invalid element [2]: \"three\"", assertThrows(IllegalArgumentException.class,
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
    assertEquals("elements cannot be invalid: [2] = \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            "elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("elements cannot be invalid: [2] = \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new String[]{"one", "two", "three"}, (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE("elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
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
            "byte elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("byte elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new byte[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("byte elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
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
            "short elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("short elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new short[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("short elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
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
            "int elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("int elements cannot be invalid: [2] = 3", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new int[]{1, 2, 3}, (n) -> n < 3,
            ExceptionProvider.OfSequence.ofIAE("int elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3L", assertThrows(IllegalArgumentException.class,
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
    assertEquals("long elements cannot be invalid: [2] = 3L", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            "long elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3L", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("long elements cannot be invalid: [2] = 3L", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new long[]{1L, 2L, 3L}, (n) -> n < 3L,
            ExceptionProvider.OfSequence.ofIAE("long elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3.0f", assertThrows(IllegalArgumentException.class,
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
    assertEquals("float elements cannot be invalid: [2] = 3.0f", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            "float elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3.0f", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("float elements cannot be invalid: [2] = 3.0f", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new float[]{1.f, 2.f, 3.f}, (n) -> n < 3.f,
            ExceptionProvider.OfSequence.ofIAE("float elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 3.0d", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.)).getMessage());
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
    assertEquals("double elements cannot be invalid: [2] = 3.0d", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            "double elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 3.0d", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("double elements cannot be invalid: [2] = 3.0d", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new double[]{1., 2., 3.}, (n) -> n < 3.,
            ExceptionProvider.OfSequence.ofIAE("double elements cannot be invalid: [%d] = %s"))).getMessage());
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
    assertEquals("Invalid element [2]: 'c'", assertThrows(IllegalArgumentException.class,
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
    assertEquals("char elements cannot be invalid: [2] = 'c'", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            "char elements cannot be invalid: [%d] = %s")).getMessage());
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
    assertEquals("Invalid element [2]: 'c'", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("char elements cannot be invalid: [2] = 'c'", assertThrows(IllegalArgumentException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            ExceptionProvider.OfSequence.ofIAE("char elements cannot be invalid: [%d] = %s"))).getMessage());
    // ISE: message
    assertEquals("char elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(new char[]{'a', 'b', 'c'}, (c) -> c < 'c',
            (a, i, e) -> new IllegalStateException("char elements cannot be invalid"))).getMessage());
  }

  // Iterable checks

  /**
   * Tests the {@link Predicates#requireAllNonNull(Iterable)} method.
   */
  @Test
  public void test_requireAllNonNull_iterable() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAllNonNull(emptySample));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAllNonNull(validSample));
    // NPE: [index]
    assertEquals("Element [1] cannot be null", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(Arrays.asList("one", null, "three"))).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAllNonNull(Iterable, String)} method.
   */
  @Test
  public void test_requireAllNonNull_iterable_message() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample, "elements cannot be null"));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAllNonNull(emptySample, "elements cannot be null"));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAllNonNull(validSample, "elements cannot be null"));
    // NPE: message: [index]
    assertEquals("elements cannot be null: [1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(Arrays.asList("one", null, "three"),
            "elements cannot be null: [%d]")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAllNonNull(Iterable, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAllNonNull_iterable_exception() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAllNonNull(nullSample, ExceptionProvider.OfSequence.ofNPE()));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAllNonNull(emptySample, ExceptionProvider.OfSequence.ofNPE()));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAllNonNull(validSample, ExceptionProvider.OfSequence.ofNPE()));
    // NPE: [index]
    assertEquals("Element [1] cannot be null", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(Arrays.asList("one", null, "three"),
            ExceptionProvider.OfSequence.ofNPE())).getMessage());
    // NPE: message: [index]
    assertEquals("elements cannot be null: [1]", assertThrows(NullPointerException.class,
        () -> requireAllNonNull(Arrays.asList("one", null, "three"),
            ExceptionProvider.OfSequence.ofNPE("elements cannot be null: [%d]"))).getMessage());
    // ISE: message
    assertEquals("elements cannot be null", assertThrows(IllegalStateException.class,
        () -> requireAllNonNull(Arrays.asList("one", null, "three"),
            (a, i, e) -> new IllegalStateException("elements cannot be null"))).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Iterable, Predicate)} method.
   */
  @Test
  public void test_requireAll_iterable() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAll(emptySample, (o) -> true));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAll(validSample, (o) -> true));
    // IAE: [index]
    assertEquals("Invalid element [2]: \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(Arrays.asList("one", "two", "three"), (o) -> o.length() == 3)).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Iterable, Predicate, String)} method.
   */
  @Test
  public void test_requireAll_iterable_message() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true, "elements cannot be invalid"));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAll(emptySample, (o) -> true, "elements cannot be invalid"));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAll(validSample, (o) -> true, "elements cannot be invalid"));
    // IAE: message: [index] = element
    assertEquals("elements cannot be invalid: [2] = \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(Arrays.asList("one", "two", "three"), (o) -> o.length() == 3,
            "elements cannot be invalid: [%d] = %s")).getMessage());
  }

  /**
   * Tests the {@link Predicates#requireAll(Iterable, Predicate, ExceptionProvider)} method.
   */
  @Test
  public void test_requireAll_iterable_exception() {
    // OK: null
    final Iterable<Object> nullSample = null;
    assertSame(nullSample, requireAll(nullSample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: []
    final Iterable<Object> emptySample = Collections.emptySet();
    assertSame(emptySample, requireAll(emptySample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // OK: [...]
    final Iterable<Object> validSample = Arrays.asList("one", "two", "three");
    assertSame(validSample, requireAll(validSample, (o) -> true, ExceptionProvider.OfSequence.ofIAE()));
    // IAE: [index] = element
    assertEquals("Invalid element [2]: \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(Arrays.asList("one", "two", "three"), (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE())).getMessage());
    // IAE: message: [index] = element
    assertEquals("elements cannot be invalid: [2] = \"three\"", assertThrows(IllegalArgumentException.class,
        () -> requireAll(Arrays.asList("one", "two", "three"), (o) -> o.length() == 3,
            ExceptionProvider.OfSequence.ofIAE("elements cannot be invalid: [%d] = %s"))).getMessage());
    // ISE: message
    assertEquals("elements cannot be invalid", assertThrows(IllegalStateException.class,
        () -> requireAll(Arrays.asList("one", "two", "three"), (o) -> o.length() == 3,
            (a, i, e) -> new IllegalStateException("elements cannot be invalid"))).getMessage());
  }

}
