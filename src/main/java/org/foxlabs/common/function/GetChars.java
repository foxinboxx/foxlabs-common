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
public interface GetChars {

  void getChars(int from, int to, char[] target, int index);

  static GetChars of(char... source) {
    return (from, to, target, index) -> System.arraycopy(source, from, target, index, to - from);
  }

  static GetChars of(String source) {
    return (from, to, target, index) -> source.getChars(from, to, target, index);
  }

  static GetChars of(StringBuilder source) {
    return (from, to, target, index) -> source.getChars(from, to, target, index);
  }

  static GetChars of(StringBuffer source) {
    return (from, to, target, index) -> source.getChars(from, to, target, index);
  }

  static GetChars of(CharSequence source) {
    // check known char sequences
    if (source instanceof GetChars) {
      return (GetChars) source;
    }
    if (source instanceof String) {
      return of((String) source);
    }
    if (source instanceof StringBuilder) {
      return of((StringBuilder) source);
    }
    if (source instanceof StringBuffer) {
      return of((StringBuffer) source);
    }
    // unknown char sequence, copy char by char
    return (from, to, target, index) -> {
      while (from < to) target[index++] = source.charAt(from++);
    };
  }

}
