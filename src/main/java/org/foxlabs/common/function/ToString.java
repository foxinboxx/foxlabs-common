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

package org.foxlabs.common.function;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.function.Consumer;

import org.foxlabs.common.Predicates;
import org.foxlabs.common.exception.ThresholdReachedException;

/**
 * An interface that allows to build a long sequence of text data avoiding string concatenations
 * (i.e. {@code String + String} or {@link String#concat(String)}).
 *
 * <p>For example, it is efficient to build a resulting string from a collection of objects which
 * classes implement this interface.</p>
 *
 * So, instead of:
 * <pre>
 * final Collection&lt;?&gt; collection = ...
 * final StringBuilder buffer = new StringBuilder();
 * collection.forEach((element) -> buffer.append(element.toString()));
 * System.out.println(buffer);
 * </pre>
 *
 * You may use:
 * <pre>
 * final Collection&lt;? extends ToString&gt; collection = ...
 * final ToString.Builder buffer = new ToString.Builder();
 * // Each element does not create a new string like toString() method does,
 * // but appends string representation directly to the buffer
 * collection.forEach((element) -> element.toString(buffer));
 * System.out.println(buffer);
 * </pre>
 *
 * @author Fox Mulder
 * @see ToString.Adapter
 * @see ToString.Builder
 */
@FunctionalInterface
public interface ToString {

  /**
   * Appends some content to the underlying buffer of the specified {@code Builder} and returns a
   * reference to the same {@code Builder}.
   *
   * @param builder The {@code Builder} to append to.
   * @return A reference to the specified {@code Builder}.
   */
  Builder toString(Builder builder);

  /**
   * Creates a new {@code Builder} with the specified underlying buffer, appends some content to it
   * and returns a reference to the created {@code Builder}.
   *
   * <p>
   * This is a shortcut for the:
   * <code>
   * toString(new Builder())
   * </code>
   * </p>
   *
   * @param buffer The underlying buffer to append to.
   * @return A reference to the created {@code Builder}.
   * @throws NullPointerException if the specified underlying buffer is {@code null}.
   * @see #toString(StringBuilder)
   */
  default Builder toString(StringBuilder buffer) {
    return toString(new Builder(buffer));
  }

  /**
   * Creates a new {@code Builder} with the specified underlying buffer and threshold, appends some
   * content to it and returns a reference to the created {@code Builder}.
   *
   * <p>
   * This is a shortcut for the:
   * <code>
   * toString(new Builder(buffer, threshold))
   * </code>
   * </p>
   *
   * @param buffer The underlying buffer to append to.
   * @param threshold The threshold for length of the underlying buffer.
   * @return A reference to the created {@code Builder}.
   * @throws NullPointerException if the specified underlying buffer is {@code null}.
   * @throws IllegalArgumentException if the specified threshold is negative.
   * @see #toString(StringBuilder)
   */
  default Builder toString(StringBuilder buffer, int threshold) {
    return toString(new Builder(buffer, threshold));
  }

  // Adapter

  /**
   * An abstract {@code ToString} implementation which overrides the {@link Object#toString()}
   * method that calls {@link #toString(Builder)} with an empty {@link Builder} and returns the
   * resulting string (i.e. {@code toString(new Builder()).build().toString()}).
   *
   * @author Fox Mulder
   */
  public static abstract class Adapter implements ToString {
    @Override public String toString() {
      try {
        return toString(new Builder()).build().toString();
      } catch (ThresholdReachedException e) {
        return e.getProducer().toString();
      }
    }
  }

  // Builder

  /**
   * A wrapper around the {@code StringBuilder} that permits append operations only. This builder
   * guarantees that length of the underlying buffer never exceeds a given threshold.
   *
   * @author Fox Mulder
   */
  public static class Builder implements Buildable<StringBuilder> {

    /**
     * The maximum threshold for length of the underlying buffer. Some VMs reserve some header
     * words in an array, so the maximum threshold is {@code Integer.MAX_VALUE - 8}.
     */
    public static final int MAX_THRESHOLD = Integer.MAX_VALUE - 8;

    /**
     * The threshold for length of the underlying buffer useful for logging purposes.
     */
    public static final int LOG_THRESHOLD = 4096;

    /**
     * The string representation of the {@code null} reference.
     */
    public static final String NULL_REFERENCE = "null";

    /**
     * The string representation of an empty array or {@code Iterable} sequence.
     */
    public static final String EMPTY_SEQUENCE = "[]";

    /**
     * The string representation of an empty map.
     */
    public static final String EMPTY_MAP = "{}";

    /**
     * The underlying buffer to append to.
     */
    private final StringBuilder buffer;

    /**
     * The threshold for length of the underlying buffer.
     */
    private final int threshold;

    /**
     * The objects cross-reference map to detect circular references.
     */
    private IdentityHashMap<Object, StringBuilder> crossrefs;

    /**
     * Constructs a new {@code Builder} with an empty underlying buffer and the highest possible
     * threshold.
     *
     * @see #Builder(StringBuilder, int)
     */
    public Builder() {
      this(MAX_THRESHOLD);
    }

    /**
     * Constructs a new {@code Builder} with an empty underlying buffer and the specified
     * threshold.
     *
     * @param threshold The threshold for length of the underlying buffer.
     * @throws IllegalArgumentException if the specified threshold is negative.
     * @see #Builder(StringBuilder, int)
     */
    public Builder(int threshold) {
      this(new StringBuilder(), threshold);
    }

    /**
     * Constructs a new {@code Builder} with the specified underlying buffer and the highest
     * possible threshold.
     *
     * @param buffer The underlying buffer to append to.
     * @see #Builder(StringBuilder, int)
     */
    public Builder(StringBuilder buffer) {
      this(buffer, MAX_THRESHOLD);
    }

    /**
     * Constructs a new {@code Builder} with the specified underlying buffer and threshold.
     *
     * @param buffer The underlying buffer to append to.
     * @param threshold The threshold for length of the underlying buffer.
     * @throws NullPointerException if the specified underlying buffer is {@code null}.
     * @throws IllegalArgumentException if the specified threshold is negative.
     */
    public Builder(StringBuilder buffer, int threshold) {
      this.buffer = Predicates.requireNonNull(buffer);
      this.threshold = Math.min(MAX_THRESHOLD,
          Predicates.require(threshold, Predicates.INT_POSITIVE));
    }

    // Query operations

    public int threshold() {
      return threshold;
    }

    public int length() {
      return buffer.length();
    }

    public boolean isEmpty() {
      return buffer.length() == 0;
    }

    /**
     * Determines if further append operations are possible or the threshold has been reached and
     * content can no longer be added.
     *
     * @return {@code true} if the underlying buffer has capacity for further append operations;
     *         {@code false} if the threshold has been reached and content can no longer be added.
     */
    public boolean canAppend() {
      return buffer.length() < threshold;
    }

    // Append operations

    /**
     * Appends the specified {@code boolean} value to the underlying buffer.
     *
     * @param value The {@code boolean} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(boolean value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code byte} value to the underlying buffer.
     *
     * @param value The {@code byte} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(byte value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code short} value to the underlying buffer.
     *
     * @param value The {@code short} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(short value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code int} value to the underlying buffer.
     *
     * @param value The {@code int} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(int value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code long} value to the underlying buffer.
     *
     * @param value The {@code long} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(long value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code float} value to the underlying buffer.
     *
     * @param value The {@code float} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(float value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code double} value to the underlying buffer.
     *
     * @param value The {@code double} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(double value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code char} value to the underlying buffer.
     *
     * @param value The {@code char} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(char value) {
      return appendSafe((sb) -> sb.append(value));
    }

    /**
     * Appends the specified {@code CharSequence} value to the underlying buffer.
     *
     * @param value The {@code CharSequence} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(CharSequence value) {
      return appendSafe((sb) -> sb.append(value != null ? value : NULL_REFERENCE));
    }

    /**
     * Appends the specified {@code Enum<?>} value to the underlying buffer.
     *
     * @param value The {@code Enum<?>} value to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #appendSafe(Consumer)
     */
    public Builder append(Enum<?> value) {
      return appendSafe((sb) -> sb.append(value != null ? value.name() : NULL_REFERENCE));
    }

    /**
     * Appends the specified object to the underlying buffer.
     *
     * <p>
     * Note that this method may produce an infinite loop and cause the {@link StackOverflowError}
     * if the specified object does not implement the {@code ToString} interface and object's graph
     * has circular references.
     *
     * For example:
     * <pre>
     * public class CircularReferenceTest {
     *
     *   static class Foo {
     *     public Bar bar;
     *     &#64;Override public String toString() {
     *       return "Bar: " + bar.toString();
     *     }
     *   }
     *
     *   static class Bar {
     *     public Foo foo;
     *     &#64;Override public String toString() {
     *       return "Foo: " + foo.toString();
     *     }
     *   }
     *
     *   public static void main(String[] args) {
     *     Foo foo = new Foo();
     *     Bar bar = new Bar();
     *     foo.bar = bar;
     *     bar.foo = foo;
     *     // Infinite loop
     *     foo.toString();
     *     // Infinite loop as well
     *     new ToString.Builder().append(foo);
     *   }
     *
     * }
     * </pre>
     * </p>
     *
     * @param object The object to append to the underlying buffer.
     * @return A reference to this builder.
     */
    public Builder append(Object object) {
      // append the object depending on its type (in order of probability)
      if (object == null) {
        return append(NULL_REFERENCE);
      } else if (object instanceof ToString) {
        return ((ToString) object).toString(this);
      } else if (object instanceof CharSequence) {
        return append((CharSequence) object);
      } else if (object instanceof Integer) {
        return append(((Integer) object).intValue());
      } else if (object instanceof Long) {
        return append(((Long) object).longValue());
      } else if (object instanceof Double) {
        return append(((Double) object).doubleValue());
      } else if (object instanceof Float) {
        return append(((Float) object).floatValue());
      } else if (object instanceof Byte) {
        return append(((Byte) object).intValue());
      } else if (object instanceof Short) {
        return append(((Short) object).intValue());
      } else if (object instanceof Character) {
        return append(((Character) object).charValue());
      } else if (object instanceof Boolean) {
        return append(((Boolean) object).booleanValue());
      } else if (object instanceof Enum<?>) {
        return append(((Enum<?>) object));
      } else if (object instanceof Iterable<?>) {
        return append((Iterable<?>) object);
      } else if (object instanceof Map<?, ?>) {
        return append((Map<?, ?>) object);
      } else if (object instanceof Object[]) {
        return append((Object[]) object);
      } else if (object instanceof int[]) {
        return append((int[]) object);
      } else if (object instanceof long[]) {
        return append((long[]) object);
      } else if (object instanceof double[]) {
        return append((double[]) object);
      } else if (object instanceof float[]) {
        return append((float[]) object);
      } else if (object instanceof byte[]) {
        return append((byte[]) object);
      } else if (object instanceof short[]) {
        return append((short[]) object);
      } else if (object instanceof char[]) {
        return append((char[]) object);
      } else if (object instanceof boolean[]) {
        return append((boolean[]) object);
      }
      // unsupported object type
      return appendPlain(object);
    }

    // Bulk append operations

    /**
     * Appends all the elements of the specified {@code boolean[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code boolean[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(boolean)
     */
    public Builder append(boolean[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code byte[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code byte[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(byte)
     */
    public Builder append(byte[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code short[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code short[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(short)
     */
    public Builder append(short[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code int[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code int[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(int)
     */
    public Builder append(int[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code long[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code long[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(long)
     */
    public Builder append(long[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code float[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code float[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(float)
     */
    public Builder append(float[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code double[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code double[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(double)
     */
    public Builder append(double[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code char[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code char[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(char)
     */
    public Builder append(char[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[').append(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(", ").append(array[index]);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code Object[]} array to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null} then
     * {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code Object[]} array which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(Object)
     */
    public Builder append(Object[] array) {
      if (array == null) {
        return append(NULL_REFERENCE);
      } else if (array.length == 0) {
        return append(EMPTY_SEQUENCE);
      }
      append('[');
      // remember reference to the array and check for cross-reference
      if (pushReference(array)) {
        // cross-reference detected!
        // reference has already been added to the underlying buffer
        return this;
      }
      try {
        append(array[0]);
        for (int index = 1; index < array.length; index++) {
          append(", ").append(array[index]);
        }
      } finally {
        // forget reference to the array
        popReference(array);
      }
      return append(']');
    }

    /**
     * Appends all the elements of the specified {@code Iterable} sequence to the underlying buffer.
     * The format is {@code [E0, E1, ...]}. If the specified {@code Iterable} reference is
     * {@code null} then {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code Iterable} sequence which elements to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(Object)
     */
    public Builder append(Iterable<?> iterable) {
      // make sure that nothing is null to avoid NPE
      final Iterator<?> itr = iterable != null
          ? iterable.iterator()
          : null;
      if (itr == null) {
        return append(NULL_REFERENCE);
      } else if (!itr.hasNext()) {
        return append(EMPTY_SEQUENCE);
      }
      append('[');
      // remember reference to the iteration and check for cross-reference
      if (pushReference(iterable)) {
        // cross-reference detected!
        // reference has already been added to the underlying buffer
        return this;
      }
      try {
        append(itr.next());
        while (itr.hasNext()) {
          append(", ").append(itr.next());
        }
      } finally {
        // forget reference to the iteration
        popReference(iterable);
      }
      return append(']');
    }

    /**
     * Appends all the entries of the specified {@code Map} table to the underlying buffer. The
     * format is <code>{K0: V0, K1: V1, ...}</code>. If the specified {@code Map} reference is
     * {@code null} then {@link #NULL_REFERENCE} will be appended.
     *
     * @param array The {@code Map} table which entries to append to the underlying buffer.
     * @return A reference to this builder.
     * @see #append(Object)
     */
    public Builder append(Map<?, ?> map) {
      // make sure that nothing is null to avoid NPE
      final Set<? extends Map.Entry<?, ?>> entries = map != null
          ? map.entrySet()
          : null;
      final Iterator<? extends Map.Entry<?, ?>> itr = entries != null
          ? entries.iterator()
          : null;
      if (itr == null) {
        return append(NULL_REFERENCE);
      } else if (!itr.hasNext()) {
        return append(EMPTY_MAP);
      }
      append('{');
      // remember reference to the map and check for cross-reference
      if (pushReference(map)) {
        // cross-reference detected!
        // reference has already been added to the underlying buffer
        return this;
      }
      try {
        Map.Entry<?, ?> entry = itr.next();
        if (entry == null) { // who knows?
          append(NULL_REFERENCE);
        } else {
          append(", ").append(entry.getKey())
            .append(": ").append(entry.getValue());
        }
        while (itr.hasNext()) {
          entry = itr.next();
          if (entry == null) { // who knows?
            append(NULL_REFERENCE);
          } else {
            append(", ").append(entry.getKey())
              .append(": ").append(entry.getValue());
          }
        }
      } finally {
        // forget reference to the map
        popReference(map);
      }
      return append('}');
    }

    // Internal operations

    protected final Builder appendPlain(Object object) {
      // remember reference to the object and check for cross-reference
      if (pushReference(object)) {
        // cross-reference detected!
        // reference has already been added to the underlying buffer
        return this;
      }
      try {
        return appendSafe((sb) -> sb.append(object.toString()));
      } finally {
        // forget reference to the object
        popReference(object);
      }
    }

    protected final Builder appendSafe(Consumer<StringBuilder> appender) {
      if (buffer.length() < threshold) {
        try {
          appender.accept(buffer);
          if (buffer.length() > threshold) {
            buffer.setLength(threshold);
            throw new ThresholdReachedException(this);
          }
        } catch (OutOfMemoryError e) {
          final StringBuilder temp = new StringBuilder(0);
          appender.accept(temp);
          buffer.append(temp, 0, threshold - buffer.length());
        }
      }
      return this;
    }

    protected final boolean pushReference(Object object) {
      if (crossrefs == null) {
        crossrefs = new IdentityHashMap<>();
      }
      if (crossrefs.containsKey(object)) {
        StringBuilder crossref = crossrefs.get(object);
        if (crossref == null) {
          final String classname = object.getClass().getName();
          final String hashcode = Integer.toHexString(System.identityHashCode(object));
          crossref = new StringBuilder(classname.length() + hashcode.length() + 1)
              .append(classname).append('@').append(hashcode);
          crossrefs.put(object, crossref);
        }
        append(crossref);
        return true;
      }
      crossrefs.put(object, null);
      return false;
    }

    protected final void popReference(Object object) {
      crossrefs.remove(object);
    }

    // Object & Buildable contracts

    /**
       * Returns the underlying buffer. Note that this method does not create a copy of the
       * underlying buffer, so any further modifications applied to the returned buffer will be
       * reflected in the result of this {@code Builder}.
     *
     * @return The underlying buffer.
     */
    @Override
    public StringBuilder build() {
      return buffer;
    }

    /**
     * Returns a resulting string representation from content of the underlying buffer.
     *
     * @return A resulting string representation from content of the underlying buffer.
     */
    @Override
    public String toString() {
      return buffer.toString();
    }

    // Helpers

    public static String toString(Object object) {
      return toString(object, LOG_THRESHOLD);
    }

    public static String toString(Object object, int threshold) {
      try {
        return new Builder(threshold).append(object).toString();
      } catch (ThresholdReachedException e) {
        return e.getProducer().toString();
      }
    }

  }

}
