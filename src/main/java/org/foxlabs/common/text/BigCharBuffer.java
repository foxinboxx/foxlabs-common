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

import static org.foxlabs.common.Predicates.*;

import org.foxlabs.common.exception.ThresholdReachedException;

/**
 *
 * @author Fox Mulder
 */
public class BigCharBuffer extends CharBuffer {

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE;

  public static final int MIN_DEPTH = 1 << 5; // 32 characters long (64 bytes)

  public static final int MAX_DEPTH = 1 << 15; // 32K characters long (64K bytes)

  private final int depth;

  private final int capacity;

  private char[][] buffer;

  private int length;

  public BigCharBuffer() {
    this(MAX_THRESHOLD, MIN_DEPTH);
  }

  public BigCharBuffer(int threshold) {
    this(threshold, MIN_DEPTH);
  }

  public BigCharBuffer(int threshold, int depth) {
    super(threshold);
    // round depth to be a multiple of 32 and trim it to maximum possible
    this.depth = Math.min((((require(depth, INT_POSITIVE) - 1) >>> 5) + 1) << 5, MAX_DEPTH);
    // calculate maximum number of slots
    this.capacity = (this.threshold - 1) / this.depth + 1;
    // allocate at most 16 initial slots depending on the capacity
    this.buffer = new char[Math.min(this.capacity, 16)][];
  }

  // Basic operations

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

  // Basic operations

  @Override
  protected CharBuffer appendChar(char ch) {
    nextSlot()[length++ % depth] = ch;
    return this;
  }

  @Override
  protected final CharBuffer appendSequence(GetChars sequence, int start, int end) {
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

  @Override
  protected void extendCapacity(int nlength) {
    // calculate total number of required slots
    final int nslots = (nlength - 1) / depth + 1;
    if (nslots > buffer.length) {
      // extend buffer for new slots as x2 required slots
      final char[][] copy = new char[Math.min(nslots << 1, capacity)][];
      System.arraycopy(buffer, 0, copy, 0, (length - 1) / depth + 1);
      buffer = copy;
    }
  }

  private final char[] nextSlot() {
    // allocate a new slot if necessary
    final int index = length / depth;
    return buffer[index] == null ? buffer[index] = new char[depth] : buffer[index];
  }

  @Override
  protected char getChar(int index) {
    return buffer[index / depth][index % depth];
  }

  @Override
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
  @Override
  public final void reset() {
    length = 0;
  }

  /**
   * Clears the buffer and releases allocated memory. This method is useful when the same buffer
   * instance can be reused multiple times in a long term (for example, in an object pool).
   *
   * @see #reset()
   */
  @Override
  public final void clear() {
    final int nslots = (length - 1) / depth + 1;
    for (int index = 0; index < nslots; index++) {
      buffer[index] = null;
    }
    length = 0;
  }

}
