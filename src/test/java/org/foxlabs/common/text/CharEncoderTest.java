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

import org.foxlabs.common.text.CharBuffer;
import org.foxlabs.common.text.CharEncoder;

import static org.foxlabs.common.text.CharEncoder.*;
import static org.junit.Assert.*;

/**
 * Tests for the character encoders defined in the {@link CharEncoder} interface.
 *
 * @author Fox Mulder
 */
public class CharEncoderTest {

  /**
   * Tests the {@link CharEncoder#DUMMY} character encoder.
   */
  @Test
  public void test_DUMMY_encoder() {
    // @formatter:off
    assertEquals("a",   DUMMY.encode('a',   new CharBuffer(1)).toString());
    assertEquals("abc", DUMMY.encode("abc", new CharBuffer(3)).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharEncoder#UPPERCASE} character encoder.
   */
  @Test
  public void test_UPPERCASE_encoder() {
    assertEquals("A", UPPERCASE.encode('a', new CharBuffer(1)).toString());
    assertEquals("A", UPPERCASE.encode('A', new CharBuffer(1)).toString());
    assertEquals("_", UPPERCASE.encode('_', new CharBuffer(1)).toString());
  }

  /**
   * Tests the {@link CharEncoder#LOWERCASE} character encoder.
   */
  @Test
  public void test_LOWERCASE_encoder() {
    assertEquals("a", LOWERCASE.encode('a', new CharBuffer(1)).toString());
    assertEquals("a", LOWERCASE.encode('A', new CharBuffer(1)).toString());
    assertEquals("_", LOWERCASE.encode('_', new CharBuffer(1)).toString());
  }

  /**
   * Tests the {@link CharEncoder#UCODE} character encoder.
   */
  @Test
  public void test_UCODE_encoder() {
    // @formatter:off
    assertEquals("\\u0001",        UCODE.encode(0x00000001, new CharBuffer( 6)).toString());
    assertEquals("\\u1000",        UCODE.encode(0x00001000, new CharBuffer( 6)).toString());
    assertEquals("\\uffff",        UCODE.encode(0x0000ffff, new CharBuffer( 6)).toString());
    assertEquals("\\ud840\\udc00", UCODE.encode(0x00020000, new CharBuffer(12)).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharEncoder#JAVA} character encoder.
   */
  @Test
  public void test_JAVA_encoder() {
    // @formatter:off
    assertEquals("a",       JAVA.encode('a',    new CharBuffer(1)).toString());
    assertEquals("\\\\",    JAVA.encode('\\',   new CharBuffer(2)).toString());
    assertEquals("\\\'",    JAVA.encode('\'',   new CharBuffer(2)).toString());
    assertEquals("\\\"",    JAVA.encode('\"',   new CharBuffer(2)).toString());
    assertEquals("\\n",     JAVA.encode('\n',   new CharBuffer(2)).toString());
    assertEquals("\\r",     JAVA.encode('\r',   new CharBuffer(2)).toString());
    assertEquals("\\t",     JAVA.encode('\t',   new CharBuffer(2)).toString());
    assertEquals("\\f",     JAVA.encode('\f',   new CharBuffer(2)).toString());
    assertEquals("\\b",     JAVA.encode('\b',   new CharBuffer(2)).toString());
    assertEquals("\\u0000", JAVA.encode(0x0000, new CharBuffer(6)).toString());
    assertEquals("\\u001f", JAVA.encode(0x001f, new CharBuffer(6)).toString());
    assertEquals("\\u007f", JAVA.encode(0x007f, new CharBuffer(6)).toString());
    assertEquals("\\u009f", JAVA.encode(0x009f, new CharBuffer(6)).toString());
    // @formatter:on
  }

}
