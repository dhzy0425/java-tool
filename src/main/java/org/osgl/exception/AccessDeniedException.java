package org.osgl.exception;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2020 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;

/**
 * A generic exception thrown when access to a certain resource is denied.
 */
public class AccessDeniedException extends UnexpectedException {

    public AccessDeniedException(File file) {
        super("Access denied: " + file);
    }

    public AccessDeniedException() {
        super("Access Denied");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Object... args) {
        super(message, args);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }

    public AccessDeniedException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }
}
