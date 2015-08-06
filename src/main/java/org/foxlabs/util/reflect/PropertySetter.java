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
 * An abstract property setter that holds property type and provides write
 * access to the property value.
 * 
 * @author Fox Mulder
 * @see BeanIntrospector
 */
public abstract class PropertySetter {
    
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
     * Assigns property value for the specified object.
     * 
     * @param object Object whose property value should be assigned.
     * @param value New property value.
     * @throws PropertyAccessException if an error occured when accessing
     *         property.
     */
    public abstract void setValue(Object object, Object value);
    
    // Method
    
    /**
     * This class provides <code>PropertySetter</code> implementation that
     * uses setter method to access the property.
     * 
     * @author Fox Mulder
     */
    public static final class Method extends PropertySetter {
        
        /**
         * Setter method.
         */
        private final java.lang.reflect.Method method;
        
        /**
         * Constructs a new <code>PropertySetter.Method</code> with the
         * specified method.
         * 
         * @param method Setter method.
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
            return method.getParameterTypes()[0];
        }
        
        /**
         * Returns generic return type of the method.
         * 
         * @return Generic return type of the method.
         */
        public Type getGenericType() {
            return method.getGenericParameterTypes()[0];
        }
        
        /**
         * Invokes the method on the specified object with the specified value
         * as method parameter.
         * 
         * @param object Object whose property value should be assigned.
         * @param value New property value.
         * @throws PropertyAccessException if an error occured when accessing
         *         property.
         */
        public void setValue(Object object, Object value) {
            try {
                method.invoke(object, value);
            } catch (IllegalAccessException e) {
                throw new PropertyAccessException(e);
            } catch (IllegalArgumentException e) {
                throw new PropertyAccessException(e);
            } catch (InvocationTargetException e) {
                throw new PropertyAccessException(e.getTargetException());
            }
        }
        
        /**
         * Returns a hash code value for this property setter.
         * 
         * @return A hash code value for this property setter.
         */
        public int hashCode() {
            return method.hashCode();
        }
        
        /**
         * Determines if this property setter equals to the specified one.
         * 
         * @param obj Another property setter.
         * @return <code>true</code> if this property setter equals to the
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
     * This class provides <code>PropertySetter</code> implementation that
     * uses class field to access the property.
     * 
     * @author Fox Mulder
     */
    public static final class Field extends PropertySetter {
        
        /**
         * Class field.
         */
        private final java.lang.reflect.Field field;
        
        /**
         * Constructs a new <code>PropertySetter.Field</code> with the
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
         * Assigns field value for the specified object.
         * 
         * @param object Object whose property value should be assigned.
         * @param value New property value.
         * @throws PropertyAccessException if an error occured when accessing
         *         property.
         */
        public void setValue(Object object, Object value) {
            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new PropertyAccessException(e);
            } catch (IllegalArgumentException e) {
                throw new PropertyAccessException(e);
            }
        }
        
        /**
         * Returns a hash code value for this property setter.
         * 
         * @return A hash code value for this property setter.
         */
        public int hashCode() {
            return field.hashCode();
        }
        
        /**
         * Determines if this property setter equals to the specified one.
         * 
         * @param obj Another property setter.
         * @return <code>true</code> if this property setter equals to the
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
     * Creates a new property setter for the specified class member.
     * 
     * @return A new property setter for the specified class member or
     *         <code>null</code> if member is abstract or <code>null</code>.
     * @throws IllegalArgumentException if illegal member type specified.
     */
    public static PropertySetter newSetter(java.lang.reflect.Member setter) {
        if (setter == null || Modifier.isAbstract(setter.getModifiers())) {
            return null;
        } else if (setter instanceof java.lang.reflect.Method) {
            return new Method((java.lang.reflect.Method) setter);
        } else if (setter instanceof java.lang.reflect.Field) {
            return new Field((java.lang.reflect.Field) setter);
        } else {
            throw new IllegalArgumentException();
        }
    }
    
}
