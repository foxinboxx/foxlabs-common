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

import org.foxlabs.common.Predicates;

@FunctionalInterface
public interface CharEncoder {

  CharBuffer encode(int ch, CharBuffer buffer);

  default CharBuffer encode(CharSequence sequence, CharBuffer buffer) {
    Predicates.requireNonNull(buffer);
    final int length = sequence == null ? 0 : sequence.length();
    for (int index = 0; index < length; index++) {
      encode(sequence.charAt(index), buffer);
    }
    return buffer;
  }

  /**
   * A dummy character encoder that does not apply any conversion to characters and appends them to
   * the buffer as is.
   */
  CharEncoder DUMMY = (ch, buffer) -> buffer.append(ch);

  /**
   * A character encoder that converts characters to uppercase according to the
   * {@link Character#toUpperCase(int)} method. Note that this encoder does not take locale into
   * account.
   */
  CharEncoder UPPERCASE = (ch, buffer) -> {
    return buffer.append(Character.toUpperCase(ch));
  };

  /**
   * A character encoder that converts characters to lowercase according to the
   * {@link Character#toLowerCase(int)} method. Note that this encoder does not take locale into
   * account.
   */
  CharEncoder LOWERCASE = (ch, buffer) -> {
    return buffer.append(Character.toLowerCase(ch));
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
  CharEncoder UCODE = (ch, buffer) -> {
    if (Character.isBmpCodePoint(ch)) {
      buffer.ensureCapacity(6);
      return buffer.append('\\').append('u').appendHex((short) ch);
    }
    buffer.ensureCapacity(11);
    return buffer.append('\\').append('u').appendHex((short) Character.highSurrogate(ch))
        .append('\\').append('u').appendHex((short) Character.lowSurrogate(ch));
  };

  /**
   * The Java character encoder that adds {@code '\\'} escape character for the {@code '\\'},
   * {@code '\''}, {@code '\"'}, {@code '\n'}, {@code '\r'}, {@code '\t'}, {@code '\b'} and
   * {@code '\f'} characters. All the {@link Character#isISOControl(int) ISO Control} characters
   * will be converted to the {@link #UCODE} format as well.
   */
  CharEncoder JAVA = (ch, buffer) -> {
    switch (ch) {
    case '\\':
      return buffer.append('\\').append('\\');
    case '\'':
      return buffer.append('\\').append('\'');
    case '\"':
      return buffer.append('\\').append('\"');
    case '\n':
      return buffer.append('\\').append('n');
    case '\r':
      return buffer.append('\\').append('r');
    case '\t':
      return buffer.append('\\').append('t');
    case '\b':
      return buffer.append('\\').append('b');
    case '\f':
      return buffer.append('\\').append('f');
    default:
      return Character.isISOControl(ch) ? UCODE.encode(ch, buffer) : buffer.append(ch);
    }
  };

}
