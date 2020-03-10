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

package org.foxlabs.common;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * The commonly used operations on objects.
 *
 * @author Fox Mulder
 */
public final class Objects {

  // Instantiation is not possible
  private Objects() {
    throw new IllegalAccessError();
  }

  // toString

  /**
   * The maximum default length that may produce the {@link #toString(Object)} method.
   */
  public static final int DEFAULT_TO_STRING_MAX_LENGTH = 1000;

  /**
   * Converts the specified object to a string representation.
   *
   * <p>
   * This is a shortcut for the:
   * <pre>
   * toString(object, DEFAULT_TO_STRING_MAX_LENGTH)
   * </pre>
   * </p>
   *
   * @param object The reference to an object to be converted to a string representation.
   * @return A string representation of the specified object.
   * @see #toString(Object, int)
   */
  public static String toString(Object object) {
    return toString(object, DEFAULT_TO_STRING_MAX_LENGTH);
  }

  /**
   * Converts the specified object to a string representation.
   *
   * <p>
   * The algorithm is:
   * <ul>
   *   <li>If the specified object reference is {@code null} then return {@code "null"}.</li>
   *   <li>If the specified object reference is of {@code String} type then return
   *       {@link Strings#escape(String)}.</li>
   *   <li>If the specified object reference is of array type then return corresponding
   *       {@code Arrays.toString()}.</li>
   *   <li>If the specified object reference is of {@code Object} array type then return
   *       {@link Arrays#deepToString(Object[])}.</li>
   *   <li>Return {@code object.toString()} otherwise.</li>
   * </ul>
   * </p>
   *
   * @param object The reference to an object to be converted to a string representation.
   * @return A string representation of the specified object.
   */
  // FIXME Need totally own implementation because Arrays.toString() does not take into account
  // special case for CharSequence
  public static String toString(Object object, int limit) {
    if (object == null) {
      return "null";
    } else if (object instanceof String) {
      return Strings.escape((String) object);
    } else if (object.getClass().isArray()) {
      if (object instanceof boolean[]) {
        return Arrays.toString((boolean[]) object);
      } else if (object instanceof char[]) {
        return Arrays.toString((char[]) object);
      } else if (object instanceof byte[]) {
        return Arrays.toString((byte[]) object);
      } else if (object instanceof short[]) {
        return Arrays.toString((short[]) object);
      } else if (object instanceof int[]) {
        return Arrays.toString((int[]) object);
      } else if (object instanceof long[]) {
        return Arrays.toString((long[]) object);
      } else if (object instanceof float[]) {
        return Arrays.toString((float[]) object);
      } else if (object instanceof double[]) {
        return Arrays.toString((double[]) object);
      } else {
        return Arrays.deepToString((Object[]) object);
      }
    }
    return object.toString();
  }

  public static StringBuilder toString(boolean[] array, StringBuilder buffer) {
    if (array == null) {
      return buffer.append("null");
    }
    buffer.append('[');
    if (array.length > 0) {
      for (int index = 1; index < array.length; index++) {

      }
    }
    return buffer.append(']');
  }

  // Miscellaneous

  /**
   * Casts the specified object reference to a custom target type. This method just provides a way
   * to avoid declaration of the {@link SuppressWarnings} annotation.
   *
   * @param <T> The target reference type.
   * @param object The object reference to cast.
   * @return The specified object reference casted to the target type.
   * @throws ClassCastException if cast operation failed.
   */
  @SuppressWarnings("unchecked")
  public static <T> T cast(Object object) {
    return (T) object;
  }

  /**
   * Returns a new object with the overridden {@link Object#toString()} method that uses the
   * specified formatter to generate the resulting string. This method is useful for lazy message
   * construction and might be used as the {@code message} argument of the {@link Predicates}
   * methods.
   *
   * <p>
   * For example, instead of:
   * <pre>
   * Predicates.requireNonNull(object, java.time.LocalDateTime.now() + ": object is null");
   * </pre>
   *
   * You may use:
   * <pre>
   * Predicates.requireNonNull(object,
   *     message(() -> java.time.LocalDateTime.now() + ": object is null"));
   * </pre>
   * </p>
   *
   * @param formatter The {@code Object.toString()} result formatter.
   * @return A new object instance with the overridden {@code Object.toString()} method.
   */
  public static Object message(Supplier<String> formatter) {
    return new Object() {
      @Override public String toString() {
        return formatter.get();
      }
    };
  }

}
