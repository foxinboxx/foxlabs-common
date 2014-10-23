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

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import org.foxlabs.util.ByteBuffer;

/**
 * Helper class used for working with resources.
 * 
 * @author Fox Mulder
 */
public abstract class ResourceHelper {
    
    /**
     * Size of buffer used for loading content of resources.
     */
    private static final int BUFFER_SIZE = 8192;
    
    /**
     * Default encoding used for loading content of character resources.
     */
    private static final String DEFAULT_ENCODING = "utf-8";
    
    /**
     * Returns class loader.
     * 
     * <p>This method tries to resolve class loader in the following sequence:
     * <ul>
     *   <li>Context class loader for the current thread.</li>
     *   <li>Class loader of this class.</li>
     *   <li>System class loader.</li>
     * </ul>
     * If all attempts above return <code>null</code> then this method returns
     * <code>null</code>.</p>
     * 
     * @return Class loader.
     */
    public static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ResourceHelper.class.getClassLoader();
            if (cl == null) {
                cl = ClassLoader.getSystemClassLoader();
            }
        }
        return cl;
    }
    
    /**
     * Returns {@link #getClassLoader()} if the specified class loader is
     * <code>null</code>; returns the specified class loader otherwise.
     * 
     * @param cl Class loader.
     * @return {@link #getClassLoader()} if the specified class loader is
     *         <code>null</code>; returns the specified class loader otherwise.
     */
    public static ClassLoader getClassLoader(ClassLoader cl) {
        return cl == null ? getClassLoader() : cl;
    }
    
    /**
     * Returns package name for the specified class.
     * 
     * @param c Class which package name should be returned.
     * @return Package name for the specified class.
     */
    public static String getPackageName(Class<?> c) {
        return c.getPackage().getName();
    }
    
    /**
     * Returns resource path for package of the specified class.
     * 
     * @param c Class contained in the package.
     * @return Resource path for package of the specified class.
     */
    public static String getResourcePath(Class<?> c) {
        return getPackageName(c).replace('.', '/');
    }
    
    /**
     * Returns resource URL for the specified resource name.
     * 
     * @param name Resource name.
     * @return Resource URL for the specified resource name.
     * @throws ResourceError if resource with the specified name is not found.
     * @see #getResourceURL(String, ClassLoader)
     */
    public static URL getResourceURL(String name) {
        return getResourceURL(name, getClassLoader());
    }
    
    /**
     * Returns resource URL for the specified resource name using the specified
     * class loader.
     * 
     * @param name Resource name.
     * @param cl Class loader to be used for resource loading.
     * @return Resource URL for the specified resource name.
     * @throws ResourceError if resource with the specified name is not found.
     */
    public static URL getResourceURL(String name, ClassLoader cl) {
        URL url = cl.getResource(name);
        if (url == null) {
            throw new ResourceError("Unable to find resource: \"" + name + "\"");
        }
        return url;
    }
    
    /**
     * Reads content of resource with the specified name as binary data.
     * 
     * @param name Resource name.
     * @return Content of resource with the specified name as binary data.
     * @throws ResourceError if an error occurred when reading resource.
     * @see #readBinaryResource(String, ClassLoader)
     */
    public static byte[] readBinaryResource(String name) {
        return readBinaryResource(name, getClassLoader());
    }
    
    /**
     * Reads content of resource with the specified name as binary data using
     * the specified class loader.
     * 
     * @param name Resource name.
     * @param cl Class loader to be used for resource loading.
     * @return Content of resource with the specified name as binary data.
     * @throws ResourceError if an error occurred when reading resource.
     */
    public static byte[] readBinaryResource(String name, ClassLoader cl) {
        URL url = getResourceURL(name, cl);
        try {
            InputStream in = url.openStream();
            try {
                byte[] chunk = new byte[BUFFER_SIZE];
                ByteBuffer buf = new ByteBuffer(chunk.length);
                for (int len = in.read(chunk); len > 0; len = in.read(chunk)) {
                    buf.append(chunk, 0, len);
                }
                return buf.getBytes();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new ResourceError("Error reading resource: \"" + name + "\"", e);
        }
    }
    
    /**
     * Reads content of resource with the specified name as character data.
     * 
     * @param name Resource name.
     * @return Content of resource with the specified name as character data.
     * @throws ResourceError if an error occurred when reading resource.
     * @see #readTextResource(String, String, ClassLoader)
     */
    public static String readTextResource(String name) {
        return readTextResource(name, DEFAULT_ENCODING, getClassLoader());
    }
    
    /**
     * Reads content of resource with the specified name as character data
     * using the specified class loader.
     * 
     * @param name Resource name.
     * @param cl Class loader to be used for resource loading.
     * @return Content of resource with the specified name as character data.
     * @throws ResourceError if an error occurred when reading resource.
     * @see #readTextResource(String, String, ClassLoader)
     */
    public static String readTextResource(String name, ClassLoader cl) {
        return readTextResource(name, DEFAULT_ENCODING, cl);
    }
    
    /**
     * Reads content of resource with the specified name as character data
     * using the specified character encoding.
     * 
     * @param name Resource name.
     * @param encoding Character encoding.
     * @return Content of resource with the specified name as character data.
     * @throws ResourceError if an error occurred when reading resource.
     * @see #readTextResource(String, String, ClassLoader)
     */
    public static String readTextResource(String name, String encoding) {
        return readTextResource(name, encoding, getClassLoader());
    }
    
    /**
     * Reads content of resource with the specified name as character data
     * using the specified character encoding and class loader.
     * 
     * @param name Resource name.
     * @param encoding Character encoding.
     * @param cl Class loader to be used for resource loading.
     * @return Content of resource with the specified name as character data.
     * @throws ResourceError if an error occurred when reading resource.
     */
    public static String readTextResource(String name, String encoding, ClassLoader cl) {
        URL url = getResourceURL(name, cl);
        try {
            InputStream in = url.openStream();
            try {
                InputStreamReader cin = new InputStreamReader(in, encoding);
                char[] chunk = new char[BUFFER_SIZE];
                StringBuilder buf = new StringBuilder(chunk.length);
                for (int len = cin.read(chunk); len > 0; len = cin.read(chunk)) {
                    buf.append(chunk, 0, len);
                }
                return buf.toString();
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new ResourceError("Error reading resource: \"" + name + "\"", e);
        }
    }
    
    /**
     * Searches for classes in the specified package.
     * 
     * @param pkg Package name where to search classes.
     * @param recurse Determines if search should be performed recursively.
     * @return Array of found classes.
     */
    public static Class<?>[] findClasses(String pkg, boolean recurse) {
        return findClasses(pkg, getClassLoader(), recurse);
    }
    
    /**
     * Searches for classes in the specified package using the specified class
     * loader.
     * 
     * @param pkg Package name where to search classes.
     * @param cl Class loader to be used for class loading.
     * @param recurse Determines if search should be performed recursively.
     * @return Array of found classes.
     */
    public static Class<?>[] findClasses(String pkg, ClassLoader cl, boolean recurse) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        URL url = cl.getResource(pkg.replace('.', '/'));
        if (url != null) {
            File dir = new File(url.getFile());
            if (dir.isDirectory()) {
                findClasses(dir, pkg, cl, classes, recurse);
            }
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }
    
    /**
     * Searches for classes in the specified directory and appends found
     * classes to the list.
     * 
     * @param dir Directory where to search classes.
     * @param pkg Package name.
     * @param cl Class loader to be used for class loading.
     * @param classes List of found classes.
     * @param recurse Determines if search should be performed recursively.
     */
    private static void findClasses(File dir, String pkg, ClassLoader cl, List<Class<?>> classes, boolean recurse) {
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".class")) {
                    String className = pkg + "." + name.substring(0, name.length() - 6);
                    try {
                        classes.add(cl.loadClass(className));
                    } catch (ClassNotFoundException e) {}
                }
            } else if (recurse && file.isDirectory()) {
                findClasses(file, pkg + "." + file.getName(), cl, classes, recurse);
            }
        }
    }
    
}
