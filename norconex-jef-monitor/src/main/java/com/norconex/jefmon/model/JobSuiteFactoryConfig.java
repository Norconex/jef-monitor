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
