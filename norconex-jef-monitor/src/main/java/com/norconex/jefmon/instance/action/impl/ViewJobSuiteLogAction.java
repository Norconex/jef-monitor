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

import java.io.File;
import java.io.IOException;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.model.ClassResourceModel;
import com.norconex.jef4.log.FileLogManager;
import com.norconex.jef4.log.ILogManager;
import com.norconex.jefmon.instance.action.IJobAction;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;

/**
 * Tree Job action for a job suite log.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class ViewJobSuiteLogAction implements IJobAction {

    private static final long serialVersionUID = -4555251136467417091L;

    @Override
    public String getFontIcon() {
        return "fa fa-align-justify";
    }
    
    @Override
    public IModel<String> getName() {
        return new ClassResourceModel(getClass(), "action.title.viewSuiteLog");
    }

    
    @Override
    public boolean isVisible(JobStatusTreeNode jobStatus) {
        ILogManager logManager = 
                jobStatus.getSuiteStatusSnapshot().getLogManager();
        if (logManager == null) {
            return false;
        }
        if (!(logManager instanceof FileLogManager)) {
            return false;
        }
        File file = ((FileLogManager)logManager).getLogFile(
                jobStatus.getJobId());
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        try {
            return jobStatus.isRoot()
                    && logManager.getLog(jobStatus.getJobId()) != null;
        } catch (IOException e) {
          throw new WicketRuntimeException("Could not obtain log for suite:"
                + jobStatus.getSuiteStatusSnapshot().getRoot().getJobId(), e);
        }
    }

    @Override
    public Component execute(JobStatusTreeNode jobStatus, String componentId) {
        return new LogViewerActionPanel(componentId, 
                new JefLogLinesReader(jobStatus, false));
    }
}
