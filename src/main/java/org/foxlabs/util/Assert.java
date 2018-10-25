/* 
 * Copyright (C) 2012 FoxLabs
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

package org.foxlabs.util;

import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * This class defines a number of methods that help to validate arguments.
 * 
 * <p>For example:</p>
 * <pre>
 *   public void setName(String name) {
 *       this.name = Assert.notEmpty(name, "Name cannot be empty!");
 *   }
 * </pre>
 * 
 * @author Fox Mulder
 */
public abstract class Assert {
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified flag is
     * <code>false</code>.
     * 
     * @param flag Test flag.
     * @param message Exception message.
     * @throws IllegalArgumentException if the specified flag is
     *         <code>false</code>.
     */
    public static void assertTrue(boolean flag, String message) {
        if (!flag) {
            throw assertError(message);
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified flag is
     * <code>true</code>.
     * 
     * @param flag Test flag.
     * @param message Exception message.
     * @throws IllegalArgumentException if the specified flag is
     *         <code>true</code>.
     */
    public static void assertFalse(boolean flag, String message) {
        if (flag) {
            throw assertError(message);
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified object is
     * <code>null</code>.
     * 
     * @param <T> The object type.
     * @param object Object to test.
     * @param message Exception message.
     * @return The specified object.
     * @throws IllegalArgumentException if the specified object is
     *         <code>null</code>.
     */
    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw assertError(message);
        } else {
            return object;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified string is
     * <code>null</code> or empty.
     * 
     * @param string String to test.
     * @param message Exception message.
     * @return The specified string.
     * @throws IllegalArgumentException if the specified string is
     *         <code>null</code> or empty.
     */
    public static String notEmpty(String string, String message) {
        if (string == null || string.isEmpty()) {
            throw assertError(message);
        } else {
            return string;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified array is
     * <code>null</code> or empty.
     * 
     * @param <T> The array type.
     * @param array Array to test.
     * @param message Exception message.
     * @return The specified array.
     * @throws IllegalArgumentException if the specified array is
     *         <code>null</code> or empty.
     */
    public static <T> T[] notEmpty(T[] array, String message) {
        if (array == null || array.length == 0) {
            throw assertError(message);
        } else {
            return array;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified collection
     * is <code>null</code> or empty.
     * 
     * @param <T> The collection type.
     * @param collection Collection to test.
     * @param message Exception message.
     * @return The specified collection.
     * @throws IllegalArgumentException if the specified collection is
     *         <code>null</code> or empty.
     */
    public static <T extends Collection<?>> T notEmpty(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw assertError(message);
        } else {
            return collection;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified array is
     * <code>null</code> or empty or contains <code>null</code> elements.
     * 
     * @param <T> The array type.
     * @param array Array to test.
     * @param message Exception message.
     * @return The specified array.
     * @throws IllegalArgumentException if the specified array is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    public static <T> T[] noNullElements(T[] array, String message) {
        if (array == null || array.length == 0) {
            throw assertError(message);
        }
        for (T element : array) {
            if (element == null) {
                throw assertError(message);
            }
        }
        return array;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified collection
     * is <code>null</code> or empty or contains <code>null</code> elements.
     * 
     * @param <T> The collection type.
     * @param collection Collection to test.
     * @param message Exception message.
     * @return The specified collection.
     * @throws IllegalArgumentException if the specified collection is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    public static <T extends Collection<?>> T noNullElements(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw assertError(message);
        }
        for (Object element : collection) {
            if (element == null) {
                throw assertError(message);
            }
        }
        return collection;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified array is
     * <code>null</code> or empty or contains <code>null</code> or empty
     * strings.
     * 
     * @param array Array to test.
     * @param message Exception message.
     * @return The specified array.
     * @throws IllegalArgumentException if the specified array is
     *         <code>null</code> or empty or contains <code>null</code>
     *         or empty strings.
     */
    public static String[] noEmptyElements(String[] array, String message) {
        if (array == null || array.length == 0) {
            throw assertError(message);
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i].isEmpty()) {
                throw assertError(message);
            }
        }
        return array;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified collection
     * is <code>null</code> or empty or contains <code>null</code> or empty
     * strings.
     * 
     * @param <T> The collection type.
     * @param collection Collection to test.
     * @param message Exception message.
     * @return The specified collection.
     * @throws IllegalArgumentException if the specified collection is
     *         <code>null</code> or empty or contains <code>null</code>
     *         or empty strings.
     */
    public static <T extends Collection<String>> T noEmptyElements(T collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw assertError(message);
        }
        for (String element : collection) {
            if (element == null || element.isEmpty()) {
                throw assertError(message);
            }
        }
        return collection;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified array is
     * <code>null</code> or empty or contains <code>null</code> strings.
     * 
     * @param array Array to test.
     * @param message Exception message.
     * @return Set of the specified strings.
     * @throws IllegalArgumentException if the specified array is
     *         <code>null</code> or empty or contains <code>null</code>
     *         strings.
     */
    public static Set<String> noNullStringSet(String[] array, String message) {
        if (array == null || array.length == 0) {
            throw assertError(message);
        }
        Set<String> set = new LinkedHashSet<String>(array.length);
        for (String element : array) {
            if (element == null) {
                throw assertError(message);
            }
            set.add(element);
        }
        return set;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified array is
     * <code>null</code> or empty or contains <code>null</code> or empty
     * strings.
     * 
     * @param array Array to test.
     * @param message Exception message.
     * @return Set of the specified strings.
     * @throws IllegalArgumentException if the specified array is
     *         <code>null</code> or empty or contains <code>null</code>
     *         or empty strings.
     */
    public static Set<String> noEmptyStringSet(String[] array, String message) {
        if (array == null || array.length == 0) {
            throw assertError(message);
        }
        Set<String> set = new LinkedHashSet<String>(array.length);
        for (String element : array) {
            if (element == null || element.isEmpty()) {
                throw assertError(message);
            }
            set.add(element);
        }
        return set;
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>int</code> value is negative.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative.
     */
    public static int notNegative(int value, String message) {
        if (value < 0) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>long</code> value is negative.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative.
     */
    public static long notNegative(long value, String message) {
        if (value < 0L) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>float</code> value is negative.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative.
     */
    public static float notNegative(float value, String message) {
        if (value < 0.0f) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>double</code> value is negative.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative.
     */
    public static double notNegative(double value, String message) {
        if (value < 0.0d) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>int</code> value is negative or zero.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative or
     *         zero.
     */
    public static int notNegativeOrZero(int value, String message) {
        if (value <= 0) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>long</code> value is negative or zero.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative or
     *         zero.
     */
    public static long notNegativeOrZero(long value, String message) {
        if (value <= 0L) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>float</code> value is negative or zero.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative or
     *         zero.
     */
    public static float notNegativeOrZero(float value, String message) {
        if (value <= 0.0f) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Throws <code>IllegalArgumentException</code> if the specified
     * <code>double</code> value is negative or zero.
     * 
     * @param value Value to test.
     * @param message Exception message.
     * @return The specified value.
     * @throws IllegalArgumentException if the specified value is negative or
     *         zero.
     */
    public static double notNegativeOrZero(double value, String message) {
        if (value <= 0.0d) {
            throw assertError(message);
        } else {
            return value;
        }
    }
    
    /**
     * Returns <code>IllegalArgumentException</code> initialized with the
     * specified message and with adjusted stack trace.
     * 
     * @param message Exception message.
     * @return <code>IllegalArgumentException</code>.
     */
    protected static IllegalArgumentException assertError(String message) {
        IllegalArgumentException exception = new IllegalArgumentException(message);
        StackTraceElement[] oldTrace = exception.getStackTrace();
        StackTraceElement[] newTrace = new StackTraceElement[oldTrace.length - 1];
        System.arraycopy(oldTrace, 1, newTrace, 0, newTrace.length);
        exception.setStackTrace(newTrace);
        return exception;
    }
    
}
