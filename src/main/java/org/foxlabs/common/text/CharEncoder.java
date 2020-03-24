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

/**
 * A character encoder that converts a given single character (Unicode code point) to a sequence of
 * characters (1:M mapping) and appends the encoded representation to the buffer.
 *
 * @author Fox Mulder
 * @see CharBuffer
 */
@FunctionalInterface
public interface CharEncoder {

  /**
   * Appends an encoded representation of the {@code ch} character (Unicode code point) to the
   * {@code buffer} and returns a number of characters ({@code char}) appended.
   */
  int encode(int ch, CharBuffer buffer);

  /**
   * An identity character encoder that does not apply any conversion to characters and appends
   * them to the buffer as is (1:1 mapping).
   */
  CharEncoder IDENTITY = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      buffer.append(ch);
      return Character.charCount(ch);
    }
  };

  /**
   * A character encoder that converts characters to uppercase according to the
   * {@link Character#toUpperCase(int)} method. Note that this encoder does not take locale into
   * account.
   */
  CharEncoder UPPERCASE = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      buffer.append(ch = Character.toUpperCase(ch));
      return Character.charCount(ch);
    }
  };

  /**
   * A character encoder that converts characters to lowercase according to the
   * {@link Character#toLowerCase(int)} method. Note that this encoder does not take locale into
   * account.
   */
  CharEncoder LOWERCASE = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      buffer.append(ch = Character.toLowerCase(ch));
      return Character.charCount(ch);
    }
  };

  /**
   * A character encoder that converts characters to titlecase according to the
   * {@link Character#toTitleCase(int)} method. Note that this encoder does not take locale into
   * account.
   */
  CharEncoder TITLECASE = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      buffer.append(ch = Character.toTitleCase(ch));
      return Character.charCount(ch);
    }
  };

  /**
   * A character encoder that converts characters to the corresponding Unicode hexadecimal
   * 16/32-bit representation using the {@link Character#isBmpCodePoint(int)} method to determine
   * if the specified character code point is in the Basic Multilingual Plane (BMP) or it is a
   * supplementary character. The format of BMP characters is <code>&#92;uXXXX</code>. The format
   * of supplementary characters is <code>&#92;uHHHH&#92;uLLLL</code>, where {@code HHHH} is high
   * surrogate part extracted using the {@link Character#highSurrogate(int)} method and
   * {@code LLLL} is low surrogate part extracted using the {@link Character#lowSurrogate(int)}
   * method. The format is compatible with Java language. Note that this encoder does not validate
   * the specified character to be a valid Unicode code point.
   */
  CharEncoder UCODE = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      if (Character.isBmpCodePoint(ch)) {
        buffer.ensureCapacity(6);
        buffer.append('\\').append('u').appendHex((short) ch);
        return 6;
      } else {
        buffer.ensureCapacity(12);
        buffer.append('\\').append('u').appendHex((short) Character.highSurrogate(ch));
        buffer.append('\\').append('u').appendHex((short) Character.lowSurrogate(ch));
        return 12;
      }
    }
  };

  /**
   * The Java character encoder that adds {@code '\\'} escape character for the {@code '\\'},
   * {@code '\''}, {@code '\"'}, {@code '\n'}, {@code '\r'}, {@code '\t'}, {@code '\b'} and
   * {@code '\f'} characters. All the {@link Character#isISOControl(int)} characters will be
   * converted to the {@link #UCODE} format as well. Other characters will be appended to the
   * buffer as is.
   */
  CharEncoder JAVA = new CharEncoder() {
    @Override public int encode(int ch, CharBuffer buffer) {
      switch (ch) {
      case '\\':
        buffer.append('\\').append('\\');
        return 2;
      case '\'':
        buffer.append('\\').append('\'');
        return 2;
      case '\"':
        buffer.append('\\').append('\"');
        return 2;
      case '\n':
        buffer.append('\\').append('n');
        return 2;
      case '\r':
        buffer.append('\\').append('r');
        return 2;
      case '\t':
        buffer.append('\\').append('t');
        return 2;
      case '\b':
        buffer.append('\\').append('b');
        return 2;
      case '\f':
        buffer.append('\\').append('f');
        return 2;
      default:
        return Character.isISOControl(ch) ? UCODE.encode(ch, buffer) : IDENTITY.encode(ch, buffer);
      }
    }
  };

}
