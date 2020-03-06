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
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;

/**
 * A collection of reusable predicates.
 *
 * @author Fox Mulder
 */
public final class Predicates {

  // Instantiation is not possible
  private Predicates() {
    throw new IllegalAccessError();
  }

  // Integer predicates

  /** {@code x == 0} */
  public static final IntPredicate INT_EQ_ZERO = (x) -> x == 0;
  /** {@code x != 0} */
  public static final IntPredicate INT_NE_ZERO = (x) -> x != 0;
  /** {@code x > 0} */
  public static final IntPredicate INT_POSITIVE = (x) -> x > 0;
  /** {@code x >= 0} */
  public static final IntPredicate INT_POSITIVE_OR_ZERO = (x) -> x >= 0;
  /** {@code x < 0} */
  public static final IntPredicate INT_NEGATIVE = (x) -> x < 0;
  /** {@code x <= 0} */
  public static final IntPredicate INT_NEGATIVE_OR_ZERO = (x) -> x <= 0;

  /** {@code x != null && x.intValue() == 0} */
  public static final Predicate<Integer> OBJECT_INT_EQ_ZERO = (x) -> x != null && x.intValue() == 0;
  /** {@code x != null && x.intValue() != 0} */
  public static final Predicate<Integer> OBJECT_INT_NE_ZERO = (x) -> x != null && x.intValue() != 0;
  /** {@code x != null && x.intValue() > 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE = (x) -> x != null && x.intValue() > 0;
  /** {@code x != null && x.intValue() >= 0} */
  public static final Predicate<Integer> OBJECT_INT_POSITIVE_OR_ZERO = (x) -> x != null && x.intValue() >= 0;
  /** {@code x != null && x.intValue() < 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE = (x) -> x != null && x.intValue() < 0;
  /** {@code x != null && x.intValue() <= 0} */
  public static final Predicate<Integer> OBJECT_INT_NEGATIVE_OR_ZERO = (x) -> x != null && x.intValue() <= 0;

  // Long predicates

  /** {@code x == 0L} */
  public static final LongPredicate LONG_EQ_ZERO = (x) -> x == 0L;
  /** {@code x != 0L} */
  public static final LongPredicate LONG_NE_ZERO = (x) -> x != 0L;
  /** {@code x > 0L} */
  public static final LongPredicate LONG_POSITIVE = (x) -> x > 0L;
  /** {@code x >= 0L} */
  public static final LongPredicate LONG_POSITIVE_OR_ZERO = (x) -> x >= 0L;
  /** {@code x < 0L} */
  public static final LongPredicate LONG_NEGATIVE = (x) -> x < 0L;
  /** {@code x <= 0L} */
  public static final LongPredicate LONG_NEGATIVE_OR_ZERO = (x) -> x <= 0L;

  /** {@code x != null && x.longValue() == 0L} */
  public static final Predicate<Long> OBJECT_LONG_EQ_ZERO = (x) -> x != null && x.longValue() == 0L;
  /** {@code x != null && x.longValue() != 0L} */
  public static final Predicate<Long> OBJECT_LONG_NE_ZERO = (x) -> x != null && x.longValue() != 0L;
  /** {@code x != null && x.longValue() > 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE = (x) -> x != null && x.longValue() > 0L;
  /** {@code x != null && x.longValue() >= 0L} */
  public static final Predicate<Long> OBJECT_LONG_POSITIVE_OR_ZERO = (x) -> x != null && x.longValue() >= 0L;
  /** {@code x != null && x.longValue() < 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE = (x) -> x != null && x.longValue() < 0L;
  /** {@code x != null && x.longValue() <= 0L} */
  public static final Predicate<Long> OBJECT_LONG_NEGATIVE_OR_ZERO = (x) -> x != null && x.longValue() <= 0L;

  // Float predicates

  /** {@code x != null && x.floatValue() == .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_EQ_ZERO = (x) -> x != null && x.floatValue() == .0f;
  /** {@code x != null && x.floatValue() != .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NE_ZERO = (x) -> x != null && x.floatValue() != .0f;
  /** {@code x != null && x.floatValue() > .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE = (x) -> x != null && x.floatValue() > .0f;
  /** {@code x != null && x.floatValue() >= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_POSITIVE_OR_ZERO = (x) -> x != null && x.floatValue() >= .0f;
  /** {@code x != null && x.floatValue() < .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE = (x) -> x != null && x.floatValue() < .0f;
  /** {@code x != null && x.floatValue() <= .0f} */
  public static final Predicate<Float> OBJECT_FLOAT_NEGATIVE_OR_ZERO = (x) -> x != null && x.floatValue() <= .0f;
  /** {@code x != null && x.isNaN() } */
  public static final Predicate<Float> OBJECT_FLOAT_NAN = (x) -> x != null && x.isNaN();
  /** {@code x != null && x.isFinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_FINITE = (x) -> x != null && x.isInfinite();
  /** {@code x != null && x.isInfinite() } */
  public static final Predicate<Float> OBJECT_FLOAT_INFINITE = (x) -> x != null && x.isInfinite();

  // Double predicates

  /** {@code x == .0d} */
  public static final DoublePredicate DOUBLE_EQ_ZERO = (x) -> x == .0d;
  /** {@code x != .0d} */
  public static final DoublePredicate DOUBLE_NE_ZERO = (x) -> x != .0d;
  /** {@code x > .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE = (x) -> x > .0d;
  /** {@code x >= .0d} */
  public static final DoublePredicate DOUBLE_POSITIVE_OR_ZERO = (x) -> x >= .0d;
  /** {@code x < .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE = (x) -> x < .0d;
  /** {@code x <= .0d} */
  public static final DoublePredicate DOUBLE_NEGATIVE_OR_ZERO = (x) -> x <= .0d;
  /** {@code Double.isNaN(x) } */
  public static final DoublePredicate DOUBLE_NAN = Double::isNaN;
  /** {@code Double.isFinite(x) } */
  public static final DoublePredicate DOUBLE_FINITE = Double::isFinite;
  /** {@code Double.isInfinite(x) } */
  public static final DoublePredicate DOUBLE_INFINITE = Double::isInfinite;

  /** {@code x != null && x.doubleValue() == .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_EQ_ZERO = (x) -> x != null && x.doubleValue() == .0d;
  /** {@code x != null && x.doubleValue() != .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NE_ZERO = (x) -> x != null && x.doubleValue() != .0d;
  /** {@code x != null && x.doubleValue() > .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE = (x) -> x != null && x.doubleValue() > .0d;
  /** {@code x != null && x.doubleValue() >= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_POSITIVE_OR_ZERO = (x) -> x != null && x.doubleValue() >= .0d;
  /** {@code x != null && x.doubleValue() < .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE = (x) -> x != null && x.doubleValue() < .0d;
  /** {@code x != null && x.doubleValue() <= .0d} */
  public static final Predicate<Double> OBJECT_DOUBLE_NEGATIVE_OR_ZERO = (x) -> x != null && x.doubleValue() <= .0d;
  /** {@code x != null && x.isNaN() } */
  public static final Predicate<Double> OBJECT_DOUBLE_NAN = (x) -> x != null && x.isNaN();
  /** {@code x != null && x.isFinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_FINITE = (x) -> x != null && x.isInfinite();
  /** {@code x != null && x.isInfinite() } */
  public static final Predicate<Double> OBJECT_DOUBLE_INFINITE = (x) -> x != null && x.isInfinite();

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

  /** {@code a != null && a.length == 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<byte[]> BYTE_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<short[]> SHORT_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<short[]> SHORT_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<int[]> INT_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<int[]> INT_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<long[]> LONG_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<long[]> LONG_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<float[]> FLOAT_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<double[]> DOUBLE_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<char[]> CHAR_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<char[]> CHAR_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<boolean[]> BOOLEAN_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;
  /** {@code a != null && a.length == 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_EMPTY = (a) -> a != null && a.length == 0;
  /** {@code a != null && a.length > 0} */
  public static final Predicate<Object[]> OBJECT_ARRAY_NON_EMPTY = (a) -> a != null && a.length > 0;

  // Miscellaneous predicates

}
