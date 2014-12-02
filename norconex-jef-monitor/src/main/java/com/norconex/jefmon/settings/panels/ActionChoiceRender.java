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

import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.norconex.jefmon.instance.action.IJobAction;

public class ActionChoiceRender implements IChoiceRenderer<IJobAction> {

    private static final long serialVersionUID = -7807789186979407115L;

    public ActionChoiceRender() {
        super();
    }

    @Override
    public Object getDisplayValue(IJobAction action) {
        return action.getName().getObject();
    }

    @Override
    public String getIdValue(IJobAction action, int index) {
        return action.getClass().toString();
    }

}
