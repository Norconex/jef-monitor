package com.norconex.jefmon.markup.head;

import org.apache.wicket.markup.head.IHeaderResponse;

import com.norconex.commons.wicket.markup.head.HeaderContributor;

public final class JEFMonLibrariesContributor {

    private JEFMonLibrariesContributor() {
        super();
    }

    public static void contribute(IHeaderResponse response) {
        HeaderContributor.addCss(
                response, JEFMonLibrariesContributor.class, true, 
                "jquery-ui-1.11.0-custom.min.css",
                "jquery.layout-1.3.0-rc30.79.css"
        );
        HeaderContributor.addJavascript(
                response, JEFMonLibrariesContributor.class, true, 
                "jquery-ui-1.11.0-custom.min.js",
                "jquery.layout-1.3.0-rc30.79.min.js"
        );
    }
}
