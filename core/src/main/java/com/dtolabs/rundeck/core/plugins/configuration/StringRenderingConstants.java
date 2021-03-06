/*
 * Copyright 2011 DTO Solutions, Inc. (http://dtosolutions.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.dtolabs.rundeck.core.plugins.configuration;

/**
 * Constants that govern the different ways a {@link Property.Type.String} can be rendered.
 * 
 * User: Kim Ho <a href="mailto:kim.ho@salesforce.com">kim.ho@salesforce.com</a>
 */
public class StringRenderingConstants {
    
    public static final String SELECTION_ACCESSOR_KEY = "selectionAccessor";
    public static final String STORAGE_PATH_ROOT_KEY = "storage-path-root";
    public static final String STORAGE_FILE_META_FILTER_KEY = "storage-file-meta-filter";
    public static final String DISPLAY_TYPE_KEY = "displayType";

    public enum DisplayType {
        SINGLE_LINE,
        MULTI_LINE
    }
    public enum SelectionAccessor{
        STORAGE_PATH,
    }
}
