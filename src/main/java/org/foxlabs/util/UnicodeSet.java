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

import java.util.Arrays;

import org.foxlabs.common.Strings;
import org.foxlabs.common.function.ToString;

/**
 * This class represents a set of unicode characters and provides operations
 * on it.
 * 
 * @author Fox Mulder
 */
public final class UnicodeSet extends ToString.Adapter implements java.io.Serializable {
    private static final long serialVersionUID = 8609572179021840846L;
    
    /**
     * The minimum value of a unicode code point.
     */
    public static final int MIN_ELEMENT = Character.MIN_CODE_POINT;
    
    /**
     * The maximum value of a unicode code point.
     */
    public static final int MAX_ELEMENT = Character.MAX_CODE_POINT;
    
    /**
     * Empty character set.
     */
    public static final UnicodeSet EMPTY = new UnicodeSet(new int[0], new int[0]);
    
    /**
     * Character set that contains all unicode characters.
     */
    public static final UnicodeSet WHOLE = new UnicodeSet(new int[]{MIN_ELEMENT}, new int[]{MAX_ELEMENT});
    
    /**
     * Character set that contains all ASCII characters.
     */
    public static final UnicodeSet ASCII = new UnicodeSet(new int[]{MIN_ELEMENT}, new int[]{0x7f});
    
    /**
     * Character set that contains ASCII digits.
     */
    public static final UnicodeSet DIGIT = new UnicodeSet('0', '9');
    
    /**
     * Character set that contains ASCII letters.
     */
    public static final UnicodeSet ALPHA = new UnicodeSet(new int[]{'A', 'a'}, new int[]{'Z', 'z'});
    
    /**
     * Character set that contains alphanumeric characters.
     */
    public static final UnicodeSet ALNUM = DIGIT.union(ALPHA);
    
    /**
     * Character set that contains ASCII whitespace characters.
     */
    public static final UnicodeSet SPACE = fromElements(" \t\n\r\f\b");
    
    /**
     * Character set that contains printable ASCII characters.
     */
    public static final UnicodeSet PRINT = unionAll(ALNUM, SPACE, fromElements("!\"#$%&\'()*+,-./:;<=>?@[\\]^_`{|}~"));
    
    /**
     * Array of minimum bounds.
     */
    private int[] min;
    
    /**
     * Array of maximum bounds.
     */
    private int[] max;
    
    /**
     * Inverse character set for this character set.
     */
    private transient UnicodeSet inverse;
    
    /**
     * Cache the hash code for this character set.
     */
    private transient int hash;
    
    /**
     * Constructs a new <code>UnicodeSet</code> that contains the specified
     * characters interval.
     * 
     * @param min Minimum bound.
     * @param max Maximum bound.
     */
    private UnicodeSet(int min, int max) {
        this(new int[]{min}, new int[]{max});
    }
    
    /**
     * Constructs a new <code>UnicodeSet</code> that contains the specified
     * characters intervals.
     * 
     * @param min Array of minimum bounds.
     * @param max Array of maximum bounds.
     */
    private UnicodeSet(int[] min, int[] max) {
        this.min = min;
        this.max = max;
    }
    
    /**
     * Returns number of intervals for this character set.
     * 
     * @return Number of intervals for this character set.
     */
    public int size() {
        return min.length;
    }
    
    /**
     * Returns minimum bound for this character set.
     * 
     * @return Minimum bound for this character set.
     */
    public int getMin() {
        return min.length > 0 ? min[0] : -1;
    }
    
    /**
     * Returns maximum bound for this character set.
     * 
     * @return Maximum bound for this character set.
     */
    public int getMax() {
        return max.length > 0 ? max[max.length - 1] : -1;
    }
    
    /**
     * Determines if this character set represents a set of single characters.
     * 
     * @return <code>true</code> if this character set represents a set of
     *         single characters; <code>false</code> otherwise.
     */
    public boolean isElementSet() {
        for (int i = 0; i < min.length; i++) {
            if (min[i] < max[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if this character set contains the specified character.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if this character set contains the specified
     *         character; <code>false</code> otherwise.
     */
    public boolean contains(int ch) {
        if (this == EMPTY) {
            return false;
        } else if (this == WHOLE) {
            return ch >= MIN_ELEMENT && ch <= MAX_ELEMENT;
        } else {
            return contains(ch, 0, min.length - 1);
        }
    }
    
    /**
     * Determines if this character set contains the specified character in
     * the specified minimum and maximum bounds.
     * 
     * @param ch Character to be tested.
     * @param i Minimum bound to be tested.
     * @param j Maximum bound to be tested.
     * @return <code>true</code> if this character set contains the specified
     *         character in the specified minimum and maximum bounds;
     *         <code>false</code> otherwise.
     */
    private boolean contains(int ch, int i, int j) {
        if (ch < min[i] || ch > max[j]) {
            return false;
        } else if (i < j) {
            int m = i + (j - i) / 2 + 1;
            return contains(ch, i, m - 1) || contains(ch, m, j);
        } else {
            return true;
        }
    }
    
    /**
     * Merges this character set with the specified one and returns a new
     * character set.
     * 
     * @param uset Character set to merge.
     * @return Union of this character set and the specified one.
     */
    public UnicodeSet union(UnicodeSet uset) {
        return unionAll(this, uset);
    }
    
    /**
     * Returns inverse character set for this character set. More formally,
     * this method returns character set that contains all characters not
     * contained in this set.
     * 
     * @return Inverse character set for this character set.
     */
    public UnicodeSet inverse() {
        if (inverse != null) {
            return inverse;
        } else if (this == EMPTY) {
            return inverse = WHOLE;
        } else if (this == WHOLE) {
            return inverse = EMPTY;
        }
        
        int[] min;
        int[] max;
        
        int n = this.min[0];
        int m = this.max[this.max.length - 1];
        int size = this.min.length;
        
        int j = 0;
        if (n == MIN_ELEMENT) {
            if (m == MAX_ELEMENT) {
                min = new int[size - 1];
                max = new int[size - 1];
            } else {
                min = new int[size];
                max = new int[size];
                min[min.length - 1] = m + 1;
                max[max.length - 1] = MAX_ELEMENT;
            }
        } else if (m == MAX_ELEMENT) {
            min = new int[size];
            max = new int[size];
            min[0] = MIN_ELEMENT;
            max[0] = n - 1;
            j = 1;
        } else {
            min = new int[size + 1];
            max = new int[size + 1];
            min[0] = MIN_ELEMENT;
            max[0] = n - 1;
            min[min.length - 1] = m + 1;
            max[max.length - 1] = MAX_ELEMENT;
            j = 1;
        }
        
        for (int i = 1; i < size; i++, j++) {
            min[j] = this.max[i - 1] + 1;
            max[j] = this.min[i] - 1;
        }
        
        return inverse = valueOf(min, max);
    }
    
    /**
     * Returns a hash code value for this character set.
     * 
     * @return A hash code value for this character set.
     */
    public int hashCode() {
        if (hash == 0) {
            for (int i = 0, hash = 1; i < min.length; i++) {
                hash = 31 * (31 * hash + min[i]) + max[i];
            }
        }
        return hash;
    }
    
    /**
     * Determines if this character set equals to the specified one.
     * 
     * @param obj Another character set.
     * @return <code>true</code> if this character set equals to the specified
     *         one; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof UnicodeSet) {
            UnicodeSet other = (UnicodeSet) obj;
            if (min.length != other.min.length) {
                return false;
            }
            for (int i = 0; i < min.length; i++) {
                if (min[i] != other.min[i] || max[i] != other.max[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * Returns array that contains all intervals of this character set. All odd
     * elements are minimum bounds, all even elements are maximum bounds.
     * 
     * @return Array that contains all intervals of this character set.
     */
    public int[] toArray() {
        int[] intervals = new int[min.length * 2];
        for (int i = 0, j = 0; i < min.length; i++) {
            intervals[j++] = min[i];
            intervals[j++] = max[i];
        }
        return intervals;
    }
    
    /**
     * Appends string representation of this character set to the specified
     * buffer.
     * 
     * @param buf Buffer to append.
     */
    @Override
    public StringBuilder toString(StringBuilder buf) {
        buf.append('[');
        if (min.length > 0) {
            toString(buf, min[0], max[0]);
            for (int i = 1; i < min.length; i++) {
                buf.append(',');
                toString(buf, min[i], max[i]);
            }
        }
        return buf.append(']');
    }
    
    /**
     * Serializes this character set.
     * 
     * @param stream Output stream.
     * @throws java.io.IOException if an IO error occurred.
     */
    private void writeObject(java.io.ObjectOutputStream stream)
            throws java.io.IOException {
        stream.defaultWriteObject();
        stream.writeInt(min.length);
        for (int i = 0; i < min.length; i++) {
            stream.writeInt(min[i]);
            stream.writeInt(max[i]);
        }
    }
    
    /**
     * Deserializes this character set.
     * 
     * @param stream Input stream.
     * @throws java.io.IOException if an IO error occurred.
     * @throws ClassNotFoundException if the class of a serialized object could
     *         not be found.
     */
    private void readObject(java.io.ObjectInputStream stream)
            throws java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
        int size = stream.readInt();
        min = new int[size];
        max = new int[size];
        for (int i = 0; i < size; i++) {
            min[i] = stream.readInt();
            max[i] = stream.readInt();
        }
    }
    
    /**
     * Replaces deserialized {@link #EMPTY} and {@link #WHOLE} instances.
     * 
     * @return {@link #EMPTY} or {@link #WHOLE} or this instance.
     * @throws java.io.ObjectStreamException never.
     */
    private Object readResolve() throws java.io.ObjectStreamException {
        return EMPTY.equals(this) ? EMPTY : WHOLE.equals(this) ? WHOLE : this;
    }
    
    /**
     * Appends string representation of the specified characters interval to
     * the specified buffer.
     * 
     * @param n Minimum bound.
     * @param m Maximum bound.
     * @param buf Buffer to append.
     */
    private static void toString(StringBuilder buf, int n, int m) {
        buf.append('\'')
           .append(Strings.escape((char) n))
           .append('\'');
        if (n < m) {
            buf.append('-')
               .append('\'')
               .append(Strings.escape((char) m))
               .append('\'');
        }
    }
    
    /**
     * Merges the specified character sets and returns a new character set.
     * 
     * @param usets Character sets to merge.
     * @return Union of the specified character sets.
     */
    public static UnicodeSet unionAll(UnicodeSet... usets) {
        int length = usets.length;
        if (length == 0) {
            return EMPTY;
        } else if (length == 1) {
            return usets[0];
        }
        
        int size = 0;
        for (UnicodeSet uset : usets) {
            if (uset == WHOLE) {
                return WHOLE;
            }
            size += uset.size();
        }
        
        if (size == 0) {
            return EMPTY;
        }
        
        int[] min = new int[size];
        int[] max = new int[size];
        
        for (int i = 0, offset = 0; i < length; i++) {
            UnicodeSet uset = usets[i];
            if (uset != EMPTY) {
                int delta = uset.size();
                System.arraycopy(uset.min, 0, min, offset, delta);
                System.arraycopy(uset.max, 0, max, offset, delta);
                offset += delta;
            }
        }
        
        return valueOf(min, max);
    }
    
    /**
     * Creates a new character set from the specified array of characters.
     * 
     * @param elements Array of characters.
     * @return A new character set from the specified array of characters.
     */
    public static UnicodeSet fromElements(char... elements) {
        int length = elements.length;
        if (length == 0) {
            return EMPTY;
        }
        
        int[] intervals = new int[length * 2];
        for (int i = 0, j = 0; i < length; i++) {
            int ch = elements[i];
            intervals[j++] = ch;
            intervals[j++] = ch;
        }
        
        return fromIntervals(intervals);
    }
    
    /**
     * Creates a new character set from the specified array of characters.
     * 
     * @param elements Array of characters.
     * @return A new character set from the specified array of characters.
     */
    public static UnicodeSet fromElements(int[] elements) {
        int length = elements.length;
        if (length == 0) {
            return EMPTY;
        }
        
        int[] intervals = new int[length * 2];
        for (int i = 0, j = 0; i < length; i++) {
            int ch = elements[i];
            intervals[j++] = ch;
            intervals[j++] = ch;
        }
        
        return fromIntervals(intervals);
    }
    
    /**
     * Creates a new character set from the specified string of characters.
     * 
     * @param elements String of characters.
     * @return A new character set from the specified string of characters.
     */
    public static UnicodeSet fromElements(String elements) {
        int length = elements.length();
        if (length == 0) {
            return EMPTY;
        }
        
        int[] intervals = new int[length * 2];
        for (int i = 0, j = 0; i < length; i++) {
            int ch = elements.codePointAt(i);
            intervals[j++] = ch;
            intervals[j++] = ch;
        }
        
        return fromIntervals(intervals);
    }
    
    /**
     * Creates a new character set from the specified array of intervals. All
     * odd elements are minimum bounds, all even elements are maximum bounds.
     * 
     * @param intervals Array of intervals.
     * @return A new character set from the specified array of intervals.
     * @throws IllegalArgumentException if length of the specified array of
     *         intervals is invalid.
     */
    public static UnicodeSet fromIntervals(char... intervals) {
        int length = intervals.length;
        if (length % 2 > 0) {
            throw new IllegalArgumentException();
        } else if (length == 0) {
            return EMPTY;
        }
        
        int size = length / 2;
        int[] min = new int[size];
        int[] max = new int[size];
        
        for (int i = 0, j = 0; i < max.length; i++) {
            int n = intervals[j++];
            int m = intervals[j++];
            if (n < m) {
                min[i] = n;
                max[i] = m;
            } else {
                min[i] = m;
                max[i] = n;
            }
        }
        
        return valueOf(min, max);
    }
    
    /**
     * Creates a new character set from the specified array of intervals. All
     * odd elements are minimum bounds, all even elements are maximum bounds.
     * 
     * @param intervals Array of intervals.
     * @return A new character set from the specified array of intervals.
     * @throws IllegalArgumentException if length of the specified array of
     *         intervals is invalid.
     */
    public static UnicodeSet fromIntervals(int[] intervals) {
        int length = intervals.length;
        if (length % 2 > 0) {
            throw new IllegalArgumentException();
        } else if (length == 0) {
            return EMPTY;
        }
        
        int size = length / 2;
        int[] min = new int[size];
        int[] max = new int[size];
        
        for (int i = 0, j = 0; i < min.length; i++) {
            int n = alignElement(intervals[j++]);
            int m = alignElement(intervals[j++]);
            if (n < m) {
                min[i] = n;
                max[i] = m;
            } else {
                min[i] = m;
                max[i] = n;
            }
        }
        
        return valueOf(min, max);
    }
    
    /**
     * Creates a new character set from the specified pattern.
     * 
     * <p>The following rules are applied to pattern expressions:</p>
     * <ul>
     *   <li>The <code>\</code> character is considered as escape character.</li>
     *   <li>The first <code>^</code> character is considered as set inverse.</li>
     *   <li>The <code>X-Y</code> is considered as characters interval from
     *       <code>X</code> to <code>Y</code>.</li>
     *   <li>Any other character is considered as single character interval.</li>
     * </ul>
     * 
     * @param pattern Pattern expression string.
     * @return A new character set from the specified pattern.
     * @throws IllegalArgumentException if the specified pattern is invalid.
     */
    public static UnicodeSet fromPattern(String pattern) {
        if (pattern.isEmpty()) {
            return EMPTY;
        }
        
        int i = 0, length = pattern.length();
        boolean inverse = pattern.charAt(0) == '^';
        if (inverse && length == ++i) {
            return WHOLE;
        }
        
        int size = 0;
        int[] min = new int[length];
        int[] max = new int[length];
        
        for (; i < length; size++) {
            char ch = pattern.charAt(i++);
            if (ch == '\\') {
                if (i == length) {
                    throw new IllegalArgumentException(pattern);
                }
                ch = pattern.charAt(i++);
            }
            if (i < length && pattern.charAt(i) == '-') {
                if (++i == length) {
                    throw new IllegalArgumentException(pattern);
                }
                min[size] = ch;
                ch = pattern.charAt(i++);
                if (ch == '\\') {
                    if (i == length) {
                        throw new IllegalArgumentException(pattern);
                    }
                    ch = pattern.charAt(i++);
                }
                max[size] = ch;
            } else {
                min[size] = max[size] = ch;
            }
        }
        
        if (size < length) {
            min = Arrays.copyOf(min, size);
            max = Arrays.copyOf(max, size);
        }
        
        UnicodeSet uset = valueOf(min, max);
        return inverse ? uset.inverse() : uset;
    }
    
    /**
     * Creates a new character set from the specified arrays of bounds.
     * 
     * @param min Array of minimum bounds.
     * @param max Array of maximum bounds.
     * @return A new character set from the specified arrays of bounds.
     */
    private static UnicodeSet valueOf(int[] min, int[] max) {
        Arrays.sort(min);
        Arrays.sort(max);
        
        int n = 0, size = 0;
        for (int i = 1; i < min.length; i++) {
            if (min[i] > max[i - 1] + 1) {
                min[size] = min[n];
                max[size] = max[i - 1];
                size++;
                n = i;
            }
        }
        
        min[size] = min[n];
        max[size] = max[max.length - 1];
        size++;
        
        if (size == 1) {
            if (min[0] == MIN_ELEMENT && max[0] == MAX_ELEMENT) {
                return WHOLE;
            }
        }
        
        if (size == max.length) {
            return new UnicodeSet(min, max);
        }
        
        return new UnicodeSet(Arrays.copyOf(min, size),
                              Arrays.copyOf(max, size));
    }
    
    /**
     * Aligns character code point to the allowed unicode bounds.
     * 
     * @param ch Character to be aligned.
     * @return Aligned character.
     */
    private static int alignElement(int ch) {
        return ch < MIN_ELEMENT ? MIN_ELEMENT : ch > MAX_ELEMENT ? MAX_ELEMENT : ch;
    }
    
}
