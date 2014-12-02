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
package com.norconex.jefmon.instances;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.UrlValidator;

import com.norconex.commons.wicket.bootstrap.modal.BootstrapAjaxModal;

public abstract class AddInstanceDialog extends BootstrapAjaxModal {

    private static final long serialVersionUID = 8628504762257690263L;

    private Form<Void> form;
    
    private String url;
    private FeedbackPanel feedback;
    
    public AddInstanceDialog(String id) {
        super(id, new ResourceModel("dlg.title"));
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected Component createBodyComponent(String id) {
        return new Body(id);
    }
    @Override
    protected Component createFooterComponent(String id) {
        return new Footer(id);
    }

    private class Body extends Panel {
        private static final long serialVersionUID = 914125064944803011L;
        public Body(String id) {
            super(id);
            url = null;
            form = new Form<>("form");
            add(form);
            
            TextField<String> urlField = new TextField<String>("url", 
                    new PropertyModel<String>(AddInstanceDialog.this, "url"));
            urlField.add(new UrlValidator(new String[]{"http", "https"}));
            urlField.setLabel(Model.of("URL"));
            urlField.setRequired(true);
            
            form.add(urlField);
            form.add(new FormComponentLabel("url.label", urlField));

            feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            form.add(feedback);
            
        }
    }
    
    private class Footer extends Panel {
        private static final long serialVersionUID = -8053168108094104855L;

        public Footer(String id) {
            super(id);


            add(new AjaxSubmitLink("submit", form) {
                private static final long serialVersionUID = 
                        8868651465300284897L;
                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
                @Override
                protected void onSubmit(
                        AjaxRequestTarget target, Form<?> form) {
                    hide(target);
                    AddInstanceDialog.this.onSubmit(target, url);
                }
            });
        }
    }
    
    protected abstract void onSubmit(AjaxRequestTarget target, String url);
}
