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

package org.foxlabs.util.resource;

import java.net.URL;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * This class used to load service providers (Java Service Provider Interface).
 * 
 * @author Fox Mulder
 */
public abstract class Service {
    
    // Can't be inherited.
    private Service() {
        super();
    }
    
    /**
     * SPI resources directory.
     */
    public static final String DIRECTORY = "META-INF/services/";
    
    /**
     * Searches for service provider classes of the specified category and
     * returns iterator over their instances.
     * 
     * @param category Service provider category to search.
     * @return Iterator over service provider instances of the specified
     *         category.
     * @throws ResourceError if an error occured when reading SPI resources.
     * @see #lookup(Class, ClassLoader)
     */
    public static <P> Iterator<P> lookup(Class<P> category) {
        return lookup(category, ResourceHelper.getClassLoader());
    }
    
    /**
     * Searches for service provider classes of the specified category using
     * the specified class loader and returns iterator over their instances.
     * 
     * @param category Service provider category to search.
     * @param cl Class loader to be used for service provider class loading.
     * @return Iterator over service provider instances of the specified
     *         category.
     * @throws ResourceError if an error occured when reading SPI resources.
     */
    public static <P> Iterator<P> lookup(Class<P> category, ClassLoader cl) {
        return new ProviderItr<P>(category, cl);
    }
    
    /**
     * Searches for service provider classes of the specified category and
     * returns first service provider instance.
     * 
     * @param category Service provider category to search.
     * @return First service provider instance or <code>null</code> if there
     *         are no service provider classes found.
     * @throws ResourceError if an error occured when reading SPI resources.
     * @see #lookupFirst(Class, ClassLoader)
     */
    public static <P> P lookupFirst(Class<P> category) {
        return lookupFirst(category, ResourceHelper.getClassLoader());
    }
    
    /**
     * Searches for service provider classes of the specified category using
     * the specified class loader and returns first service provider instance.
     * 
     * @param category Service provider category to search.
     * @param cl Class loader to be used for service provider class loading.
     * @return First service provider instance or <code>null</code> if there
     *         are no service provider classes found.
     * @throws ResourceError if an error occured when reading SPI resources.
     */
    public static <P> P lookupFirst(Class<P> category, ClassLoader cl) {
        ProviderItr<P> itr = new ProviderItr<P>(category, cl);
        return itr.hasNext() ? itr.next() : null;
    }
    
    /**
     * Searches for service provider classes of the specified category and
     * returns iterator over them.
     * 
     * @param category Service provider category to search.
     * @return Iterator over service provider classes of the specified category.
     * @throws ResourceError if an error occured when reading SPI resources.
     * @see #lookupClasses(Class, ClassLoader)
     */
    public static <P> Iterator<Class<? extends P>> lookupClasses(Class<P> category) {
        return lookupClasses(category, ResourceHelper.getClassLoader());
    }
    
    /**
     * Searches for service provider classes of the specified category using
     * the specified class loader and returns iterator over them.
     * 
     * @param category Service provider category to search.
     * @param cl Class loader to be used for service provider class loading.
     * @return Iterator over service provider classes of the specified category.
     * @throws ResourceError if an error occured when reading SPI resources.
     */
    public static <P> Iterator<Class<? extends P>> lookupClasses(Class<P> category, ClassLoader cl) {
        return new ProviderClassItr<P>(category, cl);
    }
    
    // ProviderItr
    
    /**
     * Lazy iterator over service provider instances.
     * 
     * @author Fox Mulder
     */
    protected static class ProviderItr<P> implements Iterator<P> {
        
        /**
         * Lazy iterator over service provider classes.
         */
        private final ProviderClassItr<? extends P> clsItr;
        
        /**
         * Cache of already loaded service provider instances.
         */
        private final Map<Class<?>, P> cache = new HashMap<Class<?>, P>();
        
        /**
         * Constructs a new <code>ProviderItr</code> with the specified service
         * provider category and class loader.
         * 
         * @param category Service provider category to search.
         * @param cl Class loader to be used for service provider class loading.
         * @throws ResourceError if an error occured when reading SPI resources.
         */
        private ProviderItr(Class<P> category, ClassLoader cl) {
            this.clsItr = new ProviderClassItr<P>(category, cl);
        }
        
        /**
         * Determines if the iteration has more elements.
         * 
         * @return <code>true</code> if the iteration has more elements;
         *         <code>false</code> otherwise.
         * @throws ResourceError if an error occured when reading SPI resources
         *         or service provider class is not found.
         */
        @Override
        public boolean hasNext() {
            return clsItr.hasNext();
        }
        
        /**
         * Returns next service provider instance.
         * 
         * @return Next service provider instance.
         * @throws ResourceError if an error occured when reading SPI resources
         *         or instantiation of service provider fails.
         */
        @Override
        public P next() {
            Class<? extends P> cls = clsItr.next();
            P provider = cache.get(cls);
            if (provider != null) {
                return provider;
            }
            
            try {
                provider = cls.newInstance();
                cache.put(cls, provider);
                return provider;
            } catch (Throwable e) {
                throw new ResourceError("Can't instantiate \"" + cls.getName() +
                        "\" service provider (" + clsItr.file + ")", e);
            }
        }
        
        /**
         * Unsupported operation.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    // ProviderClassItr
    
    /**
     * Lazy iterator over provider classes.
     * 
     * @author Fox Mulder
     */
    protected static class ProviderClassItr<P> implements Iterator<Class<? extends P>> {
        
        /**
         * Service provider category.
         */
        private final Class<P> category;
        
        /**
         * Class loader to be used for service provider class loading.
         */
        private final ClassLoader loader;
        
        /**
         * URLs of all service provider configuration files on the classpath.
         */
        private Enumeration<URL> files;
        
        /**
         * URL of the current service provider configuration file.
         */
        private URL file;
        
        /**
         * Reader of the current service provider configuration file.
         */
        private BufferedReader reader;
        
        /**
         * Next service provider class.
         */
        private Class<? extends P> next;
        
        /**
         * Cache of already loaded service provider classes.
         */
        private final Map<String, Class<? extends P>> cache = new HashMap<String, Class<? extends P>>();
        
        /**
         * Constructs a new <code>ProviderClassItr</code> with the specified
         * service provider category and class loader.
         * 
         * @param category Service provider category to search.
         * @param cl Class loader to be used for service provider class loading.
         * @throws ResourceError if an error occured when reading SPI resources.
         */
        private ProviderClassItr(Class<P> category, ClassLoader cl) {
            try {
                this.files = cl.getResources(DIRECTORY + category.getName());
                this.category = category;
                this.loader = cl;
            } catch (IOException e) {
                throw new ResourceError("An error occured when locating category \"" +
                    category.getName() + "\" provider-configuration files", e);
            }
        }
        
        /**
         * Determines if the iteration has more elements.
         * 
         * @return <code>true</code> if the iteration has more elements;
         *         <code>false</code> otherwise.
         * @throws ResourceError if an error occured when reading SPI resources
         *         or service provider class is not found.
         */
        @Override
        public boolean hasNext() {
            while (next == null) {
                try {
                    if (reader == null) {
                        if (files.hasMoreElements()) {
                            file = files.nextElement();
                            reader = new BufferedReader(new InputStreamReader(file.openStream(), "UTF-8"));
                        } else {
                            return false;
                        }
                    }
                    boolean fail = true;
                    try {
                        String line = reader.readLine();
                        fail = false;
                        if (line == null) {
                            reader.close();
                            reader = null;
                        } else {
                            next = parseLine(line);
                        }
                    } finally {
                        if (fail) {
                            reader.close();
                        }
                    }
                } catch (IOException e) {
                    throw new ResourceError("An error occured when reading provider-configuration file (" +
                            file + ")", e);
                }
            }
            return true;
        }
        
        /**
         * Returns next service provider instance.
         * 
         * @return Next service provider class.
         * @throws ResourceError if an error occured when reading SPI resources
         *         or service provider class is not found.
         */
        @Override
        public Class<? extends P> next() {
            if (hasNext()) {
                Class<? extends P> cls = next;
                next = null;
                return cls;
            }
            throw new NoSuchElementException();
        }
        
        /**
         * Unsupported operation.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Parses single line that may contain service provider class name.
         * 
         * @param line Line to parse.
         * @return Service provider class or <code>null</code> if the specified
         *         line contains comment.
         * @throws ResourceError if service provider class is not found.
         */
        private Class<? extends P> parseLine(String line) {
            int index = line.indexOf('#');
            String name = (index < 0 ? line : line.substring(0, index)).trim();
            if (name.isEmpty()) {
                return null;
            }
            
            Class<? extends P> cls = cache.get(name);
            if (cls != null) {
                return cls;
            }
            
            try {
                cls = loader.loadClass(name).asSubclass(category);
                cache.put(name, cls);
                return cls;
            } catch (ClassNotFoundException e) {
                throw new ResourceError("Service provider \"" + name +
                    "\" is not found (" + file + ")");
            } catch (ClassCastException e) {
                throw new ResourceError("Service provider \"" + name +
                    "\" should implement " + category.getName() +
                    " (" + file + ")");
            }
        }
        
    }
    
}
