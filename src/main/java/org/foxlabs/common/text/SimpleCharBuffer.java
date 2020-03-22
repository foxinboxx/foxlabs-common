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

public class SimpleCharBuffer extends CharBuffer {

  public static final int MAX_THRESHOLD = Integer.MAX_VALUE - 8;

  private char[] buffer;

  private int length;

  public SimpleCharBuffer() {
    this(MAX_THRESHOLD);
  }

  public SimpleCharBuffer(int threshold) {
    super(Math.min(threshold, MAX_THRESHOLD));
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  protected char getChar(int index) {
    return buffer[index];
  }

  @Override
  protected void copyChars(int start, int end, char[] target, int offset) {
    System.arraycopy(buffer, start, target, offset, end - start);
  }

  @Override
  protected void extendCapacity(int nlength) {
    if (nlength > buffer.length) {
      final char[] copy = new char[Math.min(nlength << 2, threshold)];
      System.arraycopy(buffer, 0, copy, 0, buffer.length);
      buffer = copy;
    }
  }

  @Override
  protected CharBuffer appendChar(char ch) {
    buffer[length++] = ch;
    return this;
  }

  @Override
  protected CharBuffer appendSequence(GetChars sequence, int start, int end) {
    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public void reset() {
    length = 0;
  }

  @Override
  public void clear() {
    length = 0;
  }

  @Override
  protected String toString(int start, int end) {
    return new String(buffer, start, end);
  }

}
