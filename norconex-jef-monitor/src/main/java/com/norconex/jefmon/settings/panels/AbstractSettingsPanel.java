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

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonPanel;

/**
 * @author Pascal Essiembre
 */
public abstract class AbstractSettingsPanel extends JEFMonPanel
        implements IFormModelUpdateListener {

    private static final long serialVersionUID = -6984964094746204191L;

    private final JEFMonConfig dirtyConfig;

    public AbstractSettingsPanel(String id, JEFMonConfig dirtyConfig) {
        super(id);
        this.dirtyConfig = dirtyConfig;
    }

    public final JEFMonConfig getDirtyConfig() {
        return dirtyConfig;
    }

    @Override
    public void updateModel() {
        applyState();
    }

    protected abstract void applyState();
}
