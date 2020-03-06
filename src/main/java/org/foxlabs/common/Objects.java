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
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.DoublePredicate;
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

  // Object validations

  /**
   * Checks that the specified object reference is not {@code null} and throws
   * {@link NullPointerException} without detail message if it is.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @return The specified object reference.
   * @throws NullPointerException if the specified object reference is {@code null}.
   */
  public static <T> T requireNonNull(T object) {
    if (object == null) {
      throw new NullPointerException();
    }
    return object;
  }

  /**
   * Checks that the specified object reference is not {@code null} and throws
   * {@link NullPointerException} with the specified detail message if it is.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param message The provider of the {@link NullPointerException} detail message.
   * @return The specified object reference.
   * @throws NullPointerException if the specified object reference is {@code null}.
   * @see #message(Supplier)
   */
  public static <T> T requireNonNull(T object, Object message) {
    if (object == null) {
      throw new NullPointerException(String.valueOf(
          message instanceof Supplier ? ((Supplier<?>) message).get() : message));
    }
    return object;
  }

  /**
   * Checks that the specified object satisfies the specified condition and
   * throws {@link IllegalArgumentException} without detail message if it is not.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param condition The predicate to be applied to the object.
   * @return The specified object reference.
   * @throws IllegalArgumentException if the specified object does not satisfy
   *         the specified condition.
   */
  public static <T> T require(T object, Predicate<? super T> condition) {
    if (condition.test(object)) {
      return object;
    }
    throw new IllegalArgumentException(String.valueOf(object));
  }

  /**
   * Checks that the specified object satisfies the specified condition and
   * throws {@link IllegalArgumentException} with the specified detail message
   * built as {@code String.valueOf(message)} if it is not.
   *
   * @param <T> The type of the object.
   * @param object The object reference to check.
   * @param condition The predicate to be applied to the object.
   * @param message The detail message provider for the {@link IllegalArgumentException}.
   * @return The specified object reference.
   * @throws IllegalArgumentException if the specified object does not satisfy
   *         the specified condition.
   * @see #message(Supplier)
   */
  public static <T> T require(T object, Predicate<? super T> condition, Object message) {
    if (condition.test(object)) {
      return object;
    }
    throw new IllegalArgumentException(String.valueOf(
        message instanceof Supplier ? ((Supplier<?>) message).get() : message));

  }

  /**
   * Checks that the specified object satisfies the specified condition and
   * throws custom exception if it is not.
   *
   * @param <T> The type of the object.
   * @param <E> The type of the exception to throw.
   * @param object The object reference to check.
   * @param condition The predicate to be applied to the object.
   * @param exception The provider of the exception.
   * @return The specified object reference.
   * @throws E if the specified object does not satisfy the specified condition.
   */
  public static <T, E extends Throwable> T require(T object, Predicate<? super T> condition,
      Supplier<? extends E> exception) throws E {
    if (condition.test(object)) {
      return object;
    }
    throw exception.get();
  }

  // Primitive validations

  // int

  /**
   * Checks that the specified {@code int} number satisfies the specified
   * condition and throws {@link IllegalArgumentException} without detail
   * message if it is not.
   *
   * @param number The {@code int} number to check.
   * @param condition The predicate to be applied to the {@code int} number.
   * @return The specified {@code int} number.
   * @throws IllegalArgumentException if the specified {@code int} number does
   *         not satisfy the specified condition.
   */
  public static int require(int number, IntPredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(String.valueOf(number));
  }

  // long

  /**
   * Checks that the specified {@code long} number satisfies the specified
   * condition and throws {@link IllegalArgumentException} without detail
   * message if it is not.
   *
   * @param number The {@code long} number to check.
   * @param condition The predicate to be applied to the {@code long} number.
   * @return The specified {@code long} number.
   * @throws IllegalArgumentException if the specified {@code long} number does
   *         not satisfy the specified condition.
   */
  public static long require(long number, LongPredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(String.valueOf(number));
  }

  // double

  /**
   * Checks that the specified {@code double} number satisfies the specified
   * condition and throws {@link IllegalArgumentException} without detail
   * message if it is not.
   *
   * @param number The {@code double} number to check.
   * @param condition The predicate to be applied to the {@code double} number.
   * @return The specified {@code double} number.
   * @throws IllegalArgumentException if the specified {@code double} number
   *         does not satisfy the specified condition.
   */
  public static double require(double number, DoublePredicate condition) {
    if (condition.test(number)) {
      return number;
    }
    throw new IllegalArgumentException(String.valueOf(number));
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
