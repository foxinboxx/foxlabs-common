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
    assertEquals("a", DUMMY.encode('a', new CharBuffer()).toString());
    assertEquals("abc", DUMMY.encode("abc", new CharBuffer()).toString());
  }

  /**
   * Tests the {@link CharEncoder#UPPERCASE} character encoder.
   */
  @Test
  public void test_UPPERCASE_encoder() {
    assertEquals("A", UPPERCASE.encode('a', new CharBuffer()).toString());
    assertEquals("A", UPPERCASE.encode('A', new CharBuffer()).toString());
    assertEquals("_", UPPERCASE.encode('_', new CharBuffer()).toString());
  }

  /**
   * Tests the {@link CharEncoder#LOWERCASE} character encoder.
   */
  @Test
  public void test_LOWERCASE_encoder() {
    assertEquals("a", LOWERCASE.encode('a', new CharBuffer()).toString());
    assertEquals("a", LOWERCASE.encode('A', new CharBuffer()).toString());
    assertEquals("_", LOWERCASE.encode('_', new CharBuffer()).toString());
  }

  /**
   * Tests the {@link CharEncoder#UCODE} character encoder.
   */
  @Test
  public void test_UCODE_encoder() {
    assertEquals("\\u0001", UCODE.encode(0x0001, new CharBuffer()).toString());
    assertEquals("\\u1000", UCODE.encode(0x1000, new CharBuffer()).toString());
    assertEquals("\\u1000u0000", UCODE.encode(0x10000000, new CharBuffer()).toString());
    assertEquals("\\u1000u1000", UCODE.encode(0x10001000, new CharBuffer()).toString());
    assertEquals("\\u0001u0001", UCODE.encode(0x00010001, new CharBuffer()).toString());
    assertEquals("\\u1234u5678", UCODE.encode(0x12345678, new CharBuffer()).toString());
    assertEquals("\\u9abcudef0", UCODE.encode(0x9abcdef0, new CharBuffer()).toString());
  }

  /**
   * Tests the {@link CharEncoder#JAVA} character encoder.
   */
  @Test
  public void test_JAVA_encoder() {
    assertEquals("a", JAVA.encode('a', new CharBuffer()).toString());
    assertEquals("\\\\", JAVA.encode('\\', new CharBuffer()).toString());
    assertEquals("\\\'", JAVA.encode('\'', new CharBuffer()).toString());
    assertEquals("\\\"", JAVA.encode('\"', new CharBuffer()).toString());
    assertEquals("\\n", JAVA.encode('\n', new CharBuffer()).toString());
    assertEquals("\\r", JAVA.encode('\r', new CharBuffer()).toString());
    assertEquals("\\t", JAVA.encode('\t', new CharBuffer()).toString());
    assertEquals("\\f", JAVA.encode('\f', new CharBuffer()).toString());
    assertEquals("\\b", JAVA.encode('\b', new CharBuffer()).toString());
    assertEquals("\\u0000", JAVA.encode(0x0000, new CharBuffer()).toString());
    assertEquals("\\u001f", JAVA.encode(0x001f, new CharBuffer()).toString());
    assertEquals("\\u007f", JAVA.encode(0x007f, new CharBuffer()).toString());
    assertEquals("\\u009f", JAVA.encode(0x009f, new CharBuffer()).toString());
  }

}
