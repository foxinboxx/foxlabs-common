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

package org.foxlabs.util;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Objects {

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

}
