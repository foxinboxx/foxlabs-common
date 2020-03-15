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

/**
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface CharacterSequence extends CharSequence {

  @Override
  default int length() {
    throw new UnsupportedOperationException();
  }

  @Override
  default char charAt(int index) {
    throw new UnsupportedOperationException();
  }

  @Override
  default CharacterSequence subSequence(int start, int end) {
    throw new UnsupportedOperationException();
  }

  void copy(int from, int to, char[] target, int offset);

  static CharacterSequence of(char... source) {
    return (from, to, target, offset) -> System.arraycopy(source, from, target, offset, to - from);
  }

  static CharacterSequence of(String source) {
    return (from, to, target, offset) -> source.getChars(from, to, target, offset);
  }

  static CharacterSequence of(StringBuilder source) {
    return (from, to, target, offset) -> source.getChars(from, to, target, offset);
  }

  static CharacterSequence of(StringBuffer source) {
    return (from, to, target, offset) -> source.getChars(from, to, target, offset);
  }

  static CharacterSequence of(CharSequence source) {
    if (source instanceof CharacterSequence) {
      return (CharacterSequence) source;
    } else if (source instanceof String) {
      return of((String) source);
    } else if (source instanceof StringBuilder) {
      return of((StringBuilder) source);
    } else if (source instanceof StringBuffer) {
      return of((StringBuffer) source);
    } else { // Unknown character sequence, copy char by char
      return (from, to, target, offset) -> {
        for (int index = from; index < to; index++) {
          target[offset++] = source.charAt(index);
        }
      };
    }
  }

}
