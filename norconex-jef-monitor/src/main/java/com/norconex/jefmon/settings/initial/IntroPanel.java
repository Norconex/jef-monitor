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

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentLabel;

import com.norconex.commons.wicket.bootstrap.form.BootstrapSelect;
import com.norconex.commons.wicket.markup.html.i18n.SessionLocaleDropDownChoice;
import com.norconex.jefmon.JEFMonPanel;

/**
 * @author Pascal Essiembre
 *
 */
@SuppressWarnings("nls")
public class IntroPanel extends JEFMonPanel {

    private static final long serialVersionUID = -4938801836613969374L;

    public IntroPanel(String id) {
        super(id);
        
        setOutputMarkupId(true);
        
        DropDownChoice<?> language = new SessionLocaleDropDownChoice(
                "language", Arrays.asList(getApp().getSupportedLocales()));
        language.add(new BootstrapSelect());
        add(language);

        add(new FormComponentLabel("languageLabel", language));
    }        
}
