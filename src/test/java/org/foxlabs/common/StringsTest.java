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

import org.junit.Test;
import org.junit.Assert;

import org.foxlabs.util.Strings;

/**
 * Tests for the {@link Strings} class.
 *
 * @author Fox Mulder
 */
public class StringsTest {

  private String original;

  /**
   * Tests the {@link Strings#nullSafe(String)} method.
   */
  @Test
  public void testNullSafe() {
    Assert.assertEquals("", Strings.nullSafe(null));
    Assert.assertEquals("", Strings.nullSafe(""));
    Assert.assertEquals(" ", Strings.nullSafe(" "));
    Assert.assertEquals("TEST", Strings.nullSafe("TEST"));
  }

  /**
   * Tests the {@link Strings#isEmpty(String)} method.
   */
  @Test
  public void testIsEmpty() {
    Assert.assertFalse(Strings.isEmpty(null));
    Assert.assertTrue(Strings.isEmpty(""));
    Assert.assertFalse(Strings.isEmpty(" "));
    Assert.assertFalse(Strings.isEmpty("TEST"));
  }

  /**
   * Tests the {@link Strings#isEmptyOrNull(String)} method.
   */
  @Test
  public void testIsEmptyOrNull() {
    Assert.assertTrue(Strings.isEmptyOrNull(null));
    Assert.assertTrue(Strings.isEmptyOrNull(""));
    Assert.assertFalse(Strings.isEmptyOrNull(" "));
    Assert.assertFalse(Strings.isEmptyOrNull("TEST"));
  }

  /**
   * Tests the {@link Strings#isNonEmpty(String)} method.
   */
  @Test
  public void testIsNonEmpty() {
    Assert.assertFalse(Strings.isNonEmpty(null));
    Assert.assertFalse(Strings.isNonEmpty(""));
    Assert.assertTrue(Strings.isNonEmpty(" "));
    Assert.assertTrue(Strings.isNonEmpty("TEST"));
  }

  /**
   * Tests the {@link Strings#isBlank(String)} method.
   */
  @Test
  public void testIsBlank() {
    Assert.assertFalse(Strings.isBlank(null));
    Assert.assertTrue(Strings.isBlank(""));
    Assert.assertTrue(Strings.isBlank(" "));
    Assert.assertTrue(Strings.isBlank("  "));
    Assert.assertTrue(Strings.isBlank("\n\r\t"));
    Assert.assertFalse(Strings.isBlank("TEST"));
  }

  /**
   * Tests the {@link Strings#isBlankOrNull(String)} method.
   */
  @Test
  public void testIsBlankOrNull() {
    Assert.assertTrue(Strings.isBlankOrNull(null));
    Assert.assertTrue(Strings.isBlankOrNull(""));
    Assert.assertTrue(Strings.isBlankOrNull(" "));
    Assert.assertTrue(Strings.isBlankOrNull("  "));
    Assert.assertTrue(Strings.isBlankOrNull("\n\r\t"));
    Assert.assertFalse(Strings.isBlankOrNull("TEST"));
  }

  /**
   * Tests the {@link Strings#isNonBlank(String)} method.
   */
  @Test
  public void testIsNonBlank() {
    Assert.assertFalse(Strings.isNonBlank(null));
    Assert.assertFalse(Strings.isNonBlank(""));
    Assert.assertFalse(Strings.isNonBlank(" "));
    Assert.assertFalse(Strings.isNonBlank("  "));
    Assert.assertFalse(Strings.isNonBlank("\n\r\t"));
    Assert.assertTrue(Strings.isNonBlank("TEST"));
  }

  /**
   * Tests the {@link Strings#isWhitespaced(String)} method.
   */
  @Test
  public void testIsWhitespaced() {
    Assert.assertFalse(Strings.isWhitespaced(null));
    Assert.assertTrue(Strings.isWhitespaced(""));
    Assert.assertTrue(Strings.isWhitespaced(" "));
    Assert.assertTrue(Strings.isWhitespaced("  "));
    Assert.assertTrue(Strings.isWhitespaced("\n\r\t"));
    Assert.assertFalse(Strings.isWhitespaced("TEST"));
    Assert.assertTrue(Strings.isWhitespaced(" TEST"));
    Assert.assertTrue(Strings.isWhitespaced("TEST "));
    Assert.assertTrue(Strings.isWhitespaced(" TEST "));
    Assert.assertTrue(Strings.isWhitespaced("T E S T"));
  }

  /**
   * Tests the {@link Strings#isWhitespacedOrNull(String)} method.
   */
  @Test
  public void testIsWhitespacedOrNull() {
    Assert.assertTrue(Strings.isWhitespacedOrNull(null));
    Assert.assertTrue(Strings.isWhitespacedOrNull(""));
    Assert.assertTrue(Strings.isWhitespacedOrNull(" "));
    Assert.assertTrue(Strings.isWhitespacedOrNull("  "));
    Assert.assertTrue(Strings.isWhitespacedOrNull("\n\r\t"));
    Assert.assertFalse(Strings.isWhitespacedOrNull("TEST"));
    Assert.assertTrue(Strings.isWhitespacedOrNull(" TEST"));
    Assert.assertTrue(Strings.isWhitespacedOrNull("TEST "));
    Assert.assertTrue(Strings.isWhitespacedOrNull(" TEST "));
    Assert.assertTrue(Strings.isWhitespacedOrNull("T E S T"));
  }

  /**
   * Tests the {@link Strings#isNonWhitespaced(String)} method.
   */
  @Test
  public void testIsNonWhitespaced() {
    Assert.assertFalse(Strings.isNonWhitespaced(null));
    Assert.assertFalse(Strings.isNonWhitespaced(""));
    Assert.assertFalse(Strings.isNonWhitespaced(" "));
    Assert.assertFalse(Strings.isNonWhitespaced("  "));
    Assert.assertFalse(Strings.isNonWhitespaced("\n\r\t"));
    Assert.assertTrue(Strings.isNonWhitespaced("TEST"));
    Assert.assertFalse(Strings.isNonWhitespaced(" TEST"));
    Assert.assertFalse(Strings.isNonWhitespaced("TEST "));
    Assert.assertFalse(Strings.isNonWhitespaced(" TEST "));
    Assert.assertFalse(Strings.isNonWhitespaced("T E S T"));
  }

  /**
   * Tests the {@link Strings#trim(String)} method.
   */
  @Test
  public void testTrim() {
    Assert.assertEquals(null, Strings.trim(null));
    Assert.assertEquals(null, Strings.trim(""));
    Assert.assertEquals(null, Strings.trim(" "));
    Assert.assertEquals(null, Strings.trim("  "));
    Assert.assertEquals(null, Strings.trim("\n\r\t"));
    Assert.assertEquals("TEST", Strings.trim("TEST"));
    Assert.assertEquals("TEST", Strings.trim(" TEST"));
    Assert.assertEquals("TEST", Strings.trim("TEST "));
    Assert.assertEquals("TEST", Strings.trim(" TEST "));
    Assert.assertEquals("T E S T", Strings.trim("T E S T"));
    Assert.assertEquals("T E S T", Strings.trim(" T E S T "));
  }

  /**
   * Tests the {@link Strings#trimNullSafe(String)} method.
   */
  @Test
  public void testTrimNullSafe() {
    Assert.assertEquals("", Strings.trimNullSafe(null));
    Assert.assertEquals("", Strings.trimNullSafe(""));
    Assert.assertEquals("", Strings.trimNullSafe(" "));
    Assert.assertEquals("", Strings.trimNullSafe("  "));
    Assert.assertEquals("", Strings.trimNullSafe("\n\r\t"));
    Assert.assertEquals("TEST", Strings.trimNullSafe("TEST"));
    Assert.assertEquals("TEST", Strings.trimNullSafe(" TEST"));
    Assert.assertEquals("TEST", Strings.trimNullSafe("TEST "));
    Assert.assertEquals("TEST", Strings.trimNullSafe(" TEST "));
    Assert.assertEquals("T E S T", Strings.trimNullSafe("T E S T"));
    Assert.assertEquals("T E S T", Strings.trimNullSafe(" T E S T "));
  }

  /**
   * Tests the {@link Strings#toLowerCase(String)} method.
   */
  @Test
  public void testToLowerCase() {
    Assert.assertTrue(Strings.toLowerCase(original = null) == original);
    Assert.assertTrue(Strings.toLowerCase(original = "") == original);
    Assert.assertTrue(Strings.toLowerCase(original = "test") == original);
    Assert.assertEquals("test", Strings.toLowerCase("TesT"));
    Assert.assertEquals("test", Strings.toLowerCase("TEST"));
    Assert.assertEquals(" test", Strings.toLowerCase(" TEST"));
    Assert.assertEquals("test ", Strings.toLowerCase("TEST "));
    Assert.assertEquals(" test ", Strings.toLowerCase(" TEST "));
  }

  /**
   * Tests the {@link Strings#toLowerCaseNullSafe(String)} method.
   */
  @Test
  public void testToLowerCaseNullSafe() {
    Assert.assertEquals("", Strings.toLowerCaseNullSafe(null));
    Assert.assertTrue(Strings.toLowerCaseNullSafe(original = "") == original);
    Assert.assertTrue(Strings.toLowerCaseNullSafe(original = "test") == original);
    Assert.assertEquals("test", Strings.toLowerCaseNullSafe("TesT"));
    Assert.assertEquals("test", Strings.toLowerCaseNullSafe("TEST"));
    Assert.assertEquals(" test", Strings.toLowerCaseNullSafe(" TEST"));
    Assert.assertEquals("test ", Strings.toLowerCaseNullSafe("TEST "));
    Assert.assertEquals(" test ", Strings.toLowerCaseNullSafe(" TEST "));
  }

  /**
   * Tests the {@link Strings#toUpperCase(String)} method.
   */
  @Test
  public void testToUpperCase() {
    Assert.assertTrue(Strings.toUpperCase(original = null) == original);
    Assert.assertTrue(Strings.toUpperCase(original = "") == original);
    Assert.assertTrue(Strings.toUpperCase(original = "TEST") == original);
    Assert.assertEquals("TEST", Strings.toUpperCase("TesT"));
    Assert.assertEquals(" TEST", Strings.toUpperCase(" test"));
    Assert.assertEquals("TEST ", Strings.toUpperCase("test "));
    Assert.assertEquals(" TEST ", Strings.toUpperCase(" test "));
  }

  /**
   * Tests the {@link Strings#toUpperCaseNullSafe(String)} method.
   */
  @Test
  public void testToUpperCaseNullSafe() {
    Assert.assertEquals("", Strings.toUpperCaseNullSafe(null));
    Assert.assertTrue(Strings.toUpperCaseNullSafe(original = "") == original);
    Assert.assertTrue(Strings.toUpperCaseNullSafe(original = "TEST") == original);
    Assert.assertEquals("TEST", Strings.toUpperCaseNullSafe("TesT"));
    Assert.assertEquals(" TEST", Strings.toUpperCaseNullSafe(" test"));
    Assert.assertEquals("TEST ", Strings.toUpperCaseNullSafe("test "));
    Assert.assertEquals(" TEST ", Strings.toUpperCaseNullSafe(" test "));
  }

}
