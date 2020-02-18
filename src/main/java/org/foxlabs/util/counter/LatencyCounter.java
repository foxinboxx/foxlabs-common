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
 * Counter that allows to gather latency time statistics including minimum
 * latency, maximum latency, average latency and total latency.
 * 
 * <p>Example of usage:</p>
 * <pre>
 * final int testCount = 10000;
 * final LatencyCounter counter = Counters.defaultLatencyCounter();
 * for (int i = 0; i &lt; testCount; i++) {
 *   counter.start();
 *   // ... the code being measured ...
 *   counter.stop();
 * }
 * System.out.println("Latency time statistics: " + counter);
 * </pre>
 * 
 * @author Fox Mulder
 * @see Counters#defaultLatencyCounter()
 * @see Counters#concurrentLatencyCounter()
 * @see Counters#LATENCY_COUNTER_STUB
 */
public interface LatencyCounter extends ToString {

  /**
   * Returns maximum recursion depth.
   * 
   * @return Maximum recursion depth.
   */
  int getMaxDepth();

  /**
   * Returns total number of invocations.
   * 
   * @return Total number of invocations.
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
   * Starts next measurement.
   */
  void start();

  /**
   * Stops measurement and updates statistics.
   * 
   * @throws IllegalStateException if corresponding {@link #start()} method was
   *         not called before.
   */
  void stop();

  /**
   * Determines whether this counter is started (the {@link #start()} method was
   * called and no corresponding {@link #stop()} method called yet).
   * 
   * @return <code>true</code> if this counter was started;
   *         <code>false</code> otherwise.
   */
  boolean isStarted();

  /**
   * Determines whether this counter is stopped (the {@link #stop()} method was
   * called).
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
  void merge(LatencyCounter counter);

  /**
   * Resets statistics of this counter.
   */
  void reset();

}
