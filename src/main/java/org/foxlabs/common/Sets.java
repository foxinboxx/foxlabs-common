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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedSet;
import java.util.TreeSet;
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
   * Returns a new {@link HashSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new HashSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link HashSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E> HashSet<E> toHashSet(E... elements) {
    Predicates.requireNonNull(elements);
    return addAll0(new HashSet<>(), elements);
  }

  /**
   * Returns a new immutable {@link Set} based on the {@link HashSet} with the specified elements
   * added.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new immutable {@link Set} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E> Set<E> toImmutableHashSet(E... elements) {
    Predicates.requireNonNull(elements);
    return elements.length > 0
        ? Collections.unmodifiableSet(addAll0(new HashSet<>(), elements))
        : Collections.emptySet();
  }

  /**
   * Returns a new {@link LinkedHashSet} with the specified elements added. This is a shortcut for
   * the {@code addAll(new LinkedHashSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link LinkedHashSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E> LinkedHashSet<E> toLinkedHashSet(E... elements) {
    Predicates.requireNonNull(elements);
    return addAll0(new LinkedHashSet<>(), elements);
  }

  /**
   * Returns a new immutable {@link Set} based on the {@link LinkedHashSet} with the specified
   * elements added.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new immutable {@link Set} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E> Set<E> toImmutableLinkedHashSet(E... elements) {
    Predicates.requireNonNull(elements);
    return elements.length > 0
        ? Collections.unmodifiableSet(addAll0(new LinkedHashSet<>(), elements))
        : Collections.emptySet();
  }

  /**
   * Returns a new {@link TreeSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new TreeSet<>(), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link TreeSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E extends Comparable<? super E>> TreeSet<E> toTreeSet(E... elements) {
    Predicates.requireNonNull(elements);
    return addAll0(new TreeSet<>(), elements);
  }

  /**
   * Returns a new immutable {@link SortedSet} based on the {@link TreeSet} with the specified
   * elements added.
   *
   * @param <E> The type of elements of the set.
   * @param elements The array of elements to be added to the set.
   * @return A new immutable {@link SortedSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to elements is {@code null}.
   */
  @SafeVarargs
  public static <E> SortedSet<E> toImmutableTreeSet(E... elements) {
    Predicates.requireNonNull(elements);
    return elements.length > 0
        ? Collections.unmodifiableSortedSet(addAll0(new TreeSet<>(), elements))
        : Collections.emptySortedSet();
  }

  /**
   * Returns a new {@link TreeSet} with the specified elements added. This is a shortcut for the
   * {@code addAll(new TreeSet<>(comparator), elements)}.
   *
   * @param <E> The type of elements of the set.
   * @param comparator The comparator that will be used to order the resulting set.
   * @param elements The array of elements to be added to the set.
   * @return A new {@link TreeSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to comparator or elements is
   *         {@code null}.
   */
  @SafeVarargs
  public static <E> TreeSet<E> toTreeSet(Comparator<? super E> comparator, E... elements) {
    Predicates.requireNonNull(comparator);
    Predicates.requireNonNull(elements);
    return addAll0(new TreeSet<>(comparator), elements);
  }

  /**
   * Returns a new immutable {@link SortedSet} based on the {@link TreeSet} with the specified
   * elements added.
   *
   * @param <E> The type of elements of the set.
   * @param comparator The comparator that will be used to order the resulting set.
   * @param elements The array of elements to be added to the set.
   * @return A new immutable {@link SortedSet} with the specified elements added.
   * @throws NullPointerException if the specified reference to comparator or elements is
   *         {@code null}.
   */
  @SafeVarargs
  public static <E> SortedSet<E> toImmutableTreeSet(Comparator<? super E> comparator, E... elements) {
    Predicates.requireNonNull(comparator);
    Predicates.requireNonNull(elements);
    return elements.length > 0
        ? Collections.unmodifiableSortedSet(addAll0(new TreeSet<>(comparator), elements))
        : Collections.emptySortedSet();
  }

  /**
   * Adds all the specified elements to the specified set. This method does the same as the
   * {@link Collections#addAll(java.util.Collection, Object...)}, but returns a reference to the
   * specified set.
   *
   * @param <E> The type of elements of the set.
   * @param <S> The type of the set.
   * @param set The set to add new elements to.
   * @param elements The array of elements to be added to the set.
   * @return A reference to the specified set.
   * @throws NullPointerException if the specified reference to set or elements is {@code null}.
   */
  @SafeVarargs
  public static <E, S extends Set<E>> S addAll(S set, E... elements) {
    Predicates.requireNonNull(set);
    Predicates.requireNonNull(elements);
    return addAll0(set, elements);
  }

  // Utilities
  
  /* Adds all the specified elements to the specified set */
  @SafeVarargs
  private static <E, S extends Set<E>> S addAll0(S set, E... elements) {
    Collections.addAll(set, elements);
    return set;
  }

}
