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
        ILogManager logManager = 
                jobStatus.getSuiteStatusSnapshot().getLogManager();
        
        return new LogViewerActionPanel(componentId, 
                new JefLogLinesReader(logManager, 
                      jobStatus.getSuiteStatusSnapshot().getRoot().getJobId(), 
                      null));
    }
    
//    public IModel<String> getTooltip(final JobStatusTreeNode job) {
//        return StringResourceLoaderUtil.getStringModel(
//                this.getClass(), "action.title.viewSuiteLog");
//    }
}
