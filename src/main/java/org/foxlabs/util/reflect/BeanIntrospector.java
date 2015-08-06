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

import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;
import java.util.Collections;

/**
 * This class is wrapper around <code>java.beans.Introspector</code> that
 * allows to collect information about annotations.
 * 
 * @author Fox Mulder
 */
public final class BeanIntrospector {
    
    /**
     * The type of bean.
     */
    private final Class<?> type;
    
    /**
     * Annotations that are placed on the bean class.
     */
    private final Map<Class<?>, Annotation> annotations;
    
    /**
     * Property descriptiors.
     */
    private final Map<String, Property> properties;
    
    /**
     * Constructs a new <code>BeanIntrospector</code> for the specified type.
     * 
     * @param type The type of bean.
     */
    private BeanIntrospector(Class<?> type) {
        Map<Class<?>, Annotation> annotations = new LinkedHashMap<Class<?>, Annotation>();
        Map<String, Property> properties = new LinkedHashMap<String, Property>();
        
        Class<?> supertype = (this.type = type).getSuperclass();
        if (!(supertype == null || supertype == Object.class)) {
            BeanIntrospector parent = getInstance(supertype);
            annotations.putAll(parent.annotations);
            properties.putAll(parent.properties);
        }
        
        overrideIntfAnnotations(type, annotations);
        overrideAnnotations(type, annotations);
        findProperties(type, properties);
        
        this.annotations = Collections.unmodifiableMap(annotations);
        this.properties = Collections.unmodifiableMap(properties);
    }
    
    /**
     * Returns the type of bean.
     * 
     * @return The type of bean.
     */
    public Class<?> getType() {
        return type;
    }
    
    /**
     * Determines if this bean type has annotation with the specified type.
     * 
     * @param annotationType The type of annotation.
     * @return <code>true</code> if this bean type has annotation with the
     *         specified type; <code>false</code> otherwise.
     */
    public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
        return annotations.containsKey(annotationType);
    }
    
    /**
     * Returns bean annotation for the specified annotation type or
     * <code>null</code> if annotation is not defined.
     * 
     * @param annotationType The type of annotation.
     * @return Bean annotation for the specified annotation type or
     *         <code>null</code> if annotation is not defined.
     */
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return annotationType.cast(annotations.get(annotationType));
    }
    
    /**
     * Returns array of annotations defined on the bean type.
     * 
     * @return Array of annotations defined on the bean type.
     */
    public Annotation[] getAnnotations() {
        return annotations.values().toArray(new Annotation[annotations.size()]);
    }
    
    /**
     * Returns set of property names.
     * 
     * @return Set of property names.
     */
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }
    
    /**
     * Determines if this bean type has property with the specified name.
     * 
     * @param name Property name.
     * @return <code>true</code> if this bean type has property with the
     *         specified name; <code>false</code> otherwise.
     */
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }
    
    /**
     * Returns property descriptor for the specified property name or
     * <code>null</code> if property is not exists.
     * 
     * @param name Property name.
     * @return Property descriptor for the specified property name or
     *         <code>null</code> if property is not exists.
     */
    public Property getProperty(String name) {
        return properties.get(name);
    }
    
    /**
     * Returns collection of all the properties defined on the bean.
     * 
     * @return Collection of all the properties defined on the bean.
     */
    public Collection<Property> getProperties() {
        return properties.values();
    }
    
    /**
     * Returns string representing this bean type.
     * 
     * @return String representing this bean type.
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(type);
        if (annotations.size() > 0) {
            buf.append('\u0020');
            toString(annotations, buf);
        }
        for (Property property : properties.values()) {
            buf.append('\n').append(" - ");
            property.toString(buf);
        }
        return buf.toString();
    }
    
    /**
     * Searches for the properties in the specified type and puts them to the
     * specified property map.
     * 
     * @param type The type of bean.
     * @param properties Property map.
     */
    static void findProperties(Class<?> type, Map<String, Property> properties) {
        try {
            BeanInfo info = Introspector.getBeanInfo(type);
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors())
                if (!(descriptor instanceof IndexedPropertyDescriptor)) {
                    String propertyName = descriptor.getName();
                    if (acceptProperty(type, propertyName)) {
                        Class<?> propertyType = descriptor.getPropertyType();
                        Method readMethod = descriptor.getReadMethod();
                        Method writeMethod = descriptor.getWriteMethod();
                        PropertyGetter getter = PropertyGetter.newGetter(readMethod);
                        PropertySetter setter = PropertySetter.newSetter(writeMethod);
                        Property property = properties.get(propertyName);
                        Map<Class<?>, Annotation> annotations = new LinkedHashMap<Class<?>, Annotation>();
                        if (property != null) {
                            annotations.putAll(property.annotations);
                        }
                        overrideAnnotations(readMethod, annotations);
                        overrideAnnotations(writeMethod, annotations);
                        property = new Property(propertyName, propertyType, getter, setter, annotations);
                        properties.put(propertyName, property);
                    }
                }
        } catch (IntrospectionException e) {
            throw new InternalError(); // should never happen
        }
    }
    
    /**
     * Determines if property with the specified type and name should be
     * accepted.
     * 
     * @param type Property type.
     * @param name Property name.
     * @return <code>true</code> if property with the specified type and name
     *         should be accepted; <code>false</code> otherwise.
     */
    static boolean acceptProperty(Class<?> type, String name) {
        if ("class".equals(name)) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Overrides the specified annotations with annotations of the specified
     * annotated element.
     * 
     * @param source Annotated element that contains overriding annotations.
     * @param annotations Annotations to override.
     */
    static void overrideAnnotations(AnnotatedElement source, Map<Class<?>, Annotation> annotations) {
        if (source != null) {
            for (Annotation annotation : source.getAnnotations()) {
                annotations.put(annotation.annotationType(), annotation);
            }
        }
    }
    
    /**
     * Overrides the specified annotations with annotations of interfaces of
     * the specified type.
     * 
     * @param type The type whose interfaces should be scanned.
     * @param annotations Annotations to override.
     */
    static void overrideIntfAnnotations(Class<?> type, Map<Class<?>, Annotation> annotations) {
        for (Class<?> intf : type.getInterfaces()) {
            overrideIntfAnnotations(intf, annotations);
            overrideAnnotations(intf, annotations);
        }
    }
    
    /**
     * Appends string representations of the specified annotations to the
     * specified buffer.
     * 
     * @param annotations Annotations which string representations should be
     *        appended to the buffer.
     * @param buf Buffer to append.
     */
    static void toString(Map<Class<?>, Annotation> annotations, StringBuilder buf) {
        Iterator<Annotation> itr = annotations.values().iterator();
        if (itr.hasNext()) {
            buf.append('(');
            buf.append(itr.next());
            while (itr.hasNext()) {
                buf.append(',').append(itr.next());
            }
            buf.append(')');
        }
    }
    
    // Property
    
    /**
     * Property descriptor.
     * 
     * @author Fox Mulder
     */
    public static final class Property {
        
        /**
         * Property name.
         */
        private final String name;
        
        /**
         * Property type.
         */
        private final Class<?> type;
        
        /**
         * Property value getter.
         */
        private final PropertyGetter getter;
        
        /**
         * Property value setter.
         */
        private final PropertySetter setter;
        
        /**
         * Annotations that are placed on the property.
         */
        private final Map<Class<?>, Annotation> annotations;
        
        /**
         * Constructs a new <code>BeanIntrospector.Property</code> with the
         * specified name, type, getter, setter and annotations.
         * 
         * @param name Property name.
         * @param type Property type.
         * @param getter Property value getter.
         * @param setter Property value setter.
         * @param annotations Annotations that are placed on the property.
         */
        private Property(String name, Class<?> type, PropertyGetter getter, PropertySetter setter,
                Map<Class<?>, Annotation> annotations) {
            this.name = name;
            this.type = type;
            this.getter = getter;
            this.setter = setter;
            this.annotations = annotations;
        }
        
        /**
         * Returns name of this property.
         * 
         * @return Name of this property.
         */
        public String getName() {
            return name;
        }
        
        /**
         * Returns raw type of this property.
         * 
         * @return Raw type of this property.
         */
        public Class<?> getType() {
            return type;
        }
        
        /**
         * Returns generic type of this property.
         * 
         * @return Generic type of this property.
         */
        public Type getGenericType() {
            return isReadable() ? getter.getGenericType() : setter.getGenericType();
        }
        
        /**
         * Returns property value getter.
         * 
         * @return Property value getter.
         */
        public PropertyGetter getGetter() {
            return getter;
        }
        
        /**
         * Returns property value setter.
         * 
         * @return Property value setter.
         */
        public PropertySetter getSetter() {
            return setter;
        }
        
        /**
         * Determines if this property is readable.
         * 
         * @return <code>true</code> if this property is readable;
         *         <code>false</code> otherwise.
         */
        public boolean isReadable() {
            return getter != null;
        }
        
        /**
         * Determines if this property is writeable.
         * 
         * @return <code>true</code> if this property is writeable;
         *         <code>false</code> otherwise.
         */
        public boolean isWriteable() {
            return setter != null;
        }
        
        /**
         * Determines if this property has annotation with the specified type.
         * 
         * @param annotationType The type of annotation.
         * @return <code>true</code> if this property has annotation with the
         *         specified type; <code>false</code> otherwise.
         */
        public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
            return annotations.containsKey(annotationType);
        }
        
        /**
         * Returns this property annotation for the specified annotation type
         * or <code>null</code> if annotation is not defined.
         * 
         * @param annotationType The type of annotation.
         * @return This property annotation for the specified annotation type
         *         or <code>null</code> if annotation is not defined.
         */
        public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
            return annotationType.cast(annotations.get(annotationType));
        }
        
        /**
         * Returns array of annotations defined on this property.
         * 
         * @return Array of annotations defined on this property.
         */
        public Annotation[] getAnnotations() {
            return annotations.values().toArray(new Annotation[annotations.size()]);
        }
        
        /**
         * Returns string representing this property.
         * 
         * @return String representing this property.
         */
        public String toString() {
            StringBuilder buf = new StringBuilder();
            toString(buf);
            return buf.toString();
        }
        
        /**
         * Appends string representing this property to the specified buffer.
         * 
         * @param buf Buffer to append.
         */
        public void toString(StringBuilder buf) {
            buf.append(type.getName());
            buf.append('\u0020');
            buf.append(name);
            if (annotations.size() > 0) {
                buf.append('\u0020');
                BeanIntrospector.toString(annotations, buf);
            }
        }
        
    }
    
    // Factory
    
    /**
     * <code>BeanIntrospector</code> cache.
     */
    private static final Map<Class<?>, BeanIntrospector> cache =
        new WeakHashMap<Class<?>, BeanIntrospector>();
    
    /**
     * Returns <code>BeanIntrospector</code> instance for the specified type.
     * 
     * @param type The type of bean.
     * @return <code>BeanIntrospector</code> instance for the specified type.
     */
    public static synchronized BeanIntrospector getInstance(Class<?> type) {
        BeanIntrospector introspector = cache.get(type);
        if (introspector == null) {
            cache.put(type, introspector = new BeanIntrospector(type));
        }
        return introspector;
    }
    
}
