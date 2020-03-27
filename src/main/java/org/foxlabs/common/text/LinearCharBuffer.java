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

import org.foxlabs.common.Checks;
import org.foxlabs.common.exception.ThresholdReachedException;

public final class LinearCharBuffer extends CharBuffer {

  /**
   * The maximum allowed buffer threshold. Some VMs reserve some header words in an array, that is
   * why the value is less than {@code Integer.MAX_VALUE}.
   */
  public static final int MAX_THRESHOLD = Integer.MAX_VALUE - 8;

  /**
   * Default initial capacity of the buffer.
   */
  public static final int DEFAULT_CAPACITY = 32;

  /**
   * The buffer contents.
   */
  private char[] data;

  /**
   * The current length of the buffer.
   */
  private int length;

  /**
   * Constructs a new {@code LinearCharBuffer} of the {@link #DEFAULT_CAPACITY} initial capacity
   * and with the {@link #MAX_THRESHOLD} threshold.
   *
   * @see #LinearCharBuffer(int, int)
   */
  public LinearCharBuffer() {
    this(DEFAULT_CAPACITY, MAX_THRESHOLD);
  }

  /**
   * Constructs a new {@code LinearCharBuffer} of the specified initial {@code capacity} and with
   * the {@link #MAX_THRESHOLD} threshold.
   *
   * @throws IllegalArgumentException if the specified {@code capacity} is negative.
   * @see #LinearCharBuffer(int, int)
   */
  public LinearCharBuffer(int capacity) {
    this(capacity, MAX_THRESHOLD);
  }

  /**
   * Constructs a new {@code LinearCharBuffer} of the specified initial {@code capacity} and with
   * the specified {@code threshold}.
   *
   * <p>If the specified {@code threshold} is greater than {@link #MAX_THRESHOLD} then it will be
   * quietly reduced to the {@link #MAX_THRESHOLD}. If the specified {@code capacity} is greater
   * than the specified {@code threshold} then it will be quietly reduced to the {@code threshold}
   * as well. Zero length {@code capacity} and {@code threshold} are allowed.</p>
   *
   * @throws IllegalArgumentException if the specified {@code capacity} or {@code threshold} is
   *         negative.
   * @see CharBuffer#CharBuffer(int)
   */
  public LinearCharBuffer(int capacity, int threshold) {
    super(Math.min(threshold, MAX_THRESHOLD));
    this.data = new char[Math.min(Checks.checkThat(capacity, capacity >= 0), this.threshold)];
  }

  /**
   * Returns the current length of the buffer (i.e. the current number of characters that have
   * already been appended to the buffer).
   */
  @Override
  public final int length() {
    return length;
  }

  /**
   * Returns a character at the specified {@code index} in the buffer contents.
   *
   * <p>The {@code index} is zero-based and must be less than {@link #length()} (i.e.
   * {@code 0 <= index < length()}).</p>
   *
   * @throws IndexOutOfBoundsException if the specified {@code index} is out of range.
   */
  @Override
  public final char charAt(int index) {
    if (index < length) { // fast check
      try {
        // throws AIOOBE if index is negative
        return data[index];
      } catch (ArrayIndexOutOfBoundsException e) {
        // handle it a bit later
      }
    }
    // throw IOOBE exception with pretty message
    // performance does not matter now
    Checks.checkIndex(this, index);
    // should never happen
    throw new InternalError();
  }

  /**
   * Returns a character (Unicode code point) at the specified {@code index} in the buffer contents.
   *
   * <p>The {@code index} is zero-based and must be less than {@link #length()} (i.e.
   * {@code 0 <= index < length()}).</p>
   *
   * @throws IndexOutOfBoundsException if the specified {@code index} is out of range.
   */
  @Override
  public final int codePointAt(int index) {
    if (index < length) { // fast check
      try {
        // throws AIOOBE if index is negative
        final char high = data[index];
        // detect supplementary character
        if (Character.isHighSurrogate(high) && ++index < length) {
          final char low = data[index];
          if (Character.isLowSurrogate(low)) {
            return Character.toCodePoint(high, low);
          }
        }
        return high;
      } catch (ArrayIndexOutOfBoundsException e) {
        // handle it a bit later
      }
    }
    // throw IOOBE exception with pretty message
    // performance does not matter now
    Checks.checkIndex(this, index);
    // should never happen
    throw new InternalError();
  }

  /**
   * Appends the specified {@code ch} character to the buffer contents and increments the current
   * length.
   *
   * <p>If current capacity of the buffer is not enough then additional space will be automatically
   * allocated.</p>
   *
   * @throws ThresholdReachedException if threshold of the buffer has been exceeded.
   * @see #append(int)
   */
  @Override
  public final CharBuffer append(char ch) {
    // check buffer capacity first
    // it cannot be greater than threshold
    if (length >= data.length) {
      // check threshold
      if (length >= threshold) {
        throw new ThresholdReachedException(this);
      }
      // double the buffer
      doubleBuffer(length + 2);
    }
    // append character
    data[length++] = ch;
    return this;
  }

  /**
   * Appends the specified {@code cp} character (Unicode code point) to the buffer contents and
   * increases the current length by 1 or 2 depending on whether the specified character is in the
   * Basic Multilingual Plane (BMP) or is it a supplementary character, respectively.
   *
   * <p>If current capacity of the buffer is not enough then additional space will be automatically
   * allocated. If the specified character is a supplementary character (i.e. 2 {@code char}s long)
   * and the {@link #remaining()} number of characters is 1 then none will be appended and
   * {@link ThresholdReachedException} will be thrown.</p>
   *
   * <p>Note that this method does not validate the specified character to be a valid Unicode code
   * point.</p>
   *
   * @throws ThresholdReachedException if threshold of the buffer has been reached.
   * @see #append(char)
   */
  @Override
  public final CharBuffer append(int cp) {
    // detect BMP or supplementary character
    if (Character.isBmpCodePoint(cp)) {
      return append((char) cp);
    }
    final int nlength = length + 2; // overflow-safe (MAX_THRESHOLD)
    // check buffer capacity first
    // it cannot be greater than threshold
    if (nlength > data.length) {
      // check threshold
      if (nlength > threshold) {
        throw new ThresholdReachedException(this);
      }
      // double the buffer
      doubleBuffer(nlength);
    }
    // append high and low surrogates
    data[length++] = Character.highSurrogate(cp);
    data[length++] = Character.lowSurrogate(cp);
    return this;
  }

  /**
   * Appends the specified {@code segment} of characters to the buffer contents and increases
   * current length accordingly.
   *
   * <p>If current capacity of the buffer is not enough then additional space will be automatically
   * allocated. If threshold of the buffer is exceeded during this operation then remaining number
   * of characters (i.e. {@code threshold - length}) will be copied anyway.</p>
   *
   * @throws NullPointerException if the specified {@code segment} reference is {@code null}.
   * @throws ThresholdReachedException if threshold of the buffer has been exceeded.
   */
  @Override
  public CharBuffer append(CharSegment segment) {
    int count = segment.length();
    // fast current capacity check
    if (data.length - count >= length) {
      // current capacity is enough
      segment.copyTo(0, count, data, length);
      length += count;
      return this;
    }
    // check threshold
    if (threshold - count >= length) {
      // threshold is not reached
      // double the buffer
      doubleBuffer(length + count);
      segment.copyTo(0, count, data, length);
      length += count;
      return this;
    }
    // threshold exceeded
    if (length < threshold) {
      // copy remainder
      segment.copyTo(0, threshold - length, data, length);
      length = threshold;
    }
    throw new ThresholdReachedException(this);
  }

  @Override
  public void reset() {
    length = 0;
  }

  @Override
  public void clear() {
    length = 0;
  }

  @Override
  protected String toString(int start, int end) {
    return new String(data, start, end);
  }

  @Override
  protected void copyChars(int start, int end, char[] target, int offset) {
    System.arraycopy(data, start, target, offset, end - start);
  }

  @Override
  protected void extendCapacity(int nlength) {
    if (nlength > data.length) {
      final char[] copy = new char[Math.min(nlength << 1, threshold)];
      System.arraycopy(data, 0, copy, 0, data.length);
      data = copy;
    }
  }

  private void doubleBuffer(long nlength) {
    final char[] copy = new char[(int) Math.min(nlength << 1, threshold)];
    System.arraycopy(data, 0, copy, 0, data.length);
    data = copy;
  }

}
