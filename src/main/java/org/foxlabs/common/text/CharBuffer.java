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

import org.foxlabs.common.function.GetChars;
import org.foxlabs.common.exception.ThresholdReachedException;

import static org.foxlabs.common.Predicates.*;
import static org.foxlabs.common.Predicates.ExceptionProvider.*;

public class CharBuffer implements Appendable, CharSequence, GetChars, ToString {

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE;

  public static final int LOG_THRESHOLD = 4096;

  public static final int MIN_DEPTH = 1 << 5; // 32 characters long (64 bytes)

  public static final int MAX_DEPTH = 1 << 15; // 32K characters long (64K bytes)

  private final int threshold;

  private final int depth;

  private final int capacity;

  private char[][] buffer;

  private int length;

  public CharBuffer() {
    this(MAX_THRESHOLD, MIN_DEPTH);
  }

  public CharBuffer(int threshold) {
    this(threshold, MIN_DEPTH);
  }

  public CharBuffer(int threshold, int depth) {
    this.threshold = require(threshold, INT_POSITIVE);
    // round depth to be a multiple of 32 and trim it to maximum possible
    this.depth = Math.min((((require(depth, INT_POSITIVE) - 1) >>> 5) + 1) << 5, MAX_DEPTH);
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

  @Override
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
    return appendSafe(GetChars.from(values), 0, values.length);
  }

  public final CharBuffer append(char[] values, int start) {
    final int length = values.length; // NPE check as well
    require(values, checkCharArrayRange(start), ofIOOB(start, length));
    return appendSafe(GetChars.from(values), start, length);
  }

  public final CharBuffer append(char[] values, int start, int end) {
    require(requireNonNull(values), checkCharArrayRange(start, end), ofIOOB(start, end));
    return appendSafe(GetChars.from(values), start, end);
  }

  @Override
  public final CharBuffer append(CharSequence sequence) {
    return appendSafe(GetChars.from(sequence), 0, sequence.length());
  }

  public final CharBuffer append(CharSequence sequence, int start) {
    final int length = sequence.length(); // NPE check as well
    require(sequence, checkCharSequenceRange(start), ofIOOB(start, length));
    return appendSafe(GetChars.from(sequence), start, length);
  }

  @Override
  public final CharBuffer append(CharSequence sequence, int start, int end) {
    require(requireNonNull(sequence), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return appendSafe(GetChars.from(sequence), start, sequence.length());
  }

  protected final CharBuffer append0(char ch) {
    return append(ch); // FIXME!!!
  }

  protected final CharBuffer appendSafe(GetChars sequence, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        sequence.getChars(start, start + remainder, nextSlot(), offset);
        length += remainder;
        start += remainder;
        count -= remainder;
      }
      if (start < end) {
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

  @Override
  public char charAt(int index) {
    require(this, checkCharSequenceIndex(index), ofIOOB(index));
    return buffer[index / depth][index % depth];
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return substring(start, end);
  }

  public String substring(int start) {
    return substring(start, length);
  }

  public String substring(int start, int end) {
    require(this, checkCharSequenceRange(start, end), ofIOOB(start, end));
    final char[] value = new char[end - start];
    copyChars(start, end, value, 0);
    return new String(value);
  }

  @Override
  public void getChars(int start, int end, char[] array, int index) {
    require(this, checkCharSequenceRange(start, end), ofIOOB(start, end));
    require(array, checkCharArrayIndex(index), ofIOOB(index));
    copyChars(start, end, array, index);
  }

  @Override
  public CharBuffer toString(CharBuffer buffer) {
    // TODO
    return buffer;
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

  protected final void copyChars(int start, int end, char[] target, int offset) {
    while (start < end) {
      final int index = start % depth;
      final int remainder = Math.min(depth - index, end - start);
      System.arraycopy(buffer[start / depth], index, target, offset, remainder);
      start += remainder;
      offset += remainder;
    }
  }

  // Cleanup operations

  /**
   * Resets the buffer length to 0 but does not release allocated memory. This method is useful
   * when the same buffer instance can be reused multiple times.
   *
   * @see #clear()
   */
  public final void reset() {
    length = 0;
  }

  /**
   * Clears the buffer and releases allocated memory. This method is useful when the same buffer
   * instance can be reused multiple times in a long term (for example, in an object pool).
   *
   * @see #reset()
   */
  public final void clear() {
    final int nslots = (length - 1) / depth + 1;
    for (int index = 0; index < nslots; index++) {
      buffer[index] = null;
    }
    length = 0;
  }

  // Number to string representation

  /**
   * All possible digits (up to the hexadecimal system) of which an integer number can consist.
   */
  private static final char[] DIGITS = {
      '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
  };

  /**
   * The string representation of the {@code int} minimum value ({@code -2147483648}).
   */
  private static final char[] MIN_INT_DIGITS = {
      '-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8'
  };

  /**
   * The string representation of the {@code long} minimum value ({@code -9223372036854775808}).
   */
  private static final char[] MIN_LONG_DIGITS = {
      '-', '9', '2', '2', '3', '3', '7', '2', '0', '3',
      '6', '8', '5', '4', '7', '7', '5', '8', '0', '8'
  };

  /**
   * All possible {@code 10^n} values for the {@code int} number.
   */
  private static final int[] INT_TENS = {
      1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000
  };

  /**
   * All possible {@code 10^n} values for the {@code long} number.
   */
  private static final long[] LONG_TENS = {
      1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L,
      10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L,
      1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L
  };

  // Decimal representation

  /**
   * Returns a number of characters required to represent the specified signed {@code int} value
   * without leading zeros in the decimal system.
   *
   * @param value The signed {@code int} value to be converted to a decimal string.
   * @return A number of characters required to represent the specified signed {@code int} value
   *         without leading zeros in the decimal system.
   * @see #appendDec(byte)
   * @see #appendDec(short)
   * @see #appendDec(int)
   */
  public static int getDecCapacity(int value) {
    if (value != Integer.MIN_VALUE) {
      return (value >>> 31) + getDecCapacity0(value < 0 ? -value : value);
    }
    return MIN_INT_DIGITS.length;
  }

  /**
   * Returns a number of characters required to represent the specified positive signed {@code int}
   * value without leading zeros in the decimal system.
   *
   * @param value The signed non-negative {@code int} value to be converted to a decimal string.
   * @return A number of characters required to represent the specified positive signed {@code int}
   *         value without leading zeros in the decimal system.
   */
  private static int getDecCapacity0(int value) {
    if (value < 1000) {
      return value < 100 ? value < 10 ? 1 : 2 : 3;
    }
    if (value < 1000000) {
      return value < 100000 ? value < 10000 ? 4 : 5 : 6;
    }
    if (value < 1000000000) {
      return value < 100000000 ? value < 10000000 ? 7 : 8 : 9;
    }
    return 10;
  }

  /**
   * Returns a number of characters required to represent the specified signed {@code long} value
   * without leading zeros in the decimal system.
   *
   * @param value The signed {@code long} value to be converted to a decimal string.
   * @return A number of characters required to represent the specified signed {@code long} value
   *         without leading zeros in the decimal system.
   * @see #appendDec(long)
   */
  public static int getDecCapacity(long value) {
    if (value != Long.MIN_VALUE) {
      return (int) (value >>> 63) + getDecCapacity0(value < 0L ? -value : value);
    }
    return MIN_LONG_DIGITS.length;
  }

  /**
   * Returns a number of characters required to represent the specified positive signed
   * {@code long} value without leading zeros in the decimal system.
   *
   * @param value The signed non-negative {@code long} value to be converted to a decimal string.
   * @return A number of characters required to represent the specified positive signed
   *         {@code long} value without leading zeros in the decimal system.
   */
  private static int getDecCapacity0(long value) {
    if (value < 1000000000L) {
      return getDecCapacity0((int) value);
    }
    if (value < 1000000000000L) {
      return value < 100000000000L ? value < 10000000000L ? 10 : 11 : 12;
    }
    if (value < 1000000000000000L) {
      return value < 100000000000000L ? value < 10000000000000L ? 13 : 14 : 15;
    }
    if (value < 1000000000000000000L) {
      return value < 100000000000000000L ? value < 10000000000000000L ? 16 : 17 : 18;
    }
    return 19;
  }

  public final CharBuffer appendDec(byte value) {
    final int sign = value >>> 31;
    int v = value < 0 ? -value : value;
    int n = getDecCapacity0(v);
    ensureCapacity(sign + n--);
    for (appendSign(sign); n > 0; v %= INT_TENS[n--]) {
      append0(DIGITS[v / INT_TENS[n]]);
    }
    return append0(DIGITS[v]);
  }

  public final CharBuffer appendDec(short value) {
    final int sign = value >>> 31;
    int v = value < 0 ? -value : value;
    int n = getDecCapacity0(v);
    ensureCapacity(sign + n--);
    for (appendSign(sign); n > 0; v %= INT_TENS[n--]) {
      append0(DIGITS[v / INT_TENS[n]]);
    }
    return append0(DIGITS[v]);
  }

  public final CharBuffer appendDec(int value) {
    if (value == Integer.MIN_VALUE) {
      // special case - sign inversion does not work for -2147483648
      return append(MIN_INT_DIGITS);
    }
    final int sign = value >>> 31;
    int v = value < 0 ? -value : value;
    int n = getDecCapacity0(v);
    ensureCapacity(sign + n--);
    for (appendSign(sign); n > 0; v %= INT_TENS[n--]) {
      append0(DIGITS[v / INT_TENS[n]]);
    }
    return append0(DIGITS[v]);
  }

  public final CharBuffer appendDec(long value) {
    if (value == Long.MIN_VALUE) {
      // special case - sign inversion does not work for -9223372036854775808
      return append(MIN_LONG_DIGITS);
    }
    final int sign = (int) (value >>> 63);
    long v = value < 0L ? -value : value;
    int n = getDecCapacity0(v);
    ensureCapacity(sign + n--);
    for (appendSign(sign); n > 0; v %= LONG_TENS[n--]) {
      append0(DIGITS[(int) (v / LONG_TENS[n])]);
    }
    return append0(DIGITS[(int) v]);
  }

  public final CharBuffer appendDec(float value) {
    // not now!
    return append(Float.toString(value));
  }

  public final CharBuffer appendDec(double value) {
    // not now!
    return append(Double.toString(value));
  }

  /**
   * Appends the {@code '-'} sign to the buffer if the specified sign flag is not {@code 0}.
   *
   * @param sign The sign flag ({@code 0} for zero or positive number; negative number otherwise).
   */
  private final void appendSign(int sign) {
    if (sign != 0) {
      append0('-');
    }
  }

  // Hexadecimal representation

  /**
   * Returns a number of characters required to represent the specified unsigned {@code int} value
   * without leading zeros in the hexadecimal system.
   *
   * @param value The unsigned {@code int} value to be converted to a hexadecimal string.
   * @return A number of characters required to represent the specified unsigned {@code int} value
   *         without leading zeros in the hexadecimal system.
   * @see #appendHexTrimZeros(byte)
   * @see #appendHexTrimZeros(short)
   * @see #appendHexTrimZeros(int)
   */
  public static int getHexCapacity(int value) {
    // low 16 bits
    if ((value & 0xffff) == value) {
      if ((value & 0xff) == value) {
        return (value & 0xf) == value ? 1 : 2;
      }
      return (value & 0xfff) == value ? 3 : 4;
    }
    // high 16 bits
    if ((value & 0xffffff) == value) {
      return (value & 0xfffff) == value ? 5 : 6;
    }
    return (value & 0xfffffff) == value ? 7 : 8;
  }

  /**
   * Returns a number of characters required to represent the specified unsigned {@code long} value
   * without leading zeros in the hexadecimal system.
   *
   * @param value The unsigned {@code long} value to be converted to a hexadecimal string.
   * @return A number of characters required to represent the specified unsigned {@code long} value
   *         without leading zeros in the hexadecimal system.
   * @see #appendHexTrimZeros(long)
   */
  public static int getHexCapacity(long value) {
    // low 32 bits
    if ((value & 0xffffffffL) == value) {
      return getHexCapacity((int) value);
    }
    // high 32 bits
    return 8 + getHexCapacity((int) (value >>> 32));
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
    // high 8 bits
    append0(DIGITS[(value >>> 0x0c) & 0xf]);
    append0(DIGITS[(value >>> 0x08) & 0xf]);
    // low 8 bits
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
    // high 16 bits
    append0(DIGITS[(value >>> 0x1c) & 0xf]);
    append0(DIGITS[(value >>> 0x18) & 0xf]);
    append0(DIGITS[(value >>> 0x14) & 0xf]);
    append0(DIGITS[(value >>> 0x10) & 0xf]);
    // low 16 bits
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
    int n = getHexCapacity(value);
    // less than 32 bits long?
    if (n < 8) {
      // from 20 to 28 bits long
      ensureCapacity(n);
      for (n = (n - 1) << 2; n >= 0; n -= 4) {
        append0(DIGITS[(value >>> n) & 0xf]);
      }
      return this;
    }
    // 32 bits long
    return appendHex(value);
  }

  public final CharBuffer appendHex(long value) {
    ensureCapacity(16);
    // high 32 bits
    append0(DIGITS[(int) ((value >>> 0x3c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x38) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x34) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x30) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x2c) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x28) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x24) & 0xfL)]);
    append0(DIGITS[(int) ((value >>> 0x20) & 0xfL)]);
    // low 32 bits
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
    int n = getHexCapacity(value);
    // less than 64 bits long?
    if (n < 16) {
      // from 36 to 60 bits long
      ensureCapacity(n);
      for (n = (n - 1) << 2; n >= 0; n -= 4) {
        append0(DIGITS[(int) ((value >>> n) & 0xfL)]);
      }
      return this;
    }
    // 64 bits long
    return appendHex(value);
  }

  // Binary representation

  /**
   * Appends an unsigned binary string representation of the specified {@code byte} value with
   * leading zeros to the buffer. The format is <code>[0-1]{8}</code>.
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinTrimZeros(byte)
   */
  public final CharBuffer appendBin(byte value) {
    ensureCapacity(8);
    append0(DIGITS[(value >>> 0x07) & 0x01]);
    append0(DIGITS[(value >>> 0x06) & 0x01]);
    append0(DIGITS[(value >>> 0x05) & 0x01]);
    append0(DIGITS[(value >>> 0x04) & 0x01]);
    append0(DIGITS[(value >>> 0x03) & 0x01]);
    append0(DIGITS[(value >>> 0x02) & 0x01]);
    append0(DIGITS[(value >>> 0x01) & 0x01]);
    append0(DIGITS[(value >>> 0x00) & 0x01]);
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code byte} value without
   * leading zeros to the buffer. The format is <code>[0-1]{1, 8}</code>.
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(byte)
   * @see #appendBin(byte)
   */
  public final CharBuffer appendBinTrimZeros(byte value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      append0(DIGITS[(value >>> n) & 0x01]);
    }
    return append0(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code byte} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code byte} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinTrimZeros(byte)
   */
  public static int getBinCapacity(byte value) {
    // low 4 bits
    if ((value & 0x0f) == value) {
      if ((value & 0x03) == value) {
        return (value & 0x01) == value ? 1 : 2;
      }
      return (value & 0x07) == value ? 3 : 4;
    }
    // high 4 bits
    if ((value & 0x3f) == value) {
      return (value & 0x1f) == value ? 5 : 6;
    }
    return (value & 0x7f) == value ? 7 : 8;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code short} value with
   * leading zeros to the buffer. The format is <code>[0-1]{16}</code>.
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinTrimZeros(short)
   */
  public final CharBuffer appendBin(short value) {
    ensureCapacity(16);
    // high 8 bits
    append0(DIGITS[(value >>> 0x0f) & 0x01]);
    append0(DIGITS[(value >>> 0x0e) & 0x01]);
    append0(DIGITS[(value >>> 0x0d) & 0x01]);
    append0(DIGITS[(value >>> 0x0c) & 0x01]);
    append0(DIGITS[(value >>> 0x0b) & 0x01]);
    append0(DIGITS[(value >>> 0x0a) & 0x01]);
    append0(DIGITS[(value >>> 0x09) & 0x01]);
    append0(DIGITS[(value >>> 0x08) & 0x01]);
    // low 8 bits
    append0(DIGITS[(value >>> 0x07) & 0x01]);
    append0(DIGITS[(value >>> 0x06) & 0x01]);
    append0(DIGITS[(value >>> 0x05) & 0x01]);
    append0(DIGITS[(value >>> 0x04) & 0x01]);
    append0(DIGITS[(value >>> 0x03) & 0x01]);
    append0(DIGITS[(value >>> 0x02) & 0x01]);
    append0(DIGITS[(value >>> 0x01) & 0x01]);
    append0(DIGITS[(value >>> 0x00) & 0x01]);
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code short} value without
   * leading zeros to the buffer. The format is <code>[0-1]{1, 16}</code>.
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(short)
   * @see #appendBin(short)
   */
  public final CharBuffer appendBinTrimZeros(short value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      append0(DIGITS[(value >>> n) & 0x01]);
    }
    return append0(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code short} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code short} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinTrimZeros(short)
   */
  public static int getBinCapacity(short value) {
    // low 8 bits
    if ((value & 0xff) == value) {
      return getBinCapacity((byte) value);
    }
    // high 8 bits
    return 8 + getBinCapacity((byte) (value >>> 8));
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code int} value with
   * leading zeros to the buffer. The format is <code>[0-1]{32}</code>.
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinTrimZeros(int)
   */
  public final CharBuffer appendBin(int value) {
    ensureCapacity(32);
    // high 16 bits
    append0(DIGITS[(value >>> 0x1f) & 0x01]);
    append0(DIGITS[(value >>> 0x1e) & 0x01]);
    append0(DIGITS[(value >>> 0x1d) & 0x01]);
    append0(DIGITS[(value >>> 0x1c) & 0x01]);
    append0(DIGITS[(value >>> 0x1b) & 0x01]);
    append0(DIGITS[(value >>> 0x1a) & 0x01]);
    append0(DIGITS[(value >>> 0x19) & 0x01]);
    append0(DIGITS[(value >>> 0x18) & 0x01]);
    append0(DIGITS[(value >>> 0x17) & 0x01]);
    append0(DIGITS[(value >>> 0x16) & 0x01]);
    append0(DIGITS[(value >>> 0x15) & 0x01]);
    append0(DIGITS[(value >>> 0x14) & 0x01]);
    append0(DIGITS[(value >>> 0x13) & 0x01]);
    append0(DIGITS[(value >>> 0x12) & 0x01]);
    append0(DIGITS[(value >>> 0x11) & 0x01]);
    append0(DIGITS[(value >>> 0x10) & 0x01]);
    // low 16 bits
    append0(DIGITS[(value >>> 0x0f) & 0x01]);
    append0(DIGITS[(value >>> 0x0e) & 0x01]);
    append0(DIGITS[(value >>> 0x0d) & 0x01]);
    append0(DIGITS[(value >>> 0x0c) & 0x01]);
    append0(DIGITS[(value >>> 0x0b) & 0x01]);
    append0(DIGITS[(value >>> 0x0a) & 0x01]);
    append0(DIGITS[(value >>> 0x09) & 0x01]);
    append0(DIGITS[(value >>> 0x08) & 0x01]);
    append0(DIGITS[(value >>> 0x07) & 0x01]);
    append0(DIGITS[(value >>> 0x06) & 0x01]);
    append0(DIGITS[(value >>> 0x05) & 0x01]);
    append0(DIGITS[(value >>> 0x04) & 0x01]);
    append0(DIGITS[(value >>> 0x03) & 0x01]);
    append0(DIGITS[(value >>> 0x02) & 0x01]);
    append0(DIGITS[(value >>> 0x01) & 0x01]);
    append0(DIGITS[(value >>> 0x00) & 0x01]);
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code int} value without
   * leading zeros to the buffer. The format is <code>[0-1]{1, 32}</code>.
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(int)
   * @see #appendBin(int)
   */
  public final CharBuffer appendBinTrimZeros(int value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      append0(DIGITS[(value >>> n) & 0x01]);
    }
    return append0(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code int} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code int} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinTrimZeros(int)
   */
  public static int getBinCapacity(int value) {
    // low 16 bits
    if ((value & 0xffff) == value) {
      return getBinCapacity((short) value);
    }
    // high 16 bits
    return 16 + getBinCapacity((short) (value >>> 16));
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code long} value with
   * leading zeros to the buffer. The format is <code>[0-1]{64}</code>.
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinTrimZeros(long)
   */
  public final CharBuffer appendBin(long value) {
    ensureCapacity(64);
    // high 32 bits
    append0(DIGITS[(int) ((value >>> 0x3f) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x3e) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x3d) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x3c) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x3b) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x3a) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x39) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x38) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x37) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x36) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x35) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x34) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x33) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x32) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x31) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x30) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2f) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2e) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2d) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2c) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2b) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x2a) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x29) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x28) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x27) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x26) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x25) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x24) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x23) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x22) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x21) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x20) & 0x01L)]);
    // low 32 bits
    append0(DIGITS[(int) ((value >>> 0x1f) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x1e) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x1d) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x1c) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x1b) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x1a) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x19) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x18) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x17) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x16) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x15) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x14) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x13) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x12) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x11) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x10) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0f) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0e) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0d) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0c) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0b) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x0a) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x09) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x08) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x07) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x06) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x05) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x04) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x03) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x02) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x01) & 0x01L)]);
    append0(DIGITS[(int) ((value >>> 0x00) & 0x01L)]);
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code long} value without
   * leading zeros to the buffer. The format is <code>[0-1]{1, 64}</code>.
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(long)
   * @see #appendBin(long)
   */
  public final CharBuffer appendBinTrimZeros(long value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      append0(DIGITS[(int) ((value >>> n) & 0x01L)]);
    }
    return append0(DIGITS[(int) (value & 0x01L)]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code long} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code long} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinTrimZeros(long)
   */
  public static int getBinCapacity(long value) {
    // low 32 bits
    if ((value & 0xffffffffL) == value) {
      return getBinCapacity((int) value);
    }
    // high 32 bits
    return 32 + getBinCapacity((int) (value >>> 32));
  }

  // Object to string representation

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
   * The objects cross-reference map to detect circular references.
   */
  private IdentityHashMap<Object, CharBuffer> crossrefs;

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
      return append0('[').append0(']');
    }
    append0('[');
    // remember reference to the array and check for cross-reference
    if (pushReference(array)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(array[0]);
      for (int index = 1; index < array.length; index++) {
        append0(',').append0(' ').appendObject(array[index]);
      }
    } finally {
      // forget reference to the array
      popReference(array);
    }
    return append0(']');
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
      return append0('[').append0(']');
    }
    append0('[').appendBoolean(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendBoolean(array[index]);
    }
    return append0(']');
  }

  /**
   * Appends string representation of the specified {@code byte} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code byte} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(byte)
   */
  public CharBuffer appendByte(byte value) {
    return appendDec(value);
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
      return append0('[').append0(']');
    }
    append0('[').appendByte(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendByte(array[index]);
    }
    return append0(']');
  }

  /**
   * Appends string representation of the specified {@code short} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code short} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(short)
   */
  public CharBuffer appendShort(short value) {
    return appendDec(value);
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
      return append0('[').append0(']');
    }
    append0('[').appendShort(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendShort(array[index]);
    }
    return append0(']');
  }

  /**
   * Appends string representation of the specified {@code int} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code int} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(int)
   */
  public CharBuffer appendInt(int value) {
    return appendDec(value);
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
      return append0('[').append0(']');
    }
    append0('[').appendInt(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendInt(array[index]);
    }
    return append0(']');
  }

  /**
   * Appends string representation of the specified {@code long} value to the buffer using the
   * {@link Long#toString(long)} method. The format is {@code <LONG>L}.
   *
   * @param value The {@code long} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(long)
   */
  public CharBuffer appendLong(long value) {
    return appendDec(value).append('L');
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
      return append0('[').append0(']');
    }
    append0('[').appendLong(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendLong(array[index]);
    }
    return append0(']');
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
    return append(Float.toString(value)).append('f');
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
      return append0('[').append0(']');
    }
    append0('[').appendFloat(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendFloat(array[index]);
    }
    return append0(']');
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
    return append(Double.toString(value)).append('d');
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
      return append0('[').append0(']');
    }
    append0('[').appendDouble(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendDouble(array[index]);
    }
    return append0(']');
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
    return CharEncoder.JAVA.encode(value, append0('\'')).append0('\'');
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
      return append0('[').append0(']');
    }
    append0('[').appendChar(array[0]);
    for (int index = 1; index < array.length; index++) {
      append0(',').append0(' ').appendChar(array[index]);
    }
    return append0(']');
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
    return value != null ? CharEncoder.JAVA.encode(value, append0('\"')).append0('\"') : appendNull();
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
      return append0('[').append0(']');
    }
    append0('[');
    // remember reference to the iteration and check for cross-reference
    if (pushReference(iterable)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(itr.next());
      while (itr.hasNext()) {
        append0(',').append0(' ').appendObject(itr.next());
      }
    } finally {
      // forget reference to the iteration
      popReference(iterable);
    }
    return append0(']');
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
      return append0('{').append0('}');
    }
    append0('{');
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
        append0(':').append0(' ');
        appendObject(entry.getValue());
      }
      while (itr.hasNext()) {
        entry = itr.next();
        append0(',').append0(' ');
        if (entry == null) { // who knows
          appendNull();
        } else {
          appendObject(entry.getKey());
          append0(':').append0(' ');
          appendObject(entry.getValue());
        }
      }
    } finally {
      // forget reference to the map
      popReference(map);
    }
    return append0('}');
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
      CharBuffer crossref = crossrefs.get(object);
      if (crossref == null) {
        final String classname = object.getClass().getName();
        crossref = new CharBuffer(classname.length() + 10);
        crossref.append0('!').append(classname);
        crossref.append0('@').appendHex(System.identityHashCode(object));
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

  // Advanced operations

  /**
   * An array of space sequences ({@code '\u0020'}) ranging from 4 to 16 in length, used by the
   * {@link #appendIdent(int)} method.
   */
  private static final char[][] SPACES = {
      {' ', ' ', ' ', ' '}, // 4
      {' ', ' ', ' ', ' ', ' '}, // 5
      {' ', ' ', ' ', ' ', ' ', ' '}, // 6
      {' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 7
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 8
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 9
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 10
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 11
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 12
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 13
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 14
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 15
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, // 16
  };

  /**
   * Appends an indentation of the specified length to the buffer using the {@code '\u0020'} space
   * character.
   *
   * @param length The number of {@code '\u0020'} space characters to append.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, char)
   * @see #appendIndent(int, int)
   */
  public final CharBuffer appendIndent(int length) {
    if (require(length, INT_POSITIVE_OR_ZERO) > 0) {
      ensureCapacity(length);
      do {
        if (length < 4) { // could be a bit faster
          for (; length > 0; length--) {
            append0(' ');
          }
        } else {
          final char[] spaces = SPACES[Math.min(length, SPACES.length) - 4];
          length -= spaces.length;
          append(spaces);
        }
      } while (length > 0);
    }
    return this;
  }

  /**
   * Appends an indentation of the specified length to the buffer using the specified character.
   *
   * @param length The number of indentation characters to append.
   * @param ch The indentation character.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, int)
   */
  public final CharBuffer appendIndent(int length, char ch) {
    if (ch == ' ') { // who knows
      return appendIndent(length);
    }
    if (require(length, INT_POSITIVE_OR_ZERO) > 0) {
      ensureCapacity(length);
      for (; length > 0; length--) {
        append0(ch);
      }
    }
    return this;
  }

  /**
   * Does the same as the {@link #appendIndent(int, char)} but uses the specified character Unicode
   * code point. Note that this method does not validate the specified character to be a valid
   * Unicode code point.
   *
   * @param length The number of indentation characters to append.
   * @param ch The Unicode code point of indentation character.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, char)
   */
  public final CharBuffer appendIndent(int length, int ch) {
    if (Character.isBmpCodePoint(ch)) {
      return appendIndent(length, (char) ch);
    }
    if (require(length, INT_POSITIVE_OR_ZERO) > 0) {
      ensureCapacity(length * 2);
      final char high = Character.highSurrogate(ch);
      final char low = Character.lowSurrogate(ch);
      for (; length > 0; length--) {
        append0(high).append0(low);
      }
    }
    return this;
  }

}
