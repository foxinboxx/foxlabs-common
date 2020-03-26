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

package org.foxlabs.common.text;

import org.junit.Test;

import org.foxlabs.common.text.CharEncoder;

import static org.junit.Assert.*;
import static org.foxlabs.common.text.CharEncoder.*;

/**
 * Tests for the character encoders defined in the {@link CharEncoder} interface.
 *
 * @author Fox Mulder
 */
public class CharEncoderTest {

  /**
   * Tests the {@link CharEncoder#IDENTITY} character encoder.
   */
  @Test
  public void test_IDENTITY_encoder() {
    // @formatter:off
    final CharBuffer buffer = new PaginalCharBuffer(1);
    assertEquals(1, IDENTITY.encode('a', buffer));
    assertEquals("a", buffer.toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharEncoder#UPPERCASE} character encoder.
   */
  @Test
  public void test_UPPERCASE_encoder() {
    final CharBuffer buffer = new PaginalCharBuffer(3);
    assertEquals(1, UPPERCASE.encode('a', buffer));
    assertEquals(1, UPPERCASE.encode('A', buffer));
    assertEquals(1, UPPERCASE.encode('_', buffer));
    assertEquals("AA_", buffer.toString());
  }

  /**
   * Tests the {@link CharEncoder#LOWERCASE} character encoder.
   */
  @Test
  public void test_LOWERCASE_encoder() {
    final CharBuffer buffer = new PaginalCharBuffer(3);
    assertEquals(1, LOWERCASE.encode('a', buffer));
    assertEquals(1, LOWERCASE.encode('A', buffer));
    assertEquals(1, LOWERCASE.encode('_', buffer));
    assertEquals("aa_", buffer.toString());
  }

  /**
   * Tests the {@link CharEncoder#UCODE} character encoder.
   */
  @Test
  public void test_UCODE_encoder() {
    // @formatter:off
    final CharBuffer buffer = new PaginalCharBuffer(30);
    assertEquals( 6, UCODE.encode(0x00000001, buffer));
    assertEquals( 6, UCODE.encode(0x00001000, buffer));
    assertEquals( 6, UCODE.encode(0x0000ffff, buffer));
    assertEquals(12, UCODE.encode(0x00020000, buffer));
    assertEquals("\\u0001\\u1000\\uffff\\ud840\\udc00", buffer.toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharEncoder#JAVA} character encoder.
   */
  @Test
  public void test_JAVA_encoder() {
    // @formatter:off
    final CharBuffer buffer = new PaginalCharBuffer(41);
    assertEquals(1, JAVA.encode('a',    buffer));
    assertEquals(2, JAVA.encode('\\',   buffer));
    assertEquals(2, JAVA.encode('\'',   buffer));
    assertEquals(2, JAVA.encode('\"',   buffer));
    assertEquals(2, JAVA.encode('\n',   buffer));
    assertEquals(2, JAVA.encode('\r',   buffer));
    assertEquals(2, JAVA.encode('\t',   buffer));
    assertEquals(2, JAVA.encode('\f',   buffer));
    assertEquals(2, JAVA.encode('\b',   buffer));
    assertEquals(6, JAVA.encode(0x0000, buffer));
    assertEquals(6, JAVA.encode(0x001f, buffer));
    assertEquals(6, JAVA.encode(0x007f, buffer));
    assertEquals(6, JAVA.encode(0x009f, buffer));
    assertEquals("a\\\\\\\'\\\"\\n\\r\\t\\f\\b\\u0000\\u001f\\u007f\\u009f", buffer.toString());
    // @formatter:on
  }

}
