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

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.commons.wicket.bootstrap.resources.BootstrapLibrariesContributor;
import com.norconex.commons.wicket.markup.head.HeaderContributor;
import com.norconex.commons.wicket.markup.head.JQueryLibrariesContributor;
import com.norconex.commons.wicket.markup.html.CssPage;
import com.norconex.jefmon.markup.head.JEFMonLibrariesContributor;

/**
 * Base class for all pages in the JEF application requiring the same
 * set of header contributions.
 */
@SuppressWarnings("nls")
public abstract class JEFMonPage extends CssPage {

    private static final long serialVersionUID = 5094394223384313891L;

    public JEFMonPage() {
        super();
    }

    public JEFMonPage(IModel<?> model) {
        super(model);
    }

    public JEFMonPage(PageParameters pageParameters) {
        super(pageParameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("title", getTitle()));
    }

    protected abstract IModel<String> getTitle();

    /**
     * Get downcast session object for easy access by subclasses
     *
     * @return The session
     */
    public JEFMonSession getJEFMonSession() {
        return (JEFMonSession) getSession();
    }

    /**
     * Get downcast application object for easy access by subclasses
     *
     * @return The application
     */
    public JEFMonApplication getJEFMonApplication() {
        return (JEFMonApplication) getApplication();
    }

    public JEFMonConfig getJEFMonConfig() {
        return getJEFMonApplication().getConfig();
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        JQueryLibrariesContributor.contribute(getApplication(), response);  
        BootstrapLibrariesContributor.contribute(response);
        JEFMonLibrariesContributor.contribute(response);
        HeaderContributor.addCss(response, JEFMonPage.class, 
                JEFMonPage.class.getSimpleName() + ".css");
    }

}