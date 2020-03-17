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

  // Number operations

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
  }

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
  }

}
