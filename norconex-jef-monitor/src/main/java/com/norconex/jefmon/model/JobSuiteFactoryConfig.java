/* Copyright 2007-2014 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.jefmon.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class JobSuiteFactoryConfig implements Serializable {

    private static final long serialVersionUID = -5247407685184941610L;

    private final String factoryClass;
    private final Map<String, String> properties = 
            new HashMap<String, String>();

    public JobSuiteFactoryConfig(String factoryClass) {
        super();
        this.factoryClass = factoryClass;
    }

    public String getFactoryClass() {
        return factoryClass;
    }
    public Map<String, String> getProperties() {
        return properties;
    }
    public boolean hasProperties() {
        return !properties.isEmpty();
    }
    public boolean hasNonBlankProperties() {
        for (String value : properties.values()) {
            if (StringUtils.isNotBlank(value)) {
                return true;
            }
        }
        return false;
    }
    
    
}
