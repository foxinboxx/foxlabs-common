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

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.IntPredicate;

/**
 * A collection of utility methods that deal with the {@link Iterable}s and {@link Iterator}s.
 *
 * @author Fox Mulder
 */
public final class Iterators {

  // Instantiation is not possible
  private Iterators() {
    throw new IllegalAccessError();
  }

  // Array iterators

  /**
   * Returns an {@code Iterable} instance for the specified array of elements. Note that this
   * method does not create a copy of the original array, so any modifications will be reflected to
   * the results of iteration.
   *
   * @param <E> The type of the array elements.
   * @param elements An array of elements to iterate over.
   * @return A new {@code Iterable} instance that wraps the specified array of elements.
   * @throws NullPointerException if the specified array of elements is {@code null}.
   * @see #toIterator(Object...)
   */
  @SafeVarargs
  public static <E> Iterable<E> toIterable(E... elements) {
    if (elements.length == 0) {
      return Collections.emptyList();
    } else {
      return new Iterable<E>() {
        @Override public Iterator<E> iterator() {
          return toIterator(elements);
        }
      };
    }
  }

  /**
   * Returns an {code Iterator} instance for the specified array of elements. Note that this method
   * does not create a copy of the original array, so any modifications will be reflected to the
   * results of iteration. The {@link Iterator#remove()} method is not supported.
   *
   * @param <E> The type of the array elements.
   * @param elements An array of elements to iterate over.
   * @return A new {@code Iterator} instance that wraps the specified array of elements.
   * @throws NullPointerException if the specified array of elements is {@code null}.
   */
  @SafeVarargs
  public static <E> Iterator<E> toIterator(E... elements) {
    if (elements.length == 0) {
      return Collections.emptyIterator();
    } else {
      return new Iterator<E>() {
        int index = 0;
        @Override public boolean hasNext() {
          return index < elements.length;
        }
        @Override public E next() {
          return elements[index++];
        }
      };
    }
  }

  // Mapping iterators

  /**
   * Returns an {@code Iterable} instance that wraps the specified one and applies the specified
   * mapper function for each element during iteration.
   *
   * @param <S> The type of elements of the original iteration.
   * @param <T> The type of elements of the resulting iteration.
   * @param mapper A mapper function to be applied for each element during iteration.
   * @param iterable The {@code Iterable} instance to be wrapped.
   * @return A new {@code Iterable} instance that wraps the specified one.
   * @throws NullPointerException if the specified mapper function or {@code Iterable} instance is
   *         {@code null}.
   * @see #withMapper(Function, Iterator)
   */
  public static <S, T> Iterable<T> withMapper(Function<S, T> mapper, Iterable<S> iterable) {
    Predicates.requireNonNull(mapper);
    Predicates.requireNonNull(iterable);
    return new Iterable<T>() {
      @Override public Iterator<T> iterator() {
        return withMapper(mapper, iterable.iterator());
      }
    };
  }

  /**
   * Returns an {@code Iterator} instance that wraps the specified one and applies the specified
   * mapper function for each element during iteration.
   *
   * @param <S> The type of elements of the original iteration.
   * @param <T> The type of elements of the resulting iteration.
   * @param mapper A mapper function to be applied for each element during iteration.
   * @param iterator The {@code Iterator} instance to be wrapped.
   * @return A new {@code Iterator} instance that wraps the specified one.
   * @throws NullPointerException if the specified mapper function or {@code Iterator} instance is
   *         {@code null}.
   */
  public static <S, T> Iterator<T> withMapper(Function<S, T> mapper, Iterator<S> iterator) {
    Predicates.requireNonNull(mapper);
    Predicates.requireNonNull(iterator);
    return new Iterator<T>() {
      @Override public boolean hasNext() {
        return iterator.hasNext();
      }
      @Override public T next() {
        return mapper.apply(iterator.next());
      }
      @Override public void remove() {
        iterator.remove();
      }
    };
  }

  // Miscellaneous

  /**
   * Returns an {@code CodePointIterator} instance that provides iteration over code points of the
   * specified character sequence.
   *
   * @param cs The character sequence to iterate over.
   * @return A new {@link CodePointIterator} instance for the specified character sequence.
   * @throws NullPointerException if the specified character sequence is {@code null}.
   */
  public static CodePointIterator codePoints(CharSequence cs) {
    return new CodePointIterator(Predicates.requireNonNull(cs));
  }

  /**
   * The {@code Iterator} over code points of a character sequence. This is an equivalent to the
   * {@code CharSequence.codePoints().iterator()}, but defines a number of additional methods and
   * does not create heavy-weight {@link java.util.stream.IntStream}.
   *
   * <p>
   * In addition to methods of the {@code PrimitiveIterator.OfInt}, this class defines the
   * following ones:
   * <ul>
   *   <li>{@link #position()} - returns current position of the iteration.</li>
   *   <li>{@link #tryEachRemaining(IntPredicate)} - allows to immediately exit the iteration loop
   *       depending on the result of a predicate.</li>
   * </ul>
   * </p>
   *
   * @author Fox Mulder
   */
  public static final class CodePointIterator implements PrimitiveIterator.OfInt {

    /**
     * The iterable character sequence.
     */
    private final CharSequence cs;

    /**
     * The length of the character sequence.
     */
    private final int length;

    /**
     * The current position in the character sequence.
     */
    private int index;

    /**
     * Constructs a new {@code CodePointIterator} for the specified character
     * sequence.
     *
     * @param cs The iterable character sequence.
     */
    CodePointIterator(CharSequence cs) {
      this.length = (this.cs = cs).length();
    }

    /**
     * Returns current position in the character sequence.
     *
     * @return The current position in the character sequence.
     */
    public int position() {
      return index;
    }

    /**
     * Determines if the iteration has more code points.
     *
     * @return {@code true} if the iteration has more code points.
     */
    @Override
    public boolean hasNext() {
      return index < length;
    }

    /**
     * Returns next code point in the iteration.
     *
     * @return The next code point in the iteration.
     * @throws NoSuchElementException if the iteration has no more code points.
     */
    @Override
    public int nextInt() {
      if (index < length) {
        final char ch1 = cs.charAt(index++);
        if (Character.isHighSurrogate(ch1) && index < length) {
          final char ch2 = cs.charAt(index);
          if (Character.isLowSurrogate(ch2)) {
            index++;
            return Character.toCodePoint(ch1, ch2);
          }
        }
        return ch1;
      }
      throw new NoSuchElementException();
    }

    /**
     * Performs the specified action for each remaining code point until the end of character
     * sequence has been reached or the action returns {@code false} or throws an exception.
     *
     * @param action The predicate to be performed for each code point in the remaining iteration.
     * @return The current position in the character sequence.
     */
    public int tryEachRemaining(IntPredicate action) {
      Predicates.requireNonNull(action);
      while (hasNext()) {
        final int ch = nextInt();
        if (!action.test(ch)) {
          return index -= Character.charCount(ch);
        }
      }
      return index;
    }

  }

}
