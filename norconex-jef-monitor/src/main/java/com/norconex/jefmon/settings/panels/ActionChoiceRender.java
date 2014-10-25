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
