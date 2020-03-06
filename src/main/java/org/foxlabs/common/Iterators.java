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
 *
 * @author Fox Mulder
 */
public final class Iterators {

  // Instantiation is not possible
  private Iterators() {
    throw new IllegalAccessError();
  }

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

  /**
   *
   */
  public static <S, T> Iterable<T> withMapper(Function<S, T> mapper, Iterable<S> iterable) {
    Objects.requireNonNull(mapper);
    Objects.requireNonNull(iterable);
    return new Iterable<T>() {
      @Override public Iterator<T> iterator() {
        return withMapper(mapper, iterable.iterator());
      }
    };
  }

  /**
   * Returns an {@link Iterator} instance which wraps the specified iterator
   * and applies the specified mapper function for each element of the original
   * iterator.
   *
   * @param <S> The type of the elements of the original iterator.
   * @param <T> The type of the elements of the resulting iterator.
   * @param mapper The mapper function to apply for elements of the original iterator.
   * @param iterator The original iterator to wrap.
   * @return A new {@link Iterator} instance which wraps the original one.
   * @throws NullPointerException if the specified mapper function or original
   *         iterator is {@code null}.
   */
  public static <S, T> Iterator<T> withMapper(Function<S, T> mapper, Iterator<S> iterator) {
    Objects.requireNonNull(mapper);
    Objects.requireNonNull(iterator);
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

  /**
   * Returns an iterator over code points of the specified character sequence.
   * Note that this method returns an empty iterator if the specified character
   * sequence is {@code null} or an empty (i.e. {@code cs.length() == 0}).
   *
   * @param cs The character sequence to iterate over.
   * @return The {@link CodePointIterator} over code points of the specified
   *         character sequence.
   */
  public static CodePointIterator codePoints(CharSequence cs) {
    return new CodePointIterator(cs == null ? "" : cs);
  }

  /**
   * The iterator over code points of a character sequence. This is an equivalent
   * to the {@code CharSequence.codePoints().iterator()}, but defines a number of
   * advanced methods and does not create heavy-weight {@link java.util.stream.IntStream}.
   *
   * <p>
   * In addition to the {@code PrimitiveIterator.OfInt} methods, this iterator:
   * <ul>
   *   <li>Defines the {@link #tryEachRemaining(IntPredicate)} method, which
   *   allows to immediately exit the iteration loop depending on the result of
   *   a predicate.</li>
   * </ul>
   * </p>
   *
   * @author Fox Mulder
   */
  public static class CodePointIterator implements PrimitiveIterator.OfInt {

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
     * Constructs a new code point iterator for the specified character sequence.
     *
     * @param cs The iterable character sequence.
     */
    CodePointIterator(CharSequence cs) {
      this.length = (this.cs = cs).length();
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
     * Performs the specified action for each remaining code point until the end
     * of the string has been reached or the action returns {@code false} or
     * throws an exception.
     *
     * @param action The predicate to be performed for each code point in the string.
     * @return The current position in the string.
     */
    public int tryEachRemaining(IntPredicate action) {
      Objects.requireNonNull(action);
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
