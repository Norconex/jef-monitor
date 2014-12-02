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
package com.norconex.jefmon;

import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.markup.html.panel.CssPanel;
import com.norconex.jefmon.instance.JEFMonInstance;

/**
 * Base JEF Monitory panel.
 * @author Pascal Essiembre
 */
public class JEFMonPanel extends CssPanel {

    private static final long serialVersionUID = 3060318185404879150L;

    public JEFMonPanel(String id, IModel<?> model) {
        super(id, model);
    }

    public JEFMonPanel(String id) {
        super(id);
    }

    /**
     * Get downcast session object for easy access by subclasses
     * 
     * @return The session
     */
    public JEFMonSession getSession() {
        return (JEFMonSession) super.getSession();
    }

    /**
     * Get downcast application object for easy access by subclasses
     * 
     * @return The application
     */
    public JEFMonApplication getApp() {
        return (JEFMonApplication) getApplication();
    }

    public JEFMonConfig getJEFMonConfig() {
        return getApp().getConfig();
    }
    public JEFMonInstance getJobSuitesStatusesMonitor() {
        return getApp().getJobSuitesStatusesMonitor();
    }
}
