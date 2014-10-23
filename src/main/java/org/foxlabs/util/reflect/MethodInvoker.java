/* 
 * Copyright (C) 2014 FoxLabs
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

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.foxlabs.util.resource.ResourceHelper;

/**
 * An abstract utility class that allows to invoke methods on objects using
 * specific libraries like CGLIB if they are present on the classpath.
 * 
 * @author Fox Mulder
 */
public abstract class MethodInvoker {
    
    /**
     * Invokes concrete method on the specified object with the specified
     * arguments.
     * 
     * @param obj The object the underlying method is invoked from.
     * @param args The arguments used for the method call.
     * @return The result of the underlying method invocation.
     * @throws Exception if method invocation fails.
     */
    public abstract Object invoke(Object obj, Object... args) throws Throwable;
    
    // Default
    
    /**
     * A simple wrapper around <code>java.lang.reflect.Method</code>.
     * 
     * @author Fox Mulder
     */
    static final class Default extends MethodInvoker {
        
        /**
         * The underlying method.
         */
        private final Method method;
        
        /**
         * Constructs a new <code>MethodInvoker.Default</code> with the
         * specified underlying method.
         * 
         * @param method The underlying method.
         */
        private Default(Method method) {
            method.setAccessible(true);
            this.method = method;
        }
        
        /**
         * Invokes the underlying method on the specified object with the
         * specified arguments.
         * 
         * @param obj The object the underlying method is invoked from.
         * @param args The arguments used for the method call.
         * @return The result of the underlying method invocation.
         * @throws Exception if method invocation fails.
         */
        @Override
        public Object invoke(Object obj, Object... args) throws Throwable {
            try {
                return method.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
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
    
    // CGLIB
    
    /**
     * A <code>MethodInvoker</code> that uses CGLIB library to invoke methods.
     * 
     * @author Fox Mulder
     */
    static final class Fast extends MethodInvoker {
        
        /**
         * The underlying method.
         */
        private final FastMethod method;
        
        /**
         * Constructs a new <code>MethodInvoker.Default</code> with the
         * specified underlying method.
         * 
         * @param method The underlying method.
         */
        private Fast(Method method) {
            method.setAccessible(true);
            this.method = FastClass.create(method.getDeclaringClass()).getMethod(method);
        }
        
        /**
         * Invokes the underlying method on the specified object with the
         * specified arguments.
         * 
         * @param obj The object the underlying method is invoked from.
         * @param args The arguments used for the method call.
         * @return The result of the underlying method invocation.
         * @throws Exception if method invocation fails.
         */
        @Override
        public Object invoke(Object obj, Object... args) throws Throwable {
            try {
                return method.invoke(obj, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
        
        /**
         * Returns string representing signature of the method.
         * 
         * @return String representing signature of the method.
         */
        public String toString() {
            return method.getJavaMethod().toGenericString();
        }
        
    }
    
    /**
     * Creates a new <code>MethodInvoker</code> for the specified method.
     * 
     * @return A new <code>MethodInvoker</code> for the specified method.
     */
    public static MethodInvoker newInvoker(Method method) {
        return CGLIB ? new Fast(method) : new Default(method);
    }
    
    /**
     * Determines if CGLIB is available on the classpath.
     */
    static final boolean CGLIB = ResourceHelper.getClassLoader()
            .getResource("net/sf/cglib/reflect/FastClass.class") != null;
    
}
