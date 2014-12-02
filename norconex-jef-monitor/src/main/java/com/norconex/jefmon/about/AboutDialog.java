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
package com.norconex.jefmon.about;

import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.application.DefaultClassResolver;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import com.norconex.commons.wicket.behaviors.CssClass;
import com.norconex.commons.wicket.bootstrap.modal.BootstrapAjaxModal;
import com.norconex.jefmon.JEFMonException;

@SuppressWarnings("nls")
public class AboutDialog extends BootstrapAjaxModal {

    private static final long serialVersionUID = -7562468687155375090L;

    public AboutDialog(String id) {
        super(id, new ResourceModel("about.title"));
        add(new CssClass("nx-about-dialog"));
    }

    @Override
    protected Component createBodyComponent(String id) {
        return new Body(id);
    }
    
    class Body extends Panel {
        private static final long serialVersionUID = 1058222153259044202L;
        public Body(String id) {
            super(id);
            PropertiesConfiguration properties = getAboutProperties();
            add(new Label("version", properties.getString("version")));
        }
    }
    
    private PropertiesConfiguration getAboutProperties() {
        try {
            URL resource = new DefaultClassResolver().
                    getClassLoader().getResource("about.properties");
            return new PropertiesConfiguration(resource);
        } catch (ConfigurationException e) {
            throw new JEFMonException(
                    "Exception while reading about.properties", e);
        }
    }
}
