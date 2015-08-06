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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import java.text.MessageFormat;

/**
 * This class is lightweight wrapper around <code>java.util.ResourceBundle</code>.
 * 
 * @author Fox Mulder
 */
public final class MessageBundle {
    
    /**
     * Message bundle base name.
     */
    private final String baseName;
    
    /**
     * Class loader to be used for resource loading.
     */
    private final ClassLoader classLoader;
    
    /**
     * Parent message bundle.
     */
    private final MessageBundle parentBundle;
    
    /**
     * Constructs a new message bundle.
     * 
     * @param name Message bundle base name.
     * @param loader Class loader to be used for resource loading.
     * @param parent Parent message bundle.
     */
    private MessageBundle(String name, ClassLoader loader, MessageBundle parent) {
        this.baseName = name;
        this.classLoader = loader;
        this.parentBundle = parent;
    }
    
    /**
     * Returns message bundle base name.
     * 
     * @return Message bundle base name.
     */
    public String getBaseName() {
        return baseName;
    }
    
    /**
     * Returns class loader to be used for loading resources.
     * 
     * @return Class loader to be used for loading resources.
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /**
     * Returns parent message bundle.
     * 
     * @return Parent message bundle.
     */
    public MessageBundle getParentBundle() {
        return parentBundle;
    }
    
    /**
     * Determines whether the specified message bundle is either the same or
     * is a parent bundle of this bundle.
     * 
     * @param bundle Message bundle to be checked.
     * @return <code>true</code> if the specified message bundle is either the
     *         same or is a parent bundle of this bundle; <code>false</code>
     *         otherwise.
     */
    public boolean isAssignableFrom(MessageBundle bundle) {
        return baseName.equals(bundle.baseName) && classLoader.equals(bundle.classLoader)
            || parentBundle != null && parentBundle.isAssignableFrom(bundle);
    }
    
    /**
     * Determines whether the specified message key is contained in this
     * message bundle or its parent bundles.
     * 
     * @param key Message key.
     * @return <code>true</code> if the specified message key is contained in
     *         this message bundle or its parent bundles; <code>false</code>
     *         otherwise.
     */
    public boolean contains(String key) {
        return getResourceBundle(Locale.ROOT).containsKey(key)
            || parentBundle != null && parentBundle.contains(key);
    }
    
    /**
     * Returns message for the specified key and default locale.
     * 
     * @param key Message key.
     * @return Message for the specified key and default locale.
     * @throws MissingResourceException if message for the specified key is
     *         missing.
     * @see #get(String, Locale)
     */
    public String get(String key) {
        return get(key, Locale.getDefault());
    }
    
    /**
     * Returns message for the specified key and locale.
     * 
     * @param key Message key.
     * @param locale Desired locale.
     * @return Message for the specified key and locale.
     * @throws MissingResourceException if message for the specified key is
     *         missing.
     */
    public String get(String key, Locale locale) {
        try {
            return getResourceBundle(locale).getString(key);
        } catch (MissingResourceException e) {
            if (parentBundle != null) {
                try {
                    return parentBundle.get(key);
                } catch (MissingResourceException unused) {
                }
            }
            throw new MissingResourceException("Can't find message \"" + key + "\": " + this,
                    e.getClassName(), key);
        }
    }
    
    /**
     * Formats message for the specified key and default locale.
     * 
     * @param key Message key.
     * @param args Message arguments.
     * @return Formatted message for the specified key and default locale.
     * @throws MissingResourceException if message for the specified key is
     *         missing.
     * @see #format(String, Locale, Object...)
     */
    public String format(String key, Object... args) {
        return format(key, Locale.getDefault(), args);
    }
    
    /**
     * Formats message for the specified key and locale.
     * 
     * @param key Message key.
     * @param locale Desired locale.
     * @param args Message arguments.
     * @return Formatted message for the specified key and locale.
     * @throws MissingResourceException if message for the specified key is
     *         missing.
     */
    public String format(String key, Locale locale, Object... args) {
        return new MessageFormat(get(key, locale), locale).format(args);
    }
    
    /**
     * Returns resource bundle for the specified locale.
     * 
     * @param locale Desired locale.
     * @return Resource bundle for the specified locale.
     * @throws ResourceError if no resource bundle for the base name can be
     *         found.
     */
    private ResourceBundle getResourceBundle(Locale locale) {
        try {
            return ResourceBundle.getBundle(baseName, locale, classLoader);
        } catch (MissingResourceException e) {
            throw new ResourceError("Can't find resource bundle: " + baseName);
        }
    }
    
    /**
     * Returns a hash code value for this message bundle.
     * 
     * @return A hash code value for this message bundle. 
     */
    public int hashCode() {
        int hash = 31 * baseName.hashCode() + classLoader.hashCode();
        return parentBundle == null ? hash : 31 * hash + parentBundle.hashCode();
    }
    
    /**
     * Determines if this message bundle equals to the specified one.
     * 
     * @return <code>true</code> if this message bundle equals to the specified
     *         one; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof MessageBundle) {
            MessageBundle other = (MessageBundle) obj;
            if (baseName.equals(other.baseName) && classLoader.equals(other.classLoader)) {
                return parentBundle == null
                    ? other.parentBundle == null
                    : parentBundle.equals(other.parentBundle);
            }
        }
        return false;
    }
    
    /**
     * Returns a string that contains base name of this message bundle and its
     * parent bundles.
     * 
     * @return A string that contains base name of this message bundle and its
     *         parent bundles.
     */
    public String toString() {
        if (parentBundle == null) {
            return baseName;
        }
        StringBuilder buf = new StringBuilder(baseName);
        for (MessageBundle bundle = parentBundle; bundle != null; bundle = bundle.parentBundle) {
            buf.append(", ").append(bundle.baseName);
        }
        return buf.toString();
    }
    
    // Factory methods
    
    /**
     * Message bundle cache.
     */
    private static final ConcurrentMap<MessageBundle, MessageBundle> bundleCache =
            new ConcurrentHashMap<MessageBundle, MessageBundle>();
    
    /**
     * Returns message bundle for the specified base name and default class
     * loader.
     * 
     * @param name Message bundle base name.
     * @return Message bundle for the specified base name and default class
     *         loader.
     * @see #getInstance(String, ClassLoader, MessageBundle)
     */
    public static MessageBundle getInstance(String name) {
        return getInstance(name, null, null);
    }
    
    /**
     * Returns message bundle for the specified base name and class loader.
     * 
     * @param name Message bundle base name.
     * @param loader Class loader to be used for resource loading.
     * @return Message bundle for the specified base name and class loader.
     * @see #getInstance(String, ClassLoader, MessageBundle)
     */
    public static MessageBundle getInstance(String name, ClassLoader loader) {
        return getInstance(name, loader, null);
    }
    
    /**
     * Returns message bundle for the specified base name, parent bundle and
     * default class loader.
     * 
     * @param name Message bundle base name.
     * @param parent Parent message bundle. 
     * @return Message bundle for the specified base name, parent bundle and
     *         default class loader.
     * @see #getInstance(String, ClassLoader, MessageBundle)
     */
    public static MessageBundle getInstance(String name, MessageBundle parent) {
        return getInstance(name, null, parent);
    }
    
    /**
     * Returns message bundle for the specified base name, class loader and
     * parent bundle.
     * 
     * @param name Message bundle base name.
     * @param loader Class loader to be used for resource loading.
     * @param parent Parent message bundle. 
     * @return Message bundle for the specified base name, class loader and
     *         parent bundle.
     */
    public static MessageBundle getInstance(String name, ClassLoader loader, MessageBundle parent) {
        MessageBundle bundle = new MessageBundle(name, ResourceHelper.getClassLoader(loader), parent);
        if (parent == null) {
            MessageBundle cached = bundleCache.get(bundle);
            if (cached != null) {
                return cached;
            }
            bundleCache.put(bundle, bundle);
            return bundle;
        }
        return bundle.baseName.equals(parent.baseName)
            && bundle.classLoader.equals(parent.classLoader)
                ? parent : bundle;
    }
    
}
