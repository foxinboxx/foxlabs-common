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
    assertEquals("0F", new CharBuffer().appendHex((byte) 0x0f).toString());
    assertEquals("FF", new CharBuffer().appendHex((byte) 0xff).toString());
    assertEquals("F0", new CharBuffer().appendHex((byte) 0xf0).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(byte)} method.
   */
  @Test
  public void test_appendHexTrimZeros_byte() {
    // @formatter:off
    assertEquals( "0", new CharBuffer().appendHexTrimZeros((byte) 0x00).toString());
    assertEquals( "F", new CharBuffer().appendHexTrimZeros((byte) 0x0f).toString());
    assertEquals("FF", new CharBuffer().appendHexTrimZeros((byte) 0xff).toString());
    assertEquals("F0", new CharBuffer().appendHexTrimZeros((byte) 0xf0).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHex(short)} method.
   */
  @Test
  public void test_appendHex_short() {
    // @formatter:off
    assertEquals("0000", new CharBuffer().appendHex((short) 0x0000).toString());
    assertEquals("000F", new CharBuffer().appendHex((short) 0x000f).toString());
    assertEquals("00FF", new CharBuffer().appendHex((short) 0x00ff).toString());
    assertEquals("0FFF", new CharBuffer().appendHex((short) 0x0fff).toString());
    assertEquals("FFFF", new CharBuffer().appendHex((short) 0xffff).toString());
    assertEquals("FFF0", new CharBuffer().appendHex((short) 0xfff0).toString());
    assertEquals("FF00", new CharBuffer().appendHex((short) 0xff00).toString());
    assertEquals("F000", new CharBuffer().appendHex((short) 0xf000).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(short)} method.
   */
  @Test
  public void test_appendHexTrimZeros_short() {
    // @formatter:off
    assertEquals(   "0", new CharBuffer().appendHexTrimZeros((short) 0x0000).toString());
    assertEquals(   "F", new CharBuffer().appendHexTrimZeros((short) 0x000f).toString());
    assertEquals(  "FF", new CharBuffer().appendHexTrimZeros((short) 0x00ff).toString());
    assertEquals( "FFF", new CharBuffer().appendHexTrimZeros((short) 0x0fff).toString());
    assertEquals("FFFF", new CharBuffer().appendHexTrimZeros((short) 0xffff).toString());
    assertEquals("FFF0", new CharBuffer().appendHexTrimZeros((short) 0xfff0).toString());
    assertEquals("FF00", new CharBuffer().appendHexTrimZeros((short) 0xff00).toString());
    assertEquals("F000", new CharBuffer().appendHexTrimZeros((short) 0xf000).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHex(int)} method.
   */
  @Test
  public void test_appendHex_int() {
    // @formatter:off
    assertEquals("00000000", new CharBuffer().appendHex(0x00000000).toString());
    assertEquals("0000000F", new CharBuffer().appendHex(0x0000000f).toString());
    assertEquals("000000FF", new CharBuffer().appendHex(0x000000ff).toString());
    assertEquals("00000FFF", new CharBuffer().appendHex(0x00000fff).toString());
    assertEquals("0000FFFF", new CharBuffer().appendHex(0x0000ffff).toString());
    assertEquals("000FFFFF", new CharBuffer().appendHex(0x000fffff).toString());
    assertEquals("00FFFFFF", new CharBuffer().appendHex(0x00ffffff).toString());
    assertEquals("0FFFFFFF", new CharBuffer().appendHex(0x0fffffff).toString());
    assertEquals("FFFFFFFF", new CharBuffer().appendHex(0xffffffff).toString());
    assertEquals("FFFFFFF0", new CharBuffer().appendHex(0xfffffff0).toString());
    assertEquals("FFFFFF00", new CharBuffer().appendHex(0xffffff00).toString());
    assertEquals("FFFFF000", new CharBuffer().appendHex(0xfffff000).toString());
    assertEquals("FFFF0000", new CharBuffer().appendHex(0xffff0000).toString());
    assertEquals("FFF00000", new CharBuffer().appendHex(0xfff00000).toString());
    assertEquals("FF000000", new CharBuffer().appendHex(0xff000000).toString());
    assertEquals("F0000000", new CharBuffer().appendHex(0xf0000000).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(int)} method.
   */
  @Test
  public void test_appendHexTrimZeros_int() {
    // @formatter:off
    assertEquals(       "0", new CharBuffer().appendHexTrimZeros(0x00000000).toString());
    assertEquals(       "F", new CharBuffer().appendHexTrimZeros(0x0000000f).toString());
    assertEquals(      "FF", new CharBuffer().appendHexTrimZeros(0x000000ff).toString());
    assertEquals(     "FFF", new CharBuffer().appendHexTrimZeros(0x00000fff).toString());
    assertEquals(    "FFFF", new CharBuffer().appendHexTrimZeros(0x0000ffff).toString());
    assertEquals(   "FFFFF", new CharBuffer().appendHexTrimZeros(0x000fffff).toString());
    assertEquals(  "FFFFFF", new CharBuffer().appendHexTrimZeros(0x00ffffff).toString());
    assertEquals( "FFFFFFF", new CharBuffer().appendHexTrimZeros(0x0fffffff).toString());
    assertEquals("FFFFFFFF", new CharBuffer().appendHexTrimZeros(0xffffffff).toString());
    assertEquals("FFFFFFF0", new CharBuffer().appendHexTrimZeros(0xfffffff0).toString());
    assertEquals("FFFFFFF0", new CharBuffer().appendHexTrimZeros(0xfffffff0).toString());
    assertEquals("FFFFFF00", new CharBuffer().appendHexTrimZeros(0xffffff00).toString());
    assertEquals("FFFFF000", new CharBuffer().appendHexTrimZeros(0xfffff000).toString());
    assertEquals("FFFF0000", new CharBuffer().appendHexTrimZeros(0xffff0000).toString());
    assertEquals("FFF00000", new CharBuffer().appendHexTrimZeros(0xfff00000).toString());
    assertEquals("FF000000", new CharBuffer().appendHexTrimZeros(0xff000000).toString());
    assertEquals("F0000000", new CharBuffer().appendHexTrimZeros(0xf0000000).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHex(long)} method.
   */
  @Test
  public void test_appendHex_long() {
    // @formatter:off
    assertEquals("0000000000000000", new CharBuffer().appendHex(0x0000000000000000L).toString());
    assertEquals("000000000000000F", new CharBuffer().appendHex(0x000000000000000fL).toString());
    assertEquals("00000000000000FF", new CharBuffer().appendHex(0x00000000000000ffL).toString());
    assertEquals("0000000000000FFF", new CharBuffer().appendHex(0x0000000000000fffL).toString());
    assertEquals("000000000000FFFF", new CharBuffer().appendHex(0x000000000000ffffL).toString());
    assertEquals("00000000000FFFFF", new CharBuffer().appendHex(0x00000000000fffffL).toString());
    assertEquals("0000000000FFFFFF", new CharBuffer().appendHex(0x0000000000ffffffL).toString());
    assertEquals("000000000FFFFFFF", new CharBuffer().appendHex(0x000000000fffffffL).toString());
    assertEquals("00000000FFFFFFFF", new CharBuffer().appendHex(0x00000000ffffffffL).toString());
    assertEquals("0000000FFFFFFFFF", new CharBuffer().appendHex(0x0000000fffffffffL).toString());
    assertEquals("000000FFFFFFFFFF", new CharBuffer().appendHex(0x000000ffffffffffL).toString());
    assertEquals("00000FFFFFFFFFFF", new CharBuffer().appendHex(0x00000fffffffffffL).toString());
    assertEquals("0000FFFFFFFFFFFF", new CharBuffer().appendHex(0x0000ffffffffffffL).toString());
    assertEquals("000FFFFFFFFFFFFF", new CharBuffer().appendHex(0x000fffffffffffffL).toString());
    assertEquals("00FFFFFFFFFFFFFF", new CharBuffer().appendHex(0x00ffffffffffffffL).toString());
    assertEquals("0FFFFFFFFFFFFFFF", new CharBuffer().appendHex(0x0fffffffffffffffL).toString());
    assertEquals("FFFFFFFFFFFFFFFF", new CharBuffer().appendHex(0xffffffffffffffffL).toString());
    assertEquals("FFFFFFFFFFFFFFF0", new CharBuffer().appendHex(0xfffffffffffffff0L).toString());
    assertEquals("FFFFFFFFFFFFFF00", new CharBuffer().appendHex(0xffffffffffffff00L).toString());
    assertEquals("FFFFFFFFFFFFF000", new CharBuffer().appendHex(0xfffffffffffff000L).toString());
    assertEquals("FFFFFFFFFFFF0000", new CharBuffer().appendHex(0xffffffffffff0000L).toString());
    assertEquals("FFFFFFFFFFF00000", new CharBuffer().appendHex(0xfffffffffff00000L).toString());
    assertEquals("FFFFFFFFFF000000", new CharBuffer().appendHex(0xffffffffff000000L).toString());
    assertEquals("FFFFFFFFF0000000", new CharBuffer().appendHex(0xfffffffff0000000L).toString());
    assertEquals("FFFFFFFF00000000", new CharBuffer().appendHex(0xffffffff00000000L).toString());
    assertEquals("FFFFFFF000000000", new CharBuffer().appendHex(0xfffffff000000000L).toString());
    assertEquals("FFFFFF0000000000", new CharBuffer().appendHex(0xffffff0000000000L).toString());
    assertEquals("FFFFF00000000000", new CharBuffer().appendHex(0xfffff00000000000L).toString());
    assertEquals("FFFF000000000000", new CharBuffer().appendHex(0xffff000000000000L).toString());
    assertEquals("FFF0000000000000", new CharBuffer().appendHex(0xfff0000000000000L).toString());
    assertEquals("FF00000000000000", new CharBuffer().appendHex(0xff00000000000000L).toString());
    assertEquals("F000000000000000", new CharBuffer().appendHex(0xf000000000000000L).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendHexTrimZeros(long)} method.
   */
  @Test
  public void test_appendHexTrimZeros_long() {
    // @formatter:off
    assertEquals(               "0", new CharBuffer().appendHexTrimZeros(0x0000000000000000L).toString());
    assertEquals(               "F", new CharBuffer().appendHexTrimZeros(0x000000000000000fL).toString());
    assertEquals(              "FF", new CharBuffer().appendHexTrimZeros(0x00000000000000ffL).toString());
    assertEquals(             "FFF", new CharBuffer().appendHexTrimZeros(0x0000000000000fffL).toString());
    assertEquals(            "FFFF", new CharBuffer().appendHexTrimZeros(0x000000000000ffffL).toString());
    assertEquals(           "FFFFF", new CharBuffer().appendHexTrimZeros(0x00000000000fffffL).toString());
    assertEquals(          "FFFFFF", new CharBuffer().appendHexTrimZeros(0x0000000000ffffffL).toString());
    assertEquals(         "FFFFFFF", new CharBuffer().appendHexTrimZeros(0x000000000fffffffL).toString());
    assertEquals(        "FFFFFFFF", new CharBuffer().appendHexTrimZeros(0x00000000ffffffffL).toString());
    assertEquals(       "FFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x0000000fffffffffL).toString());
    assertEquals(      "FFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x000000ffffffffffL).toString());
    assertEquals(     "FFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x00000fffffffffffL).toString());
    assertEquals(    "FFFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x0000ffffffffffffL).toString());
    assertEquals(   "FFFFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x000fffffffffffffL).toString());
    assertEquals(  "FFFFFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x00ffffffffffffffL).toString());
    assertEquals( "FFFFFFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0x0fffffffffffffffL).toString());
    assertEquals("FFFFFFFFFFFFFFFF", new CharBuffer().appendHexTrimZeros(0xffffffffffffffffL).toString());
    assertEquals("FFFFFFFFFFFFFFF0", new CharBuffer().appendHexTrimZeros(0xfffffffffffffff0L).toString());
    assertEquals("FFFFFFFFFFFFFF00", new CharBuffer().appendHexTrimZeros(0xffffffffffffff00L).toString());
    assertEquals("FFFFFFFFFFFFF000", new CharBuffer().appendHexTrimZeros(0xfffffffffffff000L).toString());
    assertEquals("FFFFFFFFFFFF0000", new CharBuffer().appendHexTrimZeros(0xffffffffffff0000L).toString());
    assertEquals("FFFFFFFFFFF00000", new CharBuffer().appendHexTrimZeros(0xfffffffffff00000L).toString());
    assertEquals("FFFFFFFFFF000000", new CharBuffer().appendHexTrimZeros(0xffffffffff000000L).toString());
    assertEquals("FFFFFFFFF0000000", new CharBuffer().appendHexTrimZeros(0xfffffffff0000000L).toString());
    assertEquals("FFFFFFFF00000000", new CharBuffer().appendHexTrimZeros(0xffffffff00000000L).toString());
    assertEquals("FFFFFFF000000000", new CharBuffer().appendHexTrimZeros(0xfffffff000000000L).toString());
    assertEquals("FFFFFF0000000000", new CharBuffer().appendHexTrimZeros(0xffffff0000000000L).toString());
    assertEquals("FFFFF00000000000", new CharBuffer().appendHexTrimZeros(0xfffff00000000000L).toString());
    assertEquals("FFFF000000000000", new CharBuffer().appendHexTrimZeros(0xffff000000000000L).toString());
    assertEquals("FFF0000000000000", new CharBuffer().appendHexTrimZeros(0xfff0000000000000L).toString());
    assertEquals("FF00000000000000", new CharBuffer().appendHexTrimZeros(0xff00000000000000L).toString());
    assertEquals("F000000000000000", new CharBuffer().appendHexTrimZeros(0xf000000000000000L).toString());
  }

}
