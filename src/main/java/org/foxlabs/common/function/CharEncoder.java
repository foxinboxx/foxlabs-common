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

package org.foxlabs.common.function;

import org.foxlabs.common.Predicates;
import org.foxlabs.common.text.CharBuffer;

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
   * A character encoder that converts characters to the corresponding Unicode hexadecimal unsigned
   * 32/16-bit representation. The format is <code>&#92;uXXXX</code> for BMP characters and
   * <code>&#92;uXXXXuXXXX</code> for supplementary characters, leading zeros are preserved.
   */
  CharEncoder UCODE = (ch, buffer) -> {
    final int high = ch >>> 16;
    buffer.ensureCapacity(high != 0 ? 11 : 6);
    buffer.append('\\').append('u');
    if (high != 0) {
      buffer.appendHex((short) (high)).append('u');
    }
    return buffer.appendHex((short) (ch & 0xffff));
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
