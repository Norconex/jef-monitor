package com.norconex.jefmon.instance.action.impl;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.bootstrap.modal.BootstrapAjaxModal;

/**
 * Display lines from a log file.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class LogViewerActionDialog extends BootstrapAjaxModal {

    private static final long serialVersionUID = -4909440150570319617L;
    private final LinesReader linesReader;
    
    /**
     * Creates a new log viewer action dialog.
     * @param id component id
     * @param title dialog title
     * @param linesReader lines reader
     */
    public LogViewerActionDialog(
            String id, IModel<String> title, LinesReader linesReader) {
        super(id, title);
        this.linesReader = linesReader;
    }

//
//    @Override
//    public void renderHead(IHeaderResponse response) {
//        super.renderHead(response);
//        response.render(CssHeaderItem.forReference(
//                new PackageResourceReference(
//                        this.getClass(), "wicket-package.css")));
//    }
//    


    @Override
    protected Component createBodyComponent(String id) {
        return new LogViewerActionPanel(id, linesReader);
    }
    

}
