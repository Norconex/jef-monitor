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
package com.norconex.jefmon.settings.panels;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.norconex.jefmon.JEFMonConfig;

/**
 * Panel for giving a name to a JEF installation.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class IdentityPanel extends AbstractSettingsPanel {

    private static final long serialVersionUID = -6095982251770897729L;
    private final String instanceName;

    public IdentityPanel(String id, JEFMonConfig config) {
        super(id, config);

        setOutputMarkupId(true);
        setDefaultModel(new CompoundPropertyModel<IdentityPanel>(this));

        instanceName = config.getInstanceName();

        TextField<String> nameField = new TextField<String>("instanceName");
        nameField.setRequired(true);
        add(nameField);

    }

    @Override
    protected void applyState() {
        final JEFMonConfig config = getDirtyConfig();
        config.setInstanceName(instanceName);
    }
}
