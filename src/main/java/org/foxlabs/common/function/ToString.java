/*
 * Copyright (C) 2016 FoxLabs
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
 * An interface that allows to build a long sequence of text data avoiding string concatenations
 * (i.e. {@code String + String} or {@link String#concat(String)}).
 *
 * <p>
 * For example, it is efficient to build a resulting string from a collection of objects which
 * classes implement this interface.
 * </p>
 *
 * So, instead of:
 * <pre>
 * final Collection&lt;?&gt; collection = ...
 * final StringBuilder buffer = new StringBuilder();
 * collection.forEach((element) -> buffer.append(element.toString()));
 * </pre>
 *
 * You may use:
 * <pre>
 * final Collection&lt;? extends ToString&gt; collection = ...
 * final StringBuilder buffer = new StringBuilder();
 * // Each element does not build a new string like toString() method does,
 * // but appends it directly to the buffer
 * collection.forEach((element) -> element.toString(buffer));
 * </pre>
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface ToString {

  /**
   * Appends some text data to the specified buffer and returns a reference to the same buffer.
   *
   * @param buffer The buffer to append to.
   * @return A reference to the specified buffer.
   */
  StringBuilder toString(StringBuilder buffer);

  // ToString.Adapter

  /**
   * An abstract {@link ToString} implementation which overrides the {@link Object#toString()}
   * method that calls {@link #toString(StringBuilder)} with an empty {@link StringBuilder} and
   * returns the resulting string.
   *
   * @author Fox Mulder
   */
  abstract class Adapter implements ToString {
    @Override public String toString() {
      return toString(new StringBuilder()).toString();
    }
  }

}
