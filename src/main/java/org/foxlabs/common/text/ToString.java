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

package org.foxlabs.common.text;

import org.foxlabs.common.exception.ThresholdReachedException;

/**
 * An interface that allows to build a long sequence of text data avoiding string concatenations
 * with less memory consumption. It is designed primarily to replace the {@link Object#toString()}
 * method which is not ideal when the string representation of an object consists of string
 * representations of other objects or primitives.
 *
 * <p>
 * For example:
 * <pre>
 * public class Point {
 *   double x, y;
 *   &#64;Override public String toString() {
 *     return "Point(" + x + ", " + y + ")";
 *   }
 * }
 *
 * public class Line {
 *   Point p1, p2;
 *   &#64;Override public String toString() {
 *     return "Line(" + p1 + ", " + p2 + ")";
 *   }
 * }
 * </pre>
 *
 * May be implemented as:
 * <pre>
 * public class Point extends ToString.Adapter {
 *   double x, y;
 *   &#64;Override public CharBuffer toString(CharBuffer buffer) {
 *     return buffer.append("Point(").appendDec(x).append(", ").appendDec(y).append(")");
 *   }
 * }
 *
 * public class Line extends ToString.Adapter {
 *   Point p1, p2;
 *   &#64;Override public CharBuffer toString(CharBuffer buffer) {
 *     // Shortcut for the:
 *     // buffer.append("Line(");
 *     // p1.toString(buffer);
 *     // buffer.append(", ");
 *     // p2.toString(buffer);
 *     // buffer.append(")");
 *     return p2.toString(p1.toString(buffer.append("Line(")).append(", ")).append(")");
 *   }
 * }
 * </pre>
 * Thus, there are no {@code String} instances were created at all.
 * </p>
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
   * method that calls the {@link #toString(CharBuffer)} method with an empty buffer and returns
   * the resulting string (i.e. {@code toString(new CharacterBuffer()).toString()}). In case of
   * buffer overflow partial result will be returned and no exception will be thrown.
   *
   * @author Fox Mulder
   */
  public static abstract class Adapter implements ToString {
    @Override public String toString() {
      try {
        return toString(new CharBuffer()).toString();
      } catch (ThresholdReachedException e) {
        // threshold has been reached, return partial result anyway
        return e.getProducer().toString();
      }
    }
  }

}
