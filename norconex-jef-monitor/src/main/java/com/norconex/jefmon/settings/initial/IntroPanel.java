package com.norconex.jefmon.settings.initial;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentLabel;

import com.norconex.commons.wicket.bootstrap.form.BootstrapSelect;
import com.norconex.commons.wicket.markup.html.i18n.SessionLocaleDropDownChoice;
import com.norconex.jefmon.JEFMonPanel;

/**
 * @author Pascal Essiembre
 *
 */
@SuppressWarnings("nls")
public class IntroPanel extends JEFMonPanel {

    private static final long serialVersionUID = -4938801836613969374L;

    public IntroPanel(String id) {
        super(id);
        
        setOutputMarkupId(true);
        
        DropDownChoice<?> language = new SessionLocaleDropDownChoice(
                "language", Arrays.asList(getApp().getSupportedLocales()));
        language.add(new BootstrapSelect());
        add(language);

        add(new FormComponentLabel("languageLabel", language));
    }        
}
