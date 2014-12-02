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
package com.norconex.jefmon.instance;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.jefmon.JEFMonPage;

public class InstancePage extends JEFMonPage {

    private static final long serialVersionUID = 1559009691638968793L;

    public InstancePage() {
        super();
    }
    public InstancePage(IModel<?> model) {
        super(model);
    }
    public InstancePage(PageParameters pageParameters) {
        super(pageParameters);
    }

    @Override
    protected IModel<String> getTitle() {
        return Model.of("JEF Jobs");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new InstancePanel("treePanel"));
    }

}
