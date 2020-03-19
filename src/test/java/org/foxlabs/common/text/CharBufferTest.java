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

import static org.junit.Assert.*;

/**
 * Tests for methods of the {@link CharBuffer} class.
 *
 * @author Fox Mulder
 */
public class CharBufferTest {

  // Boolean to string representation

  /**
   * Tests the {@link CharBuffer#appendBool(boolean)} method.
   */
  @Test
  public void test_appendBool() {
    // @formatter:off
    assertEquals("true",  new CharBuffer().appendBool(true ).toString());
    assertEquals("false", new CharBuffer().appendBool(false).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBoolCapacity(boolean)} method.
   */
  @Test
  public void test_getBoolCapacity() {
    // @formatter:off
    assertEquals(4, CharBuffer.getBoolCapacity(true ));
    assertEquals(5, CharBuffer.getBoolCapacity(false));
    // @formatter:on
  }

  // Number to string representation

  // Decimal representation

  /**
   * Tests the {@link CharBuffer#getDecCapacity(int)} method.
   */
  @Test
  public void test_getDecCapacity_int() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getDecCapacity(          0));
    assertEquals( 1, CharBuffer.getDecCapacity(          9));
    assertEquals( 2, CharBuffer.getDecCapacity(         10));
    assertEquals( 2, CharBuffer.getDecCapacity(         99));
    assertEquals( 3, CharBuffer.getDecCapacity(        100));
    assertEquals( 3, CharBuffer.getDecCapacity(        999));
    assertEquals( 4, CharBuffer.getDecCapacity(       1000));
    assertEquals( 4, CharBuffer.getDecCapacity(       9999));
    assertEquals( 5, CharBuffer.getDecCapacity(      10000));
    assertEquals( 5, CharBuffer.getDecCapacity(      99999));
    assertEquals( 6, CharBuffer.getDecCapacity(     100000));
    assertEquals( 6, CharBuffer.getDecCapacity(     999999));
    assertEquals( 7, CharBuffer.getDecCapacity(    1000000));
    assertEquals( 7, CharBuffer.getDecCapacity(    9999999));
    assertEquals( 8, CharBuffer.getDecCapacity(   10000000));
    assertEquals( 8, CharBuffer.getDecCapacity(   99999999));
    assertEquals( 9, CharBuffer.getDecCapacity(  100000000));
    assertEquals( 9, CharBuffer.getDecCapacity(  999999999));
    assertEquals(10, CharBuffer.getDecCapacity( 1000000000));
    assertEquals(10, CharBuffer.getDecCapacity( 2147483647));
    assertEquals(11, CharBuffer.getDecCapacity(-2147483648));
    assertEquals(11, CharBuffer.getDecCapacity(-1000000000));
    assertEquals(10, CharBuffer.getDecCapacity( -999999999));
    assertEquals(10, CharBuffer.getDecCapacity( -100000000));
    assertEquals( 9, CharBuffer.getDecCapacity(  -99999999));
    assertEquals( 9, CharBuffer.getDecCapacity(  -10000000));
    assertEquals( 8, CharBuffer.getDecCapacity(   -9999999));
    assertEquals( 8, CharBuffer.getDecCapacity(   -1000000));
    assertEquals( 7, CharBuffer.getDecCapacity(    -999999));
    assertEquals( 7, CharBuffer.getDecCapacity(    -100000));
    assertEquals( 6, CharBuffer.getDecCapacity(     -99999));
    assertEquals( 6, CharBuffer.getDecCapacity(     -10000));
    assertEquals( 5, CharBuffer.getDecCapacity(      -9999));
    assertEquals( 5, CharBuffer.getDecCapacity(      -1000));
    assertEquals( 4, CharBuffer.getDecCapacity(       -999));
    assertEquals( 4, CharBuffer.getDecCapacity(       -100));
    assertEquals( 3, CharBuffer.getDecCapacity(        -99));
    assertEquals( 3, CharBuffer.getDecCapacity(        -10));
    assertEquals( 2, CharBuffer.getDecCapacity(         -9));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getDecCapacity(long)} method.
   */
  @Test
  public void test_getDecCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getDecCapacity(                   0L));
    assertEquals( 1, CharBuffer.getDecCapacity(                   9L));
    assertEquals( 2, CharBuffer.getDecCapacity(                  10L));
    assertEquals( 2, CharBuffer.getDecCapacity(                  99L));
    assertEquals( 3, CharBuffer.getDecCapacity(                 100L));
    assertEquals( 3, CharBuffer.getDecCapacity(                 999L));
    assertEquals( 4, CharBuffer.getDecCapacity(                1000L));
    assertEquals( 4, CharBuffer.getDecCapacity(                9999L));
    assertEquals( 5, CharBuffer.getDecCapacity(               10000L));
    assertEquals( 5, CharBuffer.getDecCapacity(               99999L));
    assertEquals( 6, CharBuffer.getDecCapacity(              100000L));
    assertEquals( 6, CharBuffer.getDecCapacity(              999999L));
    assertEquals( 7, CharBuffer.getDecCapacity(             1000000L));
    assertEquals( 7, CharBuffer.getDecCapacity(             9999999L));
    assertEquals( 8, CharBuffer.getDecCapacity(            10000000L));
    assertEquals( 8, CharBuffer.getDecCapacity(            99999999L));
    assertEquals( 9, CharBuffer.getDecCapacity(           100000000L));
    assertEquals( 9, CharBuffer.getDecCapacity(           999999999L));
    assertEquals(10, CharBuffer.getDecCapacity(          1000000000L));
    assertEquals(10, CharBuffer.getDecCapacity(          9999999999L));
    assertEquals(11, CharBuffer.getDecCapacity(         10000000000L));
    assertEquals(11, CharBuffer.getDecCapacity(         99999999999L));
    assertEquals(12, CharBuffer.getDecCapacity(        100000000000L));
    assertEquals(12, CharBuffer.getDecCapacity(        999999999999L));
    assertEquals(13, CharBuffer.getDecCapacity(       1000000000000L));
    assertEquals(13, CharBuffer.getDecCapacity(       9999999999999L));
    assertEquals(14, CharBuffer.getDecCapacity(      10000000000000L));
    assertEquals(14, CharBuffer.getDecCapacity(      99999999999999L));
    assertEquals(15, CharBuffer.getDecCapacity(     100000000000000L));
    assertEquals(15, CharBuffer.getDecCapacity(     999999999999999L));
    assertEquals(16, CharBuffer.getDecCapacity(    1000000000000000L));
    assertEquals(16, CharBuffer.getDecCapacity(    9999999999999999L));
    assertEquals(17, CharBuffer.getDecCapacity(   10000000000000000L));
    assertEquals(17, CharBuffer.getDecCapacity(   99999999999999999L));
    assertEquals(18, CharBuffer.getDecCapacity(  100000000000000000L));
    assertEquals(18, CharBuffer.getDecCapacity(  999999999999999999L));
    assertEquals(19, CharBuffer.getDecCapacity( 1000000000000000000L));
    assertEquals(19, CharBuffer.getDecCapacity( 9223372036854775807L));
    assertEquals(20, CharBuffer.getDecCapacity(-9223372036854775808L));
    assertEquals(20, CharBuffer.getDecCapacity(-1000000000000000000L));
    assertEquals(19, CharBuffer.getDecCapacity( -999999999999999999L));
    assertEquals(19, CharBuffer.getDecCapacity( -100000000000000000L));
    assertEquals(18, CharBuffer.getDecCapacity(  -99999999999999999L));
    assertEquals(18, CharBuffer.getDecCapacity(  -10000000000000000L));
    assertEquals(17, CharBuffer.getDecCapacity(   -9999999999999999L));
    assertEquals(17, CharBuffer.getDecCapacity(   -1000000000000000L));
    assertEquals(16, CharBuffer.getDecCapacity(    -999999999999999L));
    assertEquals(16, CharBuffer.getDecCapacity(    -100000000000000L));
    assertEquals(15, CharBuffer.getDecCapacity(     -99999999999999L));
    assertEquals(15, CharBuffer.getDecCapacity(     -10000000000000L));
    assertEquals(14, CharBuffer.getDecCapacity(      -9999999999999L));
    assertEquals(14, CharBuffer.getDecCapacity(      -1000000000000L));
    assertEquals(13, CharBuffer.getDecCapacity(       -999999999999L));
    assertEquals(13, CharBuffer.getDecCapacity(       -100000000000L));
    assertEquals(12, CharBuffer.getDecCapacity(        -99999999999L));
    assertEquals(12, CharBuffer.getDecCapacity(        -10000000000L));
    assertEquals(11, CharBuffer.getDecCapacity(         -9999999999L));
    assertEquals(11, CharBuffer.getDecCapacity(         -1000000000L));
    assertEquals(10, CharBuffer.getDecCapacity(          -999999999L));
    assertEquals(10, CharBuffer.getDecCapacity(          -100000000L));
    assertEquals( 9, CharBuffer.getDecCapacity(           -99999999L));
    assertEquals( 9, CharBuffer.getDecCapacity(           -10000000L));
    assertEquals( 8, CharBuffer.getDecCapacity(            -9999999L));
    assertEquals( 8, CharBuffer.getDecCapacity(            -1000000L));
    assertEquals( 7, CharBuffer.getDecCapacity(             -999999L));
    assertEquals( 7, CharBuffer.getDecCapacity(             -100000L));
    assertEquals( 6, CharBuffer.getDecCapacity(              -99999L));
    assertEquals( 6, CharBuffer.getDecCapacity(              -10000L));
    assertEquals( 5, CharBuffer.getDecCapacity(               -9999L));
    assertEquals( 5, CharBuffer.getDecCapacity(               -1000L));
    assertEquals( 4, CharBuffer.getDecCapacity(                -999L));
    assertEquals( 4, CharBuffer.getDecCapacity(                -100L));
    assertEquals( 3, CharBuffer.getDecCapacity(                 -99L));
    assertEquals( 3, CharBuffer.getDecCapacity(                 -10L));
    assertEquals( 2, CharBuffer.getDecCapacity(                  -9L));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendDec(byte)} method.
   */
  @Test
  public void test_appendDec_byte() {
    // @formatter:off
    assertEquals(   "0", new CharBuffer().appendDec((byte)    0).toString());
    assertEquals(   "9", new CharBuffer().appendDec((byte)    9).toString());
    assertEquals(  "10", new CharBuffer().appendDec((byte)   10).toString());
    assertEquals(  "99", new CharBuffer().appendDec((byte)   99).toString());
    assertEquals( "100", new CharBuffer().appendDec((byte)  100).toString());
    assertEquals( "127", new CharBuffer().appendDec((byte)  127).toString());
    assertEquals("-128", new CharBuffer().appendDec((byte) -128).toString());
    assertEquals("-100", new CharBuffer().appendDec((byte) -100).toString());
    assertEquals( "-99", new CharBuffer().appendDec((byte)  -99).toString());
    assertEquals( "-10", new CharBuffer().appendDec((byte)  -10).toString());
    assertEquals(  "-9", new CharBuffer().appendDec((byte)   -9).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendDec(short)} method.
   */
  @Test
  public void test_appendDec_short() {
    // @formatter:off
    assertEquals(     "0", new CharBuffer().appendDec((short)      0).toString());
    assertEquals(     "9", new CharBuffer().appendDec((short)      9).toString());
    assertEquals(    "10", new CharBuffer().appendDec((short)     10).toString());
    assertEquals(    "99", new CharBuffer().appendDec((short)     99).toString());
    assertEquals(   "100", new CharBuffer().appendDec((short)    100).toString());
    assertEquals(   "999", new CharBuffer().appendDec((short)    999).toString());
    assertEquals(  "1000", new CharBuffer().appendDec((short)   1000).toString());
    assertEquals(  "9999", new CharBuffer().appendDec((short)   9999).toString());
    assertEquals( "10000", new CharBuffer().appendDec((short)  10000).toString());
    assertEquals( "32767", new CharBuffer().appendDec((short)  32767).toString());
    assertEquals("-32768", new CharBuffer().appendDec((short) -32768).toString());
    assertEquals("-10000", new CharBuffer().appendDec((short) -10000).toString());
    assertEquals( "-9999", new CharBuffer().appendDec((short)  -9999).toString());
    assertEquals( "-1000", new CharBuffer().appendDec((short)  -1000).toString());
    assertEquals(  "-999", new CharBuffer().appendDec((short)   -999).toString());
    assertEquals(  "-100", new CharBuffer().appendDec((short)   -100).toString());
    assertEquals(   "-99", new CharBuffer().appendDec((short)    -99).toString());
    assertEquals(   "-10", new CharBuffer().appendDec((short)    -10).toString());
    assertEquals(    "-9", new CharBuffer().appendDec((short)     -9).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendDec(int)} method.
   */
  @Test
  public void test_appendDec_int() {
    // @formatter:off
    assertEquals(          "0", new CharBuffer().appendDec(          0).toString());
    assertEquals(          "9", new CharBuffer().appendDec(          9).toString());
    assertEquals(         "10", new CharBuffer().appendDec(         10).toString());
    assertEquals(         "99", new CharBuffer().appendDec(         99).toString());
    assertEquals(        "100", new CharBuffer().appendDec(        100).toString());
    assertEquals(        "999", new CharBuffer().appendDec(        999).toString());
    assertEquals(       "1000", new CharBuffer().appendDec(       1000).toString());
    assertEquals(       "9999", new CharBuffer().appendDec(       9999).toString());
    assertEquals(      "10000", new CharBuffer().appendDec(      10000).toString());
    assertEquals(      "99999", new CharBuffer().appendDec(      99999).toString());
    assertEquals(     "100000", new CharBuffer().appendDec(     100000).toString());
    assertEquals(     "999999", new CharBuffer().appendDec(     999999).toString());
    assertEquals(    "1000000", new CharBuffer().appendDec(    1000000).toString());
    assertEquals(    "9999999", new CharBuffer().appendDec(    9999999).toString());
    assertEquals(   "10000000", new CharBuffer().appendDec(   10000000).toString());
    assertEquals(   "99999999", new CharBuffer().appendDec(   99999999).toString());
    assertEquals(  "100000000", new CharBuffer().appendDec(  100000000).toString());
    assertEquals(  "999999999", new CharBuffer().appendDec(  999999999).toString());
    assertEquals( "1000000000", new CharBuffer().appendDec( 1000000000).toString());
    assertEquals( "2147483647", new CharBuffer().appendDec( 2147483647).toString());
    assertEquals("-2147483648", new CharBuffer().appendDec(-2147483648).toString());
    assertEquals("-1000000000", new CharBuffer().appendDec(-1000000000).toString());
    assertEquals( "-999999999", new CharBuffer().appendDec( -999999999).toString());
    assertEquals( "-100000000", new CharBuffer().appendDec( -100000000).toString());
    assertEquals(  "-99999999", new CharBuffer().appendDec(  -99999999).toString());
    assertEquals(  "-10000000", new CharBuffer().appendDec(  -10000000).toString());
    assertEquals(   "-9999999", new CharBuffer().appendDec(   -9999999).toString());
    assertEquals(   "-1000000", new CharBuffer().appendDec(   -1000000).toString());
    assertEquals(    "-999999", new CharBuffer().appendDec(    -999999).toString());
    assertEquals(    "-100000", new CharBuffer().appendDec(    -100000).toString());
    assertEquals(     "-99999", new CharBuffer().appendDec(     -99999).toString());
    assertEquals(     "-10000", new CharBuffer().appendDec(     -10000).toString());
    assertEquals(      "-9999", new CharBuffer().appendDec(      -9999).toString());
    assertEquals(      "-1000", new CharBuffer().appendDec(      -1000).toString());
    assertEquals(       "-999", new CharBuffer().appendDec(       -999).toString());
    assertEquals(       "-100", new CharBuffer().appendDec(       -100).toString());
    assertEquals(        "-99", new CharBuffer().appendDec(        -99).toString());
    assertEquals(        "-10", new CharBuffer().appendDec(        -10).toString());
    assertEquals(         "-9", new CharBuffer().appendDec(         -9).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendDec(long)} method.
   */
  @Test
  public void test_appendDec_long() {
    // @formatter:off
    assertEquals(                   "0", new CharBuffer().appendDec(                   0L).toString());
    assertEquals(                   "9", new CharBuffer().appendDec(                   9L).toString());
    assertEquals(                  "10", new CharBuffer().appendDec(                  10L).toString());
    assertEquals(                  "99", new CharBuffer().appendDec(                  99L).toString());
    assertEquals(                 "100", new CharBuffer().appendDec(                 100L).toString());
    assertEquals(                 "999", new CharBuffer().appendDec(                 999L).toString());
    assertEquals(                "1000", new CharBuffer().appendDec(                1000L).toString());
    assertEquals(                "9999", new CharBuffer().appendDec(                9999L).toString());
    assertEquals(               "10000", new CharBuffer().appendDec(               10000L).toString());
    assertEquals(               "99999", new CharBuffer().appendDec(               99999L).toString());
    assertEquals(              "100000", new CharBuffer().appendDec(              100000L).toString());
    assertEquals(              "999999", new CharBuffer().appendDec(              999999L).toString());
    assertEquals(             "1000000", new CharBuffer().appendDec(             1000000L).toString());
    assertEquals(             "9999999", new CharBuffer().appendDec(             9999999L).toString());
    assertEquals(            "10000000", new CharBuffer().appendDec(            10000000L).toString());
    assertEquals(            "99999999", new CharBuffer().appendDec(            99999999L).toString());
    assertEquals(           "100000000", new CharBuffer().appendDec(           100000000L).toString());
    assertEquals(           "999999999", new CharBuffer().appendDec(           999999999L).toString());
    assertEquals(          "1000000000", new CharBuffer().appendDec(          1000000000L).toString());
    assertEquals(          "9999999999", new CharBuffer().appendDec(          9999999999L).toString());
    assertEquals(         "10000000000", new CharBuffer().appendDec(         10000000000L).toString());
    assertEquals(         "99999999999", new CharBuffer().appendDec(         99999999999L).toString());
    assertEquals(        "100000000000", new CharBuffer().appendDec(        100000000000L).toString());
    assertEquals(        "999999999999", new CharBuffer().appendDec(        999999999999L).toString());
    assertEquals(       "1000000000000", new CharBuffer().appendDec(       1000000000000L).toString());
    assertEquals(       "9999999999999", new CharBuffer().appendDec(       9999999999999L).toString());
    assertEquals(      "10000000000000", new CharBuffer().appendDec(      10000000000000L).toString());
    assertEquals(      "99999999999999", new CharBuffer().appendDec(      99999999999999L).toString());
    assertEquals(     "100000000000000", new CharBuffer().appendDec(     100000000000000L).toString());
    assertEquals(     "999999999999999", new CharBuffer().appendDec(     999999999999999L).toString());
    assertEquals(    "1000000000000000", new CharBuffer().appendDec(    1000000000000000L).toString());
    assertEquals(    "9999999999999999", new CharBuffer().appendDec(    9999999999999999L).toString());
    assertEquals(   "10000000000000000", new CharBuffer().appendDec(   10000000000000000L).toString());
    assertEquals(   "99999999999999999", new CharBuffer().appendDec(   99999999999999999L).toString());
    assertEquals(  "100000000000000000", new CharBuffer().appendDec(  100000000000000000L).toString());
    assertEquals(  "999999999999999999", new CharBuffer().appendDec(  999999999999999999L).toString());
    assertEquals( "1000000000000000000", new CharBuffer().appendDec( 1000000000000000000L).toString());
    assertEquals( "9223372036854775807", new CharBuffer().appendDec( 9223372036854775807L).toString());
    assertEquals("-9223372036854775808", new CharBuffer().appendDec(-9223372036854775808L).toString());
    assertEquals("-1000000000000000000", new CharBuffer().appendDec(-1000000000000000000L).toString());
    assertEquals( "-999999999999999999", new CharBuffer().appendDec( -999999999999999999L).toString());
    assertEquals( "-100000000000000000", new CharBuffer().appendDec( -100000000000000000L).toString());
    assertEquals(  "-99999999999999999", new CharBuffer().appendDec(  -99999999999999999L).toString());
    assertEquals(  "-10000000000000000", new CharBuffer().appendDec(  -10000000000000000L).toString());
    assertEquals(   "-9999999999999999", new CharBuffer().appendDec(   -9999999999999999L).toString());
    assertEquals(   "-1000000000000000", new CharBuffer().appendDec(   -1000000000000000L).toString());
    assertEquals(    "-999999999999999", new CharBuffer().appendDec(    -999999999999999L).toString());
    assertEquals(    "-100000000000000", new CharBuffer().appendDec(    -100000000000000L).toString());
    assertEquals(     "-99999999999999", new CharBuffer().appendDec(     -99999999999999L).toString());
    assertEquals(     "-10000000000000", new CharBuffer().appendDec(     -10000000000000L).toString());
    assertEquals(      "-9999999999999", new CharBuffer().appendDec(      -9999999999999L).toString());
    assertEquals(      "-1000000000000", new CharBuffer().appendDec(      -1000000000000L).toString());
    assertEquals(       "-999999999999", new CharBuffer().appendDec(       -999999999999L).toString());
    assertEquals(       "-100000000000", new CharBuffer().appendDec(       -100000000000L).toString());
    assertEquals(        "-99999999999", new CharBuffer().appendDec(        -99999999999L).toString());
    assertEquals(        "-10000000000", new CharBuffer().appendDec(        -10000000000L).toString());
    assertEquals(         "-9999999999", new CharBuffer().appendDec(         -9999999999L).toString());
    assertEquals(         "-1000000000", new CharBuffer().appendDec(         -1000000000L).toString());
    assertEquals(          "-999999999", new CharBuffer().appendDec(          -999999999L).toString());
    assertEquals(          "-100000000", new CharBuffer().appendDec(          -100000000L).toString());
    assertEquals(           "-99999999", new CharBuffer().appendDec(           -99999999L).toString());
    assertEquals(           "-10000000", new CharBuffer().appendDec(           -10000000L).toString());
    assertEquals(            "-9999999", new CharBuffer().appendDec(            -9999999L).toString());
    assertEquals(            "-1000000", new CharBuffer().appendDec(            -1000000L).toString());
    assertEquals(             "-999999", new CharBuffer().appendDec(             -999999L).toString());
    assertEquals(             "-100000", new CharBuffer().appendDec(             -100000L).toString());
    assertEquals(              "-99999", new CharBuffer().appendDec(              -99999L).toString());
    assertEquals(              "-10000", new CharBuffer().appendDec(              -10000L).toString());
    assertEquals(               "-9999", new CharBuffer().appendDec(               -9999L).toString());
    assertEquals(               "-1000", new CharBuffer().appendDec(               -1000L).toString());
    assertEquals(                "-999", new CharBuffer().appendDec(                -999L).toString());
    assertEquals(                "-100", new CharBuffer().appendDec(                -100L).toString());
    assertEquals(                 "-99", new CharBuffer().appendDec(                 -99L).toString());
    assertEquals(                 "-10", new CharBuffer().appendDec(                 -10L).toString());
    assertEquals(                  "-9", new CharBuffer().appendDec(                  -9L).toString());
    // @formatter:on
  }

  // Hexadecimal representation

  /**
   * Tests the {@link CharBuffer#appendHex(byte)} method.
   */
  @Test
  public void test_appendHex_byte() {
    // @formatter:off
    assertEquals("00", new CharBuffer().appendHex((byte) 0x00).toString());
    assertEquals("0f", new CharBuffer().appendHex((byte) 0x0f).toString());
    assertEquals("ff", new CharBuffer().appendHex((byte) 0xff).toString());
    assertEquals("f0", new CharBuffer().appendHex((byte) 0xf0).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(byte)} method.
   */
  @Test
  public void test_appendHexTrimZeros_byte() {
    // @formatter:off
    assertEquals( "0", new CharBuffer().appendHexTrimZeros((byte) 0x00).toString());
    assertEquals( "f", new CharBuffer().appendHexTrimZeros((byte) 0x0f).toString());
    assertEquals("ff", new CharBuffer().appendHexTrimZeros((byte) 0xff).toString());
    assertEquals("f0", new CharBuffer().appendHexTrimZeros((byte) 0xf0).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(byte)} method.
   */
  @Test
  public void test_getHexCapacity_byte() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x00));
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x0f));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0xff));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0xf0));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(short)} method.
   */
  @Test
  public void test_appendHex_short() {
    // @formatter:off
    assertEquals("0000", new CharBuffer().appendHex((short) 0x0000).toString());
    assertEquals("000f", new CharBuffer().appendHex((short) 0x000f).toString());
    assertEquals("00ff", new CharBuffer().appendHex((short) 0x00ff).toString());
    assertEquals("0fff", new CharBuffer().appendHex((short) 0x0fff).toString());
    assertEquals("ffff", new CharBuffer().appendHex((short) 0xffff).toString());
    assertEquals("fff0", new CharBuffer().appendHex((short) 0xfff0).toString());
    assertEquals("ff00", new CharBuffer().appendHex((short) 0xff00).toString());
    assertEquals("f000", new CharBuffer().appendHex((short) 0xf000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(short)} method.
   */
  @Test
  public void test_appendHexTrimZeros_short() {
    // @formatter:off
    assertEquals(   "0", new CharBuffer().appendHexTrimZeros((short) 0x0000).toString());
    assertEquals(   "f", new CharBuffer().appendHexTrimZeros((short) 0x000f).toString());
    assertEquals(  "ff", new CharBuffer().appendHexTrimZeros((short) 0x00ff).toString());
    assertEquals( "fff", new CharBuffer().appendHexTrimZeros((short) 0x0fff).toString());
    assertEquals("ffff", new CharBuffer().appendHexTrimZeros((short) 0xffff).toString());
    assertEquals("fff0", new CharBuffer().appendHexTrimZeros((short) 0xfff0).toString());
    assertEquals("ff00", new CharBuffer().appendHexTrimZeros((short) 0xff00).toString());
    assertEquals("f000", new CharBuffer().appendHexTrimZeros((short) 0xf000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(short)} method.
   */
  @Test
  public void test_getHexCapacity_short() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0000));
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x000f));
    assertEquals(2, CharBuffer.getHexCapacity((short) 0x00ff));
    assertEquals(3, CharBuffer.getHexCapacity((short) 0x0fff));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0xffff));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0xfff0));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0xff00));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0xf000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(int)} method.
   */
  @Test
  public void test_appendHex_int() {
    // @formatter:off
    assertEquals("00000000", new CharBuffer().appendHex(0x00000000).toString());
    assertEquals("0000000f", new CharBuffer().appendHex(0x0000000f).toString());
    assertEquals("000000ff", new CharBuffer().appendHex(0x000000ff).toString());
    assertEquals("00000fff", new CharBuffer().appendHex(0x00000fff).toString());
    assertEquals("0000ffff", new CharBuffer().appendHex(0x0000ffff).toString());
    assertEquals("000fffff", new CharBuffer().appendHex(0x000fffff).toString());
    assertEquals("00ffffff", new CharBuffer().appendHex(0x00ffffff).toString());
    assertEquals("0fffffff", new CharBuffer().appendHex(0x0fffffff).toString());
    assertEquals("ffffffff", new CharBuffer().appendHex(0xffffffff).toString());
    assertEquals("fffffff0", new CharBuffer().appendHex(0xfffffff0).toString());
    assertEquals("ffffff00", new CharBuffer().appendHex(0xffffff00).toString());
    assertEquals("fffff000", new CharBuffer().appendHex(0xfffff000).toString());
    assertEquals("ffff0000", new CharBuffer().appendHex(0xffff0000).toString());
    assertEquals("fff00000", new CharBuffer().appendHex(0xfff00000).toString());
    assertEquals("ff000000", new CharBuffer().appendHex(0xff000000).toString());
    assertEquals("f0000000", new CharBuffer().appendHex(0xf0000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(int)} method.
   */
  @Test
  public void test_appendHexTrimZeros_int() {
    // @formatter:off
    assertEquals(       "0", new CharBuffer().appendHexTrimZeros(0x00000000).toString());
    assertEquals(       "f", new CharBuffer().appendHexTrimZeros(0x0000000f).toString());
    assertEquals(      "ff", new CharBuffer().appendHexTrimZeros(0x000000ff).toString());
    assertEquals(     "fff", new CharBuffer().appendHexTrimZeros(0x00000fff).toString());
    assertEquals(    "ffff", new CharBuffer().appendHexTrimZeros(0x0000ffff).toString());
    assertEquals(   "fffff", new CharBuffer().appendHexTrimZeros(0x000fffff).toString());
    assertEquals(  "ffffff", new CharBuffer().appendHexTrimZeros(0x00ffffff).toString());
    assertEquals( "fffffff", new CharBuffer().appendHexTrimZeros(0x0fffffff).toString());
    assertEquals("ffffffff", new CharBuffer().appendHexTrimZeros(0xffffffff).toString());
    assertEquals("fffffff0", new CharBuffer().appendHexTrimZeros(0xfffffff0).toString());
    assertEquals("fffffff0", new CharBuffer().appendHexTrimZeros(0xfffffff0).toString());
    assertEquals("ffffff00", new CharBuffer().appendHexTrimZeros(0xffffff00).toString());
    assertEquals("fffff000", new CharBuffer().appendHexTrimZeros(0xfffff000).toString());
    assertEquals("ffff0000", new CharBuffer().appendHexTrimZeros(0xffff0000).toString());
    assertEquals("fff00000", new CharBuffer().appendHexTrimZeros(0xfff00000).toString());
    assertEquals("ff000000", new CharBuffer().appendHexTrimZeros(0xff000000).toString());
    assertEquals("f0000000", new CharBuffer().appendHexTrimZeros(0xf0000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(int)} method.
   */
  @Test
  public void test_getHexCapacity_int() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity(0x00000000));
    assertEquals(1, CharBuffer.getHexCapacity(0x0000000f));
    assertEquals(2, CharBuffer.getHexCapacity(0x000000ff));
    assertEquals(3, CharBuffer.getHexCapacity(0x00000fff));
    assertEquals(4, CharBuffer.getHexCapacity(0x0000ffff));
    assertEquals(5, CharBuffer.getHexCapacity(0x000fffff));
    assertEquals(6, CharBuffer.getHexCapacity(0x00ffffff));
    assertEquals(7, CharBuffer.getHexCapacity(0x0fffffff));
    assertEquals(8, CharBuffer.getHexCapacity(0xffffffff));
    assertEquals(8, CharBuffer.getHexCapacity(0xfffffff0));
    assertEquals(8, CharBuffer.getHexCapacity(0xffffff00));
    assertEquals(8, CharBuffer.getHexCapacity(0xfffff000));
    assertEquals(8, CharBuffer.getHexCapacity(0xffff0000));
    assertEquals(8, CharBuffer.getHexCapacity(0xfff00000));
    assertEquals(8, CharBuffer.getHexCapacity(0xff000000));
    assertEquals(8, CharBuffer.getHexCapacity(0xf0000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(long)} method.
   */
  @Test
  public void test_appendHex_long() {
    // @formatter:off
    assertEquals("0000000000000000", new CharBuffer().appendHex(0x0000000000000000L).toString());
    assertEquals("000000000000000f", new CharBuffer().appendHex(0x000000000000000fL).toString());
    assertEquals("00000000000000ff", new CharBuffer().appendHex(0x00000000000000ffL).toString());
    assertEquals("0000000000000fff", new CharBuffer().appendHex(0x0000000000000fffL).toString());
    assertEquals("000000000000ffff", new CharBuffer().appendHex(0x000000000000ffffL).toString());
    assertEquals("00000000000fffff", new CharBuffer().appendHex(0x00000000000fffffL).toString());
    assertEquals("0000000000ffffff", new CharBuffer().appendHex(0x0000000000ffffffL).toString());
    assertEquals("000000000fffffff", new CharBuffer().appendHex(0x000000000fffffffL).toString());
    assertEquals("00000000ffffffff", new CharBuffer().appendHex(0x00000000ffffffffL).toString());
    assertEquals("0000000fffffffff", new CharBuffer().appendHex(0x0000000fffffffffL).toString());
    assertEquals("000000ffffffffff", new CharBuffer().appendHex(0x000000ffffffffffL).toString());
    assertEquals("00000fffffffffff", new CharBuffer().appendHex(0x00000fffffffffffL).toString());
    assertEquals("0000ffffffffffff", new CharBuffer().appendHex(0x0000ffffffffffffL).toString());
    assertEquals("000fffffffffffff", new CharBuffer().appendHex(0x000fffffffffffffL).toString());
    assertEquals("00ffffffffffffff", new CharBuffer().appendHex(0x00ffffffffffffffL).toString());
    assertEquals("0fffffffffffffff", new CharBuffer().appendHex(0x0fffffffffffffffL).toString());
    assertEquals("ffffffffffffffff", new CharBuffer().appendHex(0xffffffffffffffffL).toString());
    assertEquals("fffffffffffffff0", new CharBuffer().appendHex(0xfffffffffffffff0L).toString());
    assertEquals("ffffffffffffff00", new CharBuffer().appendHex(0xffffffffffffff00L).toString());
    assertEquals("fffffffffffff000", new CharBuffer().appendHex(0xfffffffffffff000L).toString());
    assertEquals("ffffffffffff0000", new CharBuffer().appendHex(0xffffffffffff0000L).toString());
    assertEquals("fffffffffff00000", new CharBuffer().appendHex(0xfffffffffff00000L).toString());
    assertEquals("ffffffffff000000", new CharBuffer().appendHex(0xffffffffff000000L).toString());
    assertEquals("fffffffff0000000", new CharBuffer().appendHex(0xfffffffff0000000L).toString());
    assertEquals("ffffffff00000000", new CharBuffer().appendHex(0xffffffff00000000L).toString());
    assertEquals("fffffff000000000", new CharBuffer().appendHex(0xfffffff000000000L).toString());
    assertEquals("ffffff0000000000", new CharBuffer().appendHex(0xffffff0000000000L).toString());
    assertEquals("fffff00000000000", new CharBuffer().appendHex(0xfffff00000000000L).toString());
    assertEquals("ffff000000000000", new CharBuffer().appendHex(0xffff000000000000L).toString());
    assertEquals("fff0000000000000", new CharBuffer().appendHex(0xfff0000000000000L).toString());
    assertEquals("ff00000000000000", new CharBuffer().appendHex(0xff00000000000000L).toString());
    assertEquals("f000000000000000", new CharBuffer().appendHex(0xf000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(long)} method.
   */
  @Test
  public void test_appendHexTrimZeros_long() {
    // @formatter:off
    assertEquals(               "0", new CharBuffer().appendHexTrimZeros(0x0000000000000000L).toString());
    assertEquals(               "f", new CharBuffer().appendHexTrimZeros(0x000000000000000fL).toString());
    assertEquals(              "ff", new CharBuffer().appendHexTrimZeros(0x00000000000000ffL).toString());
    assertEquals(             "fff", new CharBuffer().appendHexTrimZeros(0x0000000000000fffL).toString());
    assertEquals(            "ffff", new CharBuffer().appendHexTrimZeros(0x000000000000ffffL).toString());
    assertEquals(           "fffff", new CharBuffer().appendHexTrimZeros(0x00000000000fffffL).toString());
    assertEquals(          "ffffff", new CharBuffer().appendHexTrimZeros(0x0000000000ffffffL).toString());
    assertEquals(         "fffffff", new CharBuffer().appendHexTrimZeros(0x000000000fffffffL).toString());
    assertEquals(        "ffffffff", new CharBuffer().appendHexTrimZeros(0x00000000ffffffffL).toString());
    assertEquals(       "fffffffff", new CharBuffer().appendHexTrimZeros(0x0000000fffffffffL).toString());
    assertEquals(      "ffffffffff", new CharBuffer().appendHexTrimZeros(0x000000ffffffffffL).toString());
    assertEquals(     "fffffffffff", new CharBuffer().appendHexTrimZeros(0x00000fffffffffffL).toString());
    assertEquals(    "ffffffffffff", new CharBuffer().appendHexTrimZeros(0x0000ffffffffffffL).toString());
    assertEquals(   "fffffffffffff", new CharBuffer().appendHexTrimZeros(0x000fffffffffffffL).toString());
    assertEquals(  "ffffffffffffff", new CharBuffer().appendHexTrimZeros(0x00ffffffffffffffL).toString());
    assertEquals( "fffffffffffffff", new CharBuffer().appendHexTrimZeros(0x0fffffffffffffffL).toString());
    assertEquals("ffffffffffffffff", new CharBuffer().appendHexTrimZeros(0xffffffffffffffffL).toString());
    assertEquals("fffffffffffffff0", new CharBuffer().appendHexTrimZeros(0xfffffffffffffff0L).toString());
    assertEquals("ffffffffffffff00", new CharBuffer().appendHexTrimZeros(0xffffffffffffff00L).toString());
    assertEquals("fffffffffffff000", new CharBuffer().appendHexTrimZeros(0xfffffffffffff000L).toString());
    assertEquals("ffffffffffff0000", new CharBuffer().appendHexTrimZeros(0xffffffffffff0000L).toString());
    assertEquals("fffffffffff00000", new CharBuffer().appendHexTrimZeros(0xfffffffffff00000L).toString());
    assertEquals("ffffffffff000000", new CharBuffer().appendHexTrimZeros(0xffffffffff000000L).toString());
    assertEquals("fffffffff0000000", new CharBuffer().appendHexTrimZeros(0xfffffffff0000000L).toString());
    assertEquals("ffffffff00000000", new CharBuffer().appendHexTrimZeros(0xffffffff00000000L).toString());
    assertEquals("fffffff000000000", new CharBuffer().appendHexTrimZeros(0xfffffff000000000L).toString());
    assertEquals("ffffff0000000000", new CharBuffer().appendHexTrimZeros(0xffffff0000000000L).toString());
    assertEquals("fffff00000000000", new CharBuffer().appendHexTrimZeros(0xfffff00000000000L).toString());
    assertEquals("ffff000000000000", new CharBuffer().appendHexTrimZeros(0xffff000000000000L).toString());
    assertEquals("fff0000000000000", new CharBuffer().appendHexTrimZeros(0xfff0000000000000L).toString());
    assertEquals("ff00000000000000", new CharBuffer().appendHexTrimZeros(0xff00000000000000L).toString());
    assertEquals("f000000000000000", new CharBuffer().appendHexTrimZeros(0xf000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(long)} method.
   */
  @Test
  public void test_getHexCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000000L));
    assertEquals( 1, CharBuffer.getHexCapacity(0x000000000000000fL));
    assertEquals( 2, CharBuffer.getHexCapacity(0x00000000000000ffL));
    assertEquals( 3, CharBuffer.getHexCapacity(0x0000000000000fffL));
    assertEquals( 4, CharBuffer.getHexCapacity(0x000000000000ffffL));
    assertEquals( 5, CharBuffer.getHexCapacity(0x00000000000fffffL));
    assertEquals( 6, CharBuffer.getHexCapacity(0x0000000000ffffffL));
    assertEquals( 7, CharBuffer.getHexCapacity(0x000000000fffffffL));
    assertEquals( 8, CharBuffer.getHexCapacity(0x00000000ffffffffL));
    assertEquals( 9, CharBuffer.getHexCapacity(0x0000000fffffffffL));
    assertEquals(10, CharBuffer.getHexCapacity(0x000000ffffffffffL));
    assertEquals(11, CharBuffer.getHexCapacity(0x00000fffffffffffL));
    assertEquals(12, CharBuffer.getHexCapacity(0x0000ffffffffffffL));
    assertEquals(13, CharBuffer.getHexCapacity(0x000fffffffffffffL));
    assertEquals(14, CharBuffer.getHexCapacity(0x00ffffffffffffffL));
    assertEquals(15, CharBuffer.getHexCapacity(0x0fffffffffffffffL));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffffffffffffffL));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffffffffffffff0L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffffffffffff00L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffffffffffff000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffffffffff0000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffffffffff00000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffffffff000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffffffff0000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffffff00000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffffff000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffffff0000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfffff00000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xffff000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xfff0000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xff00000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0xf000000000000000L));
    // @formatter:on
  }

  // Binary representation

  /**
   * Tests the {@link CharBuffer#appendBin(byte)} method.
   */
  @Test
  public void test_appendBin_byte() {
    // @formatter:off
    assertEquals("00000000", new CharBuffer().appendBin((byte) 0x00).toString());
    assertEquals("00000001", new CharBuffer().appendBin((byte) 0x01).toString());
    assertEquals("00000010", new CharBuffer().appendBin((byte) 0x02).toString());
    assertEquals("00000100", new CharBuffer().appendBin((byte) 0x04).toString());
    assertEquals("00001000", new CharBuffer().appendBin((byte) 0x08).toString());
    assertEquals("00010000", new CharBuffer().appendBin((byte) 0x10).toString());
    assertEquals("00100000", new CharBuffer().appendBin((byte) 0x20).toString());
    assertEquals("01000000", new CharBuffer().appendBin((byte) 0x40).toString());
    assertEquals("10000000", new CharBuffer().appendBin((byte) 0x80).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinTrimZeros(byte)} method.
   */
  @Test
  public void test_appendBinTrimZeros_byte() {
    // @formatter:off
    assertEquals(       "0", new CharBuffer().appendBinTrimZeros((byte) 0x00).toString());
    assertEquals(       "1", new CharBuffer().appendBinTrimZeros((byte) 0x01).toString());
    assertEquals(      "10", new CharBuffer().appendBinTrimZeros((byte) 0x02).toString());
    assertEquals(     "100", new CharBuffer().appendBinTrimZeros((byte) 0x04).toString());
    assertEquals(    "1000", new CharBuffer().appendBinTrimZeros((byte) 0x08).toString());
    assertEquals(   "10000", new CharBuffer().appendBinTrimZeros((byte) 0x10).toString());
    assertEquals(  "100000", new CharBuffer().appendBinTrimZeros((byte) 0x20).toString());
    assertEquals( "1000000", new CharBuffer().appendBinTrimZeros((byte) 0x40).toString());
    assertEquals("10000000", new CharBuffer().appendBinTrimZeros((byte) 0x80).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(byte)} method.
   */
  @Test
  public void test_getBinCapacity_byte() {
    // @formatter:off
    assertEquals(1, CharBuffer.getBinCapacity((byte) 0x00));
    assertEquals(1, CharBuffer.getBinCapacity((byte) 0x01));
    assertEquals(2, CharBuffer.getBinCapacity((byte) 0x02));
    assertEquals(3, CharBuffer.getBinCapacity((byte) 0x04));
    assertEquals(4, CharBuffer.getBinCapacity((byte) 0x08));
    assertEquals(5, CharBuffer.getBinCapacity((byte) 0x10));
    assertEquals(6, CharBuffer.getBinCapacity((byte) 0x20));
    assertEquals(7, CharBuffer.getBinCapacity((byte) 0x40));
    assertEquals(8, CharBuffer.getBinCapacity((byte) 0x80));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(short)} method.
   */
  @Test
  public void test_appendBin_short() {
    // @formatter:off
    assertEquals("0000000000000000", new CharBuffer().appendBin((short) 0x0000).toString());
    assertEquals("0000000000000001", new CharBuffer().appendBin((short) 0x0001).toString());
    assertEquals("0000000000000010", new CharBuffer().appendBin((short) 0x0002).toString());
    assertEquals("0000000000000100", new CharBuffer().appendBin((short) 0x0004).toString());
    assertEquals("0000000000001000", new CharBuffer().appendBin((short) 0x0008).toString());
    assertEquals("0000000000010000", new CharBuffer().appendBin((short) 0x0010).toString());
    assertEquals("0000000000100000", new CharBuffer().appendBin((short) 0x0020).toString());
    assertEquals("0000000001000000", new CharBuffer().appendBin((short) 0x0040).toString());
    assertEquals("0000000010000000", new CharBuffer().appendBin((short) 0x0080).toString());
    assertEquals("0000000100000000", new CharBuffer().appendBin((short) 0x0100).toString());
    assertEquals("0000001000000000", new CharBuffer().appendBin((short) 0x0200).toString());
    assertEquals("0000010000000000", new CharBuffer().appendBin((short) 0x0400).toString());
    assertEquals("0000100000000000", new CharBuffer().appendBin((short) 0x0800).toString());
    assertEquals("0001000000000000", new CharBuffer().appendBin((short) 0x1000).toString());
    assertEquals("0010000000000000", new CharBuffer().appendBin((short) 0x2000).toString());
    assertEquals("0100000000000000", new CharBuffer().appendBin((short) 0x4000).toString());
    assertEquals("1000000000000000", new CharBuffer().appendBin((short) 0x8000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinTrimZeros(short)} method.
   */
  @Test
  public void test_appendBinTrimZeros_short() {
    // @formatter:off
    assertEquals(               "0", new CharBuffer().appendBinTrimZeros((short) 0x0000).toString());
    assertEquals(               "1", new CharBuffer().appendBinTrimZeros((short) 0x0001).toString());
    assertEquals(              "10", new CharBuffer().appendBinTrimZeros((short) 0x0002).toString());
    assertEquals(             "100", new CharBuffer().appendBinTrimZeros((short) 0x0004).toString());
    assertEquals(            "1000", new CharBuffer().appendBinTrimZeros((short) 0x0008).toString());
    assertEquals(           "10000", new CharBuffer().appendBinTrimZeros((short) 0x0010).toString());
    assertEquals(          "100000", new CharBuffer().appendBinTrimZeros((short) 0x0020).toString());
    assertEquals(         "1000000", new CharBuffer().appendBinTrimZeros((short) 0x0040).toString());
    assertEquals(        "10000000", new CharBuffer().appendBinTrimZeros((short) 0x0080).toString());
    assertEquals(       "100000000", new CharBuffer().appendBinTrimZeros((short) 0x0100).toString());
    assertEquals(      "1000000000", new CharBuffer().appendBinTrimZeros((short) 0x0200).toString());
    assertEquals(     "10000000000", new CharBuffer().appendBinTrimZeros((short) 0x0400).toString());
    assertEquals(    "100000000000", new CharBuffer().appendBinTrimZeros((short) 0x0800).toString());
    assertEquals(   "1000000000000", new CharBuffer().appendBinTrimZeros((short) 0x1000).toString());
    assertEquals(  "10000000000000", new CharBuffer().appendBinTrimZeros((short) 0x2000).toString());
    assertEquals( "100000000000000", new CharBuffer().appendBinTrimZeros((short) 0x4000).toString());
    assertEquals("1000000000000000", new CharBuffer().appendBinTrimZeros((short) 0x8000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(short)} method.
   */
  @Test
  public void test_getBinCapacity_short() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity((short) 0x0000));
    assertEquals( 1, CharBuffer.getBinCapacity((short) 0x0001));
    assertEquals( 2, CharBuffer.getBinCapacity((short) 0x0002));
    assertEquals( 3, CharBuffer.getBinCapacity((short) 0x0004));
    assertEquals( 4, CharBuffer.getBinCapacity((short) 0x0008));
    assertEquals( 5, CharBuffer.getBinCapacity((short) 0x0010));
    assertEquals( 6, CharBuffer.getBinCapacity((short) 0x0020));
    assertEquals( 7, CharBuffer.getBinCapacity((short) 0x0040));
    assertEquals( 8, CharBuffer.getBinCapacity((short) 0x0080));
    assertEquals( 9, CharBuffer.getBinCapacity((short) 0x0100));
    assertEquals(10, CharBuffer.getBinCapacity((short) 0x0200));
    assertEquals(11, CharBuffer.getBinCapacity((short) 0x0400));
    assertEquals(12, CharBuffer.getBinCapacity((short) 0x0800));
    assertEquals(13, CharBuffer.getBinCapacity((short) 0x1000));
    assertEquals(14, CharBuffer.getBinCapacity((short) 0x2000));
    assertEquals(15, CharBuffer.getBinCapacity((short) 0x4000));
    assertEquals(16, CharBuffer.getBinCapacity((short) 0x8000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(int)} method.
   */
  @Test
  public void test_appendBin_int() {
    // @formatter:off
    assertEquals("00000000000000000000000000000000", new CharBuffer().appendBin(0x00000000).toString());
    assertEquals("00000000000000000000000000000001", new CharBuffer().appendBin(0x00000001).toString());
    assertEquals("00000000000000000000000000000010", new CharBuffer().appendBin(0x00000002).toString());
    assertEquals("00000000000000000000000000000100", new CharBuffer().appendBin(0x00000004).toString());
    assertEquals("00000000000000000000000000001000", new CharBuffer().appendBin(0x00000008).toString());
    assertEquals("00000000000000000000000000010000", new CharBuffer().appendBin(0x00000010).toString());
    assertEquals("00000000000000000000000000100000", new CharBuffer().appendBin(0x00000020).toString());
    assertEquals("00000000000000000000000001000000", new CharBuffer().appendBin(0x00000040).toString());
    assertEquals("00000000000000000000000010000000", new CharBuffer().appendBin(0x00000080).toString());
    assertEquals("00000000000000000000000100000000", new CharBuffer().appendBin(0x00000100).toString());
    assertEquals("00000000000000000000001000000000", new CharBuffer().appendBin(0x00000200).toString());
    assertEquals("00000000000000000000010000000000", new CharBuffer().appendBin(0x00000400).toString());
    assertEquals("00000000000000000000100000000000", new CharBuffer().appendBin(0x00000800).toString());
    assertEquals("00000000000000000001000000000000", new CharBuffer().appendBin(0x00001000).toString());
    assertEquals("00000000000000000010000000000000", new CharBuffer().appendBin(0x00002000).toString());
    assertEquals("00000000000000000100000000000000", new CharBuffer().appendBin(0x00004000).toString());
    assertEquals("00000000000000001000000000000000", new CharBuffer().appendBin(0x00008000).toString());
    assertEquals("00000000000000010000000000000000", new CharBuffer().appendBin(0x00010000).toString());
    assertEquals("00000000000000100000000000000000", new CharBuffer().appendBin(0x00020000).toString());
    assertEquals("00000000000001000000000000000000", new CharBuffer().appendBin(0x00040000).toString());
    assertEquals("00000000000010000000000000000000", new CharBuffer().appendBin(0x00080000).toString());
    assertEquals("00000000000100000000000000000000", new CharBuffer().appendBin(0x00100000).toString());
    assertEquals("00000000001000000000000000000000", new CharBuffer().appendBin(0x00200000).toString());
    assertEquals("00000000010000000000000000000000", new CharBuffer().appendBin(0x00400000).toString());
    assertEquals("00000000100000000000000000000000", new CharBuffer().appendBin(0x00800000).toString());
    assertEquals("00000001000000000000000000000000", new CharBuffer().appendBin(0x01000000).toString());
    assertEquals("00000010000000000000000000000000", new CharBuffer().appendBin(0x02000000).toString());
    assertEquals("00000100000000000000000000000000", new CharBuffer().appendBin(0x04000000).toString());
    assertEquals("00001000000000000000000000000000", new CharBuffer().appendBin(0x08000000).toString());
    assertEquals("00010000000000000000000000000000", new CharBuffer().appendBin(0x10000000).toString());
    assertEquals("00100000000000000000000000000000", new CharBuffer().appendBin(0x20000000).toString());
    assertEquals("01000000000000000000000000000000", new CharBuffer().appendBin(0x40000000).toString());
    assertEquals("10000000000000000000000000000000", new CharBuffer().appendBin(0x80000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinTrimZeros(int)} method.
   */
  @Test
  public void test_appendBinTrimZeros_int() {
    // @formatter:off
    assertEquals(                               "0", new CharBuffer().appendBinTrimZeros(0x00000000).toString());
    assertEquals(                               "1", new CharBuffer().appendBinTrimZeros(0x00000001).toString());
    assertEquals(                              "10", new CharBuffer().appendBinTrimZeros(0x00000002).toString());
    assertEquals(                             "100", new CharBuffer().appendBinTrimZeros(0x00000004).toString());
    assertEquals(                            "1000", new CharBuffer().appendBinTrimZeros(0x00000008).toString());
    assertEquals(                           "10000", new CharBuffer().appendBinTrimZeros(0x00000010).toString());
    assertEquals(                          "100000", new CharBuffer().appendBinTrimZeros(0x00000020).toString());
    assertEquals(                         "1000000", new CharBuffer().appendBinTrimZeros(0x00000040).toString());
    assertEquals(                        "10000000", new CharBuffer().appendBinTrimZeros(0x00000080).toString());
    assertEquals(                       "100000000", new CharBuffer().appendBinTrimZeros(0x00000100).toString());
    assertEquals(                      "1000000000", new CharBuffer().appendBinTrimZeros(0x00000200).toString());
    assertEquals(                     "10000000000", new CharBuffer().appendBinTrimZeros(0x00000400).toString());
    assertEquals(                    "100000000000", new CharBuffer().appendBinTrimZeros(0x00000800).toString());
    assertEquals(                   "1000000000000", new CharBuffer().appendBinTrimZeros(0x00001000).toString());
    assertEquals(                  "10000000000000", new CharBuffer().appendBinTrimZeros(0x00002000).toString());
    assertEquals(                 "100000000000000", new CharBuffer().appendBinTrimZeros(0x00004000).toString());
    assertEquals(                "1000000000000000", new CharBuffer().appendBinTrimZeros(0x00008000).toString());
    assertEquals(               "10000000000000000", new CharBuffer().appendBinTrimZeros(0x00010000).toString());
    assertEquals(              "100000000000000000", new CharBuffer().appendBinTrimZeros(0x00020000).toString());
    assertEquals(             "1000000000000000000", new CharBuffer().appendBinTrimZeros(0x00040000).toString());
    assertEquals(            "10000000000000000000", new CharBuffer().appendBinTrimZeros(0x00080000).toString());
    assertEquals(           "100000000000000000000", new CharBuffer().appendBinTrimZeros(0x00100000).toString());
    assertEquals(          "1000000000000000000000", new CharBuffer().appendBinTrimZeros(0x00200000).toString());
    assertEquals(         "10000000000000000000000", new CharBuffer().appendBinTrimZeros(0x00400000).toString());
    assertEquals(        "100000000000000000000000", new CharBuffer().appendBinTrimZeros(0x00800000).toString());
    assertEquals(       "1000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x01000000).toString());
    assertEquals(      "10000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x02000000).toString());
    assertEquals(     "100000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x04000000).toString());
    assertEquals(    "1000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x08000000).toString());
    assertEquals(   "10000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x10000000).toString());
    assertEquals(  "100000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x20000000).toString());
    assertEquals( "1000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x40000000).toString());
    assertEquals("10000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x80000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(int)} method.
   */
  @Test
  public void test_getBinCapacity_int() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity(0x00000000));
    assertEquals( 1, CharBuffer.getBinCapacity(0x00000001));
    assertEquals( 2, CharBuffer.getBinCapacity(0x00000002));
    assertEquals( 3, CharBuffer.getBinCapacity(0x00000004));
    assertEquals( 4, CharBuffer.getBinCapacity(0x00000008));
    assertEquals( 5, CharBuffer.getBinCapacity(0x00000010));
    assertEquals( 6, CharBuffer.getBinCapacity(0x00000020));
    assertEquals( 7, CharBuffer.getBinCapacity(0x00000040));
    assertEquals( 8, CharBuffer.getBinCapacity(0x00000080));
    assertEquals( 9, CharBuffer.getBinCapacity(0x00000100));
    assertEquals(10, CharBuffer.getBinCapacity(0x00000200));
    assertEquals(11, CharBuffer.getBinCapacity(0x00000400));
    assertEquals(12, CharBuffer.getBinCapacity(0x00000800));
    assertEquals(13, CharBuffer.getBinCapacity(0x00001000));
    assertEquals(14, CharBuffer.getBinCapacity(0x00002000));
    assertEquals(15, CharBuffer.getBinCapacity(0x00004000));
    assertEquals(16, CharBuffer.getBinCapacity(0x00008000));
    assertEquals(17, CharBuffer.getBinCapacity(0x00010000));
    assertEquals(18, CharBuffer.getBinCapacity(0x00020000));
    assertEquals(19, CharBuffer.getBinCapacity(0x00040000));
    assertEquals(20, CharBuffer.getBinCapacity(0x00080000));
    assertEquals(21, CharBuffer.getBinCapacity(0x00100000));
    assertEquals(22, CharBuffer.getBinCapacity(0x00200000));
    assertEquals(23, CharBuffer.getBinCapacity(0x00400000));
    assertEquals(24, CharBuffer.getBinCapacity(0x00800000));
    assertEquals(25, CharBuffer.getBinCapacity(0x01000000));
    assertEquals(26, CharBuffer.getBinCapacity(0x02000000));
    assertEquals(27, CharBuffer.getBinCapacity(0x04000000));
    assertEquals(28, CharBuffer.getBinCapacity(0x08000000));
    assertEquals(29, CharBuffer.getBinCapacity(0x10000000));
    assertEquals(30, CharBuffer.getBinCapacity(0x20000000));
    assertEquals(31, CharBuffer.getBinCapacity(0x40000000));
    assertEquals(32, CharBuffer.getBinCapacity(0x80000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(long)} method.
   */
  @Test
  public void test_appendBin_long() {
    // @formatter:off
    assertEquals("0000000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000001", new CharBuffer().appendBin(0x0000000000000001L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000010", new CharBuffer().appendBin(0x0000000000000002L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000100", new CharBuffer().appendBin(0x0000000000000004L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000001000", new CharBuffer().appendBin(0x0000000000000008L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000010000", new CharBuffer().appendBin(0x0000000000000010L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000100000", new CharBuffer().appendBin(0x0000000000000020L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000001000000", new CharBuffer().appendBin(0x0000000000000040L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000010000000", new CharBuffer().appendBin(0x0000000000000080L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000100000000", new CharBuffer().appendBin(0x0000000000000100L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000001000000000", new CharBuffer().appendBin(0x0000000000000200L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000010000000000", new CharBuffer().appendBin(0x0000000000000400L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000100000000000", new CharBuffer().appendBin(0x0000000000000800L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000001000000000000", new CharBuffer().appendBin(0x0000000000001000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000010000000000000", new CharBuffer().appendBin(0x0000000000002000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000100000000000000", new CharBuffer().appendBin(0x0000000000004000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000001000000000000000", new CharBuffer().appendBin(0x0000000000008000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000010000000000000000", new CharBuffer().appendBin(0x0000000000010000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000100000000000000000", new CharBuffer().appendBin(0x0000000000020000L).toString());
    assertEquals("0000000000000000000000000000000000000000000001000000000000000000", new CharBuffer().appendBin(0x0000000000040000L).toString());
    assertEquals("0000000000000000000000000000000000000000000010000000000000000000", new CharBuffer().appendBin(0x0000000000080000L).toString());
    assertEquals("0000000000000000000000000000000000000000000100000000000000000000", new CharBuffer().appendBin(0x0000000000100000L).toString());
    assertEquals("0000000000000000000000000000000000000000001000000000000000000000", new CharBuffer().appendBin(0x0000000000200000L).toString());
    assertEquals("0000000000000000000000000000000000000000010000000000000000000000", new CharBuffer().appendBin(0x0000000000400000L).toString());
    assertEquals("0000000000000000000000000000000000000000100000000000000000000000", new CharBuffer().appendBin(0x0000000000800000L).toString());
    assertEquals("0000000000000000000000000000000000000001000000000000000000000000", new CharBuffer().appendBin(0x0000000001000000L).toString());
    assertEquals("0000000000000000000000000000000000000010000000000000000000000000", new CharBuffer().appendBin(0x0000000002000000L).toString());
    assertEquals("0000000000000000000000000000000000000100000000000000000000000000", new CharBuffer().appendBin(0x0000000004000000L).toString());
    assertEquals("0000000000000000000000000000000000001000000000000000000000000000", new CharBuffer().appendBin(0x0000000008000000L).toString());
    assertEquals("0000000000000000000000000000000000010000000000000000000000000000", new CharBuffer().appendBin(0x0000000010000000L).toString());
    assertEquals("0000000000000000000000000000000000100000000000000000000000000000", new CharBuffer().appendBin(0x0000000020000000L).toString());
    assertEquals("0000000000000000000000000000000001000000000000000000000000000000", new CharBuffer().appendBin(0x0000000040000000L).toString());
    assertEquals("0000000000000000000000000000000010000000000000000000000000000000", new CharBuffer().appendBin(0x0000000080000000L).toString());
    assertEquals("0000000000000000000000000000000100000000000000000000000000000000", new CharBuffer().appendBin(0x0000000100000000L).toString());
    assertEquals("0000000000000000000000000000001000000000000000000000000000000000", new CharBuffer().appendBin(0x0000000200000000L).toString());
    assertEquals("0000000000000000000000000000010000000000000000000000000000000000", new CharBuffer().appendBin(0x0000000400000000L).toString());
    assertEquals("0000000000000000000000000000100000000000000000000000000000000000", new CharBuffer().appendBin(0x0000000800000000L).toString());
    assertEquals("0000000000000000000000000001000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000001000000000L).toString());
    assertEquals("0000000000000000000000000010000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000002000000000L).toString());
    assertEquals("0000000000000000000000000100000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000004000000000L).toString());
    assertEquals("0000000000000000000000001000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000008000000000L).toString());
    assertEquals("0000000000000000000000010000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000010000000000L).toString());
    assertEquals("0000000000000000000000100000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000020000000000L).toString());
    assertEquals("0000000000000000000001000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000040000000000L).toString());
    assertEquals("0000000000000000000010000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000080000000000L).toString());
    assertEquals("0000000000000000000100000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000100000000000L).toString());
    assertEquals("0000000000000000001000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000200000000000L).toString());
    assertEquals("0000000000000000010000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000400000000000L).toString());
    assertEquals("0000000000000000100000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0000800000000000L).toString());
    assertEquals("0000000000000001000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0001000000000000L).toString());
    assertEquals("0000000000000010000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0002000000000000L).toString());
    assertEquals("0000000000000100000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0004000000000000L).toString());
    assertEquals("0000000000001000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0008000000000000L).toString());
    assertEquals("0000000000010000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0010000000000000L).toString());
    assertEquals("0000000000100000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0020000000000000L).toString());
    assertEquals("0000000001000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0040000000000000L).toString());
    assertEquals("0000000010000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0080000000000000L).toString());
    assertEquals("0000000100000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0100000000000000L).toString());
    assertEquals("0000001000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0200000000000000L).toString());
    assertEquals("0000010000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0400000000000000L).toString());
    assertEquals("0000100000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x0800000000000000L).toString());
    assertEquals("0001000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x1000000000000000L).toString());
    assertEquals("0010000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x2000000000000000L).toString());
    assertEquals("0100000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x4000000000000000L).toString());
    assertEquals("1000000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBin(0x8000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinTrimZeros(long)} method.
   */
  @Test
  public void test_appendBinTrimZeros_long() {
    // @formatter:off
    assertEquals(                                                               "0", new CharBuffer().appendBinTrimZeros(0x0000000000000000L).toString());
    assertEquals(                                                               "1", new CharBuffer().appendBinTrimZeros(0x0000000000000001L).toString());
    assertEquals(                                                              "10", new CharBuffer().appendBinTrimZeros(0x0000000000000002L).toString());
    assertEquals(                                                             "100", new CharBuffer().appendBinTrimZeros(0x0000000000000004L).toString());
    assertEquals(                                                            "1000", new CharBuffer().appendBinTrimZeros(0x0000000000000008L).toString());
    assertEquals(                                                           "10000", new CharBuffer().appendBinTrimZeros(0x0000000000000010L).toString());
    assertEquals(                                                          "100000", new CharBuffer().appendBinTrimZeros(0x0000000000000020L).toString());
    assertEquals(                                                         "1000000", new CharBuffer().appendBinTrimZeros(0x0000000000000040L).toString());
    assertEquals(                                                        "10000000", new CharBuffer().appendBinTrimZeros(0x0000000000000080L).toString());
    assertEquals(                                                       "100000000", new CharBuffer().appendBinTrimZeros(0x0000000000000100L).toString());
    assertEquals(                                                      "1000000000", new CharBuffer().appendBinTrimZeros(0x0000000000000200L).toString());
    assertEquals(                                                     "10000000000", new CharBuffer().appendBinTrimZeros(0x0000000000000400L).toString());
    assertEquals(                                                    "100000000000", new CharBuffer().appendBinTrimZeros(0x0000000000000800L).toString());
    assertEquals(                                                   "1000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000001000L).toString());
    assertEquals(                                                  "10000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000002000L).toString());
    assertEquals(                                                 "100000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000004000L).toString());
    assertEquals(                                                "1000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000008000L).toString());
    assertEquals(                                               "10000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000010000L).toString());
    assertEquals(                                              "100000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000020000L).toString());
    assertEquals(                                             "1000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000040000L).toString());
    assertEquals(                                            "10000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000080000L).toString());
    assertEquals(                                           "100000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000100000L).toString());
    assertEquals(                                          "1000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000200000L).toString());
    assertEquals(                                         "10000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000400000L).toString());
    assertEquals(                                        "100000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000000800000L).toString());
    assertEquals(                                       "1000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000001000000L).toString());
    assertEquals(                                      "10000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000002000000L).toString());
    assertEquals(                                     "100000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000004000000L).toString());
    assertEquals(                                    "1000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000008000000L).toString());
    assertEquals(                                   "10000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000010000000L).toString());
    assertEquals(                                  "100000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000020000000L).toString());
    assertEquals(                                 "1000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000040000000L).toString());
    assertEquals(                                "10000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000080000000L).toString());
    assertEquals(                               "100000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000100000000L).toString());
    assertEquals(                              "1000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000200000000L).toString());
    assertEquals(                             "10000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000400000000L).toString());
    assertEquals(                            "100000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000000800000000L).toString());
    assertEquals(                           "1000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000001000000000L).toString());
    assertEquals(                          "10000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000002000000000L).toString());
    assertEquals(                         "100000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000004000000000L).toString());
    assertEquals(                        "1000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000008000000000L).toString());
    assertEquals(                       "10000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000010000000000L).toString());
    assertEquals(                      "100000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000020000000000L).toString());
    assertEquals(                     "1000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000040000000000L).toString());
    assertEquals(                    "10000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000080000000000L).toString());
    assertEquals(                   "100000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000100000000000L).toString());
    assertEquals(                  "1000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000200000000000L).toString());
    assertEquals(                 "10000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000400000000000L).toString());
    assertEquals(                "100000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0000800000000000L).toString());
    assertEquals(               "1000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0001000000000000L).toString());
    assertEquals(              "10000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0002000000000000L).toString());
    assertEquals(             "100000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0004000000000000L).toString());
    assertEquals(            "1000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0008000000000000L).toString());
    assertEquals(           "10000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0010000000000000L).toString());
    assertEquals(          "100000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0020000000000000L).toString());
    assertEquals(         "1000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0040000000000000L).toString());
    assertEquals(        "10000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0080000000000000L).toString());
    assertEquals(       "100000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0100000000000000L).toString());
    assertEquals(      "1000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0200000000000000L).toString());
    assertEquals(     "10000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0400000000000000L).toString());
    assertEquals(    "100000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x0800000000000000L).toString());
    assertEquals(   "1000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x1000000000000000L).toString());
    assertEquals(  "10000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x2000000000000000L).toString());
    assertEquals( "100000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x4000000000000000L).toString());
    assertEquals("1000000000000000000000000000000000000000000000000000000000000000", new CharBuffer().appendBinTrimZeros(0x8000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(long)} method.
   */
  @Test
  public void test_getBinCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity(0x0000000000000000L));
    assertEquals( 1, CharBuffer.getBinCapacity(0x0000000000000001L));
    assertEquals( 2, CharBuffer.getBinCapacity(0x0000000000000002L));
    assertEquals( 3, CharBuffer.getBinCapacity(0x0000000000000004L));
    assertEquals( 4, CharBuffer.getBinCapacity(0x0000000000000008L));
    assertEquals( 5, CharBuffer.getBinCapacity(0x0000000000000010L));
    assertEquals( 6, CharBuffer.getBinCapacity(0x0000000000000020L));
    assertEquals( 7, CharBuffer.getBinCapacity(0x0000000000000040L));
    assertEquals( 8, CharBuffer.getBinCapacity(0x0000000000000080L));
    assertEquals( 9, CharBuffer.getBinCapacity(0x0000000000000100L));
    assertEquals(10, CharBuffer.getBinCapacity(0x0000000000000200L));
    assertEquals(11, CharBuffer.getBinCapacity(0x0000000000000400L));
    assertEquals(12, CharBuffer.getBinCapacity(0x0000000000000800L));
    assertEquals(13, CharBuffer.getBinCapacity(0x0000000000001000L));
    assertEquals(14, CharBuffer.getBinCapacity(0x0000000000002000L));
    assertEquals(15, CharBuffer.getBinCapacity(0x0000000000004000L));
    assertEquals(16, CharBuffer.getBinCapacity(0x0000000000008000L));
    assertEquals(17, CharBuffer.getBinCapacity(0x0000000000010000L));
    assertEquals(18, CharBuffer.getBinCapacity(0x0000000000020000L));
    assertEquals(19, CharBuffer.getBinCapacity(0x0000000000040000L));
    assertEquals(20, CharBuffer.getBinCapacity(0x0000000000080000L));
    assertEquals(21, CharBuffer.getBinCapacity(0x0000000000100000L));
    assertEquals(22, CharBuffer.getBinCapacity(0x0000000000200000L));
    assertEquals(23, CharBuffer.getBinCapacity(0x0000000000400000L));
    assertEquals(24, CharBuffer.getBinCapacity(0x0000000000800000L));
    assertEquals(25, CharBuffer.getBinCapacity(0x0000000001000000L));
    assertEquals(26, CharBuffer.getBinCapacity(0x0000000002000000L));
    assertEquals(27, CharBuffer.getBinCapacity(0x0000000004000000L));
    assertEquals(28, CharBuffer.getBinCapacity(0x0000000008000000L));
    assertEquals(29, CharBuffer.getBinCapacity(0x0000000010000000L));
    assertEquals(30, CharBuffer.getBinCapacity(0x0000000020000000L));
    assertEquals(31, CharBuffer.getBinCapacity(0x0000000040000000L));
    assertEquals(32, CharBuffer.getBinCapacity(0x0000000080000000L));
    assertEquals(33, CharBuffer.getBinCapacity(0x0000000100000000L));
    assertEquals(34, CharBuffer.getBinCapacity(0x0000000200000000L));
    assertEquals(35, CharBuffer.getBinCapacity(0x0000000400000000L));
    assertEquals(36, CharBuffer.getBinCapacity(0x0000000800000000L));
    assertEquals(37, CharBuffer.getBinCapacity(0x0000001000000000L));
    assertEquals(38, CharBuffer.getBinCapacity(0x0000002000000000L));
    assertEquals(39, CharBuffer.getBinCapacity(0x0000004000000000L));
    assertEquals(40, CharBuffer.getBinCapacity(0x0000008000000000L));
    assertEquals(41, CharBuffer.getBinCapacity(0x0000010000000000L));
    assertEquals(42, CharBuffer.getBinCapacity(0x0000020000000000L));
    assertEquals(43, CharBuffer.getBinCapacity(0x0000040000000000L));
    assertEquals(44, CharBuffer.getBinCapacity(0x0000080000000000L));
    assertEquals(45, CharBuffer.getBinCapacity(0x0000100000000000L));
    assertEquals(46, CharBuffer.getBinCapacity(0x0000200000000000L));
    assertEquals(47, CharBuffer.getBinCapacity(0x0000400000000000L));
    assertEquals(48, CharBuffer.getBinCapacity(0x0000800000000000L));
    assertEquals(49, CharBuffer.getBinCapacity(0x0001000000000000L));
    assertEquals(50, CharBuffer.getBinCapacity(0x0002000000000000L));
    assertEquals(51, CharBuffer.getBinCapacity(0x0004000000000000L));
    assertEquals(52, CharBuffer.getBinCapacity(0x0008000000000000L));
    assertEquals(53, CharBuffer.getBinCapacity(0x0010000000000000L));
    assertEquals(54, CharBuffer.getBinCapacity(0x0020000000000000L));
    assertEquals(55, CharBuffer.getBinCapacity(0x0040000000000000L));
    assertEquals(56, CharBuffer.getBinCapacity(0x0080000000000000L));
    assertEquals(57, CharBuffer.getBinCapacity(0x0100000000000000L));
    assertEquals(58, CharBuffer.getBinCapacity(0x0200000000000000L));
    assertEquals(59, CharBuffer.getBinCapacity(0x0400000000000000L));
    assertEquals(60, CharBuffer.getBinCapacity(0x0800000000000000L));
    assertEquals(61, CharBuffer.getBinCapacity(0x1000000000000000L));
    assertEquals(62, CharBuffer.getBinCapacity(0x2000000000000000L));
    assertEquals(63, CharBuffer.getBinCapacity(0x4000000000000000L));
    assertEquals(64, CharBuffer.getBinCapacity(0x8000000000000000L));
    // @formatter:on
  }

  // Advanced operations

  /**
   * Tests the {@link CharBuffer#appendIdent(int)} method.
   */
  @Test
  public void test_appendIdent_int() {
    assertThrows(IllegalArgumentException.class, () -> new CharBuffer().appendIndent(-1));
    assertEquals("", new CharBuffer().appendIndent(0).toString());
    assertEquals(" ", new CharBuffer().appendIndent(1).toString());
    assertEquals("  ", new CharBuffer().appendIndent(2).toString());
    assertEquals("   ", new CharBuffer().appendIndent(3).toString());
    assertEquals("    ", new CharBuffer().appendIndent(4).toString());
    assertEquals("     ", new CharBuffer().appendIndent(5).toString());
    assertEquals("      ", new CharBuffer().appendIndent(6).toString());
    assertEquals("       ", new CharBuffer().appendIndent(7).toString());
    assertEquals("        ", new CharBuffer().appendIndent(8).toString());
    assertEquals("         ", new CharBuffer().appendIndent(9).toString());
    assertEquals("          ", new CharBuffer().appendIndent(10).toString());
    assertEquals("           ", new CharBuffer().appendIndent(11).toString());
    assertEquals("            ", new CharBuffer().appendIndent(12).toString());
    assertEquals("             ", new CharBuffer().appendIndent(13).toString());
    assertEquals("              ", new CharBuffer().appendIndent(14).toString());
    assertEquals("               ", new CharBuffer().appendIndent(15).toString());
    assertEquals("                ", new CharBuffer().appendIndent(16).toString());
    assertEquals("                 ", new CharBuffer().appendIndent(17).toString());
    assertEquals("                  ", new CharBuffer().appendIndent(18).toString());
    assertEquals("                   ", new CharBuffer().appendIndent(19).toString());
    assertEquals("                    ", new CharBuffer().appendIndent(20).toString());
    assertEquals("                     ", new CharBuffer().appendIndent(21).toString());
    assertEquals("                      ", new CharBuffer().appendIndent(22).toString());
    assertEquals("                       ", new CharBuffer().appendIndent(23).toString());
    assertEquals("                        ", new CharBuffer().appendIndent(24).toString());
    assertEquals("                         ", new CharBuffer().appendIndent(25).toString());
    assertEquals("                          ", new CharBuffer().appendIndent(26).toString());
    assertEquals("                           ", new CharBuffer().appendIndent(27).toString());
    assertEquals("                            ", new CharBuffer().appendIndent(28).toString());
    assertEquals("                             ", new CharBuffer().appendIndent(29).toString());
    assertEquals("                              ", new CharBuffer().appendIndent(30).toString());
    assertEquals("                               ", new CharBuffer().appendIndent(31).toString());
    assertEquals("                                ", new CharBuffer().appendIndent(32).toString());
    assertEquals("                                 ", new CharBuffer().appendIndent(33).toString());
    assertEquals("                                  ", new CharBuffer().appendIndent(34).toString());
    assertEquals("                                   ", new CharBuffer().appendIndent(35).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendIdent(int, char)} method.
   */
  @Test
  public void test_appendIdent_int_char() {
    assertThrows(IllegalArgumentException.class, () -> new CharBuffer().appendIndent(-1, '_'));
    assertEquals("", new CharBuffer().appendIndent(0, '0').toString());
    assertEquals("1", new CharBuffer().appendIndent(1, '1').toString());
    assertEquals("22", new CharBuffer().appendIndent(2, '2').toString());
    assertEquals("333", new CharBuffer().appendIndent(3, '3').toString());
    assertEquals("4444", new CharBuffer().appendIndent(4, '4').toString());
    assertEquals("55555", new CharBuffer().appendIndent(5, '5').toString());
    assertEquals("666666", new CharBuffer().appendIndent(6, '6').toString());
    assertEquals("7777777", new CharBuffer().appendIndent(7, '7').toString());
    assertEquals("88888888", new CharBuffer().appendIndent(8, '8').toString());
    assertEquals("999999999", new CharBuffer().appendIndent(9, '9').toString());
    assertEquals("aaaaaaaaaa", new CharBuffer().appendIndent(10, 'a').toString());
    assertEquals("bbbbbbbbbbb", new CharBuffer().appendIndent(11, 'b').toString());
    assertEquals("cccccccccccc", new CharBuffer().appendIndent(12, 'c').toString());
    assertEquals("ddddddddddddd", new CharBuffer().appendIndent(13, 'd').toString());
    assertEquals("eeeeeeeeeeeeee", new CharBuffer().appendIndent(14, 'e').toString());
    assertEquals("fffffffffffffff", new CharBuffer().appendIndent(15, 'f').toString());
    assertEquals("gggggggggggggggg", new CharBuffer().appendIndent(16, 'g').toString());
    assertEquals("hhhhhhhhhhhhhhhhh", new CharBuffer().appendIndent(17, 'h').toString());
    assertEquals("iiiiiiiiiiiiiiiiii", new CharBuffer().appendIndent(18, 'i').toString());
    assertEquals("jjjjjjjjjjjjjjjjjjj", new CharBuffer().appendIndent(19, 'j').toString());
    assertEquals("kkkkkkkkkkkkkkkkkkkk", new CharBuffer().appendIndent(20, 'k').toString());
    assertEquals("lllllllllllllllllllll", new CharBuffer().appendIndent(21, 'l').toString());
    assertEquals("mmmmmmmmmmmmmmmmmmmmmm", new CharBuffer().appendIndent(22, 'm').toString());
    assertEquals("nnnnnnnnnnnnnnnnnnnnnnn", new CharBuffer().appendIndent(23, 'n').toString());
    assertEquals("oooooooooooooooooooooooo", new CharBuffer().appendIndent(24, 'o').toString());
    assertEquals("ppppppppppppppppppppppppp", new CharBuffer().appendIndent(25, 'p').toString());
    assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqq", new CharBuffer().appendIndent(26, 'q').toString());
    assertEquals("rrrrrrrrrrrrrrrrrrrrrrrrrrr", new CharBuffer().appendIndent(27, 'r').toString());
    assertEquals("ssssssssssssssssssssssssssss", new CharBuffer().appendIndent(28, 's').toString());
    assertEquals("ttttttttttttttttttttttttttttt", new CharBuffer().appendIndent(29, 't').toString());
    assertEquals("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuu", new CharBuffer().appendIndent(30, 'u').toString());
    assertEquals("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv", new CharBuffer().appendIndent(31, 'v').toString());
    assertEquals("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww", new CharBuffer().appendIndent(32, 'w').toString());
    assertEquals("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", new CharBuffer().appendIndent(33, 'x').toString());
    assertEquals("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy", new CharBuffer().appendIndent(34, 'y').toString());
    assertEquals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz", new CharBuffer().appendIndent(35, 'z').toString());
  }

  /**
   * Tests the {@link CharBuffer#appendIdent(int, int)} method.
   */
  @Test
  public void test_appendIdent_int_int() {
    final int ch = 0x2070e;
    // @formatter:off
    assertThrows(IllegalArgumentException.class, () -> new CharBuffer().appendIndent(-1, ch));
    assertEquals( 0, new CharBuffer().appendIndent( 0, ch).length());
    assertEquals( 2, new CharBuffer().appendIndent( 1, ch).length());
    assertEquals( 4, new CharBuffer().appendIndent( 2, ch).length());
    assertEquals( 6, new CharBuffer().appendIndent( 3, ch).length());
    assertEquals( 8, new CharBuffer().appendIndent( 4, ch).length());
    assertEquals(10, new CharBuffer().appendIndent( 5, ch).length());
    assertEquals(12, new CharBuffer().appendIndent( 6, ch).length());
    assertEquals(14, new CharBuffer().appendIndent( 7, ch).length());
    assertEquals(16, new CharBuffer().appendIndent( 8, ch).length());
    assertEquals(18, new CharBuffer().appendIndent( 9, ch).length());
    assertEquals(20, new CharBuffer().appendIndent(10, ch).length());
    assertEquals(22, new CharBuffer().appendIndent(11, ch).length());
    assertEquals(24, new CharBuffer().appendIndent(12, ch).length());
    assertEquals(26, new CharBuffer().appendIndent(13, ch).length());
    assertEquals(28, new CharBuffer().appendIndent(14, ch).length());
    assertEquals(30, new CharBuffer().appendIndent(15, ch).length());
    assertEquals(32, new CharBuffer().appendIndent(16, ch).length());
    assertEquals(34, new CharBuffer().appendIndent(17, ch).length());
    assertEquals(36, new CharBuffer().appendIndent(18, ch).length());
    assertEquals(38, new CharBuffer().appendIndent(19, ch).length());
    assertEquals(40, new CharBuffer().appendIndent(20, ch).length());
    assertEquals(42, new CharBuffer().appendIndent(21, ch).length());
    assertEquals(44, new CharBuffer().appendIndent(22, ch).length());
    assertEquals(46, new CharBuffer().appendIndent(23, ch).length());
    assertEquals(48, new CharBuffer().appendIndent(24, ch).length());
    assertEquals(50, new CharBuffer().appendIndent(25, ch).length());
    // @formatter:on
  }

}
