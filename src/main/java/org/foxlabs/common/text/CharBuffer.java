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
import java.util.function.Function;

import org.foxlabs.common.Objects;
import org.foxlabs.common.Strings;
import org.foxlabs.common.exception.ThresholdReachedException;

import static org.foxlabs.common.Predicates.*;
import static org.foxlabs.common.Predicates.ExceptionProvider.*;

public abstract class CharBuffer implements CharSequence, Appendable, GetChars, ToString {

  /**
   * The buffer threshold that is recommended for logging purposes.
   */
  public static final int LOG_THRESHOLD = 4096;

  /**
   * The buffer threshold (i.e. the maximum number of characters that the buffer can contain).
   */
  protected final int threshold;

  /**
   * Constructs a new {@code CharBuffer} with the specified threshold.
   *
   * @param threshold The buffer threshold.
   * @throws IllegalArgumentException if the specified threshold is negative.
   */
  protected CharBuffer(int threshold) {
    this.threshold = require(threshold, INT_POSITIVE_OR_ZERO);
  }

  // Basic operations

  /**
   * Returns the current length of the buffer (i.e. the current number of characters that have
   * already been appended to the buffer).
   *
   * @return The current length of the buffer.
   */
  @Override
  public abstract int length();

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
   * Returns the remaining number of characters that can be appended to the buffer until the
   * {@link ThresholdReachedException} will be thrown.
   *
   * @return The remaining number of characters that can be appended to the buffer.
   */
  public final int remaining() {
    return threshold - length();
  }

  /**
   * Returns a sequence that is a copy of the buffer contents in the specified range (i.e.
   * {@code substring(start, end)}.
   *
   * @param start The starting position in the buffer (inclusive).
   * @param end The ending position in the buffer (exclusive).
   * @return A sequence that is a copy of the buffer contents in the specified range.
   * @throws IndexOutOfBoundsException if the specified starting or ending position is invalid.
   * @see #substring(int, int)
   */
  @Override
  public final CharSequence subSequence(int start, int end) {
    return substring(start, end);
  }

  /**
   * Returns a string that is a copy of the buffer contents from the specified starting position
   * (i.e. {@code substring(start, length())}.
   *
   * @param start The starting position in the buffer (inclusive).
   * @param end The ending position in the buffer (exclusive).
   * @return A string that is a copy of the buffer contents from the specified starting position.
   * @throws IndexOutOfBoundsException if the specified starting position is invalid.
   * @see #substring(int, int)
   */
  public final String substring(int start) {
    return substring(start, length());
  }

  /**
   * Returns a string that is a copy of the buffer contents in the specified range.
   *
   * @param start The starting position in the buffer (inclusive).
   * @param end The ending position in the buffer (exclusive).
   * @return A string that is a copy of the buffer contents in the specified range.
   * @throws IndexOutOfBoundsException if the specified starting or ending position is invalid.
   * @see #doGetChars(int, int, char[], int)
   */
  public final String substring(int start, int end) {
    require(this, checkCharSequenceRange(start, end), ofIOOB(start, end));
    if (start == end) {
      return Strings.EMPTY;
    } else {
      final char[] copy = new char[end - start];
      doGetChars(start, end, copy, 0);
      return new String(copy);
    }
  }

  /**
   * Copies characters from the buffer contents in the specified range into the specified target
   * array starting from the specified offset.
   *
   * @param start The starting position in the buffer (inclusive).
   * @param end The ending position in the buffer (exclusive).
   * @param target The target array to copy to.
   * @param offset The starting position in the target array (inclusive).
   * @throws NullPointerException if the specified target array is {@code null}.
   * @throws IndexOutOfBoundsException if the specified starting or ending position is invalid.
   * @see #doGetChars(int, int, char[], int)
   */
  @Override
  public final void getChars(int start, int end, char[] array, int index) {
    require(this, checkCharSequenceRange(start, end), ofIOOB(start, end));
    require(requireNonNull(array), checkCharArrayIndex(index), ofIOOB(index));
    if (start < end) {
      doGetChars(start, end, array, index);
    }
  }

  /**
   * Performs the actual copying of characters from the buffer contents in the specified range into
   * the specified target array starting from the specified offset. Subclasses should not worry
   * about the correctness of the given arguments as they should already be verified by a
   * {@code public} methods.
   *
   * @param start The starting position in the buffer (inclusive).
   * @param end The ending position in the buffer (exclusive).
   * @param target The target array to copy to.
   * @param offset The starting position in the target array (inclusive).
   */
  protected abstract void doGetChars(int start, int end, char[] target, int offset);

  /**
   * Ensures that the buffer capacity is sufficient to append the specified number of characters
   * and returns actual number of characters that can be appended. If the current capacity is not
   * enough then this method should try to allocate additional space to append as many characters
   * as possible. In other words, returned actual number of characters cannot be greater than
   * result of the {@link #remaining()} method.
   *
   * @param count The desired number of characters to append.
   * @return The actual number of characters that can be appended.
   * @see #remaining()
   */
  public abstract int ensureCapacity(int count);

  /**
   * Appends the specified character to the buffer and increments the current length. Note that
   * this method does not completely satisfy the {@link Appendable} interface contract because it
   * may throw {@link ThresholdReachedException} instead of {@link java.io.IOException}.
   *
   * @param ch The character to append.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(int)
   * @see #doAppend(char)
   */
  @Override
  public final CharBuffer append(char ch) {
    ensureCapacity(1);
    return doAppend(ch);
  }

  /**
   * Appends the specified character (Unicode code point) to the buffer and increases the current
   * length by 1 or 2 depending on whether the specified character is in the Basic Multilingual
   * Plane (BMP) or is it a supplementary character, respectively. If the specified character is a
   * supplementary character (i.e. 2 {@code char}s long) and the {@link #remaining()} number of
   * characters is 1 then none will be appended and {@link ThresholdReachedException} will be
   * thrown. Note that this method does not validate the specified character to be a valid Unicode
   * code point.
   *
   * @param ch The character (Unicode code point) to append.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see Character#isBmpCodePoint(int)
   * @see #doAppend(char)
   */
  public final CharBuffer append(int ch) {
    if (Character.isBmpCodePoint(ch)) {
      ensureCapacity(1);
      return doAppend((char) ch);
    } else if (ensureCapacity(2) < 2) {
      throw new ThresholdReachedException(this);
    } else {
      doAppend(Character.highSurrogate(ch));
      return doAppend(Character.lowSurrogate(ch));
    }
  }

  /**
   * Appends all characters of the specified array to the buffer and increases the current length
   * accordingly. If threshold of the buffer will be exceeded during this operation then the first
   * {@link #remaining()} characters will be appended and the {@code ThresholdReachedException}
   * will be thrown.
   *
   * @param array The array of characters to append.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified array of characters is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #doAppend(GetChars, int, int)
   */
  public final CharBuffer append(char... array) {
    return doAppend(GetChars.from(array), 0, array.length);
  }

  /**
   * Appends characters of the specified array from the specified starting position to the buffer
   * and increases the current length accordingly. If threshold of the buffer will be exceeded
   * during this operation then the first {@link #remaining()} characters will be appended and the
   * {@code ThresholdReachedException} will be thrown.
   *
   * @param array The array of characters to append.
   * @param start The starting position in the specified array to append from.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified array of characters is {@code null}.
   * @throws IndexOutOfBoundsException if the specified starting position is invalid.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #doAppend(GetChars, int, int)
   */
  public final CharBuffer append(char[] array, int start) {
    require(array, checkCharArrayRange(start), ofIOOB(start, array.length));
    return doAppend(GetChars.from(array), start, array.length);
  }

  /**
   * Appends characters of the specified array in the specified range to the buffer and increases
   * the current length accordingly. If threshold of the buffer will be exceeded during this
   * operation then the first {@link #remaining()} characters will be appended and the
   * {@code ThresholdReachedException} will be thrown.
   *
   * @param array The array of characters to append.
   * @param start The starting position in the specified array to append from.
   * @param end The ending position in the specified array to append to.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified array of characters is {@code null}.
   * @throws IndexOutOfBoundsException if the specified starting or ending position is invalid.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #doAppend(GetChars, int, int)
   */
  public final CharBuffer append(char[] array, int start, int end) {
    require(requireNonNull(array), checkCharArrayRange(start, end), ofIOOB(start, end));
    return doAppend(GetChars.from(array), start, end);
  }

  /**
   * Appends all characters of the specified sequence to the buffer and increases the current length
   * accordingly. If threshold of the buffer will be exceeded during this operation then the first
   * {@link #remaining()} characters will be appended and the {@code ThresholdReachedException}
   * will be thrown.
   *
   * @param sequence The sequence of characters to append.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified sequence of characters is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #doAppend(GetChars, int, int)
   */
  @Override
  public final CharBuffer append(CharSequence sequence) {
    return doAppend(GetChars.from(sequence), 0, sequence.length());
  }

  /**
   * Appends characters of the specified sequence from the specified starting position to the
   * buffer and increases the current length accordingly. If threshold of the buffer will be
   * exceeded during this operation then the first {@link #remaining()} characters will be appended
   * and the {@code ThresholdReachedException} will be thrown.
   *
   * @param sequence The sequence of characters to append.
   * @param start The starting position in the specified sequence to append from.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified sequence of characters is {@code null}.
   * @throws IndexOutOfBoundsException if the specified starting position is invalid.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #doAppend(GetChars, int, int)
   */
  public final CharBuffer append(CharSequence sequence, int start) {
    require(sequence, checkCharSequenceRange(start), ofIOOB(start, sequence.length()));
    return doAppend(GetChars.from(sequence), start, sequence.length());
  }

  /**
   * Appends characters of the specified sequence in the specified range to the buffer and
   * increases the current length accordingly. If threshold of the buffer will be exceeded during
   * this operation then the first {@link #remaining()} characters will be appended and the
   * {@code ThresholdReachedException} will be thrown.
   *
   * @param sequence The sequence of characters to append.
   * @param start The starting position in the specified sequence to append from.
   * @param end The ending position in the specified sequence to append to.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified sequence of characters is {@code null}.
   * @throws IndexOutOfBoundsException if the specified starting or ending position is invalid.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   */
  @Override
  public final CharBuffer append(CharSequence sequence, int start, int end) {
    require(requireNonNull(sequence), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return doAppend(GetChars.from(sequence), start, sequence.length());
  }

  /**
   * Performs the actual appending of the specified character to the buffer. Subclasses should
   * expect that the {@code #ensureCapacity(int)} method has been called right before calling this
   * method.
   *
   * @param ch The character to append.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   */
  protected abstract CharBuffer doAppend(char ch);

  /**
   * Performs the actual appending of the characters of the specified {@link GetChars} sequence in
   * the specified range to the buffer. Subclasses should not worry about the correctness of the
   * given arguments as they should already be verified by a {@code public} methods. Note that
   * there is no guarantee that the {@code #ensureCapacity(int)} method was called right before
   * calling this method, so subclasses must do this themselves.
   *
   * @param sequence The {@link GetChars} sequence of characters to append.
   * @param start The starting position in the specified sequence to append from.
   * @param end The ending position in the specified sequence to append to.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   */
  protected abstract CharBuffer doAppend(GetChars sequence, int start, int end);

  /**
   * Resets the buffer length to 0 but does not release allocated memory. This method is useful
   * when the same buffer instance can be reused multiple times.
   *
   * @see #clear()
   */
  public abstract void reset();

  /**
   * Clears the buffer and releases allocated memory. This method is useful when the same buffer
   * instance can be reused multiple times in a long term (for example, in an object pool).
   *
   * @see #reset()
   */
  public abstract void clear();

  /**
   * Appends current contents of the buffer to the specified one and returns a reference to it (i.e.
   * {@code buffer.append(this)}).
   *
   * @param buffer The buffer to append to.
   * @return A reference to the specified buffer.
   * @throws NullPointerException if the specified buffer is {@code null}.
   * @throws ThresholdReachedException if threshold of the specified buffer has been reached.
   */
  @Override
  public CharBuffer toString(CharBuffer buffer) {
    return buffer.doAppend(GetChars.from(this), 0, length());
  }

  /**
   * Returns a string representation of current state of the buffer (i.e. creates a {@code String}
   * copy initialized with the current contents of the buffer).
   *
   * @return A string representation of current state of the buffer.
   */
  @Override
  public String toString() {
    if (length() == 0) {
      return Strings.EMPTY;
    } else {
      final char[] copy = new char[length()];
      doGetChars(0, copy.length, copy, 0);
      return new String(copy);
    }
  }

  // Boolean to string representation

  /**
   * The string representation of the {@code true} constant.
   */
  private static final char[] TRUE_CONSTANT = {
      't', 'r', 'u', 'e'
  };

  /**
   * The string representation of the {@code false} constant.
   */
  private static final char[] FALSE_CONSTANT = {
      'f', 'a', 'l', 's', 'e'
  };

  /**
   * Appends a string representation of the specified {@code boolean} value to the buffer.
   *
   * <p>The format is <code>(true)|(false)</code>.</p>
   *
   * @param value The {@code boolean} value to be converted to a string.
   * @return A reference to this buffer.
   * @see #getBoolCapacity(boolean)
   */
  public final CharBuffer appendBool(boolean value) {
    return value ? append(TRUE_CONSTANT) : append(FALSE_CONSTANT);
  }

  /**
   * Returns the number of characters required to represent the specified {@code boolean} value as
   * a string.
   *
   * @param value The {@code boolean} value to be converted to a string.
   * @return The number of characters required to represent the specified {@code boolean} value as
   *         a string.
   * @see #appendBool(boolean)
   */
  public static final int getBoolCapacity(boolean value) {
    return value ? TRUE_CONSTANT.length : FALSE_CONSTANT.length;
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
   * The decimal string representation of the {@code int} min value ({@code -2147483648}).
   */
  private static final char[] MIN_INT_DIGITS = {
      '-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8'
  };

  /**
   * The decimal string representation of the {@code long} min value ({@code -9223372036854775808}).
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
   * Appends a signed decimal string representation of the specified {@code int} value to the
   * buffer.
   *
   * <p>The format is <code>\-?[0-9]{1,10}</code>.</p>
   *
   * @param value The {@code int} value to be converted to a signed decimal string.
   * @return A reference to this buffer.
   * @see #getDecCapacity(int)
   */
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
      doAppend(DIGITS[v / INT_TENS[n]]);
    }
    return doAppend(DIGITS[v]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code int} value as a
   * signed decimal string.
   *
   * @param value The {@code int} value to be converted to a signed decimal string.
   * @return The number of characters required to represent the specified {@code int} value as a
   *         signed decimal string.
   * @see #appendDec(int)
   */
  public static int getDecCapacity(int value) {
    if (value != Integer.MIN_VALUE) {
      return (value >>> 31) + getDecCapacity0(value < 0 ? -value : value);
    }
    // special case - sign inversion does not work for -2147483648
    return MIN_INT_DIGITS.length;
  }

  /**
   * Returns the number of characters required to represent the specified non-negative {@code int}
   * value as a signed decimal string.
   *
   * @param value The non-negative {@code int} value to be converted to a signed decimal string.
   * @return The number of characters required to represent the specified non-negative {@code int}
   *         value as a signed decimal string.
   */
  private static int getDecCapacity0(int value) {
    if (value < 1_000) {
      return value < 100 ? value < 10 ? 1 : 2 : 3;
    }
    if (value < 1_000_000) {
      return value < 100_000 ? value < 10_000 ? 4 : 5 : 6;
    }
    if (value < 1_000_000_000) {
      return value < 100_000_000 ? value < 10_000_000 ? 7 : 8 : 9;
    }
    return 10;
  }

  /**
   * Appends a signed decimal string representation of the specified {@code long} value to the
   * buffer.
   *
   * <p>The format is <code>\-?[0-9]{1,19}</code>.</p>
   *
   * @param value The {@code long} value to be converted to a signed decimal string.
   * @return A reference to this buffer.
   * @see #getDecCapacity(long)
   */
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
      doAppend(DIGITS[(int) (v / LONG_TENS[n])]);
    }
    return doAppend(DIGITS[(int) v]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code long} value as a
   * signed decimal string.
   *
   * @param value The {@code long} value to be converted to a signed decimal string.
   * @return The number of characters required to represent the specified {@code long} value as a
   *         signed decimal string.
   * @see #appendDec(long)
   */
  public static int getDecCapacity(long value) {
    if (value != Long.MIN_VALUE) {
      return (int) (value >>> 63) + getDecCapacity0(value < 0L ? -value : value);
    }
    // special case - sign inversion does not work for -9223372036854775808
    return MIN_LONG_DIGITS.length;
  }

  /**
   * Returns the number of characters required to represent the specified non-negative {@code long}
   * value as a signed decimal string.
   *
   * @param value The non-negative {@code long} value to be converted to a signed decimal string.
   * @return The number of characters required to represent the specified non-negative {@code long}
   *         value as a signed decimal string.
   * @see #getDecCapacity0(int)
   */
  private static int getDecCapacity0(long value) {
    if (value < 1_000_000_000L) {
      return getDecCapacity0((int) value);
    }
    if (value < 1_000_000_000_000L) {
      return value < 100_000_000_000L ? value < 10_000_000_000L ? 10 : 11 : 12;
    }
    if (value < 1_000_000_000_000_000L) {
      return value < 100_000_000_000_000L ? value < 10_000_000_000_000L ? 13 : 14 : 15;
    }
    if (value < 1_000_000_000_000_000_000L) {
      return value < 100_000_000_000_000_000L ? value < 10_000_000_000_000_000L ? 16 : 17 : 18;
    }
    return 19;
  }

  /**
   * Appends a decimal string representation of the specified {@code float} value to the buffer.
   * This is a shortcut for the {@code append(Float.toString(value))}.
   *
   * <p>The format is <code>(NaN)|(\-?Infinity)|(\-?[0-9]*\.?[0-9]+([eE]\-?[0-9]+)?)</code>.</p>
   *
   * @param value The {@code float} value to be converted to a decimal string.
   * @return A reference to this buffer.
   * @see Float#toString(float)
   */
  public final CharBuffer appendDec(float value) {
    // the float-to-string algorithm is hard enough
    // so let's use Float.toString() for now
    return append(Float.toString(value));
  }

  /**
   * Appends a decimal string representation of the specified {@code double} value to the buffer.
   * This is a shortcut for the {@code append(Double.toString(value))}.
   *
   * <p>The format is <code>(NaN)|(\-?Infinity)|(\-?[0-9]*\.?[0-9]+([eE]\-?[0-9]+)?)</code>.</p>
   *
   * @param value The {@code double} value to be converted to a decimal string.
   * @return A reference to this buffer.
   * @see Double#toString(double)
   */
  public final CharBuffer appendDec(double value) {
    // the double-to-string algorithm is hard enough
    // so let's use Double.toString() for now
    return append(Double.toString(value));
  }

  /**
   * Appends the {@code '-'} sign to the buffer if the specified sign flag is not {@code 0}.
   *
   * @param sign The sign flag ({@code 0} for zero or positive number; negative number otherwise).
   */
  private final void appendSign(int sign) {
    if (sign != 0) {
      doAppend('-');
    }
  }

  // Hexadecimal representation

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code byte} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{2}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #appendHexStripLeadingZeros(byte)
   */
  public final CharBuffer appendHex(byte value) {
    ensureCapacity(2);
    // @formatter:off
    doAppend(DIGITS[value >>> 4 & 0x0f]);
    doAppend(DIGITS[value >>> 0 & 0x0f]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code byte} value
   * without leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{1,2}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #getHexCapacity(byte)
   * @see #appendHex(byte)
   */
  public final CharBuffer appendHexStripLeadingZeros(byte value) {
    // 4 or less bits long?
    if (value >>> 4 == 0) {
      ensureCapacity(1);
      return doAppend(DIGITS[value]);
    }
    // from 5 to 8 bits long
    return appendHex(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code byte} value as an
   * unsigned hexadecimal string without leading zeros.
   *
   * @param value The {@code byte} value to be converted to an unsigned hexadecimal string.
   * @return The number of characters required to represent the specified {@code byte} value as an
   *         unsigned hexadecimal string without leading zeros.
   * @see #appendHexStripLeadingZeros(byte)
   */
  public static int getHexCapacity(byte value) {
    return value >>> 4 == 0 ? 1 : 2;
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code short} value
   * with leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{4}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #appendHexStripLeadingZeros(short)
   */
  public final CharBuffer appendHex(short value) {
    ensureCapacity(4);
    // @formatter:off
    doAppend(DIGITS[value >>> 12 & 0x0f]);
    doAppend(DIGITS[value >>>  8 & 0x0f]);
    doAppend(DIGITS[value >>>  4 & 0x0f]);
    doAppend(DIGITS[value >>>  0 & 0x0f]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code short} value
   * without leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{1,4}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #getHexCapacity(short)
   * @see #appendHex(short)
   */
  public final CharBuffer appendHexStripLeadingZeros(short value) {
    // 8 or less bits long?
    if (value >>> 8 == 0) {
      return appendHexStripLeadingZeros((byte) value);
    }
    // 12 or less bits long?
    if (value >>> 12 == 0) {
      // from 9 to 12 bits long
      ensureCapacity(3);
      doAppend(DIGITS[value >>> 8 & 0x0f]);
      doAppend(DIGITS[value >>> 4 & 0x0f]);
      doAppend(DIGITS[value >>> 0 & 0x0f]);
      return this;
    }
    // from 13 to 16 bits long
    return appendHex(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code short} value as an
   * unsigned hexadecimal string without leading zeros.
   *
   * @param value The {@code short} value to be converted to an unsigned hexadecimal string.
   * @return The number of characters required to represent the specified {@code short} value as an
   *         unsigned hexadecimal string without leading zeros.
   * @see #appendHexStripLeadingZeros(short)
   */
  public static int getHexCapacity(short value) {
    if (value >>> 8 == 0) {
      return getHexCapacity((byte) value);
    }
    return 2 + getHexCapacity((byte) (value >>> 8));
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code int} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{8}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #appendHexStripLeadingZeros(int)
   */
  public final CharBuffer appendHex(int value) {
    ensureCapacity(8);
    // @formatter:off
    doAppend(DIGITS[value >>> 28 & 0x0f]);
    doAppend(DIGITS[value >>> 24 & 0x0f]);
    doAppend(DIGITS[value >>> 20 & 0x0f]);
    doAppend(DIGITS[value >>> 16 & 0x0f]);
    doAppend(DIGITS[value >>> 12 & 0x0f]);
    doAppend(DIGITS[value >>>  8 & 0x0f]);
    doAppend(DIGITS[value >>>  4 & 0x0f]);
    doAppend(DIGITS[value >>>  0 & 0x0f]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code int} value
   * without leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{1,8}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #getHexCapacity(int)
   * @see #appendHex(int)
   */
  public final CharBuffer appendHexStripLeadingZeros(int value) {
    // 16 or less bits long?
    if (value >>> 16 == 0) {
      return appendHexStripLeadingZeros((short) value);
    }
    int n = getHexCapacity(value);
    // 28 or less bits long?
    if (n < 8) {
      // from 17 to 28 bits long
      ensureCapacity(n--);
      for (n <<= 2; n >= 0; n -= 4) {
        doAppend(DIGITS[value >>> n & 0x0f]);
      }
      return this;
    }
    // from 29 to 32 bits long
    return appendHex(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code int} value as an
   * unsigned hexadecimal string without leading zeros.
   *
   * @param value The {@code int} value to be converted to an unsigned hexadecimal string.
   * @return The number of characters required to represent the specified {@code int} value as an
   *         unsigned hexadecimal string without leading zeros.
   * @see #appendHexStripLeadingZeros(int)
   */
  public static int getHexCapacity(int value) {
    if (value >>> 16 == 0) {
      return getHexCapacity((short) value);
    }
    return 4 + getHexCapacity((short) (value >>> 16));
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code long} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{16}</code>.</p>
   *
   * @param value The {@code long} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #appendHexStripLeadingZeros(long)
   */
  public final CharBuffer appendHex(long value) {
    ensureCapacity(16);
    // @formatter:off
    doAppend(DIGITS[(int) (value >>> 60 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 56 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 52 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 48 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 44 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 40 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 36 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 32 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 28 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 24 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 20 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 16 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>> 12 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>>  8 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>>  4 & 0x0fL)]);
    doAppend(DIGITS[(int) (value >>>  0 & 0x0fL)]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned hexadecimal string representation of the specified {@code long} value
   * without leading zeros to the buffer.
   *
   * <p>The format is <code>[0-9a-f]{1,16}</code></p>.
   *
   * @param value The {@code long} value to be converted to an unsigned hexadecimal string.
   * @return A reference to this buffer.
   * @see #getHexCapacity(long)
   * @see #appendHex(long)
   */
  public final CharBuffer appendHexStripLeadingZeros(long value) {
    // 32 or less bits long?
    if (value >>> 32 == 0) {
      return appendHexStripLeadingZeros((int) value);
    }
    int n = getHexCapacity(value);
    // 60 or less bits long?
    if (n < 16) {
      // from 33 to 60 bits long
      ensureCapacity(n--);
      for (n <<= 2; n >= 0; n -= 4) {
        doAppend(DIGITS[(int) (value >>> n & 0x0fL)]);
      }
      return this;
    }
    // from 61 to 64 bits long
    return appendHex(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code long} value as an
   * unsigned hexadecimal string without leading zeros.
   *
   * @param value The {@code long} value to be converted to an unsigned hexadecimal string.
   * @return The number of characters required to represent the specified {@code long} value as an
   *         unsigned hexadecimal string without leading zeros.
   * @see #appendHexStripLeadingZeros(long)
   */
  public static int getHexCapacity(long value) {
    if (value >>> 32 == 0) {
      return getHexCapacity((int) value);
    }
    return 8 + getHexCapacity((int) (value >>> 32));
  }

  // Octal representation

  /**
   * Appends an unsigned octal string representation of the specified {@code byte} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{3}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #appendOctStripLeadingZeros(byte)
   */
  public final CharBuffer appendOct(byte value) {
    ensureCapacity(3);
    // @formatter:off
    doAppend(DIGITS[value >>> 6 & 0x03]);
    doAppend(DIGITS[value >>> 3 & 0x07]);
    doAppend(DIGITS[value >>> 0 & 0x07]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code byte} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{1,3}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #getOctCapacity(byte)
   * @see #appendOct(byte)
   */
  public final CharBuffer appendOctStripLeadingZeros(byte value) {
    // 3 or less bits long?
    if (value >>> 3 == 0) {
      ensureCapacity(1);
      return doAppend(DIGITS[value]);
    }
    // 6 or less bits long?
    if (value >>> 6 == 0) {
      // from 4 to 6 bits long
      ensureCapacity(2);
      doAppend(DIGITS[value >>> 3 & 0x07]);
      doAppend(DIGITS[value >>> 0 & 0x07]);
      return this;
    }
    // from 7 to 8 bits long
    return appendOct(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code byte} value as an
   * unsigned octal string without leading zeros.
   *
   * @param value The {@code byte} value to be converted to an unsigned octal string.
   * @return The number of characters required to represent the specified {@code byte} value as an
   *         unsigned octal string without leading zeros.
   * @see #appendOctStripLeadingZeros(byte)
   */
  public static int getOctCapacity(byte value) {
    return value >>> 6 == 0 ? value >>> 3 == 0 ? 1 : 2 : 3;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code short} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{6}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #appendOctStripLeadingZeros(short)
   */
  public final CharBuffer appendOct(short value) {
    ensureCapacity(6);
    // @formatter:off
    doAppend(DIGITS[value >>> 15 & 0x01]);
    doAppend(DIGITS[value >>> 12 & 0x07]);
    doAppend(DIGITS[value >>>  9 & 0x07]);
    doAppend(DIGITS[value >>>  6 & 0x07]);
    doAppend(DIGITS[value >>>  3 & 0x07]);
    doAppend(DIGITS[value >>>  0 & 0x07]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code short} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{1,6}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #getOctCapacity(short)
   * @see #appendOct(short)
   */
  public final CharBuffer appendOctStripLeadingZeros(short value) {
    // 8 or less bits long?
    if (value >>> 8 == 0) {
      return appendOctStripLeadingZeros((byte) value);
    }
    int n = getOctCapacity(value);
    // 15 or less bits long?
    if (n < 6) {
      // from 9 to 15 bits long
      ensureCapacity(n--);
      for (n *= 3; n >= 0; n -= 3) {
        doAppend(DIGITS[value >>> n & 0x07]);
      }
      return this;
    }
    // 16 bits long
    return appendOct(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code short} value as an
   * unsigned octal string without leading zeros.
   *
   * @param value The {@code short} value to be converted to an unsigned octal string.
   * @return The number of characters required to represent the specified {@code short} value as an
   *         unsigned octal string without leading zeros.
   * @see #appendOctStripLeadingZeros(short)
   */
  public static int getOctCapacity(short value) {
    if (value >>> 8 == 0) {
      return getOctCapacity((byte) value);
    }
    if (value >>> 12 == 0) {
      return value >>> 9 == 0 ? 3 : 4;
    }
    return value >>> 15 == 0 ? 5 : 6;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code int} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{11}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #appendOctStripLeadingZeros(int)
   */
  public final CharBuffer appendOct(int value) {
    ensureCapacity(11);
    // @formatter:off
    doAppend(DIGITS[value >>> 30 & 0x03]);
    doAppend(DIGITS[value >>> 27 & 0x07]);
    doAppend(DIGITS[value >>> 24 & 0x07]);
    doAppend(DIGITS[value >>> 21 & 0x07]);
    doAppend(DIGITS[value >>> 18 & 0x07]);
    doAppend(DIGITS[value >>> 15 & 0x07]);
    doAppend(DIGITS[value >>> 12 & 0x07]);
    doAppend(DIGITS[value >>>  9 & 0x07]);
    doAppend(DIGITS[value >>>  6 & 0x07]);
    doAppend(DIGITS[value >>>  3 & 0x07]);
    doAppend(DIGITS[value >>>  0 & 0x07]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code int} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{1,11}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #getOctCapacity(int)
   * @see #appendOct(int)
   */
  public final CharBuffer appendOctStripLeadingZeros(int value) {
    // 16 or less bits long?
    if (value >>> 16 == 0) {
      return appendOctStripLeadingZeros((short) value);
    }
    int n = getOctCapacity(value);
    // 30 or less bits long?
    if (n < 11) {
      // from 17 to 30 bits long
      ensureCapacity(n--);
      for (n *= 3; n >= 0; n -= 3) {
        doAppend(DIGITS[value >>> n & 0x07]);
      }
      return this;
    }
    // from 31 to 32 bits long
    return appendOct(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code int} value as an
   * unsigned octal string without leading zeros.
   *
   * @param value The {@code int} value to be converted to an unsigned octal string.
   * @return The number of characters required to represent the specified {@code int} value as an
   *         unsigned octal string without leading zeros.
   * @see #appendOctStripLeadingZeros(int)
   */
  public static int getOctCapacity(int value) {
    if (value >>> 16 == 0) {
      return getOctCapacity((short) value);
    }
    if (value >>> 24 == 0) {
      return value >>> 21 == 0 ? value >>> 18 == 0 ? 6 : 7 : 8;
    }
    return value >>> 30 == 0 ? value >>> 27 == 0 ? 9 : 10 : 11;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code long} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{22}</code>.</p>
   *
   * @param value The {@code long} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #appendOctStripLeadingZeros(long)
   */
  public final CharBuffer appendOct(long value) {
    ensureCapacity(22);
    // @formatter:off
    doAppend(DIGITS[(int) (value >>> 63 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 60 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 57 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 54 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 51 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 48 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 45 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 42 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 39 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 36 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 33 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 30 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 27 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 24 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 21 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 18 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 15 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>> 12 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>>  9 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>>  6 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>>  3 & 0x07L)]);
    doAppend(DIGITS[(int) (value >>>  0 & 0x07L)]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned octal string representation of the specified {@code long} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-7]{1,22}</code>.</p>
   *
   * @param value The {@code long} value to be converted to an unsigned octal string.
   * @return A reference to this buffer.
   * @see #getOctCapacity(long)
   * @see #appendOct(long)
   */
  public final CharBuffer appendOctStripLeadingZeros(long value) {
    // 32 or less bits long?
    if (value >>> 32 == 0L) {
      return appendOctStripLeadingZeros((int) value);
    }
    int n = getOctCapacity(value);
    // 63 or less bits long?
    if (n < 22) {
      // from 33 to 63 bits long
      ensureCapacity(n--);
      for (n *= 3; n >= 0; n -= 3) {
        doAppend(DIGITS[(int) (value >>> n & 0x07L)]);
      }
      return this;
    }
    // 64 bits long
    return appendOct(value);
  }

  /**
   * Returns the number of characters required to represent the specified {@code long} value as an
   * unsigned octal string without leading zeros.
   *
   * @param value The {@code long} value to be converted to an unsigned octal string.
   * @return The number of characters required to represent the specified {@code long} value as an
   *         unsigned octal string without leading zeros.
   * @see #appendOctStripLeadingZeros(long)
   */
  public static int getOctCapacity(long value) {
    if (value >>> 32 == 0L) {
      return getOctCapacity((int) value);
    }
    if (value >>> 48 == 0L) {
      if (value >>> 39 == 0L) {
        return value >>> 36 == 0L ? value >>> 33 == 0L ? 11 : 12 : 13;
      }
      return value >>> 45 == 0L ? value >>> 42 == 0L ? 14 : 15 : 16;
    }
    if (value >>> 57 == 0L) {
      return value >>> 54 == 0L ? value >>> 51 == 0L ? 17 : 18 : 19;
    }
    return value >>> 63 == 0L ? value >>> 60 == 0L ? 20 : 21 : 22;
  }

  // Binary representation

  /**
   * Appends an unsigned binary string representation of the specified {@code byte} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{8}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinStripLeadingZeros(byte)
   */
  public final CharBuffer appendBin(byte value) {
    ensureCapacity(8);
    // @formatter:off
    doAppend(DIGITS[value >>> 7 & 0x01]);
    doAppend(DIGITS[value >>> 6 & 0x01]);
    doAppend(DIGITS[value >>> 5 & 0x01]);
    doAppend(DIGITS[value >>> 4 & 0x01]);
    doAppend(DIGITS[value >>> 3 & 0x01]);
    doAppend(DIGITS[value >>> 2 & 0x01]);
    doAppend(DIGITS[value >>> 1 & 0x01]);
    doAppend(DIGITS[value >>> 0 & 0x01]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code byte} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{1,8}</code>.</p>
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(byte)
   * @see #appendBin(byte)
   */
  public final CharBuffer appendBinStripLeadingZeros(byte value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      doAppend(DIGITS[value >>> n & 0x01]);
    }
    return doAppend(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code byte} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code byte} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code byte} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinStripLeadingZeros(byte)
   */
  public static int getBinCapacity(byte value) {
    if (value >>> 4 == 0) {
      if (value >>> 2 == 0) {
        return value >>> 1 == 0 ? 1 : 2;
      }
      return value >>> 3 == 0 ? 3 : 4;
    }
    if (value >>> 6 == 0) {
      return value >>> 5 == 0 ? 5 : 6;
    }
    return value >>> 7 == 0 ? 7 : 8;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code short} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{16}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinStripLeadingZeros(short)
   */
  public final CharBuffer appendBin(short value) {
    ensureCapacity(16);
    // @formatter:off
    doAppend(DIGITS[value >>> 15 & 0x01]);
    doAppend(DIGITS[value >>> 14 & 0x01]);
    doAppend(DIGITS[value >>> 13 & 0x01]);
    doAppend(DIGITS[value >>> 12 & 0x01]);
    doAppend(DIGITS[value >>> 11 & 0x01]);
    doAppend(DIGITS[value >>> 10 & 0x01]);
    doAppend(DIGITS[value >>>  9 & 0x01]);
    doAppend(DIGITS[value >>>  8 & 0x01]);
    doAppend(DIGITS[value >>>  7 & 0x01]);
    doAppend(DIGITS[value >>>  6 & 0x01]);
    doAppend(DIGITS[value >>>  5 & 0x01]);
    doAppend(DIGITS[value >>>  4 & 0x01]);
    doAppend(DIGITS[value >>>  3 & 0x01]);
    doAppend(DIGITS[value >>>  2 & 0x01]);
    doAppend(DIGITS[value >>>  1 & 0x01]);
    doAppend(DIGITS[value >>>  0 & 0x01]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code short} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{1,16}</code>.</p>
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(short)
   * @see #appendBin(short)
   */
  public final CharBuffer appendBinStripLeadingZeros(short value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      doAppend(DIGITS[value >>> n & 0x01]);
    }
    return doAppend(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code short} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code short} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code short} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinStripLeadingZeros(short)
   */
  public static int getBinCapacity(short value) {
    if (value >>> 8 == 0) {
      return getBinCapacity((byte) value);
    }
    return 8 + getBinCapacity((byte) (value >>> 8));
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code int} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{32}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinStripLeadingZeros(int)
   */
  public final CharBuffer appendBin(int value) {
    ensureCapacity(32);
    // @formatter:off
    doAppend(DIGITS[value >>> 31 & 0x01]);
    doAppend(DIGITS[value >>> 30 & 0x01]);
    doAppend(DIGITS[value >>> 29 & 0x01]);
    doAppend(DIGITS[value >>> 28 & 0x01]);
    doAppend(DIGITS[value >>> 27 & 0x01]);
    doAppend(DIGITS[value >>> 26 & 0x01]);
    doAppend(DIGITS[value >>> 25 & 0x01]);
    doAppend(DIGITS[value >>> 24 & 0x01]);
    doAppend(DIGITS[value >>> 23 & 0x01]);
    doAppend(DIGITS[value >>> 22 & 0x01]);
    doAppend(DIGITS[value >>> 21 & 0x01]);
    doAppend(DIGITS[value >>> 20 & 0x01]);
    doAppend(DIGITS[value >>> 19 & 0x01]);
    doAppend(DIGITS[value >>> 18 & 0x01]);
    doAppend(DIGITS[value >>> 17 & 0x01]);
    doAppend(DIGITS[value >>> 16 & 0x01]);
    doAppend(DIGITS[value >>> 15 & 0x01]);
    doAppend(DIGITS[value >>> 14 & 0x01]);
    doAppend(DIGITS[value >>> 13 & 0x01]);
    doAppend(DIGITS[value >>> 12 & 0x01]);
    doAppend(DIGITS[value >>> 11 & 0x01]);
    doAppend(DIGITS[value >>> 10 & 0x01]);
    doAppend(DIGITS[value >>>  9 & 0x01]);
    doAppend(DIGITS[value >>>  8 & 0x01]);
    doAppend(DIGITS[value >>>  7 & 0x01]);
    doAppend(DIGITS[value >>>  6 & 0x01]);
    doAppend(DIGITS[value >>>  5 & 0x01]);
    doAppend(DIGITS[value >>>  4 & 0x01]);
    doAppend(DIGITS[value >>>  3 & 0x01]);
    doAppend(DIGITS[value >>>  2 & 0x01]);
    doAppend(DIGITS[value >>>  1 & 0x01]);
    doAppend(DIGITS[value >>>  0 & 0x01]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code int} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{1,32}</code>.</p>
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(int)
   * @see #appendBin(int)
   */
  public final CharBuffer appendBinStripLeadingZeros(int value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      doAppend(DIGITS[value >>> n & 0x01]);
    }
    return doAppend(DIGITS[value & 0x01]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code int} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code int} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code int} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinStripLeadingZeros(int)
   */
  public static int getBinCapacity(int value) {
    if (value >>> 16 == 0) {
      return getBinCapacity((short) value);
    }
    return 16 + getBinCapacity((short) (value >>> 16));
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code long} value with
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{64}</code>.</p>
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #appendBinStripLeadingZeros(long)
   */
  public final CharBuffer appendBin(long value) {
    ensureCapacity(64);
    // @formatter:off
    doAppend(DIGITS[(int) (value >>> 63 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 62 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 61 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 60 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 59 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 58 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 57 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 56 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 55 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 54 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 53 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 52 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 51 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 50 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 49 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 48 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 47 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 46 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 45 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 44 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 43 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 42 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 41 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 40 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 39 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 38 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 37 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 36 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 35 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 34 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 33 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 32 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 31 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 30 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 29 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 28 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 27 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 26 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 25 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 24 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 23 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 22 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 21 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 20 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 19 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 18 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 17 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 16 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 15 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 14 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 13 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 12 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 11 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>> 10 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  9 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  8 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  7 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  6 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  5 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  4 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  3 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  2 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  1 & 0x01L)]);
    doAppend(DIGITS[(int) (value >>>  0 & 0x01L)]);
    // @formatter:on
    return this;
  }

  /**
   * Appends an unsigned binary string representation of the specified {@code long} value without
   * leading zeros to the buffer.
   *
   * <p>The format is <code>[0-1]{1,64}</code>.</p>
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return A reference to this buffer.
   * @see #getBinCapacity(long)
   * @see #appendBin(long)
   */
  public final CharBuffer appendBinStripLeadingZeros(long value) {
    int n = getBinCapacity(value);
    ensureCapacity(n--);
    for (; n > 0; n--) {
      doAppend(DIGITS[(int) (value >>> n & 0x01L)]);
    }
    return doAppend(DIGITS[(int) (value & 0x01L)]);
  }

  /**
   * Returns the number of characters required to represent the specified {@code long} value as an
   * unsigned binary string without leading zeros.
   *
   * @param value The {@code long} value to be converted to an unsigned binary string.
   * @return The number of characters required to represent the specified {@code long} value as an
   *         unsigned binary string without leading zeros.
   * @see #appendBinStripLeadingZeros(long)
   */
  public static int getBinCapacity(long value) {
    if (value >>> 32 == 0L) {
      return getBinCapacity((int) value);
    }
    return 32 + getBinCapacity((int) (value >>> 32));
  }

  // Object to string representation

  /**
   * The map of handlers that override {@link Object#toString()} behavior for specific classes.
   */
  private static final Map<Class<?>, ToString.Overrider<?>> TO_STRING_OVERRIDERS = new IdentityHashMap<>();
  static {
    TO_STRING_OVERRIDERS.put(java.net.URI.class, ToString.Overrider.URI);
  }

  /**
   * The string representation of the {@code null} reference.
   */
  private static final char[] NULL_REFERENCE = {
      'n', 'u', 'l', 'l'
  };

  /**
   * The objects cross-reference map to detect circular references.
   */
  private IdentityHashMap<Object, CharBuffer> crossrefs;

  /**
   * Appends string representation of the {@code null} reference to the buffer.
   *
   * <p>The format is <code>(null)</code>.</p>
   *
   * @return A reference to this buffer.
   */
  public CharBuffer appendNull() {
    return append(NULL_REFERENCE);
  }

  /**
   * Appends string representation of the specified object to the buffer.
   *
   * <p>The cross-reference map is used to detect circular references between objects.</p>
   *
   * @param object The object to append to the buffer.
   * @return A reference to this buffer.
   */
  public CharBuffer appendObject(Object object) {
    // discard null reference
    if (object == null) {
      return appendNull();
    }
    // check known types (in order of probability)
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
   * Appends a string representation of all elements of the specified {@code Object[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendObject(Object)} method.</p>
   *
   * @param array The {@code Object[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   * @see #appendNull()
   */
  public CharBuffer appendObjectArray(Object[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 4); // guessing
    doAppend('[');
    // remember reference to the object and check for circular reference
    if (pushReference(array)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(',').append(' ').appendObject(array[index]);
      }
    } finally {
      // forget reference to the array
      popReference(array);
    }
    return append(']');
  }

  /**
   * Appends a string representation of the specified {@code boolean} value to the buffer. Default
   * implementation uses the {@link #appendBool(boolean)} method.
   *
   * <p>The format is <code>(true)|(false)</code>.</p>
   *
   * @param value The {@code boolean} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendBool(boolean)
   */
  public CharBuffer appendBoolean(boolean value) {
    return appendBool(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code boolean[]} array to
   * the buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendBoolean(boolean)} method.</p>
   *
   * @param array The {@code boolean[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendBoolean(boolean)
   * @see #appendNull()
   */
  public CharBuffer appendBooleanArray(boolean[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * FALSE_CONSTANT.length); // almost guessing
    doAppend('[').appendBoolean(array[0]);
    for (int index = 1; index < array.length; index++) {
      // cannot use append0() because appendBoolean() may be overridden
      append(',').append(' ').appendBoolean(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code byte} value to the buffer. Default
   * implementation uses the {@link #appendDec(int)} method.
   *
   * <p>The format is <code>\-?[0-9]{1,3}</code>.</p>
   *
   * @param value The {@code byte} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(int)
   */
  public CharBuffer appendByte(byte value) {
    return appendDec(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code byte[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendByte(byte)} method.</p>
   *
   * @param array The {@code byte[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendByte(byte)
   * @see #appendNull()
   */
  public CharBuffer appendByteArray(byte[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 4); // guessing
    doAppend('[').appendByte(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendByte(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code short} value to the buffer. Default
   * implementation uses the {@link #appendDec(int)} method.
   *
   * <p>The format is <code>\-?[0-9]{1,5}</code>.</p>
   *
   * @param value The {@code short} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(int)
   */
  public CharBuffer appendShort(short value) {
    return appendDec(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code short[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendShort(short)} method.</p>
   *
   * @param array The {@code short[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendShort(short)
   * @see #appendNull()
   */
  public CharBuffer appendShortArray(short[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 4); // guessing
    doAppend('[').appendShort(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendShort(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code int} value to the buffer. Default
   * implementation uses the {@link #appendDec(int)} method.
   *
   * <p>The format is <code>\-?[0-9]{1,10}</code>.</p>
   *
   * @param value The {@code int} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(int)
   */
  public CharBuffer appendInt(int value) {
    return appendDec(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code int[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendInt(int)} method.</p>
   *
   * @param array The {@code int[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendInt(int)
   * @see #appendNull()
   */
  public CharBuffer appendIntArray(int[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 4); // guessing
    doAppend('[').appendInt(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendInt(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code long} value to the buffer. Default
   * implementation uses the {@link #appendDec(long)} method.
   *
   * <p>The format is <code>\-?[0-9]{1,19}L</code>.</p>
   *
   * @param value The {@code long} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(long)
   */
  public CharBuffer appendLong(long value) {
    return appendDec(value).append('L');
  }

  /**
   * Appends a string representation of all elements of the specified {@code long[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendLong(long)} method.</p>
   *
   * @param array The {@code long[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendLong(long)
   * @see #appendNull()
   */
  public CharBuffer appendLongArray(long[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 4); // guessing
    doAppend('[').appendLong(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendLong(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code float} value to the buffer. Default
   * implementation uses the {@link #appendDec(float)} method.
   *
   * <p>The format is <code>(NaN)|(\-?Infinity)|(\-?[0-9]*\.?[0-9]+([eE]\-?[0-9]+)?f)</code>.</p>
   *
   * @param value The {@code float} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(float)
   */
  public CharBuffer appendFloat(float value) {
    return Float.isFinite(value) ? appendDec(value).append('f') : appendDec(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code float[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendFloat(float)} method.</p>
   *
   * @param array The {@code float[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendFloat(float)
   * @see #appendNull()
   */
  public CharBuffer appendFloatArray(float[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 6); // guessing
    doAppend('[').appendFloat(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendFloat(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code double} value to the buffer. Default
   * implementation uses the {@link #appendDec(double)} method.
   *
   * <p>The format is <code>(NaN)|(\-?Infinity)|(\-?[0-9]*\.?[0-9]+([eE]\-?[0-9]+)?d)</code>.</p>
   *
   * @param value The {@code double} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(double)
   */
  public CharBuffer appendDouble(double value) {
    return Double.isFinite(value) ? appendDec(value).append('d') : appendDec(value);
  }

  /**
   * Appends a string representation of all elements of the specified {@code double[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendDouble(double)} method.</p>
   *
   * @param array The {@code double[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDouble(double)
   * @see #appendNull()
   */
  public CharBuffer appendDoubleArray(double[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 6); // guessing
    doAppend('[').appendDouble(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendDouble(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code char} value to the buffer.
   *
   * <p>The format is <code>'C'</code>, where {@code C} is a result of character encoding applied
   * by the {@link CharEncoder#JAVA} to the specified character.</p>
   *
   * @param value The {@code char} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see CharEncoder#JAVA
   */
  public CharBuffer appendChar(char value) {
    return CharEncoder.JAVA.encode(value, append('\'')).append('\'');
  }

  /**
   * Appends a string representation of all elements of the specified {@code char[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendChar(char)} method.</p>
   *
   * @param array The {@code char[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendChar(char)
   * @see #appendNull()
   */
  public CharBuffer appendCharArray(char[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    ensureCapacity(array.length * 5); // guessing
    doAppend('[').appendChar(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendChar(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code CharSequence} value to the buffer. If
   * the specified {@code CharSequence} reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>"(C<sub>n={0 &lt;= n &lt; L</sub>)*"</code>, where {@code L} is length
   * of the character sequence and <code>C<sub>n</sub></code> is a result of character encoding
   * applied by the {@link CharEncoder#JAVA} to n<sup>th</sup> character in the specified character
   * sequence.</p>
   *
   * @param value The character sequence which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see CharEncoder#JAVA
   * @see #appendNull()
   */
  public CharBuffer appendString(CharSequence value) {
    if (value == null) {
      return appendNull();
    }
    ensureCapacity(value.length() + 2); // guessing
    return CharEncoder.JAVA.encode(value, append('\"')).append('\"');
  }

  /**
   * Appends string representation of the specified {@code Enum<?>} value to the buffer. Default
   * implementation just appends result of the {@link Enum#name()} method to the buffer. If the
   * specified {@code Enum<?>} reference is {@code null} then the resulting string representation
   * will be provided by the {@link #appendNull()} method.
   *
   * @param value The enumeration constant which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   * @see #appendNull()
   */
  public CharBuffer appendEnum(Enum<?> value) {
    return value != null ? append(value.name()) : appendNull();
  }

  /**
   * Appends a string representation of all elements of the specified {@code Iterable} sequence to
   * the buffer. If the specified sequence reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the sequence, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the sequence
   * respectively, appended to the buffer by the {@link #appendObject(Object)} method.</p>
   *
   * @param iterable The {@code Iterable} sequence which string representation to append to the
   *        buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   * @see #appendNull()
   */
  public CharBuffer appendIterable(Iterable<?> iterable) {
    // make sure that nothing is null to avoid NPE
    final Iterator<?> itr = iterable != null
        ? iterable.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      ensureCapacity(2);
      return doAppend('[').doAppend(']');
    }
    append('[');
    // remember reference to the object and check for circular reference
    if (pushReference(iterable)) {
      // cross-reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      appendObject(itr.next());
      while (itr.hasNext()) {
        append(',').append(' ').appendObject(itr.next());
      }
    } finally {
      // forget reference to the iteration
      popReference(iterable);
    }
    return append(']');
  }

  /**
   * Appends a string representation of all entries of the specified {@code Map} table to the
   * buffer. If the specified map reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\{(K<sub>0</sub>: V<sub>0</sub>(, K<sub>n={0 &lt; n &lt; L}</sub>:
   * V<sub>n</sub>)*)?\}</code>, where {@code L} is size of the map, <code>K<sub>0</sub></code>,
   * <code>V<sub>0</sub></code> are string representations of the first key value pair of the map
   * respectively, <code>K<sub>n</sub></code>, <code>V<sub>n</sub></code> are string
   * representations of the n<sup>th</sup> key value pairs of the map respectively, appended to the
   * buffer by the {@link #appendObject(Object)} method.</p>
   *
   * @param map The {@code Map} table which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   * @see #appendNull()
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
      ensureCapacity(2);
      return doAppend('{').doAppend('}');
    }
    ensureCapacity(map.size() * 10); // guessing
    doAppend('{');
    // remember reference to the object and check for circular reference
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
        append(':').append(' ');
        appendObject(entry.getValue());
      }
      while (itr.hasNext()) {
        entry = itr.next();
        append(',').append(' ');
        if (entry == null) { // who knows
          appendNull();
        } else {
          appendObject(entry.getKey());
          append(':').append(' ');
          appendObject(entry.getValue());
        }
      }
    } finally {
      // forget reference to the map
      popReference(map);
    }
    return append('}');
  }

  /**
   * Appends a string representation of the specified object to the buffer using the
   * {@code Object#toString()} method or the {@link ToString#toString(CharBuffer)} method if the
   * specified object implements {@code ToString} interface. If the specified object or its string
   * representation is {@code null} then the resulting string representation will be provided by
   * the {@link #appendNull()} method.
   *
   * @param object The object which string representation to append to the buffer.
   * @return A reference to this buffer.
   */
  public CharBuffer appendPlain(Object object) {
    if (object == null) {
      return appendNull();
    }
    // remember reference to the object and check for circular reference
    if (pushReference(object)) {
      // circular reference detected!
      // reference has already been appended to the buffer
      return this;
    }
    try {
      // FIXME Move advanced features to a subclass like AdvancedCharBuffer or EnhancedCharBuffer?
      // look for overrider first
      final ToString.Overrider<Object> overrider =
          Objects.cast(TO_STRING_OVERRIDERS.get(object.getClass()));
      if (overrider != null) {
        // redefine string representation
        overrider.toString(object, this);
        // avoid potential buffer swapping
        return this;
      }
      // check ToString interface
      if (object instanceof ToString) {
        ((ToString) object).toString(this);
        // avoid potential buffer swapping
        return this;
      }
      final String string = object.toString();
      if (string == null) {
        return appendNull();
      }
      return append(string);
    } finally {
      // forget reference to the object
      popReference(object);
    }
  }

  /**
   * Adds a reference to the specified object to the cross-reference map. If the specified object
   * reference appears for the first time then it will be added to the cross-reference map and
   * {@code false} will be returned, which means that there is no circular reference yet. But if
   * the cross-reference map already contains the specified reference then circular reference takes
   * place. In that case this method will create a string representation of the circular reference,
   * save it in the cross-reference map (multiple circular references are also possible), append
   * the reference string to the buffer and return {@code true}. The format of circular reference
   * is <code>!CLASS@HASH</code>, where {@code CLASS} is the full name of the object's class and
   * {@code HASH} is the object's hash code created by the {@link System#identityHashCode(Object)}
   * method.
   *
   * <p>Typical usage:</p>
   * <blockquote><pre>
   * // remember reference to the object and check for circular reference
   * if (pushReference(object)) {
   *   // circular reference detected!
   *   // reference has already been appended to the buffer
   *   return this;
   * }
   * try {
   *   // calls to the appendObject(), appendObjectArray(), etc.
   * } finally {
   *   // forget reference to the object
   *   popReference(object);
   * }
   * </pre></blockquote>
   *
   * @param object An object reference to be added to the cross-reference map.
   * @return {@code true} if the cross-reference map already contains the specified reference to an
   *         object (circular reference detected); {@code false} if the specified reference appears
   *         for the first time while traversing a graph of objects.
   * @see #popReference(Object)
   */
  protected final boolean pushReference(Object object) {
    if (crossrefs == null) {
      crossrefs = new IdentityHashMap<>();
    }
    if (crossrefs.containsKey(object)) {
      CharBuffer crossref = crossrefs.get(object);
      if (crossref == null) {
        final String classname = object.getClass().getName();
        crossref = new SimpleCharBuffer(classname.length() + 10);
        crossref.doAppend('!').append(classname);
        crossref.doAppend('@').appendHex(System.identityHashCode(object));
      }
      append(crossref);
      return true;
    }
    crossrefs.put(object, null);
    return false;
  }

  /**
   * Removes the specified object reference previously added by the {@link #pushReference(Object)}
   * method from the cross-reference map.
   *
   * @param object An object reference to be removed from the cross-reference map.
   * @see #pushReference(Object)
   */
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

  // Indentation

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
            doAppend(' ');
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
   * Appends an indentation of the specified length to the buffer using the specified indentation
   * character.
   *
   * @param length The number of indentation characters to append.
   * @param indent The indentation character.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, int)
   */
  public final CharBuffer appendIndent(int length, char indent) {
    if (indent == ' ') { // who knows
      return appendIndent(length);
    }
    if (require(length, INT_POSITIVE_OR_ZERO) > 0) {
      ensureCapacity(length);
      for (; length > 0; length--) {
        doAppend(indent);
      }
    }
    return this;
  }

  /**
   * Does the same as the {@link #appendIndent(int, char)} but uses the specified Unicode code
   * point of indentation character. Note that this method does not validate the specified
   * character to be a valid Unicode code point.
   *
   * @param length The number of indentation characters to append.
   * @param indent The Unicode code point of indentation character.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, char)
   */
  public final CharBuffer appendIndent(int length, int indent) {
    if (Character.isBmpCodePoint(indent)) {
      return appendIndent(length, (char) indent);
    }
    if (require(length, INT_POSITIVE_OR_ZERO) > 0) {
      ensureCapacity(length * 2);
      final char high = Character.highSurrogate(indent);
      final char low = Character.lowSurrogate(indent);
      for (; length > 0; length--) {
        doAppend(high).doAppend(low);
      }
    }
    return this;
  }

  // Joining

  public final CharBuffer appendJoin(CharSequence delimiter, CharSequence... elements) {
    // TODO
    return this;
  }

  public final <T> CharBuffer appendJoin(CharSequence delimiter, Function<T, CharSequence> mapper,
      CharSequence... elements) {
    // TODO
    return this;
  }

  public final CharBuffer appendJoin(CharSequence delimiter, Iterable<CharSequence> elements) {
    // TODO
    return this;
  }

  public final <T> CharBuffer appendJoin(CharSequence delimiter, Function<T, CharSequence> mapper,
      Iterable<CharSequence> elements) {
    // TODO
    return this;
  }

  // Encoding

  public final CharBuffer appendEncoded(CharSequence sequence, CharEncoder encoder) {
    return encoder.encode(sequence, this);
  }

}
