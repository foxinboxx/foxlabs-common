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
 * Defines the {@link #set(Object, Object)} method which allows to assign a
 * value to some property of the target object.
 *
 * @param <T> The type of the target object.
 * @param <V> The type of the value to set on the target object.
 * @param <E> The type of the exception thrown if operation failed.
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface Setter<T, V, E extends Throwable> {

  /**
   * Sets the specified value on the specified target object.
   *
   * @param target The target object.
   * @param value The value to set on the target object.
   * @throws E if operation failed.
   */
  void set(T target, V value) throws E;

  // Setter.Unchecked

  /**
   * The same as the {@link Setter} but does not throw checked exception.
   *
   * @param <T> The type of the target object.
   * @param <V> The type of the value to set on the target object.
   *
   * @author Fox Mulder
   */
  @FunctionalInterface
  interface Unchecked<T, V> extends Setter<T, V, RuntimeException> {

    /**
     * Sets the specified value on the specified target object.
     *
     * @param target The target object.
     * @param value The value to set on the target object.
     * @throws RuntimeException if operation failed.
     */
    @Override void set(T target, V value);

  }

  // Stubs

  /**
   * The auxiliary {@link Setter} stub that does nothing.
   */
  Setter<?, ?, RuntimeException> NULL = new Setter.Unchecked<Object, Object>() {
    @Override public void set(Object target, Object value) {
      // Nothing to do
    }
  };

  /**
   * Returns reference to the {@link #NULL} stub instance as a {@link Setter}.
   *
   * @return A reference to the {@link #NULL} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V, E extends Throwable> Setter<T, V, E> nullStub() {
    return (Setter<T, V, E>) NULL;
  }

  /**
   * Returns reference to the {@link #NULL} stub instance as a {@link Setter.Unchecked}.
   *
   * @return A reference to the {@link #NULL} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V> Setter.Unchecked<T, V> nullUncheckedStub() {
    return (Setter.Unchecked<T, V>) NULL;
  }

  /**
   * The auxiliary {@link Setter} stub that throws {@link UnsupportedOperationException}.
   */
  Setter<?, ?, RuntimeException> UNSUPPORTED = new Setter.Unchecked<Object, Object>() {
    @Override public void set(Object target, Object value) {
      throw new UnsupportedOperationException();
    }
  };

  /**
   * Returns reference to the {@link #UNSUPPORTED} stub instance as a {@link Setter}.
   *
   * @return A reference to the {@link #UNSUPPORTED} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V, E extends Throwable> Setter<T, V, E> unsupportedStub() {
    return (Setter<T, V, E>) UNSUPPORTED;
  }

  /**
   * Returns reference to the {@link #UNSUPPORTED} stub instance as a {@link Setter.Unchecked}.
   *
   * @return A reference to the {@link #UNSUPPORTED} stub instance.
   */
  @SuppressWarnings("unchecked")
  static <T, V> Setter.Unchecked<T, V> unsupportedUncheckedStub() {
    return (Setter.Unchecked<T, V>) UNSUPPORTED;
  }

}
