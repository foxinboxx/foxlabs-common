/* 
 * Copyright (C) 2012 FoxLabs
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

package org.foxlabs.util;

/**
 * This class defines mutable sequence of bytes. It is like a byte array, but
 * can be modified.
 * 
 * @author Fox Mulder
 */
public class ByteBuffer implements java.io.Serializable {
    private static final long serialVersionUID = 2395415332291976964L;
    
    /**
     * The buffer where data is stored.
     */
    protected byte[] data;
    
    /**
     * The number of valid bytes in this buffer.
     */
    protected int length;
    
    /**
     * Constructs a new <code>ByteBuffer</code> with default initial capacity.
     */
    public ByteBuffer() {
        this(512);
    }
    
    /**
     * Constructs a new <code>ByteBuffer</code> with the specified initial
     * capacity.
     * 
     * @param capacity Initial capacity of the buffer.
     */
    public ByteBuffer(int capacity) {
        data = new byte[capacity];
    }
    
    /**
     * Constructs a new <code>ByteBuffer</code> with the specified initial data.
     * 
     * @param array Initial data.
     */
    public ByteBuffer(byte[] array) {
        this(array, 0, array.length);
    }
    
    /**
     * Constructs a new <code>ByteBuffer</code> with the specified initial data.
     * 
     * @param array Initial data.
     * @param offset Start offset in the data.
     * @param count Number of bytes to copy.
     */
    public ByteBuffer(byte[] array, int offset, int count) {
        System.arraycopy(array, offset, data = new byte[length = count], 0, count);
    }
    
    /**
     * Returns the number of valid bytes in this buffer.
     * 
     * @return The number of valid bytes in this buffer.
     */
    public int length() {
        return length;
    }
    
    /**
     * Appends the specified <code>byte</code> value to this buffer.
     * 
     * @param value <code>byte</code> value to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(byte value) {
        ensureCapacity(length + 1);
        data[length++] = value;
        return this;
    }
    
    /**
     * Appends the specified <code>byte[]</code> array to this buffer.
     * 
     * @param array <code>byte[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(byte[], int, int)
     */
    public ByteBuffer append(byte[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>byte[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>byte[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(byte[] array, int offset, int count) {
        ensureCapacity(length + count);
        System.arraycopy(array, offset, data, length, count);
        length += count;
        return this;
    }

    /**
     * Appends the specified <code>short</code> value to this buffer as two
     * bytes, high byte first.
     * 
     * @param value <code>short</code> value to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(short value) {
        ensureCapacity(length + 2);
        data[length++] = (byte) ((value >>> 8) & 0xff);
        data[length++] = (byte) ((value >>> 0) & 0xff);
        return this;
    }
    
    /**
     * Appends the specified <code>short[]</code> array to this buffer.
     * 
     * @param array <code>short[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(short[], int, int)
     */
    public ByteBuffer append(short[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>short[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>short[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(short)
     */
    public ByteBuffer append(short[] array, int offset, int count) {
        ensureCapacity(length + count * 2);
        for (int i = 0; i < count; i++)
            append(array[offset + i]);
        return this;
    }
    
    /**
     * Appends the specified <code>int</code> value to this buffer as four
     * bytes, high byte first.
     * 
     * @param value <code>int</code> value to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(int value) {
        ensureCapacity(length + 4);
        data[length++] = (byte) ((value >>> 24) & 0xff);
        data[length++] = (byte) ((value >>> 16) & 0xff);
        data[length++] = (byte) ((value >>>  8) & 0xff);
        data[length++] = (byte) ((value >>>  0) & 0xff);
        return this;
    }
    
    /**
     * Appends the specified <code>int[]</code> array to this buffer.
     * 
     * @param array <code>int[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(int[], int, int)
     */
    public ByteBuffer append(int[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>int[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>int[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(int)
     */
    public ByteBuffer append(int[] array, int offset, int count) {
        ensureCapacity(length + count * 4);
        for (int i = 0; i < count; i++)
            append(array[offset + i]);
        return this;
    }
    
    /**
     * Appends the specified <code>long</code> value to this buffer as eight
     * bytes, high byte first.
     * 
     * @param value <code>long</code> value to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(long value) {
        ensureCapacity(length + 8);
        data[length++] = (byte) ((value >>> 56) & 0xff);
        data[length++] = (byte) ((value >>> 48) & 0xff);
        data[length++] = (byte) ((value >>> 40) & 0xff);
        data[length++] = (byte) ((value >>> 32) & 0xff);
        data[length++] = (byte) ((value >>> 24) & 0xff);
        data[length++] = (byte) ((value >>> 16) & 0xff);
        data[length++] = (byte) ((value >>>  8) & 0xff);
        data[length++] = (byte) ((value >>>  0) & 0xff);
        return this;
    }
    
    /**
     * Appends the specified <code>long[]</code> array to this buffer.
     * 
     * @param array <code>long[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(long[], int, int)
     */
    public ByteBuffer append(long[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>long[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>long[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(long)
     */
    public ByteBuffer append(long[] array, int offset, int count) {
        ensureCapacity(length + count * 8);
        for (int i = 0; i < count; i++)
            append(array[offset + i]);
        return this;
    }
    
    /**
     * Appends the specified <code>float</code> value to this buffer as a
     * 4-byte quantity, high byte first.
     * 
     * @param value <code>float</code> value to append.
     * @return Reference to this buffer instance.
     * @see #append(int)
     */
    public ByteBuffer append(float value) {
        return append(Float.floatToIntBits(value));
    }
    
    /**
     * Appends the specified <code>float[]</code> array to this buffer.
     * 
     * @param array <code>float[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(float[], int, int)
     */
    public ByteBuffer append(float[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>float[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>float[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(int)
     */
    public ByteBuffer append(float[] array, int offset, int count) {
        ensureCapacity(length + count * 4);
        for (int i = 0; i < count; i++)
            append(Float.floatToIntBits(array[offset + i]));
        return this;
    }
    
    /**
     * Appends the specified <code>double</code> value to this buffer as a
     * 8-byte quantity, high byte first.
     * 
     * @param value <code>double</code> value to append.
     * @return Reference to this buffer instance.
     * @see #append(long)
     */
    public ByteBuffer append(double value) {
        return append(Double.doubleToLongBits(value));
    }
    
    /**
     * Appends the specified <code>double[]</code> array to this buffer.
     * 
     * @param array <code>double[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(double[], int, int)
     */
    public ByteBuffer append(double[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>double[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>double[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(long)
     */
    public ByteBuffer append(double[] array, int offset, int count) {
        ensureCapacity(length + count * 8);
        for (int i = 0; i < count; i++)
            append(Double.doubleToLongBits(array[offset + i]));
        return this;
    }
    
    /**
     * Appends the specified <code>char</code> value to this buffer as a
     * 2-byte value, high byte first.
     * 
     * @param value <code>char</code> value to append.
     * @return Reference to this buffer instance.
     * @see #append(short)
     */
    public ByteBuffer append(char value) {
        return append((short) value);
    }
    
    /**
     * Appends the specified <code>char[]</code> array to this buffer.
     * 
     * @param array <code>char[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(char[], int, int)
     */
    public ByteBuffer append(char[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>char[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>char[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(short)
     */
    public ByteBuffer append(char[] array, int offset, int count) {
        ensureCapacity(length + count * 8);
        for (int i = 0; i < count; i++)
            append((short) array[offset + i]);
        return this;
    }
    
    /**
     * Appends the specified <code>boolean</code> value to this buffer as a
     * 1-byte value. The value <code>true</code> is appended as <code>1</code>,
     * the value <code>false</code> is appended as <code>0</code>.
     * 
     * @param value <code>boolean</code> value to append.
     * @return Reference to this buffer instance.
     * @see #append(byte)
     */
    public ByteBuffer append(boolean value) {
        return append(value ? (byte) 1 : (byte) 0);
    }
    
    /**
     * Appends the specified <code>boolean[]</code> array to this buffer.
     * 
     * @param array <code>boolean[]</code> array to append.
     * @return Reference to this buffer instance.
     * @see #append(boolean[], int, int)
     */
    public ByteBuffer append(boolean[] array) {
        return append(array, 0, array.length);
    }
    
    /**
     * Appends the specified number of elements from the specified
     * <code>boolean[]</code> array starting from the specified offset to this
     * buffer.
     * 
     * @param array <code>boolean[]</code> array to append.
     * @param offset Start offset in the specified array.
     * @param count Number of elements to append.
     * @return Reference to this buffer instance.
     * @see #append(byte)
     */
    public ByteBuffer append(boolean[] array, int offset, int count) {
        ensureCapacity(length + count * 8);
        for (int i = 0; i < count; i++)
            append(array[offset + i] ? (byte) 1 : (byte) 0);
        return this;
    }
    
    /**
     * Appends all the data from the specified buffer to this buffer.
     * 
     * @param buffer Buffer to append.
     * @return Reference to this buffer instance.
     */
    public ByteBuffer append(ByteBuffer buffer) {
        return append(buffer.data, 0, buffer.length);
    }
    
    /**
     * Clears this buffer.
     */
    public void reset() {
        length = 0;
    }
    
    /**
     * Increases the capacity of this buffer if necessary.
     * 
     * @param capacity Desired minimum capacity.
     */
    public void ensureCapacity(int capacity) {
        if (capacity > data.length) {
            int len = (data.length * 3) / 2 + 1;
            byte[] buf = new byte[len > capacity ? len : capacity];
            System.arraycopy(data, 0, buf, 0, length);
            data = buf;
        }
    }
    
    /**
     * Returns array of bytes from this buffer.
     * 
     * @return Array of bytes from this buffer.
     */
    public byte[] getBytes() {
        byte[] buf = new byte[length];
        System.arraycopy(data, 0, buf, 0, buf.length);
        return buf;
    }
    
    /**
     * Serializes this buffer data.
     */
    private void writeObject(java.io.ObjectOutputStream stream)
            throws java.io.IOException {
        stream.defaultWriteObject();
        stream.writeInt(length);
        stream.write(data, 0, length);
    }
    
    /**
     * Deserializes this buffer data.
     */
    private void readObject(java.io.ObjectInputStream stream)
            throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
        data = new byte[stream.readInt()];
        length = stream.read(data);
        if (length != data.length)
            throw new java.io.IOException();
    }
    
}
