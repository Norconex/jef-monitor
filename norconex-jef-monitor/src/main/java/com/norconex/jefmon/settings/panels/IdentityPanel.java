package com.norconex.jefmon.settings.panels;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import com.norconex.jefmon.JEFMonConfig;

/**
 * Panel for giving a name to a JEF installation.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class IdentityPanel extends AbstractSettingsPanel {

    private static final long serialVersionUID = -6095982251770897729L;
    private final String instanceName;

    public IdentityPanel(String id, JEFMonConfig config) {
        super(id, config);

        setOutputMarkupId(true);
        setDefaultModel(new CompoundPropertyModel<IdentityPanel>(this));

        instanceName = config.getInstanceName();

        TextField<String> nameField = new TextField<String>("instanceName");
        nameField.setRequired(true);
        add(nameField);

    }

    @Override
    protected void applyState() {
        final JEFMonConfig config = getDirtyConfig();
        config.setInstanceName(instanceName);
    }
}
