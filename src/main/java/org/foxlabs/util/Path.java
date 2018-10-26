/*
 * Copyright (C) 2018 FoxLabs
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

import java.io.File;

/**
 * Holds abstract pathname. The abstract pathname does not follow rules of any
 * file system, URL syntax and so on. The format of valid pathname is described
 * in the {@link #parse(String)} method.
 * 
 * @author Fox Mulder
 */
public final class Path implements Comparable<Path>, java.io.Serializable {
    private static final long serialVersionUID = 7453763729390828576L;
    
    /**
     * Path separator character.
     */
    public static final char SEPARATOR = '/';
    
    /**
     * Root path (<code>'/'</code>).
     */
    public static final Path ROOT = new Path(Character.toString(SEPARATOR));
    
    /**
     * Pathname of this path.
     */
    private final String pathname;
    
    /**
     * Name part of the pathname.
     */
    private transient String name;
    
    /**
     * Extension part of the pathname.
     */
    private transient String extension;
    
    /**
     * Parent path.
     */
    private transient Path parent;
    
    /**
     * Constructs a new path with the specified pathname.
     * 
     * @param pathname Pathname of this path.
     */
    private Path(String pathname) {
        this.pathname = pathname;
    }
    
    /**
     * Returns pathname of this path.
     * 
     * @return Pathname of this path.
     */
    public String getPathname() {
        return pathname;
    }
    
    /**
     * Returns relative pathname of this path (i.e. pathname without leading
     * <code>'/'</code> character).
     * 
     * @return Relative pathname of this path.
     */
    public String getRelativePathname() {
        return pathname.substring(1);
    }
    
    /**
     * Returns name part of the pathname. If this path is root path then empty
     * string will be returned.
     * 
     * @return Name part of the pathname.
     */
    public String getName() {
        if (name == null) {
            int index = pathname.lastIndexOf(SEPARATOR);
            name = pathname.substring(index + 1);
        }
        return name;
    }
    
    /**
     * Returns extension part of the pathname. If this path has no extension
     * then empty string will be returned.
     * 
     * @return Extension part of this path.
     */
    public String getExtension() {
        if (extension == null) {
            int index = getName().lastIndexOf('.');
            extension = index < 0 ? "" : name.substring(index + 1);
        }
        return extension;
    }
    
    /**
     * Returns parent path of this path. If this path is root path then parent
     * path is <code>null</code>.
     * 
     * @return Parent path of this path.
     */
    public Path getParent() {
        if (!(parent != null || isRoot())) {
            int index = pathname.lastIndexOf(SEPARATOR);
            parent = index == 0 ? ROOT : new Path(pathname.substring(0, index));
        }
        return parent;
    }
    
    /**
     * Determines if this path is root path (i.e. <code>'/'</code>).
     * 
     * @return <code>true</code> if this path is root path;
     *         <code>false</code> otherwise.
     */
    public boolean isRoot() {
        return this == ROOT;
    }
    
    /**
     * Appends the specified pathname to this path and returns new path.
     * 
     * @param pathname Pathname to append.
     * @return New path concatenated from this path and the specified pathname.
     * @throws IllegalArgumentException if the specified pathname is invalid.
     */
    public Path append(String pathname) {
        return append(parse(pathname));
    }
    
    /**
     * Appends the specified path to this path and returns new one.
     * 
     * @param path Path to append.
     * @return New path concatenated from this path and the specified one.
     */
    public Path append(Path path) {
        if (path == null || path.isRoot()) {
            return this;
        } else if (isRoot()) {
            return path;
        } else {
            final Path result = new Path(pathname + path.pathname);
            result.name = path.name;
            result.extension = path.extension;
            return result;
        }
    }
    
    /**
     * Returns file representing this path.
     * 
     * @return File representing this path.
     */
    public File toFile() {
        return new File(pathname);
    }
    
    /**
     * Appends this path to the specified parent file.
     * 
     * @return New file concatenated from the specified parent file and this path.
     */
    public File toFile(File parent) {
        return isRoot() ? parent : new File(parent, pathname);
    }
    
    /**
     * Returns hash code of this path.
     * 
     * @return Hash code of this path.
     */
    @Override
    public int hashCode() {
        return pathname.hashCode();
    }
    
    /**
     * Determines if this path is equal to the specified one. Two paths are
     * equal if their pathnames are exactly the same.
     * 
     * @param obj Path to compare with this path.
     * @return <code>true</code> if this path is equal to the specified one;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Path && ((Path) obj).pathname.equals(pathname);
    }
    
    /**
     * Compares this path with the specified one. Comparison is case sensitive.
     * 
     * @param other Path to compare with this path.
     * @return A negative integer, zero, or a positive integer as this path
     *         is less than, equal to, or greater than the specified one.
     */
    @Override
    public int compareTo(Path other) {
        return pathname.compareTo(other.pathname);
    }
    
    /**
     * Returns pathname of this path.
     * 
     * @return Pathname of this path.
     */
    @Override
    public String toString() {
        return pathname;
    }
    
    /**
     * Replaces deserialized {@link #ROOT} constant.
     * 
     * @return {@link #ROOT} or this path instance.
     * @throws java.io.ObjectStreamException never.
     */
    private Object readResolve() throws java.io.ObjectStreamException {
        return isRoot() ? ROOT : this;
    }
    
    /**
     * Returns path instance for the specified pathname.
     * 
     * <p>Pathname syntax:</p>
     * <ul>
     *   <li>All <code>'\'</code> characters will be replaced with <code>'/'</code> character.</li>
     *   <li>Empty parts like <code>'//'</code> are not allowed.</li>
     *   <li>Pathname always starts with <code>'/'</code> character.</li>
     *   <li>Pathname never ends with <code>'/'</code> character.</li>
     * </ul>
     * 
     * @return Path instance for the specified pathname.
     * @throws IllegalArgumentException if the specified pathname is invalid.
     */
    public static Path parse(String pathname) {
        if (pathname.equals(ROOT.pathname) || pathname.equals('\\')) {
            return ROOT;
        }
        pathname = pathname.replace('\\', SEPARATOR);
        if (pathname.contains("//")) {
            throw new IllegalArgumentException("Invalid pathname: \"" + pathname + "\"");
        }
        if (pathname.charAt(0) != SEPARATOR) {
            pathname = SEPARATOR + pathname;
        }
        if (pathname.charAt(pathname.length() - 1) == SEPARATOR) {
            pathname = pathname.substring(0, pathname.length() - 1);
        }
        return new Path(pathname);
    }
    
}
