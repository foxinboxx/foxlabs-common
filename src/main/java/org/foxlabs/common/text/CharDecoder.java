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

public interface CharDecoder {

  int decode(CharSegment segment, int index, CharBuffer buffer);

  /**
   * An identity character decoder that does not apply any conversion to characters and appends
   * them to the buffer as is (1:1 mapping).
   */
  CharDecoder IDENTITY = new CharDecoder() {
    @Override public int decode(CharSegment segment, int index, CharBuffer buffer) {
      buffer.append(segment.charAt(index));
      return 1;
    }
  };

  /**
   * A character decoder that converts characters (Unicode code points) to uppercase according to
   * the {@link Character#toUpperCase(int)} method. Note that this decoder does not take locale
   * into account.
   */
  CharDecoder UPPERCASE = new CharDecoder() {
    @Override public int decode(CharSegment segment, int index, CharBuffer buffer) {
      final int cp = segment.codePointAt(index);
      buffer.append(Character.toUpperCase(cp));
      return Character.charCount(cp);
    }
  };

  /**
   * A character decoder that converts characters (Unicode code points) to lowercase according to
   * the {@link Character#toLowerCase(int)} method. Note that this decoder does not take locale
   * into account.
   */
  CharDecoder LOWERCASE = new CharDecoder() {
    @Override public int decode(CharSegment segment, int index, CharBuffer buffer) {
      final int cp = segment.codePointAt(index);
      buffer.append(Character.toLowerCase(cp));
      return Character.charCount(cp);
    }
  };

  /**
   * A character decoder that converts characters (Unicode code points) to titlecase according to
   * the {@link Character#toTitleCase(int)} method. Note that this decoder does not take locale
   * into account.
   */
  CharDecoder TITLECASE = new CharDecoder() {
    @Override public int decode(CharSegment segment, int index, CharBuffer buffer) {
      final int cp = segment.codePointAt(index);
      buffer.append(Character.toTitleCase(cp));
      return Character.charCount(cp);
    }
  };

  /**
   * A character decoder that converts Unicode hexadecimal 16-bit character representation to
   * corresponding character ({@code char}).
   */
  CharDecoder UCODE = new CharDecoder() {
    @Override public int decode(CharSegment segment, int index, CharBuffer buffer) {
      char ch = segment.charAt(index++);
      if (ch == '\\') {
        if (index < segment.length() && segment.charAt(index++) == 'u') {
          if (index + 4 > segment.length()) {
            throw new IllegalArgumentException("Premature end of Unicode code unit");
          }
          int cp = 0;
          for (int n = 0; n < 4; n++) {
            ch = segment.charAt(index++);
            if (ch >= 0x30 && ch <= 0x39) { // [0-9]
              cp = cp << 4 | (ch - 0x30);
            } else if (ch >= 0x61 && ch <= 0x7a) { // [a-f]
              cp = cp << 4 | (ch - 0x57);
            } else if (ch >= 0x41 && ch <= 0x5a) { // [A-F]
              cp = cp << 4 | (ch - 0x37);
            } else {
              throw new IllegalArgumentException("Invalid Unicode code unit at " + index);
            }
          }
          if (!Character.isHighSurrogate((char) cp)) {
            buffer.append((char) cp);
            return 6;
          }

        }
      }
      buffer.append(ch);
      return 1;
    }
  };


}
