/* 
 * Copyright (C) 2018 FoxLabs
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

import java.util.Objects;

/**
 * Holds version value. The main goal of this class is to provide proper
 * comparison of versions taking into account integer parts.
 * 
 * @author Fox Mulder
 */
public final class Version implements Comparable<Version>, java.io.Serializable {
    private static final long serialVersionUID = -3188109965410924967L;
    
    /**
     * This version value.
     */
    private final String value;
    
    /**
     * Constructs a new version with the specified value.
     * 
     * @param version This version value.
     * @throws NullPointerException if this specified value is <code>null</code>.
     */
    public Version(String value) {
        this.value = Objects.requireNonNull(value);
    }
    
    /**
     * Returns this version value.
     * 
     * @return This version value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Returns hash code of this version.
     * 
     * @return Hash code of this version.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    /**
     * Determines if this version is the same as the specified one. Two versions
     * are equal if their values are equal.
     * 
     * @param obj Version to be compared with this version.
     * @return <code>true</code> if this version is the same as the specified
     *         one; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Version && value.equals(((Version) obj).value);
    }
    
    /**
     * Compares this version with the specified one taking into account integer parts.
     * 
     * @param that Version to be compared with this version.
     * @return A negative integer, zero, or a positive integer as this version
     *         is less than, equal to, or greater than the specified one.
     */
    @Override
    public int compareTo(Version that) {
        int intc = Integer.MAX_VALUE;
        final String v1 = this.value;
        final String v2 = that.value;
        final int length = Math.min(v1.length(), v2.length());
        for (int i = 0; i < length; i++) {
            final char ch1 = v1.charAt(i);
            final char ch2 = v2.charAt(i);
            final int c = ch1 - ch2;
            if (intc != Integer.MAX_VALUE) { // Integer mode
                final boolean int1 = !(ch1 < '0' || ch1 > '9');
                final boolean int2 = !(ch2 < '0' || ch2 > '9');
                if (int1 != int2) {
                    return int1 ? 1 : -1; // Longer integer is greater
                } else if (int1) { // Both are still integers
                    if (intc == 0) { // And they are the same so far
                        intc = c;
                    }
                } else { // Both integers are the same length
                    if (intc != 0) { // Return first difference
                        return intc;
                    } else if (c != 0) { // Both integers are equal but next characters are not
                        return c;
                    } else { // Both are equal so far but they are not integers anymore
                        intc = Integer.MAX_VALUE; // Switch back to string mode
                    }
                }
            } else if (ch1 < '1' || ch1 > '9' || ch2 < '1' || ch2 > '9') { // Do not take into account leading zeros
                if (c != 0) {
                    return c;
                }
            } else { // Switch to integer mode
                intc = c;
            }
        }
        if (!(intc == Integer.MAX_VALUE || intc == 0)) { // Check latest integers comparison result if any
            return intc;
        }
        return v1.length() - v2.length(); // Common part is the same, longer string is greater
    }
    
    /**
     * Returns this version value.
     * 
     * @return This version value.
     */
    @Override
    public String toString() {
        return value;
    }
    
}
