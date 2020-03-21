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
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface GetChars {

  void getChars(int start, int end, char[] target, int index);

  static GetChars from(char... source) {
    return (start, end, target, index) -> System.arraycopy(source, start, target, index, end - start);
  }

  static GetChars from(String source) {
    return (start, end, target, index) -> source.getChars(start, end, target, index);
  }

  static GetChars from(StringBuilder source) {
    return (start, end, target, index) -> source.getChars(start, end, target, index);
  }

  static GetChars from(StringBuffer source) {
    return (start, end, target, index) -> source.getChars(start, end, target, index);
  }

  static GetChars from(CharSequence source) {
    // check known char sequences
    if (source instanceof GetChars) {
      return (GetChars) source;
    }
    if (source instanceof String) {
      return from((String) source);
    }
    if (source instanceof StringBuilder) {
      return from((StringBuilder) source);
    }
    if (source instanceof StringBuffer) {
      return from((StringBuffer) source);
    }
    // unknown char sequence, copy char by char
    return (start, end, target, index) -> {
      while (start < end) target[index++] = source.charAt(start++);
    };
  }

}
