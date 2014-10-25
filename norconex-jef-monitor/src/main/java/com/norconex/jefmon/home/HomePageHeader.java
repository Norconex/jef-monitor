package com.norconex.jefmon.home;

import org.apache.wicket.markup.html.WebMarkupContainer;

import com.norconex.commons.wicket.bootstrap.modal.BootstrapModalLauncher;
import com.norconex.jefmon.JEFMonHeaderPanel;
import com.norconex.jefmon.about.AboutDialog;

@SuppressWarnings("nls")
public class HomePageHeader extends JEFMonHeaderPanel {

    private static final long serialVersionUID = -6224560846395649167L;

    public HomePageHeader(String id) {
        super(id);
        
        AboutDialog aboutDialog = new AboutDialog("aboutModal");
        add(aboutDialog);
        
        WebMarkupContainer aboutLink = new WebMarkupContainer("aboutLink");
        aboutLink.add(new BootstrapModalLauncher(aboutDialog));
        add(aboutLink);
    }
}
