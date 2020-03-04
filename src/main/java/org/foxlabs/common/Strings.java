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

package org.foxlabs.common;

import java.net.URLEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.IntUnaryOperator;

/**
 * Implementation of commonly used string operations.
 *
 * @author Fox Mulder
 */
public final class Strings {

  // Instantiation is not possible
  private Strings() {
    throw new IllegalAccessError();
  }

  /**
   * Returns an empty string (i.e. {@code ""}) if the specified one is
   * {@code null}, otherwise returns the specified string itself.
   *
   * @param string The string to test.
   * @return {@code null}-safe string.
   */
  public static String nullSafe(String string) {
    return string == null ? "" : string;
  }

  // Validations

  /**
   * Determines if the specified string is not {@code null}, but an empty
   * string (i.e. {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is an empty string.
   */
  public static boolean isEmpty(String string) {
    return string != null && string.length() == 0;
  }

  /**
   * Determines if the specified string is {@code null} or an empty string
   * (i.e. {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or an empty
   *         string.
   */
  public static boolean isEmptyOrNull(String string) {
    return string == null || string.length() == 0;
  }

  /**
   * Determines if the specified string is not {@code null} and not an empty
   * string (i.e. {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is not {@code null} and not
   *         an empty string.
   */
  public static boolean isNonEmpty(String string) {
    return string != null && string.length() > 0;
  }

  /**
   * Determines if the specified string is not {@code null}, but an empty
   * string (i.e. {@code ""}) or consists of whitespace characters only
   * according to the {@link Character#isWhitespace(int)} method. This method
   * does not take locale into account.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is a blank string.
   */
  public static boolean isBlank(String string) {
    if (string != null) {
      final int length = string.length();
      for (int i = 0; i < length;) {
        final int ch = string.codePointAt(i);
        if (Character.isWhitespace(ch)) {
          i += Character.charCount(ch);
        } else {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * The same as the {@link #isBlank(String)} but also returns {@code true} if
   * the specified string is {@code null}. This is a shortcut for the
   * {@code string == null || isBlank(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or a blank
   *         string.
   * @see #isBlank(String)
   */
  public static boolean isBlankOrNull(String string) {
    return string == null || isBlank(string);
  }

  /**
   * Determines if the specified string is not {@code null}, not an empty
   * string (i.e. {@code ""}) and contains at least one non-whitespace character
   * according to the {@link Character#isWhitespace(int)} method. This method
   * does not take locale into account.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is not a blank string.
   */
  public static boolean isNonBlank(String string) {
    // !isBlankOrNull() has the same effect but scans the entire string
    final int length = string == null ? 0 : string.length();
    for (int i = 0; i < length;) {
      final int ch = string.codePointAt(i);
      if (Character.isWhitespace(ch)) {
        i += Character.charCount(ch);
      } else {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if the specified string is not {@code null}, but an empty
   * string (i.e. {@code ""}) or contains at least one whitespace character
   * according to the {@link Character#isWhitespace(int)} method. This method
   * does not take locale into account.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string contains at least one
   *         whitespace character.
   */
  public static boolean isWhitespaced(String string) {
    if (string != null) {
      final int length = string.length();
      for (int i = 0; i < length;) {
        final int ch = string.codePointAt(i);
        if (Character.isWhitespace(ch)) {
          return true;
        } else {
          i += Character.charCount(ch);
        }
      }
      return length == 0;
    }
    return false;
  }

  /**
   * The same as the {@link #isWhitespaced(String)} but also returns
   * {@code true} if the specified string is {@code null}. This is a shortcut
   * for the {@code string == null || isWhitespaced(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or contains
   *         at least one whitespace character.
   * @see #isWhitespaced(String)
   */
  public static boolean isWhitespacedOrNull(String string) {
    return string == null || isWhitespaced(string);
  }

  /**
   * Determines if the specified string is not {@code null}, not an empty
   * string (i.e. {@code ""}) and does not contain whitespace characters at all
   * according to the {@link Character#isWhitespace(int)} method. This method
   * does not take locale into account. This is a shortcut for the
   * {@code !isWhitespacedOrNull(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string does not contain whitespace
   *         characters.
   * @see #isWhitespacedOrNull(String)
   */
  public static boolean isNonWhitespaced(String string) {
    return !isWhitespacedOrNull(string);
  }

  // Modifications

  /**
   * Applies the specified operator for each character of the specified string
   * and returns the resulting string which may be a different length than the
   * original one because Unicode mappings are not always 1:1.
   *
   * @param string The string to be modified.
   * @param operator The operator to be applied for each character of the string.
   * @return A resulting string or the original one if none of the characters
   *         have been modified.
   * @throws NullPointerException if the specified operator is {@code null}.
   */
  public static String forEachChar(String string, IntUnaryOperator operator) {
    Objects.require(operator);
    final int length0 = string == null ? 0 : string.length();
    for (int i = 0, j, cc0, cc1; i < length0; i += cc0) {
      int ch0 = string.codePointAt(i);
      int ch1 = operator.applyAsInt(ch0);
      cc0 = Character.charCount(ch0);
      if (ch0 != ch1) {
        // x2 length in case of growing
        final int length1 = (int) Math.min(length0 * 2L, Integer.MAX_VALUE);
        final char[] value = new char[length1];
        string.getChars(0, i, value, 0);
        Character.toChars(ch1, value, i);
        cc1 = Character.charCount(ch1);
        for (j = i + cc1, i += cc0; i < length0; i += cc0, j += cc1) {
          ch0 = string.codePointAt(i);
          ch1 = operator.applyAsInt(ch0);
          cc0 = Character.charCount(ch0);
          cc1 = Character.charCount(ch1);
          Character.toChars(ch1, value, j);
        }
        return new String(value, 0, j);
      }
    }
    return string;
  }

  /**
   * Removes all of the leading and trailing whitespace characters in the
   * specified string according to the {@link Character#isWhitespace(int)}
   * method. Returns {@code null} if the resulting string is an empty string
   * (i.e. {@code ""}). This method does not take locale into account.
   *
   * @param string The string to be trimmed.
   * @return A trimmed copy of the specified string or the original one if none
   *         of the characters have been removed.
   */
  public static String trim(String string) {
    final int length = string == null ? 0 : string.length();
    int start = 0, end = length, cc = 0;
    // need to scan the entire string because code points of characters
    // may differ in length
    for (int i = 0, n; i < length; i += n) {
      int ch = string.codePointAt(i);
      n = Character.charCount(ch);
      if (!Character.isWhitespace(ch)) {
        start = cc == 0 ? i : start;
        end = i;
        cc = n;
      }
    }
    // analyze scan results
    return cc > 0
      ? end + cc - start < length
        ? string.substring(start, end + cc) // trimmed
        : string // original string has no leading and trailing whitespaces
      : null; // original string is null, empty or blank
  }

  /**
   * The same as the {@link #trim(String)} but never returns {@code null}.
   * This is a shortcut for the {@code nullSafe(trim(string))}.
   *
   * @param string The string to be trimmed.
   * @return A {@code null}-safe trimmed copy of the specified string.
   * @see #nullSafe(String)
   * @see #trim(String)
   */
  public static String trimNullSafe(String string) {
    return nullSafe(trim(string));
  }

  /**
   * Converts all of the characters in the specified string to lower case using
   * the {@link Character#toLowerCase(int)} method. The resulting string may be
   * a different length than the original one. This method does not take locale
   * into account.
   *
   * @param string The string to be converted to lower case.
   * @return The string converted to lower case or the original one if none of
   *         the characters have been converted.
   * @see #forEachChar(String, IntUnaryOperator)
   */
  public static String toLowerCase(String string) {
    return forEachChar(string, Character::toLowerCase);
  }

  /**
   * The same as the {@link #toLowerCase(String)} but never returns {@code null}.
   * This is a shortcut for the {@code nullSafe(toLowerCase(string))}.
   *
   * @param string The string to be converted to lower case.
   * @return The {@code null}-safe string converted to lower case.
   * @see #nullSafe(String)
   * @see #toLowerCase(String)
   */
  public static String toLowerCaseNullSafe(String string) {
    return nullSafe(toLowerCase(string));
  }

  /**
   * Converts all of the characters in the specified string to upper case using
   * the {@link Character#toUpperCase(int)} method. The resulting string may be
   * a different length than the original one. This method does not take locale
   * into account.
   *
   * @param string The string to be converted to upper case.
   * @return The string converted to upper case or the original one if none of
   *         the characters have been converted.
   * @see #forEachChar(String, IntUnaryOperator)
   */
  public static String toUpperCase(String string) {
    return forEachChar(string, Character::toUpperCase);
  }

  /**
   * The same as the {@link #toUpperCase(String)} but never returns {@code null}.
   * This is a shortcut for the {@code nullSafe(toUpperCase(string))}.
   *
   * @param string The string to be converted to upper case.
   * @return The {@code null}-safe string converted to upper case.
   * @see #nullSafe(String)
   * @see #toUpperCase(String)
   */
  public static String toUpperCaseNullSafe(String string) {
    return nullSafe(toUpperCase(string));
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
     * Removes leading and trailing quotes and returns unquoted string or the
     * specified value if there are no leading and trailing quotes found.
     * Returns {@code null} if the specified value is {@code null}.
     *
     * @param value String to unquote.
     * @param quote Quote character.
     * @return Unquoted string.
     */
    public static String unquote(String value, char quote) {
        final int length = value == null ? 0 : value.length();
        if (length > 1) {
            if (value.charAt(0) == quote) {
                if (value.charAt(length - 1) == quote) {
                    return length == 2 ? "" : value.substring(1, length - 1);
                }
            }
        }
        return value;
    }

    /**
     * Replaces any of the specified characters in the specified value with the
     * specified replacement character. If there are no characters replaced
     * then original value will be returned.
     *
     * @param value String to replace characters.
     * @param chars Characters to replace.
     * @param replacement Replacement character.
     * @return String with replaced characters.
     */
    public static String replaceChars(String value, String chars, char replacement) {
        final int length = value == null ? 0 : value.length();
        if (length > 0) {
            char[] buf = null;
            for (int i = 0; i < length; i++) {
                if (chars.indexOf(value.charAt(i)) >= 0) {
                    if (buf == null) {
                        buf = value.toCharArray();
                    }
                    buf[i] = replacement;
                }
            }
            if (buf != null) {
                return new String(buf);
            }
        }
        return value;
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
        if (Character.isISOControl(ch)) {
          char[] ucode = new char[] { '\\', 'u', '0', '0', '0', '0' };
          for (int i = 3; i >= 0; i--) {
            int x = (ch >> i * 4) & 0x0f;
            ucode[5 - i] = (char) (x + (x > 0x09 ? 0x57 : 0x30));
          }
          return new String(ucode);
        }
        return Character.toString(ch);
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

}
