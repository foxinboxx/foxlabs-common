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

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Objects.*;

/**
 * Tests for methods of the {@link Objects} class.
 *
 * @author Fox Mulder
 */
public class ObjectsTest {

  // Miscellaneous

  /**
   * Tests the {@link Objects#cast(Object)} method.
   */
  @Test
  public void test_cast() {
    final Integer sampleObject = Integer.valueOf(10);
    assertSame(sampleObject, cast(sampleObject));
    assertThrows(ClassCastException.class, () -> new String(Objects.<char[]>cast(Integer.valueOf(10))));
  }

}
