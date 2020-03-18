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

package org.foxlabs.common.exception;

import org.foxlabs.common.Objects;
import org.foxlabs.common.text.CharBuffer;

/**
 * Thrown to indicate that a threshold of some data structure like buffer has been reached and
 * further operations that may add more data are no longer possible. Normally this exception should
 * not be handled by data producers, but data consumers.
 *
 * @author Fox Mulder
 * @see CharBuffer
 */
public class ThresholdReachedException extends UnsupportedOperationException {
  private static final long serialVersionUID = -6690298736199595817L;

  /**
   * The producer that caused this exception.
   */
  private final Object producer;

  /**
   * Constructs a new {@code ThresholdReachedException} with the specified producer.
   *
   * @param producer The producer that caused this exception.
   */
  public <T> ThresholdReachedException(Object producer) {
    this.producer = producer;
  }

  /**
   * Returns producer that caused this exception. There is no guarantee that the returned producer
   * is not {@code null}.
   *
   * @return The producer that caused this exception.
   */
  public <T> T getProducer() {
    return Objects.cast(producer);
  }

}
