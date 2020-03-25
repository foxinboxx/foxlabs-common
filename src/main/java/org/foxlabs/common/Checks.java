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

import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.function.Predicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;

/**
 * A collection of {@code static} methods that check that a given primitive or object satisfies a
 * given condition and throw an appropriate exception if not. This class is designed primarily for
 * doing parameter validation in methods and constructors.
 *
 * @author Fox Mulder
 */
public final class Checks {

  // Instantiation is not possible
  private Checks() {
    throw new IllegalAccessError();
  }

  // ===== NULL CHECKS ============================================================================

  // --- Object

  /**
   * Throws {@link NullPointerException} if the specified {@code object} reference is {@code null}.
   * Otherwise, returns a reference to the specified {@code object}.
   *
   * @see #checkNotNull(Object, String)
   */
  public static <T> T checkNotNull(T object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  /**
   * Throws {@link NullPointerException} with the specified detail {@code message} if the specified
   * {@code object} reference is {@code null}. Otherwise, returns a reference to the specified
   * {@code object}.
   */
  public static <T> T checkNotNull(T object, String message) {
    if (object == null) {
      throw new NullPointerException(message);
    }
    return object;
  }

  // --- Object[]

  /**
   * Throws {@link NullPointerException} if the specified {@code array} contains at least one
   * {@code null} element. Otherwise, returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned.</p>
   *
   * @see #checkAllNotNull(Object[], String)
   */
  public static <T> T[] checkAllNotNull(T[] array) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (array[index] == null) {
          throw new NullPointerException(String.format("[%s]", index));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link NullPointerException} with the specified detail {@code message} formatted using
   * the {@link String#format(String, Object...)} method if the specified {@code array} contains at
   * least one {@code null} element. Otherwise, returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned.</p>
   */
  public static <T> T[] checkAllNotNull(T[] array, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (array[index] == null) {
          throw new NullPointerException(String.format(message, index));
        }
      }
    }
    return array;
  }

  // --- Iterable

  /**
   * Throws {@link NullPointerException} if the specified {@code iterable} sequence contains at
   * least one {@code null} element. Otherwise, returns a reference to the specified
   * {@code iterable} sequence.
   *
   * <p>Note that if the specified {@code iterable} sequence reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   *
   * @see #checkAllNotNull(Iterable, String)
   */
  public static <I extends Iterable<?>> I checkAllNotNull(I iterable) {
    if (iterable != null) {
      final Iterator<?> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        if (itr.next() == null) {
          throw new NullPointerException(String.format("[%s]", index));
        }
      }
    }
    return iterable;
  }

  /**
   * Throws {@link NullPointerException} with the specified detail {@code message} formatted using
   * the {@link String#format(String, Object...)} method if the specified {@code iterable} sequence
   * contains at least one {@code null} element. Otherwise, returns a reference to the specified
   * {@code iterable} sequence.
   *
   * <p>Note that if the specified {@code iterable} sequence reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   */
  public static <I extends Iterable<?>> I checkAllNotNull(I iterable, String message) {
    if (iterable != null) {
      final Iterator<?> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        if (itr.next() == null) {
          throw new NullPointerException(String.format(message, index));
        }
      }
    }
    return iterable;
  }

  // ===== EMPTY CHECKS ===========================================================================

  // TODO All primitive type arrays, Object[], String, CharSequence, Collection, Map

  // ===== INDEX CHECKS ===========================================================================

  // --- boolean[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(boolean[], int, String)
   */
  public static boolean[] checkIndex(boolean[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static boolean[] checkIndex(boolean[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- byte[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(byte[], int, String)
   */
  public static byte[] checkIndex(byte[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static byte[] checkIndex(byte[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- short[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(short[], int, String)
   */
  public static short[] checkIndex(short[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static short[] checkIndex(short[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- int[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(int[], int, String)
   */
  public static int[] checkIndex(int[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static int[] checkIndex(int[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- long[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(long[], int, String)
   */
  public static long[] checkIndex(long[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static long[] checkIndex(long[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- float[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(float[], int, String)
   */
  public static float[] checkIndex(float[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static float[] checkIndex(float[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- double[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(double[], int, String)
   */
  public static double[] checkIndex(double[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static double[] checkIndex(double[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- char[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(char[], int, String)
   */
  public static char[] checkIndex(char[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static char[] checkIndex(char[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- Object[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkIndex(Object[], int, String)
   */
  public static <T> T[] checkIndex(T[] array, int index) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Array index out of range: {0 <= %s < %s}", index, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; array.length}</code>. Otherwise,
   * returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static <T> T[] checkIndex(T[] array, int index, String message) {
    if (index < 0 || index >= array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, index, array.length));
    }
    return array;
  }

  // --- String

  /**
   * Throws {@link StringIndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; string.length()}</code>. Otherwise, returns a reference to the
   * specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   * @see #checkIndex(String, int, String)
   */
  public static String checkIndex(String string, int index) {
    if (index < 0 || index >= string.length()) {
      throw new StringIndexOutOfBoundsException(
          String.format("String index out of range: {0 <= %s < %s}", index, string.length()));
    }
    return string;
  }

  /**
   * Throws {@link StringIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code index} is out of range <code>{0 &lt;= index &lt; string.length()}</code>. Otherwise,
   * returns a reference to the specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   */
  public static String checkIndex(String string, int index, String message) {
    if (index < 0 || index >= string.length()) {
      throw new StringIndexOutOfBoundsException(String.format(message, index, string.length()));
    }
    return string;
  }

  // --- CharSequence

  /**
   * Throws {@link IndexOutOfBoundsException} if the specified {@code index} is out of range
   * <code>{0 &lt;= index &lt; sequence.length()}</code>. Otherwise, returns a reference to the
   * specified character {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   */
  public static <S extends CharSequence> S checkIndex(S sequence, int index) {
    if (index < 0 || index >= sequence.length()) {
      throw new IndexOutOfBoundsException(
          String.format("Index out of range: {0 <= %s < %s}", index, sequence.length()));
    }
    return sequence;
  }

  /**
   * Throws {@link IndexOutOfBoundsException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code index} is
   * out of range <code>{0 &lt;= index &lt; sequence.length()}</code>. Otherwise, returns reference
   * a to the specified character {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   */
  public static <S extends CharSequence> S checkIndex(S sequence, int index, String message) {
    if (index < 0 || index >= sequence.length()) {
      throw new IndexOutOfBoundsException(String.format(message, index, sequence.length()));
    }
    return sequence;
  }

  // ===== RANGE CHECKS ===========================================================================

  // --- boolean[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(boolean[], int, String)
   */
  public static boolean[] checkRange(boolean[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static boolean[] checkRange(boolean[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(boolean[], int, int, String)
   */
  public static boolean[] checkRange(boolean[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static boolean[] checkRange(boolean[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- byte[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(byte[], int, String)
   */
  public static byte[] checkRange(byte[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * <p>Note that {@link NullPointerException} will be thrown if the specified {@code array}
   * reference is {@code null}. </p>
   */
  public static byte[] checkRange(byte[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(byte[], int, int, String)
   */
  public static byte[] checkRange(byte[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static byte[] checkRange(byte[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- short[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(short[], int, String)
   */
  public static short[] checkRange(short[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static short[] checkRange(short[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(short[], int, int, String)
   */
  public static short[] checkRange(short[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static short[] checkRange(short[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- int[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(int[], int, String)
   */
  public static int[] checkRange(int[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static int[] checkRange(int[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(int[], int, int, String)
   */
  public static int[] checkRange(int[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static int[] checkRange(int[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- long[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(long[], int, String)
   */
  public static long[] checkRange(long[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static long[] checkRange(long[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(long[], int, int, String)
   */
  public static long[] checkRange(long[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static long[] checkRange(long[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- float[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(float[], int, String)
   */
  public static float[] checkRange(float[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static float[] checkRange(float[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(float[], int, int, String)
   */
  public static float[] checkRange(float[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static float[] checkRange(float[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- double[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(double[], int, String)
   */
  public static double[] checkRange(double[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static double[] checkRange(double[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(double[], int, int, String)
   */
  public static double[] checkRange(double[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static double[] checkRange(double[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- char[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(char[], int, String)
   */
  public static char[] checkRange(char[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static char[] checkRange(char[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(char[], int, int, String)
   */
  public static char[] checkRange(char[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static char[] checkRange(char[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- Object[]

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(Object[], int, String)
   */
  public static <T> T[] checkRange(T[] array, int start) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s}", start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= array.length}</code>.
   * Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static <T> T[] checkRange(T[] array, int start, String message) {
    if (start < 0 || start > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} if the specified range {@code [start, end]} does
   * not fit into a boundaries of the specified {@code array} <code>{0 &lt;= start &lt;= end &lt;=
   * array.length}</code>. Otherwise, returns a reference to the specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   * @see #checkRange(Object[], int, int, String)
   */
  public static <T> T[] checkRange(T[] array, int start, int end) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(
          String.format("Invalid array range: {0 <= %s <= %s <= %s}", start, end, array.length));
    }
    return array;
  }

  /**
   * Throws {@link ArrayIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code array} <code>{0
   * &lt;= start &lt;= end &lt;= array.length}</code>. Otherwise, returns a reference to the
   * specified {@code array}.
   *
   * @throws NullPointerException if a reference to the specified {@code array} is {@code null}.
   */
  public static <T> T[] checkRange(T[] array, int start, int end, String message) {
    if (start < 0 || start > end || end > array.length) {
      throw new ArrayIndexOutOfBoundsException(String.format(message, start, end, array.length));
    }
    return array;
  }

  // --- String

  /**
   * Throws {@link StringIndexOutOfBoundsException} if the specified {@code start} offset is out of
   * range <code>{0 &lt;= start &lt;= string.length()}</code>. Otherwise, returns a reference to
   * the specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   * @see #checkRange(String, int, String)
   */
  public static String checkRange(String string, int start) {
    if (start < 0 || start > string.length()) {
      throw new StringIndexOutOfBoundsException(
          String.format("Invalid string range: {0 <= %s <= %s}", start, string.length()));
    }
    return string;
  }

  /**
   * Throws {@link StringIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified
   * {@code start} offset is out of range <code>{0 &lt;= start &lt;= string.length()}</code>.
   * Otherwise, returns a reference to the specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   */
  public static String checkRange(String string, int start, String message) {
    if (start < 0 || start > string.length()) {
      throw new StringIndexOutOfBoundsException(String.format(message, start, string.length()));
    }
    return string;
  }

  /**
   * Throws {@link StringIndexOutOfBoundsException} if the specified range {@code [start, end]}
   * does not fit into a boundaries of the specified {@code string} <code>{0 &lt;= start &lt;= end
   * &lt;= string.length()}</code>. Otherwise, returns a reference to the specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   * @see #checkRange(String, int, int, String)
   */
  public static String checkRange(String string, int start, int end) {
    if (start < 0 || start > end || end > string.length()) {
      throw new StringIndexOutOfBoundsException(
          String.format("Invalid string range: {0 <= %s <= %s <= %s}", start, end, string.length()));
    }
    return string;
  }

  /**
   * Throws {@link StringIndexOutOfBoundsException} with the specified detail {@code message}
   * formatted using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified {@code string} <code>{0
   * &lt;= start &lt;= end &lt;= string.length()}</code>. Otherwise, returns a reference to the
   * specified {@code string}.
   *
   * @throws NullPointerException if a reference to the specified {@code string} is {@code null}.
   */
  public static String checkRange(String string, int start, int end, String message) {
    if (start < 0 || start > end || end > string.length()) {
      throw new StringIndexOutOfBoundsException(String.format(message, start, end, string.length()));
    }
    return string;
  }

  // --- CharSequence

  /**
   * Throws {@link IndexOutOfBoundsException} if the specified {@code start} offset is out of range
   * <code>{0 &lt;= start &lt;= sequence.length()}</code>. Otherwise, returns a reference to the
   * specified character {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   * @see #checkRange(CharSequence, int, String)
   */
  public static <S extends CharSequence> S checkRange(S sequence, int start) {
    if (start < 0 || start > sequence.length()) {
      throw new IndexOutOfBoundsException(String.format("Invalid range: {0 <= %s <= %s}", start, sequence.length()));
    }
    return sequence;
  }

  /**
   * Throws {@link IndexOutOfBoundsException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code start}
   * offset is out of range <code>{0 &lt;= start &lt;= sequence.length()}</code>. Otherwise,
   * returns a reference to the specified character {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   */
  public static <S extends CharSequence> S checkRange(S sequence, int start, String message) {
    if (start < 0 || start > sequence.length()) {
      throw new IndexOutOfBoundsException(String.format(message, start, sequence.length()));
    }
    return sequence;
  }

  /**
   * Throws {@link IndexOutOfBoundsException} if the specified range {@code [start, end]} does not
   * fit into a boundaries of the specified character {@code sequence} <code>{0 &lt;= start &lt;=
   * end &lt;= sequence.length()}</code>. Otherwise, returns a reference to the specified character
   * {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   * @see #checkRange(CharSequence, int, int, String)
   */
  public static <S extends CharSequence> S checkRange(S sequence, int start, int end) {
    if (start < 0 || start > end || end > sequence.length()) {
      throw new IndexOutOfBoundsException(
          String.format("Invalid range: {0 <= %s <= %s <= %s}", start, end, sequence.length()));
    }
    return sequence;
  }

  /**
   * Throws {@link IndexOutOfBoundsException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified range
   * {@code [start, end]} does not fit into a boundaries of the specified character
   * {@code sequence} <code>{0 &lt;= start &lt;= end &lt;= sequence.length()}</code>. Otherwise,
   * returns a reference to the specified {@code sequence}.
   *
   * @throws NullPointerException if a reference to the specified {@code sequence} is {@code null}.
   */
  public static <S extends CharSequence> S checkRange(S sequence, int start, int end, String message) {
    if (start < 0 || start > end || end > sequence.length()) {
      throw new IndexOutOfBoundsException(String.format(message, start, end, sequence.length()));
    }
    return sequence;
  }

  // ===== CONDITIONAL CHECKS =====================================================================

  // --- byte

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(byte, boolean, String)
   */
  public static byte checkThat(byte value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static byte checkThat(byte value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- byte[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(byte[], IntPredicate, String)
   */
  public static byte[] checkThatAll(byte[] array, IntPredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static byte[] checkThatAll(byte[] array, IntPredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- short

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(short, boolean, String)
   */
  public static short checkThat(short value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static short checkThat(short value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- short[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(short[], IntPredicate, String)
   */
  public static short[] checkThatAll(short[] array, IntPredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static short[] checkThatAll(short[] array, IntPredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- int

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(int, boolean, String)
   */
  public static int checkThat(int value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static int checkThat(int value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- int[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(int[], IntPredicate, String)
   */
  public static int[] checkThatAll(int[] array, IntPredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static int[] checkThatAll(int[] array, IntPredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- long

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(long, boolean, String)
   */
  public static long checkThat(long value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static long checkThat(long value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- long[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(long[], LongPredicate, String)
   */
  public static long[] checkThatAll(long[] array, LongPredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static long[] checkThatAll(long[] array, LongPredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- float

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(float, boolean, String)
   */
  public static float checkThat(float value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static float checkThat(float value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- float[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(float[], DoublePredicate, String)
   */
  public static float[] checkThatAll(float[] array, DoublePredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static float[] checkThatAll(float[] array, DoublePredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- double

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(double, boolean, String)
   */
  public static double checkThat(double value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", value));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static double checkThat(double value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, value));
    }
    return value;
  }

  // --- double[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(double[], DoublePredicate, String)
   */
  public static double[] checkThatAll(double[] array, DoublePredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format("Invalid argument: [%s] = %s", index, array[index]));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static double[] checkThatAll(double[] array, DoublePredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, array[index]));
        }
      }
    }
    return array;
  }

  // --- char

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns the specified {@code value}.
   *
   * @see #checkThat(char, boolean, String)
   */
  public static char checkThat(char value, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", Objects.toString(value)));
    }
    return value;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns the specified {@code value}.
   */
  public static char checkThat(char value, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, Objects.toString(value)));
    }
    return value;
  }

  // --- char[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   *
   * @see #checkThatAll(char[], IntPredicate, String)
   */
  public static char[] checkThatAll(char[] array, IntPredicate condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(
              String.format("Invalid argument: [%s] = %s", index, Objects.toString(array[index])));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned. </p>
   */
  public static char[] checkThatAll(char[] array, IntPredicate condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, Objects.toString(array[index])));
        }
      }
    }
    return array;
  }

  // --- Object

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} is {@code false}.
   * Otherwise, returns a reference to the specified {@code object}.
   *
   * @see #checkThat(Object, boolean, String)
   */
  public static <T> T checkThat(T object, boolean condition) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format("Invalid argument: %s", Objects.toString(object)));
    }
    return object;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * is {@code false}. Otherwise, returns a reference to the specified {@code object}.
   */
  public static <T> T checkThat(T object, boolean condition, String message) {
    if (condition == false) {
      throw new IllegalArgumentException(String.format(message, Objects.toString(object)));
    }
    return object;
  }

  // --- Object[]

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code array}. Otherwise, returns a
   * reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned.</p>
   *
   * @see #checkThatAll(Object[], Predicate, String)
   */
  public static <T> T[] checkThatAll(T[] array, Predicate<T> condition) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(
              String.format("Invalid argument: [%s] = %s", index, Objects.toString(array[index])));
        }
      }
    }
    return array;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code array}. Otherwise
   * returns a reference to the specified {@code array}.
   *
   * <p>Note that if the specified {@code array} reference is {@code null} then no exception will
   * be thrown and {@code null} will be returned.</p>
   */
  public static <T> T[] checkThatAll(T[] array, Predicate<T> condition, String message) {
    if (array != null) {
      for (int index = 0; index < array.length; index++) {
        if (condition.test(array[index]) == false) {
          throw new IllegalArgumentException(String.format(message, index, Objects.toString(array[index])));
        }
      }
    }
    return array;
  }

  // --- Iterable

  /**
   * Throws {@link IllegalArgumentException} if the specified {@code condition} returns
   * {@code false} for at least one element of the specified {@code iterable} sequence. Otherwise,
   * returns a reference to the specified {@code iterable} sequence.
   *
   * <p>Note that if the specified {@code iterable} sequence reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   *
   * @see #checkThatAll(Iterable, Predicate, String)
   */
  public static <T, I extends Iterable<T>> I checkThatAll(I iterable, Predicate<T> condition) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (condition.test(element) == false) {
          throw new IllegalArgumentException(
              String.format("Invalid argument: [%s] = %s", index, Objects.toString(element)));
        }
      }
    }
    return iterable;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified {@code condition}
   * returns {@code false} for at least one element of the specified {@code iterable} sequence.
   * Otherwise returns a reference to the specified {@code iterable} sequence.
   *
   * <p>Note that if the specified {@code iterable} sequence reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   */
  public static <T, I extends Iterable<T>> I checkThatAll(I iterable, Predicate<T> condition, String message) {
    if (iterable != null) {
      final Iterator<T> itr = iterable.iterator();
      for (int index = 0; itr.hasNext(); index++) {
        final T element = itr.next();
        if (condition.test(element) == false) {
          throw new IllegalArgumentException(String.format(message, index, Objects.toString(element)));
        }
      }
    }
    return iterable;
  }

  // ===== MISCELANOUS CHECKS =====================================================================

  // --- Pattern

  /**
   * Throws {@link IllegalArgumentException} if the specified character {@code sequence} does not
   * match the specified {@code pattern}. Otherwise, returns a reference to the specified character
   * {@code sequence}.
   *
   * <p>Note that if the specified character {@code sequence} reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   *
   * @see #checkMatch(CharSequence, Pattern, String)
   */
  public static <S extends CharSequence> S checkMatch(S sequence, Pattern pattern) {
    if (sequence != null) {
      if (pattern.matcher(sequence).matches() == false) {
        throw new IllegalArgumentException(
            String.format("Must match regular expression (%s): %s", pattern, Objects.toString(sequence)));
      }
    }
    return sequence;
  }

  /**
   * Throws {@link IllegalArgumentException} with the specified detail {@code message} formatted
   * using the {@link String#format(String, Object...)} method if the specified character
   * {@code sequence} does not match the specified {@code pattern}. Otherwise, returns a reference
   * to the specified character {@code sequence}.
   *
   * <p>Note that if the specified character {@code sequence} reference is {@code null} then no
   * exception will be thrown and {@code null} will be returned.</p>
   */
  public static <S extends CharSequence> S checkMatch(S sequence, Pattern pattern, String message) {
    if (sequence != null) {
      if (pattern.matcher(sequence).matches() == false) {
        throw new IllegalArgumentException(String.format(message, pattern, Objects.toString(sequence)));
      }
    }
    return sequence;
  }

}
