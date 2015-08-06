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

import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;

/**
 * An abstract property getter that holds property type and provides read
 * access to the property value.
 * 
 * @author Fox Mulder
 * @see BeanIntrospector
 */
public abstract class PropertyGetter {
    
    /**
     * Returns raw type of the property.
     * 
     * @return Raw type of the property.
     */
    public abstract Class<?> getType();
    
    /**
     * Returns generic type of the property.
     * 
     * @return Generic type of the property.
     */
    public abstract Type getGenericType();
    
    /**
     * Returns property value for the specified object.
     * 
     * @param object Object whose property value should be returned.
     * @return Property value for the specified object.
     * @throws PropertyAccessException if an error occured when accessing
     *         property.
     */
    public abstract Object getValue(Object object);
    
    // Method
    
    /**
     * This class provides <code>PropertyGetter</code> implementation that
     * uses getter method to access the property.
     * 
     * @author Fox Mulder
     */
    static final class Method extends PropertyGetter {
        
        /**
         * Getter method.
         */
        private final java.lang.reflect.Method method;
        
        /**
         * Constructs a new <code>PropertyGetter.Method</code> with the
         * specified method.
         * 
         * @param method Getter method.
         */
        private Method(java.lang.reflect.Method method) {
            method.setAccessible(true);
            this.method = method;
        }
        
        /**
         * Returns raw return type of the method.
         * 
         * @return Raw return type of the method.
         */
        public Class<?> getType() {
            return method.getReturnType();
        }
        
        /**
         * Returns generic return type of the method.
         * 
         * @return Generic return type of the method.
         */
        public Type getGenericType() {
            return method.getGenericReturnType();
        }
        
        /**
         * Invokes the method on the specified object and returns value.
         * 
         * @param object Object whose property value should be returned.
         * @return Property value for the specified object.
         * @throws PropertyAccessException if an error occured when accessing
         *         property.
         */
        public Object getValue(Object object) {
            try {
                return method.invoke(object);
            } catch (IllegalAccessException e) {
                throw new PropertyAccessException(e);
            } catch (IllegalArgumentException e) {
                throw new PropertyAccessException(e);
            } catch (InvocationTargetException e) {
                throw new PropertyAccessException(e.getTargetException());
            }
        }
        
        /**
         * Returns a hash code value for this property getter.
         * 
         * @return A hash code value for this property getter.
         */
        public int hashCode() {
            return method.hashCode();
        }
        
        /**
         * Determines if this property getter equals to the specified one.
         * 
         * @param obj Another property getter.
         * @return <code>true</code> if this property getter equals to the
         *         specified one; <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            return obj instanceof Method && ((Method) obj).method.equals(method);
        }
        
        /**
         * Returns string representing signature of the method.
         * 
         * @return String representing signature of the method.
         */
        public String toString() {
            return method.toGenericString();
        }

    }
    
    // Field
    
    /**
     * This class provides <code>PropertyGetter</code> implementation that
     * uses class field to access the property.
     * 
     * @author Fox Mulder
     */
    static final class Field extends PropertyGetter {
        
        /**
         * Class field.
         */
        private final java.lang.reflect.Field field;
        
        /**
         * Constructs a new <code>PropertyGetter.Field</code> with the
         * specified field.
         * 
         * @param field Class field.
         */
        private Field(java.lang.reflect.Field field) {
            field.setAccessible(true);
            this.field = field;
        }
        
        /**
         * Returns raw type of the field.
         * 
         * @return Raw type of the field.
         */
        public Class<?> getType() {
            return field.getType();
        }
        
        /**
         * Returns generic type of the field.
         * 
         * @return Generic type of the field.
         */
        public Type getGenericType() {
            return field.getGenericType();
        }
        
        /**
         * Returns field value for the specified object.
         * 
         * @param object Object whose property value should be returned.
         * @return Property value for the specified object.
         * @throws PropertyAccessException if an error occured when accessing
         *         property.
         */
        public Object getValue(Object object) {
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                throw new PropertyAccessException(e);
            } catch (IllegalArgumentException e) {
                throw new PropertyAccessException(e);
            }
        }
        
        /**
         * Returns a hash code value for this property getter.
         * 
         * @return A hash code value for this property getter.
         */
        public int hashCode() {
            return field.hashCode();
        }
        
        /**
         * Determines if this property getter equals to the specified one.
         * 
         * @param obj Another property getter.
         * @return <code>true</code> if this property getter equals to the
         *         specified one; <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            return obj instanceof Field && ((Field) obj).field.equals(field);
        }
        
        /**
         * Returns string representing signature of the field.
         * 
         * @return String representing signature of the field.
         */
        public String toString() {
            return field.toGenericString();
        }
        
    }
    
    /**
     * Creates a new property getter for the specified class member.
     * 
     * @return A new property getter for the specified class member or
     *         <code>null</code> if member is abstract or <code>null</code>.
     * @throws IllegalArgumentException if illegal member type specified.
     */
    public static PropertyGetter newGetter(java.lang.reflect.Member getter) {
        if (getter == null || Modifier.isAbstract(getter.getModifiers())) {
            return null;
        } else if (getter instanceof java.lang.reflect.Method) {
            return new Method((java.lang.reflect.Method) getter);
        } else if (getter instanceof java.lang.reflect.Field) {
            return new Field((java.lang.reflect.Field) getter);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
