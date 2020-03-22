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
 * representations of other objects or primitives. Although it is defined as the functional
 * interface, it is better to avoid such usage.
 *
 * <p>
 * For example:
 * <blockquote><pre>
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
 * </pre></blockquote>
 *
 * May be implemented as:
 * <blockquote><pre>
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
 *     // Short form of the:
 *     // buffer.append("Line(");
 *     // p1.toString(buffer);
 *     // buffer.append(", ");
 *     // p2.toString(buffer);
 *     // buffer.append(")");
 *     return p2.toString(p1.toString(buffer.append("Line(")).append(", ")).append(")");
 *   }
 * }
 * </pre></blockquote>
 * Thus, there are no new {@code String} instances were created at all.
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
   * method that calls the {@link #toString(CharBuffer)} method with a new empty buffer and returns
   * the resulting string (i.e. {@code toString(new SimpleCharBuffer()).toString()}). In case of
   * buffer overflow partial result will be returned and no exception will be thrown.
   *
   * @author Fox Mulder
   */
  public static abstract class Adapter implements ToString {
    @Override public String toString() {
      try {
        return toString(new SimpleCharBuffer()).toString();
      } catch (ThresholdReachedException e) {
        // threshold has been reached, return partial result anyway
        return e.getProducer().toString();
      }
    }
  }

  // Overrider

  /**
   * An interface that allows to redefine the {@link Object#toString()} method for specific cases
   * or for legacy classes that do not implement {@code ToString} interface. It is primarily
   * designed for use by the {@link CharBuffer#appendObject(Object)} method.
   *
   * @param <T> The type which {@code toString()} method to be redefined.
   *
   * @author Fox Mulder
   */
  @FunctionalInterface
  public static interface Overrider<T> {

    /**
     * Appends a string representation of the specified object to the specified buffer and returns
     * a reference to the same buffer.
     *
     * @param object The object which string representation to append to the buffer.
     * @param buffer The buffer to append to.
     * @return A reference to the specified buffer.
     */
    CharBuffer toString(T object, CharBuffer buffer);

    /**
     * Appends string representation of the specified {@link java.net.URI} to the specified buffer
     * replacing password field with {@code ***} characters from the user info part if any.
     */
    Overrider<java.net.URI> URI = new Overrider<java.net.URI>() {
      @Override public CharBuffer toString(java.net.URI uri, CharBuffer buffer) {
        // the algorithm is taken from the URI.defineString() private method
        if (uri.getScheme() != null) {
          buffer.append(uri.getScheme()).append(':');
        }
        if (uri.isOpaque()) {
          buffer.append(uri.getSchemeSpecificPart());
        } else {
          final String host = uri.getHost();
          if (host != null) {
            buffer.append('/').append('/');
            final String userInfo = uri.getUserInfo();
            if (userInfo != null) {
              final int index = userInfo.indexOf(':');
              if (index < 0) {
                buffer.append(userInfo);
              } else {
                if (index > 0) {
                  buffer.append(userInfo.substring(0, index));
                }
                buffer.append(":***");
              }
              buffer.append('@');
            }
            boolean needBrackets = host.indexOf(':') >= 0
                && !host.startsWith("[")
                && !host.endsWith("]");
            if (needBrackets) {
              buffer.append('[');
            }
            buffer.append(host);
            if (needBrackets) {
              buffer.append(']');
            }
            if (uri.getPort() != -1) {
              buffer.append(':').appendDec(uri.getPort());
            }
          } else if (uri.getAuthority() != null) {
            buffer.append('/').append('/').append(uri.getAuthority());
          }
          if (uri.getPath() != null) {
            buffer.append(uri.getPath());
          }
          if (uri.getQuery() != null) {
            buffer.append('?').append(uri.getQuery());
          }
        }
        if (uri.getFragment() != null) {
          buffer.append('#').append(uri.getFragment());
        }
        return buffer;
      }
    };

  }

}
