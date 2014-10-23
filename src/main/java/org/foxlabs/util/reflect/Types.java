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

package org.foxlabs.util.reflect;

import java.lang.annotation.Annotation;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;

import java.util.*;
import java.util.concurrent.*;

/**
 * Helper class used for working with java types.
 * 
 * @author Fox Mulder
 */
@SuppressWarnings("unchecked")
public abstract class Types {
    
    /**
     * Casts an object.
     * 
     * @param obj The object to be cast.
     * @return The object after casting.
     */
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
    
    /**
     * Returns array of types for the specified array of objects. Note that
     * type for <code>null</code> elements is <code>java.lang.Object</code>
     * type.
     * 
     * @param objs Array of objects.
     * @return Array of types for the specified array of objects.
     */
    public static Class<?>[] typesOf(Object... objs) {
        Class<?>[] types = new Class<?>[objs.length];
        for (int i = 0; i < objs.length; i++)
            types[i] = objs[i] == null
                ? Object.class
                : objs[i] instanceof Annotation
                    ? ((Annotation) objs[i]).annotationType()
                    : objs[i].getClass();
        return types;
    }
    
    /**
     * Determines if the specified type is a boolean type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a boolean type;
     *         <code>false</code> otherwise.
     */
    public static boolean isBoolean(Class<?> type) {
        return type == Boolean.class || type == Boolean.TYPE;
    }
    
    /**
     * Determines if the specified type is a byte type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a byte type;
     *         <code>false</code> otherwise.
     */
    public static boolean isByte(Class<?> type) {
        return type == Byte.class || type == Byte.TYPE;
    }
    
    /**
     * Determines if the specified type is a short type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a short type;
     *         <code>false</code> otherwise.
     */
    public static boolean isShort(Class<?> type) {
        return type == Short.class || type == Short.TYPE;
    }
    
    /**
     * Determines if the specified type is an integer type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is an integer type;
     *         <code>false</code> otherwise.
     */
    public static boolean isInteger(Class<?> type) {
        return type == Integer.class || type == Integer.TYPE;
    }
    
    /**
     * Determines if the specified type is a long type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a long type;
     *         <code>false</code> otherwise.
     */
    public static boolean isLong(Class<?> type) {
        return type == Long.class || type == Long.TYPE;
    }
    
    /**
     * Determines if the specified type is a float type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a float type;
     *         <code>false</code> otherwise.
     */
    public static boolean isFloat(Class<?> type) {
        return type == Float.class || type == Float.TYPE;
    }
    
    /**
     * Determines if the specified type is a double type (either primitive or
     * object).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is a double type;
     *         <code>false</code> otherwise.
     */
    public static boolean isDouble(Class<?> type) {
        return type == Double.class || type == Double.TYPE;
    }
    
    /**
     * Determines if the specified type is an object type (not a primitive,
     * array, enum or annotation).
     * 
     * @param type The type to test.
     * @return <code>true</code> if the specified type is an object type;
     *         <code>false</code> otherwise.
     */
    public static boolean isObject(Class<?> type) {
        return !(type == null || type.isPrimitive() || type.isArray() || type.isEnum() || type.isAnnotation());
    }
    
    /**
     * Returns corresponding wrapper type for the specified primitive type.
     * This method has no effect if the specified type is not primitive type.
     * 
     * @param type Primitive type.
     * @return Wrapper type for the specified primitive type.
     */
    public static <T> Class<T> wrapperTypeOf(Class<T> type) {
        if (type != null && type.isPrimitive()) {
            if (type == Boolean.TYPE)
                return (Class<T>) Boolean.class;
            if (type == Character.TYPE)
                return (Class<T>) Character.class;
            if (type == Byte.TYPE)
                return (Class<T>) Byte.class;
            if (type == Short.TYPE)
                return (Class<T>) Short.class;
            if (type == Integer.TYPE)
                return (Class<T>) Integer.class;
            if (type == Long.TYPE)
                return (Class<T>) Long.class;
            if (type == Float.TYPE)
                return (Class<T>) Float.class;
            if (type == Double.TYPE)
                return (Class<T>) Double.class;
        }
        return type;
    }
    
    /**
     * Returns corresponding primitive type for the specified wrapper type.
     * This method has no effect if the specified type is not wrapper type.
     * 
     * @param type Wrapper type.
     * @return Primitive type for the specified wrapper type.
     */
    public static Class<?> primitiveTypeOf(Class<?> type) {
        if (type == Boolean.class)
            return Boolean.TYPE;
        if (type == Character.class)
            return Character.TYPE;
        if (type == Byte.class)
            return Byte.TYPE;
        if (type == Short.class)
            return Short.TYPE;
        if (type == Integer.class)
            return Integer.TYPE;
        if (type == Long.class)
            return Long.TYPE;
        if (type == Float.class)
            return Float.TYPE;
        if (type == Double.class)
            return Double.TYPE;
        return type;
    }
    
    /**
     * Returns default value for the specified type. Note that this method can
     * return default value for primitive types only. For other types it
     * returns <code>null</code>.
     * 
     * @param type The type for which default value should be returned.
     * @return Default value for the specified type.
     */
    public static <T> T defaultValueOf(Class<T> type) {
        if (type != null && type.isPrimitive()) {
            if (type == Boolean.TYPE)
                return (T) Boolean.FALSE;
            if (type == Character.TYPE)
                return (T) Character.valueOf((char) 0);
            if (type == Byte.TYPE)
                return (T) Byte.valueOf((byte) 0);
            if (type == Short.TYPE)
                return (T) Short.valueOf((short) 0);
            if (type == Integer.TYPE)
                return (T) Integer.valueOf(0);
            if (type == Long.TYPE)
                return (T) Long.valueOf(0L);
            if (type == Float.TYPE)
                return (T) Float.valueOf(0f);
            if (type == Double.TYPE)
                return (T) Double.valueOf(0d);
        }
        return null;
    }
    
    /**
     * Returns super type for the specified type. Actually, this method just
     * checks if the specified type is <code>null</code> and returns
     * <code>java.lang.Object</code> type if so.
     * 
     * @param type The type to test.
     * @return Super type for the specified type.
     */
    public static Class<?> superTypeOf(Class<?> type) {
        return type == null ? Object.class : type;
    }
    
    /**
     * Returns super type for the specified two types.
     * 
     * @param type1 First type.
     * @param type2 Second type.
     * @return Super type for the specified two types.
     */
    public static Class<?> superTypeOf(Class<?> type1, Class<?> type2) {
        if (type1 == type2)
            return type1;
        if (type1 == null)
            return type2;
        if (type2 == null)
            return type1;
        if (type1.isAssignableFrom(type2))
            return type1;
        if (type2.isAssignableFrom(type1))
            return type2;
        return Object.class;
    }
    
    /**
     * Returns super type for the specified array of types.
     * 
     * @param types Array of types.
     * @return Super type for the specified array of types.
     */
    public static Class<?> superTypeOf(Class<?>... types) {
        int count = types.length;
        if (count == 0)
            return Object.class;
        Class<?> type = types[0];
        if (count == 1)
            return superTypeOf(type);
        for (int i = 1; i < count; i++) {
            type = superTypeOf(type, types[i]);
            if (type == Object.class)
                return Object.class;
        }
        return type;
    }
    
    /**
     * Returns raw type for the specified generic type.
     * 
     * @param type Generic type.
     * @return Raw type for the specified generic type.
     */
    public static <T> Class<T> rawTypeOf(Type type) {
        if (type instanceof Class)
            return (Class<T>) type;
        if (type instanceof ParameterizedType)
            return (Class<T>) ((ParameterizedType) type).getRawType();
        if (type instanceof GenericArrayType)
            return (Class<T>) arrayTypeOf(rawTypeOf(((GenericArrayType) type).getGenericComponentType()));
        return (Class<T>) Object.class;
    }
    
    /**
     * Returns array type for the specified type of array elements.
     * 
     * @param elementType Type of array elements.
     * @return Array type for the specified type of array elements.
     */
    public static <T, E> Class<T> arrayTypeOf(Class<E> elementType) {
        if (elementType == null)
            return (Class<T>) Object[].class;
        return (Class<T>) Array.newInstance(elementType, 0).getClass();
    }
    
    /**
     * Returns raw element (value) type for the specified generic array,
     * collection or map type. Note that this method returns <code>null</code>
     * if the specified type is not array, collection or map type.
     * 
     * @param type Generic array, collection or map type.
     * @return Raw element (value) type for the specified type.
     */
    public static <T> Class<T> elementTypeOf(Type type) {
        Class<?> rawtype = rawTypeOf(type);
        if (rawtype.isArray())
            return (Class<T>) rawtype.getComponentType();
        if (Collection.class.isAssignableFrom(rawtype)) {
            if (type instanceof Class)
                return (Class<T>) parameterTypeOf((Class<?>) type, Collection.class, 0);
            if (type instanceof ParameterizedType) {
                Type[] argtypes = ((ParameterizedType) type).getActualTypeArguments();
                return (Class<T>) (argtypes.length == 0 ? Object.class : rawTypeOf(argtypes[0]));
            }
        } else if (Map.class.isAssignableFrom(rawtype)) {
            if (type instanceof Class)
                return (Class<T>) parameterTypeOf((Class<?>) type, Map.class, 1);
            if (type instanceof ParameterizedType) {
                Type[] argtypes = ((ParameterizedType) type).getActualTypeArguments();
                return (Class<T>) (argtypes.length < 2 ? Object.class : rawTypeOf(argtypes[1]));
            }
        }
        return null;
    }
    
    /**
     * Returns raw key type for the specified generic map type. Note that this
     * method returns <code>null</code> if the specified type is not map type.
     * 
     * @param type Generic map type.
     * @return Raw key type for the specified type.
     */
    public static <T> Class<T> keyTypeOf(Type type) {
        if (Map.class.isAssignableFrom(rawTypeOf(type))) {
            if (type instanceof Class)
                return (Class<T>) parameterTypeOf((Class<?>) type, Map.class, 0);
            if (type instanceof ParameterizedType) {
                Type[] argtypes = ((ParameterizedType) type).getActualTypeArguments();
                return (Class<T>) (argtypes.length < 1 ? Object.class : rawTypeOf(argtypes[0]));
            }
        }
        return null;
    }
    
    /**
     * Returns the type of parameter with the specified index for the specified
     * type.
     * 
     * @param type Subtype.
     * @param base Base super type.
     * @param index Parameter index.
     * @return The type of parameter with the specified index for the specified
     *         type.
     */
    public static Class<?> parameterTypeOf(Class<?> type, Class<?> base, int index) {
        List<Type> supertypes = new LinkedList<Type>();
        if (type.getGenericSuperclass() != null)
            supertypes.add(type.getGenericSuperclass());
        for (Type intf : type.getGenericInterfaces())
            supertypes.add(intf);
        for (Type supertype : supertypes) {
            Class<?> rawtype = rawTypeOf(supertype);
            if (base.isAssignableFrom(rawtype)) {
                Class<?> paramtype = parameterTypeOf(rawtype, base, index);
                if (paramtype != Object.class)
                    return paramtype;
                if (supertype instanceof ParameterizedType) {
                    Type[] argtypes = ((ParameterizedType) supertype).getActualTypeArguments();
                    if (index < argtypes.length && argtypes[index] instanceof Class)
                        return (Class<?>) argtypes[index];
                }
            }
        }
        return Object.class;
    }
    
    /**
     * Creates a new object instance of the specified type.
     * 
     * @param type The type of object to be created.
     * @return A new object instance of the specified type.
     * @throws RuntimeException if object instantiation fails.
     */
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Creates a new array for the specified type of array elements.
     * 
     * @param elementType The type of array elements.
     * @param length Length of the new array.
     * @return A new array for the specified type of array elements.
     */
    public static <T, E> T newArray(Class<E> elementType, int length) {
        return (T) Array.newInstance(elementType, length);
    }
    
    /**
     * Creates a new collection of the specified type.
     * 
     * @param type The type of collection to be created.
     * @param size Initial size of the new collection.
     * @return A new collection of the specified type.
     */
    public static <T extends Collection<E>, E> T newCollection(Class<T> type, int size) {
        if (Collection.class.equals(type))
            return type.cast(new ArrayList<E>(size));
        if (List.class.isAssignableFrom(type))
            return type.cast(newList(type.asSubclass(List.class), size));
        if (Set.class.isAssignableFrom(type))
            return type.cast(newSet(type.asSubclass(Set.class), size));
        if (Queue.class.isAssignableFrom(type))
            return type.cast(newQueue(type.asSubclass(Queue.class), size));
        return newInstance(type);
    }
    
    /**
     * Creates a new list of the specified type.
     * 
     * @param type The type of list to be created.
     * @param size Initial size of the new list.
     * @return A new list of the specified type.
     */
    public static <T extends List<E>, E> T newList(Class<T> type, int size) {
        if (List.class.equals(type) || ArrayList.class.equals(type))
            return type.cast(new ArrayList<E>(size));
        if (LinkedList.class.equals(type))
            return type.cast(new LinkedList<E>());
        return newInstance(type);
    }
    
    /**
     * Creates a new set of the specified type.
     * 
     * @param type The type of set to be created.
     * @param size Initial size of the new set.
     * @return A new set of the specified type.
     */
    public static <T extends Set<E>, E> T newSet(Class<T> type, int size) {
        if (Set.class.equals(type) || LinkedHashSet.class.equals(type))
            return type.cast(new LinkedHashSet<E>(size));
        if (HashSet.class.equals(type))
            return type.cast(new HashSet<E>(size));
        if (SortedSet.class.equals(type) || NavigableSet.class.equals(type) || TreeSet.class.equals(type))
            return type.cast(new TreeSet<E>());
        if (ConcurrentSkipListSet.class.equals(type))
            return type.cast(new ConcurrentSkipListSet<E>());
        return newInstance(type);
    }
    
    /**
     * Creates a new queue of the specified type.
     * 
     * @param type The type of queue to be created.
     * @param size Initial size of the new queue.
     * @return A new queue of the specified type.
     */
    public static <T extends Queue<E>, E> T newQueue(Class<T> type, int size) {
        if (Queue.class.equals(type) || PriorityQueue.class.equals(type))
            return type.cast(new PriorityQueue<E>(size));
        if (Deque.class.equals(type) || LinkedList.class.equals(type))
            return type.cast(new LinkedList<E>());
        if (ArrayDeque.class.equals(type))
            return type.cast(new ArrayDeque<E>(size));
        if (ConcurrentLinkedQueue.class.equals(type))
            return type.cast(new ConcurrentLinkedQueue<E>());
        if (BlockingQueue.class.equals(type) || LinkedBlockingQueue.class.equals(type))
            return type.cast(new LinkedBlockingQueue<E>(size));
        if (ArrayBlockingQueue.class.equals(type))
            return type.cast(new ArrayBlockingQueue<E>(size));
        if (PriorityBlockingQueue.class.equals(type))
            return type.cast(new PriorityBlockingQueue<E>(size));
        if (SynchronousQueue.class.equals(type))
            return type.cast(new SynchronousQueue<E>());
        if (BlockingDeque.class.equals(type) || LinkedBlockingDeque.class.equals(type))
            return type.cast(new LinkedBlockingDeque<E>(size));
        return newInstance(type);
    }
    
    /**
     * Creates a new map of the specified type.
     * 
     * @param type The type of map to be created.
     * @param size Initial size of the new map.
     * @return A new map of the specified type.
     */
    public static <T extends Map<K, V>, K, V> T newMap(Class<T> type, int size) {
        if (Map.class.equals(type) || LinkedHashMap.class.equals(type))
            return type.cast(new LinkedHashMap<K, V>(size));
        if (HashMap.class.equals(type))
            return type.cast(new HashMap<K, V>(size));
        if (IdentityHashMap.class.equals(type))
            return type.cast(new IdentityHashMap<K, V>(size));
        if (SortedMap.class.equals(type) || NavigableMap.class.equals(type) || TreeMap.class.equals(type))
            return type.cast(new TreeMap<K, V>());
        if (ConcurrentMap.class.equals(type) || ConcurrentHashMap.class.equals(type))
            return type.cast(new ConcurrentHashMap<K, V>(size));
        if (ConcurrentNavigableMap.class.equals(type) || ConcurrentSkipListMap.class.equals(type))
            return type.cast(new ConcurrentSkipListMap<K, V>());
        return newInstance(type);
    }
    
    /**
     * Returns a hash code value for the specified array of types.
     * 
     * @param types Array of types.
     * @return A hash code value for the specified array of types.
     */
    public static int hashCode(Class<?>... types) {
        if (types == null || types.length == 0)
            return 0;
        int hash = 1;
        for (Class<?> type : types)
            hash = 31 * hash + (type == null ? 0 : type.hashCode());
        return hash;
    }
    
}
