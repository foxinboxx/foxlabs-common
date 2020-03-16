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

import org.foxlabs.common.text.CharBuffer;
import org.foxlabs.common.exception.ThresholdReachedException;

/**
 * An interface that allows to build a long sequence of text data avoiding string concatenations
 * (i.e. {@code String + String} or {@link String#concat(String)}).
 *
 * <p>For example, it is efficient to build a resulting string from a collection of objects which
 * classes implement this interface.</p>
 *
 * So, instead of:
 * <pre>
 * final Collection&lt;?&gt; collection = ...
 * final StringBuilder buffer = new StringBuilder();
 * collection.forEach((element) -> buffer.append(element.toString()));
 * System.out.println(buffer);
 * </pre>
 *
 * You may use:
 * <pre>
 * final Collection&lt;? extends ToString&gt; collection = ...
 * final CharacterBuffer buffer = new CharacterBuffer();
 * // Each element does not create a new string like toString() method does,
 * // but appends string representation directly to the buffer
 * collection.forEach((element) -> element.toString(buffer));
 * System.out.println(buffer);
 * </pre>
 *
 * @author Fox Mulder
 * @see CharBuffer
 */
@FunctionalInterface
public interface ToString {

  /**
   * Appends some content to the specified buffer and returns a reference to the same buffer.
   *
   * @param buffer The buffer to append to.
   * @return A reference to the specified buffer.
   */
  CharBuffer toString(CharBuffer buffer);

  // Adapter

  /**
   * An abstract {@code ToString} implementation which overrides the {@link Object#toString()}
   * method that calls {@link #toString(CharBuffer)} with an empty buffer and returns the
   * resulting string (i.e. {@code toString(new CharacterBuffer()).toString()}).
   *
   * @author Fox Mulder
   */
  public static abstract class Adapter implements ToString {
    @Override public String toString() {
      try {
        return toString(new CharBuffer()).toString();
      } catch (ThresholdReachedException e) {
        return e.getProducer().toString();
      }
    }
  }

}
