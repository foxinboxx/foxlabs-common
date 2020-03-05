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
 * Defines the {@link #build()} method which could be implemented by classes
 * that follow the <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder</a>
 * pattern.
 *
 * @param <R> The type of the resulting assembly object.
 *
 * @author Fox Mulder
 */
@FunctionalInterface
public interface Buildable<R> {

  /**
   * Returns resulting assembly object.
   *
   * @return A resulting assembly object.
   */
  R build();

}
