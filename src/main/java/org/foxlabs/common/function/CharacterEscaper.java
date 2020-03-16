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
import org.foxlabs.common.text.CharacterBuffer;

@FunctionalInterface
public interface CharacterEscaper {

  void escape(int ch, CharacterBuffer buffer);

  default void escape(CharSequence sequence, CharacterBuffer buffer) {
    Predicates.requireNonNull(buffer);
    final int length = sequence == null ? 0 : sequence.length();
    for (int index = 0; index < length; index++) {
      escape(sequence.charAt(index), buffer);
    }
  }

  CharacterEscaper JAVA_ESCAPER = (ch, buffer) -> {
    switch (ch) {
    case '\\':
      buffer.append('\\').append('\\');
      break;
    case '\'':
      buffer.append('\\').append('\'');
      break;
    case '\"':
      buffer.append('\\').append('\"');
      break;
    case '\n':
      buffer.append('\\').append('n');
      break;
    case '\r':
      buffer.append('\\').append('r');
      break;
    case '\t':
      buffer.append('\\').append('t');
      break;
    case '\b':
      buffer.append('\\').append('b');
      break;
    case '\f':
      buffer.append('\\').append('f');
      break;
    default:
      if (Character.isISOControl(ch)) {
        final char[] ucode = new char[] { '\\', 'u', '0', '0', '0', '0' };
        for (int i = 3; i >= 0; i--) {
          final int x = (ch >> i * 4) & 0x0f;
          ucode[5 - i] = (char) (x + (x > 0x09 ? 0x57 : 0x30));
        }
        buffer.append(ucode);
      } else {
        buffer.append((char) ch);
      }
      break;
    }
  };

}
