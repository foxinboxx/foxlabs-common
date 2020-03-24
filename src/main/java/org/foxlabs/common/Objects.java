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

import java.util.function.Supplier;

import org.foxlabs.common.text.CharBuffer;
import org.foxlabs.common.text.SimpleCharBuffer;
import org.foxlabs.common.exception.ThresholdReachedException;

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
   * Converts the specified object to a string representation with a length that will never exceed
   * {@link CharBuffer#LOG_THRESHOLD}. This method is designed primarily for logging purposes.
   *
   * @param object The reference to an object to be converted to a string representation.
   * @return A string representation of the specified object.
   * @see #toString(Object, int)
   */
  public static String toString(Object object) {
    return toString(object, CharBuffer.LOG_THRESHOLD);
  }

  /**
   * Converts the specified object to a string representation with a length that will never exceed
   * the specified threshold. This method is designed primarily for logging purposes.
   *
   * @param object The reference to an object to be converted to a string representation.
   * @param threshold The maximum length that the resulting string will never exceed.
   * @return A string representation of the specified object.
   * @see CharBuffer#appendObject(Object)
   */
  public static String toString(Object object, int threshold) {
    try {
      return new SimpleCharBuffer(threshold).appendObject(object).toString();
    } catch (ThresholdReachedException e) {
      // threshold has been reached, return partial result anyway
      return e.getProducer().toString();
    }
  }

  /**
   * Returns a new object with the overridden {@link Object#toString()} method that uses the
   * specified formatter to generate the resulting string. This method is useful for lazy message
   * construction.
   *
   * @param formatter The {@code Object.toString()} result formatter.
   * @return A new object instance with the overridden {@code Object.toString()} method.
   * @throws NullPointerException if the specified formatter is {@code null}.
   */
  public static Object message(Supplier<String> formatter) {
    Checks.checkNotNull(formatter);
    return new Object() {
      @Override public String toString() {
        return formatter.get();
      }
    };
  }

}
