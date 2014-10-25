package com.norconex.jefmon.settings.panels;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonPanel;

/**
 * @author Pascal Essiembre
 */
public abstract class AbstractSettingsPanel extends JEFMonPanel
        implements IFormModelUpdateListener {

    private static final long serialVersionUID = -6984964094746204191L;

    private final JEFMonConfig dirtyConfig;

    public AbstractSettingsPanel(String id, JEFMonConfig dirtyConfig) {
        super(id);
        this.dirtyConfig = dirtyConfig;
    }

    public final JEFMonConfig getDirtyConfig() {
        return dirtyConfig;
    }

    @Override
    public void updateModel() {
        applyState();
    }

    protected abstract void applyState();
}
