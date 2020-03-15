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

package org.foxlabs.common.text;

import java.util.Set;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.Iterator;

import org.foxlabs.common.function.ToString;
import org.foxlabs.common.function.CharacterSequence;
import org.foxlabs.common.exception.ThresholdReachedException;

import static org.foxlabs.common.Predicates.*;
import static org.foxlabs.common.Predicates.ExceptionProvider.*;

public class CharacterBuffer {

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE;

  public static final int LOG_THRESHOLD = 4096;

  public static final int MIN_DEPTH = 1 << 5; // 32 characters long (64 bytes)

  public static final int MAX_DEPTH = 1 << 15; // 32K characters long (64K bytes)

  private final int threshold;

  private final int depth;

  private final int capacity;

  private char[][] buffer;

  private int length;

  public CharacterBuffer() {
    this(MAX_THRESHOLD, MIN_DEPTH);
  }

  public CharacterBuffer(int threshold) {
    this(threshold, MIN_DEPTH);
  }

  public CharacterBuffer(int threshold, int depth) {
    this.threshold = require(threshold, INT_POSITIVE);
    // round depth to be a multiple of 32 and trim it to maximum possible
    this.depth = Math.min((((require(depth, INT_POSITIVE) - 1) >> 5) + 1) << 5, MAX_DEPTH);
    // calculate maximum number of slots
    this.capacity = (this.threshold - 1) / this.depth + 1;
    // allocate at most 16 initial slots depending on the capacity
    this.buffer = new char[Math.min(this.capacity, 16)][];
  }

  // Query operations

  /**
   * Returns the threshold of the buffer (i.e. the maximum number of characters that the buffer can
   * contain).
   *
   * @return The threshold of the buffer.
   */
  public final int threshold() {
    return threshold;
  }

  /**
   * Returns the current number of characters that have already been appended to the buffer (i.e.
   * the current length of the buffer).
   *
   * @return The current number of characters that have already been appended to the buffer.
   */
  public final int length() {
    return length;
  }

  /**
   * Returns the remaining number of characters that can be appended to the buffer until the
   * {@link ThresholdReachedException} will be thrown.
   *
   * @return The remaining number of characters that can be appended to the buffer.
   */
  public final int remaining() {
    return threshold - length;
  }

  // Core operations

  public final CharacterBuffer append(char value) {
    ensureCapacity(1);
    nextSlot()[length++ % depth] = value;
    return this;
  }

  public final CharacterBuffer append(char... values) {
    return appendSafe(CharacterSequence.of(values), 0, values.length);
  }

  public final CharacterBuffer append(char[] values, int start) {
    require(requireNonNull(values), checkCharArrayRange(start), ofIOOB(start));
    return appendSafe(CharacterSequence.of(values), start, values.length);
  }

  public final CharacterBuffer append(char[] values, int start, int end) {
    require(requireNonNull(values), checkCharArrayRange(start, end), ofIOOB(start, end));
    return appendSafe(CharacterSequence.of(values), start, end);
  }

  public final CharacterBuffer append(CharSequence sequence) {
    return appendSafe(CharacterSequence.of(sequence), 0, sequence.length());
  }

  public final CharacterBuffer append(CharSequence sequence, int start) {
    require(requireNonNull(sequence), checkCharSequenceRange(start), ofIOOB(start));
    return appendSafe(CharacterSequence.of(sequence), start, sequence.length());
  }

  public final CharacterBuffer append(CharSequence sequence, int start, int end) {
    require(requireNonNull(sequence), checkCharSequenceRange(start, end), ofIOOB(start, end));
    return appendSafe(CharacterSequence.of(sequence), start, sequence.length());
  }

  protected final CharacterBuffer appendSafe(CharacterSequence sequence, int start, int end) {
    // calculate the number of characters to append
    int count = end - start;
    if (count > 0) { // fast check
      for (count = ensureCapacity(count); count > 0;) {
        // copy part of the characters to the current slot
        final int offset = length % depth;
        final int remainder = Math.min(depth - offset, count);
        sequence.copy(start, start + remainder, nextSlot(), offset);
        length += remainder;
        start += remainder;
        count -= remainder;
      }
      if (start < end) {
        // Not all the characters have been appended
        throw new ThresholdReachedException(this);
      }
    }
    return this;
  }

  protected final int ensureCapacity(int count) {
    // trim count if it exceeds threshold
    long nlength = (long) length + (long) count; // avoid int overflow
    if (nlength > threshold) {
      count = (int) (nlength = threshold) - length;
      if (count == 0) { // fast check
        throw new ThresholdReachedException(this);
      }
    }
    // calculate total number of required slots
    final int nslots = ((int) nlength - 1) / depth + 1;
    if (nslots > buffer.length) {
      // extend buffer for new slots as x2 required slots
      final char[][] copy = new char[Math.min(nslots << 1, capacity)][];
      System.arraycopy(buffer, 0, copy, 0, (length - 1) / depth + 1);
      buffer = copy;
    }
    // return the actual number of characters that can be appended
    return count;
  }

  private final char[] nextSlot() {
    // allocate a new slot if necessary
    final int index = length / depth;
    return buffer[index] == null ? buffer[index] = new char[depth] : buffer[index];
  }

  // Advanced operations

  public CharacterBuffer appendEscaped(char value) {
    // TODO
    return this;
  }

  public CharacterBuffer appendEscaped(CharSequence value) {
    // TODO
    return this;
  }

  // Conversion operations

  private static final char CHAR_QUOTE = '\'';

  private static final char STRING_QUOTE = '\"';

  private static final char SEQUENCE_OPEN = '[';

  private static final char SEQUENCE_CLOSE = ']';

  private static final char MAP_OPEN = '{';

  private static final char MAP_CLOSE = '}';

  /**
   * The string representation of the {@code null} reference.
   */
  private static final char[] NULL_REFERENCE = {'n', 'u', 'l', 'l'};

  /**
   * The string representation of the {@code true} constant.
   */
  private static final char[] TRUE_CONSTANT = {'t', 'r', 'u', 'e'};

  /**
   * The string representation of the {@code false} constant.
   */
  private static final char[] FALSE_CONSTANT = {'f', 'a', 'l', 's', 'e'};

  /**
   * The string representation of an empty array or {@code Iterable} sequence.
   */
  private static final char[] EMPTY_SEQUENCE = {SEQUENCE_OPEN, SEQUENCE_CLOSE};

  /**
   * The string representation of an empty map.
   */
  private static final char[] EMPTY_MAP = {MAP_OPEN, MAP_CLOSE};

  /**
   * The string representation of elements separator in an array or {@code Iterable} sequence.
   */
  private static final char[] ELEMENT_SEPARATOR = {',', ' '};

  /**
   * The string representation of key value pair separator in a map.
   */
  private static final char[] KEY_VALUE_SEPARATOR = {':', ' '};

  /**
   * The objects cross-reference map to detect circular references.
   */
  private IdentityHashMap<Object, char[]> crossrefs;

  /**
   * Appends string representation of the {@code null} reference to the buffer.
   *
   * @return A reference to this buffer.
   */
  public CharacterBuffer appendNull() {
    return append(NULL_REFERENCE);
  }

  /**
   * Appends string representation of the specified object to the buffer.
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
   * @param object The object to append to the buffer.
   * @return A reference to this buffer.
   */
  public CharacterBuffer appendObject(Object object) {
    // append the object depending on its type (in order of probability)
    if (object == null) {
      return appendNull();
    } else if (object instanceof CharSequence) {
      return appendString((CharSequence) object);
    } else if (object instanceof Integer) {
      return appendInt(((Integer) object).intValue());
    } else if (object instanceof Long) {
      return appendLong(((Long) object).longValue());
    } else if (object instanceof Double) {
      return appendDouble(((Double) object).doubleValue());
    } else if (object instanceof Float) {
      return appendFloat(((Float) object).floatValue());
    } else if (object instanceof Byte) {
      return appendByte(((Byte) object).byteValue());
    } else if (object instanceof Short) {
      return appendShort(((Short) object).shortValue());
    } else if (object instanceof Character) {
      return appendChar(((Character) object).charValue());
    } else if (object instanceof Boolean) {
      return appendBoolean(((Boolean) object).booleanValue());
    } else if (object instanceof Enum<?>) {
      return appendEnum(((Enum<?>) object));
    } else if (object instanceof Iterable<?>) {
      return appendIterable((Iterable<?>) object);
    } else if (object instanceof Map<?, ?>) {
      return appendMap((Map<?, ?>) object);
    } else if (object instanceof Object[]) {
      return appendObjectArray((Object[]) object);
    } else if (object instanceof int[]) {
      return appendIntArray((int[]) object);
    } else if (object instanceof long[]) {
      return appendLongArray((long[]) object);
    } else if (object instanceof double[]) {
      return appendDoubleArray((double[]) object);
    } else if (object instanceof float[]) {
      return appendFloatArray((float[]) object);
    } else if (object instanceof byte[]) {
      return appendByteArray((byte[]) object);
    } else if (object instanceof short[]) {
      return appendShortArray((short[]) object);
    } else if (object instanceof char[]) {
      return appendCharArray((char[]) object);
    } else if (object instanceof boolean[]) {
      return appendBooleanArray((boolean[]) object);
    }
    // unsupported object type
    return appendPlain(object);
  }

  /**
   * Appends string representation of all the elements of the specified {@code Object[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Object[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharacterBuffer appendObjectArray(Object[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN);
    // remember reference to the array and check for cross-reference
    if (pushReference(array)) {
      // cross-reference detected!
      // reference has already been added to the underlying buffer
      return this;
    }
    try {
      appendObject(array[0]);
      for (int index = 1; index < array.length; index++) {
        append(ELEMENT_SEPARATOR).appendObject(array[index]);
      }
    } finally {
      // forget reference to the array
      popReference(array);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code boolean} value to the buffer.
   *
   * @param value The {@code boolean} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char...)
   */
  public CharacterBuffer appendBoolean(boolean value) {
    return value ? append(TRUE_CONSTANT) : append(FALSE_CONSTANT);
  }

  /**
   * Appends string representation of all the elements of the specified {@code boolean[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code boolean[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendBoolean(boolean)
   */
  public CharacterBuffer appendBooleanArray(boolean[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendBoolean(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendBoolean(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code byte} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code byte} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendByte(byte value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code byte[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code byte[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendByte(byte)
   */
  public CharacterBuffer appendByteArray(byte[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendByte(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendByte(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code short} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code short} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendShort(short value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code short[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code short[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendShort(short)
   */
  public CharacterBuffer appendShortArray(short[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendShort(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendShort(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code int} value to the buffer using the
   * {@link Integer#toString(int)} method.
   *
   * @param value The {@code int} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendInt(int value) {
    return append(Integer.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code int[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code int[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendInt(int)
   */
  public CharacterBuffer appendIntArray(int[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendInt(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendInt(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code long} value to the buffer using the
   * {@link Long#toString(long)} method.
   *
   * @param value The {@code long} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendLong(long value) {
    return append(Long.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code long[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code long[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendLong(long)
   */
  public CharacterBuffer appendLongArray(long[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendLong(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendLong(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code float} value to the buffer using the
   * {@link Float#toString(float)} method.
   *
   * @param value The {@code float} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendFloat(float value) {
    return append(Float.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code float[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code float[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendFloat(float)
   */
  public CharacterBuffer appendFloatArray(float[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendFloat(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendFloat(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code double} value to the buffer using the
   * {@link Double#toString(double)} method.
   *
   * @param value The {@code double} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendDouble(double value) {
    return append(Double.toString(value));
  }

  /**
   * Appends string representation of all the elements of the specified {@code double[]} array to
   * the buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is
   * {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code double[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendDouble(double)
   */
  public CharacterBuffer appendDoubleArray(double[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendDouble(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendDouble(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code char} value to the buffer. The format is
   * {@code '<CHAR>'}.
   *
   * @param value The {@code char} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char)
   * @see #appendEscaped(char)
   */
  public CharacterBuffer appendChar(char value) {
    return append(CHAR_QUOTE).appendEscaped(value).append(CHAR_QUOTE);
  }

  /**
   * Appends string representation of all the elements of the specified {@code char[]} array to the
   * buffer. The format is {@code [E0, E1, ...]}. If the specified array reference is {@code null}
   * then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code char[]} array which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendChar(char)
   */
  public CharacterBuffer appendCharArray(char[] array) {
    if (array == null) {
      return appendNull();
    } else if (array.length == 0) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN).appendChar(array[0]);
    for (int index = 1; index < array.length; index++) {
      append(ELEMENT_SEPARATOR).appendChar(array[index]);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of the specified {@code CharSequence} value to the buffer. The
   * format is {@code "<SEQUENCE>"}.
   *
   * @param value The {@code CharSequence} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(char)
   * @see #appendEscaped(CharSequence)
   */
  public CharacterBuffer appendString(CharSequence value) {
    return value != null ? append(STRING_QUOTE).appendEscaped(value).append(STRING_QUOTE) : appendNull();
  }

  /**
   * Appends string representation of the specified {@code Enum<?>} value to the buffer using the
   * {@link Enum#name()} method.
   *
   * @param value The {@code Enum<?>} value to append to the buffer.
   * @return A reference to this buffer.
   * @see #append(CharSequence)
   */
  public CharacterBuffer appendEnum(Enum<?> value) {
    return value != null ? append(value.name()) : appendNull();
  }

  /**
   * Appends string representation of all the elements of the specified {@code Iterable} sequence
   * to the buffer. The format is {@code [E0, E1, ...]}. If the specified {@code Iterable}
   * reference is {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Iterable} sequence which elements to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharacterBuffer appendIterable(Iterable<?> iterable) {
    // make sure that nothing is null to avoid NPE
    final Iterator<?> itr = iterable != null
        ? iterable.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      return append(EMPTY_SEQUENCE);
    }
    append(SEQUENCE_OPEN);
    // remember reference to the iteration and check for cross-reference
    if (pushReference(iterable)) {
      // cross-reference detected!
      // reference has already been added to the underlying buffer
      return this;
    }
    try {
      appendObject(itr.next());
      while (itr.hasNext()) {
        append(ELEMENT_SEPARATOR).appendObject(itr.next());
      }
    } finally {
      // forget reference to the iteration
      popReference(iterable);
    }
    return append(SEQUENCE_CLOSE);
  }

  /**
   * Appends string representation of all the entries of the specified {@code Map} table to the
   * buffer. The format is <code>{K0: V0, K1: V1, ...}</code>. If the specified {@code Map}
   * reference is {@code null} then result of the {@link #appendNull()} will be appended.
   *
   * @param array The {@code Map} table which entries to append to the buffer.
   * @return A reference to this buffer.
   * @see #appendObject(Object)
   */
  public CharacterBuffer appendMap(Map<?, ?> map) {
    // make sure that nothing is null to avoid NPE
    final Set<? extends Map.Entry<?, ?>> entries = map != null
        ? map.entrySet()
        : null;
    final Iterator<? extends Map.Entry<?, ?>> itr = entries != null
        ? entries.iterator()
        : null;
    if (itr == null) {
      return appendNull();
    } else if (!itr.hasNext()) { // fast check
      return append(EMPTY_MAP);
    }
    append(MAP_OPEN);
    // remember reference to the map and check for cross-reference
    if (pushReference(map)) {
      // cross-reference detected!
      // reference has already been added to the underlying buffer
      return this;
    }
    try {
      Map.Entry<?, ?> entry = itr.next();
      if (entry == null) { // who knows
        appendNull();
      } else {
        appendObject(entry.getKey());
        append(KEY_VALUE_SEPARATOR);
        appendObject(entry.getValue());
      }
      while (itr.hasNext()) {
        entry = itr.next();
        append(ELEMENT_SEPARATOR);
        if (entry == null) { // who knows
          appendNull();
        } else {
          appendObject(entry.getKey());
          append(KEY_VALUE_SEPARATOR);
          appendObject(entry.getValue());
        }
      }
    } finally {
      // forget reference to the map
      popReference(map);
    }
    return append(MAP_CLOSE);
  }

  public CharacterBuffer appendPlain(Object object) {
    if (object == null) {
      return appendNull();
    }
    // remember reference to the object and check for cross-reference
    if (pushReference(object)) {
      // cross-reference detected!
      // reference has already been added to the underlying buffer
      return this;
    }
    try {
      if (object instanceof ToString) {
        ((ToString) object).toString(this);
      } else {
        append(object.toString());
      }
      return this;
    } finally {
      // forget reference to the object
      popReference(object);
    }
  }

  protected final boolean pushReference(Object object) {
    if (crossrefs == null) {
      crossrefs = new IdentityHashMap<>();
    }
    if (crossrefs.containsKey(object)) {
      char[] crossref = crossrefs.get(object);
      if (crossref == null) {
        final String classname = object.getClass().getName();
        final String hashcode = Integer.toHexString(System.identityHashCode(object));
        crossref = new char[classname.length() + hashcode.length() + 2];
        classname.getChars(0, classname.length(), crossref, 1);
        hashcode.getChars(0, hashcode.length(), crossref, classname.length() + 2);
        crossref[classname.length() + 1] = '@';
        crossref[0] = '!';
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

  // Cleanup operations

  public final void reset() {
    length = 0;
  }

  public final void clear() {
    final int nslots = (length - 1) / depth + 1;
    for (int index = 0; index < nslots; index++) {
      buffer[index] = null;
    }
    length = 0;
  }

  // Helpers

  public static String toString(Object object) {
    return toString(object, LOG_THRESHOLD);
  }

  public static String toString(Object object, int threshold) {
    try {
      return new CharacterBuffer(threshold).appendObject(object).toString();
    } catch (ThresholdReachedException e) {
      return e.getProducer().toString();
    }
  }

}
