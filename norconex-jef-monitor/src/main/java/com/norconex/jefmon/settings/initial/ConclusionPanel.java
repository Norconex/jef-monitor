package com.norconex.jefmon.settings.initial;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.wicket.WicketRuntimeException;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.model.ConfigurationDAO;
import com.norconex.jefmon.settings.panels.AbstractSettingsPanel;

/**
 * @author Pascal Essiembre
 *
 */
public class ConclusionPanel extends AbstractSettingsPanel {


    private static final long serialVersionUID = -4938801836613969374L;

    public ConclusionPanel(String id, JEFMonConfig dirtyConfig) {
        super(id, dirtyConfig);
        
        setOutputMarkupId(true);
    }

    @Override
    protected void applyState() {
        JEFMonConfig dirtyConfig = getDirtyConfig();
//        getJEFMonConfig().
        try {
            BeanUtils.copyProperties(getJEFMonConfig(), dirtyConfig);
            ConfigurationDAO.saveConfig(getJEFMonConfig());
        } catch (IOException 
                | IllegalAccessException 
                | InvocationTargetException e) {
            throw new WicketRuntimeException("Cannot save configuration.", e);
        }
//        getApp().setJEFMonConfig(config);
    }        
}
