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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.foxlabs.common.function.ToString;
import org.foxlabs.common.text.CharBuffer;

/**
 * Factory class for all counters.
 *
 * <p>All the <code>defaultXXXCounter()</code> methods return non thread-safe
 * counter instances; the <code>concurrentXXXCounter()</code> methods return
 * thread-safe ones. Also the <code>XXX_COUNTER_STUB</code> constants contain
 * counter stubs that do not perform any measurements at all and could be used
 * when statistics gathering is disabled.</p>
 *
 * @author Fox Mulder
 * @see HitCounter
 * @see LatencyCounter
 * @see HitLatencyCounter
 */
public abstract class Counters {

  // Instances are not allowed
  private Counters() {
    throw new IllegalAccessError();
  }

  // ----- HitCounter ---------------------------------------------------------

  /**
   * Thread-safe stub implementation of the {@link HitCounter}.
   */
  public static final HitCounter HIT_COUNTER_STUB = new HitCounter() {

    @Override
    public long getInvocationCount() {
      return 0L;
    }

    @Override
    public long getHitCount() {
      return 0L;
    }

    @Override
    public double getHitRate() {
      return 0.0;
    }

    @Override
    public long getMissCount() {
      return 0L;
    }

    @Override
    public double getMissRate() {
      return 0.0;
    }

    @Override
    public void increment(boolean hit) {
    }

    @Override
    public void merge(HitCounter counter) {
    }

    @Override
    public void reset() {
    }

    @Override
    public String toString() {
      return NA;
    }

    @Override
    public CharBuffer toString(CharBuffer buf) {
      return buf.append(NA);
    }

  };

  /**
   * Returns new {@link HitCounter} instance.
   *
   * @return New {@link HitCounter} instance.
   */
  public static HitCounter defaultHitCounter() {
    return new DefaultHitCounter();
  }

  /**
   * Returns new thread-safe {@link HitCounter} instance.
   *
   * @return New thread-safe {@link HitCounter} instance.
   */
  public static HitCounter concurrentHitCounter() {
    return new ConcurrentHitCounter();
  }

  // AbstractHitCounter

  /**
   * Abstract implementation of the {@link HitCounter}.
   *
   * @author Fox Mulder
   * @see HitCounter
   * @see DefaultHitCounter
   * @see ConcurrentHitCounter
   */
  private static abstract class AbstractHitCounter extends ToString.Adapter implements HitCounter {

    /**
     * Returns total number of invocations (hits + misses).
     *
     * @return Total number of invocations (hits + misses).
     */
    @Override
    public long getInvocationCount() {
      return getHitCount() + getMissCount();
    }

    /**
     * Returns percentage of hits.
     *
     * @return Percentage of hits.
     */
    @Override
    public double getHitRate() {
      return getInvocationCount() > 0L ? (double) getHitCount() / (double) getInvocationCount() : 0.0;
    }

    /**
     * Returns percentage of misses.
     *
     * @return Percentage of misses.
     */
    @Override
    public double getMissRate() {
      return getInvocationCount() > 0L ? (double) getMissCount() / (double) getInvocationCount() : 0.0;
    }

    /**
     * Appends string representation of current state of this counter to the
     * specified buffer.
     *
     * The format is <code>INVOCATION_COUNT (+HIT_COUNT HIT_RATE% | -MISS_COUNT MISS_RATE%)</code>.
     *
     * @param buf Buffer to append.
     * @return The specified buffer.
     */
    @Override
    public CharBuffer toString(CharBuffer buf) {
      buf.appendLong(getInvocationCount());
      formatRate(getHitRate(), buf.append(" (+").appendLong(getHitCount()).append(" "));
      formatRate(getMissRate(), buf.append(" | -").appendLong(getMissCount()).append(" "));
      return buf.append(")");
    }

  }

  // DefaultHitCounter

  /**
   * Default non thread-safe implementation of the {@link HitCounter}.
   *
   * @author Fox Mulder
   * @see AbstractHitCounter
   */
  private final static class DefaultHitCounter extends AbstractHitCounter {

    /**
     * Number of hits.
     */
    private long hitCount;

    /**
     * Number of misses.
     */
    private long missCount;

    /**
     * Returns number of hits.
     *
     * @return Number of hits.
     */
    @Override
    public long getHitCount() {
      return hitCount;
    }

    /**
     * Returns number of misses.
     *
     * @return Number of misses.
     */
    @Override
    public long getMissCount() {
      return missCount;
    }

    /**
     * Increments hits value if the specified hit flag is <code>true</code>;
     * increments misses value otherwise.
     *
     * @param hit Determines whether hits or misses value should be incremented.
     */
    @Override
    public void increment(boolean hit) {
      if (hit) {
        hitCount++;
      } else {
        missCount++;
      }
    }

    /**
     * Merges statistics of the specified counter into this counter.
     *
     * @param counter Counter which statistics should be merged into this
     *        counter.
     */
    @Override
    public void merge(HitCounter counter) {
      hitCount += counter.getHitCount();
      missCount += counter.getMissCount();
    }

    /**
     * Resets statistics of this counter.
     */
    @Override
    public void reset() {
      hitCount = missCount = 0L;
    }

  }

  // ConcurrentHitCounter

  /**
   * Thread-safe implementation of the {@link HitCounter}.
   *
   * @author Fox Mulder
   * @see AbstractHitCounter
   */
  private static final class ConcurrentHitCounter extends AbstractHitCounter {

    /**
     * Number of hits.
     */
    private AtomicLong hitCount = new AtomicLong();

    /**
     * Number of misses.
     */
    private AtomicLong missCount = new AtomicLong();

    /**
     * Returns number of hits.
     *
     * @return Number of hits.
     */
    @Override
    public long getHitCount() {
      return hitCount.get();
    }

    /**
     * Returns number of misses.
     *
     * @return Number of misses.
     */
    @Override
    public long getMissCount() {
      return missCount.get();
    }

    /**
     * Increments hits value if the specified hit flag is <code>true</code>;
     * increments misses value otherwise.
     *
     * @param hit Determines whether hits or misses value should be incremented.
     */
    @Override
    public void increment(boolean hit) {
      if (hit) {
        hitCount.incrementAndGet();
      } else {
        missCount.incrementAndGet();
      }
    }

    /**
     * Merges statistics of the specified counter into this counter.
     *
     * @param counter Counter which statistics should be merged into this counter.
     */
    @Override
    public void merge(HitCounter counter) {
      hitCount.addAndGet(counter.getHitCount());
      missCount.addAndGet(counter.getMissCount());
    }

    /**
     * Resets statistics of this counter.
     */
    @Override
    public void reset() {
      hitCount.set(0L);
      missCount.set(0L);
    }

  }

  // ----- LatencyCounter -----------------------------------------------------

  /**
   * Thread-safe stub implementation of the {@link LatencyCounter}.
   */
  public static final LatencyCounter LATENCY_COUNTER_STUB = new LatencyCounter() {

    private volatile int depth = 0;

    @Override
    public int getMaxDepth() {
      return 0;
    }

    @Override
    public long getInvocationCount() {
      return 0L;
    }

    @Override
    public long getMinLatency() {
      return 0L;
    }

    @Override
    public long getMaxLatency() {
      return 0L;
    }

    @Override
    public long getAverageLatency() {
      return 0L;
    }

    @Override
    public long getTotalLatency() {
      return 0L;
    }

    @Override
    public void start() {
      depth++;
    }

    @Override
    public void stop() {
      if (--depth <= 0)
        throw new IllegalStateException();
    }

    @Override
    public boolean isStarted() {
      return depth > 0;
    }

    @Override
    public boolean isStopped() {
      return depth <= 0;
    }

    @Override
    public void merge(LatencyCounter counter) {
    }

    @Override
    public void reset() {
      depth = 0;
    }

    @Override
    public String toString() {
      return NA;
    }

    @Override
    public CharBuffer toString(CharBuffer buf) {
      return buf.append(NA);
    }

  };

  /**
   * Returns new {@link LatencyCounter} instance.
   *
   * @return New {@link LatencyCounter} instance.
   */
  public static LatencyCounter defaultLatencyCounter() {
    return new DefaultLatencyCounter();
  }

  /**
   * Returns new thread-safe {@link LatencyCounter} instance.
   *
   * @return New thread-safe {@link LatencyCounter} instance.
   */
  public static LatencyCounter concurrentLatencyCounter() {
    return new ConcurrentLatencyCounter();
  }

  // AbstractLatencyCounter

  /**
   * Abstract implementation of the {@link LatencyCounter}.
   *
   * @author Fox Mulder
   * @see LatencyCounter
   * @see DefaultLatencyCounter
   * @see ConcurrentLatencyCounter
   */
  private static abstract class AbstractLatencyCounter extends ToString.Adapter implements LatencyCounter {

    /**
     * Returns average latency time over all the invocations in milliseconds.
     *
     * @return Average latency time over all the invocations in milliseconds.
     */
    @Override
    public long getAverageLatency() {
      return getInvocationCount() > 0L ? getTotalLatency() / getInvocationCount() : 0L;
    }

    /**
     * Starts next measurement.
     *
     * @see #start(long)
     */
    @Override
    public void start() {
      start(System.currentTimeMillis());
    }

    /**
     * Starts next measurement using the specified start time.
     *
     * @param time Start time.
     */
    protected abstract void start(long time);

    /**
     * Stops measurement and updates statistics.
     *
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     * @see #stop(long)
     */
    @Override
    public void stop() {
      stop(System.currentTimeMillis());
    }

    /**
     * Stops measurement using the specified stop time and updates statistics.
     *
     * @param time Stop time.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     */
    protected abstract void stop(long time);

    /**
     * Appends string representation of current state of this latency counter to
     * the specified buffer.
     *
     * The format is <code>INVOCATION_COUNT / TOTAL_LATENCY (-MIN_LATENCY +MAX_LATENCY ~AVERAGE_LATENCY)</code>.
     *
     * @param buf Buffer to append.
     * @return The specified buffer.
     * @see #formatLatency(long, StringBuilder)
     */
    @Override
    public CharBuffer toString(CharBuffer buf) {
      buf.appendLong(getInvocationCount());
      formatLatency(getTotalLatency(), buf.append(" / "));
      formatLatency(getMinLatency(), buf.append(" (-"));
      formatLatency(getMaxLatency(), buf.append(" +"));
      formatLatency(getAverageLatency(), buf.append(" ~"));
      return buf.append(")");
    }

  }

  // DefaultLatencyCounter

  /**
   * Default non thread-safe implementation of the {@link LatencyCounter}.
   *
   * @author Fox Mulder
   * @see AbstractLatencyCounter
   */
  private static final class DefaultLatencyCounter extends AbstractLatencyCounter {

    /**
     * Current recursion depth.
     */
    private int currentDepth;

    /**
     * Maximum recursion depth.
     */
    private int maxDepth;

    /**
     * Total number of invocations.
     */
    private long invocationCount;

    /**
     * Minimum latency time over all the invocations in milliseconds.
     */
    private long minLatency;

    /**
     * Maximum latency time over all the invocations in milliseconds.
     */
    private long maxLatency;

    /**
     * Total latency time over all the invocations in milliseconds.
     */
    private long totalLatency;

    /**
     * Head time node.
     */
    private TimeNode head;

    /**
     * Returns maximum recursion depth.
     *
     * @return Maximum recursion depth.
     */
    @Override
    public int getMaxDepth() {
      return maxDepth;
    }

    /**
     * Returns total number of invocations.
     *
     * @return Total number of invocations.
     */
    @Override
    public long getInvocationCount() {
      return invocationCount;
    }

    /**
     * Returns minimum latency time over all the invocations in milliseconds.
     *
     * @return Minimum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMinLatency() {
      return minLatency;
    }

    /**
     * Returns maximum latency time over all the invocations in milliseconds.
     *
     * @return Maximum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMaxLatency() {
      return maxLatency;
    }

    /**
     * Returns total latency time over all the invocations in milliseconds.
     *
     * @return Total latency time over all the invocations in milliseconds.
     */
    @Override
    public long getTotalLatency() {
      return totalLatency;
    }

    /**
     * Starts next measurement using the specified start time.
     *
     * @param time Start time.
     */
    @Override
    protected void start(long time) {
      invocationCount++;
      currentDepth++;
      if (maxDepth < currentDepth) {
        maxDepth = currentDepth;
      }
      head = new TimeNode(head, time);
    }

    /**
     * Stops measurement using the specified stop time and updates statistics.
     *
     * @param time Stop time.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     */
    @Override
    protected void stop(long time) {
      if (head == null) {
        throw new IllegalStateException();
      } else {
        currentDepth--;
        long latency = time - head.time;
        if (minLatency > latency || minLatency == 0L) {
          minLatency = latency;
        }
        if (maxLatency < latency || maxLatency == 0L) {
          maxLatency = latency;
        }
        if ((head = head.next) == null) {
          totalLatency += latency;
        }
      }
    }

    /**
     * Determines whether this counter is started (the {@link #start()} method
     * was called and no corresponding {@link #stop()} method called yet).
     *
     * @return <code>true</code> if this counter was started;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isStarted() {
      return head != null;
    }

    /**
     * Determines whether this counter is stopped (the {@link #stop()} method
     * was called).
     *
     * @return <code>true</code> if this counter was stopped;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isStopped() {
      return head == null;
    }

    /**
     * Merges statistics of the specified counter into this counter.
     *
     * @param counter Counter which statistics should be merged into this counter.
     */
    @Override
    public void merge(LatencyCounter counter) {
      invocationCount += counter.getInvocationCount();
      if (maxDepth < counter.getMaxDepth()) {
        maxDepth = counter.getMaxDepth();
      }
      if (minLatency > counter.getMinLatency() || minLatency == 0L) {
        minLatency = counter.getMinLatency();
      }
      if (maxLatency < counter.getMaxLatency() || maxLatency == 0L) {
        maxLatency = counter.getMaxLatency();
      }
      totalLatency += counter.getTotalLatency();
    }

    /**
     * Resets statistics of this counter.
     */
    @Override
    public void reset() {
      currentDepth = maxDepth = 0;
      invocationCount = 0L;
      minLatency = maxLatency = totalLatency = 0L;
      head = null;
    }

  }

  // ConcurrentLatencyCounter

  /**
   * Thread-safe implementation of the {@link LatencyCounter}.
   *
   * @author Fox Mulder
   * @see AbstractLatencyCounter
   */
  private static final class ConcurrentLatencyCounter extends AbstractLatencyCounter {

    private final AtomicInteger maxDepth = new AtomicInteger();

    private final AtomicLong invocationCount = new AtomicLong();

    private final AtomicLong minLatency = new AtomicLong();

    private final AtomicLong maxLatency = new AtomicLong();

    private final AtomicLong totalLatency = new AtomicLong();

    private final ThreadLocal<DefaultLatencyCounter> localCounters = new ThreadLocal<DefaultLatencyCounter>();

    private volatile int threadCount = 0;

    /**
     * Returns maximum recursion depth.
     *
     * @return Maximum recursion depth.
     */
    @Override
    public int getMaxDepth() {
      return maxDepth.get();
    }

    /**
     * Returns total number of invocations.
     *
     * @return Total number of invocations.
     */
    @Override
    public long getInvocationCount() {
      return invocationCount.get();
    }

    /**
     * Returns minimum latency time over all the invocations in milliseconds.
     *
     * @return Minimum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMinLatency() {
      return minLatency.get();
    }

    /**
     * Returns maximum latency time over all the invocations in milliseconds.
     *
     * @return Maximum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMaxLatency() {
      return maxLatency.get();
    }

    /**
     * Returns total latency time over all the invocations in milliseconds.
     *
     * @return Total latency time over all the invocations in milliseconds.
     */
    @Override
    public long getTotalLatency() {
      return totalLatency.get();
    }

    /**
     * Starts next measurement using the specified start time.
     *
     * @param time Start time.
     */
    @Override
    protected void start(long time) {
      DefaultLatencyCounter counter = localCounters.get();
      if (counter == null) {
        localCounters.set(counter = new DefaultLatencyCounter());
        threadCount++;
      }
      counter.start(time);
    }

    /**
     * Stops measurement using the specified stop time and updates statistics.
     *
     * @param time Stop time.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     */
    @Override
    protected void stop(long time) {
      DefaultLatencyCounter counter = localCounters.get();
      if (counter == null) {
        throw new IllegalStateException();
      } else {
        counter.stop(time);
        if (counter.isStopped()) {
          merge(counter);
          localCounters.remove();
          threadCount--;
        }
      }
    }

    @Override
    public boolean isStarted() {
      return threadCount > 0;
    }

    @Override
    public boolean isStopped() {
      return threadCount == 0;
    }

    /**
     * Merges statistics of the specified counter into this counter.
     *
     * @param counter Counter which statistics should be merged into this counter.
     */
    @Override
    public void merge(LatencyCounter counter) {
      invocationCount.addAndGet(counter.getInvocationCount());
      maxDepth.updateAndGet(v -> v < counter.getMaxDepth() || v == 0 ? counter.getMaxDepth() : v);
      minLatency.updateAndGet(v -> v > counter.getMinLatency() || v == 0L ? counter.getMinLatency() : v);
      maxLatency.updateAndGet(v -> v < counter.getMaxLatency() || v == 0L ? counter.getMaxLatency() : v);
      totalLatency.addAndGet(counter.getTotalLatency());
    }

    /**
     * Resets statistics of this counter.
     */
    @Override
    public void reset() {
      if (isStarted()) {
        throw new IllegalArgumentException();
      } else {
        maxDepth.set(0);
        invocationCount.set(0L);
        minLatency.set(0L);
        maxLatency.set(0L);
        totalLatency.set(0L);
      }
    }

  }

  // ----- HitLatencyCounter --------------------------------------------------

  /**
   * Thread-safe stub implementation of the {@link HitLatencyCounter}.
   */
  public static final HitLatencyCounter HIT_LATENCY_COUNTER_STUB = new HitLatencyCounter() {

    private volatile int depth = 0;

    @Override
    public int getMaxDepth() {
      return 0;
    }

    @Override
    public long getInvocationCount() {
      return 0L;
    }

    @Override
    public long getMinLatency() {
      return 0L;
    }

    @Override
    public long getMaxLatency() {
      return 0L;
    }

    @Override
    public long getAverageLatency() {
      return 0L;
    }

    @Override
    public long getTotalLatency() {
      return 0L;
    }

    @Override
    public long getHitCount() {
      return 0L;
    }

    @Override
    public double getHitRate() {
      return 0.0;
    }

    @Override
    public long getHitMinLatency() {
      return 0L;
    }

    @Override
    public long getHitMaxLatency() {
      return 0L;
    }

    @Override
    public long getHitAverageLatency() {
      return 0L;
    }

    @Override
    public long getHitTotalLatency() {
      return 0L;
    }

    @Override
    public long getMissCount() {
      return 0L;
    }

    @Override
    public double getMissRate() {
      return 0.0;
    }

    @Override
    public long getMissMinLatency() {
      return 0L;
    }

    @Override
    public long getMissMaxLatency() {
      return 0L;
    }

    @Override
    public long getMissAverageLatency() {
      return 0L;
    }

    @Override
    public long getMissTotalLatency() {
      return 0L;
    }

    @Override
    public void start() {
      depth++;
    }

    @Override
    public void stop(boolean hit) {
      if (--depth <= 0)
        throw new IllegalStateException();
    }

    @Override
    public boolean isStarted() {
      return depth > 0;
    }

    @Override
    public boolean isStopped() {
      return depth <= 0;
    }

    @Override
    public void merge(HitLatencyCounter counter) {
    }

    @Override
    public void reset() {
      depth = 0;
    }

    @Override
    public String toString() {
      return NA;
    }

    @Override
    public CharBuffer toString(CharBuffer buf) {
      return buf.append(NA);
    }

  };

  /**
   * Returns new {@link HitLatencyCounter} instance.
   *
   * @return New {@link HitLatencyCounter} instance.
   */
  public static HitLatencyCounter defaultHitLatencyCounter() {
    return new DefaultHitLatencyCounter();
  }

  // AbstractHitLatencyCounter

  /**
   * Abstract implementation of the {@link HitLatencyCounter}.
   *
   * @author Fox Mulder
   * @see HitLatencyCounter
   * @see DefaultHitLatencyCounter
   * @see ConcurrentHitLatencyCounter
   */
  private static abstract class AbstractHitLatencyCounter extends ToString.Adapter implements HitLatencyCounter {

    /**
     * Returns total number of invocations (hits + misses).
     *
     * @return Total number of invocations (hits + misses).
     */
    @Override
    public long getInvocationCount() {
      return getHitCount() + getMissCount();
    }

    /**
     * Returns minimum latency time over all the invocations in milliseconds.
     *
     * @return Minimum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMinLatency() {
      return getHitMinLatency() < getMissMinLatency() ? getHitMinLatency() : getMissMinLatency();
    }

    /**
     * Returns maximum latency time over all the invocations in milliseconds.
     *
     * @return Maximum latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMaxLatency() {
      return getHitMaxLatency() > getMissMaxLatency() ? getHitMaxLatency() : getMissMaxLatency();
    }

    /**
     * Returns average latency time over all the invocations in milliseconds.
     *
     * @return Average latency time over all the invocations in milliseconds.
     */
    @Override
    public long getAverageLatency() {
      return getInvocationCount() > 0L ? getTotalLatency() / getInvocationCount() : 0L;
    }

    /**
     * Returns total latency time over all the invocations in milliseconds.
     *
     * @return Total latency time over all the invocations in milliseconds.
     */
    @Override
    public long getTotalLatency() {
      return getHitTotalLatency() + getMissTotalLatency();
    }

    /**
     * Returns rate of hits (hits / invocations).
     *
     * @return Rate of hits (hits / invocations).
     */
    @Override
    public double getHitRate() {
      return getInvocationCount() > 0L ? (double) getHitCount() / (double) getInvocationCount() : 0.0;
    }

    /**
     * Returns average hits latency time over all the invocations in milliseconds.
     *
     * @return Average hits latency time over all the invocations in milliseconds.
     */
    @Override
    public long getHitAverageLatency() {
      return getHitCount() > 0L ? getHitTotalLatency() / getHitCount() : 0L;
    }

    /**
     * Returns rate of misses (misses / invocations).
     *
     * @return Rate of misses (misses / invocations).
     */
    @Override
    public double getMissRate() {
      return getInvocationCount() > 0L ? (double) getMissCount() / (double) getInvocationCount() : 0.0;
    }

    /**
     * Returns average misses latency time over all the invocations in milliseconds.
     *
     * @return Average misses latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMissAverageLatency() {
      return getMissCount() > 0L ? getMissTotalLatency() / getMissCount() : 0L;
    }

    /**
     * Starts next measurement.
     *
     * @see #start(long)
     */
    @Override
    public void start() {
      start(System.currentTimeMillis());
    }

    /**
     * Starts next measurement using the specified start time.
     *
     * @param time Start time.
     */
    protected abstract void start(long time);

    /**
     * Stops measurement and updates statistics.
     *
     * @param hit Determines whether hits or misses value should be incremented.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     * @see #stop(long, boolean)
     */
    @Override
    public void stop(boolean hit) {
      stop(System.currentTimeMillis(), hit);
    }

    /**
     * Stops measurement using the specified stop time and updates statistics.
     *
     * @param time Stop time.
     * @param hit Determines whether hits or misses value should be incremented.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     */
    protected abstract void stop(long time, boolean hit);

    /**
     * Appends string representation of current state of this counter to the
     * specified buffer. The format is <code>INVOCATION_COUNT / TOTAL_LATENCY
     * (+HIT_COUNT HIT_RATE% HIT_TOTAL_LATENCY ~HIT_AVERAGE_LATENCY |
     * -MISS_COUNT MISS_RATE% MISS_TOTAL_LATENCY ~MISS_AVERAGE_LATENCY)</code>.
     *
     * @param buf Buffer to append.
     * @return The specified buffer.
     * @see #formatLatency(long, StringBuilder)
     */
    @Override
    public CharBuffer toString(CharBuffer buf) {
      buf.appendLong(getInvocationCount());
      formatLatency(getTotalLatency(), buf.append(" / "));
      formatRate(getHitRate(), buf.append(" (+").appendLong(getHitCount()).append(" "));
      formatLatency(getHitTotalLatency(), buf.append(" "));
      formatLatency(getHitAverageLatency(), buf.append(" ~"));
      formatRate(getMissRate(), buf.append(" | -").appendLong(getMissCount()).append(" "));
      formatLatency(getMissTotalLatency(), buf.append(" "));
      formatLatency(getMissAverageLatency(), buf.append(" ~"));
      return buf.append(")");
    }

  }

  /**
   * Default non thread-safe implementation of the {@link HitLatencyCounter}.
   *
   * @author Fox Mulder
   * @see AbstractHitLatencyCounter
   */
  private static final class DefaultHitLatencyCounter extends AbstractHitLatencyCounter {

    /**
     * Current recursion depth.
     */
    private int currentDepth;

    /**
     * Maximum recursion depth.
     */
    private int maxDepth;

    /**
     * Number of hits.
     */
    private long hitCount;

    /**
     * Minimum hits latency time over all the invocations in milliseconds.
     */
    private long hitMinLatency;

    /**
     * Maximum hits latency time over all the invocations in milliseconds.
     */
    private long hitMaxLatency;

    /**
     * Total hits latency time over all the invocations in milliseconds.
     */
    private long hitTotalLatency;

    /**
     * Number of misses.
     */
    private long missCount;

    /**
     * Minimum misses latency time over all the invocations in milliseconds.
     */
    private long missMinLatency;

    /**
     * Maximum misses latency time over all the invocations in milliseconds.
     */
    private long missMaxLatency;

    /**
     * Total misses latency time over all the invocations in milliseconds.
     */
    private long missTotalLatency;

    /**
     * Head time node.
     */
    private TimeNode head;

    /**
     * Returns maximum recursion depth.
     *
     * @return Maximum recursion depth.
     */
    @Override
    public int getMaxDepth() {
      return maxDepth;
    }

    /**
     * Returns number of hits.
     *
     * @return Number of hits.
     */
    @Override
    public long getHitCount() {
      return hitCount;
    }

    /**
     * Returns minimum hits latency time over all the invocations in milliseconds.
     *
     * @return Minimum hits latency time over all the invocations in milliseconds.
     */
    @Override
    public long getHitMinLatency() {
      return hitMinLatency;
    }

    /**
     * Returns maximum hits latency time over all the invocations in milliseconds.
     *
     * @return Maximum hits latency time over all the invocations in milliseconds.
     */
    @Override
    public long getHitMaxLatency() {
      return hitMaxLatency;
    }

    /**
     * Returns total hits latency time over all the invocations in milliseconds.
     *
     * @return Total hits latency time over all the invocations in milliseconds.
     */
    @Override
    public long getHitTotalLatency() {
      return hitTotalLatency;
    }

    /**
     * Returns number of misses.
     *
     * @return Number of misses.
     */
    @Override
    public long getMissCount() {
      return missCount;
    }

    /**
     * Returns minimum misses latency time over all the invocations in milliseconds.
     *
     * @return Minimum misses latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMissMinLatency() {
      return missMinLatency;
    }

    /**
     * Returns maximum misses latency time over all the invocations in milliseconds.
     *
     * @return Maximum misses latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMissMaxLatency() {
      return missMaxLatency;
    }

    /**
     * Returns total misses latency time over all the invocations in milliseconds.
     *
     * @return Total misses latency time over all the invocations in milliseconds.
     */
    @Override
    public long getMissTotalLatency() {
      return missTotalLatency;
    }

    /**
     * Starts next measurement using the specified start time.
     *
     * @param time Start time.
     */
    @Override
    protected void start(long time) {
      currentDepth++;
      if (maxDepth < currentDepth) {
        maxDepth = currentDepth;
      }
      head = new TimeNode(head, time);
    }

    /**
     * Stops measurement using the specified stop time and updates statistics.
     *
     * @param time Stop time.
     * @param hit Determines whether hits or misses value should be incremented.
     * @throws IllegalStateException if corresponding {@link #start()} method
     *         was not called before.
     */
    @Override
    protected void stop(long time, boolean hit) {
      if (head == null) {
        throw new IllegalStateException();
      } else {
        currentDepth--;
        long latency = time - head.time;
        if (hit) {
          hitCount++;
          hitTotalLatency += latency;
          if (hitMinLatency > latency || hitMinLatency == 0L) {
            hitMinLatency = latency;
          }
          if (hitMaxLatency < latency || hitMaxLatency == 0L) {
            hitMaxLatency = latency;
          }
        } else {
          missCount++;
          missTotalLatency += latency;
          if (missMinLatency > latency || missMinLatency == 0L) {
            missMinLatency = latency;
          }
          if (missMaxLatency < latency || missMaxLatency == 0L) {
            missMaxLatency = latency;
          }
        }
        head = head.next;
      }
    }

    /**
     * Determines whether this counter is started (the {@link #start()} method
     * was called and no corresponding {@link #stop(boolean)} method called
     * yet).
     *
     * @return <code>true</code> if this counter was started;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isStarted() {
      return head != null;
    }

    /**
     * Determines whether this counter is stopped (the {@link #stop(boolean)}
     * method was called).
     *
     * @return <code>true</code> if this counter was stopped;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isStopped() {
      return head == null;
    }

    /**
     * Merges statistics of the specified counter into this counter.
     *
     * @param counter Counter which statistics should be merged into this counter.
     */
    @Override
    public void merge(HitLatencyCounter counter) {
      hitCount += counter.getHitCount();
      hitTotalLatency += counter.getHitTotalLatency();
      if (hitMinLatency > counter.getHitMinLatency() || hitMinLatency == 0L) {
        hitMinLatency = counter.getHitMinLatency();
      }
      if (hitMaxLatency < counter.getHitMaxLatency() || hitMaxLatency == 0L) {
        hitMaxLatency = counter.getHitMaxLatency();
      }

      missCount += counter.getMissCount();
      missTotalLatency += counter.getMissTotalLatency();
      if (missMinLatency > counter.getMissMinLatency() || missMinLatency == 0L) {
        missMinLatency = counter.getMissMinLatency();
      }
      if (missMaxLatency < counter.getMissMaxLatency() || missMaxLatency == 0L) {
        missMaxLatency = counter.getMissMaxLatency();
      }
    }

    /**
     * Resets statistics of this counter.
     */
    @Override
    public void reset() {
      currentDepth = maxDepth = 0;
      hitCount = hitMinLatency = hitMaxLatency = hitTotalLatency = 0L;
      missCount = missMinLatency = missMaxLatency = missTotalLatency = 0L;
      head = null;
    }

  }

  // TimeNode

  /**
   * Node that stores time in milliseconds.
   *
   * @author Fox Mulder
   * @see DefaultLatencyCounter
   */
  private static final class TimeNode {

    /**
     * Next time node.
     */
    private final TimeNode next;

    /**
     * Time in milliseconds.
     */
    private final long time;

    /**
     * Constructs new time node with the specified next node and time.
     *
     * @param next Next time node.
     * @param time Time in milliseconds.
     */
    private TimeNode(TimeNode next, long time) {
      this.next = next;
      this.time = time;
    }

  }

  // Utility methods

  /**
   * Returns string representation of the specified rate value. The format is
   * <code>00.00%</code>.
   *
   * @param rate Rate value.
   * @return String representation of the specified rate value.
   * @throws IllegalArgumentException if the specified rate is not in range [0..1].
   * @see #formatRate(double, StringBuilder)
   */
  public static String formatRate(double rate) {
    return formatRate(rate, new CharBuffer()).toString();
  }

  /**
   * Appends string representation of the specified rate value to the specified
   * buffer. The format is <code>00.00%</code>.
   *
   * @param rate Rate value.
   * @param buf Buffer to append.
   * @throws IllegalArgumentException if the specified rate is not in range [0..1].
   * @return String representation of the specified rate value.
   */
  public static CharBuffer formatRate(double rate, CharBuffer buf) {
    if (rate < 0.0 || rate > 1.0 || Double.isNaN(rate) || Double.isInfinite(rate)) {
      throw new IllegalArgumentException();
    } else {
      int value = (int) Math.round(rate * 10000.0);
      int integer = value / 100;
      int decimal = value % 100;

      if (integer < 10) {
        buf.append('0');
      }
      buf.appendInt(integer).append('.');
      if (decimal < 10) {
        buf.append('0');
      }
      return buf.appendInt(decimal).append('%');
    }
  }

  /**
   * Returns string representation of the specified latency time. The format is
   * <code>[[[DAYS:]HOURS:]MINUTES:]SECONDS.MILLIS</code>.
   *
   * @param latency Latency time in milliseconds.
   * @return String representation of the specified latency time.
   * @throws IllegalArgumentException if the specified latency time is negative.
   * @see #formatLatency(long, StringBuilder)
   */
  public static String formatLatency(long latency) {
    return formatLatency(latency, new CharBuffer()).toString();
  }

  /**
   * Appends string representation of the specified latency time to the
   * specified buffer. The format is <code>[[[DAYS:]HOURS:]MINUTES:]SECONDS.MILLIS</code>.
   *
   * @param latency Latency time in milliseconds.
   * @param buf Buffer to append.
   * @return The specified buffer.
   * @throws IllegalArgumentException if the specified latency time is negative.
   */
  public static CharBuffer formatLatency(long latency, CharBuffer buf) {
    if (latency < 0L) {
      throw new IllegalArgumentException(Long.toString(latency));
    } else {
      long days = latency / ONE_DAY;
      long hours = (latency %= ONE_DAY) / ONE_HOUR;
      long minutes = (latency %= ONE_HOUR) / ONE_MINUTE;
      long seconds = (latency %= ONE_MINUTE) / ONE_SECOND;
      long millis = latency %= ONE_SECOND;

      boolean appended = false;
      if (days > 0L) {
        buf.appendDec(days).append(':');
        appended = true;
      }
      if (appended || hours > 0L) {
        if (hours < 10L) {
          buf.append('0');
        }
        buf.appendLong(hours).append(':');
        appended = true;
      }
      if (appended || minutes > 0L) {
        if (minutes < 10L) {
          buf.append('0');
        }
        buf.appendDec(minutes).append(':');
        appended = true;
      }
      if (appended && seconds < 10L) {
        buf.append('0');
      }
      buf.appendDec(seconds).append('.');
      if (millis < 100L) {
        buf.append('0');
      }
      if (millis < 10L) {
        buf.append('0');
      }
      return buf.appendLong(millis);
    }
  }

  // Time unit constants
  private static final long ONE_SECOND = 1000L;

  private static final long ONE_MINUTE = 60L * ONE_SECOND;

  private static final long ONE_HOUR = 60L * ONE_MINUTE;

  private static final long ONE_DAY = 24L * ONE_HOUR;

  // N/A
  private static final String NA = "N/A";

  public static void main(String[] args) {
    System.out.println(formatRate(0.5678));
  }

}
