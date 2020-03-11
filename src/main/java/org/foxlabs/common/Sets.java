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

import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Comparator;
import java.util.Collections;

/**
 * A collection of utility methods that deal with the {@link Set}s.
 *
 * @author Fox Mulder
 */
public final class Sets {

  // Instantiation is not possible
  private Sets() {
    throw new IllegalAccessError();
  }

  // Array operations

  /**
   * Adds all the specified elements to the specified set. This is the same as the
   * {@code Collections.addAll(set, elements)}, but returns a reference to the specified set.
   *
   * @param <E> The type of elements of the set.
   * @param <S> The type of the set.
   * @param set The set to add new elements to.
   * @param elements The array of elements to be added to the set.
   * @return A reference to the specified set.
   * @throws NullPointerException if the specified set or elements is {@code null}.
   * @see Collections#addAll(java.util.Collection, Object...)
   */
  @SafeVarargs
  public static <E, S extends Set<E>> S addAll(S set, E... elements) {
    Predicates.requireNonNull(set);
    Predicates.requireNonNull(elements);
    Collections.addAll(set, elements);
    return set;
  }

  /**
   * Returns a new {@link HashSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new HashSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link HashSet} with the specified elements added.
   * @throws NullPointerException if the specified elements is {@code null}.
   * @see #addAll(Set, Object...)
   */
  @SafeVarargs
  public static <E> HashSet<E> toHashSet(E... elements) {
    return addAll(new HashSet<>(), elements);
  }

  /**
   * Returns a new {@link LinkedHashSet} with the specified elements added. This is a shortcut for
   * the {@code addAll(new LinkedHashSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link LinkedHashSet} with the specified elements added.
   * @throws NullPointerException if the specified elements is {@code null}.
   * @see #addAll(Set, Object...)
   */
  @SafeVarargs
  public static <E> LinkedHashSet<E> toLinkedHashSet(E... elements) {
    return addAll(new LinkedHashSet<>(), elements);
  }

  /**
   * Returns a new {@link TreeSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new TreeSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link TreeSet} with the specified elements added.
   * @throws NullPointerException if the specified elements is {@code null}.
   * @see #addAll(Set, Object...)
   */
  @SafeVarargs
  public static <E extends Comparable<? super E>> TreeSet<E> toTreeSet(E... elements) {
    return addAll(new TreeSet<>(), elements);
  }

  /**
   * Returns a new {@link TreeSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new TreeSet<>(comparator), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param comparator The comparator that will be used to order the resulting set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link TreeSet} with the specified elements added.
   * @throws NullPointerException if the specified elements is {@code null}.
   * @see #addAll(Set, Object...)
   */
  @SafeVarargs
  public static <E> TreeSet<E> toTreeSet(Comparator<? super E> comparator, E... elements) {
    return addAll(new TreeSet<>(comparator), elements);
  }

}
