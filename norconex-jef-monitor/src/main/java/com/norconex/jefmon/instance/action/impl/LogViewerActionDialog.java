/* Copyright 2007-2014 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Override
    protected Component createBodyComponent(String id) {
        return new LogViewerActionPanel(id, linesReader);
    }
    

}
