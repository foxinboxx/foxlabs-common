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

package org.foxlabs.common.function;

/**
 * Defines the {@link #get(Object)} method which allows to retrieve a value of some property from
 * the target object.
 *
 * @param <T> The type of the target object.
 * @param <V> The type of the value to get from the target object.
 * @param <E> The type of the exception thrown if operation failed.
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface Getter<T, V, E extends Throwable> {

  /**
   * Gets a value from the specified target object.
   *
   * @param target The target object.
   * @return A value got from the target object.
   * @throws E if operation failed.
   */
  V get(T target) throws E;

  // Getter.Unchecked

  /**
   * The same as the {@link Getter} but does not throw checked exception.
   *
   * @param <T> The type of the target object.
   * @param <V> The type of the value to get from the target object.
   *
   * @author Fox Mulder
   */
  @FunctionalInterface
  interface Unchecked<T, V> extends Getter<T, V, RuntimeException> {

    /**
     * Gets a value from the specified target object.
     *
     * @param target The target object.
     * @return A value got from the target object.
     * @throws RuntimeException if operation failed.
     */
    @Override V get(T target);

  }

  // Stubs

  /**
   * The auxiliary {@link Getter} stub that returns {@code null}.
   */
  Getter<?, ?, RuntimeException> NULL = new Getter.Unchecked<Object, Object>() {
    @Override public Object get(Object target) {
      return null;
    }
  };

  /**
   * Returns reference to the {@link #NULL} stub instance as a {@link Getter}.
   *
   * @return A reference to the {@link #NULL} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V, E extends Throwable> Getter<T, V, E> nullStub() {
    return (Getter<T, V, E>) NULL;
  }

  /**
   * Returns reference to the {@link #NULL} stub instance as a {@link Getter.Unchecked}.
   *
   * @return A reference to the {@link #NULL} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V> Getter.Unchecked<T, V> nullUncheckedStub() {
    return (Getter.Unchecked<T, V>) NULL;
  }

  /**
   * The auxiliary {@link Getter} stub that throws {@link UnsupportedOperationException}.
   */
  Getter<?, ?, RuntimeException> UNSUPPORTED = new Getter.Unchecked<Object, Object>() {
    @Override public Object get(Object target) {
      throw new UnsupportedOperationException();
    }
  };

  /**
   * Returns reference to the {@link #UNSUPPORTED} stub instance as a {@link Getter}.
   *
   * @return A reference to the {@link #UNSUPPORTED} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V, E extends Throwable> Getter<T, V, E> unsupportedStub() {
    return (Getter<T, V, E>) UNSUPPORTED;
  }

  /**
   * Returns reference to the {@link #UNSUPPORTED} stub instance as a {@link Getter.Unchecked}.
   *
   * @return A reference to the {@link #UNSUPPORTED} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V> Getter.Unchecked<T, V> unsupportedUncheckedStub() {
    return (Getter.Unchecked<T, V>) UNSUPPORTED;
  }

}
