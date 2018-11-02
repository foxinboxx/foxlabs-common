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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * URI utilities.
 * 
 * @author Fox Mulder
 */
public abstract class URIs {
    
    /**
     * It is utility class, don't allow instantiation.
     */
    private URIs() {
        throw new IllegalAccessError();
    }
    
    /**
     * Returns username part for the specified URI reference or <code>null</code>
     * if URI does not contain username.
     * 
     * @param uri URI reference.
     * @return Username part for the specified URI reference.
     */
    public static String username(URI uri) {
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            final int index = userInfo.indexOf(':');
            if (index > 0) {
                return userInfo.substring(0, index);
            }
        }
        return null;
    }
    
    /**
     * Returns password part for the specified URI reference or <code>null</code>
     * if URI does not contain password.
     * 
     * @param uri URI reference.
     * @return Password part for the specified URI reference.
     */
    public static String password(URI uri) {
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            final int index = userInfo.indexOf(':');
            if (index < userInfo.length() - 1) {
                return userInfo.substring(index + 1);
            }
        }
        return null;
    }
    
    /**
     * Removes password from the specified URI reference and returns new URI
     * reference. If the specified URI does not contain password part then the
     * same URI instance will be returned.
     * 
     * <p>Use this method to construct exception and log messages to hide
     * confidential information.</p>
     * 
     * @param uri URI reference to remove password.
     * @return URI reference without password part.
     */
    public static URI toPublicURI(URI uri) {
        if (password(uri) != null) {
            final String username = username(uri);
            final String scheme = uri.getScheme();
            final String host = uri.getHost();
            final int port = uri.getPort();
            final String path = uri.getPath();
            final String query = uri.getQuery();
            final String fragment = uri.getFragment();
            final String userInfo = username == null ? null : username + ":***";
            try {
                return new URI(scheme, userInfo, host, port, path, query, fragment);
            } catch (URISyntaxException e) {
                // Should never happen
            }
        }
        return uri;
    }
    
    /**
     * Determines if scheme part of the specified URI reference matches at least
     * one scheme from the specified array of schemes.
     * 
     * @param uri URI reference to test.
     * @param schemes Array of schemes to match.
     * @return <code>true</code> if scheme part of the specified URI reference
     *         matches at least one scheme from the specified array of schemes;
     *         <code>false</code> otherwise.
     */
    public static boolean hasScheme(URI uri, String... schemes) {
        final String scheme = uri.getScheme();
        for (final String sch : schemes) {
            if (scheme == null) {
                if (sch == null) {
                    return true;
                }
            } else if (scheme.equalsIgnoreCase(sch)) {
                return true;
            }
        }
        return false;
    }
    
}
