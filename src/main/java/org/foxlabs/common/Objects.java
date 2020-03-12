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

import java.util.Map;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * The commonly used operations on objects.
 *
 * @author Fox Mulder
 */
public final class Objects {

  // Instantiation is not possible
  private Objects() {
    throw new IllegalAccessError();
  }

  // toString

  /**
   * The maximum default length of a string that may produce the {@code Objects.toString()} methods.
   */
  public static final int DEFAULT_BUFFER_THRESHOLD = 4096;

  /**
   * Converts the specified object to a string representation.
   *
   * <p>
   * This is a shortcut for the:
   * <code>
   * toString(object, new StringBuilder(DEFAULT_BUFFER_THRESHOLD), DEFAULT_BUFFER_THRESHOLD)
   *     .toString()
   * </code>
   * </p>
   *
   * @param object The reference to an object to be converted to a string representation.
   * @return A string representation of the specified object.
   * @see #toString(Object, StringBuilder, int)
   */
  public static String toString(Object object) {
    return toString(object, new StringBuilder(DEFAULT_BUFFER_THRESHOLD), DEFAULT_BUFFER_THRESHOLD)
        .toString();
  }

  /**
   * Converts the specified object to a string representation.
   *
   * <p>
   * This is a shortcut for the:
   * <code>
   * toString(object, new StringBuilder(Math.min(DEFAULT_BUFFER_THRESHOLD, threshold)), threshold)
   *     .toString()
   * </code>
   * </p>
   *
   * @param object The reference to an object to be converted to a string representation.
   * @param threshold The maximum length of a resulting string representation of the object.
   * @return A string representation of the specified object.
   * @see #toString(Object, StringBuilder, int)
   */
  public static String toString(Object object, int threshold) {
    return toString(object, new StringBuilder(Math.min(DEFAULT_BUFFER_THRESHOLD, threshold)), threshold)
        .toString();
  }

  /**
   * Appends a string representation of the specified object to the specified buffer.
   *
   * <p>
   * This is a shortcut for the:
   * <code>
   * toString(object, buffer, DEFAULT_BUFFER_THRESHOLD)
   * </code>
   * </p>
   *
   * @param object The reference to an object to be converted to a string representation.
   * @param buffer The buffer to append to.
   * @return A reference to the specified buffer.
   * @see #toString(Object, StringBuilder, int)
   */
  public static StringBuilder toString(Object object, StringBuilder buffer) {
    return toString(object, buffer, DEFAULT_BUFFER_THRESHOLD);
  }

  /**
   * Appends a string representation of the specified object to the specified buffer.
   *
   * @param object The reference to an object to be converted to a string representation.
   * @param buffer The buffer to append to.
   * @param threshold The maximum length of a resulting string representation of the object.
   * @return A reference to the specified buffer.
   * @throws NullPointerException if the specified buffer is {@code null}.
   * @throws IllegalArgumentException if the specified threshold is negative.
   */
  public static StringBuilder toString(Object object, StringBuilder buffer, int threshold) {
    Predicates.requireNonNull(buffer);
    Predicates.require(threshold, Predicates.INT_POSITIVE_OR_ZERO);
    toString0(object, buffer, threshold, new IdentityHashMap<>());
    return buffer;
  }

  static boolean toString0(Object object, StringBuilder buffer, int threshold,
      Map<Object, StringBuilder> crossrefs) {
    // In order of probability
    if (object == null) {
      return Strings.appendSafe0("null", buffer, threshold);
    } else if (object instanceof CharSequence) {
      return toString0((CharSequence) object, buffer, threshold);
    } else if (object instanceof Integer) {
      return Strings.appendSafe0(((Integer) object).intValue(), buffer, threshold);
    } else if (object instanceof Long) {
      return Strings.appendSafe0(((Long) object).longValue(), buffer, threshold);
    } else if (object instanceof Double) {
      return Strings.appendSafe0(((Double) object).doubleValue(), buffer, threshold);
    } else if (object instanceof Float) {
      return Strings.appendSafe0(((Float) object).floatValue(), buffer, threshold);
    } else if (object instanceof Byte) {
      return Strings.appendSafe0(((Integer) object).intValue(), buffer, threshold);
    } else if (object instanceof Short) {
      return Strings.appendSafe0(((Integer) object).intValue(), buffer, threshold);
    } else if (object instanceof Character) {
      return toString0(((Character) object).charValue(), buffer, threshold);
    } else if (object instanceof Boolean) {
      return Strings.appendSafe0(((Boolean) object).booleanValue(), buffer, threshold);
    } else if (object instanceof Enum<?>) {
      return Strings.appendSafe0(((Enum<?>) object).name(), buffer, threshold);
    } else if (object instanceof Iterable<?>) {
      return toString0(object, buffer, threshold, crossrefs);
    } else if (object instanceof Map<?, ?>) {
      return toString0(object, buffer, threshold, crossrefs);
    } else if (object instanceof Object[]) {
      return toString0(object, buffer, threshold, crossrefs);
    } else if (object instanceof int[]) {
      return toString0((int[]) object, buffer, threshold);
    } else if (object instanceof long[]) {
      return toString0((long[]) object, buffer, threshold);
    } else if (object instanceof double[]) {
      return toString0((double[]) object, buffer, threshold);
    } else if (object instanceof float[]) {
      return toString0((float[]) object, buffer, threshold);
    } else if (object instanceof byte[]) {
      return toString0((byte[]) object, buffer, threshold);
    } else if (object instanceof short[]) {
      return toString0((short[]) object, buffer, threshold);
    } else if (object instanceof char[]) {
      return toString0((char[]) object, buffer, threshold);
    } else if (object instanceof boolean[]) {
      return toString0((boolean[]) object, buffer, threshold);
    }
    return toStringDefault(object, buffer, threshold);
  }

  static boolean toString0(CharSequence value, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('\"', buffer, threshold)) {
      // FIXME need to escape: Strings.escape(value, buffer, threshold)
      if (Strings.appendSafe0(value, buffer, threshold)) {
        return Strings.appendSafe0('\"', buffer, threshold);
      }
    }
    return false;
  }

  static boolean toString0(char value, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('\'', buffer, threshold)) {
      // FIXME need to escape: Strings.escape(value, buffer, threshold)
      if (Strings.appendSafe0(value, buffer, threshold)) {
        return Strings.appendSafe0('\'', buffer, threshold);
      }
    }
    return false;
  }

  static boolean toString0(boolean[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(char[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (toString0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!toString0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(byte[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(short[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(int[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(long[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(float[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(double[] array, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('[', buffer, threshold)) {
      if (array.length > 0) {
        if (Strings.appendSafe0(array[0], buffer, threshold)) {
          for (int index = 1; index < array.length; index++) {
            if (Strings.appendSafe0(", ", buffer, threshold)) {
              if (!Strings.appendSafe0(array[index], buffer, threshold)) {
                return false;
              }
            }
          }
        }
      }
      return Strings.appendSafe0(']', buffer, threshold);
    }
    return false;
  }

  static boolean toString0(Object[] array, StringBuilder buffer, int threshold,
      Map<Object, StringBuilder> crossrefs) {
    crossrefs.put(array, null);
    try {
      if (Strings.appendSafe0('[', buffer, threshold)) {
        if (array.length > 0) {
          if (toStringCrossref(array[0], buffer, threshold, crossrefs)) {
            for (int index = 1; index < array.length; index++) {
              if (Strings.appendSafe0(", ", buffer, threshold)) {
                if (!toStringCrossref(array[index], buffer, threshold, crossrefs)) {
                  return false;
                }
              }
            }
          }
        }
        return Strings.appendSafe0(']', buffer, threshold);
      }
      return false;
    } finally {
      crossrefs.remove(array);
    }
  }

  static boolean toString0(Iterable<?> iterable, StringBuilder buffer, int threshold,
      Map<Object, StringBuilder> crossrefs) {
    crossrefs.put(iterable, null);
    try {
      if (Strings.appendSafe0('[', buffer, threshold)) {
        final Iterator<?> itr = iterable.iterator();
        if (itr != null && itr.hasNext()) {
          if (toStringCrossref(itr.next(), buffer, threshold, crossrefs)) {
            while (itr.hasNext()) {
              if (Strings.appendSafe0(", ", buffer, threshold)) {
                if (!toStringCrossref(itr.next(), buffer, threshold, crossrefs)) {
                  return false;
                }
              }
            }
          }
        }
        return Strings.appendSafe0(']', buffer, threshold);
      }
      return false;
    } finally {
      crossrefs.remove(iterable);
    }
  }

  static boolean toString0(Map<?, ?> map, StringBuilder buffer, int threshold,
      Map<Object, StringBuilder> crossrefs) {
    crossrefs.put(map, null);
    try {
      if (Strings.appendSafe0('{', buffer, threshold)) {
        if (map.size() > 0) {
          final Iterator<Map.Entry<?, ?>> itr = cast(map.entrySet().iterator());
          if (itr != null && itr.hasNext()) {
            Map.Entry<?, ?> entry = itr.next();
            if (toStringCrossref(entry.getKey(), buffer, threshold, crossrefs)) {
              if (Strings.appendSafe0(": ", buffer, threshold)) {
                if (toStringCrossref(entry.getValue(), buffer, threshold, crossrefs)) {
                  while (itr.hasNext()) {
                    entry = itr.next();
                    if (!Strings.appendSafe0(", ", buffer, threshold)) {
                      return false;
                    } else if (!toStringCrossref(entry.getKey(), buffer, threshold, crossrefs)) {
                      return false;
                    } else if (!Strings.appendSafe0(": ", buffer, threshold)) {
                      return false;
                    } else if (!toStringCrossref(entry.getValue(), buffer, threshold, crossrefs)) {
                      return false;
                    }
                  }
                }
              }
            }
          }
        }
        return Strings.appendSafe0('}', buffer, threshold);
      }
      return false;
    } finally {
      crossrefs.remove(map);
    }
  }

  static boolean toStringDefault(Object object, StringBuilder buffer, int threshold) {
    if (Strings.appendSafe0('(', buffer, threshold)) {
      // FIXME escape?
      if (Strings.appendSafe0(object.toString(), buffer, threshold)) {
        return Strings.appendSafe0(')', buffer, threshold);
      }
    }
    return false;
  }

  private static boolean toStringCrossref(Object object, StringBuilder buffer, int threshold,
      Map<Object, StringBuilder> crossrefs) {
    if (crossrefs.containsKey(object)) {
      StringBuilder crossref = crossrefs.get(object);
      if (crossref == null) {
        final String classname = object.getClass().getSimpleName();
        crossref = new StringBuilder(classname.length() + 9).append(classname).append('@')
            .append(Integer.toHexString(System.identityHashCode(object)));
        crossrefs.put(object, crossref);
      }
      return Strings.appendSafe0(crossref, buffer, threshold);
    }
    return toString0(object, buffer, threshold, crossrefs);
  }

  // Miscellaneous

  /**
   * Casts the specified object reference to a custom target type. This method just provides a way
   * to avoid declaration of the {@link SuppressWarnings} annotation.
   *
   * @param <T> The target reference type.
   * @param object The object reference to cast.
   * @return The specified object reference casted to the target type.
   * @throws ClassCastException if cast operation failed.
   */
  @SuppressWarnings("unchecked")
  public static <T> T cast(Object object) {
    return (T) object;
  }

  /**
   * Returns a new object with the overridden {@link Object#toString()} method that uses the
   * specified formatter to generate the resulting string. This method is useful for lazy message
   * construction.
   *
   * @param formatter The {@code Object.toString()} result formatter.
   * @return A new object instance with the overridden {@code Object.toString()} method.
   * @throws NullPointerException if the specified formatter is {@code null}.
   */
  public static Object message(Supplier<String> formatter) {
    Predicates.requireNonNull(formatter);
    return new Object() {
      @Override public String toString() {
        return formatter.get();
      }
    };
  }

}
