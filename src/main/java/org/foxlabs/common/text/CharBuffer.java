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

package org.foxlabs.common.text;

import java.util.Set;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.Iterator;

import org.foxlabs.common.function.ToString;
import org.foxlabs.common.function.CharEncoder;
import org.foxlabs.common.function.GetChars;
import org.foxlabs.common.exception.ThresholdReachedException;

import static org.foxlabs.common.Predicates.*;
import static org.foxlabs.common.Predicates.ExceptionProvider.*;

public class CharBuffer implements CharSequence, GetChars {

  // Public constants

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE;

  public static final int LOG_THRESHOLD = 4096;

  public static final int MIN_DEPTH = 1 << 5; // 32 characters long (64 bytes)

  public static final int MAX_DEPTH = 1 << 15; // 32K characters long (64K bytes)

  // Private constants

  private static final char CHAR_QUOTE = '\'';

  private static final char STRING_QUOTE = '\"';

  private static final char LONG_SUFFIX = 'L';

  private static final char FLOAT_SUFFIX = 'F';

  private static final char DOUBLE_SUFFIX = 'D';

  private static final char SEQUENCE_OPEN = '[';

  private static final char SEQUENCE_CLOSE = ']';

  private static final char MAP_OPEN = '{';

  private static final char MAP_CLOSE = '}';

  /**
   * The string representation of the {@code null} reference.
   */
  private static final char[] NULL_REFERENCE = {'n', 'u', 'l', 'l'};

  /**
   * The string representation of the {@code true} constant.
   */
  private static final char[] TRUE_CONSTANT = {'t', 'r', 'u', 'e'};

  /**
   * The string representation of the {@code false} constant.
   */
  private static final char[] FALSE_CONSTANT = {'f', 'a', 'l', 's', 'e'};

  /**
   * The string representation of an empty array or {@code Iterable} sequence.
   */
  private static final char[] EMPTY_SEQUENCE = {SEQUENCE_OPEN, SEQUENCE_CLOSE};

  /**
   * The string representation of an empty map.
   */
  private static final char[] EMPTY_MAP = {MAP_OPEN, MAP_CLOSE};

  /**
   * The string representation of elements separator in an array or {@code Iterable} sequence.
   */
  private static final char[] ELEMENT_SEPARATOR = {',', ' '};

  /**
   * The string representation of key value pair separator in a map.
   */
  private static final char[] KEY_VALUE_SEPARATOR = {':', ' '};

  // Fields

  private final int threshold;

  private final int depth;

  private final int capacity;

  private char[][] buffer;

  private int length;

  /**
   * The objects cross-reference map to detect circular references.
   */
  private IdentityHashMap<Object, char[]> crossrefs;

  public CharBuffer() {
    this(MAX_THRESHOLD, MIN_DEPTH);
  }

  public CharBuffer(int threshold) {
    this(threshold, MIN_DEPTH);
  }

  public CharBuffer(int threshold, int depth) {
    this.threshold = require(threshold, INT_POSITIVE);
    // round depth to be a multiple of 32 and trim it to maximum possible
    this.depth = Math.min((((require(depth, INT_POSITIVE) - 1) >> 5) + 1) << 5, MAX_DEPTH);
    // calculate maximum number of slots
    this.capacity = (this.threshold - 1) / this.depth + 1;
    // allocate at most 16 initial slots depending on the capacity
    this.buffer = new char[Math.min(this.capacity, 16)][];
  }

  // Query operations

  /**
   * Returns the threshold of the buffer (i.e. the maximum number of characters that the buffer can
   * contain).
   *
   * @return The threshold of the buffer.
   */
  public final int threshold() {
    return threshold;
  }

  /**
   * Returns the current number of characters that have already been appended to the buffer (i.e.
   * the current length of the buffer).
   *
   * @return The current number of characters that have already been appended to the buffer.
   */
  @Override
  public final int length() {
    return length;
  }

  /**
   * Returns the remaining number of characters that can be appended to the buffer until the
   * {@link ThresholdReachedException} will be thrown.
   *
   * @return The remaining number of characters that can be appended to the buffer.
   */
  public final int remaining() {
    return threshold - length;
  }

  // Core operations

  public final CharBuffer append(char value) {
    ensureCapacity(1);
    nextSlot()[length++ % depth] = value;
    return this;
  }

  public final CharBuffer append(int value) {
    if (Character.isBmpCodePoint(value)) {
      return append((char) value);
    } else {
      append(Character.highSurrogate(value));
      return append(Character.lowSurrogate(value));
    }
  }

  public final CharBuffer append(char... values) {
    return appendSafe(GetChars.of(values), 0, values.length);
  }

  public final CharBuffer append(char[] values, int start) {
    final int length = values.length; // NPE check as well
    require(values, checkCharArrayRange(start), ofIOOB(start, length));
    return appendSafe(GetChars.of(values), start, length);
  }

  public final CharBuffer append(char[] values, int start, int end) {
    require(requireNonNull(values), checkCharArrayRange(start, end), ofIOOB(start, end));
    return appendSafe(GetChars.of(values), start, end);
  }

  public final CharBuffer append(CharSequence sequence) {
    return appendSafe(GetChars.of(sequence), 0, sequence.length());
  }

  public final CharBuffer append(CharSequence sequence, int start) {
    final int length = sequence.length(); // NPE check as well
    require(sequence, checkCharSequenceRange(start), ofIOOB(start, length));
    return appendSafe(GetChars.of(sequence), start, length);
  }

  public final CharBuffer append(CharSequence sequence, int start, int end) {
    require(requireNonNull(sequence), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return appendSafe(GetChars.of(sequence), start, sequence.length());
  }

  protected final CharBuffer append0(char ch) {
    return append(ch); // FIXME!!!
  }

  protected final CharBuffer appendSafe(GetChars sequence, int from, int to) {
    // calculate the number of characters to append
    int count = to - from;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        sequence.getChars(from, from + remainder, nextSlot(), offset);
        length += remainder;
        from += remainder;
        count -= remainder;
      }
      if (from < to) {
        // Not all the characters have been appended
        throw new ThresholdReachedException(this);
      }
    }
    return this;
  }

  public final int ensureCapacity(int count) {
    // trim count if it exceeds threshold
    long nlength = (long) length + (long) count; // avoid int overflow
    if (nlength > threshold) {
      count = (int) (nlength = threshold) - length;
      if (count == 0) { // fast check
        throw new ThresholdReachedException(this);
      }
    }
    // calculate total number of required slots
    final int nslots = ((int) nlength - 1) / depth + 1;
    if (nslots > buffer.length) {
      // extend buffer for new slots as x2 required slots
      final char[][] copy = new char[Math.min(nslots << 1, capacity)][];
      System.arraycopy(buffer, 0, copy, 0, (length - 1) / depth + 1);
      buffer = copy;
    }
    // return the actual number of characters that can be appended
    return count;
  }

  private final char[] nextSlot() {
    // allocate a new slot if necessary
    final int index = length / depth;
    return buffer[index] == null ? buffer[index] = new char[depth] : buffer[index];
  }

  // Advanced operations

  // Number to string representation

  /**
   * All possible digits (up to the hexadecimal system) of which an integer number can consist.
   */
  private static final char[] DIGITS = {
      '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
  };

  public final CharBuffer appendDec(byte value) {
    // check sign
    int sign = 0;
    int v = value;
    if (v < 0) {
      sign = 1;
      v = -v;
    }
    // one digit?
    if (v < 10) {
      ensureCapacity(sign + 1);
      appendSign(value);
      append0(DIGITS[v]);
      return this;
    }
    // two digits?
    if (v < 100) {
      ensureCapacity(sign + 2);
      appendSign(value);
      append0(DIGITS[v / 10]);
      append0(DIGITS[v % 10]);
      return this;
    }
    // three digits
    ensureCapacity(sign + 3);
    appendSign(value);
    append0(DIGITS[1]); // the only case
    append0(DIGITS[v % 100 / 10]);
    append0(DIGITS[v % 10]);
    return this;
  }

  public final CharBuffer appendHex(byte value) {
    ensureCapacity(2);
    append0(DIGITS[(value >>> 0x04) & 0xf]);
    append0(DIGITS[(value >>> 0x00) & 0xf]);
    return this;
  }

  public final CharBuffer appendHexTrimZeros(byte value) {
    // 4 or less bits long?
    if ((value & 0xf) == value) {
      ensureCapacity(1);
      return append0(DIGITS[(value >>> 0x00) & 0xf]);
    }
    // 8 bits long
    return appendHex(value);
  }

  public final CharBuffer appendHex(short value) {
    ensureCapacity(4);
    append0(DIGITS[(value >>> 0x0c) & 0xf]);
    append0(DIGITS[(value >>> 0x08) & 0xf]);
    append0(DIGITS[(value >>> 0x04) & 0xf]);
    append0(DIGITS[(value >>> 0x00) & 0xf]);
    return this;
  }

  public final CharBuffer appendHexTrimZeros(short value) {
    // 8 or less bits long?
    if ((value & 0xff) == value) {
      return appendHexTrimZeros((byte) value);
    }
    // less than 16 bits long?
    if ((value & 0xfff) == value) {
      ensureCapacity(3);
      append0(DIGITS[(value >>> 0x08) & 0xf]);
      append0(DIGITS[(value >>> 0x04) & 0xf]);
      append0(DIGITS[(value >>> 0x00) & 0xf]);
      return this;
    }
    // 16 bits long
    return appendHex(value);
  }

  public final CharBuffer appendHex(int value) {
    ensureCapacity(8);
    append0(DIGITS[(value >>> 0x1c) & 0xf]);
    append0(DIGITS[(value >>> 0x18) & 0xf]);
    append0(DIGITS[(value >>> 0x14) & 0xf]);
    append0(DIGITS[(value >>> 0x10) & 0xf]);
    append0(DIGITS[(value >>> 0x0c) & 0xf]);
    append0(DIGITS[(value >>> 0x08) & 0xf]);
    append0(DIGITS[(value >>> 0x04) & 0xf]);
    append0(DIGITS[(value >>> 0x00) & 0xf]);
    return this;
  }

  public final CharBuffer appendHexTrimZeros(int value) {
    // 16 or less bits long?
    if ((value & 0xffff) == value) {
      return appendHexTrimZeros((short) value);
    }
    // less than 32 bits long?
    if ((value & 0xfffffff) == value) {
      // from 20 to 28 bits long
      for (int n = 20; n < 32; n += 4) {
        if ((value >>> n) == 0) {
          ensureCapacity(n / 4);
          for (n -= 4; n >= 0; n -= 4) {
            append0(DIGITS[(value >>> n) & 0xf]);
          }
          return this;
        }
      }
    }
    // 32 bits long
    return appendHex(value);
  }

  public final CharBuffer appendHex(long value) {
    ensureCapacity(16);
    append0(DIGITS[(int) ((value >>> 0x3c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x38) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x34) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x30) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x2c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x28) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x24) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x20) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x1c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x18) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x14) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x10) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x0c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x08) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x04) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x00) & 0xfL)]);
    return this;
  }

  public final CharBuffer appendHexTrimZeros(long value) {
    // 32 or less bits long?
    if ((value & 0xffffffffL) == value) {
      return appendHexTrimZeros((int) value);
    }
    // less than 64 bits long?
    if ((value & 0xfffffffffffffffL) == value) {
      // from 36 to 60 bits long
      for (int n = 36; n < 64; n += 4) {
        if ((value >>> n) == 0L) {
          ensureCapacity(n / 4);
          for (n -= 4; n >= 0; n -= 4) {
            append0(DIGITS[(int) ((value >>> n) & 0xfL)]);
          }
          return this;
        }
      }
    }
    // 64 bits long
    return appendHex(value);
  }

  private final void appendSign(int value) {
    if (value < 0) {
      append0('-');
    }
  }

  // Object to string representation

  /**
   * Appends string representation of the {@code null} reference to the buffer.
   *
   * @return A reference to this buffer.
   */
  public CharBuffer appendNull() {
    return append(NULL_REFERENCE);
  }

  /**
   * Appends string representation of the specified object to the buffer.
   *
   * <p>
   * Note that this method may produce an infinite loop and cause the {@link StackOverflowError}
   * if the specified object does not implement the {@code ToString} interface and object's graph
   * has circular references.
   *
   * For example:
   * <pre>
   * public class CircularReferenceTest {
   *
   *   static class Foo {
   *     public Bar bar;
   *     &#64;Override public String toString() {
   *       return "Bar: " + bar.toString();
   *     }
   *   }
   *
   *   static class Bar {
   *     public Foo foo;
   *     &#64;Override public String toString() {
   *       return "Foo: " + foo.toString();
   *     }
   *   }
   *
   *   public static void main(String[] args) {
   *     Foo foo = new Foo();
   *     Bar bar = new Bar();
   *     foo.bar = bar;
   *     bar.foo = foo;
   *     // Infinite loop
   *     foo.toString();
   *   }
   *
   * }
   * </pre>
   * </p>
   *
   * @param object The object to append to the buffer.
   * @return A reference to this buffer.
   */
  public CharBuffer appendObject(Object object) {
    // append the object depending on its type (in order of probability)
    if (object == null) {
      return appendNull();
    }
    if (object instanceof CharSequence) {
      return appendString((CharSequence) object);
    }
    if (object instanceof Integer) {
      return appendInt(((Integer) object).intValue());
    }
    if (object instanceof Long) {
      return appendLong(((Long) object).longValue());
    }
    if (object instanceof Double) {
      return appendDouble(((Double) object).doubleValue());
    }
    if (object instanceof Float) {
      return appendFloat(((Float) object).floatValue());
    }
    if (object instanceof Byte) {
      return appendByte(((Byte) object).byteValue());
    }
    if (object instanceof Short) {
      return appendShort(((Short) object).shortValue());
    }
    if (object instanceof Character) {
      return appendChar(((Character) object).charValue());
    }
    if (object instanceof Boolean) {
      return appendBoolean(((Boolean) object).booleanValue());
    }
    if (object instanceof Enum<?>) {
      return appendEnum(((Enum<?>) object));
    }
    if (object instanceof Iterable<?>) {
      return appendIterable((Iterable<?>) object);
    }
    if (object instanceof Map<?, ?>) {
      return appendMap((Map<?, ?>) object);
    }
    if (object instanceof Object[]) {
      return appendObjectArray((Object[]) object);
    }
    if (object instanceof int[]) {
      return appendIntArray((int[]) object);
    }
    if (object instanceof long[]) {
      return appendLongArray((long[]) object);
    }
    if (object instanceof double[]) {
      return appendDoubleArray((double[]) object);
    }
    if (object instanceof float[]) {
      return appendFloatArray((float[]) object);
    }
    if (object instanceof byte[]) {
      return appendByteArray((byte[]) object);
    }
    if (object instanceof short[]) {
      return appendShortArray((short[]) object);
    }
    if (object instanceof char[]) {
      return appendCharArray((char[]) object);
    }
    if (object instanceof boolean[]) {
      return appendBooleanArray((boolean[]) object);
    }
    // unsupported object type
    return appendPlain(object);
  }

  /**
   * Appends string representation of all the elements of the specified {@code Object[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Object[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharBuffer appendObjectArray(Object[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN);
    // remember reference to the array and check for cross-reference
    if (pushReference(array)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(ELEMENT_SEPARATOR).appendObject(array[index]);
      }
    } finally {
      // forget reference to the array
      popReference(array);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code boolean} value to the buffer.
   *
   * @param value The {@code boolean} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char...)
   */
  public CharBuffer appendBoolean(boolean value) {
    return value ? append(TRUE_CONSTANT) : append(FALSE_CONSTANT);
  }

  /**
   * Appends string representation of all the elements of the specified {@code boolean[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code boolean[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendBoolean(boolean)
   */
  public CharBuffer appendBooleanArray(boolean[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendBoolean(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendBoolean(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code byte} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code byte} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendByte(byte value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code byte[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code byte[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendByte(byte)
   */
  public CharBuffer appendByteArray(byte[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendByte(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendByte(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code short} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code short} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendShort(short value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code short[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code short[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendShort(short)
   */
  public CharBuffer appendShortArray(short[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendShort(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendShort(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code int} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code int} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendInt(int value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code int[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code int[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendInt(int)
   */
  public CharBuffer appendIntArray(int[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendInt(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendInt(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code long} value to the buffer using the
   * {@link Long#toString(long)} method. The format is {@code <LONG>L}.
   *
   * @param value The {@code long} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendLong(long value) {
    return append(Long.toString(value)).append(LONG_SUFFIX);
  }

  /**
   * Appends string representation of all the elements of the specified {@code long[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code long[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendLong(long)
   */
  public CharBuffer appendLongArray(long[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendLong(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendLong(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code float} value to the buffer using the
   * {@link Float#toString(float)} method. The format is {@code <FLOAT>F}.
   *
   * @param value The {@code float} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendFloat(float value) {
    return append(Float.toString(value)).append(FLOAT_SUFFIX);
  }

  /**
   * Appends string representation of all the elements of the specified {@code float[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code float[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendFloat(float)
   */
  public CharBuffer appendFloatArray(float[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendFloat(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendFloat(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code double} value to the buffer using the
   * {@link Double#toString(double)} method. The format is {@code <DOUBLE>D}.
   *
   * @param value The {@code double} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendDouble(double value) {
    return append(Double.toString(value)).append(DOUBLE_SUFFIX);
  }

  /**
   * Appends string representation of all the elements of the specified {@code double[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code double[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDouble(double)
   */
  public CharBuffer appendDoubleArray(double[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendDouble(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendDouble(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code char} value to the buffer. The format is
   * {@code '<CHAR>'}.
   *
   * @param value The {@code char} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char)
   * @see CharEncoder#JAVA
   */
  public CharBuffer appendChar(char value) {
    return CharEncoder.JAVA.encode(value, append(CHAR_QUOTE)).append(CHAR_QUOTE);
  }

  /**
   * Appends string representation of all the elements of the specified {@code char[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code char[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendChar(char)
   */
  public CharBuffer appendCharArray(char[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendChar(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendChar(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code CharSequence} value to the buffer. The
   * format is {@code "<SEQUENCE>"}.
   *
   * @param value The {@code CharSequence} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char)
   * @see CharEncoder#JAVA
   */
  public CharBuffer appendString(CharSequence value) {
    return value != null
        ? CharEncoder.JAVA.encode(value, append(STRING_QUOTE)).append(STRING_QUOTE)
        : appendNull();
  }

  /**
   * Appends string representation of the specified {@code Enum<?>} value to the buffer using the
   * {@link Enum#name()} method.
   *
   * @param value The {@code Enum<?>} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharBuffer appendEnum(Enum<?> value) {
    return value != null ? append(value.name()) : appendNull();
  }

  /**
   * Appends string representation of all the elements of the specified {@code Iterable} sequence
   * to the buffer. The format is {@code [E0, E1, ...]}. If the specified {@code Iterable}
   * reference is {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Iterable} sequence which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharBuffer appendIterable(Iterable<?> iterable) {
    // make sure that nothing is null to avoid NPE
    final Iterator<?> itr = iterable != null
        ? iterable.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN);
    // remember reference to the iteration and check for cross-reference
    if (pushReference(iterable)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(itr.next());
      while (itr.hasNext()) {
        append(ELEMENT_SEPARATOR).appendObject(itr.next());
      }
    } finally {
      // forget reference to the iteration
      popReference(iterable);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of all the entries of the specified {@code Map} table to the
   * buffer. The format is <code>{K0: V0, K1: V1, ...}</code>. If the specified {@code Map}
   * reference is {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Map} table which entries to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharBuffer appendMap(Map<?, ?> map) {
    // make sure that nothing is null to avoid NPE
    final Set<? extends Map.Entry<?, ?>> entries = map != null
        ? map.entrySet()
        : null;
    final Iterator<? extends Map.Entry<?, ?>> itr = entries != null
        ? entries.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      return append(EMPTY_MAP);
    }
    append(MAP_OPEN);
    // remember reference to the map and check for cross-reference
    if (pushReference(map)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      Map.Entry<?, ?> entry = itr.next();
      if (entry == null) { // who knows
        appendNull();
      } else {
        appendObject(entry.getKey());
        append(KEY_VALUE_SEPARATOR);
        appendObject(entry.getValue());
      }
      while (itr.hasNext()) {
        entry = itr.next();
        append(ELEMENT_SEPARATOR);
        if (entry == null) { // who knows
          appendNull();
        } else {
          appendObject(entry.getKey());
          append(KEY_VALUE_SEPARATOR);
          appendObject(entry.getValue());
        }
      }
    } finally {
      // forget reference to the map
      popReference(map);
    }
    return append(MAP_CLOSE);
  }

  public CharBuffer appendPlain(Object object) {
    if (object == null) {
      return appendNull();
    }
    // remember reference to the object and check for cross-reference
    if (pushReference(object)) {
      // cross-reference detected!
      // reference has already been added to the underlying buffer
      return this;
    }
    try {
      if (object instanceof ToString) {
        ((ToString) object).toString(this);
      } else {
        append(object.toString());
      }
      return this;
    } finally {
      // forget reference to the object
      popReference(object);
    }
  }

  protected final boolean pushReference(Object object) {
    if (crossrefs == null) {
      crossrefs = new IdentityHashMap<>();
    }
    if (crossrefs.containsKey(object)) {
      char[] crossref = crossrefs.get(object);
      if (crossref == null) {
        final String classname = object.getClass().getName();
        final String hashcode = Integer.toHexString(System.identityHashCode(object));
        crossrefs.put(object, crossref = new char[classname.length() + hashcode.length() + 2]);
        classname.getChars(0, classname.length(), crossref, 1);
        hashcode.getChars(0, hashcode.length(), crossref, classname.length() + 2);
        crossref[classname.length() + 1] = '@';
        crossref[0] = '!';
      }
      append(crossref);
      return true;
    }
    crossrefs.put(object, null);
    return false;
  }

  protected final void popReference(Object object) {
    crossrefs.remove(object);
  }

  // Cleanup operations

  public final void reset() {
    length = 0;
  }

  public final void clear() {
    final int nslots = (length - 1) / depth + 1;
    for (int index = 0; index < nslots; index++) {
      buffer[index] = null;
    }
    length = 0;
  }

  // Miscelanous

  @Override
  public char charAt(int index) {
    require(this, checkCharSequenceIndex(index), ofIOOB(index));
    return buffer[index / depth][index % depth];
  }

  @Override
  public CharSequence subSequence(int from, int to) {
    return substring(from, to);
  }

  public String substring(int from) {
    return substring(from, length);
  }

  public String substring(int from, int to) {
    require(this, checkCharSequenceRange(from, to), ofIOOB(from, to));
    final char[] value = new char[to - from];
    copyChars(from, to, value, 0);
    return new String(value);
  }

  @Override
  public void getChars(int from, int to, char[] array, int index) {
    require(this, checkCharSequenceRange(from, to), ofIOOB(from, to));
    require(array, checkCharArrayIndex(index), ofIOOB(index));
    copyChars(from, to, array, index);
  }

  @Override
  public String toString() {
    if (length != 0) { // fast check
      final char[] value = new char[length];
      copyChars(0, length, value, 0);
      return new String(value);
    }
    return "";
  }

  protected final void copyChars(int from, int to, char[] target, int offset) {
    while (from < to) {
      final int index = from % depth;
      final int remainder = Math.min(depth - index, to - from);
      System.arraycopy(buffer[from / depth], index, target, offset, remainder);
      from += remainder;
      offset += remainder;
    }
  }

  // Helpers

  public static String toString(Object object) {
    return toString(object, LOG_THRESHOLD);
  }

  public static String toString(Object object, int threshold) {
    try {
      return new CharBuffer(threshold).appendObject(object).toString();
    } catch (ThresholdReachedException e) {
      return e.getProducer().toString();
    }
  }

}