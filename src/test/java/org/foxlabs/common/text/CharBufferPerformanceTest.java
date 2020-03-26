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
   * Number of iterations for each test.
   */
  private static final int ITERATION_COUNT = 1000000;

  /**
   * A coefficient to multiply loop count in each test.
   */
  private static final double LOOP_FACTOR = 1.0;

  /**
   * Tests performance of the {@link CharBuffer#append(char)} method against {@link StringBuilder}.
   */
  @Test
  public void test_append_char() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append((char) n);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.append((char) n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.append((char) n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(char)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#append(int)} method against {@link StringBuilder}.
   */
  @Test
  public void test_append_int() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        if (Character.isValidCodePoint(n)) {
          sb.appendCodePoint(n);
        }
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        if (Character.isValidCodePoint(n)) {
          lcb.append(n);
        }
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        if (Character.isValidCodePoint(n)) {
          pcb.append(n);
        }
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("appendCodePoint(int) / append(int)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#append(CharSequence)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_append_CharSequence() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      final CharSequence sequence = "test";
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(sequence);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.append(sequence);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.append(sequence);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(CharSequence)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBool(boolean)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBool() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(n % 10 == 0);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendBool(n % 10 == 0);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendBool(n % 10 == 0);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(boolan) / appendBool(boolean)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_int() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(n);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendDec(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendDec(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(int) / appendDec(int)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_long() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        sb.append(n);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        lcb.appendDec(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        pcb.appendDec(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(long) / appendDec(long)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(float)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_float() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append((float) n);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendDec((float) n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendDec((float) n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(float) / appendDec(float)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendDec(double)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendDec_double() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append((double) n);
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendDec((double) n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendDec((double) n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(double) / appendDec(double)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendHexStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendHex_int() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(Integer.toHexString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendHexStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendHexStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Integer.toHexString(int)) / appendHex(int)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendHexStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendHex_long() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        sb.append(Long.toHexString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        lcb.appendHexStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        pcb.appendHexStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Long.toHexString(long)) / appendHex(long)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendOctStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendOct_int() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(Integer.toOctalString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendOctStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendOctStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Integer.toOctalString(int)) / appendOct(int)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendOctStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendOct_long() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        sb.append(Long.toOctalString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        lcb.appendOctStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        pcb.appendOctStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Long.toOctalString(long)) / appendOct(long)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBinStripLeadingZeros(int)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBin_int() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        sb.append(Integer.toBinaryString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        lcb.appendBinStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (int n = 0; n < loopCount; n++) {
        pcb.appendBinStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Integer.toBinaryString(int)) / appendBin(int)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Tests performance of the {@link CharBuffer#appendBinStripLeadingZeros(long)} method against
   * {@link StringBuilder}.
   */
  @Test
  public void test_appendBin_long() {
    final int loopCount = (int) (10000 * LOOP_FACTOR);
    long startTime, jsbTime = 0L, lcbTime = 0L, pcbTime = 0L;
    for (int i = 0; i < ITERATION_COUNT; i++) {
      // StringBuilder
      final StringBuilder sb = new StringBuilder(32);
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        sb.append(Long.toBinaryString(n));
      }
      jsbTime += System.nanoTime() - startTime;
      // SimpleCharBuffer
      final LinearCharBuffer lcb = new LinearCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        lcb.appendBinStripLeadingZeros(n);
      }
      lcbTime += System.nanoTime() - startTime;
      // BigCharBuffer
      final PaginalCharBuffer pcb = new PaginalCharBuffer();
      startTime = System.nanoTime();
      for (long n = 0L; n < loopCount; n++) {
        pcb.appendBinStripLeadingZeros(n);
      }
      pcbTime += System.nanoTime() - startTime;
    }
    printResults("append(Long.toBinaryString(long)) / appendBin(long)", jsbTime, lcbTime, pcbTime, loopCount);
  }

  /**
   * Prints performance results and fails a test if {@link LinearCharBuffer} is slower than
   * {@link StringBuilder}.
   */
  private static void printResults(String methodName, long jsbTime, long lcbTime, long pcbTime, long loopCount) {
    // calculate parameters
    double jsbAvg = jsbTime / ((double) ITERATION_COUNT * loopCount);
    double lcbAvg = lcbTime / ((double) ITERATION_COUNT * loopCount);
    double pcbAvg = pcbTime / ((double) ITERATION_COUNT * loopCount);
    double lcbRate = (double) jsbTime / (double) lcbTime;
    double pcbRate = (double) jsbTime / (double) pcbTime;
    String lcbFaster = "FASTER";
    char lcbSign = '+';
    if (lcbRate < 1.0) {
      lcbRate = 1.0 / lcbRate;
      lcbFaster = "slower";
      lcbSign = '-';
    }
    String pcbFaster = "FASTER";
    char pcbSign = '+';
    if (pcbRate < 1.0) {
      pcbRate = 1.0 / pcbRate;
      pcbFaster = "slower";
      pcbSign = '-';
    }
    // print results
    System.out.printf("METHOD                : %s\n", methodName);
    System.out.printf("Samples               : CALLS = %,d\tLOOPS = %,d\n", loopCount * ITERATION_COUNT, loopCount);
    System.out.printf("StringBuilder         : TOTAL = %s ns\tAVG = %.10f ns\n", jsbTime, jsbAvg);
    System.out.printf("LinearCharBuffer      : TOTAL = %s ns\tAVG = %.10f ns\n", lcbTime, lcbAvg);
    System.out.printf("PaginalCharBuffer     : TOTAL = %s ns\tAVG = %.10f ns\n", pcbTime, pcbAvg);
    System.out.printf("[%s] LinearCharBuffer  : %.6f times %s!\n", lcbSign, lcbRate, lcbFaster);
    System.out.printf("[%s] PaginalCharBuffer : %.6f times %s!\n", pcbSign, pcbRate, pcbFaster);
    System.out.printf("%s!\n\n", jsbTime < lcbTime ? "FAILED" : "SUCCEED");
    // fail if slower
    if (jsbTime < lcbTime) {
      Assert.fail(lcbRate + " times slower!");
    }
  }

}
