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

package org.foxlabs.util.resource;

/**
 * Thrown to indicate that operation with resources has failed.
 * 
 * @author Fox Mulder
 */
public class ResourceError extends Error {
    private static final long serialVersionUID = -6723763158930315964L;
    
    /**
     * Constructs a new <code>ResourceError</code> with the specified detail
     * message.
     * 
     * @param message The detail message.
     */
    public ResourceError(String message) {
        super(message);
    }
    
    /**
     * Constructs a new <code>ResourceError</code> with the specified detail
     * message and cause.
     * 
     * @param message The detail message.
     * @param cause Cause to be wrapped.
     */
    public ResourceError(String message, Throwable cause) {
        super(message, cause);
    }
    
}
