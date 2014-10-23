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

package org.foxlabs.util.reflect;

/**
 * Runtime wrapper for exceptions that can occur when invoking a method or
 * accessing a field.
 * 
 * @author Fox Mulder
 */
public class PropertyAccessException extends IllegalStateException {
    private static final long serialVersionUID = -8836934631774863377L;
    
    /**
     * Constructs a new <code>PropertyAccessException</code> with the specified
     * cause.
     * 
     * @param cause Cause to be wrapped.
     */
    public PropertyAccessException(Throwable cause) {
        super(cause);
    }
    
}
