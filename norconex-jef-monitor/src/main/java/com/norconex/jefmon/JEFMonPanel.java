package com.norconex.jefmon;

import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.markup.html.panel.CssPanel;
import com.norconex.jefmon.instance.JEFMonInstance;

public class JEFMonPanel extends CssPanel {

    private static final long serialVersionUID = 3060318185404879150L;

    public JEFMonPanel(String id, IModel<?> model) {
        super(id, model);
    }

    public JEFMonPanel(String id) {
        super(id);
    }

    /**
     * Get downcast session object for easy access by subclasses
     * 
     * @return The session
     */
    public JEFMonSession getSession() {
        return (JEFMonSession) super.getSession();
    }

    /**
     * Get downcast application object for easy access by subclasses
     * 
     * @return The application
     */
    public JEFMonApplication getApp() {
        return (JEFMonApplication) getApplication();
    }

    public JEFMonConfig getJEFMonConfig() {
        return getApp().getConfig();
    }
    public JEFMonInstance getJobSuitesStatusesMonitor() {
        return getApp().getJobSuitesStatusesMonitor();
    }
}
