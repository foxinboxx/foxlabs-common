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

import org.foxlabs.common.exception.ThresholdReachedException;

import static org.foxlabs.common.Predicates.*;
import static org.foxlabs.common.Predicates.ExceptionProvider.*;

public class CharacterBuffer {

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE;

  public static final int LOG_THRESHOLD = 4096;

  public static final int MIN_DEPTH = 1 << 5; // 32 characters long (64 bytes)

  public static final int MAX_DEPTH = 1 << 15; // 32K characters long (64K bytes)

  private final int threshold;

  private final int depth;

  private final int capacity;

  private char[][] buffer;

  private int length;

  public CharacterBuffer() {
    this(MAX_THRESHOLD, MIN_DEPTH);
  }

  public CharacterBuffer(int threshold) {
    this(threshold, MIN_DEPTH);
  }

  public CharacterBuffer(int threshold, int depth) {
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

  // Append operations

  public final CharacterBuffer append(char value) {
    ensureCapacity(1);
    nextSlot()[length++ % depth] = value;
    return this;
  }

  public final CharacterBuffer append(char[] values) {
    return appendSafe(values, 0, values.length);
  }

  public final CharacterBuffer append(char[] values, int start) {
    require(requireNonNull(values), checkCharArrayRange(start), ofIOOB(start));
    return appendSafe(values, start, values.length);
  }

  public final CharacterBuffer append(char[] values, int start, int end) {
    require(requireNonNull(values), checkCharArrayRange(start, end), ofIOOB(start, end));
    return appendSafe(values, start, end);
  }

  public final CharacterBuffer append(String string) {
    return appendSafe(string, 0, string.length());
  }

  public final CharacterBuffer append(String string, int start) {
    require(requireNonNull(string), checkCharSequenceRange(start), ofIOOB(start));
    return appendSafe(string, start, string.length());
  }

  public final CharacterBuffer append(String string, int start, int end) {
    require(requireNonNull(string), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return appendSafe(string, start, end);
  }

  public final CharacterBuffer append(StringBuilder buffer) {
    return appendSafe(buffer, 0, buffer.length());
  }

  public final CharacterBuffer append(StringBuilder buffer, int start) {
    require(requireNonNull(buffer), checkCharSequenceRange(start), ofIOOB(start));
    return appendSafe(buffer, start, buffer.length());
  }

  public final CharacterBuffer append(StringBuilder buffer, int start, int end) {
    require(requireNonNull(buffer), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return appendSafe(buffer, start, end);
  }

  public final CharacterBuffer append(CharSequence sequence) {
    requireNonNull(sequence);
    return sequence instanceof String
        ? appendSafe((String) sequence, 0, sequence.length())
        : sequence instanceof StringBuilder
          ? appendSafe((StringBuilder) sequence, 0, sequence.length())
          : append(sequence, 0, sequence.length());
  }

  public final CharacterBuffer append(CharSequence sequence, int start) {
    require(requireNonNull(sequence), checkCharSequenceRange(start), ofIOOB(start));
    return sequence instanceof String
        ? appendSafe((String) sequence, start, sequence.length())
        : sequence instanceof StringBuilder
          ? appendSafe((StringBuilder) sequence, start, sequence.length())
          : append(sequence, start, sequence.length());
  }

  public final CharacterBuffer append(CharSequence sequence, int start, int end) {
    require(requireNonNull(sequence), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return sequence instanceof String
        ? appendSafe((String) sequence, start, end)
        : sequence instanceof StringBuilder
          ? appendSafe((StringBuilder) sequence, start, end)
          : append(sequence, start, end);
  }

  // Cleanup operations

  public final void clear() {
    length = 0;
  }

  public final void reset() {
    final int nslots = (length - 1) / depth + 1;
    for (int index = 0; index < nslots; index++) {
      buffer[index] = null;
    }
    clear();
  }

  // Internal operations

  protected final CharacterBuffer appendSafe(char[] values, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        System.arraycopy(values, start, nextSlot(), offset, remainder);
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

  protected final CharacterBuffer appendSafe(String string, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the string characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        string.getChars(start, offset + remainder, nextSlot(), offset);
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

  protected final CharacterBuffer appendSafe(StringBuilder buffer, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the string characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        buffer.getChars(start, offset + remainder, nextSlot(), offset);
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

  protected final CharacterBuffer appendSafe(CharSequence sequence, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the characters to the current slot
        int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        final int limit = offset + remainder;
        final char[] slot = nextSlot();
        while (offset < limit) {
          slot[offset++] = sequence.charAt(start++);
        }
        length += remainder;
        count -= remainder;
      }
      if (start < end) {
        // Not all the characters have been appended
        throw new ThresholdReachedException(this);
      }
    }
    return this;
  }

  protected final int ensureCapacity(int count) {
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

}
