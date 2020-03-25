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

import org.foxlabs.common.Checks;
import org.foxlabs.common.Strings;
import org.foxlabs.common.Iterators;
import org.foxlabs.common.exception.ThresholdReachedException;

public abstract class CharBuffer implements Appendable, CharSegment, ToString {

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
    this.threshold = Checks.checkThat(threshold, threshold >= 0);
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
   * Returns a character at the specified index in the buffer contents.
   *
   * <p>The index is zero-based and must be less than {@link #length()} (i.e.
   * {@code 0 <= index < length()}).</p>
   *
   * @param index The index of a character to return.
   * @return A character at the specified index in the buffer contents.
   * @throws IndexOutOfBoundsException if the specified index is out of range.
   * @see #getChar(int)
   */
  @Override
  public final char charAt(int index) {
    return Checks.checkIndex(this, index).getChar(index);
  }

  /**
   * Does an actual character retrieval at the specified index in the buffer contents.
   *
   * <p>Subclasses should not worry about correctness of the index provided since it should already
   * be verified by the {@code public} methods.</p>
   *
   * @param index The index of a character to return.
   * @return A character at the specified index in the buffer contents.
   */
  protected abstract char getChar(int index);

  /**
   * Returns a sequence that is a copy of the buffer contents in the specified range (i.e.
   * {@code substring(start, end)}.
   *
   * <p>The start and end positions are zero-based and must not be greater than {@link #length()},
   * the start position must not be greater than the end position (i.e.
   * {@code 0 <= start <= end <= length()}).</p>
   *
   * @param start The start position in the buffer contents.
   * @param end The end position in the buffer contents.
   * @return A sequence that is a copy of the buffer contents in the specified range.
   * @throws IndexOutOfBoundsException if the specified start or end position is out of range.
   * @see #substring(int, int)
   */
  @Override
  public final CharSegment subSequence(int start, int end) {
    return CharSegment.from(substring(start, end));
  }

  /**
   * Returns a string that is a copy of the buffer contents from the specified start position
   * (i.e. {@code substring(start, length())}.
   *
   * <p>The start position is zero-based and must not be greater than {@link #length()} (i.e.
   * {@code 0 <= start <= length()}).</p>
   *
   * @param start The start position in the buffer contents.
   * @return A string that is a copy of the buffer contents from the specified start position.
   * @throws IndexOutOfBoundsException if the specified start position is out of range.
   * @see #substring(int, int)
   */
  public final String substring(int start) {
    return substring(start, length());
  }

  /**
   * Returns a string that is a copy of the buffer contents in the specified range.
   *
   * <p>The start and end positions are zero-based and must not be greater than {@link #length()},
   * the start position must not be greater than the end position (i.e.
   * {@code 0 <= start <= end <= length()}).</p>
   *
   * @param start The start position in the buffer contents.
   * @param end The end position in the buffer contents.
   * @return A string that is a copy of the buffer contents in the specified range.
   * @throws IndexOutOfBoundsException if the specified start or end position is out of range.
   * @see #toString(int, int)
   */
  public final String substring(int start, int end) {
    Checks.checkRange(this, start, end);
    return start == end ? Strings.EMPTY : toString(start, end);
  }

  /**
   * Copies characters from the buffer contents in the specified range to the specified target
   * array starting from the specified offset.
   *
   * <p>The start and end positions are zero-based and must not be greater than {@link #length()},
   * the start position must not be greater than the end position (i.e.
   * {@code 0 <= start <= end <= length()}). The target offset is zero-based and must not be
   * greater than the target array length, which must be enough to receive requested number of
   * characters (i.e. {@code 0 <= offset <= offset + end - start <= array.length}).</p>
   *
   * @param start The start position in the buffer contents.
   * @param end The end position in the buffer contents.
   * @param target The target array to copy.
   * @param offset The start position in the target array.
   * @throws NullPointerException if the specified target array is {@code null}.
   * @throws IndexOutOfBoundsException if the specified start or end position is out of range or
   *         length of the specified target array is not enough to receive requested number of
   *         characters.
   * @see #copyChars(int, int, char[], int)
   */
  @Override
  public final void copyTo(int start, int end, char[] target, int offset) {
    Checks.checkRange(this, start, end);
    Checks.checkRange(target, offset, offset + end - start);
    if (start < end) {
      copyChars(start, end, target, offset);
    }
  }

  /**
   * Does an actual copying of characters from the buffer contents in the specified range to the
   * specified target array starting from the specified offset.
   *
   * <p>Subclasses should not worry about correctness of the arguments provided since they should
   * already be verified by the {@code public} methods.</p>
   *
   * @param start The start position in the buffer contents.
   * @param end The end position in the buffer contents.
   * @param target The target array to copy.
   * @param offset The start position in the target array.
   */
  protected abstract void copyChars(int start, int end, char[] target, int offset);

  /**
   * Ensures that the buffer capacity is sufficient to append the specified number of characters
   * and returns actual number of characters that can be appended.
   *
   * <p>If the current capacity is not enough then additional space should be allocated to append
   * as many characters as possible. In other words, returned actual number of characters cannot be
   * greater than {@link #remaining()}.</p>
   *
   * @param count The desired number of characters to append.
   * @return The actual number of characters that can be appended.
   * @throws ThresholdReachedException if the buffer is full and cannot receive characters anymore.
   * @see #extendCapacity(int)
   * @see #remaining()
   */
  public final int ensureCapacity(int count) {
    long nlength = (long) length() + (long) count; // avoid int overflow
    // trim count if it exceeds threshold
    if (nlength > threshold) {
      count = (int) (nlength = threshold) - length();
      if (count == 0) { // fast check
        throw new ThresholdReachedException(this);
      }
    }
    extendCapacity((int) nlength);
    return count;
  }

  /**
   * Extends capacity of the buffer to the specified new length, if necessary, in order to be able
   * to append new characters.
   *
   * @param nlength A new requested length of the buffer.
   */
  protected abstract void extendCapacity(int nlength);

  /**
   * Appends the specified character to the buffer and increments the current length.
   *
   * <p>Note that there is no guarantee that the {@link #ensureCapacity(int)} method was called
   * right before calling this method, so subclasses must do that themselves.</p>
   *
   * @param ch The character to append.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(int)
   */
  @Override
  public abstract CharBuffer append(char ch);

  /**
   * Appends the specified character (Unicode code point) to the buffer and increases the current
   * length by 1 or 2 depending on whether the specified character is in the Basic Multilingual
   * Plane (BMP) or is it a supplementary character, respectively.
   *
   * <p>If the specified character is a supplementary character (i.e. 2 {@code char}s long) and the
   * {@link #remaining()} number of characters is 1 then none will be appended and
   * {@link ThresholdReachedException} will be thrown.
   *
   * <p>Note that this method does not validate the specified character to be a valid Unicode code
   * point.</p>
   *
   * @param ch The character (Unicode code point) to append.
   * @return A reference to this buffer.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see Character#isBmpCodePoint(int)
   * @see #append(char)
   */
  public final CharBuffer append(int ch) {
    if (Character.isBmpCodePoint(ch)) {
      return append((char) ch);
    } else if (ensureCapacity(2) < 2) {
      throw new ThresholdReachedException(this);
    } else {
      append(Character.highSurrogate(ch));
      return append(Character.lowSurrogate(ch));
    }
  }

  /**
   * Appends all characters of the specified array to the buffer and increases the current length
   * accordingly.
   *
   * <p>If threshold of the buffer is exceeded during this operation then the first
   * {@link #remaining()} characters will be appended and the {@code ThresholdReachedException}
   * will be thrown.</p>
   *
   * @param array The array of characters to append.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified array of characters is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(CharSegment)
   */
  public final CharBuffer append(char... array) {
    return append(CharSegment.from(array));
  }

  /**
   * Appends all characters of the specified sequence to the buffer and increases the current
   * length accordingly.
   *
   * <p>If threshold of the buffer is exceeded during this operation then the first
   * {@link #remaining()} characters will be appended and the {@code ThresholdReachedException}
   * will be thrown.</p>
   *
   * @param sequence The sequence of characters to append.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified sequence of characters is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(CharSegment)
   */
  @Override
  public final CharBuffer append(CharSequence sequence) {
    return append(CharSegment.from(sequence));
  }

  /**
   * Appends characters of the specified sequence in the specified range to the buffer and
   * increases the current length accordingly.
   *
   * <p>The start and end positions are zero-based and must not be greater than length of the
   * specified sequence, the start position must not be greater than the end position (i.e.
   * {@code 0 <= start <= end <= sequence.length()}). If threshold of the buffer is exceeded during
   * this operation then the first {@link #remaining()} characters will be appended and the
   * {@code ThresholdReachedException} will be thrown.</p>
   *
   * @param sequence The sequence of characters to append.
   * @param start The start position in the specified sequence.
   * @param end The end position in the specified sequence.
   * @return A reference to this buffer.
   * @throws NullPointerException if the specified sequence of characters is {@code null}.
   * @throws IndexOutOfBoundsException if the specified start or end position is out of range.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(CharSegment)
   */
  @Override
  public final CharBuffer append(CharSequence sequence, int start, int end) {
    return append(CharSegment.from(sequence, start, end));
  }

  /**
   * Does an actual appending of the characters of the specified {@link CharSegment} sequence in
   * the specified range to the buffer.
   *
   * <p>Subclasses should not worry about the correctness of the arguments provided since they
   * should already be verified by the {@code public} methods. Note that there is no guarantee that
   * the {@link #ensureCapacity(int)} method was called right before calling this method, so
   * subclasses must do that themselves.</p>
   *
   * @param segment The {@link CharSegment} to append.
   * @return A reference to this buffer.
   * @throws NullPointerException if a reference to the specified {@code segment} is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   */
  public abstract CharBuffer append(CharSegment segment);

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
    return buffer.append(this, 0, length());
  }

  /**
   * Returns a string representation of current contents of the buffer (i.e. creates a
   * {@code String} copy).
   *
   * @return A string representation of current contents of the buffer.
   * @see #substring(int, int)
   */
  @Override
  public String toString() {
    return length() == 0 ? Strings.EMPTY : toString(0, length());
  }

  /**
   * Returns a string representation of current contents of the buffer in the specified range
   * (i.e. creates a {@code String} copy).
   *
   * <p>Subclasses may want to override this method to avoid redundant array allocation.</p>
   *
   * @param start The start position in the buffer contents.
   * @param end The end position in the buffer contents.
   * @return A string representation of current contents of the buffer in the specified range.
   * @see #copyChars(int, int, char[], int)
   */
  protected String toString(int start, int end) {
    final char[] copy = new char[end - start];
    copyChars(start, end, copy, 0);
    return new String(copy);
  }

  // Boolean to string representation

  /**
   * The string representation of the {@code true} constant.
   */
  private static final CharSegment TRUE_CONSTANT = CharSegment.from('t', 'r', 'u', 'e');

  /**
   * The string representation of the {@code false} constant.
   */
  private static final CharSegment FALSE_CONSTANT = CharSegment.from('f', 'a', 'l', 's', 'e');

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
  public static int getBoolCapacity(boolean value) {
    return value ? TRUE_CONSTANT.length() : FALSE_CONSTANT.length();
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
  private static final CharSegment MIN_INT_DIGITS = CharSegment.from(
      '-', '2', '1', '4', '7', '4', '8', '3', '6', '4', '8'
  );

  /**
   * The decimal string representation of the {@code long} min value ({@code -9223372036854775808}).
   */
  private static final CharSegment MIN_LONG_DIGITS = CharSegment.from(
      '-', '9', '2', '2', '3', '3', '7', '2', '0', '3',
      '6', '8', '5', '4', '7', '7', '5', '8', '0', '8'
  );

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
      append(DIGITS[v / INT_TENS[n]]);
    }
    return append(DIGITS[v]);
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
    return MIN_INT_DIGITS.length();
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
      append(DIGITS[(int) (v / LONG_TENS[n])]);
    }
    return append(DIGITS[(int) v]);
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
    return MIN_LONG_DIGITS.length();
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
      append('-');
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
    append(DIGITS[value >>> 4 & 0x0f]);
    append(DIGITS[value >>> 0 & 0x0f]);
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
      return append(DIGITS[value]);
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
    append(DIGITS[value >>> 12 & 0x0f]);
    append(DIGITS[value >>>  8 & 0x0f]);
    append(DIGITS[value >>>  4 & 0x0f]);
    append(DIGITS[value >>>  0 & 0x0f]);
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
      append(DIGITS[value >>> 8 & 0x0f]);
      append(DIGITS[value >>> 4 & 0x0f]);
      append(DIGITS[value >>> 0 & 0x0f]);
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
    append(DIGITS[value >>> 28 & 0x0f]);
    append(DIGITS[value >>> 24 & 0x0f]);
    append(DIGITS[value >>> 20 & 0x0f]);
    append(DIGITS[value >>> 16 & 0x0f]);
    append(DIGITS[value >>> 12 & 0x0f]);
    append(DIGITS[value >>>  8 & 0x0f]);
    append(DIGITS[value >>>  4 & 0x0f]);
    append(DIGITS[value >>>  0 & 0x0f]);
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
        append(DIGITS[value >>> n & 0x0f]);
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
    append(DIGITS[(int) (value >>> 60 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 56 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 52 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 48 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 44 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 40 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 36 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 32 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 28 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 24 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 20 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 16 & 0x0fL)]);
    append(DIGITS[(int) (value >>> 12 & 0x0fL)]);
    append(DIGITS[(int) (value >>>  8 & 0x0fL)]);
    append(DIGITS[(int) (value >>>  4 & 0x0fL)]);
    append(DIGITS[(int) (value >>>  0 & 0x0fL)]);
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
        append(DIGITS[(int) (value >>> n & 0x0fL)]);
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
    append(DIGITS[value >>> 6 & 0x03]);
    append(DIGITS[value >>> 3 & 0x07]);
    append(DIGITS[value >>> 0 & 0x07]);
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
      return append(DIGITS[value]);
    }
    // 6 or less bits long?
    if (value >>> 6 == 0) {
      // from 4 to 6 bits long
      ensureCapacity(2);
      append(DIGITS[value >>> 3 & 0x07]);
      append(DIGITS[value >>> 0 & 0x07]);
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
    append(DIGITS[value >>> 15 & 0x01]);
    append(DIGITS[value >>> 12 & 0x07]);
    append(DIGITS[value >>>  9 & 0x07]);
    append(DIGITS[value >>>  6 & 0x07]);
    append(DIGITS[value >>>  3 & 0x07]);
    append(DIGITS[value >>>  0 & 0x07]);
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
        append(DIGITS[value >>> n & 0x07]);
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
    append(DIGITS[value >>> 30 & 0x03]);
    append(DIGITS[value >>> 27 & 0x07]);
    append(DIGITS[value >>> 24 & 0x07]);
    append(DIGITS[value >>> 21 & 0x07]);
    append(DIGITS[value >>> 18 & 0x07]);
    append(DIGITS[value >>> 15 & 0x07]);
    append(DIGITS[value >>> 12 & 0x07]);
    append(DIGITS[value >>>  9 & 0x07]);
    append(DIGITS[value >>>  6 & 0x07]);
    append(DIGITS[value >>>  3 & 0x07]);
    append(DIGITS[value >>>  0 & 0x07]);
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
        append(DIGITS[value >>> n & 0x07]);
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
    append(DIGITS[(int) (value >>> 63 & 0x01L)]);
    append(DIGITS[(int) (value >>> 60 & 0x07L)]);
    append(DIGITS[(int) (value >>> 57 & 0x07L)]);
    append(DIGITS[(int) (value >>> 54 & 0x07L)]);
    append(DIGITS[(int) (value >>> 51 & 0x07L)]);
    append(DIGITS[(int) (value >>> 48 & 0x07L)]);
    append(DIGITS[(int) (value >>> 45 & 0x07L)]);
    append(DIGITS[(int) (value >>> 42 & 0x07L)]);
    append(DIGITS[(int) (value >>> 39 & 0x07L)]);
    append(DIGITS[(int) (value >>> 36 & 0x07L)]);
    append(DIGITS[(int) (value >>> 33 & 0x07L)]);
    append(DIGITS[(int) (value >>> 30 & 0x07L)]);
    append(DIGITS[(int) (value >>> 27 & 0x07L)]);
    append(DIGITS[(int) (value >>> 24 & 0x07L)]);
    append(DIGITS[(int) (value >>> 21 & 0x07L)]);
    append(DIGITS[(int) (value >>> 18 & 0x07L)]);
    append(DIGITS[(int) (value >>> 15 & 0x07L)]);
    append(DIGITS[(int) (value >>> 12 & 0x07L)]);
    append(DIGITS[(int) (value >>>  9 & 0x07L)]);
    append(DIGITS[(int) (value >>>  6 & 0x07L)]);
    append(DIGITS[(int) (value >>>  3 & 0x07L)]);
    append(DIGITS[(int) (value >>>  0 & 0x07L)]);
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
        append(DIGITS[(int) (value >>> n & 0x07L)]);
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
    append(DIGITS[value >>> 7 & 0x01]);
    append(DIGITS[value >>> 6 & 0x01]);
    append(DIGITS[value >>> 5 & 0x01]);
    append(DIGITS[value >>> 4 & 0x01]);
    append(DIGITS[value >>> 3 & 0x01]);
    append(DIGITS[value >>> 2 & 0x01]);
    append(DIGITS[value >>> 1 & 0x01]);
    append(DIGITS[value >>> 0 & 0x01]);
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
      append(DIGITS[value >>> n & 0x01]);
    }
    return append(DIGITS[value & 0x01]);
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
    append(DIGITS[value >>> 15 & 0x01]);
    append(DIGITS[value >>> 14 & 0x01]);
    append(DIGITS[value >>> 13 & 0x01]);
    append(DIGITS[value >>> 12 & 0x01]);
    append(DIGITS[value >>> 11 & 0x01]);
    append(DIGITS[value >>> 10 & 0x01]);
    append(DIGITS[value >>>  9 & 0x01]);
    append(DIGITS[value >>>  8 & 0x01]);
    append(DIGITS[value >>>  7 & 0x01]);
    append(DIGITS[value >>>  6 & 0x01]);
    append(DIGITS[value >>>  5 & 0x01]);
    append(DIGITS[value >>>  4 & 0x01]);
    append(DIGITS[value >>>  3 & 0x01]);
    append(DIGITS[value >>>  2 & 0x01]);
    append(DIGITS[value >>>  1 & 0x01]);
    append(DIGITS[value >>>  0 & 0x01]);
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
      append(DIGITS[value >>> n & 0x01]);
    }
    return append(DIGITS[value & 0x01]);
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
    append(DIGITS[value >>> 31 & 0x01]);
    append(DIGITS[value >>> 30 & 0x01]);
    append(DIGITS[value >>> 29 & 0x01]);
    append(DIGITS[value >>> 28 & 0x01]);
    append(DIGITS[value >>> 27 & 0x01]);
    append(DIGITS[value >>> 26 & 0x01]);
    append(DIGITS[value >>> 25 & 0x01]);
    append(DIGITS[value >>> 24 & 0x01]);
    append(DIGITS[value >>> 23 & 0x01]);
    append(DIGITS[value >>> 22 & 0x01]);
    append(DIGITS[value >>> 21 & 0x01]);
    append(DIGITS[value >>> 20 & 0x01]);
    append(DIGITS[value >>> 19 & 0x01]);
    append(DIGITS[value >>> 18 & 0x01]);
    append(DIGITS[value >>> 17 & 0x01]);
    append(DIGITS[value >>> 16 & 0x01]);
    append(DIGITS[value >>> 15 & 0x01]);
    append(DIGITS[value >>> 14 & 0x01]);
    append(DIGITS[value >>> 13 & 0x01]);
    append(DIGITS[value >>> 12 & 0x01]);
    append(DIGITS[value >>> 11 & 0x01]);
    append(DIGITS[value >>> 10 & 0x01]);
    append(DIGITS[value >>>  9 & 0x01]);
    append(DIGITS[value >>>  8 & 0x01]);
    append(DIGITS[value >>>  7 & 0x01]);
    append(DIGITS[value >>>  6 & 0x01]);
    append(DIGITS[value >>>  5 & 0x01]);
    append(DIGITS[value >>>  4 & 0x01]);
    append(DIGITS[value >>>  3 & 0x01]);
    append(DIGITS[value >>>  2 & 0x01]);
    append(DIGITS[value >>>  1 & 0x01]);
    append(DIGITS[value >>>  0 & 0x01]);
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
      append(DIGITS[value >>> n & 0x01]);
    }
    return append(DIGITS[value & 0x01]);
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
    append(DIGITS[(int) (value >>> 63 & 0x01L)]);
    append(DIGITS[(int) (value >>> 62 & 0x01L)]);
    append(DIGITS[(int) (value >>> 61 & 0x01L)]);
    append(DIGITS[(int) (value >>> 60 & 0x01L)]);
    append(DIGITS[(int) (value >>> 59 & 0x01L)]);
    append(DIGITS[(int) (value >>> 58 & 0x01L)]);
    append(DIGITS[(int) (value >>> 57 & 0x01L)]);
    append(DIGITS[(int) (value >>> 56 & 0x01L)]);
    append(DIGITS[(int) (value >>> 55 & 0x01L)]);
    append(DIGITS[(int) (value >>> 54 & 0x01L)]);
    append(DIGITS[(int) (value >>> 53 & 0x01L)]);
    append(DIGITS[(int) (value >>> 52 & 0x01L)]);
    append(DIGITS[(int) (value >>> 51 & 0x01L)]);
    append(DIGITS[(int) (value >>> 50 & 0x01L)]);
    append(DIGITS[(int) (value >>> 49 & 0x01L)]);
    append(DIGITS[(int) (value >>> 48 & 0x01L)]);
    append(DIGITS[(int) (value >>> 47 & 0x01L)]);
    append(DIGITS[(int) (value >>> 46 & 0x01L)]);
    append(DIGITS[(int) (value >>> 45 & 0x01L)]);
    append(DIGITS[(int) (value >>> 44 & 0x01L)]);
    append(DIGITS[(int) (value >>> 43 & 0x01L)]);
    append(DIGITS[(int) (value >>> 42 & 0x01L)]);
    append(DIGITS[(int) (value >>> 41 & 0x01L)]);
    append(DIGITS[(int) (value >>> 40 & 0x01L)]);
    append(DIGITS[(int) (value >>> 39 & 0x01L)]);
    append(DIGITS[(int) (value >>> 38 & 0x01L)]);
    append(DIGITS[(int) (value >>> 37 & 0x01L)]);
    append(DIGITS[(int) (value >>> 36 & 0x01L)]);
    append(DIGITS[(int) (value >>> 35 & 0x01L)]);
    append(DIGITS[(int) (value >>> 34 & 0x01L)]);
    append(DIGITS[(int) (value >>> 33 & 0x01L)]);
    append(DIGITS[(int) (value >>> 32 & 0x01L)]);
    append(DIGITS[(int) (value >>> 31 & 0x01L)]);
    append(DIGITS[(int) (value >>> 30 & 0x01L)]);
    append(DIGITS[(int) (value >>> 29 & 0x01L)]);
    append(DIGITS[(int) (value >>> 28 & 0x01L)]);
    append(DIGITS[(int) (value >>> 27 & 0x01L)]);
    append(DIGITS[(int) (value >>> 26 & 0x01L)]);
    append(DIGITS[(int) (value >>> 25 & 0x01L)]);
    append(DIGITS[(int) (value >>> 24 & 0x01L)]);
    append(DIGITS[(int) (value >>> 23 & 0x01L)]);
    append(DIGITS[(int) (value >>> 22 & 0x01L)]);
    append(DIGITS[(int) (value >>> 21 & 0x01L)]);
    append(DIGITS[(int) (value >>> 20 & 0x01L)]);
    append(DIGITS[(int) (value >>> 19 & 0x01L)]);
    append(DIGITS[(int) (value >>> 18 & 0x01L)]);
    append(DIGITS[(int) (value >>> 17 & 0x01L)]);
    append(DIGITS[(int) (value >>> 16 & 0x01L)]);
    append(DIGITS[(int) (value >>> 15 & 0x01L)]);
    append(DIGITS[(int) (value >>> 14 & 0x01L)]);
    append(DIGITS[(int) (value >>> 13 & 0x01L)]);
    append(DIGITS[(int) (value >>> 12 & 0x01L)]);
    append(DIGITS[(int) (value >>> 11 & 0x01L)]);
    append(DIGITS[(int) (value >>> 10 & 0x01L)]);
    append(DIGITS[(int) (value >>>  9 & 0x01L)]);
    append(DIGITS[(int) (value >>>  8 & 0x01L)]);
    append(DIGITS[(int) (value >>>  7 & 0x01L)]);
    append(DIGITS[(int) (value >>>  6 & 0x01L)]);
    append(DIGITS[(int) (value >>>  5 & 0x01L)]);
    append(DIGITS[(int) (value >>>  4 & 0x01L)]);
    append(DIGITS[(int) (value >>>  3 & 0x01L)]);
    append(DIGITS[(int) (value >>>  2 & 0x01L)]);
    append(DIGITS[(int) (value >>>  1 & 0x01L)]);
    append(DIGITS[(int) (value >>>  0 & 0x01L)]);
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
      append(DIGITS[(int) (value >>> n & 0x01L)]);
    }
    return append(DIGITS[(int) (value & 0x01L)]);
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
   * The string representation of the {@code null} reference.
   */
  private static final CharSegment NULL_REFERENCE = CharSegment.from('n', 'u', 'l', 'l');

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
  public final CharBuffer appendNull() {
    return append(NULL_REFERENCE);
  }

  /**
   * Appends a string representation of the specified object to the buffer.
   *
   * <p>
   * This method just delegates creation of the resulting string representation to another method
   * depending on type of the specified object. The following table shows mappings between object
   * classes and corresponding methods that will be used to create the resulting string
   * representation:
   * <table>
   *   <tr>
   *     <th>Class</th>
   *     <th>Method</th>
   *   </tr>
   *   <tr>
   *     <td>{@code null}</td>
   *     <td>{@link #appendNull()}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code Object[]}</td>
   *     <td>{@link #appendObjectArray(Object[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Boolean}</td>
   *     <td>{@link #appendBoolean(boolean)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code boolean[]}</td>
   *     <td>{@link #appendBooleanArray(boolean[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Byte}</td>
   *     <td>{@link #appendByte(byte)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code byte[]}</td>
   *     <td>{@link #appendByteArray(byte[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Short}</td>
   *     <td>{@link #appendShort(short)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code short[]}</td>
   *     <td>{@link #appendShortArray(short[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Integer}</td>
   *     <td>{@link #appendInteger(int)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code int[]}</td>
   *     <td>{@link #appendIntegerArray(int[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Long}</td>
   *     <td>{@link #appendLong(long)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code long[]}</td>
   *     <td>{@link #appendLongArray(long[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Float}</td>
   *     <td>{@link #appendFloat(float)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code float[]}</td>
   *     <td>{@link #appendFloatArray(float[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Double}</td>
   *     <td>{@link #appendDouble(double)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code double[]}</td>
   *     <td>{@link #appendDoubleArray(double[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Character}</td>
   *     <td>{@link #appendCharacter(char)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@code char[]}</td>
   *     <td>{@link #appendCharacterArray(char[])}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link CharSequence}</td>
   *     <td>{@link #appendString(CharSequence)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Enum}</td>
   *     <td>{@link #appendEnum(Enum)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Iterable}</td>
   *     <td>{@link #appendIterable(Iterable)}</td>
   *   </tr>
   *   <tr>
   *     <td>{@link Map}</td>
   *     <td>{@link #appendMap(Map)}</td>
   *   </tr>
   *   <tr>
   *     <td><strong>None of the above</strong></td>
   *     <td>{@link #appendPlain(Object)}</td>
   *   </tr>
   * </table>
   * </p>
   *
   * @param object The object to append to the buffer.
   * @return A reference to this buffer.
   */
  public final CharBuffer appendObject(Object object) {
    // discard null reference
    if (object == null) {
      return appendNull();
    }
    // check known types (in order of probability)
    if (object instanceof CharSequence) {
      return appendString((CharSequence) object);
    }
    if (object instanceof Integer) {
      return appendInteger(((Integer) object).intValue());
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
      return appendCharacter(((Character) object).charValue());
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
      return appendIntegerArray((int[]) object);
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
      return appendCharacterArray((char[]) object);
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
  public final CharBuffer appendObjectArray(Object[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 4); // guessing
    append('[');
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
   * Appends a string representation of the specified {@code boolean} value to the buffer.
   *
   * <p>The format is <code>(true)|(false)</code>.</p>
   *
   * @param value The {@code boolean} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendBool(boolean)
   */
  public final CharBuffer appendBoolean(boolean value) {
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
  public final CharBuffer appendBooleanArray(boolean[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * FALSE_CONSTANT.length()); // almost guessing
    append('[').appendBoolean(array[0]);
    for (int index = 1; index < array.length; index++) {
      // cannot use append() because appendBoolean() may be overridden
      append(',').append(' ').appendBoolean(array[index]);
    }
    return append(']');
  }

  /**
   * Appends string representation of the specified {@code byte} value to the buffer.
   *
   * <p>The format is <code>0x[0-9a-f]{2}</code>.</p>
   *
   * @param value The {@code byte} value which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDec(int)
   */
  public final CharBuffer appendByte(byte value) {
    return append('0').append('x').appendHex(value);
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
  public final CharBuffer appendByteArray(byte[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 4); // guessing
    append('[').appendByte(array[0]);
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
  public final CharBuffer appendShort(short value) {
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
  public final CharBuffer appendShortArray(short[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 4); // guessing
    append('[').appendShort(array[0]);
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
  public final CharBuffer appendInteger(int value) {
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
   * appended to the buffer by the {@link #appendInteger(int)} method.</p>
   *
   * @param array The {@code int[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendInteger(int)
   * @see #appendNull()
   */
  public final CharBuffer appendIntegerArray(int[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 4); // guessing
    append('[').appendInteger(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendInteger(array[index]);
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
  public final CharBuffer appendLong(long value) {
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
  public final CharBuffer appendLongArray(long[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 4); // guessing
    append('[').appendLong(array[0]);
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
  public final CharBuffer appendFloat(float value) {
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
  public final CharBuffer appendFloatArray(float[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 6); // guessing
    append('[').appendFloat(array[0]);
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
  public final CharBuffer appendDouble(double value) {
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
  public final CharBuffer appendDoubleArray(double[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 6); // guessing
    append('[').appendDouble(array[0]);
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
  public final CharBuffer appendCharacter(char value) {
    return append('\'').appendEncoded(value, CharEncoder.JAVA).append('\'');
  }

  /**
   * Appends a string representation of all elements of the specified {@code char[]} array to the
   * buffer. If the specified array reference is {@code null} then the resulting string
   * representation will be provided by the {@link #appendNull()} method.
   *
   * <p>The format is <code>\[(E<sub>0</sub>(, E<sub>n={0 &lt; n &lt; L}</sub>)*)?\]</code>, where
   * {@code L} is length of the array, <code>E<sub>0</sub></code> and <code>E<sub>n</sub></code>
   * are string representations of the first and n<sup>th</sup> elements of the array respectively,
   * appended to the buffer by the {@link #appendCharacter(char)} method.</p>
   *
   * @param array The {@code char[]} array which string representation to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendCharacter(char)
   * @see #appendNull()
   */
  public final CharBuffer appendCharacterArray(char[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append('[').append(']');
    }
    ensureCapacity(array.length * 5); // guessing
    append('[').appendCharacter(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(',').append(' ').appendCharacter(array[index]);
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
  public final CharBuffer appendString(CharSequence value) {
    return value != null ? append('\"').appendEncoded(value, CharEncoder.JAVA).append('\"') : appendNull();
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
  public final CharBuffer appendEnum(Enum<?> value) {
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
  public final CharBuffer appendIterable(Iterable<?> iterable) {
    // make sure that nothing is null to avoid NPE
    final Iterator<?> itr = iterable != null
        ? iterable.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      return append('[').append(']');
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
  public final CharBuffer appendMap(Map<?, ?> map) {
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
      return append('{').append('}');
    }
    ensureCapacity(map.size() * 10); // guessing
    append('{');
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
  public final CharBuffer appendPlain(Object object) {
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
      // check ToString interface
      if (object instanceof ToString) {
        ((ToString) object).toString(this);
        // avoid potential buffer swapping
        return this;
      }
      // fallback to Object.toString() method
      final String string = object.toString();
      return string != null ? append(string) : appendNull();
    } finally {
      // forget reference to the object
      popReference(object);
    }
  }

  /**
   * Adds a reference to the specified object to the cross-reference map.
   *
   * <p>If the specified object reference appears for the first time then it will be added to the
   * cross-reference map and {@code false} will be returned, which means that there is no circular
   * reference yet. But if the cross-reference map already contains the specified reference then
   * circular reference takes place. In that case this method will create a string representation
   * of the circular reference, save it in the cross-reference map (multiple circular references
   * are also possible), append the reference string to the buffer and return {@code true}. The
   * format of circular reference is <code>!CLASS@HASH</code>, where {@code CLASS} is the full name
   * of the object's class and {@code HASH} is the object's hash code created by the
   * {@link System#identityHashCode(Object)} method.</p>
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
        crossref.append('!').append(classname);
        crossref.append('@').appendHex(System.identityHashCode(object));
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

  // Encoding

  public final CharBuffer appendEncoded(int ch, CharEncoder encoder) {
    encoder.encode(ch, this);
    return this;
  }

  public final CharBuffer appendEncoded(CharSequence sequence, CharEncoder encoder) {
    Checks.checkNotNull(encoder);
    final int length = sequence.length();
    for (int index = 0; index < length; index++) {
      encoder.encode(sequence.charAt(index), this);
    }
    return this;
  }

  // Indentation

  /**
   * An array of space sequences ({@code '\u0020'}) ranging from 4 to 16 in length, used by the
   * {@link #appendIdent(int)} method.
   */
  private static final CharSegment[] SPACES = {
      CharSegment.from(' ', ' ', ' ', ' '), // 4
      CharSegment.from(' ', ' ', ' ', ' ', ' '), // 5
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' '), // 6
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' '), // 7
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 8
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 9
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 10
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 11
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 12
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 13
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 14
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 15
      CharSegment.from(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '), // 16
  };

  /**
   * Appends an indentation of the specified length to the buffer using the {@code '\u0020'} space
   * character.
   *
   * @param count The number of {@code '\u0020'} space characters to append.
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, char)
   * @see #appendIndent(int, int)
   */
  public final CharBuffer appendIndent(int count) {
    if (Checks.checkThat(count, count >= 0) > 0) {
      ensureCapacity(count);
      do {
        if (count < 4) { // could be a bit faster
          for (; count > 0; count--) {
            append(' ');
          }
        } else {
          final CharSegment spaces = SPACES[Math.min(count, SPACES.length) - 4];
          count -= spaces.length();
          append(spaces);
        }
      } while (count > 0);
    }
    return this;
  }

  /**
   * Does the same as the {@link #appendIndent(int, char)} but uses the specified Unicode code
   * point of indentation character. Note that this method does not validate the specified
   * character to be a valid Unicode code point.
   *
   * @param count The number of indentation characters to append.
   * @param indent The indentation character (Unicode code point).
   * @return A reference to this buffer.
   * @throws IllegalArgumentException if the specified length is negative.
   * @see #appendIndent(int, char)
   */
  public final CharBuffer appendIndent(int indent, int count) {
    if (indent == ' ') { // who knows
      return appendIndent(count);
    } else if (Checks.checkThat(count, count >= 0) > 0) {
      if (Character.isBmpCodePoint(indent)) {
        final char ch = (char) indent;
        ensureCapacity(count);
        for (; count > 0; count--) {
          append(ch);
        }
      } else {
        ensureCapacity(count * 2);
        final char high = Character.highSurrogate(indent);
        final char low = Character.lowSurrogate(indent);
        for (; count > 0; count--) {
          append(high).append(low);
        }
      }
    }
    return this;
  }

  public final CharBuffer appendIndent(CharSequence indent, int count) {
    final CharSegment indentation = CharSegment.from(indent);
    if (Checks.checkThat(count, count >= 0) > 0) {
      ensureCapacity(count * indent.length());
      for (; count > 0; count--) {
        append(indentation);
      }
    }
    return this;
  }

  // Joining

  public final CharBuffer appendJoin(int delimiter, CharSequence... elements) {
    return appendJoin(delimiter, Iterators.toIterable(elements));
  }

  @SafeVarargs
  public final <T> CharBuffer appendJoin(int delimiter, Function<T, CharSequence> mapper, T... elements) {
    return appendJoin(delimiter, Iterators.withMapper(mapper, Iterators.toIterable(elements)));
  }

  public final <T> CharBuffer appendJoin(int delimiter, Function<T, CharSequence> mapper, Iterable<T> elements) {
    return appendJoin(delimiter, Iterators.withMapper(mapper, elements));
  }

  public final CharBuffer appendJoin(int delimiter, Iterable<CharSequence> elements) {
    final Iterator<CharSequence> itr = elements.iterator();
    if (itr.hasNext()) {
      // append first element
      CharSequence element = itr.next();
      if (element == null) {
        append(NULL_REFERENCE);
      } else {
        append(CharSegment.from(element));
      }
      // Is delimiter BMP or surrogate character?
      if (Character.isBmpCodePoint(delimiter)) { // BMP character
        final char ch = (char) delimiter;
        // append remaining elements
        while (itr.hasNext()) {
          append(ch);
          element = itr.next();
          if (element == null) {
            append(NULL_REFERENCE);
          } else {
            append(CharSegment.from(element));
          }
        }
      } else { // surrogate character
        final char high = Character.highSurrogate(delimiter);
        final char low = Character.lowSurrogate(delimiter);
        // append remaining elements
        while (itr.hasNext()) {
          append(high).append(low);
          element = itr.next();
          if (element == null) {
            append(NULL_REFERENCE);
          } else {
            append(CharSegment.from(element));
          }
        }
      }
    }
    return this;
  }

  public final CharBuffer appendJoin(CharSequence delimiter, CharSequence... elements) {
    return appendJoin(delimiter, Iterators.toIterable(elements));
  }

  @SafeVarargs
  public final <T> CharBuffer appendJoin(CharSequence delimiter, Function<T, CharSequence> mapper, T... elements) {
    return appendJoin(delimiter, Iterators.withMapper(mapper, Iterators.toIterable(elements)));
  }

  public final <T> CharBuffer appendJoin(CharSequence delimiter, Function<T, CharSequence> mapper, Iterable<T> elements) {
    return appendJoin(delimiter, Iterators.withMapper(mapper, elements));
  }

  public final CharBuffer appendJoin(CharSequence delimiter, Iterable<CharSequence> elements) {
    final CharSegment separator = CharSegment.from(delimiter);
    final Iterator<CharSequence> itr = elements.iterator();
    if (itr.hasNext()) {
      // append first element
      CharSequence element = itr.next();
      if (element == null) {
        append(NULL_REFERENCE);
      } else {
        append(CharSegment.from(element));
      }
      // append remaining elements
      while (itr.hasNext()) {
        append(separator);
        element = itr.next();
        if (element == null) {
          append(NULL_REFERENCE);
        } else {
          append(CharSegment.from(element));
        }
      }
    }
    return this;
  }

}
