/* 
 * Copyright (C) 2016 FoxLabs
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

package org.foxlabs.util.counter;

import org.foxlabs.util.ToString;

/**
 * Counter that allows to gather hits/misses statistics.
 * 
 * <p>Example of usage:</p>
 * <pre>
 * final int testCount = 10000;
 * final HitCounter counter = Counters.defaultHitCounter();
 * for (int i = 0; i &lt; testCount; i++) {
 *   boolean hit = // ... the code being measured ...
 *   counter.increment(hit);
 * }
 * System.out.println("Hits statistics: " + counter);
 * </pre>
 * 
 * @author Fox Mulder
 * @see Counters#defaultHitCounter()
 * @see Counters#concurrentHitCounter()
 * @see Counters#HIT_COUNTER_STUB
 */
public interface HitCounter extends ToString {

  /**
   * Returns total number of invocations (hits + misses).
   * 
   * @return Total number of invocations (hits + misses).
   */
  long getInvocationCount();

  /**
   * Returns number of hits.
   * 
   * @return Number of hits.
   */
  long getHitCount();

  /**
   * Returns percentage of hits.
   * 
   * @return Percentage of hits.
   */
  double getHitRate();

  /**
   * Returns number of misses.
   * 
   * @return Number of misses.
   */
  long getMissCount();

  /**
   * Returns percentage of misses.
   * 
   * @return Percentage of misses.
   */
  double getMissRate();

  /**
   * Increments hits value if the specified hit flag is <code>true</code>;
   * increments misses value otherwise.
   * 
   * @param hit Determines whether hits or misses value should be incremented.
   */
  void increment(boolean hit);

  /**
   * Merges statistics of the specified counter into this counter.
   * 
   * @param counter Counter which statistics should be merged into this counter.
   */
  void merge(HitCounter counter);

  /**
   * Resets statistics of this counter.
   */
  void reset();

}
