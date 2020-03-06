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

import java.util.function.IntUnaryOperator;

/**
 * A collection of reusable functions.
 *
 * @author Fox Mulder
 */
public class Functions {

  // Instantiation is not possible
  private Functions() {
    throw new IllegalAccessError();
  }

  // Character code point functions

  /** Character.toLowerCase(c) */
  public static final IntUnaryOperator CHAR_TO_LOWER_CASE = Character::toLowerCase;
  /** Character.toUpperCase(c) */
  public static final IntUnaryOperator CHAR_TO_UPPER_CASE = Character::toUpperCase;

}
