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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.net.URI;
import java.net.URLEncoder;
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
     * Translates the specified value into {@code application/x-www-form-urlencoded}
     * format using the {@code ISO-8859-1} encoding. Returns {@code null} if the
     * specified value is {@code null}.
     *
     * @param value The value to encode.
     * @return The encoded value.
     * @see #urlEncode(String, Charset)
     */
    public static String urlEncode(String value) {
        return urlEncode(value, StandardCharsets.ISO_8859_1);
    }

    /**
     * Translates the specified value into {@code application/x-www-form-urlencoded}
     * format using the specified encoding. Returns {@code null} if the specified
     * value is {@code null}.
     *
     * @param value The value to encode.
     * @param charset The encoding to use.
     * @return The encoded value.
     */
    public static String urlEncode(String value, Charset charset) {
        if (!(value == null || value.isEmpty())) {
            try {
                return URLEncoder.encode(value, charset.name());
            } catch (java.io.UnsupportedEncodingException e) {
                // Should never happen
                throw new IllegalStateException();
            }
        }
        return value;
    }

    /**
     * Returns username part for the specified URI reference or <code>null</code>
     * if URI does not contain username.
     *
     * @param uri URI reference.
     * @return Username part for the specified URI reference.
     */
    public static String getUsername(URI uri) {
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            final int index = userInfo.indexOf(':');
            return index < 0 ? userInfo : userInfo.substring(0, index);
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
    public static String getPassword(URI uri) {
        final String userInfo = uri.getUserInfo();
        if (userInfo != null) {
            final int index = userInfo.indexOf(':');
            if (!(index < 0 || index + 1 == userInfo.length())) {
                return userInfo.substring(index + 1);
            }
        }
        return null;
    }

    /**
     * Returns path part of the specified URI reference as {@link Path}.
     *
     * @param uri The URI reference.
     * @return The path part of the specified URI reference as {@link Path}.
     */
    public static Path getPath(URI uri) {
        return uri.getPath() != null ? Path.parse(uri.getPath()) : Path.ROOT;
    }

    /**
     * Returns last segment of the path of the specified URI. For example, for
     * the {@code http://example.com/path/to/resource} URI last path segment is
     * {@code resource}. Returns {@code null} if there is no path part at all.
     *
     * @param uri URI reference.
     * @return Last segment of the path of the specified URI.
     */
    public static String getLastPathSegment(URI uri) {
        final String path = uri.getPath();
        if (!(path == null || path.isEmpty())) {
            int start = path.lastIndexOf('/');
            if (start < path.length() - 1) {
                return path.substring(start + 1);
            } else if (start > 0) {
                int end = start;
                start = path.lastIndexOf('/', start - 1);
                if (end - start > 1) {
                    return path.substring(start + 1, end);
                }
            }
        }
        return null;
    }

    /**
     * Adds the specified subpath to the specified URI reference and returns
     * new URI reference.
     *
     * @param uri The URI reference.
     * @param subpath The subpath to add.
     * @return A new URI reference with subpath added.
     */
    public static URI addPath(URI uri, Path subpath) {
        final String scheme = uri.getScheme();
        final String userInfo = uri.getUserInfo();
        final String host = uri.getHost();
        final int port = uri.getPort();
        final String path = getPath(uri).append(subpath).getPathname();
        final String query = uri.getQuery();
        final String fragment = uri.getFragment();
        try {
            return new URI(scheme, userInfo, host, port, path, query, fragment);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns last scheme from scheme part of the specified URI. For example,
     * for the {@code "zip:http://..."} URI last scheme is {@code "http"}.
     * Returns {@code null} if scheme part is not provided.
     *
     * @param uri URI reference.
     * @return Last scheme from scheme part of the specified URI.
     */
    public static String getLastSchemePart(URI uri) {
        final String scheme = uri.getScheme();
        if (scheme == null) {
            return null;
        } else {
            final String spec = uri.getSchemeSpecificPart();
            for (int end = spec.lastIndexOf(':') - 1; end >= 0; end--) {
                if (spec.charAt(end) != ':') {
                    final int start = spec.lastIndexOf(':', end);
                    final String last = spec.substring(Math.max(0, start + 1), end + 1);
                    if (last.isEmpty()) {
                        end = start;
                    } else {
                        return last;
                    }
                }
            }
            return scheme;
        }
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
        if (getPassword(uri) != null) {
            final String username = getUsername(uri);
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

}
