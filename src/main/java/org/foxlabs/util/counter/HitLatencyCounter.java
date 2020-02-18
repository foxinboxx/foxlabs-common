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
 * Counter that is aggregation of {@link HitCounter} and {@link LatencyCounter}.
 * It allows to gather hits/misses statistics including latency times.
 * 
 * <p>Example of usage:</p>
 * <pre>
 * final int testCount = 10000;
 * final HitLatencyCounter counter = Counters.defaultHitLatencyCounter();
 * for (int i = 0; i &lt; testCount; i++) {
 *   counter.start();
 *   boolean hit = // ... the code being measured ...
 *   counter.stop(hit);
 * }
 * System.out.println("Hits and latency time statistics: " + counter);
 * </pre>
 * 
 * @author Fox Mulder
 * @see HitCounter
 * @see LatencyCounter
 * @see Counters#defaultHitLatencyCounter()
 * @see Counters#HIT_LATENCY_COUNTER_STUB
 */
public interface HitLatencyCounter extends ToString {

  /**
   * Returns maximum recursion depth.
   * 
   * @return Maximum recursion depth.
   */
  int getMaxDepth();

  /**
   * Returns total number of invocations (hits + misses).
   * 
   * @return Total number of invocations (hits + misses).
   */
  long getInvocationCount();

  /**
   * Returns minimum latency time over all the invocations in nanoseconds.
   * 
   * @return Minimum latency time over all the invocations in nanoseconds.
   */
  long getMinLatency();

  /**
   * Returns maximum latency time over all the invocations in nanoseconds.
   * 
   * @return Maximum latency time over all the invocations in nanoseconds.
   */
  long getMaxLatency();

  /**
   * Returns average latency time over all the invocations in nanoseconds.
   * 
   * @return Average latency time over all the invocations in nanoseconds.
   */
  long getAverageLatency();

  /**
   * Returns total latency time over all the invocations in nanoseconds.
   * 
   * @return Total latency time over all the invocations in nanoseconds.
   */
  long getTotalLatency();

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
   * Returns minimum hits latency time over all the invocations in nanoseconds.
   * 
   * @return Minimum hits latency time over all the invocations in nanoseconds.
   */
  long getHitMinLatency();

  /**
   * Returns maximum hits latency time over all the invocations in nanoseconds.
   * 
   * @return Maximum hits latency time over all the invocations in nanoseconds.
   */
  long getHitMaxLatency();

  /**
   * Returns average hits latency time over all the invocations in nanoseconds.
   * 
   * @return Average hits latency time over all the invocations in nanoseconds.
   */
  long getHitAverageLatency();

  /**
   * Returns total hits latency time over all the invocations in nanoseconds.
   * 
   * @return Total hits latency time over all the invocations in nanoseconds.
   */
  long getHitTotalLatency();

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
   * Returns minimum misses latency time over all the invocations in nanoseconds.
   * 
   * @return Minimum misses latency time over all the invocations in nanoseconds.
   */
  long getMissMinLatency();

  /**
   * Returns maximum misses latency time over all the invocations in nanoseconds.
   * 
   * @return Maximum misses latency time over all the invocations in nanoseconds.
   */
  long getMissMaxLatency();

  /**
   * Returns average misses latency time over all the invocations in nanoseconds.
   * 
   * @return Average misses latency time over all the invocations in nanoseconds.
   */
  long getMissAverageLatency();

  /**
   * Returns total misses latency time over all the invocations in nanoseconds.
   * 
   * @return Total misses latency time over all the invocations in nanoseconds.
   */
  long getMissTotalLatency();

  /**
   * Starts next measurement.
   */
  void start();

  /**
   * Stops measurement and updates statistics.
   * 
   * @param hit Determines whether hits or misses value should be incremented.
   * @throws IllegalStateException if the {@link #start()} method was not called
   *         before.
   */
  void stop(boolean hit);

  /**
   * Determines whether this counter is started (the {@link #start()} method was
   * called and no corresponding {@link #stop(boolean)} method called yet).
   * 
   * @return <code>true</code> if this counter was started;
   *         <code>false</code> otherwise.
   */
  boolean isStarted();

  /**
   * Determines whether this counter is stopped (the {@link #stop(boolean)}
   * method was called).
   * 
   * @return <code>true</code> if this counter was stopped;
   *         <code>false</code> otherwise.
   */
  boolean isStopped();

  /**
   * Merges statistics of the specified counter into this counter.
   * 
   * @param counter Counter which statistics should be merged into this counter.
   */
  void merge(HitLatencyCounter counter);

  /**
   * Resets statistics of this counter.
   */
  void reset();

}
