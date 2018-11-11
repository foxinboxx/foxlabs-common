/* 
 * Copyright (C) 2016 FoxLabs
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

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implemention of commonly used string operations.
 * 
 * @author Fox Mulder
 */
public abstract class Strings {
    
    /**
     * It is utility class, don't allow instantiation.
     */
    private Strings() {
        throw new IllegalAccessError();
    }
    
    /**
     * Returns {@code Object} instance with overridden {@code toString()} method
     * that uses the specified formatter to generate resulting string. This
     * method is useful for lazy message construction in Log4J logging.
     * 
     * <pre>
     * // Instead of this
     * if (log.isDebugEnabled()) {
     *     log.debug("System properties:\n" + System.getProperties());
     * }
     * 
     * // You can use this
     * log.debug(Strings.message(() -> "System properties:\n" + System.getProperties()));
     * </pre>
     * 
     * @param formatter {@code toString()} result formatter.
     * @return {@code Object} instance with overridden {@code toString()} method
     *         that uses the specified formatter to generate resulting string.
     */
    public static Object message(Supplier<String> formatter) {
        return new Object() {
            @Override public String toString() {
                return formatter.get();
            }
        };
    }
    
    /**
     * Returns trimmed copy of the specified string or {@code null} if the
     * specified string is empty or {@code null}.
     * 
     * @param value String value.
     * @return Trimmed copy of the specified string or {@code null} if the
     *         specified string is empty or {@code null}.
     */
    public static String trim(String value) {
        return value == null || (value = value.trim()).isEmpty() ? null : value;
    }
    
    /**
     * Cuts length of the specified string to the specified maximum length.
     * 
     * @param value String to cut.
     * @param maxlength Maximum allowed length for the specified string.
     * @return Cutted string or the specified one if its length less or equal
     *         than maximum length.
     */
    public static String cut(String value, int maxlength) {
        return value == null
            ? null
            : value.length() > maxlength
                ? value.substring(0, Math.max(maxlength, 0))
                : value;
    }
    
    /**
     * Does the same as {@link #cut(String, int)} but adds {@code "..."} at the
     * end of cutted string if cut operation has been applied.
     * 
     * @param value String to cut.
     * @param maxlength Maximum allowed length for the specified string.
     * @return Cutted string with {@code "..."} at the end.
     */
    public static String ellipsis(String value, int maxlength) {
        return value == null
            ? null
            : value.length() > maxlength
                ? value.substring(0, Math.max(maxlength, 0)) + "..."
                : value;
    }
    
    /**
     * Converts the specified string to lower case in locale insensitive way.
     * 
     * @param value String to be converted.
     * @return The specified string in lower case.
     */
    public static String lower(String value) {
        return value == null ? null : value.toLowerCase(Locale.ENGLISH);
    }
    
    /**
     * Converts the specified string to upper case in locale insensitive way.
     * 
     * @param value String to be converted.
     * @return The specified string in upper case.
     */
    public static String upper(String value) {
        return value == null ? null : value.toUpperCase(Locale.ENGLISH);
    }
    
    /**
     * Returns a new string composed of copies of the specified array of elements
     * joined together with a copy of the specified delimiter using the specified
     * mapper function to convert elements to strings.
     * 
     * @param <T> The type of elements.
     * @param delimiter A sequence of characters that is used to separate each
     *        of the elements in the resulting string.
     * @param elements An array of elements to join.
     * @param mapper A function that converts array elements to string representation.
     * @return A new string that is composed from the specified elements.
     */
    public static <T> String join(String delimiter, T[] elements, Function<T, String> mapper) {
        return String.join(delimiter, new Iterable<CharSequence>() {
            @Override public Iterator<CharSequence> iterator() {
                return new Iterator<CharSequence>() {
                    int index = 0;
                    @Override public boolean hasNext() {
                        return index < elements.length;
                    }
                    @Override public CharSequence next() {
                        if (index >= elements.length) {
                            throw new NoSuchElementException();
                        }
                        return mapper.apply(elements[index++]);
                    }
                };
            }
        });
    }
    
    /**
     * Returns a new string composed of copies of the specified elements joined
     * together with a copy of the specified delimiter using the specified mapper
     * function to convert elements to strings.
     * 
     * @param <T> The type of elements.
     * @param delimiter A sequence of characters that is used to separate each
     *        of the elements in the resulting string.
     * @param elements An {@code Iterable} that will have its elements joined together.
     * @param mapper A function that converts array elements to string representation.
     * @return A new string that is composed from the specified elements.
     */
    public static <T> String join(String delimiter, Iterable<T> elements, Function<T, String> mapper) {
        return String.join(delimiter, new Iterable<CharSequence>() {
            @Override public Iterator<CharSequence> iterator() {
                return new Iterator<CharSequence>() {
                    final Iterator<T> itr = elements.iterator();
                    @Override public boolean hasNext() {
                        return itr.hasNext();
                    }
                    @Override public CharSequence next() {
                        return mapper.apply(itr.next());
                    }
                };
            }
        });
    }
    
    /**
     * Adds escape ({@code \}) character to the specified one if needed.
     * 
     * @param ch Source character.
     * @return Escaped character string.
     */
    public static String escape(char ch) {
        switch (ch) {
        case '\\':
            return "\\\\";
        case '\'':
            return "\\'";
        case '\"':
            return "\\\"";
        case '\n':
            return "\\n";
        case '\r':
            return "\\r";
        case '\t':
            return "\\t";
        case '\b':
            return "\\b";
        case '\f':
            return "\\f";
        default:
            if (UnicodeSet.PRINT.contains(ch)) {
                return Character.toString(ch);
            }
            char[] ucode = new char[]{'\\', 'u', '0', '0', '0', '0'};
            for (int i = 3; i >= 0; i--) {
                int x = (ch >> i * 4) & 0x0f;
                ucode[5 - i] = (char) (x + (x > 0x09 ? 0x57 : 0x30));
            }
            return new String(ucode);
        }
    }
    
    /**
     * Adds escape ({@code \}) characters to the specified string.
     * 
     * @param value Source string.
     * @return String with added escape characters.
     * @see #escape(String, StringBuilder)
     */
    public static String escape(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        return escape(value, new StringBuilder(value.length())).toString();
    }
    
    /**
     * Adds escape ({@code \}) characters to the specified string and
     * appends result to the specified buffer.
     * 
     * @param value Source string.
     * @param buf Buffer to append.
     * @return The specified buffer.
     * @see #escape(char)
     */
    public static StringBuilder escape(String value, StringBuilder buf) {
        int length = value == null ? 0 : value.length();
        for (int i = 0; i < length; i++) {
            buf.append(escape(value.charAt(i)));
        }
        return buf;
    }
    
    /**
     * Removes escape ({@code \}) characters from the specified string.
     * 
     * @param value Source string.
     * @return String with removed escape characters.
     * @throws IllegalArgumentException if the specified string is malformed.
     * @see #unescape(String, StringBuilder)
     */
    public static String unescape(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        return unescape(value, new StringBuilder(value.length())).toString();
    }
    
    /**
     * Removes escape ({@code \}) characters from the specified string and
     * appends result to the specified buffer.
     * 
     * @param value Source string.
     * @param buf Buffer to append.
     * @return The specified buffer.
     * @throws IllegalArgumentException if the specified string is malformed.
     */
    public static StringBuilder unescape(String value, StringBuilder buf) {
        int length = value == null ? 0 : value.length();
        for (int i = 0; i < length;) {
            char ch = value.charAt(i++);
            if (ch == '\\') {
                if (i == length) {
                    throw new IllegalArgumentException(value);
                }
                switch (value.charAt(i++)) {
                case '\"':
                    buf.append('\"');
                    break;
                case '\'':
                    buf.append('\'');
                    break;
                case '\\':
                    buf.append('\\');
                    break;
                case 'n':
                    buf.append('\n');
                    break;
                case 'r':
                    buf.append('\r');
                    break;
                case 't':
                    buf.append('\t');
                    break;
                case 'f':
                    buf.append('\f');
                    break;
                case 'b':
                    buf.append('\b');
                    break;
                case 'u':
                    if (i + 4 > length) {
                        throw new IllegalArgumentException(value);
                    }
                    int ucode = 0;
                    for (int j = 0; j < 4; j++) {
                        ucode <<= 4;
                        ch = value.charAt(i++);
                        if (ch >= 0x30 && ch <= 0x39) {
                            ucode |= ch - 0x30;
                        } else if (ch >= 0x61 && ch <= 0x7a) {
                            ucode |= ch - 0x57;
                        } else if (ch >= 0x41 && ch <= 0x5a) {
                            ucode |= ch - 0x37;
                        } else {
                            throw new IllegalArgumentException(value);
                        }
                    }
                    buf.append((char) ucode);
                    break;
                default:
                    throw new IllegalArgumentException(value);
                }
            } else {
                buf.append(ch);
            }
        }
        return buf;
    }
    
}
