package com.norconex.jefmon.instance.action;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.norconex.jefmon.JEFMonPage;

public class ActionPage extends JEFMonPage {

    private static final long serialVersionUID = 7150691633736456613L;

    public static final String COMPONENT_ID = "actionComponent";
    
    private final IModel<String> title;
    
    public ActionPage(Component component, IModel<String> title) {
        super();
        this.title = title;
        add(component);
    }

    @Override
    protected IModel<String> getTitle() {
        return title;
    }
    
}
