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
 * This class represents text position in some abstract resource.
 * 
 * @author Fox Mulder
 */
public final class Location implements Comparable<Location>, java.io.Serializable {
    private static final long serialVersionUID = -8995151179791454627L;
    
    /**
     * Unknown location.
     */
    public static final Location UNKNOWN = new Location(null, 0, 0);
    
    /**
     * Abstract resource name.
     */
    public final String file;
    
    /**
     * Line position.
     */
    public final int line;
    
    /**
     * Column position.
     */
    public final int column;
    
    /**
     * Constructs a new <code>Location</code> with the specified resource name,
     * line and column.
     * 
     * @param file Abstract resource name.
     * @param line Line position.
     * @param column Column position.
     */
    private Location(String file, int line, int column) {
        this.file = file;
        this.line = line;
        this.column = column;
    }
    
    /**
     * Determines if this location is unknown location.
     * 
     * @return <code>true</code> if this location is unknown location;
     *         <code>false</code> otherwise.
     */
    public boolean isUnknown() {
        return this == UNKNOWN;
    }
    
    /**
     * Compares this location to the specified one. Returns a negative integer,
     * zero, or a positive integer as this location is less than, equal to, or
     * greater than the specified location.
     * 
     * @param other Another location.
     * @return A negative integer, zero, or a positive integer as this location
     *         is less than, equal to, or greater than the specified location.
     */
    public int compareTo(Location other) {
        if (file != other.file) {
            if (file == null) {
                return -1;
            } else if (other.file == null) {
                return 1;
            }
            int c = file.compareTo(other.file);
            if (c != 0) {
                return c;
            }
        }
        int c = line - other.line;
        return c == 0 ? column - other.column : c;
    }
    
    /**
     * Returns a hash code value for this location.
     * 
     * @return A hash code value for this location.
     */
    public int hashCode() {
        int hash = file == null ? 17 : file.hashCode();
        hash = 31 * hash + line;
        hash = 31 * hash + column;
        return hash;
    }
    
    /**
     * Determines if this location equals to the specified one. Two locations
     * considered to be equal if they have equal resource names, line and
     * column positions.
     * 
     * @param obj Another location.
     * @return <code>true</code> if this location equals to the specified one;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            if (line == other.line && column == other.column) {
                return file == null ? other.file == null : file.equals(other.file);
            }
        }
        return false;
    }
    
    /**
     * Returns string representing this location.
     * 
     * @return String representing this location.
     * @see #toString(StringBuilder)
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toString(buf);
        return buf.toString();
    }
    
    /**
     * Appends string representation of this location to the specified buffer.
     * The full format is <code>file:line:column</code>.
     * 
     * @param buf Buffer to append.
     */
    public void toString(StringBuilder buf) {
        if (file != null) {
            buf.append(file);
        }
        if (line > 0) {
            if (file != null) {
                buf.append(':');
            }
            buf.append(line);
            if (column > 0) {
                buf.append(':');
                buf.append(column);
            }
        } else if (file == null) {
            buf.append("unknown");
        }
    }
    
    /**
     * Replaces deserialized unknown location to {@link #UNKNOWN}.
     */
    private Object readResolve() throws java.io.ObjectStreamException {
        return UNKNOWN.equals(this) ? UNKNOWN : this;
    }
    
    /**
     * Returns location for the specified resource name.
     * 
     * @param file Abstract resource name.
     * @return Location for the specified resource name.
     */
    public static Location valueOf(String file) {
        if (file == null || (file = file.trim()).isEmpty()) {
            return UNKNOWN;
        } else {
            return new Location(file, 0, 0);
        }
    }
    
    /**
     * Returns location for the specified line and column positions.
     * 
     * @param line Line position.
     * @param column Column position.
     * @return Location for the specified line and column positions.
     */
    public static Location valueOf(int line, int column) {
        if (line > 0) {
            return new Location(null, line, column < 0 ? 0 : column);
        } else {
            return column > 0 ? new Location(null, 1, column) : UNKNOWN;
        }
    }
    
    /**
     * Returns location for the specified resource name, line and column
     * positions.
     * 
     * @param file Abstract resource name.
     * @param line Line position.
     * @param column Column position.
     * @return Location for the specified resource name, line and column
     *         positions.
     */
    public static Location valueOf(String file, int line, int column) {
        if (file == null || (file = file.trim()).isEmpty()) {
            return valueOf(line, column);
        } else if (line > 0) {
            return new Location(file, line, column < 0 ? 0 : column);
        } else {
            return column > 0 ? new Location(file, 1, column) : UNKNOWN;
        }
    }
    
}
