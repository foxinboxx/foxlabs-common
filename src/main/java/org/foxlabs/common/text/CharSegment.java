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
import org.foxlabs.common.Strings;

/**
 * A segment of characters with read-only random access.
 *
 * <p>In addition to the {@link CharSequence} interface, this interface defines a
 * {@link #copyTo(int, int, char[], int)} method, which has the same semantics as the
 * {@link String#getChars(int, int, char[], int)} method and allows to avoid copying characters in
 * a loop. This interface also defines a number of {@code static} methods that create
 * {@code CharSegment}s from {@code char[]} arrays and from the well-known {@code String} and
 * {@code StringBuilder} classes.</p>
 *
 * <p>
 * One of the main goals of this interface is to reduce a number of overloaded methods in
 * classes that operate on different character sequences and ranges within. For example:
 * <blockquote><pre>
 * public class CharConcatenator {
 *   public void concat(char[] array) {
 *     Checks.checkNotNull(array);
 *     // ...
 *   }
 *   public void concat(char[] array, int start) {
 *     Checks.checkRange(array, start);
 *     // ...
 *   }
 *   public void concat(char[] array, int start, int end) {
 *     Checks.checkRange(array, start, end);
 *     // ...
 *   }
 *   public void concat(CharSequence sequence) {
 *     Checks.checkNotNull(sequence);
 *     // ...
 *   }
 *   public void concat(CharSequence sequence, int start) {
 *     Checks.checkRange(sequence, start);
 *     // ...
 *   }
 *   public void concat(CharSequence sequence, int start, int end) {
 *     Checks.checkRange(sequence, start, end);
 *     // ...
 *   }
 * }
 * </pre></blockquote>
 *
 * Can be replaced with:
 * <blockquote><pre>
 * public class CharConcatenator {
 *   public void concat(CharSegment segment) {
 *     Checks.checkNotNull(segment);
 *     // ...
 *   }
 * }
 * </pre></blockquote>
 * Moreover, there is no need to validate character ranges anymore since it is done already.
 * </p>
 *
 * @author Fox Mulder
 */
public interface CharSegment extends CharSequence {

  /**
   * Returns a new {@code CharSegment} that is a subsegment of this {@code CharSegment} in the
   * specified range {@code [start, end)}.
   *
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   */
  @Override
  CharSegment subSequence(int start, int end);

  /**
   * Copies characters in the specified range {@code [start, end)} from this {@code CharSegment}
   * contents into the specified {@code target} array starting from the specified {@code offset}.
   *
   * @throws NullPointerException if the specified {@code target} array reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid or the specified
   *         {@code target} array does not have sufficient capacity to receive that range of
   *         characters.
   */
  void copyTo(int start, int end, char[] target, int offset);

  /**
   * An empty character source with the following behavior:
   * <ul>
   *   <li>{@link #length()} returns {@code 0}.</li>
   *   <li>{@link #charAt(int)} throws {@link IndexOutOfBoundsException}.</li>
   *   <li>{@link #subSequence(int, int)} throws {@link IndexOutOfBoundsException} if
   *       {@code start != 0 || end != 0}. Otherwise, returns a reference to itself.</li>
   *   <li>{@link #copyTo(int, int, char[], int)} throws {@link NullPointerException} if
   *       {@code target == null}. Throws {@link IndexOutOfBoundsException} if
   *       {@code start != 0 || end != 0 || offset < 0 || offset > target.length}.</li>
   * </ul>
   */
  CharSegment EMPTY = new CharSegment() {
    @Override public int length() {
      return 0;
    }
    @Override public char charAt(int index) {
      return Checks.checkIndex(this, index).charAt(index);
    }
    @Override public CharSegment subSequence(int start, int end) {
      return Checks.checkRange(this, start, end);
    }
    @Override public void copyTo(int start, int end, char[] target, int offset) {
      Checks.checkRange(this, start, end);
      Checks.checkIndex(target, offset);
    }
    @Override public String toString() {
      return Strings.EMPTY;
    }
  };

  // char[]

  /**
   * Creates a new {@code CharSegment} for the specified {@code array} or returns {@link #EMPTY} if
   * the specified {@code array} is empty.
   *
   * @throws NullPointerException if the specified {@code char[]} reference is {@code null}.
   * @see #from(char[], int, int)
   */
  static CharSegment from(char[] array) {
    return from(array, 0, array.length);
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, array.length]} (inclusive) in
   * the specified {@code array} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code char[]} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   * @see #from(char[], int, int)
   */
  static CharSegment from(char[] array, int from) {
    return from(array, from, array.length);
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, to]} (inclusive) in the
   * specified {@code array} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code char[]} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   */
  static CharSegment from(char[] array, int from, int to) {
    Checks.checkRange(array, from, to);
    final int length = to - from;
    return length == 0 ? EMPTY : new CharSegment() {
      @Override public int length() {
        return length;
      }
      @Override public char charAt(int index) {
        Checks.checkIndex(this, index);
        return array[from + index];
      }
      @Override public CharSegment subSequence(int start, int end) {
        Checks.checkRange(this, start, end);
        return start > 0 || end < length ? from(array, from + start, from + end) : this;
      }
      @Override public void copyTo(int start, int end, char[] target, int offset) {
        Checks.checkRange(this, start, end);
        Checks.checkRange(target, offset, offset + end - start);
        System.arraycopy(array, from + start, target, offset, end - start);
      }
      @Override public String toString() {
        return new String(array, from, length);
      }
    };
  }

  // String

  /**
   * Creates a new {@code CharSegment} for the specified {@code string} or returns {@link #EMPTY}
   * if the specified {@code string} is empty.
   *
   * @throws NullPointerException if the specified {@code String} reference is {@code null}.
   * @see #from(String, int, int)
   */
  static CharSegment from(final String string) {
    return from(string, 0, string.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, string.length()]} (inclusive) in
   * the specified {@code string} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code String} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   * @see #from(String, int, int)
   */
  static CharSegment from(final String string, int from) {
    return from(string, from, string.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, to]} (inclusive) in the
   * specified {@code string} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code String} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   */
  static CharSegment from(final String string, int from, int to) {
    Checks.checkRange(string, from, to);
    final int length = to - from;
    return length == 0 ? EMPTY : new CharSegment() {
      @Override public int length() {
        return length;
      }
      @Override public char charAt(int index) {
        Checks.checkIndex(this, index);
        return string.charAt(from + index);
      }
      @Override public CharSegment subSequence(int start, int end) {
        Checks.checkRange(this, start, end);
        return start > 0 || end < length ? from(string, from + start, from + end) : this;
      }
      @Override public void copyTo(int start, int end, char[] target, int offset) {
        Checks.checkRange(this, start, end);
        Checks.checkRange(target, offset, offset + end - start);
        string.getChars(from + start, from + end, target, offset);
      }
      @Override public String toString() {
        return string.substring(from, from + length);
      }
    };
  }

  // StringBuilder

  /**
   * Creates a new {@code CharSegment} for the specified {@code builder} or returns {@link #EMPTY}
   * if the specified {@code builder} is empty.
   *
   * @throws NullPointerException if the specified {@code StringBuilder} reference is {@code null}.
   * @see #from(StringBuilder, int, int)
   */
  static CharSegment from(final StringBuilder builder) {
    return from(builder, 0, builder.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, builder.length()]} (inclusive)
   * in the specified {@code builder} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code StringBuilder} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   * @see #from(StringBuilder, int, int)
   */
  static CharSegment from(final StringBuilder builder, int from) {
    return from(builder, from, builder.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, to]} (inclusive) in the
   * specified {@code builder} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code StringBuilder} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   */
  static CharSegment from(final StringBuilder builder, int from, int to) {
    Checks.checkRange(builder, from, to);
    final int length = to - from;
    return length == 0 ? EMPTY : new CharSegment() {
      @Override public int length() {
        return length;
      }
      @Override public char charAt(int index) {
        Checks.checkIndex(this, index);
        return builder.charAt(from + index);
      }
      @Override public CharSegment subSequence(int start, int end) {
        Checks.checkRange(this, start, end);
        return start > 0 || end < length ? from(builder, from + start, from + end) : this;
      }
      @Override public void copyTo(int start, int end, char[] target, int offset) {
        Checks.checkRange(this, start, end);
        Checks.checkRange(target, offset, offset + end - start);
        builder.getChars(from + start, from + end, target, offset);
      }
      @Override public String toString() {
        return builder.substring(from, from + length);
      }
    };
  }

  // CharSequence

  /**
   * Creates a new {@code CharSegment} for the specified {@code sequence} or returns {@link #EMPTY}
   * if the specified {@code sequence} is empty.
   *
   * @throws NullPointerException if the specified {@code CharSequence} reference is {@code null}.
   * @see #from(CharSequence, int, int)
   */
  static CharSegment from(final CharSequence sequence) {
    return from(sequence, 0, sequence.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, sequence.length()]} (inclusive)
   * in the specified {@code sequence} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code CharSequence} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   * @see #from(CharSequence, int, int)
   */
  static CharSegment from(final CharSequence sequence, int from) {
    return from(sequence, from, sequence.length());
  }

  /**
   * Creates a new {@code CharSegment} for the range {@code [from, to]} (inclusive) in the
   * specified {@code sequence} or returns {@link #EMPTY} if range is empty.
   *
   * @throws NullPointerException if the specified {@code CharSequence} reference is {@code null}.
   * @throws IndexOutOfBoundsException if the specified range is invalid.
   */
  static CharSegment from(final CharSequence sequence, int from, int to) {
    // check sequence type first
    if (sequence instanceof CharSegment) {
      return ((CharSegment) sequence).subSequence(from, to);
    }
    if (sequence instanceof String) {
      return from((String) sequence, from, to);
    }
    if (sequence instanceof StringBuilder) {
      return from((StringBuilder) sequence, from, to);
    }
    // no luck
    Checks.checkRange(sequence, from, to);
    final int length = to - from;
    return length == 0 ? EMPTY : new CharSegment() {
      @Override public int length() {
        return length;
      }
      @Override public char charAt(int index) {
        Checks.checkIndex(this, index);
        return sequence.charAt(from + index);
      }
      @Override public CharSegment subSequence(int start, int end) {
        return start > 0 || end < length ? from(sequence, from + start, from + end) : this;
      }
      @Override public void copyTo(int start, int end, char[] target, int offset) {
        Checks.checkRange(this, start, end);
        Checks.checkRange(target, offset, offset + end - start);
        while (start < end) {
          target[offset++] = sequence.charAt(start++);
        }
      }
      @Override public String toString() {
        return sequence.subSequence(from, from + length).toString();
      }
    };
  }

}
