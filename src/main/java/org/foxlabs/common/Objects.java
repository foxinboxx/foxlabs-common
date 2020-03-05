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

public final class Objects {

  // Instantiation is not possible
  private Objects() {
    throw new IllegalAccessError();
  }

  // Validations

  public static <T> T require(T object) {
    return require(object, java.util.Objects::nonNull, NullPointerException::new);
  }

  public static <T> T require(T object, Predicate<T> condition) {
    return require(object, condition, IllegalArgumentException::new);
  }

  public static <T, E extends Throwable> T require(T object, Predicate<T> condition,
      Supplier<? extends E> exception) throws E {
    if (condition.test(object)) {
      return object;
    } else {
      throw exception.get();
    }
  }

  // Miscellaneous

  /**
   * Returns a new object with the overridden {@link Object#toString()} method
   * that uses the specified formatter to generate the resulting string. This
   * method is useful for lazy message construction (e.g. in Log4j loggers).
   *
   * <p>
   * For example, instead of this:
   * <pre>
   * if (log.isDebugEnabled()) {
   *   log.debug("System properties:\n" + System.getProperties());
   * }
   * </pre>
   *
   * You may use this:
   * <pre>
   * log.debug(message(() -> "System properties:\n" + System.getProperties()));
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
