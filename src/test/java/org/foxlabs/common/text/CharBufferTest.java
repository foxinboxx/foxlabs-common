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
    assertEquals("true", new BigCharBuffer(4).appendBool(true).toString());
    assertEquals("false", new BigCharBuffer(5).appendBool(false).toString());
  }

  /**
   * Tests the {@link CharBuffer#getBoolCapacity(boolean)} method.
   */
  @Test
  public void test_getBoolCapacity() {
    assertEquals(4, CharBuffer.getBoolCapacity(true));
    assertEquals(5, CharBuffer.getBoolCapacity(false));
  }

  // Number to string representation

  // Decimal representation

  /**
   * Tests the {@link CharBuffer#appendDec(int)} method.
   */
  @Test
  public void test_appendDec_int() {
    // @formatter:off
    assertEquals(          "0", new BigCharBuffer( 1).appendDec(          0).toString());
    assertEquals(          "9", new BigCharBuffer( 1).appendDec(          9).toString());
    assertEquals(         "10", new BigCharBuffer( 2).appendDec(         10).toString());
    assertEquals(         "99", new BigCharBuffer( 2).appendDec(         99).toString());
    assertEquals(        "100", new BigCharBuffer( 3).appendDec(        100).toString());
    assertEquals(        "999", new BigCharBuffer( 3).appendDec(        999).toString());
    assertEquals(       "1000", new BigCharBuffer( 4).appendDec(       1000).toString());
    assertEquals(       "9999", new BigCharBuffer( 4).appendDec(       9999).toString());
    assertEquals(      "10000", new BigCharBuffer( 5).appendDec(      10000).toString());
    assertEquals(      "99999", new BigCharBuffer( 5).appendDec(      99999).toString());
    assertEquals(     "100000", new BigCharBuffer( 6).appendDec(     100000).toString());
    assertEquals(     "999999", new BigCharBuffer( 6).appendDec(     999999).toString());
    assertEquals(    "1000000", new BigCharBuffer( 7).appendDec(    1000000).toString());
    assertEquals(    "9999999", new BigCharBuffer( 7).appendDec(    9999999).toString());
    assertEquals(   "10000000", new BigCharBuffer( 8).appendDec(   10000000).toString());
    assertEquals(   "99999999", new BigCharBuffer( 8).appendDec(   99999999).toString());
    assertEquals(  "100000000", new BigCharBuffer( 9).appendDec(  100000000).toString());
    assertEquals(  "999999999", new BigCharBuffer( 9).appendDec(  999999999).toString());
    assertEquals( "1000000000", new BigCharBuffer(10).appendDec( 1000000000).toString());
    assertEquals( "2147483647", new BigCharBuffer(10).appendDec( 2147483647).toString());
    assertEquals("-2147483648", new BigCharBuffer(11).appendDec(-2147483648).toString());
    assertEquals("-1000000000", new BigCharBuffer(11).appendDec(-1000000000).toString());
    assertEquals( "-999999999", new BigCharBuffer(10).appendDec( -999999999).toString());
    assertEquals( "-100000000", new BigCharBuffer(10).appendDec( -100000000).toString());
    assertEquals(  "-99999999", new BigCharBuffer( 9).appendDec(  -99999999).toString());
    assertEquals(  "-10000000", new BigCharBuffer( 9).appendDec(  -10000000).toString());
    assertEquals(   "-9999999", new BigCharBuffer( 8).appendDec(   -9999999).toString());
    assertEquals(   "-1000000", new BigCharBuffer( 8).appendDec(   -1000000).toString());
    assertEquals(    "-999999", new BigCharBuffer( 7).appendDec(    -999999).toString());
    assertEquals(    "-100000", new BigCharBuffer( 7).appendDec(    -100000).toString());
    assertEquals(     "-99999", new BigCharBuffer( 6).appendDec(     -99999).toString());
    assertEquals(     "-10000", new BigCharBuffer( 6).appendDec(     -10000).toString());
    assertEquals(      "-9999", new BigCharBuffer( 5).appendDec(      -9999).toString());
    assertEquals(      "-1000", new BigCharBuffer( 5).appendDec(      -1000).toString());
    assertEquals(       "-999", new BigCharBuffer( 4).appendDec(       -999).toString());
    assertEquals(       "-100", new BigCharBuffer( 4).appendDec(       -100).toString());
    assertEquals(        "-99", new BigCharBuffer( 3).appendDec(        -99).toString());
    assertEquals(        "-10", new BigCharBuffer( 3).appendDec(        -10).toString());
    assertEquals(         "-9", new BigCharBuffer( 2).appendDec(         -9).toString());
    // @formatter:on
  }

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
   * Tests the {@link CharBuffer#appendDec(long)} method.
   */
  @Test
  public void test_appendDec_long() {
    // @formatter:off
    assertEquals(                   "0", new BigCharBuffer( 1).appendDec(                   0L).toString());
    assertEquals(                   "9", new BigCharBuffer( 1).appendDec(                   9L).toString());
    assertEquals(                  "10", new BigCharBuffer( 2).appendDec(                  10L).toString());
    assertEquals(                  "99", new BigCharBuffer( 2).appendDec(                  99L).toString());
    assertEquals(                 "100", new BigCharBuffer( 3).appendDec(                 100L).toString());
    assertEquals(                 "999", new BigCharBuffer( 3).appendDec(                 999L).toString());
    assertEquals(                "1000", new BigCharBuffer( 4).appendDec(                1000L).toString());
    assertEquals(                "9999", new BigCharBuffer( 4).appendDec(                9999L).toString());
    assertEquals(               "10000", new BigCharBuffer( 5).appendDec(               10000L).toString());
    assertEquals(               "99999", new BigCharBuffer( 5).appendDec(               99999L).toString());
    assertEquals(              "100000", new BigCharBuffer( 6).appendDec(              100000L).toString());
    assertEquals(              "999999", new BigCharBuffer( 6).appendDec(              999999L).toString());
    assertEquals(             "1000000", new BigCharBuffer( 7).appendDec(             1000000L).toString());
    assertEquals(             "9999999", new BigCharBuffer( 7).appendDec(             9999999L).toString());
    assertEquals(            "10000000", new BigCharBuffer( 8).appendDec(            10000000L).toString());
    assertEquals(            "99999999", new BigCharBuffer( 8).appendDec(            99999999L).toString());
    assertEquals(           "100000000", new BigCharBuffer( 9).appendDec(           100000000L).toString());
    assertEquals(           "999999999", new BigCharBuffer( 9).appendDec(           999999999L).toString());
    assertEquals(          "1000000000", new BigCharBuffer(10).appendDec(          1000000000L).toString());
    assertEquals(          "9999999999", new BigCharBuffer(10).appendDec(          9999999999L).toString());
    assertEquals(         "10000000000", new BigCharBuffer(11).appendDec(         10000000000L).toString());
    assertEquals(         "99999999999", new BigCharBuffer(11).appendDec(         99999999999L).toString());
    assertEquals(        "100000000000", new BigCharBuffer(12).appendDec(        100000000000L).toString());
    assertEquals(        "999999999999", new BigCharBuffer(12).appendDec(        999999999999L).toString());
    assertEquals(       "1000000000000", new BigCharBuffer(13).appendDec(       1000000000000L).toString());
    assertEquals(       "9999999999999", new BigCharBuffer(13).appendDec(       9999999999999L).toString());
    assertEquals(      "10000000000000", new BigCharBuffer(14).appendDec(      10000000000000L).toString());
    assertEquals(      "99999999999999", new BigCharBuffer(14).appendDec(      99999999999999L).toString());
    assertEquals(     "100000000000000", new BigCharBuffer(15).appendDec(     100000000000000L).toString());
    assertEquals(     "999999999999999", new BigCharBuffer(15).appendDec(     999999999999999L).toString());
    assertEquals(    "1000000000000000", new BigCharBuffer(16).appendDec(    1000000000000000L).toString());
    assertEquals(    "9999999999999999", new BigCharBuffer(16).appendDec(    9999999999999999L).toString());
    assertEquals(   "10000000000000000", new BigCharBuffer(17).appendDec(   10000000000000000L).toString());
    assertEquals(   "99999999999999999", new BigCharBuffer(17).appendDec(   99999999999999999L).toString());
    assertEquals(  "100000000000000000", new BigCharBuffer(18).appendDec(  100000000000000000L).toString());
    assertEquals(  "999999999999999999", new BigCharBuffer(18).appendDec(  999999999999999999L).toString());
    assertEquals( "1000000000000000000", new BigCharBuffer(19).appendDec( 1000000000000000000L).toString());
    assertEquals( "9223372036854775807", new BigCharBuffer(19).appendDec( 9223372036854775807L).toString());
    assertEquals("-9223372036854775808", new BigCharBuffer(20).appendDec(-9223372036854775808L).toString());
    assertEquals("-1000000000000000000", new BigCharBuffer(20).appendDec(-1000000000000000000L).toString());
    assertEquals( "-999999999999999999", new BigCharBuffer(19).appendDec( -999999999999999999L).toString());
    assertEquals( "-100000000000000000", new BigCharBuffer(19).appendDec( -100000000000000000L).toString());
    assertEquals(  "-99999999999999999", new BigCharBuffer(18).appendDec(  -99999999999999999L).toString());
    assertEquals(  "-10000000000000000", new BigCharBuffer(18).appendDec(  -10000000000000000L).toString());
    assertEquals(   "-9999999999999999", new BigCharBuffer(17).appendDec(   -9999999999999999L).toString());
    assertEquals(   "-1000000000000000", new BigCharBuffer(17).appendDec(   -1000000000000000L).toString());
    assertEquals(    "-999999999999999", new BigCharBuffer(16).appendDec(    -999999999999999L).toString());
    assertEquals(    "-100000000000000", new BigCharBuffer(16).appendDec(    -100000000000000L).toString());
    assertEquals(     "-99999999999999", new BigCharBuffer(15).appendDec(     -99999999999999L).toString());
    assertEquals(     "-10000000000000", new BigCharBuffer(15).appendDec(     -10000000000000L).toString());
    assertEquals(      "-9999999999999", new BigCharBuffer(14).appendDec(      -9999999999999L).toString());
    assertEquals(      "-1000000000000", new BigCharBuffer(14).appendDec(      -1000000000000L).toString());
    assertEquals(       "-999999999999", new BigCharBuffer(13).appendDec(       -999999999999L).toString());
    assertEquals(       "-100000000000", new BigCharBuffer(13).appendDec(       -100000000000L).toString());
    assertEquals(        "-99999999999", new BigCharBuffer(12).appendDec(        -99999999999L).toString());
    assertEquals(        "-10000000000", new BigCharBuffer(12).appendDec(        -10000000000L).toString());
    assertEquals(         "-9999999999", new BigCharBuffer(11).appendDec(         -9999999999L).toString());
    assertEquals(         "-1000000000", new BigCharBuffer(11).appendDec(         -1000000000L).toString());
    assertEquals(          "-999999999", new BigCharBuffer(10).appendDec(          -999999999L).toString());
    assertEquals(          "-100000000", new BigCharBuffer(10).appendDec(          -100000000L).toString());
    assertEquals(           "-99999999", new BigCharBuffer( 9).appendDec(           -99999999L).toString());
    assertEquals(           "-10000000", new BigCharBuffer( 9).appendDec(           -10000000L).toString());
    assertEquals(            "-9999999", new BigCharBuffer( 8).appendDec(            -9999999L).toString());
    assertEquals(            "-1000000", new BigCharBuffer( 8).appendDec(            -1000000L).toString());
    assertEquals(             "-999999", new BigCharBuffer( 7).appendDec(             -999999L).toString());
    assertEquals(             "-100000", new BigCharBuffer( 7).appendDec(             -100000L).toString());
    assertEquals(              "-99999", new BigCharBuffer( 6).appendDec(              -99999L).toString());
    assertEquals(              "-10000", new BigCharBuffer( 6).appendDec(              -10000L).toString());
    assertEquals(               "-9999", new BigCharBuffer( 5).appendDec(               -9999L).toString());
    assertEquals(               "-1000", new BigCharBuffer( 5).appendDec(               -1000L).toString());
    assertEquals(                "-999", new BigCharBuffer( 4).appendDec(                -999L).toString());
    assertEquals(                "-100", new BigCharBuffer( 4).appendDec(                -100L).toString());
    assertEquals(                 "-99", new BigCharBuffer( 3).appendDec(                 -99L).toString());
    assertEquals(                 "-10", new BigCharBuffer( 3).appendDec(                 -10L).toString());
    assertEquals(                  "-9", new BigCharBuffer( 2).appendDec(                  -9L).toString());
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

  // Hexadecimal representation

  /**
   * Tests the {@link CharBuffer#appendHex(byte)} method.
   */
  @Test
  public void test_appendHex_byte() {
    // @formatter:off
    assertEquals("00", new BigCharBuffer(2).appendHex((byte) 0x00).toString());
    assertEquals("0f", new BigCharBuffer(2).appendHex((byte) 0x0f).toString());
    assertEquals("ff", new BigCharBuffer(2).appendHex((byte) 0xff).toString());
    assertEquals("f0", new BigCharBuffer(2).appendHex((byte) 0xf0).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexStripLeadingZeros(byte)} method.
   */
  @Test
  public void test_appendHexTrimZeros_byte() {
    // @formatter:off
    assertEquals( "0", new BigCharBuffer(1).appendHexStripLeadingZeros((byte) 0x00).toString());
    assertEquals( "f", new BigCharBuffer(1).appendHexStripLeadingZeros((byte) 0x0f).toString());
    assertEquals("ff", new BigCharBuffer(2).appendHexStripLeadingZeros((byte) 0xff).toString());
    assertEquals("f0", new BigCharBuffer(2).appendHexStripLeadingZeros((byte) 0xf0).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(byte)} method.
   */
  @Test
  public void test_getHexCapacity_byte() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x00));
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x01));
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x02));
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x04));
    assertEquals(1, CharBuffer.getHexCapacity((byte) 0x08));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0x10));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0x20));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0x40));
    assertEquals(2, CharBuffer.getHexCapacity((byte) 0x80));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(short)} method.
   */
  @Test
  public void test_appendHex_short() {
    // @formatter:off
    assertEquals("0000", new BigCharBuffer(4).appendHex((short) 0x0000).toString());
    assertEquals("000f", new BigCharBuffer(4).appendHex((short) 0x000f).toString());
    assertEquals("00ff", new BigCharBuffer(4).appendHex((short) 0x00ff).toString());
    assertEquals("0fff", new BigCharBuffer(4).appendHex((short) 0x0fff).toString());
    assertEquals("ffff", new BigCharBuffer(4).appendHex((short) 0xffff).toString());
    assertEquals("fff0", new BigCharBuffer(4).appendHex((short) 0xfff0).toString());
    assertEquals("ff00", new BigCharBuffer(4).appendHex((short) 0xff00).toString());
    assertEquals("f000", new BigCharBuffer(4).appendHex((short) 0xf000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexStripLeadingZeros(short)} method.
   */
  @Test
  public void test_appendHexTrimZeros_short() {
    // @formatter:off
    assertEquals(   "0", new BigCharBuffer(1).appendHexStripLeadingZeros((short) 0x0000).toString());
    assertEquals(   "f", new BigCharBuffer(1).appendHexStripLeadingZeros((short) 0x000f).toString());
    assertEquals(  "ff", new BigCharBuffer(2).appendHexStripLeadingZeros((short) 0x00ff).toString());
    assertEquals( "fff", new BigCharBuffer(3).appendHexStripLeadingZeros((short) 0x0fff).toString());
    assertEquals("ffff", new BigCharBuffer(4).appendHexStripLeadingZeros((short) 0xffff).toString());
    assertEquals("fff0", new BigCharBuffer(4).appendHexStripLeadingZeros((short) 0xfff0).toString());
    assertEquals("ff00", new BigCharBuffer(4).appendHexStripLeadingZeros((short) 0xff00).toString());
    assertEquals("f000", new BigCharBuffer(4).appendHexStripLeadingZeros((short) 0xf000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(short)} method.
   */
  @Test
  public void test_getHexCapacity_short() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0000));
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0001));
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0002));
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0004));
    assertEquals(1, CharBuffer.getHexCapacity((short) 0x0008));
    assertEquals(2, CharBuffer.getHexCapacity((short) 0x0010));
    assertEquals(2, CharBuffer.getHexCapacity((short) 0x0020));
    assertEquals(2, CharBuffer.getHexCapacity((short) 0x0040));
    assertEquals(2, CharBuffer.getHexCapacity((short) 0x0080));
    assertEquals(3, CharBuffer.getHexCapacity((short) 0x0100));
    assertEquals(3, CharBuffer.getHexCapacity((short) 0x0200));
    assertEquals(3, CharBuffer.getHexCapacity((short) 0x0400));
    assertEquals(3, CharBuffer.getHexCapacity((short) 0x0800));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0x1000));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0x2000));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0x4000));
    assertEquals(4, CharBuffer.getHexCapacity((short) 0x8000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(int)} method.
   */
  @Test
  public void test_appendHex_int() {
    // @formatter:off
    assertEquals("00000000", new BigCharBuffer(8).appendHex(0x00000000).toString());
    assertEquals("0000000f", new BigCharBuffer(8).appendHex(0x0000000f).toString());
    assertEquals("000000ff", new BigCharBuffer(8).appendHex(0x000000ff).toString());
    assertEquals("00000fff", new BigCharBuffer(8).appendHex(0x00000fff).toString());
    assertEquals("0000ffff", new BigCharBuffer(8).appendHex(0x0000ffff).toString());
    assertEquals("000fffff", new BigCharBuffer(8).appendHex(0x000fffff).toString());
    assertEquals("00ffffff", new BigCharBuffer(8).appendHex(0x00ffffff).toString());
    assertEquals("0fffffff", new BigCharBuffer(8).appendHex(0x0fffffff).toString());
    assertEquals("ffffffff", new BigCharBuffer(8).appendHex(0xffffffff).toString());
    assertEquals("fffffff0", new BigCharBuffer(8).appendHex(0xfffffff0).toString());
    assertEquals("ffffff00", new BigCharBuffer(8).appendHex(0xffffff00).toString());
    assertEquals("fffff000", new BigCharBuffer(8).appendHex(0xfffff000).toString());
    assertEquals("ffff0000", new BigCharBuffer(8).appendHex(0xffff0000).toString());
    assertEquals("fff00000", new BigCharBuffer(8).appendHex(0xfff00000).toString());
    assertEquals("ff000000", new BigCharBuffer(8).appendHex(0xff000000).toString());
    assertEquals("f0000000", new BigCharBuffer(8).appendHex(0xf0000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexStripLeadingZeros(int)} method.
   */
  @Test
  public void test_appendHexTrimZeros_int() {
    // @formatter:off
    assertEquals(       "0", new BigCharBuffer(1).appendHexStripLeadingZeros(0x00000000).toString());
    assertEquals(       "f", new BigCharBuffer(1).appendHexStripLeadingZeros(0x0000000f).toString());
    assertEquals(      "ff", new BigCharBuffer(2).appendHexStripLeadingZeros(0x000000ff).toString());
    assertEquals(     "fff", new BigCharBuffer(3).appendHexStripLeadingZeros(0x00000fff).toString());
    assertEquals(    "ffff", new BigCharBuffer(4).appendHexStripLeadingZeros(0x0000ffff).toString());
    assertEquals(   "fffff", new BigCharBuffer(5).appendHexStripLeadingZeros(0x000fffff).toString());
    assertEquals(  "ffffff", new BigCharBuffer(6).appendHexStripLeadingZeros(0x00ffffff).toString());
    assertEquals( "fffffff", new BigCharBuffer(7).appendHexStripLeadingZeros(0x0fffffff).toString());
    assertEquals("ffffffff", new BigCharBuffer(8).appendHexStripLeadingZeros(0xffffffff).toString());
    assertEquals("fffffff0", new BigCharBuffer(8).appendHexStripLeadingZeros(0xfffffff0).toString());
    assertEquals("fffffff0", new BigCharBuffer(8).appendHexStripLeadingZeros(0xfffffff0).toString());
    assertEquals("ffffff00", new BigCharBuffer(8).appendHexStripLeadingZeros(0xffffff00).toString());
    assertEquals("fffff000", new BigCharBuffer(8).appendHexStripLeadingZeros(0xfffff000).toString());
    assertEquals("ffff0000", new BigCharBuffer(8).appendHexStripLeadingZeros(0xffff0000).toString());
    assertEquals("fff00000", new BigCharBuffer(8).appendHexStripLeadingZeros(0xfff00000).toString());
    assertEquals("ff000000", new BigCharBuffer(8).appendHexStripLeadingZeros(0xff000000).toString());
    assertEquals("f0000000", new BigCharBuffer(8).appendHexStripLeadingZeros(0xf0000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(int)} method.
   */
  @Test
  public void test_getHexCapacity_int() {
    // @formatter:off
    assertEquals(1, CharBuffer.getHexCapacity(0x00000000));
    assertEquals(1, CharBuffer.getHexCapacity(0x00000001));
    assertEquals(1, CharBuffer.getHexCapacity(0x00000002));
    assertEquals(1, CharBuffer.getHexCapacity(0x00000004));
    assertEquals(1, CharBuffer.getHexCapacity(0x00000008));
    assertEquals(2, CharBuffer.getHexCapacity(0x00000010));
    assertEquals(2, CharBuffer.getHexCapacity(0x00000020));
    assertEquals(2, CharBuffer.getHexCapacity(0x00000040));
    assertEquals(2, CharBuffer.getHexCapacity(0x00000080));
    assertEquals(3, CharBuffer.getHexCapacity(0x00000100));
    assertEquals(3, CharBuffer.getHexCapacity(0x00000200));
    assertEquals(3, CharBuffer.getHexCapacity(0x00000400));
    assertEquals(3, CharBuffer.getHexCapacity(0x00000800));
    assertEquals(4, CharBuffer.getHexCapacity(0x00001000));
    assertEquals(4, CharBuffer.getHexCapacity(0x00002000));
    assertEquals(4, CharBuffer.getHexCapacity(0x00004000));
    assertEquals(4, CharBuffer.getHexCapacity(0x00008000));
    assertEquals(5, CharBuffer.getHexCapacity(0x00010000));
    assertEquals(5, CharBuffer.getHexCapacity(0x00020000));
    assertEquals(5, CharBuffer.getHexCapacity(0x00040000));
    assertEquals(5, CharBuffer.getHexCapacity(0x00080000));
    assertEquals(6, CharBuffer.getHexCapacity(0x00100000));
    assertEquals(6, CharBuffer.getHexCapacity(0x00200000));
    assertEquals(6, CharBuffer.getHexCapacity(0x00400000));
    assertEquals(6, CharBuffer.getHexCapacity(0x00800000));
    assertEquals(7, CharBuffer.getHexCapacity(0x01000000));
    assertEquals(7, CharBuffer.getHexCapacity(0x02000000));
    assertEquals(7, CharBuffer.getHexCapacity(0x04000000));
    assertEquals(7, CharBuffer.getHexCapacity(0x08000000));
    assertEquals(8, CharBuffer.getHexCapacity(0x10000000));
    assertEquals(8, CharBuffer.getHexCapacity(0x20000000));
    assertEquals(8, CharBuffer.getHexCapacity(0x40000000));
    assertEquals(8, CharBuffer.getHexCapacity(0x80000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHex(long)} method.
   */
  @Test
  public void test_appendHex_long() {
    // @formatter:off
    assertEquals("0000000000000000", new BigCharBuffer(16).appendHex(0x0000000000000000L).toString());
    assertEquals("000000000000000f", new BigCharBuffer(16).appendHex(0x000000000000000fL).toString());
    assertEquals("00000000000000ff", new BigCharBuffer(16).appendHex(0x00000000000000ffL).toString());
    assertEquals("0000000000000fff", new BigCharBuffer(16).appendHex(0x0000000000000fffL).toString());
    assertEquals("000000000000ffff", new BigCharBuffer(16).appendHex(0x000000000000ffffL).toString());
    assertEquals("00000000000fffff", new BigCharBuffer(16).appendHex(0x00000000000fffffL).toString());
    assertEquals("0000000000ffffff", new BigCharBuffer(16).appendHex(0x0000000000ffffffL).toString());
    assertEquals("000000000fffffff", new BigCharBuffer(16).appendHex(0x000000000fffffffL).toString());
    assertEquals("00000000ffffffff", new BigCharBuffer(16).appendHex(0x00000000ffffffffL).toString());
    assertEquals("0000000fffffffff", new BigCharBuffer(16).appendHex(0x0000000fffffffffL).toString());
    assertEquals("000000ffffffffff", new BigCharBuffer(16).appendHex(0x000000ffffffffffL).toString());
    assertEquals("00000fffffffffff", new BigCharBuffer(16).appendHex(0x00000fffffffffffL).toString());
    assertEquals("0000ffffffffffff", new BigCharBuffer(16).appendHex(0x0000ffffffffffffL).toString());
    assertEquals("000fffffffffffff", new BigCharBuffer(16).appendHex(0x000fffffffffffffL).toString());
    assertEquals("00ffffffffffffff", new BigCharBuffer(16).appendHex(0x00ffffffffffffffL).toString());
    assertEquals("0fffffffffffffff", new BigCharBuffer(16).appendHex(0x0fffffffffffffffL).toString());
    assertEquals("ffffffffffffffff", new BigCharBuffer(16).appendHex(0xffffffffffffffffL).toString());
    assertEquals("fffffffffffffff0", new BigCharBuffer(16).appendHex(0xfffffffffffffff0L).toString());
    assertEquals("ffffffffffffff00", new BigCharBuffer(16).appendHex(0xffffffffffffff00L).toString());
    assertEquals("fffffffffffff000", new BigCharBuffer(16).appendHex(0xfffffffffffff000L).toString());
    assertEquals("ffffffffffff0000", new BigCharBuffer(16).appendHex(0xffffffffffff0000L).toString());
    assertEquals("fffffffffff00000", new BigCharBuffer(16).appendHex(0xfffffffffff00000L).toString());
    assertEquals("ffffffffff000000", new BigCharBuffer(16).appendHex(0xffffffffff000000L).toString());
    assertEquals("fffffffff0000000", new BigCharBuffer(16).appendHex(0xfffffffff0000000L).toString());
    assertEquals("ffffffff00000000", new BigCharBuffer(16).appendHex(0xffffffff00000000L).toString());
    assertEquals("fffffff000000000", new BigCharBuffer(16).appendHex(0xfffffff000000000L).toString());
    assertEquals("ffffff0000000000", new BigCharBuffer(16).appendHex(0xffffff0000000000L).toString());
    assertEquals("fffff00000000000", new BigCharBuffer(16).appendHex(0xfffff00000000000L).toString());
    assertEquals("ffff000000000000", new BigCharBuffer(16).appendHex(0xffff000000000000L).toString());
    assertEquals("fff0000000000000", new BigCharBuffer(16).appendHex(0xfff0000000000000L).toString());
    assertEquals("ff00000000000000", new BigCharBuffer(16).appendHex(0xff00000000000000L).toString());
    assertEquals("f000000000000000", new BigCharBuffer(16).appendHex(0xf000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendHexStripLeadingZeros(long)} method.
   */
  @Test
  public void test_appendHexTrimZeros_long() {
    // @formatter:off
    assertEquals(               "0", new BigCharBuffer( 1).appendHexStripLeadingZeros(0x0000000000000000L).toString());
    assertEquals(               "f", new BigCharBuffer( 1).appendHexStripLeadingZeros(0x000000000000000fL).toString());
    assertEquals(              "ff", new BigCharBuffer( 2).appendHexStripLeadingZeros(0x00000000000000ffL).toString());
    assertEquals(             "fff", new BigCharBuffer( 3).appendHexStripLeadingZeros(0x0000000000000fffL).toString());
    assertEquals(            "ffff", new BigCharBuffer( 4).appendHexStripLeadingZeros(0x000000000000ffffL).toString());
    assertEquals(           "fffff", new BigCharBuffer( 5).appendHexStripLeadingZeros(0x00000000000fffffL).toString());
    assertEquals(          "ffffff", new BigCharBuffer( 6).appendHexStripLeadingZeros(0x0000000000ffffffL).toString());
    assertEquals(         "fffffff", new BigCharBuffer( 7).appendHexStripLeadingZeros(0x000000000fffffffL).toString());
    assertEquals(        "ffffffff", new BigCharBuffer( 8).appendHexStripLeadingZeros(0x00000000ffffffffL).toString());
    assertEquals(       "fffffffff", new BigCharBuffer( 9).appendHexStripLeadingZeros(0x0000000fffffffffL).toString());
    assertEquals(      "ffffffffff", new BigCharBuffer(10).appendHexStripLeadingZeros(0x000000ffffffffffL).toString());
    assertEquals(     "fffffffffff", new BigCharBuffer(11).appendHexStripLeadingZeros(0x00000fffffffffffL).toString());
    assertEquals(    "ffffffffffff", new BigCharBuffer(12).appendHexStripLeadingZeros(0x0000ffffffffffffL).toString());
    assertEquals(   "fffffffffffff", new BigCharBuffer(13).appendHexStripLeadingZeros(0x000fffffffffffffL).toString());
    assertEquals(  "ffffffffffffff", new BigCharBuffer(14).appendHexStripLeadingZeros(0x00ffffffffffffffL).toString());
    assertEquals( "fffffffffffffff", new BigCharBuffer(15).appendHexStripLeadingZeros(0x0fffffffffffffffL).toString());
    assertEquals("ffffffffffffffff", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffffffffffffffL).toString());
    assertEquals("fffffffffffffff0", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffffffffffffff0L).toString());
    assertEquals("ffffffffffffff00", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffffffffffff00L).toString());
    assertEquals("fffffffffffff000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffffffffffff000L).toString());
    assertEquals("ffffffffffff0000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffffffffff0000L).toString());
    assertEquals("fffffffffff00000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffffffffff00000L).toString());
    assertEquals("ffffffffff000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffffffff000000L).toString());
    assertEquals("fffffffff0000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffffffff0000000L).toString());
    assertEquals("ffffffff00000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffffff00000000L).toString());
    assertEquals("fffffff000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffffff000000000L).toString());
    assertEquals("ffffff0000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffffff0000000000L).toString());
    assertEquals("fffff00000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfffff00000000000L).toString());
    assertEquals("ffff000000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xffff000000000000L).toString());
    assertEquals("fff0000000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xfff0000000000000L).toString());
    assertEquals("ff00000000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xff00000000000000L).toString());
    assertEquals("f000000000000000", new BigCharBuffer(16).appendHexStripLeadingZeros(0xf000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getHexCapacity(long)} method.
   */
  @Test
  public void test_getHexCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000000L));
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000001L));
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000002L));
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000004L));
    assertEquals( 1, CharBuffer.getHexCapacity(0x0000000000000008L));
    assertEquals( 2, CharBuffer.getHexCapacity(0x0000000000000010L));
    assertEquals( 2, CharBuffer.getHexCapacity(0x0000000000000020L));
    assertEquals( 2, CharBuffer.getHexCapacity(0x0000000000000040L));
    assertEquals( 2, CharBuffer.getHexCapacity(0x0000000000000080L));
    assertEquals( 3, CharBuffer.getHexCapacity(0x0000000000000100L));
    assertEquals( 3, CharBuffer.getHexCapacity(0x0000000000000200L));
    assertEquals( 3, CharBuffer.getHexCapacity(0x0000000000000400L));
    assertEquals( 3, CharBuffer.getHexCapacity(0x0000000000000800L));
    assertEquals( 4, CharBuffer.getHexCapacity(0x0000000000001000L));
    assertEquals( 4, CharBuffer.getHexCapacity(0x0000000000002000L));
    assertEquals( 4, CharBuffer.getHexCapacity(0x0000000000004000L));
    assertEquals( 4, CharBuffer.getHexCapacity(0x0000000000008000L));
    assertEquals( 5, CharBuffer.getHexCapacity(0x0000000000010000L));
    assertEquals( 5, CharBuffer.getHexCapacity(0x0000000000020000L));
    assertEquals( 5, CharBuffer.getHexCapacity(0x0000000000040000L));
    assertEquals( 5, CharBuffer.getHexCapacity(0x0000000000080000L));
    assertEquals( 6, CharBuffer.getHexCapacity(0x0000000000100000L));
    assertEquals( 6, CharBuffer.getHexCapacity(0x0000000000200000L));
    assertEquals( 6, CharBuffer.getHexCapacity(0x0000000000400000L));
    assertEquals( 6, CharBuffer.getHexCapacity(0x0000000000800000L));
    assertEquals( 7, CharBuffer.getHexCapacity(0x0000000001000000L));
    assertEquals( 7, CharBuffer.getHexCapacity(0x0000000002000000L));
    assertEquals( 7, CharBuffer.getHexCapacity(0x0000000004000000L));
    assertEquals( 7, CharBuffer.getHexCapacity(0x0000000008000000L));
    assertEquals( 8, CharBuffer.getHexCapacity(0x0000000010000000L));
    assertEquals( 8, CharBuffer.getHexCapacity(0x0000000020000000L));
    assertEquals( 8, CharBuffer.getHexCapacity(0x0000000040000000L));
    assertEquals( 8, CharBuffer.getHexCapacity(0x0000000080000000L));
    assertEquals( 9, CharBuffer.getHexCapacity(0x0000000100000000L));
    assertEquals( 9, CharBuffer.getHexCapacity(0x0000000200000000L));
    assertEquals( 9, CharBuffer.getHexCapacity(0x0000000400000000L));
    assertEquals( 9, CharBuffer.getHexCapacity(0x0000000800000000L));
    assertEquals(10, CharBuffer.getHexCapacity(0x0000001000000000L));
    assertEquals(10, CharBuffer.getHexCapacity(0x0000002000000000L));
    assertEquals(10, CharBuffer.getHexCapacity(0x0000004000000000L));
    assertEquals(10, CharBuffer.getHexCapacity(0x0000008000000000L));
    assertEquals(11, CharBuffer.getHexCapacity(0x0000010000000000L));
    assertEquals(11, CharBuffer.getHexCapacity(0x0000020000000000L));
    assertEquals(11, CharBuffer.getHexCapacity(0x0000040000000000L));
    assertEquals(11, CharBuffer.getHexCapacity(0x0000080000000000L));
    assertEquals(12, CharBuffer.getHexCapacity(0x0000100000000000L));
    assertEquals(12, CharBuffer.getHexCapacity(0x0000200000000000L));
    assertEquals(12, CharBuffer.getHexCapacity(0x0000400000000000L));
    assertEquals(12, CharBuffer.getHexCapacity(0x0000800000000000L));
    assertEquals(13, CharBuffer.getHexCapacity(0x0001000000000000L));
    assertEquals(13, CharBuffer.getHexCapacity(0x0002000000000000L));
    assertEquals(13, CharBuffer.getHexCapacity(0x0004000000000000L));
    assertEquals(13, CharBuffer.getHexCapacity(0x0008000000000000L));
    assertEquals(14, CharBuffer.getHexCapacity(0x0010000000000000L));
    assertEquals(14, CharBuffer.getHexCapacity(0x0020000000000000L));
    assertEquals(14, CharBuffer.getHexCapacity(0x0040000000000000L));
    assertEquals(14, CharBuffer.getHexCapacity(0x0080000000000000L));
    assertEquals(15, CharBuffer.getHexCapacity(0x0100000000000000L));
    assertEquals(15, CharBuffer.getHexCapacity(0x0200000000000000L));
    assertEquals(15, CharBuffer.getHexCapacity(0x0400000000000000L));
    assertEquals(15, CharBuffer.getHexCapacity(0x0800000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0x1000000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0x2000000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0x4000000000000000L));
    assertEquals(16, CharBuffer.getHexCapacity(0x8000000000000000L));
    // @formatter:on
  }

  // Octal representation

  /**
   * Tests the {@link CharBuffer#appendOct(byte)} method.
   */
  @Test
  public void test_appendOct_byte() {
    // @formatter:off
    assertEquals("000", new BigCharBuffer(3).appendOct((byte) 0000).toString());
    assertEquals("007", new BigCharBuffer(3).appendOct((byte) 0007).toString());
    assertEquals("077", new BigCharBuffer(3).appendOct((byte) 0077).toString());
    assertEquals("377", new BigCharBuffer(3).appendOct((byte) 0377).toString());
    assertEquals("370", new BigCharBuffer(3).appendOct((byte) 0370).toString());
    assertEquals("300", new BigCharBuffer(3).appendOct((byte) 0300).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOctStripLeadingZeros(byte)} method.
   */
  @Test
  public void test_appendOctTrimZeros_byte() {
    // @formatter:off
    assertEquals(  "0", new BigCharBuffer(1).appendOctStripLeadingZeros((byte) 0000).toString());
    assertEquals(  "7", new BigCharBuffer(1).appendOctStripLeadingZeros((byte) 0007).toString());
    assertEquals( "77", new BigCharBuffer(2).appendOctStripLeadingZeros((byte) 0077).toString());
    assertEquals("377", new BigCharBuffer(3).appendOctStripLeadingZeros((byte) 0377).toString());
    assertEquals("370", new BigCharBuffer(3).appendOctStripLeadingZeros((byte) 0370).toString());
    assertEquals("300", new BigCharBuffer(3).appendOctStripLeadingZeros((byte) 0300).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getOctCapacity(byte)} method.
   */
  @Test
  public void test_getOctCapacity_byte() {
    // @formatter:off
    assertEquals(1, CharBuffer.getOctCapacity((byte) 0000));
    assertEquals(1, CharBuffer.getOctCapacity((byte) 0001));
    assertEquals(1, CharBuffer.getOctCapacity((byte) 0002));
    assertEquals(1, CharBuffer.getOctCapacity((byte) 0004));
    assertEquals(2, CharBuffer.getOctCapacity((byte) 0010));
    assertEquals(2, CharBuffer.getOctCapacity((byte) 0020));
    assertEquals(2, CharBuffer.getOctCapacity((byte) 0040));
    assertEquals(3, CharBuffer.getOctCapacity((byte) 0100));
    assertEquals(3, CharBuffer.getOctCapacity((byte) 0200));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOct(short)} method.
   */
  @Test
  public void test_appendOct_short() {
    // @formatter:off
    assertEquals("000000", new BigCharBuffer(6).appendOct((short) 0000000).toString());
    assertEquals("000007", new BigCharBuffer(6).appendOct((short) 0000007).toString());
    assertEquals("000077", new BigCharBuffer(6).appendOct((short) 0000077).toString());
    assertEquals("000777", new BigCharBuffer(6).appendOct((short) 0000777).toString());
    assertEquals("007777", new BigCharBuffer(6).appendOct((short) 0007777).toString());
    assertEquals("077777", new BigCharBuffer(6).appendOct((short) 0077777).toString());
    assertEquals("177777", new BigCharBuffer(6).appendOct((short) 0177777).toString());
    assertEquals("177770", new BigCharBuffer(6).appendOct((short) 0177770).toString());
    assertEquals("177700", new BigCharBuffer(6).appendOct((short) 0177700).toString());
    assertEquals("177000", new BigCharBuffer(6).appendOct((short) 0177000).toString());
    assertEquals("170000", new BigCharBuffer(6).appendOct((short) 0170000).toString());
    assertEquals("100000", new BigCharBuffer(6).appendOct((short) 0100000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOctStripLeadingZeros(short)} method.
   */
  @Test
  public void test_appendOctTrimZeros_short() {
    // @formatter:off
    assertEquals(     "0", new BigCharBuffer(1).appendOctStripLeadingZeros((short) 0000000).toString());
    assertEquals(     "7", new BigCharBuffer(1).appendOctStripLeadingZeros((short) 0000007).toString());
    assertEquals(    "77", new BigCharBuffer(2).appendOctStripLeadingZeros((short) 0000077).toString());
    assertEquals(   "777", new BigCharBuffer(3).appendOctStripLeadingZeros((short) 0000777).toString());
    assertEquals(  "7777", new BigCharBuffer(4).appendOctStripLeadingZeros((short) 0007777).toString());
    assertEquals( "77777", new BigCharBuffer(5).appendOctStripLeadingZeros((short) 0077777).toString());
    assertEquals("177777", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0177777).toString());
    assertEquals("177770", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0177770).toString());
    assertEquals("177700", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0177700).toString());
    assertEquals("177000", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0177000).toString());
    assertEquals("170000", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0170000).toString());
    assertEquals("100000", new BigCharBuffer(6).appendOctStripLeadingZeros((short) 0100000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getOctCapacity(short)} method.
   */
  @Test
  public void test_getOctCapacity_short() {
    // @formatter:off
    assertEquals(1, CharBuffer.getOctCapacity((short) 0000000));
    assertEquals(1, CharBuffer.getOctCapacity((short) 0000001));
    assertEquals(1, CharBuffer.getOctCapacity((short) 0000002));
    assertEquals(1, CharBuffer.getOctCapacity((short) 0000004));
    assertEquals(2, CharBuffer.getOctCapacity((short) 0000010));
    assertEquals(2, CharBuffer.getOctCapacity((short) 0000020));
    assertEquals(2, CharBuffer.getOctCapacity((short) 0000040));
    assertEquals(3, CharBuffer.getOctCapacity((short) 0000100));
    assertEquals(3, CharBuffer.getOctCapacity((short) 0000200));
    assertEquals(3, CharBuffer.getOctCapacity((short) 0000400));
    assertEquals(4, CharBuffer.getOctCapacity((short) 0001000));
    assertEquals(4, CharBuffer.getOctCapacity((short) 0002000));
    assertEquals(4, CharBuffer.getOctCapacity((short) 0004000));
    assertEquals(5, CharBuffer.getOctCapacity((short) 0010000));
    assertEquals(5, CharBuffer.getOctCapacity((short) 0020000));
    assertEquals(5, CharBuffer.getOctCapacity((short) 0040000));
    assertEquals(6, CharBuffer.getOctCapacity((short) 0100000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOct(int)} method.
   */
  @Test
  public void test_appendOct_int() {
    // @formatter:off
    assertEquals("00000000000", new BigCharBuffer(11).appendOct(000000000000).toString());
    assertEquals("00000000007", new BigCharBuffer(11).appendOct(000000000007).toString());
    assertEquals("00000000077", new BigCharBuffer(11).appendOct(000000000077).toString());
    assertEquals("00000000777", new BigCharBuffer(11).appendOct(000000000777).toString());
    assertEquals("00000007777", new BigCharBuffer(11).appendOct(000000007777).toString());
    assertEquals("00000077777", new BigCharBuffer(11).appendOct(000000077777).toString());
    assertEquals("00000777777", new BigCharBuffer(11).appendOct(000000777777).toString());
    assertEquals("00007777777", new BigCharBuffer(11).appendOct(000007777777).toString());
    assertEquals("00077777777", new BigCharBuffer(11).appendOct(000077777777).toString());
    assertEquals("00777777777", new BigCharBuffer(11).appendOct(000777777777).toString());
    assertEquals("07777777777", new BigCharBuffer(11).appendOct(007777777777).toString());
    assertEquals("37777777777", new BigCharBuffer(11).appendOct(037777777777).toString());
    assertEquals("37777777770", new BigCharBuffer(11).appendOct(037777777770).toString());
    assertEquals("37777777700", new BigCharBuffer(11).appendOct(037777777700).toString());
    assertEquals("37777777000", new BigCharBuffer(11).appendOct(037777777000).toString());
    assertEquals("37777770000", new BigCharBuffer(11).appendOct(037777770000).toString());
    assertEquals("37777700000", new BigCharBuffer(11).appendOct(037777700000).toString());
    assertEquals("37777000000", new BigCharBuffer(11).appendOct(037777000000).toString());
    assertEquals("37770000000", new BigCharBuffer(11).appendOct(037770000000).toString());
    assertEquals("37700000000", new BigCharBuffer(11).appendOct(037700000000).toString());
    assertEquals("37000000000", new BigCharBuffer(11).appendOct(037000000000).toString());
    assertEquals("30000000000", new BigCharBuffer(11).appendOct(030000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOctStripLeadingZeros(int)} method.
   */
  @Test
  public void test_appendOctTrimZeros_int() {
    // @formatter:off
    assertEquals(          "0", new BigCharBuffer( 1).appendOctStripLeadingZeros(000000000000).toString());
    assertEquals(          "7", new BigCharBuffer( 1).appendOctStripLeadingZeros(000000000007).toString());
    assertEquals(         "77", new BigCharBuffer( 2).appendOctStripLeadingZeros(000000000077).toString());
    assertEquals(        "777", new BigCharBuffer( 3).appendOctStripLeadingZeros(000000000777).toString());
    assertEquals(       "7777", new BigCharBuffer( 4).appendOctStripLeadingZeros(000000007777).toString());
    assertEquals(      "77777", new BigCharBuffer( 5).appendOctStripLeadingZeros(000000077777).toString());
    assertEquals(     "777777", new BigCharBuffer( 6).appendOctStripLeadingZeros(000000777777).toString());
    assertEquals(    "7777777", new BigCharBuffer( 7).appendOctStripLeadingZeros(000007777777).toString());
    assertEquals(   "77777777", new BigCharBuffer( 8).appendOctStripLeadingZeros(000077777777).toString());
    assertEquals(  "777777777", new BigCharBuffer( 9).appendOctStripLeadingZeros(000777777777).toString());
    assertEquals( "7777777777", new BigCharBuffer(10).appendOctStripLeadingZeros(007777777777).toString());
    assertEquals("37777777777", new BigCharBuffer(11).appendOctStripLeadingZeros(037777777777).toString());
    assertEquals("37777777770", new BigCharBuffer(11).appendOctStripLeadingZeros(037777777770).toString());
    assertEquals("37777777700", new BigCharBuffer(11).appendOctStripLeadingZeros(037777777700).toString());
    assertEquals("37777777000", new BigCharBuffer(11).appendOctStripLeadingZeros(037777777000).toString());
    assertEquals("37777770000", new BigCharBuffer(11).appendOctStripLeadingZeros(037777770000).toString());
    assertEquals("37777700000", new BigCharBuffer(11).appendOctStripLeadingZeros(037777700000).toString());
    assertEquals("37777000000", new BigCharBuffer(11).appendOctStripLeadingZeros(037777000000).toString());
    assertEquals("37770000000", new BigCharBuffer(11).appendOctStripLeadingZeros(037770000000).toString());
    assertEquals("37700000000", new BigCharBuffer(11).appendOctStripLeadingZeros(037700000000).toString());
    assertEquals("37000000000", new BigCharBuffer(11).appendOctStripLeadingZeros(037000000000).toString());
    assertEquals("30000000000", new BigCharBuffer(11).appendOctStripLeadingZeros(030000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getOctCapacity(int)} method.
   */
  @Test
  public void test_getOctCapacity_int() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getOctCapacity(000000000000));
    assertEquals( 1, CharBuffer.getOctCapacity(000000000001));
    assertEquals( 1, CharBuffer.getOctCapacity(000000000002));
    assertEquals( 1, CharBuffer.getOctCapacity(000000000004));
    assertEquals( 2, CharBuffer.getOctCapacity(000000000010));
    assertEquals( 2, CharBuffer.getOctCapacity(000000000020));
    assertEquals( 2, CharBuffer.getOctCapacity(000000000040));
    assertEquals( 3, CharBuffer.getOctCapacity(000000000100));
    assertEquals( 3, CharBuffer.getOctCapacity(000000000200));
    assertEquals( 3, CharBuffer.getOctCapacity(000000000400));
    assertEquals( 4, CharBuffer.getOctCapacity(000000001000));
    assertEquals( 4, CharBuffer.getOctCapacity(000000002000));
    assertEquals( 4, CharBuffer.getOctCapacity(000000004000));
    assertEquals( 5, CharBuffer.getOctCapacity(000000010000));
    assertEquals( 5, CharBuffer.getOctCapacity(000000020000));
    assertEquals( 5, CharBuffer.getOctCapacity(000000040000));
    assertEquals( 6, CharBuffer.getOctCapacity(000000100000));
    assertEquals( 6, CharBuffer.getOctCapacity(000000200000));
    assertEquals( 6, CharBuffer.getOctCapacity(000000400000));
    assertEquals( 7, CharBuffer.getOctCapacity(000001000000));
    assertEquals( 7, CharBuffer.getOctCapacity(000002000000));
    assertEquals( 7, CharBuffer.getOctCapacity(000004000000));
    assertEquals( 8, CharBuffer.getOctCapacity(000010000000));
    assertEquals( 8, CharBuffer.getOctCapacity(000020000000));
    assertEquals( 8, CharBuffer.getOctCapacity(000040000000));
    assertEquals( 9, CharBuffer.getOctCapacity(000100000000));
    assertEquals( 9, CharBuffer.getOctCapacity(000200000000));
    assertEquals( 9, CharBuffer.getOctCapacity(000400000000));
    assertEquals(10, CharBuffer.getOctCapacity(001000000000));
    assertEquals(10, CharBuffer.getOctCapacity(002000000000));
    assertEquals(10, CharBuffer.getOctCapacity(004000000000));
    assertEquals(11, CharBuffer.getOctCapacity(010000000000));
    assertEquals(11, CharBuffer.getOctCapacity(020000000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOct(long)} method.
   */
  @Test
  public void test_appendOct_long() {
    // @formatter:off
    assertEquals("0000000000000000000000", new BigCharBuffer(22).appendOct(00000000000000000000000L).toString());
    assertEquals("0000000000000000000007", new BigCharBuffer(22).appendOct(00000000000000000000007L).toString());
    assertEquals("0000000000000000000077", new BigCharBuffer(22).appendOct(00000000000000000000077L).toString());
    assertEquals("0000000000000000000777", new BigCharBuffer(22).appendOct(00000000000000000000777L).toString());
    assertEquals("0000000000000000007777", new BigCharBuffer(22).appendOct(00000000000000000007777L).toString());
    assertEquals("0000000000000000077777", new BigCharBuffer(22).appendOct(00000000000000000077777L).toString());
    assertEquals("0000000000000000777777", new BigCharBuffer(22).appendOct(00000000000000000777777L).toString());
    assertEquals("0000000000000007777777", new BigCharBuffer(22).appendOct(00000000000000007777777L).toString());
    assertEquals("0000000000000077777777", new BigCharBuffer(22).appendOct(00000000000000077777777L).toString());
    assertEquals("0000000000000777777777", new BigCharBuffer(22).appendOct(00000000000000777777777L).toString());
    assertEquals("0000000000007777777777", new BigCharBuffer(22).appendOct(00000000000007777777777L).toString());
    assertEquals("0000000000077777777777", new BigCharBuffer(22).appendOct(00000000000077777777777L).toString());
    assertEquals("0000000000777777777777", new BigCharBuffer(22).appendOct(00000000000777777777777L).toString());
    assertEquals("0000000007777777777777", new BigCharBuffer(22).appendOct(00000000007777777777777L).toString());
    assertEquals("0000000077777777777777", new BigCharBuffer(22).appendOct(00000000077777777777777L).toString());
    assertEquals("0000000777777777777777", new BigCharBuffer(22).appendOct(00000000777777777777777L).toString());
    assertEquals("0000007777777777777777", new BigCharBuffer(22).appendOct(00000007777777777777777L).toString());
    assertEquals("0000077777777777777777", new BigCharBuffer(22).appendOct(00000077777777777777777L).toString());
    assertEquals("0000777777777777777777", new BigCharBuffer(22).appendOct(00000777777777777777777L).toString());
    assertEquals("0007777777777777777777", new BigCharBuffer(22).appendOct(00007777777777777777777L).toString());
    assertEquals("0077777777777777777777", new BigCharBuffer(22).appendOct(00077777777777777777777L).toString());
    assertEquals("0777777777777777777777", new BigCharBuffer(22).appendOct(00777777777777777777777L).toString());
    assertEquals("1777777777777777777777", new BigCharBuffer(22).appendOct(01777777777777777777777L).toString());
    assertEquals("1777777777777777777770", new BigCharBuffer(22).appendOct(01777777777777777777770L).toString());
    assertEquals("1777777777777777777700", new BigCharBuffer(22).appendOct(01777777777777777777700L).toString());
    assertEquals("1777777777777777777000", new BigCharBuffer(22).appendOct(01777777777777777777000L).toString());
    assertEquals("1777777777777777770000", new BigCharBuffer(22).appendOct(01777777777777777770000L).toString());
    assertEquals("1777777777777777700000", new BigCharBuffer(22).appendOct(01777777777777777700000L).toString());
    assertEquals("1777777777777777000000", new BigCharBuffer(22).appendOct(01777777777777777000000L).toString());
    assertEquals("1777777777777770000000", new BigCharBuffer(22).appendOct(01777777777777770000000L).toString());
    assertEquals("1777777777777700000000", new BigCharBuffer(22).appendOct(01777777777777700000000L).toString());
    assertEquals("1777777777777000000000", new BigCharBuffer(22).appendOct(01777777777777000000000L).toString());
    assertEquals("1777777777770000000000", new BigCharBuffer(22).appendOct(01777777777770000000000L).toString());
    assertEquals("1777777777700000000000", new BigCharBuffer(22).appendOct(01777777777700000000000L).toString());
    assertEquals("1777777777000000000000", new BigCharBuffer(22).appendOct(01777777777000000000000L).toString());
    assertEquals("1777777770000000000000", new BigCharBuffer(22).appendOct(01777777770000000000000L).toString());
    assertEquals("1777777700000000000000", new BigCharBuffer(22).appendOct(01777777700000000000000L).toString());
    assertEquals("1777777000000000000000", new BigCharBuffer(22).appendOct(01777777000000000000000L).toString());
    assertEquals("1777770000000000000000", new BigCharBuffer(22).appendOct(01777770000000000000000L).toString());
    assertEquals("1777700000000000000000", new BigCharBuffer(22).appendOct(01777700000000000000000L).toString());
    assertEquals("1777000000000000000000", new BigCharBuffer(22).appendOct(01777000000000000000000L).toString());
    assertEquals("1770000000000000000000", new BigCharBuffer(22).appendOct(01770000000000000000000L).toString());
    assertEquals("1700000000000000000000", new BigCharBuffer(22).appendOct(01700000000000000000000L).toString());
    assertEquals("1000000000000000000000", new BigCharBuffer(22).appendOct(01000000000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendOctStripLeadingZeros(long)} method.
   */
  @Test
  public void test_appendOctTrimZeros_long() {
    // @formatter:off
    assertEquals(                     "0", new BigCharBuffer( 1).appendOctStripLeadingZeros(00000000000000000000000L).toString());
    assertEquals(                     "7", new BigCharBuffer( 1).appendOctStripLeadingZeros(00000000000000000000007L).toString());
    assertEquals(                    "77", new BigCharBuffer( 2).appendOctStripLeadingZeros(00000000000000000000077L).toString());
    assertEquals(                   "777", new BigCharBuffer( 3).appendOctStripLeadingZeros(00000000000000000000777L).toString());
    assertEquals(                  "7777", new BigCharBuffer( 4).appendOctStripLeadingZeros(00000000000000000007777L).toString());
    assertEquals(                 "77777", new BigCharBuffer( 5).appendOctStripLeadingZeros(00000000000000000077777L).toString());
    assertEquals(                "777777", new BigCharBuffer( 6).appendOctStripLeadingZeros(00000000000000000777777L).toString());
    assertEquals(               "7777777", new BigCharBuffer( 7).appendOctStripLeadingZeros(00000000000000007777777L).toString());
    assertEquals(              "77777777", new BigCharBuffer( 8).appendOctStripLeadingZeros(00000000000000077777777L).toString());
    assertEquals(             "777777777", new BigCharBuffer( 9).appendOctStripLeadingZeros(00000000000000777777777L).toString());
    assertEquals(            "7777777777", new BigCharBuffer(10).appendOctStripLeadingZeros(00000000000007777777777L).toString());
    assertEquals(           "77777777777", new BigCharBuffer(11).appendOctStripLeadingZeros(00000000000077777777777L).toString());
    assertEquals(          "777777777777", new BigCharBuffer(12).appendOctStripLeadingZeros(00000000000777777777777L).toString());
    assertEquals(         "7777777777777", new BigCharBuffer(13).appendOctStripLeadingZeros(00000000007777777777777L).toString());
    assertEquals(        "77777777777777", new BigCharBuffer(14).appendOctStripLeadingZeros(00000000077777777777777L).toString());
    assertEquals(       "777777777777777", new BigCharBuffer(15).appendOctStripLeadingZeros(00000000777777777777777L).toString());
    assertEquals(      "7777777777777777", new BigCharBuffer(16).appendOctStripLeadingZeros(00000007777777777777777L).toString());
    assertEquals(     "77777777777777777", new BigCharBuffer(17).appendOctStripLeadingZeros(00000077777777777777777L).toString());
    assertEquals(    "777777777777777777", new BigCharBuffer(18).appendOctStripLeadingZeros(00000777777777777777777L).toString());
    assertEquals(   "7777777777777777777", new BigCharBuffer(19).appendOctStripLeadingZeros(00007777777777777777777L).toString());
    assertEquals(  "77777777777777777777", new BigCharBuffer(20).appendOctStripLeadingZeros(00077777777777777777777L).toString());
    assertEquals( "777777777777777777777", new BigCharBuffer(21).appendOctStripLeadingZeros(00777777777777777777777L).toString());
    assertEquals("1777777777777777777777", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777777777L).toString());
    assertEquals("1777777777777777777770", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777777770L).toString());
    assertEquals("1777777777777777777700", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777777700L).toString());
    assertEquals("1777777777777777777000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777777000L).toString());
    assertEquals("1777777777777777770000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777770000L).toString());
    assertEquals("1777777777777777700000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777700000L).toString());
    assertEquals("1777777777777777000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777777000000L).toString());
    assertEquals("1777777777777770000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777770000000L).toString());
    assertEquals("1777777777777700000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777700000000L).toString());
    assertEquals("1777777777777000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777777000000000L).toString());
    assertEquals("1777777777770000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777770000000000L).toString());
    assertEquals("1777777777700000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777700000000000L).toString());
    assertEquals("1777777777000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777777000000000000L).toString());
    assertEquals("1777777770000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777770000000000000L).toString());
    assertEquals("1777777700000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777700000000000000L).toString());
    assertEquals("1777777000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777777000000000000000L).toString());
    assertEquals("1777770000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777770000000000000000L).toString());
    assertEquals("1777700000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777700000000000000000L).toString());
    assertEquals("1777000000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01777000000000000000000L).toString());
    assertEquals("1770000000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01770000000000000000000L).toString());
    assertEquals("1700000000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01700000000000000000000L).toString());
    assertEquals("1000000000000000000000", new BigCharBuffer(22).appendOctStripLeadingZeros(01000000000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getOctCapacity(short)} method.
   */
  @Test
  public void test_getOctCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getOctCapacity(00000000000000000000000L));
    assertEquals( 1, CharBuffer.getOctCapacity(00000000000000000000001L));
    assertEquals( 1, CharBuffer.getOctCapacity(00000000000000000000002L));
    assertEquals( 1, CharBuffer.getOctCapacity(00000000000000000000004L));
    assertEquals( 2, CharBuffer.getOctCapacity(00000000000000000000010L));
    assertEquals( 2, CharBuffer.getOctCapacity(00000000000000000000020L));
    assertEquals( 2, CharBuffer.getOctCapacity(00000000000000000000040L));
    assertEquals( 3, CharBuffer.getOctCapacity(00000000000000000000100L));
    assertEquals( 3, CharBuffer.getOctCapacity(00000000000000000000200L));
    assertEquals( 3, CharBuffer.getOctCapacity(00000000000000000000400L));
    assertEquals( 4, CharBuffer.getOctCapacity(00000000000000000001000L));
    assertEquals( 4, CharBuffer.getOctCapacity(00000000000000000002000L));
    assertEquals( 4, CharBuffer.getOctCapacity(00000000000000000004000L));
    assertEquals( 5, CharBuffer.getOctCapacity(00000000000000000010000L));
    assertEquals( 5, CharBuffer.getOctCapacity(00000000000000000020000L));
    assertEquals( 5, CharBuffer.getOctCapacity(00000000000000000040000L));
    assertEquals( 6, CharBuffer.getOctCapacity(00000000000000000100000L));
    assertEquals( 6, CharBuffer.getOctCapacity(00000000000000000200000L));
    assertEquals( 6, CharBuffer.getOctCapacity(00000000000000000400000L));
    assertEquals( 7, CharBuffer.getOctCapacity(00000000000000001000000L));
    assertEquals( 7, CharBuffer.getOctCapacity(00000000000000002000000L));
    assertEquals( 7, CharBuffer.getOctCapacity(00000000000000004000000L));
    assertEquals( 8, CharBuffer.getOctCapacity(00000000000000010000000L));
    assertEquals( 8, CharBuffer.getOctCapacity(00000000000000020000000L));
    assertEquals( 8, CharBuffer.getOctCapacity(00000000000000040000000L));
    assertEquals( 9, CharBuffer.getOctCapacity(00000000000000100000000L));
    assertEquals( 9, CharBuffer.getOctCapacity(00000000000000200000000L));
    assertEquals( 9, CharBuffer.getOctCapacity(00000000000000400000000L));
    assertEquals(10, CharBuffer.getOctCapacity(00000000000001000000000L));
    assertEquals(10, CharBuffer.getOctCapacity(00000000000002000000000L));
    assertEquals(10, CharBuffer.getOctCapacity(00000000000004000000000L));
    assertEquals(11, CharBuffer.getOctCapacity(00000000000010000000000L));
    assertEquals(11, CharBuffer.getOctCapacity(00000000000020000000000L));
    assertEquals(11, CharBuffer.getOctCapacity(00000000000040000000000L));
    assertEquals(12, CharBuffer.getOctCapacity(00000000000100000000000L));
    assertEquals(12, CharBuffer.getOctCapacity(00000000000200000000000L));
    assertEquals(12, CharBuffer.getOctCapacity(00000000000400000000000L));
    assertEquals(13, CharBuffer.getOctCapacity(00000000001000000000000L));
    assertEquals(13, CharBuffer.getOctCapacity(00000000002000000000000L));
    assertEquals(13, CharBuffer.getOctCapacity(00000000004000000000000L));
    assertEquals(14, CharBuffer.getOctCapacity(00000000010000000000000L));
    assertEquals(14, CharBuffer.getOctCapacity(00000000020000000000000L));
    assertEquals(14, CharBuffer.getOctCapacity(00000000040000000000000L));
    assertEquals(15, CharBuffer.getOctCapacity(00000000100000000000000L));
    assertEquals(15, CharBuffer.getOctCapacity(00000000200000000000000L));
    assertEquals(15, CharBuffer.getOctCapacity(00000000400000000000000L));
    assertEquals(16, CharBuffer.getOctCapacity(00000001000000000000000L));
    assertEquals(16, CharBuffer.getOctCapacity(00000002000000000000000L));
    assertEquals(16, CharBuffer.getOctCapacity(00000004000000000000000L));
    assertEquals(17, CharBuffer.getOctCapacity(00000010000000000000000L));
    assertEquals(17, CharBuffer.getOctCapacity(00000020000000000000000L));
    assertEquals(17, CharBuffer.getOctCapacity(00000040000000000000000L));
    assertEquals(18, CharBuffer.getOctCapacity(00000100000000000000000L));
    assertEquals(18, CharBuffer.getOctCapacity(00000200000000000000000L));
    assertEquals(18, CharBuffer.getOctCapacity(00000400000000000000000L));
    assertEquals(19, CharBuffer.getOctCapacity(00001000000000000000000L));
    assertEquals(19, CharBuffer.getOctCapacity(00002000000000000000000L));
    assertEquals(19, CharBuffer.getOctCapacity(00004000000000000000000L));
    assertEquals(20, CharBuffer.getOctCapacity(00010000000000000000000L));
    assertEquals(20, CharBuffer.getOctCapacity(00020000000000000000000L));
    assertEquals(20, CharBuffer.getOctCapacity(00040000000000000000000L));
    assertEquals(21, CharBuffer.getOctCapacity(00100000000000000000000L));
    assertEquals(21, CharBuffer.getOctCapacity(00200000000000000000000L));
    assertEquals(21, CharBuffer.getOctCapacity(00400000000000000000000L));
    assertEquals(22, CharBuffer.getOctCapacity(01000000000000000000000L));
    // @formatter:on
  }

  // Binary representation

  /**
   * Tests the {@link CharBuffer#appendBin(byte)} method.
   */
  @Test
  public void test_appendBin_byte() {
    // @formatter:off
    assertEquals("00000000", new BigCharBuffer(8).appendBin((byte) 0b00000000).toString());
    assertEquals("00000001", new BigCharBuffer(8).appendBin((byte) 0b00000001).toString());
    assertEquals("00000010", new BigCharBuffer(8).appendBin((byte) 0b00000010).toString());
    assertEquals("00000100", new BigCharBuffer(8).appendBin((byte) 0b00000100).toString());
    assertEquals("00001000", new BigCharBuffer(8).appendBin((byte) 0b00001000).toString());
    assertEquals("00010000", new BigCharBuffer(8).appendBin((byte) 0b00010000).toString());
    assertEquals("00100000", new BigCharBuffer(8).appendBin((byte) 0b00100000).toString());
    assertEquals("01000000", new BigCharBuffer(8).appendBin((byte) 0b01000000).toString());
    assertEquals("10000000", new BigCharBuffer(8).appendBin((byte) 0b10000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinStripLeadingZeros(byte)} method.
   */
  @Test
  public void test_appendBinTrimZeros_byte() {
    // @formatter:off
    assertEquals(       "0", new BigCharBuffer(1).appendBinStripLeadingZeros((byte) 0b00000000).toString());
    assertEquals(       "1", new BigCharBuffer(1).appendBinStripLeadingZeros((byte) 0b00000001).toString());
    assertEquals(      "10", new BigCharBuffer(2).appendBinStripLeadingZeros((byte) 0b00000010).toString());
    assertEquals(     "100", new BigCharBuffer(3).appendBinStripLeadingZeros((byte) 0b00000100).toString());
    assertEquals(    "1000", new BigCharBuffer(4).appendBinStripLeadingZeros((byte) 0b00001000).toString());
    assertEquals(   "10000", new BigCharBuffer(5).appendBinStripLeadingZeros((byte) 0b00010000).toString());
    assertEquals(  "100000", new BigCharBuffer(6).appendBinStripLeadingZeros((byte) 0b00100000).toString());
    assertEquals( "1000000", new BigCharBuffer(7).appendBinStripLeadingZeros((byte) 0b01000000).toString());
    assertEquals("10000000", new BigCharBuffer(8).appendBinStripLeadingZeros((byte) 0b10000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(byte)} method.
   */
  @Test
  public void test_getBinCapacity_byte() {
    // @formatter:off
    assertEquals(1, CharBuffer.getBinCapacity((byte) 0b00000000));
    assertEquals(1, CharBuffer.getBinCapacity((byte) 0b00000001));
    assertEquals(2, CharBuffer.getBinCapacity((byte) 0b00000010));
    assertEquals(3, CharBuffer.getBinCapacity((byte) 0b00000100));
    assertEquals(4, CharBuffer.getBinCapacity((byte) 0b00001000));
    assertEquals(5, CharBuffer.getBinCapacity((byte) 0b00010000));
    assertEquals(6, CharBuffer.getBinCapacity((byte) 0b00100000));
    assertEquals(7, CharBuffer.getBinCapacity((byte) 0b01000000));
    assertEquals(8, CharBuffer.getBinCapacity((byte) 0b10000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(short)} method.
   */
  @Test
  public void test_appendBin_short() {
    // @formatter:off
    assertEquals("0000000000000000", new BigCharBuffer(16).appendBin((short) 0b0000000000000000).toString());
    assertEquals("0000000000000001", new BigCharBuffer(16).appendBin((short) 0b0000000000000001).toString());
    assertEquals("0000000000000010", new BigCharBuffer(16).appendBin((short) 0b0000000000000010).toString());
    assertEquals("0000000000000100", new BigCharBuffer(16).appendBin((short) 0b0000000000000100).toString());
    assertEquals("0000000000001000", new BigCharBuffer(16).appendBin((short) 0b0000000000001000).toString());
    assertEquals("0000000000010000", new BigCharBuffer(16).appendBin((short) 0b0000000000010000).toString());
    assertEquals("0000000000100000", new BigCharBuffer(16).appendBin((short) 0b0000000000100000).toString());
    assertEquals("0000000001000000", new BigCharBuffer(16).appendBin((short) 0b0000000001000000).toString());
    assertEquals("0000000010000000", new BigCharBuffer(16).appendBin((short) 0b0000000010000000).toString());
    assertEquals("0000000100000000", new BigCharBuffer(16).appendBin((short) 0b0000000100000000).toString());
    assertEquals("0000001000000000", new BigCharBuffer(16).appendBin((short) 0b0000001000000000).toString());
    assertEquals("0000010000000000", new BigCharBuffer(16).appendBin((short) 0b0000010000000000).toString());
    assertEquals("0000100000000000", new BigCharBuffer(16).appendBin((short) 0b0000100000000000).toString());
    assertEquals("0001000000000000", new BigCharBuffer(16).appendBin((short) 0b0001000000000000).toString());
    assertEquals("0010000000000000", new BigCharBuffer(16).appendBin((short) 0b0010000000000000).toString());
    assertEquals("0100000000000000", new BigCharBuffer(16).appendBin((short) 0b0100000000000000).toString());
    assertEquals("1000000000000000", new BigCharBuffer(16).appendBin((short) 0b1000000000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinStripLeadingZeros(short)} method.
   */
  @Test
  public void test_appendBinTrimZeros_short() {
    // @formatter:off
    assertEquals(               "0", new BigCharBuffer( 1).appendBinStripLeadingZeros((short) 0b0000000000000000).toString());
    assertEquals(               "1", new BigCharBuffer( 1).appendBinStripLeadingZeros((short) 0b0000000000000001).toString());
    assertEquals(              "10", new BigCharBuffer( 2).appendBinStripLeadingZeros((short) 0b0000000000000010).toString());
    assertEquals(             "100", new BigCharBuffer( 3).appendBinStripLeadingZeros((short) 0b0000000000000100).toString());
    assertEquals(            "1000", new BigCharBuffer( 4).appendBinStripLeadingZeros((short) 0b0000000000001000).toString());
    assertEquals(           "10000", new BigCharBuffer( 5).appendBinStripLeadingZeros((short) 0b0000000000010000).toString());
    assertEquals(          "100000", new BigCharBuffer( 6).appendBinStripLeadingZeros((short) 0b0000000000100000).toString());
    assertEquals(         "1000000", new BigCharBuffer( 7).appendBinStripLeadingZeros((short) 0b0000000001000000).toString());
    assertEquals(        "10000000", new BigCharBuffer( 8).appendBinStripLeadingZeros((short) 0b0000000010000000).toString());
    assertEquals(       "100000000", new BigCharBuffer( 9).appendBinStripLeadingZeros((short) 0b0000000100000000).toString());
    assertEquals(      "1000000000", new BigCharBuffer(10).appendBinStripLeadingZeros((short) 0b0000001000000000).toString());
    assertEquals(     "10000000000", new BigCharBuffer(11).appendBinStripLeadingZeros((short) 0b0000010000000000).toString());
    assertEquals(    "100000000000", new BigCharBuffer(12).appendBinStripLeadingZeros((short) 0b0000100000000000).toString());
    assertEquals(   "1000000000000", new BigCharBuffer(13).appendBinStripLeadingZeros((short) 0b0001000000000000).toString());
    assertEquals(  "10000000000000", new BigCharBuffer(14).appendBinStripLeadingZeros((short) 0b0010000000000000).toString());
    assertEquals( "100000000000000", new BigCharBuffer(15).appendBinStripLeadingZeros((short) 0b0100000000000000).toString());
    assertEquals("1000000000000000", new BigCharBuffer(16).appendBinStripLeadingZeros((short) 0b1000000000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(short)} method.
   */
  @Test
  public void test_getBinCapacity_short() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity((short) 0b0000000000000000));
    assertEquals( 1, CharBuffer.getBinCapacity((short) 0b0000000000000001));
    assertEquals( 2, CharBuffer.getBinCapacity((short) 0b0000000000000010));
    assertEquals( 3, CharBuffer.getBinCapacity((short) 0b0000000000000100));
    assertEquals( 4, CharBuffer.getBinCapacity((short) 0b0000000000001000));
    assertEquals( 5, CharBuffer.getBinCapacity((short) 0b0000000000010000));
    assertEquals( 6, CharBuffer.getBinCapacity((short) 0b0000000000100000));
    assertEquals( 7, CharBuffer.getBinCapacity((short) 0b0000000001000000));
    assertEquals( 8, CharBuffer.getBinCapacity((short) 0b0000000010000000));
    assertEquals( 9, CharBuffer.getBinCapacity((short) 0b0000000100000000));
    assertEquals(10, CharBuffer.getBinCapacity((short) 0b0000001000000000));
    assertEquals(11, CharBuffer.getBinCapacity((short) 0b0000010000000000));
    assertEquals(12, CharBuffer.getBinCapacity((short) 0b0000100000000000));
    assertEquals(13, CharBuffer.getBinCapacity((short) 0b0001000000000000));
    assertEquals(14, CharBuffer.getBinCapacity((short) 0b0010000000000000));
    assertEquals(15, CharBuffer.getBinCapacity((short) 0b0100000000000000));
    assertEquals(16, CharBuffer.getBinCapacity((short) 0b1000000000000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(int)} method.
   */
  @Test
  public void test_appendBin_int() {
    // @formatter:off
    assertEquals("00000000000000000000000000000000", new BigCharBuffer(32).appendBin(0b00000000000000000000000000000000).toString());
    assertEquals("00000000000000000000000000000001", new BigCharBuffer(32).appendBin(0b00000000000000000000000000000001).toString());
    assertEquals("00000000000000000000000000000010", new BigCharBuffer(32).appendBin(0b00000000000000000000000000000010).toString());
    assertEquals("00000000000000000000000000000100", new BigCharBuffer(32).appendBin(0b00000000000000000000000000000100).toString());
    assertEquals("00000000000000000000000000001000", new BigCharBuffer(32).appendBin(0b00000000000000000000000000001000).toString());
    assertEquals("00000000000000000000000000010000", new BigCharBuffer(32).appendBin(0b00000000000000000000000000010000).toString());
    assertEquals("00000000000000000000000000100000", new BigCharBuffer(32).appendBin(0b00000000000000000000000000100000).toString());
    assertEquals("00000000000000000000000001000000", new BigCharBuffer(32).appendBin(0b00000000000000000000000001000000).toString());
    assertEquals("00000000000000000000000010000000", new BigCharBuffer(32).appendBin(0b00000000000000000000000010000000).toString());
    assertEquals("00000000000000000000000100000000", new BigCharBuffer(32).appendBin(0b00000000000000000000000100000000).toString());
    assertEquals("00000000000000000000001000000000", new BigCharBuffer(32).appendBin(0b00000000000000000000001000000000).toString());
    assertEquals("00000000000000000000010000000000", new BigCharBuffer(32).appendBin(0b00000000000000000000010000000000).toString());
    assertEquals("00000000000000000000100000000000", new BigCharBuffer(32).appendBin(0b00000000000000000000100000000000).toString());
    assertEquals("00000000000000000001000000000000", new BigCharBuffer(32).appendBin(0b00000000000000000001000000000000).toString());
    assertEquals("00000000000000000010000000000000", new BigCharBuffer(32).appendBin(0b00000000000000000010000000000000).toString());
    assertEquals("00000000000000000100000000000000", new BigCharBuffer(32).appendBin(0b00000000000000000100000000000000).toString());
    assertEquals("00000000000000001000000000000000", new BigCharBuffer(32).appendBin(0b00000000000000001000000000000000).toString());
    assertEquals("00000000000000010000000000000000", new BigCharBuffer(32).appendBin(0b00000000000000010000000000000000).toString());
    assertEquals("00000000000000100000000000000000", new BigCharBuffer(32).appendBin(0b00000000000000100000000000000000).toString());
    assertEquals("00000000000001000000000000000000", new BigCharBuffer(32).appendBin(0b00000000000001000000000000000000).toString());
    assertEquals("00000000000010000000000000000000", new BigCharBuffer(32).appendBin(0b00000000000010000000000000000000).toString());
    assertEquals("00000000000100000000000000000000", new BigCharBuffer(32).appendBin(0b00000000000100000000000000000000).toString());
    assertEquals("00000000001000000000000000000000", new BigCharBuffer(32).appendBin(0b00000000001000000000000000000000).toString());
    assertEquals("00000000010000000000000000000000", new BigCharBuffer(32).appendBin(0b00000000010000000000000000000000).toString());
    assertEquals("00000000100000000000000000000000", new BigCharBuffer(32).appendBin(0b00000000100000000000000000000000).toString());
    assertEquals("00000001000000000000000000000000", new BigCharBuffer(32).appendBin(0b00000001000000000000000000000000).toString());
    assertEquals("00000010000000000000000000000000", new BigCharBuffer(32).appendBin(0b00000010000000000000000000000000).toString());
    assertEquals("00000100000000000000000000000000", new BigCharBuffer(32).appendBin(0b00000100000000000000000000000000).toString());
    assertEquals("00001000000000000000000000000000", new BigCharBuffer(32).appendBin(0b00001000000000000000000000000000).toString());
    assertEquals("00010000000000000000000000000000", new BigCharBuffer(32).appendBin(0b00010000000000000000000000000000).toString());
    assertEquals("00100000000000000000000000000000", new BigCharBuffer(32).appendBin(0b00100000000000000000000000000000).toString());
    assertEquals("01000000000000000000000000000000", new BigCharBuffer(32).appendBin(0b01000000000000000000000000000000).toString());
    assertEquals("10000000000000000000000000000000", new BigCharBuffer(32).appendBin(0b10000000000000000000000000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinStripLeadingZeros(int)} method.
   */
  @Test
  public void test_appendBinTrimZeros_int() {
    // @formatter:off
    assertEquals(                               "0", new BigCharBuffer( 1).appendBinStripLeadingZeros(0b00000000000000000000000000000000).toString());
    assertEquals(                               "1", new BigCharBuffer( 1).appendBinStripLeadingZeros(0b00000000000000000000000000000001).toString());
    assertEquals(                              "10", new BigCharBuffer( 2).appendBinStripLeadingZeros(0b00000000000000000000000000000010).toString());
    assertEquals(                             "100", new BigCharBuffer( 3).appendBinStripLeadingZeros(0b00000000000000000000000000000100).toString());
    assertEquals(                            "1000", new BigCharBuffer( 4).appendBinStripLeadingZeros(0b00000000000000000000000000001000).toString());
    assertEquals(                           "10000", new BigCharBuffer( 5).appendBinStripLeadingZeros(0b00000000000000000000000000010000).toString());
    assertEquals(                          "100000", new BigCharBuffer( 6).appendBinStripLeadingZeros(0b00000000000000000000000000100000).toString());
    assertEquals(                         "1000000", new BigCharBuffer( 7).appendBinStripLeadingZeros(0b00000000000000000000000001000000).toString());
    assertEquals(                        "10000000", new BigCharBuffer( 8).appendBinStripLeadingZeros(0b00000000000000000000000010000000).toString());
    assertEquals(                       "100000000", new BigCharBuffer( 9).appendBinStripLeadingZeros(0b00000000000000000000000100000000).toString());
    assertEquals(                      "1000000000", new BigCharBuffer(10).appendBinStripLeadingZeros(0b00000000000000000000001000000000).toString());
    assertEquals(                     "10000000000", new BigCharBuffer(11).appendBinStripLeadingZeros(0b00000000000000000000010000000000).toString());
    assertEquals(                    "100000000000", new BigCharBuffer(12).appendBinStripLeadingZeros(0b00000000000000000000100000000000).toString());
    assertEquals(                   "1000000000000", new BigCharBuffer(13).appendBinStripLeadingZeros(0b00000000000000000001000000000000).toString());
    assertEquals(                  "10000000000000", new BigCharBuffer(14).appendBinStripLeadingZeros(0b00000000000000000010000000000000).toString());
    assertEquals(                 "100000000000000", new BigCharBuffer(15).appendBinStripLeadingZeros(0b00000000000000000100000000000000).toString());
    assertEquals(                "1000000000000000", new BigCharBuffer(16).appendBinStripLeadingZeros(0b00000000000000001000000000000000).toString());
    assertEquals(               "10000000000000000", new BigCharBuffer(17).appendBinStripLeadingZeros(0b00000000000000010000000000000000).toString());
    assertEquals(              "100000000000000000", new BigCharBuffer(18).appendBinStripLeadingZeros(0b00000000000000100000000000000000).toString());
    assertEquals(             "1000000000000000000", new BigCharBuffer(19).appendBinStripLeadingZeros(0b00000000000001000000000000000000).toString());
    assertEquals(            "10000000000000000000", new BigCharBuffer(20).appendBinStripLeadingZeros(0b00000000000010000000000000000000).toString());
    assertEquals(           "100000000000000000000", new BigCharBuffer(21).appendBinStripLeadingZeros(0b00000000000100000000000000000000).toString());
    assertEquals(          "1000000000000000000000", new BigCharBuffer(22).appendBinStripLeadingZeros(0b00000000001000000000000000000000).toString());
    assertEquals(         "10000000000000000000000", new BigCharBuffer(23).appendBinStripLeadingZeros(0b00000000010000000000000000000000).toString());
    assertEquals(        "100000000000000000000000", new BigCharBuffer(24).appendBinStripLeadingZeros(0b00000000100000000000000000000000).toString());
    assertEquals(       "1000000000000000000000000", new BigCharBuffer(25).appendBinStripLeadingZeros(0b00000001000000000000000000000000).toString());
    assertEquals(      "10000000000000000000000000", new BigCharBuffer(26).appendBinStripLeadingZeros(0b00000010000000000000000000000000).toString());
    assertEquals(     "100000000000000000000000000", new BigCharBuffer(27).appendBinStripLeadingZeros(0b00000100000000000000000000000000).toString());
    assertEquals(    "1000000000000000000000000000", new BigCharBuffer(28).appendBinStripLeadingZeros(0b00001000000000000000000000000000).toString());
    assertEquals(   "10000000000000000000000000000", new BigCharBuffer(29).appendBinStripLeadingZeros(0b00010000000000000000000000000000).toString());
    assertEquals(  "100000000000000000000000000000", new BigCharBuffer(30).appendBinStripLeadingZeros(0b00100000000000000000000000000000).toString());
    assertEquals( "1000000000000000000000000000000", new BigCharBuffer(31).appendBinStripLeadingZeros(0b01000000000000000000000000000000).toString());
    assertEquals("10000000000000000000000000000000", new BigCharBuffer(32).appendBinStripLeadingZeros(0b10000000000000000000000000000000).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(int)} method.
   */
  @Test
  public void test_getBinCapacity_int() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity(0b00000000000000000000000000000000));
    assertEquals( 1, CharBuffer.getBinCapacity(0b00000000000000000000000000000001));
    assertEquals( 2, CharBuffer.getBinCapacity(0b00000000000000000000000000000010));
    assertEquals( 3, CharBuffer.getBinCapacity(0b00000000000000000000000000000100));
    assertEquals( 4, CharBuffer.getBinCapacity(0b00000000000000000000000000001000));
    assertEquals( 5, CharBuffer.getBinCapacity(0b00000000000000000000000000010000));
    assertEquals( 6, CharBuffer.getBinCapacity(0b00000000000000000000000000100000));
    assertEquals( 7, CharBuffer.getBinCapacity(0b00000000000000000000000001000000));
    assertEquals( 8, CharBuffer.getBinCapacity(0b00000000000000000000000010000000));
    assertEquals( 9, CharBuffer.getBinCapacity(0b00000000000000000000000100000000));
    assertEquals(10, CharBuffer.getBinCapacity(0b00000000000000000000001000000000));
    assertEquals(11, CharBuffer.getBinCapacity(0b00000000000000000000010000000000));
    assertEquals(12, CharBuffer.getBinCapacity(0b00000000000000000000100000000000));
    assertEquals(13, CharBuffer.getBinCapacity(0b00000000000000000001000000000000));
    assertEquals(14, CharBuffer.getBinCapacity(0b00000000000000000010000000000000));
    assertEquals(15, CharBuffer.getBinCapacity(0b00000000000000000100000000000000));
    assertEquals(16, CharBuffer.getBinCapacity(0b00000000000000001000000000000000));
    assertEquals(17, CharBuffer.getBinCapacity(0b00000000000000010000000000000000));
    assertEquals(18, CharBuffer.getBinCapacity(0b00000000000000100000000000000000));
    assertEquals(19, CharBuffer.getBinCapacity(0b00000000000001000000000000000000));
    assertEquals(20, CharBuffer.getBinCapacity(0b00000000000010000000000000000000));
    assertEquals(21, CharBuffer.getBinCapacity(0b00000000000100000000000000000000));
    assertEquals(22, CharBuffer.getBinCapacity(0b00000000001000000000000000000000));
    assertEquals(23, CharBuffer.getBinCapacity(0b00000000010000000000000000000000));
    assertEquals(24, CharBuffer.getBinCapacity(0b00000000100000000000000000000000));
    assertEquals(25, CharBuffer.getBinCapacity(0b00000001000000000000000000000000));
    assertEquals(26, CharBuffer.getBinCapacity(0b00000010000000000000000000000000));
    assertEquals(27, CharBuffer.getBinCapacity(0b00000100000000000000000000000000));
    assertEquals(28, CharBuffer.getBinCapacity(0b00001000000000000000000000000000));
    assertEquals(29, CharBuffer.getBinCapacity(0b00010000000000000000000000000000));
    assertEquals(30, CharBuffer.getBinCapacity(0b00100000000000000000000000000000));
    assertEquals(31, CharBuffer.getBinCapacity(0b01000000000000000000000000000000));
    assertEquals(32, CharBuffer.getBinCapacity(0b10000000000000000000000000000000));
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBin(long)} method.
   */
  @Test
  public void test_appendBin_long() {
    // @formatter:off
    assertEquals("0000000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000001", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000000001L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000010", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000000010L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000000100", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000000100L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000001000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000001000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000010000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000010000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000000100000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000000100000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000001000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000001000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000010000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000010000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000000100000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000000100000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000001000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000001000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000010000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000010000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000000100000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000000100000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000001000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000001000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000010000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000010000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000000100000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000000100000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000001000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000001000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000010000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000010000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000000100000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000000100000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000001000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000001000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000010000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000010000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000000100000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000000100000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000001000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000001000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000010000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000010000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000000100000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000000100000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000001000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000001000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000010000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000010000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000000100000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000000100000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000001000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000001000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000010000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000010000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000000100000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000000100000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000001000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000001000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000010000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000010000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000000100000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000000100000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000001000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000001000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000010000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000010000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000000100000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000000100000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000001000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000001000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000010000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000010000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000000100000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000000100000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000001000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000001000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000010000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000010000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000000100000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000000100000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000001000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000001000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000010000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000010000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000000100000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000000100000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000001000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000001000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000010000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000010000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000000100000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000000100000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000001000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000001000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000010000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000010000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000000100000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000000100000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000001000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000001000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000010000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000010000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000000100000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000000100000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000001000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000001000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000010000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000010000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000000100000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000000100000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000001000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000001000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000010000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000010000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0000100000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0000100000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0001000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0001000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0010000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0010000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("0100000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b0100000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("1000000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBin(0b1000000000000000000000000000000000000000000000000000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendBinStripLeadingZeros(long)} method.
   */
  @Test
  public void test_appendBinTrimZeros_long() {
    // @formatter:off
    assertEquals(                                                               "0", new BigCharBuffer( 1).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(                                                               "1", new BigCharBuffer( 1).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000000001L).toString());
    assertEquals(                                                              "10", new BigCharBuffer( 2).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000000010L).toString());
    assertEquals(                                                             "100", new BigCharBuffer( 3).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000000100L).toString());
    assertEquals(                                                            "1000", new BigCharBuffer( 4).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000001000L).toString());
    assertEquals(                                                           "10000", new BigCharBuffer( 5).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000010000L).toString());
    assertEquals(                                                          "100000", new BigCharBuffer( 6).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000000100000L).toString());
    assertEquals(                                                         "1000000", new BigCharBuffer( 7).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000001000000L).toString());
    assertEquals(                                                        "10000000", new BigCharBuffer( 8).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000010000000L).toString());
    assertEquals(                                                       "100000000", new BigCharBuffer( 9).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000000100000000L).toString());
    assertEquals(                                                      "1000000000", new BigCharBuffer(10).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000001000000000L).toString());
    assertEquals(                                                     "10000000000", new BigCharBuffer(11).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000010000000000L).toString());
    assertEquals(                                                    "100000000000", new BigCharBuffer(12).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000000100000000000L).toString());
    assertEquals(                                                   "1000000000000", new BigCharBuffer(13).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000001000000000000L).toString());
    assertEquals(                                                  "10000000000000", new BigCharBuffer(14).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000010000000000000L).toString());
    assertEquals(                                                 "100000000000000", new BigCharBuffer(15).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000000100000000000000L).toString());
    assertEquals(                                                "1000000000000000", new BigCharBuffer(16).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000001000000000000000L).toString());
    assertEquals(                                               "10000000000000000", new BigCharBuffer(17).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000010000000000000000L).toString());
    assertEquals(                                              "100000000000000000", new BigCharBuffer(18).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000000100000000000000000L).toString());
    assertEquals(                                             "1000000000000000000", new BigCharBuffer(19).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000001000000000000000000L).toString());
    assertEquals(                                            "10000000000000000000", new BigCharBuffer(20).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000010000000000000000000L).toString());
    assertEquals(                                           "100000000000000000000", new BigCharBuffer(21).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000000100000000000000000000L).toString());
    assertEquals(                                          "1000000000000000000000", new BigCharBuffer(22).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000001000000000000000000000L).toString());
    assertEquals(                                         "10000000000000000000000", new BigCharBuffer(23).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000010000000000000000000000L).toString());
    assertEquals(                                        "100000000000000000000000", new BigCharBuffer(24).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000000100000000000000000000000L).toString());
    assertEquals(                                       "1000000000000000000000000", new BigCharBuffer(25).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000001000000000000000000000000L).toString());
    assertEquals(                                      "10000000000000000000000000", new BigCharBuffer(26).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000010000000000000000000000000L).toString());
    assertEquals(                                     "100000000000000000000000000", new BigCharBuffer(27).appendBinStripLeadingZeros(0b0000000000000000000000000000000000000100000000000000000000000000L).toString());
    assertEquals(                                    "1000000000000000000000000000", new BigCharBuffer(28).appendBinStripLeadingZeros(0b0000000000000000000000000000000000001000000000000000000000000000L).toString());
    assertEquals(                                   "10000000000000000000000000000", new BigCharBuffer(29).appendBinStripLeadingZeros(0b0000000000000000000000000000000000010000000000000000000000000000L).toString());
    assertEquals(                                  "100000000000000000000000000000", new BigCharBuffer(30).appendBinStripLeadingZeros(0b0000000000000000000000000000000000100000000000000000000000000000L).toString());
    assertEquals(                                 "1000000000000000000000000000000", new BigCharBuffer(31).appendBinStripLeadingZeros(0b0000000000000000000000000000000001000000000000000000000000000000L).toString());
    assertEquals(                                "10000000000000000000000000000000", new BigCharBuffer(32).appendBinStripLeadingZeros(0b0000000000000000000000000000000010000000000000000000000000000000L).toString());
    assertEquals(                               "100000000000000000000000000000000", new BigCharBuffer(33).appendBinStripLeadingZeros(0b0000000000000000000000000000000100000000000000000000000000000000L).toString());
    assertEquals(                              "1000000000000000000000000000000000", new BigCharBuffer(34).appendBinStripLeadingZeros(0b0000000000000000000000000000001000000000000000000000000000000000L).toString());
    assertEquals(                             "10000000000000000000000000000000000", new BigCharBuffer(35).appendBinStripLeadingZeros(0b0000000000000000000000000000010000000000000000000000000000000000L).toString());
    assertEquals(                            "100000000000000000000000000000000000", new BigCharBuffer(36).appendBinStripLeadingZeros(0b0000000000000000000000000000100000000000000000000000000000000000L).toString());
    assertEquals(                           "1000000000000000000000000000000000000", new BigCharBuffer(37).appendBinStripLeadingZeros(0b0000000000000000000000000001000000000000000000000000000000000000L).toString());
    assertEquals(                          "10000000000000000000000000000000000000", new BigCharBuffer(38).appendBinStripLeadingZeros(0b0000000000000000000000000010000000000000000000000000000000000000L).toString());
    assertEquals(                         "100000000000000000000000000000000000000", new BigCharBuffer(39).appendBinStripLeadingZeros(0b0000000000000000000000000100000000000000000000000000000000000000L).toString());
    assertEquals(                        "1000000000000000000000000000000000000000", new BigCharBuffer(40).appendBinStripLeadingZeros(0b0000000000000000000000001000000000000000000000000000000000000000L).toString());
    assertEquals(                       "10000000000000000000000000000000000000000", new BigCharBuffer(41).appendBinStripLeadingZeros(0b0000000000000000000000010000000000000000000000000000000000000000L).toString());
    assertEquals(                      "100000000000000000000000000000000000000000", new BigCharBuffer(42).appendBinStripLeadingZeros(0b0000000000000000000000100000000000000000000000000000000000000000L).toString());
    assertEquals(                     "1000000000000000000000000000000000000000000", new BigCharBuffer(43).appendBinStripLeadingZeros(0b0000000000000000000001000000000000000000000000000000000000000000L).toString());
    assertEquals(                    "10000000000000000000000000000000000000000000", new BigCharBuffer(44).appendBinStripLeadingZeros(0b0000000000000000000010000000000000000000000000000000000000000000L).toString());
    assertEquals(                   "100000000000000000000000000000000000000000000", new BigCharBuffer(45).appendBinStripLeadingZeros(0b0000000000000000000100000000000000000000000000000000000000000000L).toString());
    assertEquals(                  "1000000000000000000000000000000000000000000000", new BigCharBuffer(46).appendBinStripLeadingZeros(0b0000000000000000001000000000000000000000000000000000000000000000L).toString());
    assertEquals(                 "10000000000000000000000000000000000000000000000", new BigCharBuffer(47).appendBinStripLeadingZeros(0b0000000000000000010000000000000000000000000000000000000000000000L).toString());
    assertEquals(                "100000000000000000000000000000000000000000000000", new BigCharBuffer(48).appendBinStripLeadingZeros(0b0000000000000000100000000000000000000000000000000000000000000000L).toString());
    assertEquals(               "1000000000000000000000000000000000000000000000000", new BigCharBuffer(49).appendBinStripLeadingZeros(0b0000000000000001000000000000000000000000000000000000000000000000L).toString());
    assertEquals(              "10000000000000000000000000000000000000000000000000", new BigCharBuffer(50).appendBinStripLeadingZeros(0b0000000000000010000000000000000000000000000000000000000000000000L).toString());
    assertEquals(             "100000000000000000000000000000000000000000000000000", new BigCharBuffer(51).appendBinStripLeadingZeros(0b0000000000000100000000000000000000000000000000000000000000000000L).toString());
    assertEquals(            "1000000000000000000000000000000000000000000000000000", new BigCharBuffer(52).appendBinStripLeadingZeros(0b0000000000001000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(           "10000000000000000000000000000000000000000000000000000", new BigCharBuffer(53).appendBinStripLeadingZeros(0b0000000000010000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(          "100000000000000000000000000000000000000000000000000000", new BigCharBuffer(54).appendBinStripLeadingZeros(0b0000000000100000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(         "1000000000000000000000000000000000000000000000000000000", new BigCharBuffer(55).appendBinStripLeadingZeros(0b0000000001000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(        "10000000000000000000000000000000000000000000000000000000", new BigCharBuffer(56).appendBinStripLeadingZeros(0b0000000010000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(       "100000000000000000000000000000000000000000000000000000000", new BigCharBuffer(57).appendBinStripLeadingZeros(0b0000000100000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(      "1000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(58).appendBinStripLeadingZeros(0b0000001000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(     "10000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(59).appendBinStripLeadingZeros(0b0000010000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(    "100000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(60).appendBinStripLeadingZeros(0b0000100000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(   "1000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(61).appendBinStripLeadingZeros(0b0001000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals(  "10000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(62).appendBinStripLeadingZeros(0b0010000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals( "100000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(63).appendBinStripLeadingZeros(0b0100000000000000000000000000000000000000000000000000000000000000L).toString());
    assertEquals("1000000000000000000000000000000000000000000000000000000000000000", new BigCharBuffer(64).appendBinStripLeadingZeros(0b1000000000000000000000000000000000000000000000000000000000000000L).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#getBinCapacity(long)} method.
   */
  @Test
  public void test_getBinCapacity_long() {
    // @formatter:off
    assertEquals( 1, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000000000L));
    assertEquals( 1, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000000001L));
    assertEquals( 2, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000000010L));
    assertEquals( 3, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000000100L));
    assertEquals( 4, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000001000L));
    assertEquals( 5, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000010000L));
    assertEquals( 6, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000000100000L));
    assertEquals( 7, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000001000000L));
    assertEquals( 8, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000010000000L));
    assertEquals( 9, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000000100000000L));
    assertEquals(10, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000001000000000L));
    assertEquals(11, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000010000000000L));
    assertEquals(12, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000000100000000000L));
    assertEquals(13, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000001000000000000L));
    assertEquals(14, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000010000000000000L));
    assertEquals(15, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000000100000000000000L));
    assertEquals(16, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000001000000000000000L));
    assertEquals(17, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000010000000000000000L));
    assertEquals(18, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000000100000000000000000L));
    assertEquals(19, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000001000000000000000000L));
    assertEquals(20, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000010000000000000000000L));
    assertEquals(21, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000000100000000000000000000L));
    assertEquals(22, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000001000000000000000000000L));
    assertEquals(23, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000010000000000000000000000L));
    assertEquals(24, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000000100000000000000000000000L));
    assertEquals(25, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000001000000000000000000000000L));
    assertEquals(26, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000010000000000000000000000000L));
    assertEquals(27, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000000100000000000000000000000000L));
    assertEquals(28, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000001000000000000000000000000000L));
    assertEquals(29, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000010000000000000000000000000000L));
    assertEquals(30, CharBuffer.getBinCapacity(0b0000000000000000000000000000000000100000000000000000000000000000L));
    assertEquals(31, CharBuffer.getBinCapacity(0b0000000000000000000000000000000001000000000000000000000000000000L));
    assertEquals(32, CharBuffer.getBinCapacity(0b0000000000000000000000000000000010000000000000000000000000000000L));
    assertEquals(33, CharBuffer.getBinCapacity(0b0000000000000000000000000000000100000000000000000000000000000000L));
    assertEquals(34, CharBuffer.getBinCapacity(0b0000000000000000000000000000001000000000000000000000000000000000L));
    assertEquals(35, CharBuffer.getBinCapacity(0b0000000000000000000000000000010000000000000000000000000000000000L));
    assertEquals(36, CharBuffer.getBinCapacity(0b0000000000000000000000000000100000000000000000000000000000000000L));
    assertEquals(37, CharBuffer.getBinCapacity(0b0000000000000000000000000001000000000000000000000000000000000000L));
    assertEquals(38, CharBuffer.getBinCapacity(0b0000000000000000000000000010000000000000000000000000000000000000L));
    assertEquals(39, CharBuffer.getBinCapacity(0b0000000000000000000000000100000000000000000000000000000000000000L));
    assertEquals(40, CharBuffer.getBinCapacity(0b0000000000000000000000001000000000000000000000000000000000000000L));
    assertEquals(41, CharBuffer.getBinCapacity(0b0000000000000000000000010000000000000000000000000000000000000000L));
    assertEquals(42, CharBuffer.getBinCapacity(0b0000000000000000000000100000000000000000000000000000000000000000L));
    assertEquals(43, CharBuffer.getBinCapacity(0b0000000000000000000001000000000000000000000000000000000000000000L));
    assertEquals(44, CharBuffer.getBinCapacity(0b0000000000000000000010000000000000000000000000000000000000000000L));
    assertEquals(45, CharBuffer.getBinCapacity(0b0000000000000000000100000000000000000000000000000000000000000000L));
    assertEquals(46, CharBuffer.getBinCapacity(0b0000000000000000001000000000000000000000000000000000000000000000L));
    assertEquals(47, CharBuffer.getBinCapacity(0b0000000000000000010000000000000000000000000000000000000000000000L));
    assertEquals(48, CharBuffer.getBinCapacity(0b0000000000000000100000000000000000000000000000000000000000000000L));
    assertEquals(49, CharBuffer.getBinCapacity(0b0000000000000001000000000000000000000000000000000000000000000000L));
    assertEquals(50, CharBuffer.getBinCapacity(0b0000000000000010000000000000000000000000000000000000000000000000L));
    assertEquals(51, CharBuffer.getBinCapacity(0b0000000000000100000000000000000000000000000000000000000000000000L));
    assertEquals(52, CharBuffer.getBinCapacity(0b0000000000001000000000000000000000000000000000000000000000000000L));
    assertEquals(53, CharBuffer.getBinCapacity(0b0000000000010000000000000000000000000000000000000000000000000000L));
    assertEquals(54, CharBuffer.getBinCapacity(0b0000000000100000000000000000000000000000000000000000000000000000L));
    assertEquals(55, CharBuffer.getBinCapacity(0b0000000001000000000000000000000000000000000000000000000000000000L));
    assertEquals(56, CharBuffer.getBinCapacity(0b0000000010000000000000000000000000000000000000000000000000000000L));
    assertEquals(57, CharBuffer.getBinCapacity(0b0000000100000000000000000000000000000000000000000000000000000000L));
    assertEquals(58, CharBuffer.getBinCapacity(0b0000001000000000000000000000000000000000000000000000000000000000L));
    assertEquals(59, CharBuffer.getBinCapacity(0b0000010000000000000000000000000000000000000000000000000000000000L));
    assertEquals(60, CharBuffer.getBinCapacity(0b0000100000000000000000000000000000000000000000000000000000000000L));
    assertEquals(61, CharBuffer.getBinCapacity(0b0001000000000000000000000000000000000000000000000000000000000000L));
    assertEquals(62, CharBuffer.getBinCapacity(0b0010000000000000000000000000000000000000000000000000000000000000L));
    assertEquals(63, CharBuffer.getBinCapacity(0b0100000000000000000000000000000000000000000000000000000000000000L));
    assertEquals(64, CharBuffer.getBinCapacity(0b1000000000000000000000000000000000000000000000000000000000000000L));
    // @formatter:on
  }

  // Object to string representation

  /**
   * Tests the {@link CharBuffer#appendNull()} method.
   */
  @Test
  public void test_appendNull() {
    assertEquals("null", new BigCharBuffer(4).appendNull().toString());
  }

  /**
   * Tests the {@link CharBuffer#appendBoolean(boolean)} method.
   */
  @Test
  public void test_appendBoolean() {
    assertEquals("true", new BigCharBuffer(4).appendBoolean(true).toString());
    assertEquals("false", new BigCharBuffer(5).appendBoolean(false).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendBooleanArray(boolean[])} method.
   */
  @Test
  public void test_appendBooleanArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendBooleanArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendBooleanArray(new boolean[0]).toString());
    assertEquals("[true]", new BigCharBuffer(6).appendBooleanArray(new boolean[]{true}).toString());
    assertEquals("[false]", new BigCharBuffer(7).appendBooleanArray(new boolean[]{false}).toString());
    assertEquals("[false, true, false]", new BigCharBuffer(20).appendBooleanArray(new boolean[]{false, true, false}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendByte(byte)} method.
   */
  @Test
  public void test_appendByte() {
    assertEquals("0", new BigCharBuffer(1).appendByte((byte) 0).toString());
    assertEquals("91", new BigCharBuffer(2).appendByte((byte) 91).toString());
    assertEquals("127", new BigCharBuffer(3).appendByte((byte) 127).toString());
    assertEquals("-128", new BigCharBuffer(4).appendByte((byte) -128).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendByteArray(byte[])} method.
   */
  @Test
  public void test_appendByteArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendByteArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendByteArray(new byte[0]).toString());
    assertEquals("[32]", new BigCharBuffer(4).appendByteArray(new byte[]{32}).toString());
    assertEquals("[32, -64, 96]", new BigCharBuffer(13).appendByteArray(new byte[]{32, -64, 96}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendShort(short)} method.
   */
  @Test
  public void test_appendShort() {
    assertEquals("0", new BigCharBuffer(1).appendShort((short) 0).toString());
    assertEquals("-2759", new BigCharBuffer(5).appendShort((short) -2759).toString());
    assertEquals("32767", new BigCharBuffer(5).appendShort((short) 32767).toString());
    assertEquals("-32768", new BigCharBuffer(6).appendShort((short) -32768).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendShortArray(short[])} method.
   */
  @Test
  public void test_appendShortArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendShortArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendShortArray(new short[0]).toString());
    assertEquals("[1096]", new BigCharBuffer(6).appendShortArray(new short[]{1096}).toString());
    assertEquals("[12345, -756, 3742]", new BigCharBuffer(19).appendShortArray(new short[]{12345, -756, 3742}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendInt(int)} method.
   */
  @Test
  public void test_appendInt() {
    assertEquals("0", new BigCharBuffer(1).appendInt(0).toString());
    assertEquals("2775298", new BigCharBuffer(7).appendInt(2775298).toString());
    assertEquals("2147483647", new BigCharBuffer(10).appendInt(2147483647).toString());
    assertEquals("-2147483648", new BigCharBuffer(11).appendInt(-2147483648).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendIntArray(int[])} method.
   */
  @Test
  public void test_appendIntArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendIntArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendIntArray(new int[0]).toString());
    assertEquals("[-92723782]", new BigCharBuffer(11).appendIntArray(new int[]{-92723782}).toString());
    assertEquals("[-82785, 86, -2825698]", new BigCharBuffer(22).appendIntArray(new int[]{-82785, 86, -2825698}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendLong(long)} method.
   */
  @Test
  public void test_appendLong() {
    assertEquals("0L", new BigCharBuffer(2).appendLong(0L).toString());
    assertEquals("298671262137512L", new BigCharBuffer(16).appendLong(298671262137512L).toString());
    assertEquals("9223372036854775807L", new BigCharBuffer(20).appendLong(9223372036854775807L).toString());
    assertEquals("-9223372036854775808L", new BigCharBuffer(21).appendLong(-9223372036854775808L).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendLongArray(long[])} method.
   */
  @Test
  public void test_appendLongArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendLongArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendLongArray(new long[0]).toString());
    assertEquals("[2986792751L]", new BigCharBuffer(13).appendLongArray(new long[]{2986792751L}).toString());
    assertEquals("[-296L, 9965448L, 297520954000L]", new BigCharBuffer(32).appendLongArray(new long[]{-296L, 9965448L, 297520954000L}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendFloat(float)} method.
   */
  @Test
  public void test_appendFloat() {
    assertEquals("NaN", new BigCharBuffer(3).appendFloat(Float.NaN).toString());
    assertEquals("-Infinity", new BigCharBuffer(9).appendFloat(Float.NEGATIVE_INFINITY).toString());
    assertEquals("Infinity", new BigCharBuffer(8).appendFloat(Float.POSITIVE_INFINITY).toString());
    assertEquals("0.0f", new BigCharBuffer(4).appendFloat(0.0f).toString());
    assertEquals("-0.0f", new BigCharBuffer(5).appendFloat(-0.0f).toString());
    assertEquals("16.77777f", new BigCharBuffer(9).appendFloat(16.77777f).toString());
    assertEquals("-98.0f", new BigCharBuffer(6).appendFloat(-98.0f).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendFloatArray(float[])} method.
   */
  @Test
  public void test_appendFloatArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendFloatArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendFloatArray(new float[0]).toString());
    assertEquals("[1.88655f]", new BigCharBuffer(10).appendFloatArray(new float[]{1.88655f}).toString());
    assertEquals("[-75654.22f, NaN, 0.004f]", new BigCharBuffer(28).appendFloatArray(new float[]{-75654.22f, 0.0f / 0.0f, 0.004f}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendDouble(double)} method.
   */
  @Test
  public void test_appendDouble() {
    assertEquals("NaN", new BigCharBuffer(3).appendDouble(Double.NaN).toString());
    assertEquals("-Infinity", new BigCharBuffer(9).appendDouble(Double.NEGATIVE_INFINITY).toString());
    assertEquals("Infinity", new BigCharBuffer(8).appendDouble(Double.POSITIVE_INFINITY).toString());
    assertEquals("0.0d", new BigCharBuffer(4).appendDouble(0.0d).toString());
    assertEquals("-0.0d", new BigCharBuffer(5).appendDouble(-0.0d).toString());
    assertEquals("-0.987579d", new BigCharBuffer(10).appendDouble(-0.987579d).toString());
    assertEquals("2964298.27557d", new BigCharBuffer(14).appendDouble(2964298.27557d).toString());
  }

  /**
   * Tests the {@link CharBuffer#appendDoubleArray(double[])} method.
   */
  @Test
  public void test_appendDoubleArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendDoubleArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendDoubleArray(new double[0]).toString());
    assertEquals("[1.88655d]", new BigCharBuffer(10).appendDoubleArray(new double[]{1.88655d}).toString());
    assertEquals("[NaN, -7.5E-5d, 5437.007d]", new BigCharBuffer(26).appendDoubleArray(new double[]{0.0d / 0.0d, -7.5E-5d, 5437.007d}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendChar(char)} method.
   */
  @Test
  public void test_appendChar() {
    assertEquals("'a'", new BigCharBuffer(3).appendChar('a').toString());
    assertEquals("'\\n'", new BigCharBuffer(4).appendChar('\n').toString());
    assertEquals("'\\u001f'", new BigCharBuffer(8).appendChar('\u001f').toString());
  }

  /**
   * Tests the {@link CharBuffer#appendCharArray(char[])} method.
   */
  @Test
  public void test_appendCharArray() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendCharArray(null).toString());
    assertEquals("[]", new BigCharBuffer(2).appendCharArray(new char[0]).toString());
    assertEquals("['a']", new BigCharBuffer(10).appendCharArray(new char[]{'a'}).toString());
    assertEquals("['a', '\\t', 'b']", new BigCharBuffer(26).appendCharArray(new char[]{'a', '\t', 'b'}).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendString(CharSequence)} method.
   */
  @Test
  public void test_appendString() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendString(null).toString());
    assertEquals("\"\"", new BigCharBuffer(2).appendString("").toString());
    assertEquals("\"a\"", new BigCharBuffer(3).appendString("a").toString());
    assertEquals("\"abc \\u001f \\t\\n\\f\"", new BigCharBuffer(19).appendString("abc \u001f \t\n\f").toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendEnum(Enum)} method.
   */
  @Test
  public void test_appendEnum() {
    // @formatter:off
    assertEquals("null", new BigCharBuffer(4).appendEnum(null).toString());
    assertEquals("TYPE", new BigCharBuffer(4).appendEnum(java.lang.annotation.ElementType.TYPE).toString());
    // @formatter:on
  }

  // Advanced operations

  /**
   * Tests the {@link CharBuffer#appendIdent(int)} method.
   */
  @Test
  public void test_appendIdent_int() {
    assertThrows(IllegalArgumentException.class, () -> new BigCharBuffer().appendIndent(-1));
    // @formatter:off
    assertEquals("",                                    new BigCharBuffer( 1).appendIndent( 0).toString());
    assertEquals(" ",                                   new BigCharBuffer( 1).appendIndent( 1).toString());
    assertEquals("  ",                                  new BigCharBuffer( 2).appendIndent( 2).toString());
    assertEquals("   ",                                 new BigCharBuffer( 3).appendIndent( 3).toString());
    assertEquals("    ",                                new BigCharBuffer( 4).appendIndent( 4).toString());
    assertEquals("     ",                               new BigCharBuffer( 5).appendIndent( 5).toString());
    assertEquals("      ",                              new BigCharBuffer( 6).appendIndent( 6).toString());
    assertEquals("       ",                             new BigCharBuffer( 7).appendIndent( 7).toString());
    assertEquals("        ",                            new BigCharBuffer( 8).appendIndent( 8).toString());
    assertEquals("         ",                           new BigCharBuffer( 9).appendIndent( 9).toString());
    assertEquals("          ",                          new BigCharBuffer(10).appendIndent(10).toString());
    assertEquals("           ",                         new BigCharBuffer(11).appendIndent(11).toString());
    assertEquals("            ",                        new BigCharBuffer(12).appendIndent(12).toString());
    assertEquals("             ",                       new BigCharBuffer(13).appendIndent(13).toString());
    assertEquals("              ",                      new BigCharBuffer(14).appendIndent(14).toString());
    assertEquals("               ",                     new BigCharBuffer(15).appendIndent(15).toString());
    assertEquals("                ",                    new BigCharBuffer(16).appendIndent(16).toString());
    assertEquals("                 ",                   new BigCharBuffer(17).appendIndent(17).toString());
    assertEquals("                  ",                  new BigCharBuffer(18).appendIndent(18).toString());
    assertEquals("                   ",                 new BigCharBuffer(19).appendIndent(19).toString());
    assertEquals("                    ",                new BigCharBuffer(20).appendIndent(20).toString());
    assertEquals("                     ",               new BigCharBuffer(21).appendIndent(21).toString());
    assertEquals("                      ",              new BigCharBuffer(22).appendIndent(22).toString());
    assertEquals("                       ",             new BigCharBuffer(23).appendIndent(23).toString());
    assertEquals("                        ",            new BigCharBuffer(24).appendIndent(24).toString());
    assertEquals("                         ",           new BigCharBuffer(25).appendIndent(25).toString());
    assertEquals("                          ",          new BigCharBuffer(26).appendIndent(26).toString());
    assertEquals("                           ",         new BigCharBuffer(27).appendIndent(27).toString());
    assertEquals("                            ",        new BigCharBuffer(28).appendIndent(28).toString());
    assertEquals("                             ",       new BigCharBuffer(29).appendIndent(29).toString());
    assertEquals("                              ",      new BigCharBuffer(30).appendIndent(30).toString());
    assertEquals("                               ",     new BigCharBuffer(31).appendIndent(31).toString());
    assertEquals("                                ",    new BigCharBuffer(32).appendIndent(32).toString());
    assertEquals("                                 ",   new BigCharBuffer(33).appendIndent(33).toString());
    assertEquals("                                  ",  new BigCharBuffer(34).appendIndent(34).toString());
    assertEquals("                                   ", new BigCharBuffer(35).appendIndent(35).toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendIdent(int, char)} method.
   */
  @Test
  public void test_appendIdent_int_char() {
    assertThrows(IllegalArgumentException.class, () -> new BigCharBuffer().appendIndent(-1, '_'));
    // @formatter:off
    assertEquals("",                                    new BigCharBuffer( 1).appendIndent( 0, '0').toString());
    assertEquals("1",                                   new BigCharBuffer( 1).appendIndent( 1, '1').toString());
    assertEquals("22",                                  new BigCharBuffer( 2).appendIndent( 2, '2').toString());
    assertEquals("333",                                 new BigCharBuffer( 3).appendIndent( 3, '3').toString());
    assertEquals("4444",                                new BigCharBuffer( 4).appendIndent( 4, '4').toString());
    assertEquals("55555",                               new BigCharBuffer( 5).appendIndent( 5, '5').toString());
    assertEquals("666666",                              new BigCharBuffer( 6).appendIndent( 6, '6').toString());
    assertEquals("7777777",                             new BigCharBuffer( 7).appendIndent( 7, '7').toString());
    assertEquals("88888888",                            new BigCharBuffer( 8).appendIndent( 8, '8').toString());
    assertEquals("999999999",                           new BigCharBuffer( 9).appendIndent( 9, '9').toString());
    assertEquals("aaaaaaaaaa",                          new BigCharBuffer(10).appendIndent(10, 'a').toString());
    assertEquals("bbbbbbbbbbb",                         new BigCharBuffer(11).appendIndent(11, 'b').toString());
    assertEquals("cccccccccccc",                        new BigCharBuffer(12).appendIndent(12, 'c').toString());
    assertEquals("ddddddddddddd",                       new BigCharBuffer(13).appendIndent(13, 'd').toString());
    assertEquals("eeeeeeeeeeeeee",                      new BigCharBuffer(14).appendIndent(14, 'e').toString());
    assertEquals("fffffffffffffff",                     new BigCharBuffer(15).appendIndent(15, 'f').toString());
    assertEquals("gggggggggggggggg",                    new BigCharBuffer(16).appendIndent(16, 'g').toString());
    assertEquals("hhhhhhhhhhhhhhhhh",                   new BigCharBuffer(17).appendIndent(17, 'h').toString());
    assertEquals("iiiiiiiiiiiiiiiiii",                  new BigCharBuffer(18).appendIndent(18, 'i').toString());
    assertEquals("jjjjjjjjjjjjjjjjjjj",                 new BigCharBuffer(19).appendIndent(19, 'j').toString());
    assertEquals("kkkkkkkkkkkkkkkkkkkk",                new BigCharBuffer(20).appendIndent(20, 'k').toString());
    assertEquals("lllllllllllllllllllll",               new BigCharBuffer(21).appendIndent(21, 'l').toString());
    assertEquals("mmmmmmmmmmmmmmmmmmmmmm",              new BigCharBuffer(22).appendIndent(22, 'm').toString());
    assertEquals("nnnnnnnnnnnnnnnnnnnnnnn",             new BigCharBuffer(23).appendIndent(23, 'n').toString());
    assertEquals("oooooooooooooooooooooooo",            new BigCharBuffer(24).appendIndent(24, 'o').toString());
    assertEquals("ppppppppppppppppppppppppp",           new BigCharBuffer(25).appendIndent(25, 'p').toString());
    assertEquals("qqqqqqqqqqqqqqqqqqqqqqqqqq",          new BigCharBuffer(26).appendIndent(26, 'q').toString());
    assertEquals("rrrrrrrrrrrrrrrrrrrrrrrrrrr",         new BigCharBuffer(27).appendIndent(27, 'r').toString());
    assertEquals("ssssssssssssssssssssssssssss",        new BigCharBuffer(28).appendIndent(28, 's').toString());
    assertEquals("ttttttttttttttttttttttttttttt",       new BigCharBuffer(29).appendIndent(29, 't').toString());
    assertEquals("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuu",      new BigCharBuffer(30).appendIndent(30, 'u').toString());
    assertEquals("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv",     new BigCharBuffer(31).appendIndent(31, 'v').toString());
    assertEquals("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww",    new BigCharBuffer(32).appendIndent(32, 'w').toString());
    assertEquals("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",   new BigCharBuffer(33).appendIndent(33, 'x').toString());
    assertEquals("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy",  new BigCharBuffer(34).appendIndent(34, 'y').toString());
    assertEquals("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz", new BigCharBuffer(35).appendIndent(35, 'z').toString());
    // @formatter:on
  }

  /**
   * Tests the {@link CharBuffer#appendIdent(int, int)} method.
   */
  @Test
  public void test_appendIdent_int_int() {
    final int ch = 0x2070e;
    // @formatter:off
    assertThrows(IllegalArgumentException.class, () -> new BigCharBuffer().appendIndent(-1, ch));
    assertEquals( 0, new BigCharBuffer( 1).appendIndent( 0, ch).length());
    assertEquals( 2, new BigCharBuffer( 2).appendIndent( 1, ch).length());
    assertEquals( 4, new BigCharBuffer( 4).appendIndent( 2, ch).length());
    assertEquals( 6, new BigCharBuffer( 6).appendIndent( 3, ch).length());
    assertEquals( 8, new BigCharBuffer( 8).appendIndent( 4, ch).length());
    assertEquals(10, new BigCharBuffer(10).appendIndent( 5, ch).length());
    assertEquals(12, new BigCharBuffer(12).appendIndent( 6, ch).length());
    assertEquals(14, new BigCharBuffer(14).appendIndent( 7, ch).length());
    assertEquals(16, new BigCharBuffer(16).appendIndent( 8, ch).length());
    assertEquals(18, new BigCharBuffer(18).appendIndent( 9, ch).length());
    assertEquals(20, new BigCharBuffer(20).appendIndent(10, ch).length());
    assertEquals(22, new BigCharBuffer(22).appendIndent(11, ch).length());
    assertEquals(24, new BigCharBuffer(24).appendIndent(12, ch).length());
    assertEquals(26, new BigCharBuffer(26).appendIndent(13, ch).length());
    assertEquals(28, new BigCharBuffer(28).appendIndent(14, ch).length());
    assertEquals(30, new BigCharBuffer(30).appendIndent(15, ch).length());
    assertEquals(32, new BigCharBuffer(32).appendIndent(16, ch).length());
    assertEquals(34, new BigCharBuffer(34).appendIndent(17, ch).length());
    assertEquals(36, new BigCharBuffer(36).appendIndent(18, ch).length());
    assertEquals(38, new BigCharBuffer(38).appendIndent(19, ch).length());
    assertEquals(40, new BigCharBuffer(40).appendIndent(20, ch).length());
    assertEquals(42, new BigCharBuffer(42).appendIndent(21, ch).length());
    assertEquals(44, new BigCharBuffer(44).appendIndent(22, ch).length());
    assertEquals(46, new BigCharBuffer(46).appendIndent(23, ch).length());
    assertEquals(48, new BigCharBuffer(48).appendIndent(24, ch).length());
    assertEquals(50, new BigCharBuffer(50).appendIndent(25, ch).length());
    // @formatter:on
  }

}
