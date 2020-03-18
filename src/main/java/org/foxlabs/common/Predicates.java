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

import java.lang.reflect.Array;

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;

/**
 * A collection of reusable predicates and the {@code requireNonNull()}, {@code require()},
 * {@code requireAllNonNull()}, {@code requireAll()} methods that check that a given
 * object satisfies a given condition. The primary goal of this utility class is parameter
 * validation in methods and constructors.
 *
 * <p>
 * {@code NullPointerException} examples:
 * <pre>
 * // Returns "success"
 * String result = requireNonNull("success");
 *
 * // Throws NPE without detail message
 * String result = requireNonNull(null);
 *
 * // Throws NPE with detail message: "cannot be null"
 * String result = requireNonNull(null, "cannot be null");
 *
 * // Throws NPE with detail message: "cannot be null: result"
 * String result = requireNonNull(null,
 *     ExceptionProvider.ofNPE("cannot be null", "result"));
 *
 * // Throws NPE with detail message: "[1]"
 * String[] items = requireAllNonNull(new String[]{"one", null, "three"});
 *
 * // Throws NPE with detail message: "cannot be null: [1]"
 * String[] items = requireAllNonNull(new String[]{"one", null, "three"},
 *     "cannot be null");
 *
 * // Throws NPE with detail message: "cannot be null: items[1]"
 * String[] items = requireAllNonNull(new String[]{"one", null, "three"},
 *     ExceptionProvider.OfSequence.ofNPE("cannot be null", "items"));
 * </pre>
 * </p>
 *
 * <p>
 * {@code IllegalArgumentException} examples:
 * <pre>
 * // Returns 10
 * int index = require(10, INT_POSITIVE_OR_ZERO);
 *
 * // Throws IAE with detail message: "-5"
 * int index = require(-5, INT_POSITIVE_OR_ZERO);
 *
 * // Throws IAE with detail message: "should be >= 0: -5"
 * int index = require(-5, INT_POSITIVE_OR_ZERO, "should be >= 0");
 *
 * // Throws IAE with detail message: "should be >= 0: index = -5"
 * int index = require(-5, INT_POSITIVE_OR_ZERO,
 *     ExceptionProvider.ofIAE("should be >= 0", "index"));
 *
 * // Throws IAE with detail message: "[2] = three"
 * List&lt;String&gt; items = requireAll(Arrays.asList("one", "two", "three"),
 *     (s) -> s != null && s.length() <= 3);
 *
 * // Throws IAE with detail message: "length should be <= 3: [2] = three"
 * List&lt;String&gt; items = requireAll(Arrays.asList("one", "two", "three"),
 *     (s) -> s != null && s.length() <= 3,
 *     "length should be <= 3");
 *
 * // Throws IAE with detail message: "length should be <= 3: items[2] = three"
 * List&lt;String&gt; items = requireAll(Arrays.asList("one", "two", "three"),
 *     (s) -> s != null && s.length() <= 3,
 *     ExceptionProvider.OfSequence.ofIAE("length should be <= 3", "items"));
 * </pre>
 * </p>
 *
 * <p>
 * More complex examples:
 * <pre>
 * // Throws IndexOutOfBoundsException with detail message: "index should be > 10 and < 100: -5"
 * int index = require(-5, (int i) -> i > 10 && i < 100,
 *     (i) -> new IndexOutOfBoundsException("index should be > 10 and < 100: " + i));
 *
 * // Returns "one two three"
 * final Pattern pattern = Pattern.compile(".*two.*");
 * String result = require("one two three", STRING_NON_EMPTY.and(match(pattern)),
 *     ExceptionProvider.ofIAE("cannot be null or empty and should match '" + pattern + "'"));
 *
 * // Throws NPE with detail message: "item cannot be null: items[2]"
 * List&lt;String&gt; items = requireAllNonNull(
 *     require(Arrays.asList("one", null, "three"), COLLECTION_NON_EMPTY_OR_NULL,
 *         "items cannot be null or empty"),
 *     ExceptionProvider.OfSequence.ofNPE("item cannot be null", "items"));
 * </pre>
 * </p>
 *
 * @author Fox Mulder
 */
public final class Predicates {

  // Instantiation is not possible
  private Predicates() {
    throw new IllegalAccessError();
  }

  // Object predicates

  /** {@code (o) -> o == null} */
  public static final Predicate<Object> OBJECT_NULL = (o) -> o == null;
  /** {@code (o) -> o != null} */
  public static final Predicate<Object> OBJECT_NON_NULL = (o) -> o != null;

  // Primitive int predicates

  /** {@code (n) -> n == 0} */
  public static final IntPredicate INT_EQ_ZERO = (n) -> n == 0;
  /** {@code (n) -> n != 0} */
  public static final IntPredicate INT_NE_ZERO = (n) -> n != 0;
  /** {@code (n) -> n > 0} */
  public static final IntPredicate INT_POSITIVE = (n) -> n > 0;
  /** {@code (n) -> n >= 0} */
  public static final IntPredicate INT_POSITIVE_OR_ZERO = (n) -> n >= 0;
  /** {@code (n) -> n < 0} */
  public static final IntPredicate INT_NEGATIVE = (n) -> n < 0;
  /** {@code (n) -> n <= 0} */
  public static final IntPredicate INT_NEGATIVE_OR_ZERO = (n) -> n <= 0;

  // Integer predicates

  /** {@code (n) -> n != null && n.intValue() == 0} */
  public static final Predicate<Integer> OBJECT_INT_EQ_ZERO = (n) -> n != null && n.intValue() == 0;
  /** {@code (n) -> n == null || n.intValue() == 0} */
  public static final Predicate<Integer> OBJECT_INT_EQ_ZERO_OR_NULL = (n) -> n == null || n.intValue() == 0;
  /** {@code (n) -> n != null && n.intValue() != 0} */
  public static final Predicate<Integer> OBJECT_INT_NE_ZERO = (n) -> n != null && n.intValue() != 0;
  /** {@code (n) -> n == null || n.intValue() != 0} */
  public static final Predicate<Integer> OBJECT_INT_NE_ZERO_OR_NULL = (n) -> n == null || n.intValue() != 0;
  /** {@code (n) -> n != null && n.intValue() > 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE = (n) -> n != null && n.intValue() > 0;
  /** {@code (n) -> n == null || n.intValue() > 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE_OR_NULL = (n) -> n == null || n.intValue() > 0;
  /** {@code (n) -> n != null && n.intValue() >= 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE_OR_ZERO = (n) -> n != null && n.intValue() >= 0;
  /** {@code (n) -> n == null || n.intValue() >= 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.intValue() >= 0;
  /** {@code (n) -> n != null && n.intValue() < 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE = (n) -> n != null && n.intValue() < 0;
  /** {@code (n) -> n == null || n.intValue() < 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE_OR_NULL = (n) -> n == null || n.intValue() < 0;
  /** {@code (n) -> n != null && n.intValue() <= 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE_OR_ZERO = (n) -> n != null && n.intValue() <= 0;
  /** {@code (n) -> n == null || n.intValue() <= 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.intValue() <= 0;

  // Primitive long predicates

  /** {@code (n) -> n == 0L} */
  public static final LongPredicate LONG_EQ_ZERO = (n) -> n == 0L;
  /** {@code (n) -> n != 0L} */
  public static final LongPredicate LONG_NE_ZERO = (n) -> n != 0L;
  /** {@code (n) -> n > 0L} */
  public static final LongPredicate LONG_POSITIVE = (n) -> n > 0L;
  /** {@code (n) -> n >= 0L} */
  public static final LongPredicate LONG_POSITIVE_OR_ZERO = (n) -> n >= 0L;
  /** {@code (n) -> n < 0L} */
  public static final LongPredicate LONG_NEGATIVE = (n) -> n < 0L;
  /** {@code (n) -> n <= 0L} */
  public static final LongPredicate LONG_NEGATIVE_OR_ZERO = (n) -> n <= 0L;

  // Long predicates

  /** {@code (n) -> n != null && n.longValue() == 0L} */
  public static final Predicate<Long> OBJECT_LONG_EQ_ZERO = (n) -> n != null && n.longValue() == 0L;
  /** {@code (n) -> n == null || n.longValue() == 0L} */
  public static final Predicate<Long> OBJECT_LONG_EQ_ZERO_OR_NULL = (n) -> n == null || n.longValue() == 0L;
  /** {@code (n) -> n != null && n.longValue() != 0L} */
  public static final Predicate<Long> OBJECT_LONG_NE_ZERO = (n) -> n != null && n.longValue() != 0L;
  /** {@code (n) -> n == null || n.longValue() != 0L} */
  public static final Predicate<Long> OBJECT_LONG_NE_ZERO_OR_NULL = (n) -> n == null || n.longValue() != 0L;
  /** {@code (n) -> n != null && n.longValue() > 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE = (n) -> n != null && n.longValue() > 0L;
  /** {@code (n) -> n == null || n.longValue() > 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE_OR_NULL = (n) -> n == null || n.longValue() > 0L;
  /** {@code (n) -> n != null && n.longValue() >= 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE_OR_ZERO = (n) -> n != null && n.longValue() >= 0L;
  /** {@code (n) -> n == null || n.longValue() >= 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.longValue() >= 0L;
  /** {@code (n) -> n != null && n.longValue() < 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE = (n) -> n != null && n.longValue() < 0L;
  /** {@code (n) -> n == null || n.longValue() < 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE_OR_NULL = (n) -> n == null || n.longValue() < 0L;
  /** {@code (n) -> n != null && n.longValue() <= 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE_OR_ZERO = (n) -> n != null && n.longValue() <= 0L;
  /** {@code (n) -> n == null || n.longValue() <= 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.longValue() <= 0L;

  // Float predicates

  /** {@code (n) -> n != null && n.floatValue() == .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_EQ_ZERO = (n) -> n != null && n.floatValue() == .0f;
  /** {@code (n) -> n == null || n.floatValue() == .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_EQ_ZERO_OR_NULL = (n) -> n == null || n.floatValue() == .0f;
  /** {@code (n) -> n != null && n.floatValue() != .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NE_ZERO = (n) -> n != null && n.floatValue() != .0f;
  /** {@code (n) -> n == null || n.floatValue() != .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NE_ZERO_OR_NULL = (n) -> n == null || n.floatValue() != .0f;
  /** {@code (n) -> n != null && n.floatValue() > .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE = (n) -> n != null && n.floatValue() > .0f;
  /** {@code (n) -> n == null || n.floatValue() > .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE_OR_NULL = (n) -> n == null || n.floatValue() > .0f;
  /** {@code (n) -> n != null && n.floatValue() >= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE_OR_ZERO = (n) -> n != null && n.floatValue() >= .0f;
  /** {@code (n) -> n == null || n.floatValue() >= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.floatValue() >= .0f;
  /** {@code (n) -> n != null && n.floatValue() < .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE = (n) -> n != null && n.floatValue() < .0f;
  /** {@code (n) -> n == null || n.floatValue() < .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE_OR_NULL = (n) -> n == null || n.floatValue() < .0f;
  /** {@code (n) -> n != null && n.floatValue() <= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE_OR_ZERO = (n) -> n != null && n.floatValue() <= .0f;
  /** {@code (n) -> n == null || n.floatValue() <= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.floatValue() <= .0f;
  /** {@code (n) -> n != null && n.isNaN() } */
  public static final Predicate<Float> OBJECT_FLOAT_NAN = (n) -> n != null && n.isNaN();
  /** {@code (n) -> n == null || n.isNaN() } */
  public static final Predicate<Float> OBJECT_FLOAT_NAN_OR_NULL = (n) -> n == null || n.isNaN();
  /** {@code (n) -> n != null && Float.isFinite(n.floatValue()) } */
  public static final Predicate<Float> OBJECT_FLOAT_FINITE = (n) -> n != null && Float.isFinite(n.floatValue());
  /** (n) -> n == null || Float.isFinite(n.floatValue()) } */
  public static final Predicate<Float> OBJECT_FLOAT_FINITE_OR_NULL = (n) -> n == null || Float.isFinite(n.floatValue());
  /** {@code (n) -> n != null && n.isInfinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_INFINITE = (n) -> n != null && n.isInfinite();
  /** {@code (n) -> n == null || n.isInfinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_INFINITE_OR_NULL = (n) -> n == null || n.isInfinite();

  // Primitive double predicates

  /** {@code (n) -> n == .0d} */
  public static final DoublePredicate DOUBLE_EQ_ZERO = (n) -> n == .0d;
  /** {@code (n) -> n != .0d} */
  public static final DoublePredicate DOUBLE_NE_ZERO = (n) -> n != .0d;
  /** {@code (n) -> n > .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE = (n) -> n > .0d;
  /** {@code (n) -> n >= .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE_OR_ZERO = (n) -> n >= .0d;
  /** {@code (n) -> n < .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE = (n) -> n < .0d;
  /** {@code (n) -> n <= .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE_OR_ZERO = (n) -> n <= .0d;
  /** {@code Double::isNaN } */
  public static final DoublePredicate DOUBLE_NAN = Double::isNaN;
  /** {@code Double::isFinite } */
  public static final DoublePredicate DOUBLE_FINITE = Double::isFinite;
  /** {@code Double::isInfinite } */
  public static final DoublePredicate DOUBLE_INFINITE = Double::isInfinite;

  // Double predicates

  /** {@code (n) -> n != null && n.doubleValue() == .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_EQ_ZERO = (n) -> n != null && n.doubleValue() == .0d;
  /** {@code (n) -> n == null || n.doubleValue() == .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_EQ_ZERO_OR_NULL = (n) -> n == null || n.doubleValue() == .0d;
  /** {@code (n) -> n != null && n.doubleValue() != .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NE_ZERO = (n) -> n != null && n.doubleValue() != .0d;
  /** {@code (n) -> n == null || n.doubleValue() != .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NE_ZERO_OR_NULL = (n) -> n == null || n.doubleValue() != .0d;
  /** {@code (n) -> n != null && n.doubleValue() > .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE = (n) -> n != null && n.doubleValue() > .0d;
  /** {@code (n) -> n == null || n.doubleValue() > .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE_OR_NULL = (n) -> n == null || n.doubleValue() > .0d;
  /** {@code (n) -> n != null && n.doubleValue() >= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE_OR_ZERO = (n) -> n != null && n.doubleValue() >= .0d;
  /** {@code (n) -> n == null || n.doubleValue() >= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.doubleValue() >= .0d;
  /** {@code (n) -> n != null && n.doubleValue() < .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE = (n) -> n != null && n.doubleValue() < .0d;
  /** {@code (n) -> n == null || n.doubleValue() < .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE_OR_NULL = (n) -> n == null || n.doubleValue() < .0d;
  /** {@code (n) -> n != null && n.doubleValue() <= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE_OR_ZERO = (n) -> n != null && n.doubleValue() <= .0d;
  /** {@code (n) -> n == null || n.doubleValue() <= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE_OR_ZERO_OR_NULL = (n) -> n == null || n.doubleValue() <= .0d;
  /** {@code (n) -> n != null && n.isNaN() } */
  public static final Predicate<Double> OBJECT_DOUBLE_NAN = (n) -> n != null && n.isNaN();
  /** {@code (n) -> n == null || n.isNaN() } */
  public static final Predicate<Double> OBJECT_DOUBLE_NAN_OR_NULL = (n) -> n == null || n.isNaN();
  /** {@code (n) -> n != null && Double.isFinite(n) } */
  public static final Predicate<Double> OBJECT_DOUBLE_FINITE = (n) -> n != null && Double.isFinite(n);
  /** {@code (n) -> n == null || Double.isFinite(n) } */
  public static final Predicate<Double> OBJECT_DOUBLE_FINITE_OR_NULL = (n) -> n == null || Double.isFinite(n);
  /** {@code (n) -> n != null && n.isInfinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_INFINITE = (n) -> n != null && n.isInfinite();
  /** {@code (n) -> n == null || n.isInfinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_INFINITE_OR_NULL = (n) -> n == null || n.isInfinite();

  // Character code point predicates

  /** Character::isSpaceChar */
  public static final IntPredicate CHAR_SPACE = Character::isSpaceChar;
  /** (c) -> !Character.isSpaceChar(c) */
  public static final IntPredicate CHAR_NON_SPACE = (c) -> !Character.isSpaceChar(c);
  /** Character::isWhitespace */
  public static final IntPredicate CHAR_WHITESPACE = Character::isWhitespace;
  /** (c) -> !Character.isWhitespace(c) */
  public static final IntPredicate CHAR_NON_WHITESPACE = (c) -> !Character.isWhitespace(c);
  /** Character::isDigit */
  public static final IntPredicate CHAR_DIGIT = Character::isDigit;
  /** (c) -> !Character.isDigit(c) */
  public static final IntPredicate CHAR_NON_DIGIT = (c) -> !Character.isDigit(c);
  /** Character::isLetter */
  public static final IntPredicate CHAR_LETTER = Character::isLetter;
  /** (c) -> !Character.isLetter(c) */
  public static final IntPredicate CHAR_NON_LETTER = (c) -> !Character.isLetter(c);
  /** Character::isLetterOrDigit */
  public static final IntPredicate CHAR_LETTER_OR_DIGIT = Character::isLetterOrDigit;
  /** (c) -> !Character.isLetterOrDigit(c) */
  public static final IntPredicate CHAR_NON_LETTER_OR_DIGIT = (c) -> !Character.isLetterOrDigit(c);
  /** Character::isAlphabetic */
  public static final IntPredicate CHAR_ALPHABETIC = Character::isAlphabetic;
  /** (c) -> !Character.isAlphabetic(c) */
  public static final IntPredicate CHAR_NON_ALPHABETIC = (c) -> !Character.isAlphabetic(c);
  /** Character::isLowerCase */
  public static final IntPredicate CHAR_LOWER_CASE = Character::isLowerCase;
  /** (c) -> !Character.isLowerCase(c) */
  public static final IntPredicate CHAR_NON_LOWER_CASE = (c) -> !Character.isLowerCase(c);
  /** Character::isUpperCase */
  public static final IntPredicate CHAR_UPPER_CASE = Character::isUpperCase;
  /** (c) -> !Character.isUpperCase(c) */
  public static final IntPredicate CHAR_NON_UPPER_CASE = (c) -> !Character.isUpperCase(c);
  /** Character::isTitleCase */
  public static final IntPredicate CHAR_TITLE_CASE = Character::isTitleCase;
  /** (c) -> !Character.isTitleCase(c) */
  public static final IntPredicate CHAR_NON_TITLE_CASE = (c) -> !Character.isTitleCase(c);
  /** Character::isISOControl */
  public static final IntPredicate CHAR_ISO_CONTROL = Character::isISOControl;
  /** (c) -> !Character.isISOControl(c) */
  public static final IntPredicate CHAR_NON_ISO_CONTROL = (c) -> !Character.isISOControl(c);

  // String predicates

  /** Strings::isEmpty */
  public static final Predicate<String> STRING_EMPTY = Strings::isEmpty;
  /** Strings::isEmptyOrNull */
  public static final Predicate<String> STRING_EMPTY_OR_NULL = Strings::isEmptyOrNull;
  /** Strings::isNonEmpty */
  public static final Predicate<String> STRING_NON_EMPTY = Strings::isNonEmpty;
  /** Strings::isBlank */
  public static final Predicate<String> STRING_BLANK = Strings::isBlank;
  /** Strings::isBlankOrNull */
  public static final Predicate<String> STRING_BLANK_OR_NULL = Strings::isBlankOrNull;
  /** Strings::isNonBlank */
  public static final Predicate<String> STRING_NON_BLANK = Strings::isNonBlank;
  /** Strings::isWhitespaced */
  public static final Predicate<String> STRING_WHITESPACED = Strings::isWhitespaced;
  /** Strings::isWhitespacedOrNull */
  public static final Predicate<String> STRING_WHITESPACED_OR_NULL = Strings::isWhitespacedOrNull;
  /** Strings::isNonWhitespaced */
  public static final Predicate<String> STRING_NON_WHITESPACED = Strings::isNonWhitespaced;

  // Array predicates

  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null && a.length > 0} */
  public static final Predicate<short[]> SHORT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<short[]> SHORT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<int[]> INT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<int[]> INT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<long[]> LONG_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<long[]> LONG_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<char[]> CHAR_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<char[]> CHAR_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code (a) -> a == null || a.length > 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code (a) -> a != null && a.length > 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;

  // Collection and map predicates

  /** {@code (c) -> c == null || c.size() > 0} */
  public static final Predicate<Collection<?>> COLLECTION_NON_EMPTY = (c) -> c == null || c.size() > 0;
  /** {@code (c) -> c != null && c.size() > 0} */
  public static final Predicate<Collection<?>> COLLECTION_NON_EMPTY_OR_NULL = (c) -> c != null && c.size() > 0;
  /** {@code (m) -> m == null || m.size() > 0} */
  public static final Predicate<Map<?, ?>> MAP_NON_EMPTY = (m) -> m == null || m.size() > 0;
  /** {@code (m) -> m != null && m.size() > 0} */
  public static final Predicate<Map<?, ?>> MAP_NON_EMPTY_OR_NULL = (m) -> m != null && m.size() > 0;

  // Index predicates

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<byte[]> checkByteArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<short[]> checkShortArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<int[]> checkIntArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<long[]> checkLongArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<float[]> checkFloatArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<double[]> checkDoubleArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<char[]> checkCharArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static Predicate<boolean[]> checkBooleanArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (a) -> a == null || index >= 0 && index < a.length} */
  public static <T> Predicate<T[]> checkObjectArrayIndex(int index) {
    return (a) -> a == null || index >= 0 && index < a.length;
  }

  /** {@code (s) -> s == null || index >= 0 && index < s.length()} */
  public static Predicate<CharSequence> checkCharSequenceIndex(int index) {
    return (s) -> s == null || index >= 0 && index < s.length();
  }

  // Range predicates

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<byte[]> checkByteArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<byte[]> checkByteArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<short[]> checkShortArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<short[]> checkShortArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<int[]> checkIntArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<int[]> checkIntArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<long[]> checkLongArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<long[]> checkLongArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<float[]> checkFloatArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<float[]> checkFloatArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<double[]> checkDoubleArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<double[]> checkDoubleArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<char[]> checkCharArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<char[]> checkCharArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static Predicate<boolean[]> checkBooleanArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static Predicate<boolean[]> checkBooleanArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= a.length} */
  public static <T> Predicate<T[]> checkObjectArrayRange(int start) {
    return (a) -> a == null || start >= 0 && start <= a.length;
  }

  /** {@code (a) -> a == null || start >= 0 && start <= end && end <= a.length} */
  public static <T> Predicate<T[]> checkObjectArrayRange(int start, int end) {
    return (a) -> a == null || start >= 0 && start <= end && end <= a.length;
  }

  /** {@code (s) -> s == null || start >= 0 && start <= s.length()} */
  public static Predicate<CharSequence> checkCharSequenceRange(int start) {
    return (s) -> s == null || start >= 0 && start <= s.length();
  }

  /** (s) -> s == null || start >= 0 && start <= end && end <= s.length()} */
  public static Predicate<CharSequence> checkCharSequenceRange(int start, int end) {
    return (s) -> s == null || start >= 0 && start <= end && end <= s.length();
  }

  // Miscellaneous predicates

  /** (s) -> s == null || regex.matcher(s).matches() */
  public static Predicate<CharSequence> match(java.util.regex.Pattern regex) {
    return (s) -> s == null || regex.matcher(s).matches();
  }

  // Simple checks

  // Object

  /**
   * Checks that the specified object reference is not {@code null} and throws
   * {@code NullPointerException} if it is.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * requireNonNull(object, ExceptionProvider.ofNPE())
   * </pre>
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check for {@code null}.
   * @return A reference to the specified object.
   * @throws NullPointerException if the specified object reference is {@code null}.
   */
  public static <T> T requireNonNull(T object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  /**
   * Checks that the specified object reference is not {@code null} and throws
   * {@code NullPointerException} with the specified detail message if it is.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * requireNonNull(object, ExceptionProvider.ofNPE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check for {@code null}.
   * @param message The exception detail message.
   * @return A reference to the specified object.
   * @throws NullPointerException if the specified object reference is {@code null}.
   */
  public static <T> T requireNonNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
    return object;
  }

  /**
   * Checks that the specified object reference is not {@code null} and throws an exception
   * created by the specified exception provider if it is.
   *
   * @param <T> The type of the object.
   * @param <E> The type of the exception to throw.
   * @param object The object reference to check for {@code null}.
   * @param exception The provider of the exception to throw.
   * @return A reference to the specified object.
   * @throws E if the specified object reference is {@code null}.
   */
  public static <T, E extends Throwable> T requireNonNull(T object,
      ExceptionProvider<T, E> exception) throws E {
    if (object == null) {
      throw exception.create(null);
    }
    return object;
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(object, condition, ExceptionProvider.ofIAE())
   * </pre>
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check against condition.
   * @param condition The predicate to apply to the object.
   * @return A reference to the specified object.
   * @throws IllegalArgumentException if the specified object does not satisfy the specified
   *         condition.
   */
  public static <T> T require(T object, Predicate<? super T> condition) {
    return require(object, condition, ExceptionProvider.ofIAE());
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(object, condition, ExceptionProvider.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check against condition.
   * @param condition The predicate to apply to the object.
   * @param message The exception detail message.
   * @return A reference to the specified object.
   * @throws IllegalArgumentException if the specified object does not satisfy the specified
   *         condition.
   */
  public static <T> T require(T object, Predicate<? super T> condition, String message) {
    return require(object, condition, ExceptionProvider.ofIAE(message));
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws an exception
   * created by the specified exception provider if it is not.
   *
   * @param <T> The type of the object.
   * @param <E> The type of the exception to throw.
   * @param object The object reference to check against condition.
   * @param condition The predicate to apply to the object.
   * @param exception The provider of the exception.
   * @return A reference to the specified object.
   * @throws E if the specified object does not satisfy the specified condition.
   */
  public static <T, E extends Throwable> T require(T object, Predicate<? super T> condition,
      ExceptionProvider<T, E> exception) throws E {
    if (condition.test(object)) {
      return object;
    }
    throw exception.create(object);
  }

  // int

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE())
   * </pre>
   * </p>
   *
   * @param number The {@code int} number to check against condition.
   * @param condition The predicate to apply to the {@code int} number.
   * @return The specified {@code int} number.
   * @throws IllegalArgumentException if the specified {@code int} number does not satisfy the
   *         specified condition.
   */
  public static int require(int number, IntPredicate condition) {
    return require(number, condition, ExceptionProvider.ofIAE());
  }

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param number The {@code int} number to check against condition.
   * @param condition The predicate to apply to the {@code int} number.
   * @param message The exception detail message.
   * @return The specified {@code int} number.
   * @throws IllegalArgumentException if the specified {@code int} number does not satisfy the
   *         specified condition.
   */
  public static int require(int number, IntPredicate condition, String message) {
    return require(number, condition, ExceptionProvider.ofIAE(message));
  }

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws an
   * exception created by the specified exception provider if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code int} number to check against condition.
   * @param condition The predicate to apply to the {@code int} number.
   * @param exception The provider of the exception.
   * @return The specified {@code int} number.
   * @throws E if the specified {@code int} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> int require(int number, IntPredicate condition,
      ExceptionProvider<Integer, E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.create(Integer.valueOf(number));
  }

  // long

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE())
   * </pre>
   * </p>
   *
   * @param number The {@code long} number to check against condition.
   * @param condition The predicate to apply to the {@code long} number.
   * @return The specified {@code long} number.
   * @throws IllegalArgumentException if the specified {@code long} number does not satisfy the
   *         specified condition.
   */
  public static long require(long number, LongPredicate condition) {
    return require(number, condition, ExceptionProvider.ofIAE());
  }

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param number The {@code long} number to check against condition.
   * @param condition The predicate to apply to the {@code long} number.
   * @param message The exception detail message.
   * @return The specified {@code long} number.
   * @throws IllegalArgumentException if the specified {@code long} number does not satisfy the
   *         specified condition.
   */
  public static long require(long number, LongPredicate condition, String message) {
    return require(number, condition, ExceptionProvider.ofIAE(message));
  }

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws an
   * exception created by the specified exception provider if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code long} number to check against condition.
   * @param condition The predicate to apply to the {@code long} number.
   * @param exception The provider of the exception.
   * @return The specified {@code long} number.
   * @throws E if the specified {@code long} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> long require(long number, LongPredicate condition,
      ExceptionProvider<Long, E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.create(Long.valueOf(number));
  }

  // double

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE())
   * </pre>
   * </p>
   *
   * @param number The {@code double} number to check against condition.
   * @param condition The predicate to apply to the {@code double} number.
   * @return The specified {@code double} number.
   * @throws IllegalArgumentException if the specified {@code double} number does not satisfy the
   *         specified condition.
   */
  public static double require(double number, DoublePredicate condition) {
    return require(number, condition, ExceptionProvider.ofIAE());
  }

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * require(number, condition, ExceptionProvider.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param number The {@code double} number to check against condition.
   * @param condition The predicate to apply to the {@code double} number.
   * @param message The exception detail message.
   * @return The specified {@code double} number.
   * @throws IllegalArgumentException if the specified {@code double} number does not satisfy the
   *         specified condition.
   */
  public static double require(double number, DoublePredicate condition, String message) {
    return require(number, condition, ExceptionProvider.ofIAE(message));
  }

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * an exception created by the specified exception provider if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code double} number to check against condition.
   * @param condition The predicate to apply to the {@code double} number.
   * @param exception The provider of the exception.
   * @return The specified {@code double} number.
   * @throws E if the specified {@code double} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> double require(double number, DoublePredicate condition,
      ExceptionProvider<Double, E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.create(Double.valueOf(number));
  }

  // Array checks

  // Object[]

  /**
   * Checks that the specified array does not contain {@code null} elements and throws
   * {@code NullPointerException} if it does. Note that if the specified array is {@code null} then
   * no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAllNonNull(array, ExceptionProvider.OfSequence.ofNPE())
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check for {@code null}.
   * @return A reference to the specified array.
   * @throws NullPointerException if the specified array contains {@code null} elements.
   */
  public static <T> T[] requireAllNonNull(T[] array) {
    return requireAllNonNull(array, ExceptionProvider.OfSequence.ofNPE());
  }

  /**
   * Checks that the specified array does not contain {@code null} elements and throws
   * {@code NullPointerException} with the specified detail message if it does. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAllNonNull(array, ExceptionProvider.OfSequence.ofNPE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check for {@code null}.
   * @param message The exception detail message.
   * @return A reference to the specified array.
   * @throws NullPointerException if the specified array contains {@code null} elements.
   */
  public static <T> T[] requireAllNonNull(T[] array, String message) {
    return requireAllNonNull(array, ExceptionProvider.OfSequence.ofNPE(message));
  }

  /**
   * Checks that the specified array does not contain {@code null} elements and throws an exception
   * created by the specified exception provider if it does. Note that if the specified array is
   * {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the array.
   * @param <E> The type of the exception to throw.
   * @param array The array whose elements to check for {@code null}.
   * @param exception The provider of the exception.
   * @return A reference to the specified array.
   * @throws E if the specified array contains {@code null} elements.
   */
  public static <T, E extends Throwable> T[] requireAllNonNull(T[] array,
      ExceptionProvider.OfSequence<T[], T, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (array[index] == null) {
        throw exception.create(array, index, null);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * {@code IllegalArgumentException} if they are not. Note that if the specified array is
   * {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the array.
   * @return A reference to the specified array.
   * @throws IllegalArgumentException if at least one element of the specified array does not
   *         satisfy the specified condition.
   */
  public static <T> T[] requireAll(T[] array, Predicate<? super T> condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if they are not. Note that
   * if the specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the array.
   * @param message The exception detail message.
   * @return A reference to the specified array.
   * @throws IllegalArgumentException if at least one element of the specified array does not
   *         satisfy the specified condition.
   */
  public static <T> T[] requireAll(T[] array, Predicate<? super T> condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * throws an exception created by the specified exception provider if they are not. Note that if
   * the specified array is {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the array.
   * @param <E> The type of the exception to throw.
   * @param array The array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the array.
   * @param exception The provider of the exception.
   * @return A reference to the specified array.
   * @throws E if at least one element of the specified array does not satisfy the specified
   *         condition.
   */
  public static <T, E extends Throwable> T[] requireAll(T[] array, Predicate<? super T> condition,
      ExceptionProvider.OfSequence<T[], T, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, array[index]);
      }
    }
    return array;
  }

  // byte[]

  /**
   * Checks that all the elements of the specified {@code byte[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code byte[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @return A reference to the specified {@code byte[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code byte[]} array
   *         does not satisfy the specified condition.
   */
  public static byte[] requireAll(byte[] array, IntPredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code byte[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code byte[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code byte[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code byte[]} array
   *         does not satisfy the specified condition.
   */
  public static byte[] requireAll(byte[] array, IntPredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code byte[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code byte[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code byte[]} array.
   * @throws E if at least one element of the specified {@code byte[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> byte[] requireAll(byte[] array, IntPredicate condition,
      ExceptionProvider.OfSequence<byte[], Byte, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Byte.valueOf(array[index]));
      }
    }
    return array;
  }

  // short[]

  /**
   * Checks that all the elements of the specified {@code short[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code short[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @return A reference to the specified {@code short[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code short[]}
   *         array does not satisfy the specified condition.
   */
  public static short[] requireAll(short[] array, IntPredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code short[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code short[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code short[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code short[]}
   *         array does not satisfy the specified condition.
   */
  public static short[] requireAll(short[] array, IntPredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code short[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code short[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code short[]} array.
   * @throws E if at least one element of the specified {@code short[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> short[] requireAll(short[] array, IntPredicate condition,
      ExceptionProvider.OfSequence<short[], Short, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Short.valueOf(array[index]));
      }
    }
    return array;
  }

  // int[]

  /**
   * Checks that all the elements of the specified {@code int[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code int[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @return A reference to the specified {@code int[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code int[]} array
   *         does not satisfy the specified condition.
   */
  public static int[] requireAll(int[] array, IntPredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code int[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code int[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code int[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code int[]} array
   *         does not satisfy the specified condition.
   */
  public static int[] requireAll(int[] array, IntPredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code int[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code int[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code int[]} array.
   * @throws E if at least one element of the specified {@code int[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> int[] requireAll(int[] array, IntPredicate condition,
      ExceptionProvider.OfSequence<int[], Integer, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Integer.valueOf(array[index]));
      }
    }
    return array;
  }

  // long[]

  /**
   * Checks that all the elements of the specified {@code long[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code long[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @return A reference to the specified {@code long[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code long[]} array
   *         does not satisfy the specified condition.
   */
  public static long[] requireAll(long[] array, LongPredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code long[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code long[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code long[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code long[]} array
   *         does not satisfy the specified condition.
   */
  public static long[] requireAll(long[] array, LongPredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code long[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code long[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code long[]} array.
   * @throws E if at least one element of the specified {@code long[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> long[] requireAll(long[] array, LongPredicate condition,
      ExceptionProvider.OfSequence<long[], Long, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Long.valueOf(array[index]));
      }
    }
    return array;
  }

  // float[]

  /**
   * Checks that all the elements of the specified {@code float[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code float[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @return A reference to the specified {@code float[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code float[]}
   *         array does not satisfy the specified condition.
   */
  public static float[] requireAll(float[] array, DoublePredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code float[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code float[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code float[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code float[]}
   *         array does not satisfy the specified condition.
   */
  public static float[] requireAll(float[] array, DoublePredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code float[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code float[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code float[]} array.
   * @throws E if at least one element of the specified {@code float[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> float[] requireAll(float[] array, DoublePredicate condition,
      ExceptionProvider.OfSequence<float[], Float, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Float.valueOf(array[index]));
      }
    }
    return array;
  }

  // double[]

  /**
   * Checks that all the elements of the specified {@code double[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code double[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @return A reference to the specified {@code double[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code double[]}
   *         array does not satisfy the specified condition.
   */
  public static double[] requireAll(double[] array, DoublePredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code double[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code double[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code double[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code double[]}
   *         array does not satisfy the specified condition.
   */
  public static double[] requireAll(double[] array, DoublePredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code double[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code double[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code double[]} array.
   * @throws E if at least one element of the specified {@code double[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> double[] requireAll(double[] array, DoublePredicate condition,
      ExceptionProvider.OfSequence<double[], Double, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Double.valueOf(array[index]));
      }
    }
    return array;
  }

  // char[]

  /**
   * Checks that all the elements of the specified {@code char[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param array The {@code char[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @return A reference to the specified {@code char[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code char[]} array
   *         does not satisfy the specified condition.
   */
  public static char[] requireAll(char[] array, IntPredicate condition) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code char[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param array The {@code char[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @param message The exception detail message.
   * @return A reference to the specified {@code char[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code char[]} array
   *         does not satisfy the specified condition.
   */
  public static char[] requireAll(char[] array, IntPredicate condition, String message) {
    return requireAll(array, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code char[]} array satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code char[]} array whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code char[]} array.
   * @throws E if at least one element of the specified {@code char[]} array does not satisfy the
   *         specified condition.
   */
  public static <E extends Throwable> char[] requireAll(char[] array, IntPredicate condition,
      ExceptionProvider.OfSequence<char[], Character, E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.create(array, index, Character.valueOf(array[index]));
      }
    }
    return array;
  }

  // Iterable checks

  /**
   * Checks that the specified {@code Iterable} sequence does not contain {@code null} elements and
   * throws {@code NullPointerException} if it does. Note that if the specified sequence is
   * {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAllNonNull(iterable, ExceptionProvider.OfSequence.ofNPE())
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check for {@code null}.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws NullPointerException if the specified {@code Iterable} sequence contains {@code null}
   *         elements.
   */
  public static <T, I extends Iterable<T>> I requireAllNonNull(I iterable) {
    return requireAllNonNull(iterable, ExceptionProvider.OfSequence.ofNPE());
  }

  /**
   * Checks that the specified {@code Iterable} sequence does not contain {@code null} elements and
   * throws {@code NullPointerException} with the specified detail message if it does. Note that if
   * the specified sequence is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAllNonNull(iterable, ExceptionProvider.OfSequence.ofNPE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check for {@code null}.
   * @param message The exception detail message.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws NullPointerException if the specified {@code Iterable} sequence contains {@code null}
   *         elements.
   */
  public static <T, I extends Iterable<T>> I requireAllNonNull(I iterable, String message) {
    return requireAllNonNull(iterable, ExceptionProvider.OfSequence.ofNPE(message));
  }

  /**
   * Checks that the specified {@code Iterable} sequence does not contain {@code null} elements and
   * throws an exception created by the specified exception provider if it does. Note that if the
   * specified sequence is {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param <E> The type of the exception to throw.
   * @param iterable The {@code Iterable} sequence whose elements to check for {@code null}.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws E if the specified {@code Iterable} sequence contains {@code null} elements.
   */
  public static <T, I extends Iterable<T>, E extends Throwable> I requireAllNonNull(I iterable,
      ExceptionProvider.OfSequence<I, T, E> exception) throws E {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        if (itr.next() == null) {
          throw exception.create(iterable, index, null);
        }
      }
    }
    return iterable;
  }

  /**
   * Checks that all the elements of the specified {@code Iterable} sequence satisfy the specified
   * condition and throws {@code IllegalArgumentException} if they are not. Note that if the
   * specified sequence is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(iterable, condition, ExceptionProvider.OfSequence.ofIAE())
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws IllegalArgumentException if at least one element of the specified {@code Iterable}
   *         sequence does not satisfy the specified condition.
   */
  public static <T, I extends Iterable<T>> I requireAll(I iterable, Predicate<? super T> condition) {
    return requireAll(iterable, condition, ExceptionProvider.OfSequence.ofIAE());
  }

  /**
   * Checks that all the elements of the specified {@code Iterable} sequence satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified sequence is {@code null} then no exception will be
   * thrown.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * requireAll(iterable, condition, ExceptionProvider.OfSequence.ofIAE(message))
   * </pre>
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @param message The exception detail message.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws IllegalArgumentException if at least one element of the specified {@code Iterable}
   *         sequence does not satisfy the specified condition.
   */
  public static <T, I extends Iterable<T>> I requireAll(I iterable, Predicate<? super T> condition, String message) {
    return requireAll(iterable, condition, ExceptionProvider.OfSequence.ofIAE(message));
  }

  /**
   * Checks that all the elements of the specified {@code Iterable} sequence satisfy the specified
   * condition and throws an exception created by the specified exception provider if they are not.
   * Note that if the specified sequence is {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param <E> The type of the exception to throw.
   * @param iterable The {@code Iterable} sequence whose elements to check against condition.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @param exception The provider of the exception.
   * @return A reference to the specified {@code Iterable} sequence.
   * @throws E if at least one element of the specified {@code Iterable} sequence does not satisfy
   *         the specified condition.
   */
  public static <T, I extends Iterable<T>, E extends Throwable> I requireAll(I iterable,
      Predicate<? super T> condition, ExceptionProvider.OfSequence<I, T, E> exception) throws E {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (!condition.test(element)) {
          throw exception.create(iterable, index, element);
        }
      }
    }
    return iterable;
  }

  // ExceptionProvider

  /**
   * A provider of exceptions that will be thrown by the {@code requireNonNull()} and
   * {@code require()} methods to indicate that an object does not satisfy the required condition.
   *
   * @param <T> The type of the object.
   * @param <E> The type of the exception to be thrown.
   *
   * @author Fox Mulder
   */
  @FunctionalInterface
  public static interface ExceptionProvider<T, E extends Throwable> {

    /**
     * Returns a {@code Throwable} instance to be thrown for the specified reference to an invalid
     * object.
     *
     * @param object The reference to an invalid object.
     * @return A {@code Throwable} instance to be thrown.
     */
    E create(T object);

    // NullPointerException

    /**
     * A shortcut for the:
     * <code>
     * (o) -> new NullPointerException("Cannot be null")
     * </code>
     */
    static <T> ExceptionProvider<T, NullPointerException> ofNPE() {
      return (o) -> new NullPointerException("Cannot be null");
    }

    /**
     * A shortcut for the:
     * <code>
     * (o) -> new NullPointerException(message)
     * </code>
     */
    static <T> ExceptionProvider<T, NullPointerException> ofNPE(String message) {
      return (o) -> new NullPointerException(message);
    }

    // IllegalArgumentException

    /**
     * A shortcut for the:
     * <code>
     * ofIAE("Invalid argument: %s")
     * </code>
     */
    static <T> ExceptionProvider<T, IllegalArgumentException> ofIAE() {
      return ofIAE("Invalid argument: %s");
    }

    /**
     * A shortcut for the:
     * <code>
     * (o) -> new IllegalArgumentException(String.format(message, Objects.toString(o)))
     * </code>
     */
    static <T> ExceptionProvider<T, IllegalArgumentException> ofIAE(String message) {
      return (o) -> new IllegalArgumentException(String.format(message, Objects.toString(o)));
    }

    // IndexOutOfBoundsException

    /**
     * A shortcut for the:
     * <code>
     * ofIOOB("Index out of range: [0 <= %d < %d]", index)
     * </code>
     */
    static <T> ExceptionProvider<T, IndexOutOfBoundsException> ofIOOB(int index) {
      return ofIOOB("Index out of range: [0 <= %d < %d]", index);
    }

    /**
     * A shortcut for the:
     * <code>
     * (a) -> new IndexOutOfBoundsException(String.format(message, index, Array.getLength(a)))
     * </code>
     */
    static <T> ExceptionProvider<T, IndexOutOfBoundsException> ofIOOB(String message, int index) {
      return (a) -> new IndexOutOfBoundsException(String.format(message, index, Array.getLength(a)));
    }

    /**
     * A shortcut for the:
     * <code>
     * ofIOOB("Invalid range: [0 <= %d <= %d <= %d]", start, end)
     * </code>
     */
    static <T> ExceptionProvider<T, IndexOutOfBoundsException> ofIOOB(int start, int end) {
      return ofIOOB("Invalid range: [0 <= %d <= %d <= %d]", start, end);
    }

    /**
     * A shortcut for the:
     * <code>
     * (a) -> new IndexOutOfBoundsException(String.format(message, start, end, Array.getLength(a)))
     * </code>
     */
    static <T> ExceptionProvider<T, IndexOutOfBoundsException> ofIOOB(String message, int start, int end) {
      return (a) -> new IndexOutOfBoundsException(String.format(message, start, end, Array.getLength(a)));
    }

    // ExceptionProvider.OfSequence

    /**
     * A provider of exceptions that will be thrown by the {@code requireAllNonNull()} and
     * {@code requireAll()} methods to indicate that an element of the sequence does not
     * satisfy the required condition.
     *
     * @param <S> The type of the sequence (either array or {@code Iterable}).
     * @param <T> The type of elements of the sequence.
     * @param <E> The type of the exception to be thrown.
     *
     * @author Fox Mulder
     */
    @FunctionalInterface
    public static interface OfSequence<S, T, E extends Throwable> {

      /**
       * Returns a {@code Throwable} instance to be thrown for the specified sequence, index of an
       * invalid element, and reference to it.
       *
       * @param sequence The sequence that contains an invalid element.
       * @param index The index of an invalid element in the sequence.
       * @param element The reference to an invalid element of the sequence.
       * @return A {@code Throwable} instance to be thrown.
       */
      E create(S sequence, int index, T element);

      // NullPointerException

      /**
       * A shortcut for the:
       * <code>
       * ofNPE("Element [%d] cannot be null")
       * </code>
       */
      static <S, T> OfSequence<S, T, NullPointerException> ofNPE() {
        return ofNPE("Element [%d] cannot be null");
      }

      /**
       * A shortcut for the:
       * <code>
       * (s, i, e) -> new NullPointerException(String.format(message, i))
       * </code>
       */
      static <S, T> OfSequence<S, T, NullPointerException> ofNPE(String message) {
        return (s, i, e) -> new NullPointerException(String.format(message, i));
      }

      // IllegalArgumentException

      /**
       * A shortcut for the:
       * <code>
       * ofIAE("Invalid element [%d]: %s")
       * </code>
       */
      static <S, T> OfSequence<S, T, IllegalArgumentException> ofIAE() {
        return ofIAE("Invalid element [%d]: %s");
      }

      /**
       * A shortcut for the:
       * <code>
       * (s, i, e) -> new IllegalArgumentException(String.format(message, i, Objects.toString(e)))
       * </code>
       */
      static <S, T> OfSequence<S, T, IllegalArgumentException> ofIAE(String message) {
        return (s, i, e) -> new IllegalArgumentException(String.format(message, i, Objects.toString(e)));
      }

    }

  }

}
