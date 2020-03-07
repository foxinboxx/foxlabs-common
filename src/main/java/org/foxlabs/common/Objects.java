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

import java.util.function.Predicate;
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

  // Miscellaneous

  /**
   * Casts the specified object reference to a custom target type. This method
   * just provides a way to avoid declaration of the {@link SuppressWarnings}
   * annotation.
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
   * Returns a new object with the overridden {@link Object#toString()} method
   * that uses the specified formatter to generate the resulting string. This
   * method is useful for lazy message construction and might be used as the
   * {@code message} argument of the {@link #requireNonNull(Object, Object)} and
   * {@link #require(Object, Predicate, Object)} methods.
   *
   * <p>
   * For example, instead of:
   * <pre>
   * Objects.require(object, LocalDateTime.now() + ": object is null");
   * </pre>
   *
   * You may use:
   * <pre>
   * Objects.require(object, Objects.message(() -> LocalDateTime.now() + ": object is null"));
   * </pre>
   * </p>
   *
   * @param formatter The {@link Object#toString()} result formatter.
   * @return A new object instance with the overridden {@link Object#toString()}
   *         method.
   */
  public static Object message(Supplier<String> formatter) {
    return new Object() {
      @Override public String toString() {
        return formatter.get();
      }
    };
  }

}
