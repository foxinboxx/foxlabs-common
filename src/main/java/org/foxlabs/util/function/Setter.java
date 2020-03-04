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

package org.foxlabs.util.function;

/**
 *
 * @param <T> The type of the target object.
 * @param <V> The type of the value to set on the target object.
 * @param <E> The type of the exception.
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

  /**
   * The auxiliary setter that does nothing.
   */
  Setter.Unchecked<?, ?> STUB = new Setter.Unchecked<Object, Object>() {
    @Override public void set(Object target, Object value) {
      // Nothing to do
    }
  };

}
