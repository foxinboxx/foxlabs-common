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
//import org.junit.Ignore;
import org.junit.Assert;

/**
 * Performance tests for {@link CharBuffer} class.
 *
 * @author Fox Mulder
 */
//@Ignore("Not a usual unit tests")
public class CharBufferPerformanceTest {

  /**
   * Tests performance of the {@link CharBuffer#append(char)} method against {@link StringBuilder}.
   */
  @Test
  public void test_append_char() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append((char) n);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.append((char) n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.append((char) n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(char)    : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.append(char) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.append(char)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#append(int)} method against {@link StringBuilder}.
   */
  @Test
  public void test_append_int() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      if (Character.isValidCodePoint(n)) {
        sb.appendCodePoint(n);
      }
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      if (Character.isValidCodePoint(n)) {
        scb.append(n);
      }
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      if (Character.isValidCodePoint(n)) {
        bcb.append(n);
      }
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.appendCodePoint(int) : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.append(int)       : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.append(int)          : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#append(CharSequence)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_append_CharSequence() {
    final int loop = 100000000;
    final CharSequence sequence = "test";
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(sequence);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.append(sequence);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.append(sequence);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(CharSequence)    : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.append(CharSequence) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.append(CharSequence)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBool(boolean)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBool() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(n % 10 == 0);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendBool(n % 10 == 0);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendBool(n % 10 == 0);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(boolean)        : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendBool(boolean) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendBool(boolean)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_int() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(n);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendDec(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendDec(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(int)       : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendDec(int) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendDec(int)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_long() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (long n = 0L; n < loop; n++) {
      sb.append(n);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (long n = 0L; n < loop; n++) {
      scb.appendDec(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (long n = 0L; n < loop; n++) {
      bcb.appendDec(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(long)       : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendDec(long) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendDec(long)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(float)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_float() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append((float) n);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendDec((float) n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendDec((float) n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(float)       : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendDec(float) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendDec(float)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(double)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_double() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append((double) n);
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendDec((double) n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendDec((double) n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(double)       : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendDec(double) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendDec(double)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendHexStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendHex_int() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(Integer.toHexString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendHexStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendHexStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Integer.toHexString(int))   : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendHexStripLeadingZeros(int) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendHexStripLeadingZeros(int)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendHexStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendHex_long() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (long n = 0L; n < loop; n++) {
      sb.append(Long.toHexString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (long n = 0L; n < loop; n++) {
      scb.appendHexStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (long n = 0L; n < loop; n++) {
      bcb.appendHexStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Long.toHexString(long))      : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendHexStripLeadingZeros(long) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendHexStripLeadingZeros(long)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendOctStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendOct_int() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(Integer.toOctalString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendOctStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendOctStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Integer.toOctalString(int)) : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendOctStripLeadingZeros(int) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendOctStripLeadingZeros(int)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendOctStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendOct_long() {
    final int loop = 100000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (long n = 0L; n < loop; n++) {
      sb.append(Long.toOctalString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (long n = 0L; n < loop; n++) {
      scb.appendOctStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (long n = 0L; n < loop; n++) {
      bcb.appendOctStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Long.toOctalString(long))    : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendOctStripLeadingZeros(long) : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendOctStripLeadingZeros(long)    : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBinStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBin_int() {
    final int loop = 10000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (int n = 0; n < loop; n++) {
      sb.append(Integer.toBinaryString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (int n = 0; n < loop; n++) {
      scb.appendBinStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (int n = 0; n < loop; n++) {
      bcb.appendBinStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Integer.toBinaryString(int)) : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendBinStripLeadingZeros(int)  : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendBinStripLeadingZeros(int)     : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBinStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBin_long() {
    final int loop = 10000000;
    // StringBuilder
    long jsbTime = System.currentTimeMillis();
    final StringBuilder sb = new StringBuilder(32);
    for (long n = 0L; n < loop; n++) {
      sb.append(Long.toBinaryString(n));
    }
    jsbTime = System.currentTimeMillis() - jsbTime;
    // SimpleCharBuffer
    long scbTime = System.currentTimeMillis();
    final SimpleCharBuffer scb = new SimpleCharBuffer();
    for (long n = 0L; n < loop; n++) {
      scb.appendBinStripLeadingZeros(n);
    }
    scbTime = System.currentTimeMillis() - scbTime;
    // BigCharBuffer
    long bcbTime = System.currentTimeMillis();
    final BigCharBuffer bcb = new BigCharBuffer();
    for (long n = 0L; n < loop; n++) {
      bcb.appendBinStripLeadingZeros(n);
    }
    bcbTime = System.currentTimeMillis() - bcbTime;
    // Results
    System.out.printf("StringBuilder.append(Long.toBinaryString(long))    : %dms\n", jsbTime);
    System.out.printf("SimpleCharBuffer.appendBinStripLeadingZeros(long)  : %dms\n", scbTime);
    System.out.printf("BigCharBuffer.appendBinStripLeadingZeros(long)     : %dms\n", bcbTime);
    assertFaster(jsbTime, scbTime);
  }

  /**
   * Expects that {@link SimpleCharBuffer} is faster than {@code StringBuilder}.
   */
  private static void assertFaster(double jsbTime, double scbTime) {
    if (jsbTime < scbTime) {
      final double rate = scbTime / jsbTime;
      System.out.printf("[-] %s times slower!\n\n", rate);
      Assert.fail(rate + " times slower!");
    } else {
      final double rate = jsbTime / scbTime;
      System.out.printf("[+] %s times faster!\n\n", rate);
    }
  }

}
