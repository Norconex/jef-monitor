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
package com.norconex.jefmon.settings.update;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.commons.wicket.markup.head.HeaderContributor;
import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonPage;
import com.norconex.jefmon.instance.InstancePage;
import com.norconex.jefmon.model.ConfigurationDAO;

/**
 * JEF Web Monitoring setup page.
 */
@SuppressWarnings("nls")
public abstract class SettingsPage extends JEFMonPage {

    private static final long serialVersionUID = -5929479500979931196L;

    private final JEFMonConfig dirtyConfig = new JEFMonConfig();

    
    public SettingsPage() {
        super();
        initializeComponents();
    }

    public SettingsPage(final PageParameters parameters) {
        super(parameters);
        initializeComponents();
    }

    @Override
    protected IModel<String> getTitle() {
        return new ResourceModel("settings.title");
    }

    private void initializeComponents() {
        try {
            PropertyUtils.copyProperties(dirtyConfig, getJEFMonConfig());
        } catch (Exception e) {
            throw new WicketRuntimeException(e);
        }

        AjaxLink<String> nameBack = new AjaxLink<String>("nameBack") {
            private static final long serialVersionUID = -7790485617549321158L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(InstancePage.class);
            }
        };
        nameBack.add(new Label("name", getJEFMonConfig().getInstanceName()));
        add(nameBack);

        Form<Void> form = new Form<Void>("form");
        form.setOutputMarkupId(true);
        add(form);

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);

        form.add(createConfigPanel("configPanel", dirtyConfig));
        
        final AjaxButton saveButton = new AjaxButton("save") {
            private static final long serialVersionUID = 8835758195954072646L;
            @Override
            protected void onAfterSubmit(
                    AjaxRequestTarget target, Form<?> theform) {
                try {
                    ConfigurationDAO.saveConfig(dirtyConfig);
                    PropertyUtils.copyProperties(
                            getJEFMonConfig(), dirtyConfig);
                } catch (IOException | IllegalAccessException 
                        | InvocationTargetException | NoSuchMethodException e) {
                    throw new WicketRuntimeException(
                            "Cannot save configuration.", e);
                }
                success(getString("success"));
                setResponsePage(InstancePage.class);
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> theForm) {
                target.add(feedback);
            }
        };
        form.add(saveButton);
        form.setDefaultButton(saveButton);

        AjaxLink<String> closeButton = new AjaxLink<String>("close") {
            private static final long serialVersionUID = 6062171472516887030L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(InstancePage.class);
            }
        };
        form.add(closeButton);

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        HeaderContributor.addCss(response, SettingsPage.class, 
                "wicket-package.css");
    }
    
    
    protected abstract Panel createConfigPanel(
            String markupId, JEFMonConfig dirtyConfig);
    



}
