/*
 * Copyright (C) 2020 FoxLabs
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

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.foxlabs.common.Strings.*;

/**
 * Tests for methods of the {@link Strings} class.
 *
 * @author Fox Mulder
 */
public class StringsTest {

  // Null

  /**
   * Tests the {@link Strings#nullSafe(String)} method.
   */
  @Test
  public void test_nullSafe() {
    assertEquals("", nullSafe((String) null));
    assertEquals("", nullSafe(""));
    assertEquals(" ", nullSafe(" "));
    assertEquals("non-null", nullSafe("non-null"));
  }

  // Checks

  /**
   * Tests the {@link Strings#isEmpty(String)} method.
   */
  @Test
  public void test_isEmpty() {
    assertFalse(isEmpty(null));
    assertTrue(isEmpty(""));
    assertFalse(isEmpty(" "));
    assertFalse(isEmpty("non-empty"));
  }

  /**
   * Tests the {@link Strings#isEmptyOrNull(String)} method.
   */
  @Test
  public void test_isEmptyOrNull() {
    assertTrue(isEmptyOrNull(null));
    assertTrue(isEmptyOrNull(""));
    assertFalse(isEmptyOrNull(" "));
    assertFalse(isEmptyOrNull("non-empty"));
  }

  /**
   * Tests the {@link Strings#isNonEmpty(String)} method.
   */
  @Test
  public void test_isNonEmpty() {
    assertFalse(isNonEmpty(null));
    assertFalse(isNonEmpty(""));
    assertTrue(isNonEmpty(" "));
    assertTrue(isNonEmpty("non-empty"));
  }

  /**
   * Tests the {@link Strings#isBlank(String)} method.
   */
  @Test
  public void test_isBlank() {
    assertFalse(isBlank(null));
    assertTrue(isBlank(""));
    assertTrue(isBlank(" "));
    assertTrue(isBlank("  "));
    assertTrue(isBlank("\n\r\t"));
    assertFalse(isBlank("non-blank"));
  }

  /**
   * Tests the {@link Strings#isBlankOrNull(String)} method.
   */
  @Test
  public void test_isBlankOrNull() {
    assertTrue(isBlankOrNull(null));
    assertTrue(isBlankOrNull(""));
    assertTrue(isBlankOrNull(" "));
    assertTrue(isBlankOrNull("  "));
    assertTrue(isBlankOrNull("\n\r\t"));
    assertFalse(isBlankOrNull("non-blank"));
  }

  /**
   * Tests the {@link Strings#isNonBlank(String)} method.
   */
  @Test
  public void test_isNonBlank() {
    assertFalse(isNonBlank(null));
    assertFalse(isNonBlank(""));
    assertFalse(isNonBlank(" "));
    assertFalse(isNonBlank("  "));
    assertFalse(isNonBlank("\n\r\t"));
    assertTrue(isNonBlank("non-blank"));
  }

  /**
   * Tests the {@link Strings#isWhitespaced(String)} method.
   */
  @Test
  public void test_isWhitespaced() {
    assertFalse(isWhitespaced(null));
    assertTrue(isWhitespaced(""));
    assertTrue(isWhitespaced(" "));
    assertTrue(isWhitespaced("  "));
    assertTrue(isWhitespaced("\n\r\t"));
    assertFalse(isWhitespaced("non-whitespaced"));
    assertTrue(isWhitespaced(" whitespaced"));
    assertTrue(isWhitespaced("whitespaced "));
    assertTrue(isWhitespaced(" whitespaced "));
    assertTrue(isWhitespaced("w h i t e s p a c e d"));
  }

  /**
   * Tests the {@link Strings#isWhitespacedOrNull(String)} method.
   */
  @Test
  public void test_isWhitespacedOrNull() {
    assertTrue(isWhitespacedOrNull(null));
    assertTrue(isWhitespacedOrNull(""));
    assertTrue(isWhitespacedOrNull(" "));
    assertTrue(isWhitespacedOrNull("  "));
    assertTrue(isWhitespacedOrNull("\n\r\t"));
    assertFalse(isWhitespacedOrNull("non-whitespaced"));
    assertTrue(isWhitespacedOrNull(" whitespaced"));
    assertTrue(isWhitespacedOrNull("whitespaced "));
    assertTrue(isWhitespacedOrNull(" whitespaced "));
    assertTrue(isWhitespacedOrNull("w h i t e s p a c e d"));
  }

  /**
   * Tests the {@link Strings#isNonWhitespaced(String)} method.
   */
  @Test
  public void test_isNonWhitespaced() {
    assertFalse(isNonWhitespaced(null));
    assertFalse(isNonWhitespaced(""));
    assertFalse(isNonWhitespaced(" "));
    assertFalse(isNonWhitespaced("  "));
    assertFalse(isNonWhitespaced("\n\r\t"));
    assertTrue(isNonWhitespaced("non-whitespaced"));
    assertFalse(isNonWhitespaced(" whitespaced"));
    assertFalse(isNonWhitespaced("whitespaced "));
    assertFalse(isNonWhitespaced(" whitespaced "));
    assertFalse(isNonWhitespaced("w h i t e s p a c e d"));
  }

  // Modifications

  /**
   * Tests the {@link Strings#replace(String, int, IntPredicate)} method.
   */
  @Test
  public void test_replace() {
    assertEquals(null, replace(null, 'X', (ch) -> true));
    String sampleString = "";
    assertSame(sampleString, replace(sampleString, 'X', (ch) -> true));
    sampleString = "test";
    assertSame(sampleString, replace(sampleString, 'X', (ch) -> false));
    assertSame(sampleString, replace(sampleString, 't', (ch) -> false));
    assertEquals("_es_", replace(sampleString, '_', (ch) -> ch == 't'));
  }

 /**
   * Tests the {@link Strings#toLowerCase(String)} method.
   */
  @Test
  public void test_toLowerCase() {
    assertEquals(null, toLowerCase(null));
    String sampleString = "";
    assertSame(sampleString, toLowerCase(sampleString));
    sampleString = "test";
    assertSame(sampleString, toLowerCase(sampleString));
    assertEquals("test", toLowerCase("TesT"));
    assertEquals("test", toLowerCase("TEST"));
    assertEquals(" test", toLowerCase(" TEST"));
    assertEquals("test ", toLowerCase("TEST "));
    assertEquals(" test ", toLowerCase(" TEST "));
  }

  /**
   * Tests the {@link Strings#toUpperCase(String)} method.
   */
  @Test
  public void test_toUpperCase() {
    assertEquals(null, toUpperCase(null));
    String sampleString = "";
    assertSame(sampleString, toUpperCase(sampleString));
    sampleString = "TEST";
    assertSame(sampleString, toUpperCase(sampleString));
    assertEquals("TEST", toUpperCase("TesT"));
    assertEquals(" TEST", toUpperCase(" test"));
    assertEquals("TEST ", toUpperCase("test "));
    assertEquals(" TEST ", toUpperCase(" test "));
  }

  /**
   * Tests the {@link Strings#trim(String)} method.
   */
  @Test
  public void test_trim() {
    final String sampleString = "test";
    assertSame(sampleString, trim(sampleString));
    assertEquals(null, trim(null));
    assertEquals(null, trim(""));
    assertEquals(null, trim(" "));
    assertEquals(null, trim("  "));
    assertEquals(null, trim("\n\r\t"));
    assertEquals("test", trim(" test"));
    assertEquals("test", trim("test "));
    assertEquals("test", trim(" test "));
    assertEquals("t e s t", trim("t e s t"));
    assertEquals("t e s t", trim(" t e s t "));
  }

  /**
   * Tests the {@link Strings#cut(String, int)} method.
   */
  @Test
  public void test_cut() {
    final String sampleString = "test";
    assertSame(sampleString, cut(sampleString, 4));
    assertThrows(IllegalArgumentException.class, () -> cut(null, -1));
    assertEquals(null, cut(null, 10));
    assertEquals(null, cut("", 0));
    assertEquals(null, cut("", 10));
    assertEquals(null, cut("test", 0));
    assertEquals("t", cut("test", 1));
  }

  /**
   * Tests the {@link Strings#ellipsis(String, int)} method.
   */
  @Test
  public void test_ellipsis() {
    final String sampleString = "test";
    assertSame(sampleString, ellipsis(sampleString, 4));
    assertThrows(IllegalArgumentException.class, () -> ellipsis(null, -1));
    assertEquals(null, ellipsis(null, 10));
    assertEquals(null, ellipsis("", 0));
    assertEquals(null, ellipsis("", 10));
    assertEquals(null, ellipsis("test", 0));
    assertEquals("t...", ellipsis("test", 1));
  }

  // Miscellaneous

 /**
   * Tests the {@link Strings#join(String, Function, Object...)} method.
   */
  @Test
  public void test_join_array() {
    assertThrows(NullPointerException.class, () -> join(null, Function.identity()));
    assertThrows(NullPointerException.class, () -> join(",", null));
    assertThrows(NullPointerException.class, () -> join(",", Object::toString, (Object[]) null));
    assertEquals("", join(",", Function.identity()));
    assertEquals("one,two", join(",", Function.identity(), "one", "two"));
    assertEquals("1,2", join(",", Object::toString, 1, 2));
  }

  /**
   * Tests the {@link Strings#join(String, Function, Iterable)} method.
   */
  @Test
  public void test_join_iterable() {
    assertThrows(NullPointerException.class, () -> join(null, Function.identity(), Arrays.asList()));
    assertThrows(NullPointerException.class, () -> join(",", null, Arrays.asList()));
    assertThrows(NullPointerException.class, () -> join(",", Object::toString, (Object[]) null));
    assertEquals("", join(",", Function.identity(), Arrays.asList()));
    assertEquals("one,two", join(",", Function.identity(), Arrays.asList("one", "two")));
    assertEquals("1,2", join(",", Object::toString, Arrays.asList(1, 2)));
  }

}
