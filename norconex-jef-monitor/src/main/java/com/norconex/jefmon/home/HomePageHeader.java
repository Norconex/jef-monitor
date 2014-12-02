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
package com.norconex.jefmon.home;

import org.apache.wicket.markup.html.WebMarkupContainer;

import com.norconex.commons.wicket.bootstrap.modal.BootstrapModalLauncher;
import com.norconex.jefmon.JEFMonHeaderPanel;
import com.norconex.jefmon.about.AboutDialog;

@SuppressWarnings("nls")
public class HomePageHeader extends JEFMonHeaderPanel {

    private static final long serialVersionUID = -6224560846395649167L;

    public HomePageHeader(String id) {
        super(id);
        
        AboutDialog aboutDialog = new AboutDialog("aboutModal");
        add(aboutDialog);
        
        WebMarkupContainer aboutLink = new WebMarkupContainer("aboutLink");
        aboutLink.add(new BootstrapModalLauncher(aboutDialog));
        add(aboutLink);
    }
}
