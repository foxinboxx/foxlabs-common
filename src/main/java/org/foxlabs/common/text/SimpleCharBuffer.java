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
    super(threshold);
  }

  @Override
  public char charAt(int index) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  protected void doGetChars(int start, int end, char[] target, int offset) {
    // TODO Auto-generated method stub
  }

  @Override
  public int ensureCapacity(int count) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected CharBuffer doAppend(char ch) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected CharBuffer doAppend(GetChars sequence, int start, int end) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void reset() {
    length = 0;
  }

  @Override
  public void clear() {
    length = 0;
  }

}
