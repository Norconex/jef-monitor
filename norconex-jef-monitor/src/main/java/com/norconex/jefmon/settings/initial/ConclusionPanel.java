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
package com.norconex.jefmon.settings.initial;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.wicket.WicketRuntimeException;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.model.ConfigurationDAO;
import com.norconex.jefmon.settings.panels.AbstractSettingsPanel;

/**
 * @author Pascal Essiembre
 *
 */
public class ConclusionPanel extends AbstractSettingsPanel {


    private static final long serialVersionUID = -4938801836613969374L;

    public ConclusionPanel(String id, JEFMonConfig dirtyConfig) {
        super(id, dirtyConfig);
        
        setOutputMarkupId(true);
    }

    @Override
    protected void applyState() {
        JEFMonConfig dirtyConfig = getDirtyConfig();
        try {
            BeanUtils.copyProperties(getJEFMonConfig(), dirtyConfig);
            ConfigurationDAO.saveConfig(getJEFMonConfig());
        } catch (IOException 
                | IllegalAccessException 
                | InvocationTargetException e) {
            throw new WicketRuntimeException("Cannot save configuration.", e);
        }
    }        
}
