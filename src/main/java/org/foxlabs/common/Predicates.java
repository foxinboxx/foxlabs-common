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

import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;

/**
 * A collection of reusable predicates and {@code requireXXX()} methods that check that a given
 * value satisfies a given condition.
 *
 * @author Fox Mulder
 */
public final class Predicates {

  // Instantiation is not possible
  private Predicates() {
    throw new IllegalAccessError();
  }

  // Object predicates

  /** {@code o == null} */
  public static final Predicate<Object> OBJECT_NULL = (o) -> o == null;
  /** {@code o != null} */
  public static final Predicate<Object> OBJECT_NON_NULL = (o) -> o != null;

  // Integer predicates

  /** {@code n == 0} */
  public static final IntPredicate INT_EQ_ZERO = (n) -> n == 0;
  /** {@code n != 0} */
  public static final IntPredicate INT_NE_ZERO = (n) -> n != 0;
  /** {@code n > 0} */
  public static final IntPredicate INT_POSITIVE = (n) -> n > 0;
  /** {@code n >= 0} */
  public static final IntPredicate INT_POSITIVE_OR_ZERO = (n) -> n >= 0;
  /** {@code n < 0} */
  public static final IntPredicate INT_NEGATIVE = (n) -> n < 0;
  /** {@code n <= 0} */
  public static final IntPredicate INT_NEGATIVE_OR_ZERO = (n) -> n <= 0;

  /** {@code n != null && n.intValue() == 0} */
  public static final Predicate<Integer> OBJECT_INT_EQ_ZERO = (n) -> n != null && n.intValue() == 0;
  /** {@code n != null && n.intValue() != 0} */
  public static final Predicate<Integer> OBJECT_INT_NE_ZERO = (n) -> n != null && n.intValue() != 0;
  /** {@code n != null && n.intValue() > 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE = (n) -> n != null && n.intValue() > 0;
  /** {@code n != null && n.intValue() >= 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE_OR_ZERO = (n) -> n != null && n.intValue() >= 0;
  /** {@code n != null && n.intValue() < 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE = (n) -> n != null && n.intValue() < 0;
  /** {@code n != null && n.intValue() <= 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE_OR_ZERO = (n) -> n != null && n.intValue() <= 0;

  // Long predicates

  /** {@code n == 0L} */
  public static final LongPredicate LONG_EQ_ZERO = (n) -> n == 0L;
  /** {@code n != 0L} */
  public static final LongPredicate LONG_NE_ZERO = (n) -> n != 0L;
  /** {@code n > 0L} */
  public static final LongPredicate LONG_POSITIVE = (n) -> n > 0L;
  /** {@code n >= 0L} */
  public static final LongPredicate LONG_POSITIVE_OR_ZERO = (n) -> n >= 0L;
  /** {@code n < 0L} */
  public static final LongPredicate LONG_NEGATIVE = (n) -> n < 0L;
  /** {@code n <= 0L} */
  public static final LongPredicate LONG_NEGATIVE_OR_ZERO = (n) -> n <= 0L;

  /** {@code n != null && n.longValue() == 0L} */
  public static final Predicate<Long> OBJECT_LONG_EQ_ZERO = (n) -> n != null && n.longValue() == 0L;
  /** {@code n != null && n.longValue() != 0L} */
  public static final Predicate<Long> OBJECT_LONG_NE_ZERO = (n) -> n != null && n.longValue() != 0L;
  /** {@code n != null && n.longValue() > 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE = (n) -> n != null && n.longValue() > 0L;
  /** {@code n != null && n.longValue() >= 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE_OR_ZERO = (n) -> n != null && n.longValue() >= 0L;
  /** {@code n != null && n.longValue() < 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE = (n) -> n != null && n.longValue() < 0L;
  /** {@code n != null && n.longValue() <= 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE_OR_ZERO = (n) -> n != null && n.longValue() <= 0L;

  // Float predicates

  /** {@code n != null && n.floatValue() == .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_EQ_ZERO = (n) -> n != null && n.floatValue() == .0f;
  /** {@code n != null && n.floatValue() != .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NE_ZERO = (n) -> n != null && n.floatValue() != .0f;
  /** {@code n != null && n.floatValue() > .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE = (n) -> n != null && n.floatValue() > .0f;
  /** {@code n != null && n.floatValue() >= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE_OR_ZERO = (n) -> n != null && n.floatValue() >= .0f;
  /** {@code n != null && n.floatValue() < .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE = (n) -> n != null && n.floatValue() < .0f;
  /** {@code n != null && n.floatValue() <= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE_OR_ZERO = (n) -> n != null && n.floatValue() <= .0f;
  /** {@code n != null && n.isNaN() } */
  public static final Predicate<Float> OBJECT_FLOAT_NAN = (n) -> n != null && n.isNaN();
  /** {@code n != null && n.isFinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_FINITE = (n) -> n != null && n.isInfinite();
  /** {@code n != null && n.isInfinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_INFINITE = (n) -> n != null && n.isInfinite();

  // Double predicates

  /** {@code n == .0d} */
  public static final DoublePredicate DOUBLE_EQ_ZERO = (n) -> n == .0d;
  /** {@code n != .0d} */
  public static final DoublePredicate DOUBLE_NE_ZERO = (n) -> n != .0d;
  /** {@code n > .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE = (n) -> n > .0d;
  /** {@code n >= .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE_OR_ZERO = (n) -> n >= .0d;
  /** {@code n < .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE = (n) -> n < .0d;
  /** {@code n <= .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE_OR_ZERO = (n) -> n <= .0d;
  /** {@code Double.isNaN(n) } */
  public static final DoublePredicate DOUBLE_NAN = Double::isNaN;
  /** {@code Double.isFinite(n) } */
  public static final DoublePredicate DOUBLE_FINITE = Double::isFinite;
  /** {@code Double.isInfinite(n) } */
  public static final DoublePredicate DOUBLE_INFINITE = Double::isInfinite;

  /** {@code n != null && n.doubleValue() == .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_EQ_ZERO = (n) -> n != null && n.doubleValue() == .0d;
  /** {@code n != null && n.doubleValue() != .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NE_ZERO = (n) -> n != null && n.doubleValue() != .0d;
  /** {@code n != null && n.doubleValue() > .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE = (n) -> n != null && n.doubleValue() > .0d;
  /** {@code n != null && n.doubleValue() >= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE_OR_ZERO = (n) -> n != null && n.doubleValue() >= .0d;
  /** {@code n != null && n.doubleValue() < .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE = (n) -> n != null && n.doubleValue() < .0d;
  /** {@code n != null && n.doubleValue() <= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE_OR_ZERO = (n) -> n != null && n.doubleValue() <= .0d;
  /** {@code n != null && n.isNaN() } */
  public static final Predicate<Double> OBJECT_DOUBLE_NAN = (n) -> n != null && n.isNaN();
  /** {@code n != null && n.isFinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_FINITE = (n) -> n != null && n.isInfinite();
  /** {@code n != null && n.isInfinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_INFINITE = (n) -> n != null && n.isInfinite();

  // Character code point predicates

  /** Character.isSpace(c) */
  public static final IntPredicate CHAR_SPACE = Character::isSpaceChar;
  /** !Character.isSpace(c) */
  public static final IntPredicate CHAR_NON_SPACE = (c) -> !Character.isSpaceChar(c);
  /** Character.isWhitespace(c) */
  public static final IntPredicate CHAR_WHITESPACE = Character::isWhitespace;
  /** !Character.isWhitespace(c) */
  public static final IntPredicate CHAR_NON_WHITESPACE = (c) -> !Character.isWhitespace(c);
  /** Character.isDigit(c) */
  public static final IntPredicate CHAR_DIGIT = Character::isDigit;
  /** !Character.isDigit(c) */
  public static final IntPredicate CHAR_NON_DIGIT = (c) -> !Character.isDigit(c);
  /** Character.isLetter(c) */
  public static final IntPredicate CHAR_LETTER = Character::isLetter;
  /** !Character.isLetter(c) */
  public static final IntPredicate CHAR_NON_LETTER = (c) -> !Character.isLetter(c);
  /** Character.isLetterOrDigit(c) */
  public static final IntPredicate CHAR_LETTER_OR_DIGIT = Character::isLetterOrDigit;
  /** !Character.isLetterOrDigit(c) */
  public static final IntPredicate CHAR_NON_LETTER_OR_DIGIT = (c) -> !Character.isLetterOrDigit(c);
  /** Character.isAlphabetic(c) */
  public static final IntPredicate CHAR_ALPHABETIC = Character::isAlphabetic;
  /** !Character.isAlphabetic(c) */
  public static final IntPredicate CHAR_NON_ALPHABETIC = (c) -> !Character.isAlphabetic(c);
  /** Character.isLowerCase(c) */
  public static final IntPredicate CHAR_LOWER_CASE = Character::isLowerCase;
  /** !Character.isLowerCase(c) */
  public static final IntPredicate CHAR_NON_LOWER_CASE = (c) -> !Character.isLowerCase(c);
  /** Character.isUpperCase(c) */
  public static final IntPredicate CHAR_UPPER_CASE = Character::isUpperCase;
  /** !Character.isUpperCase(c) */
  public static final IntPredicate CHAR_NON_UPPER_CASE = (c) -> !Character.isUpperCase(c);
  /** Character.isTitleCase(c) */
  public static final IntPredicate CHAR_TITLE_CASE = Character::isTitleCase;
  /** !Character.isTitleCase(c) */
  public static final IntPredicate CHAR_NON_TITLE_CASE = (c) -> !Character.isTitleCase(c);
  /** Character.isISOControl(c) */
  public static final IntPredicate CHAR_ISO_CONTROL = Character::isISOControl;
  /** !Character.isISOControl(c) */
  public static final IntPredicate CHAR_NON_ISO_CONTROL = (c) -> !Character.isISOControl(c);

  // String predicates

  /** Strings.isEmpty(s) */
  public static final Predicate<String> STRING_EMPTY = Strings::isEmpty;
  /** Strings.isEmptyOrNull(s) */
  public static final Predicate<String> STRING_EMPTY_OR_NULL = Strings::isEmptyOrNull;
  /** Strings.isNonEmpty(s) */
  public static final Predicate<String> STRING_NON_EMPTY = Strings::isNonEmpty;
  /** Strings.isBlank(s) */
  public static final Predicate<String> STRING_BLANK = Strings::isBlank;
  /** Strings.isBlankOrNull(s) */
  public static final Predicate<String> STRING_BLANK_OR_NULL = Strings::isBlankOrNull;
  /** Strings.isNonBlank(s) */
  public static final Predicate<String> STRING_NON_BLANK = Strings::isNonBlank;
  /** Strings.isWhitespaced(s) */
  public static final Predicate<String> STRING_WHITESPACED = Strings::isWhitespaced;
  /** Strings.isWhitespacedOrNull(s) */
  public static final Predicate<String> STRING_WHITESPACED_OR_NULL = Strings::isWhitespacedOrNull;
  /** Strings.isNonWhitespaced(s) */
  public static final Predicate<String> STRING_NON_WHITESPACED = Strings::isNonWhitespaced;

  // Array predicates

  /** {@code a == null || a.length > 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null && a.length > 0} */
  public static final Predicate<short[]> SHORT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<short[]> SHORT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<int[]> INT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<int[]> INT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<long[]> LONG_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<long[]> LONG_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<char[]> CHAR_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<char[]> CHAR_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;
  /** {@code a == null || a.length > 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_NON_EMPTY = (a) -> a == null || a.length > 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_NON_EMPTY_OR_NULL = (a) -> a != null && a.length > 0;

  // Collection and map predicates

  /** {@code c == null || c.size() > 0} */
  public static final Predicate<Collection<?>> COLLECTION_NON_EMPTY = (c) -> c == null || c.size() > 0;
  /** {@code c != null && c.size() > 0} */
  public static final Predicate<Collection<?>> COLLECTION_NON_EMPTY_OR_NULL = (c) -> c != null && c.size() > 0;
  /** {@code m == null || m.size() > 0} */
  public static final Predicate<Map<?, ?>> MAP_NON_EMPTY = (m) -> m == null || m.size() > 0;
  /** {@code m != null && m.size() > 0} */
  public static final Predicate<Collection<?>> MAP_NON_EMPTY_OR_NULL = (m) -> m != null && m.size() > 0;

  // Miscellaneous predicates

  // Value checks

  // Object

  /**
   * Checks that the specified object reference is not {@code null} and throws
   * {@code NullPointerException} if it is.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * require(object, Predicates.OBJECT_NON_NULL)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @return The reference to the specified object.
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
   * require(object, Predicates.OBJECT_NON_NULL, message)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified object.
   * @throws NullPointerException if the specified object reference is {@code null}.
   * @see #defer(ExceptionProvider)
   */
  public static <T> T requireNonNull(T object, Object message) {
    if (object == null) {
      throw new NullPointerException(toExceptionMessage(message));
    }
    return object;
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param condition The predicate to apply to the object.
   * @return The reference to the specified object.
   * @throws IllegalArgumentException if the specified object does not satisfy the specified
   *         condition.
   */
  public static <T> T require(T object, Predicate<? super T> condition) {
    if (condition.test(object)) {
      return object;
    }
    throw new IllegalArgumentException(String.valueOf(object));
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param condition The predicate to apply to the object.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified object.
   * @throws IllegalArgumentException if the specified object does not satisfy the specified
   *         condition.
   * @see #defer(ExceptionProvider)
   */
  public static <T> T require(T object, Predicate<? super T> condition, Object message) {
    if (condition.test(object)) {
      return object;
    }
    throw new IllegalArgumentException(toExceptionMessage(message));
  }

  /**
   * Checks that the specified object satisfies the specified condition and throws the specified
   * exception if it is not.
   *
   * @param <T> The type of the object.
   * @param <E> The type of the exception to throw.
   * @param object The object reference to check.
   * @param condition The predicate to apply to the object.
   * @param exception The provider of the exception.
   * @return The reference to the specified object.
   * @throws E if the specified object does not satisfy the specified condition.
   */
  public static <T, E extends Throwable> T require(T object, Predicate<? super T> condition,
      ExceptionProvider<E> exception) throws E {
    if (condition.test(object)) {
      return object;
    }
    throw exception.get();
  }

  // int

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * @param number The {@code int} number to check.
   * @param condition The predicate to apply to the {@code int} number.
   * @return The specified {@code int} number.
   * @throws IllegalArgumentException if the specified {@code int} number does not satisfy the
   *         specified condition.
   */
  public static int require(int number, IntPredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(Integer.toString(number));
  }

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * @param number The {@code int} number to check.
   * @param condition The predicate to apply to the {@code int} number.
   * @param message The provider of the exception detail message.
   * @return The specified {@code int} number.
   * @throws IllegalArgumentException if the specified {@code int} number does not satisfy the
   *         specified condition.
   * @see #defer(ExceptionProvider)
   */
  public static int require(int number, IntPredicate condition, Object message) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(toExceptionMessage(message));
  }

  /**
   * Checks that the specified {@code int} number satisfies the specified condition and throws the
   * specified exception if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code int} number to check.
   * @param condition The predicate to apply to the {@code int} number.
   * @param exception The provider of the exception.
   * @return The specified {@code int} number.
   * @throws E if the specified {@code int} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> int require(int number,
      IntPredicate condition, ExceptionProvider<E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.get();
  }

  // long

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * @param number The {@code long} number to check.
   * @param condition The predicate to apply to the {@code long} number.
   * @return The specified {@code long} number.
   * @throws IllegalArgumentException if the specified {@code long} number does not satisfy the
   *         specified condition.
   */
  public static long require(long number, LongPredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(Long.toString(number));
  }

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * @param number The {@code long} number to check.
   * @param condition The predicate to apply to the {@code long} number.
   * @param message The provider of the exception detail message.
   * @return The specified {@code long} number.
   * @throws IllegalArgumentException if the specified {@code long} number does not satisfy the
   *         specified condition.
   * @see #defer(ExceptionProvider)
   */
  public static long require(long number, LongPredicate condition, Object message) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(toExceptionMessage(message));
  }

  /**
   * Checks that the specified {@code long} number satisfies the specified condition and throws the
   * specified exception if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code long} number to check.
   * @param condition The predicate to apply to the {@code long} number.
   * @param exception The provider of the exception.
   * @return The specified {@code long} number.
   * @throws E if the specified {@code long} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> long require(long number, LongPredicate condition,
      ExceptionProvider<E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.get();
  }

  // double

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} if it is not.
   *
   * @param number The {@code double} number to check.
   * @param condition The predicate to apply to the {@code double} number.
   * @return The specified {@code double} number.
   * @throws IllegalArgumentException if the specified {@code double} number does not satisfy the
   *         specified condition.
   */
  public static double require(double number, DoublePredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(Double.toString(number));
  }

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if it is not.
   *
   * @param number The {@code double} number to check.
   * @param condition The predicate to apply to the {@code double} number.
   * @param message The provider of the exception detail message.
   * @return The specified {@code double} number.
   * @throws IllegalArgumentException if the specified {@code double} number does not satisfy the
   *         specified condition.
   * @see #defer(ExceptionProvider)
   */
  public static double require(double number, DoublePredicate condition, Object message) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(toExceptionMessage(message));
  }

  /**
   * Checks that the specified {@code double} number satisfies the specified condition and throws
   * the specified exception if it is not.
   *
   * @param <E> The type of the exception to throw.
   * @param number The {@code double} number to check.
   * @param condition The predicate to apply to the {@code double} number.
   * @param exception The provider of the exception.
   * @return The specified {@code double} number.
   * @throws E if the specified {@code double} number does not satisfy the specified condition.
   */
  public static <E extends Throwable> double require(double number, DoublePredicate condition,
      ExceptionProvider<E> exception) throws E {
    if (condition.test(number)) {
      return number;
    }
    throw exception.get();
  }

  // Array checks

  // Object[]

  /**
   * Checks that the specified array does not contain {@code null} elements and throws
   * {@code NullPointerException} if it does. Note that if the specified array is {@code null} then
   * no exception will be thrown.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * requireElements(array, Predicates.OBJECT_NON_NULL)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check.
   * @return The reference to the specified array.
   * @throws NullPointerException if the specified array contains {@code null} elements.
   */
  public static <T> T[] requireElementsNonNull(T[] array) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (array[index] == null) {
        throw new IllegalArgumentException("[" + index + "] = null");
      }
    }
    return array;
  }

  /**
   * Checks that the specified array does not contain {@code null} elements and throws
   * {@code NullPointerException} with the specified detail message if it does. Note that if the
   * specified array is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * requireElements(array, Predicates.OBJECT_NON_NULL, message)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified array.
   * @throws NullPointerException if the specified array contains {@code null} elements.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static <T> T[] requireElementsNonNull(T[] array, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (array[index] == null) {
        throw new NullPointerException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * {@code IllegalArgumentException} if they are not. Note that if the specified array is
   * {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check.
   * @param condition The predicate to apply for each element of the array.
   * @return The reference to the specified array.
   * @throws IllegalArgumentException if at least one element of the specified array does not
   *         satisfy the specified condition.
   */
  public static <T> T[] requireElements(T[] array, Predicate<? super T> condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * {@code IllegalArgumentException} with the specified detail message if they are not. Note that
   * if the specified array is {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the array.
   * @param array The array whose elements to check.
   * @param condition The predicate to apply for each element of the array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified array.
   * @throws IllegalArgumentException if at least one element of the specified array does not
   *         satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static <T> T[] requireElements(T[] array, Predicate<? super T> condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified array satisfy the specified condition and throws
   * the specified exception if they are not. Note that if the specified array is {@code null} then
   * no exception will be thrown.
   *
   * @param <T> The type of elements of the array.
   * @param <E> The type of the exception to throw.
   * @param array The array whose elements to check.
   * @param condition The predicate to apply for each element of the array.
   * @param exception The provider of the exception.
   * @return The reference to the specified array.
   * @throws IllegalArgumentException if at least one element of the specified array does not
   *         satisfy the specified condition.
   */
  public static <T, E extends Throwable> T[] requireElements(T[] array, Predicate<? super T> condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code byte[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @return The reference to the specified {@code byte[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code byte[]} array
   *         does not satisfy the specified condition.
   */
  public static byte[] requireElements(byte[] array, IntPredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code byte[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code byte[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code byte[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code byte[]} array
   *         does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static byte[] requireElements(byte[] array, IntPredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code byte[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code byte[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code byte[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code byte[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code byte[]} array
   *         does not satisfy the specified condition.
   */
  public static <E extends Throwable> byte[] requireElements(byte[] array, IntPredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code short[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @return The reference to the specified {@code short[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code short[]}
   *         array does not satisfy the specified condition.
   */
  public static short[] requireElements(short[] array, IntPredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code short[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code short[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code short[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code short[]}
   *         array does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static short[] requireElements(short[] array, IntPredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code short[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code short[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code short[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code short[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code short[]}
   *         array does not satisfy the specified condition.
   */
  public static <E extends Throwable> short[] requireElements(short[] array, IntPredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code int[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @return The reference to the specified {@code int[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code int[]} array
   *         does not satisfy the specified condition.
   */
  public static int[] requireElements(int[] array, IntPredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code int[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code int[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code int[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code int[]} array
   *         does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static int[] requireElements(int[] array, IntPredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code int[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code int[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code int[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code int[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code int[]} array
   *         does not satisfy the specified condition.
   */
  public static <E extends Throwable> int[] requireElements(int[] array, IntPredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code long[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @return The reference to the specified {@code long[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code long[]} array
   *         does not satisfy the specified condition.
   */
  public static long[] requireElements(long[] array, LongPredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code long[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code long[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code long[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code long[]} array
   *         does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static long[] requireElements(long[] array, LongPredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code long[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code long[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code long[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code long[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code long[]} array
   *         does not satisfy the specified condition.
   */
  public static <E extends Throwable> long[] requireElements(long[] array, LongPredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code float[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @return The reference to the specified {@code float[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code float[]}
   *         array does not satisfy the specified condition.
   */
  public static float[] requireElements(float[] array, DoublePredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code float[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code float[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code float[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code float[]}
   *         array does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static float[] requireElements(float[] array, DoublePredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code float[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code float[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code float[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code float[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code float[]}
   *         array does not satisfy the specified condition.
   */
  public static <E extends Throwable> float[] requireElements(float[] array,
      DoublePredicate condition, ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code double[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @return The reference to the specified {@code double[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code double[]}
   *         array does not satisfy the specified condition.
   */
  public static double[] requireElements(double[] array, DoublePredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code double[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code double[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code double[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code double[]}
   *         array does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static double[] requireElements(double[] array, DoublePredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code double[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code double[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code double[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code double[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code double[]}
   *         array does not satisfy the specified condition.
   */
  public static <E extends Throwable> double[] requireElements(double[] array, DoublePredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * @param array The {@code char[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @return The reference to the specified {@code char[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code char[]} array
   *         does not satisfy the specified condition.
   */
  public static char[] requireElements(char[] array, IntPredicate condition) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException("[" + index + "] = " + array[index]);
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code char[]} array satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified array is {@code null} then no exception will be
   * thrown.
   *
   * @param array The {@code char[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code char[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code char[]} array
   *         does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForArray)
   */
  public static char[] requireElements(char[] array, IntPredicate condition, Object message) {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw new IllegalArgumentException(toExceptionMessage(message, index));
      }
    }
    return array;
  }

  /**
   * Checks that all the elements of the specified {@code char[]} array satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified array
   * is {@code null} then no exception will be thrown.
   *
   * @param <E> The type of the exception to throw.
   * @param array The {@code char[]} array whose elements to check.
   * @param condition The predicate to apply for each element of the {@code char[]} array.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code char[]} array.
   * @throws IllegalArgumentException if at least one element of the specified {@code char[]} array
   *         does not satisfy the specified condition.
   */
  public static <E extends Throwable> char[] requireElements(char[] array, IntPredicate condition,
      ExceptionProvider.ForArray<E> exception) throws E {
    final int length = array == null ? 0 : array.length;
    for (int index = 0; index < length; index++) {
      if (!condition.test(array[index])) {
        throw exception.get(index);
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
   * This is an equivalent of the:
   * <pre>
   * requireElements(iterable, Predicates.OBJECT_NON_NULL)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check.
   * @return The reference to the specified {@code Iterable} sequence.
   * @throws NullPointerException if the specified {@code Iterable} sequence contains {@code null}
   *         elements.
   */
  public static <T, I extends Iterable<T>> I requireElementsNonNull(I iterable) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        if (itr.next() == null) {
          throw new NullPointerException("[" + index + "] = null");
        }
      }
    }
    return iterable;
  }

  /**
   * Checks that the specified {@code Iterable} sequence does not contain {@code null} elements and
   * throws {@code NullPointerException} with the specified detail message if it does. Note that if
   * the specified sequence is {@code null} then no exception will be thrown.
   *
   * <p>
   * This is an equivalent of the:
   * <pre>
   * requireElements(iterable, Predicates.OBJECT_NON_NULL, message)
   * </pre>
   * but throws {@code NullPointerException}.
   * </p>
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code Iterable} sequence.
   * @throws NullPointerException if the specified {@code Iterable} sequence contains {@code null}
   *         elements.
   * @see #defer(ExceptionProvider.ForIterable)
   */
  public static <T, I extends Iterable<T>> I requireElementsNonNull(I iterable, Object message) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        if (itr.next() == null) {
          throw new NullPointerException(toExceptionMessage(message, index, null));
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
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @return The reference to the specified {@code Iterable} sequence.
   * @throws IllegalArgumentException if at least one element of the specified {@code Iterable}
   *         sequence does not satisfy the specified condition.
   */
  public static <T, I extends Iterable<T>> I requireElements(I iterable, Predicate<? super T> condition) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (!condition.test(element)) {
          throw new IllegalArgumentException("[" + index + "] = " + element);
        }
      }
    }
    return iterable;
  }

  /**
   * Checks that all the elements of the specified {@code Iterable} sequence satisfy the specified
   * condition and throws {@code IllegalArgumentException} with the specified detail message if
   * they are not. Note that if the specified sequence is {@code null} then no exception will be
   * thrown.
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param iterable The {@code Iterable} sequence whose elements to check.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @param message The provider of the exception detail message.
   * @return The reference to the specified {@code Iterable} sequence.
   * @throws IllegalArgumentException if at least one element of the specified {@code Iterable}
   *         sequence does not satisfy the specified condition.
   * @see #defer(ExceptionProvider.ForIterable)
   */
  public static <T, I extends Iterable<T>> I requireElements(I iterable, Predicate<? super T> condition,
      Object message) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (!condition.test(element)) {
          throw new IllegalArgumentException(toExceptionMessage(message, index, element));
        }
      }
    }
    return iterable;
  }

  /**
   * Checks that all the elements of the specified {@code Iterable} sequence satisfy the specified
   * condition and throws the specified exception if they are not. Note that if the specified
   * sequence is {@code null} then no exception will be thrown.
   *
   * @param <T> The type of elements of the {@code Iterable} sequence.
   * @param <I> The type of the {@code Iterable} sequence.
   * @param <E> The type of the exception to throw.
   * @param iterable The {@code Iterable} sequence whose elements to check.
   * @param condition The predicate to apply for each element of the {@code Iterable} sequence.
   * @param exception The provider of the exception.
   * @return The reference to the specified {@code Iterable} sequence.
   * @throws IllegalArgumentException if at least one element of the specified {@code Iterable}
   *         sequence does not satisfy the specified condition.
   */
  public static <T, I extends Iterable<T>, E extends Throwable> I requireElements(I iterable,
      Predicate<? super T> condition, ExceptionProvider.ForIterable<E, T> exception) throws E {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (!condition.test(element)) {
          throw exception.get(index, element);
        }
      }
    }
    return iterable;
  }

  // Helpers

  /**
   * Returns a reference to the specified provider of the exception detail message. In other words,
   * simply returns its argument. This helper method just provides a way to avoid explicit casting
   * to {@code ExceptionProvider<String>} in the {@code requireXXX()} calls that accept provider of
   * the exception detail message as an {@code Object}.
   *
   * <p>
   * For example, statement:
   * <pre>
   * requireNonNull(object,
   *     (ExceptionProvider&lt;String&gt;) () -> java.time.LocalDateTime.now() + ": object is null");
   * </pre>
   * is equivalent to:
   * <pre>
   * requireNonNull(object, defer(() -> java.time.LocalDateTime.now() + ": object is null"));
   * </pre>
   * but a bit shorter.
   * </p>
   *
   * @param formatter The provider of the exception detail message.
   * @return A reference to the specified provider of the exception detail message.
   * @see Predicates
   */
  public static ExceptionProvider<String> defer(ExceptionProvider<String> formatter) {
    return formatter;
  }

  /**
   * Returns an exception detail message obtained from the specified message provider.
   *
   * @param message The provider of the exception detail message.
   * @return An exception detail message obtained from the specified message provider.
   * @see Predicates
   */
  private static String toExceptionMessage(Object message) {
    if (message instanceof ExceptionProvider) {
      message = ((ExceptionProvider<?>) message).get();
    }
    return String.valueOf(message);
  }

  /**
   * Returns a reference to the specified {@code ExceptionProvider.ForArray<String>} provider of
   * the exception detail message. In other words, simply returns its argument. This helper method
   * just provides a way to avoid explicit casting to {@code ExceptionProvider.ForArray<String>} in
   * the {@code requireElementsXXX()} calls that accept provider of the exception detail message as
   * an {@code Object}.
   *
   * <p>
   * For example, statement:
   * <pre>
   * requireElementsNonNull(array,
   *     (ExceptionProvider.ForArray&lt;String&gt;) (index) -> "[" + index + "]: element is null");
   * </pre>
   * is equivalent to:
   * <pre>
   * requireElementsNonNull(array, defer((index) -> "[" + index + "]: element is null"));
   * </pre>
   * but a bit shorter.
   * </p>
   *
   * @param formatter The provider of the exception detail message.
   * @return A reference to the specified provider of the exception detail message.
   * @see Predicates
   */
  public static ExceptionProvider.ForArray<String> defer(ExceptionProvider.ForArray<String> formatter) {
    return formatter;
  }

  /**
   * Returns an exception detail message obtained from the specified message provider.
   *
   * @param message The provider of the exception detail message.
   * @param index The index in an array or {@code Iterable} sequence at which condition failed.
   * @return An exception detail message obtained from the specified message provider.
   * @see Predicates
   */
  private static String toExceptionMessage(Object message, int index) {
    if (message instanceof ExceptionProvider.ForArray) {
      return String.valueOf(((ExceptionProvider.ForArray<?>) message).get(index));
    }
    return toExceptionMessage(message);
  }

  /**
   * Returns a reference to the specified {@code ExceptionProvider.ForIterable} provider of the
   * exception detail message. In other words, simply returns its argument. This helper method just
   * provides a way to avoid explicit casting to the {@code ExceptionProvider.ForIterable} in the
   * {@code requireElementsXXX()} calls that accept provider of the exception detail message as an
   * {@code Object}.
   *
   * <p>
   * For example, statement:
   * <pre>
   * requireElementsNonNull(array,
   *     (ExceptionProvider.ForIterable&lt;String&gt;) (index, element) -> "[" + index + "]: element is null");
   * </pre>
   * is equivalent to:
   * <pre>
   * requireElementsNonNull(array, defer((index) -> "[" + index + "]: element is null"));
   * </pre>
   * but a bit shorter.
   * </p>
   *
   * @param formatter The provider of the exception detail message.
   * @return A reference to the specified provider of the exception detail message.
   * @see Predicates
   */
  public static <T> ExceptionProvider.ForIterable<String, T> defer(
      ExceptionProvider.ForIterable<String, T> formatter) {
    return formatter;
  }

  /**
   * Returns an exception detail message obtained from the specified message provider.
   *
   * @param message The provider of the exception detail message.
   * @param index The index a {@code Iterable} sequence at which condition failed.
   * @param element The element of a {@code Iterable} sequence.
   * @return An exception detail message obtained from the specified message provider.
   * @see Predicates
   */
  private static String toExceptionMessage(Object message, int index, Object element) {
    if (message instanceof ExceptionProvider.ForIterable) {
      return String.valueOf(
          Objects.<ExceptionProvider.ForIterable<?, Object>>cast(message).get(index, element));
    }
    return toExceptionMessage(message, index);
  }

  // ExceptionProvider

  /**
   * The functional interface for providers of exceptions or exception detail messages used by the
   * {@code requireXXX} methods.
   *
   * @param <R> Either {@code Throwable} or {@code String} type.
   *
   * @author Fox Mulder
   */
  @FunctionalInterface
  public static interface ExceptionProvider<R> {

    /**
     * Returns either {@code Throwable} instance or exception detail message.
     *
     * @return Either {@code Throwable} instance or exception detail message.
     */
    R get();

    @FunctionalInterface
    public static interface ForArray<R> {

      R get(int index);

    }

    @FunctionalInterface
    public static interface ForIterable<R, E> {

      R get(int index, E element);

    }

  }

}
