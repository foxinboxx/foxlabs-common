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

import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Implementation of commonly used string operations.
 *
 * <p>Methods of this class does not take default locale into account and perform Unicode
 * operations using methods of the {@link Character} class.</p>
 *
 * <p>All the methods accept {@code null} strings normally and do not throw
 * {@code NullPointerException}. Modification methods return {@code null} if the resulting string
 * is an empty string (i.e. {@code ""}). Use corresponding {@code xxxNullSafe()} methods if
 * {@code null} results are not desired.</p>
 *
 * @author Fox Mulder
 */
public final class Strings {

  // Instantiation is not possible
  private Strings() {
    throw new IllegalAccessError();
  }

  /**
   * The ellipsis string (i.e. {@code ...}).
   */
  public static final String ELLIPSIS = "...";

  /**
   * The empty array of strings (i.e. {@code String[0]}).
   */
  public static final String[] EMPTY_ARRAY = new String[0];

  // Null

  /**
   * Returns an empty string (i.e. {@code ""}) if the specified string is {@code null}, otherwise
   * returns the a reference to the specified string.
   *
   * @param string The string to test.
   * @return {@code null}-safe string.
   */
  public static String nullSafe(String string) {
    return string == null ? "" : string;
  }

  /**
   * Returns {@code null} if the specified string is {@code null} or empty (i.e. {@code ""}),
   * otherwise returns a reference to the specified string.
   *
   * @param string The string to test for {@code null} or empty.
   * @return {@code null} if the specified string is {@code null} or empty; a reference to the
   *         specified string otherwise.
   */
  public static String nullify(String string) {
    return string == null || string.isEmpty() ? null : string;
  }

  /**
   * Returns the {@link #EMPTY_ARRAY} if the specified array of strings is {@code null} or empty,
   * otherwise returns a reference to the specified array.
   *
   * @param strings The array of strings to test for {@code null} or empty.
   * @return {@code null}-safe array of strings.
   */
  public static String[] nullSafe(String... strings) {
    return strings == null || strings.length == 0 ? EMPTY_ARRAY : strings;
  }

  /**
   * Returns {@code null} if the specified array of strings is {@code null} or empty, otherwise
   * returns a reference to the specified array.
   *
   * @param strings The array of strings to test for {@code null} or empty.
   * @return {@code null} if the specified array of strings is {@code null} or empty; a reference
   *         to the specified array otherwise.
   */
  public static String[] nullify(String... strings) {
    return strings == null || strings.length == 0 ? null : strings;
  }

  // Checks

  /**
   * Determines if the specified string is not {@code null}, but an empty string (i.e. {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is an empty string.
   */
  public static boolean isEmpty(String string) {
    return string != null && string.length() == 0;
  }

  /**
   * Determines if the specified string is {@code null} or an empty string (i.e. {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or an empty string.
   */
  public static boolean isEmptyOrNull(String string) {
    return string == null || string.length() == 0;
  }

  /**
   * Determines if the specified string is not {@code null} and not an empty string (i.e.
   * {@code ""}).
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is not {@code null} and not an empty string.
   */
  public static boolean isNonEmpty(String string) {
    return string != null && string.length() > 0;
  }

  /**
   * Determines if the specified string is not {@code null}, but an empty string (i.e. {@code ""})
   * or consists of whitespace characters only according to the {@link Character#isWhitespace(int)}
   * method.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is a blank string.
   */
  public static boolean isBlank(String string) {
    if (string == null) {
      return false;
    }
    if (string.isEmpty()) {
      return true;
    }
    return new Iterators.CodePointIterator(string)
        .tryEachRemaining(Predicates.CHAR_WHITESPACE) == string.length();
  }

  /**
   * Does the same as the {@link #isBlank(String)}, but also returns {@code true} if the specified
   * string is {@code null}. This is a shortcut for the {@code string == null || isBlank(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or a blank string.
   * @see #isBlank(String)
   */
  public static boolean isBlankOrNull(String string) {
    return string == null || isBlank(string);
  }

  /**
   * Determines if the specified string is not {@code null}, not an empty string (i.e. {@code ""})
   * and contains at least one non-whitespace character according to the
   * {@link Character#isWhitespace(int)} method.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is not a blank string.
   */
  public static boolean isNonBlank(String string) {
    if (string == null || string.isEmpty()) {
      return false;
    }
    return new Iterators.CodePointIterator(string)
        .tryEachRemaining(Predicates.CHAR_NON_WHITESPACE) == string.length();
  }

  /**
   * Determines if the specified string is not {@code null}, but an empty string (i.e. {@code ""})
   * or contains at least one whitespace character according to the
   * {@link Character#isWhitespace(int)} method.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string contains at least one whitespace character.
   */
  public static boolean isWhitespaced(String string) {
    if (string != null) {
      if (string.isEmpty()) {
        return true;
      } else {
        final Iterators.CodePointIterator itr = new Iterators.CodePointIterator(string);
        while (itr.hasNext()) {
          if (Predicates.CHAR_WHITESPACE.test(itr.nextInt())) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Does the same as the {@link #isWhitespaced(String)}, but also returns {@code true} if the
   * specified string is {@code null}. This is a shortcut for the
   * {@code string == null || isWhitespaced(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string is {@code null} or contains at least one
   *         whitespace character.
   * @see #isWhitespaced(String)
   */
  public static boolean isWhitespacedOrNull(String string) {
    return string == null || isWhitespaced(string);
  }

  /**
   * Determines if the specified string is not {@code null}, not an empty string (i.e. {@code ""})
   * and does not contain whitespace characters at all according to the
   * {@link Character#isWhitespace(int)} method. This is a shortcut for the
   * {@code !isWhitespacedOrNull(string)}.
   *
   * @param string The string to test.
   * @return {@code true} if the specified string does not contain whitespace characters.
   * @see #isWhitespacedOrNull(String)
   */
  public static boolean isNonWhitespaced(String string) {
    return !isWhitespacedOrNull(string);
  }

  // Modifications

  /**
   * Applies the specified operator for each character of the specified string and returns the
   * resulting string which may be a different length than the original one because Unicode
   * mappings are not always 1:1.
   *
   * @param string The string to be modified.
   * @param operator The operator to be applied for each character of the string.
   * @return A resulting string or the original one if none of the characters have been modified.
   * @throws NullPointerException if the specified operator is {@code null}.
   */
  public static String forEachChar(String string, IntUnaryOperator operator) {
    Predicates.requireNonNull(operator);
    final int length0 = string == null ? 0 : string.length();
    for (int i = 0, j, cc0, cc1; i < length0; i += cc0) {
      int ch0 = string.codePointAt(i);
      int ch1 = operator.applyAsInt(ch0);
      cc0 = Character.charCount(ch0);
      if (ch0 != ch1) {
        // +x2 remaining length in case of growing
        // XXX: ArrayIndexOutOfBoundsException may occur when actual result length > 2G
        final long length1 = (long) length0 + (length0 - i);
        final char[] result = new char[(int) Math.min(length1, Integer.MAX_VALUE)];
        string.getChars(0, i, result, 0);
        Character.toChars(ch1, result, i);
        cc1 = Character.charCount(ch1);
        for (j = i + cc1, i += cc0; i < length0; i += cc0, j += cc1) {
          ch0 = string.codePointAt(i);
          ch1 = operator.applyAsInt(ch0);
          cc0 = Character.charCount(ch0);
          cc1 = Character.charCount(ch1);
          Character.toChars(ch1, result, j);
        }
        return new String(result, 0, j);
      }
    }
    return string;
  }

  /**
   * Replaces all of the characters in the specified string for which the specified predicate
   * returns {@code true} with the specified replacement character. The resulting string may be a
   * different length than the original one.
   *
   * @param string The string to be modified.
   * @param replacement The replacement character.
   * @param predicate The predicate to be applied for each character of the string.
   * @return A resulting string or the original one if none of the characters have been replaced.
   * @throws NullPointerException if the specified predicate is {@code null}.
   * @see #forEachChar(String, IntUnaryOperator)
   */
  public static String replace(String string, int replacement, IntPredicate predicate) {
    Predicates.requireNonNull(predicate);
    return forEachChar(string, (ch) -> predicate.test(ch) ? replacement : ch);
  }

  /**
   * Does the same as the {@link #replace(String, int, IntPredicate)}, but never returns
   * {@code null}. This is a shortcut for the
   * {@code nullSafe(replace(string, replacement, predicate))}.
   *
   * @param string The string to be modified.
   * @param replacement The replacement character.
   * @param predicate The predicate to be applied for each character of the string.
   * @return The {@code null}-safe resulting string.
   * @see #nullSafe(String)
   * @see #replace(String, int, IntPredicate)
   */
  public static String replaceNullSafe(String string, int replacement, IntPredicate predicate) {
    return nullSafe(replace(string, replacement, predicate));
  }

  /**
   * Converts all of the characters in the specified string to lower case using the
   * {@link Character#toLowerCase(int)} method. The resulting string may be a different length than
   * the original one.
   *
   * @param string The string to be converted to lower case.
   * @return The string converted to lower case or the original one if none of the characters have
   *         been converted.
   * @see #forEachChar(String, IntUnaryOperator)
   */
  public static String toLowerCase(String string) {
    return forEachChar(string, Character::toLowerCase);
  }

  /**
   * Does the same as the {@link #toLowerCase(String)}, but never returns {@code null}. This is a
   * shortcut for the {@code nullSafe(toLowerCase(string))}.
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
   * Converts all of the characters in the specified string to upper case using the
   * {@link Character#toUpperCase(int)} method. The resulting string may be a different length than
   * the original one.
   *
   * @param string The string to be converted to upper case.
   * @return The string converted to upper case or the original one if none of the characters have
   *         been converted.
   * @see #forEachChar(String, IntUnaryOperator)
   */
  public static String toUpperCase(String string) {
    return forEachChar(string, Character::toUpperCase);
  }

  /**
   * Does the same as the {@link #toUpperCase(String)}, but never returns {@code null}. This is a
   * shortcut for the {@code nullSafe(toUpperCase(string))}.
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
   * Removes all of the leading and trailing whitespace characters in the specified string
   * according to the {@link Character#isWhitespace(int)} method. Returns {@code null} if the
   * resulting string is an empty string (i.e. {@code ""}).
   *
   * @param string The string to be trimmed.
   * @return A trimmed copy of the specified string or the original one if none of the characters
   *         have been removed.
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
   * Does the same as the {@link #trim(String)}, but never returns {@code null}. This is a shortcut
   * for the {@code nullSafe(trim(string))}.
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
   * Cuts length of the specified string up to the specified limit. If the specified string is an
   * empty string (i.e. {@code ""}) then returns {@code null}.
   *
   * @param string The string to be cutted.
   * @param limit The maximum number of allowed characters in the string.
   * @return A cutted string or the original one if its length is less than or equal to the
   *         specified limit.
   * @throws IllegalArgumentException if the specified limit is negative.
   */
  public static String cut(String string, int limit) {
    Predicates.require(limit, Predicates.INT_POSITIVE_OR_ZERO);
    if (limit == 0 || string == null || string.isEmpty()) {
      return null;
    }
    if (string.length() > limit) { // fast length check
      // need to scan the string because Unicode mappings are not 1:1
      final Iterators.CodePointIterator itr = new Iterators.CodePointIterator(string);
      for (int i = 0; i < limit && itr.hasNext(); i++, itr.nextInt());
      if (itr.position() < string.length()) {
        return string.substring(0, itr.position());
      }
    }
    return string;
  }

  /**
   * Does the same as the {@link #cut(String, int)}, but never returns {@code null}. This is a
   * shortcut for the {@code nullSafe(cut(string, limit))}.
   *
   * @param string The string to be cutted.
   * @param limit The maximum number of allowed characters in the string.
   * @return A {@code null}-safe cutted string.
   * @see #nullSafe(String)
   * @see #cut(String, int)
   */
  public static String cutNullSafe(String string, int limit) {
    return nullSafe(cut(string, limit));
  }

  /**
   * Does the same as the {@link #cut(String, int)}, but adds {@code ...} at the end of the cutted
   * string if cut operation was applied.
   *
   * @param string The string to be cutted.
   * @param limit The maximum number of allowed characters in the string.
   * @return A cutted string with {@code ...} at the end or the original string if cut operation
   *         was not applied.
   * @see #cut(String, int)
   */
  public static String ellipsis(String string, int limit) {
    final String result = cut(string, limit);
    return result == string || result == null ? result : result + ELLIPSIS;
  }

  /**
   * Does the same as the {@link #ellipsis(String, int)}, but never returns {@code null}. This is a
   * shortcut for the {@code nullSafe(ellipsis(string, limit))}.
   *
   * @param string The string to be cutted.
   * @param limit The maximum number of allowed characters in the string.
   * @return A cutted {@code null}-safe string with {@code ...} at the end.
   * @see #nullSafe(String)
   * @see #ellipsis(String, int)
   */
  public static String ellipsisNullSafe(String string, int limit) {
    return nullSafe(ellipsis(string, limit));
  }

  // ----- TO BE REFACTORED START ---------------------------------------------

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
    public static StringBuilder escape(CharSequence value, StringBuilder buf) {
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

  // ----- TO BE REFACTORED END -----------------------------------------------

 // Miscellaneous

  /**
   * Returns a new string composed of copies of the specified array of elements joined together
   * with a copy of the specified delimiter using the specified mapper function to convert elements
   * to strings.
   *
   * @param <E> The type of elements to join.
   * @param delimiter A sequence of characters that is used to separate each of the elements in the
   *        resulting string.
   * @param mapper A function that converts elements to strings.
   * @param elements An array that will have its elements joined together.
   * @return A new string that is composed from the specified elements.
   * @see String#join(CharSequence, Iterable)
   * @see Iterators#toIterable(Object...)
   * @see Iterators#withMapper(Function, Iterable)
   */
  @SafeVarargs
  public static <E> String join(String delimiter, Function<E, String> mapper, E... elements) {
    return String.join(delimiter, Iterators.withMapper(mapper, Iterators.toIterable(elements)));
  }

  /**
   * Returns a new string composed of copies of the specified elements joined together with a copy
   * of the specified delimiter using the specified mapper function to convert elements to strings.
   *
   * @param <E> The type of elements to join.
   * @param delimiter A sequence of characters that is used to separate each of the elements in the
   *        resulting string.
   * @param mapper A function that converts elements to strings.
   * @param elements An {@code Iterable} that will have its elements joined together.
   * @return A new string that is composed from the specified elements.
   * @see String#join(CharSequence, Iterable)
   * @see Iterators#withMapper(Function, Iterable)
   */
  public static <E> String join(String delimiter, Function<E, String> mapper, Iterable<E> elements) {
    return String.join(delimiter, Iterators.withMapper(mapper, elements));
  }

}
