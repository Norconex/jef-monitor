package com.norconex.jefmon.instance.action.impl;

import java.io.File;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.model.ClassResourceModel;
import com.norconex.jef4.log.FileLogManager;
import com.norconex.jef4.log.ILogManager;
import com.norconex.jefmon.instance.action.IJobAction;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;

/**
 * Tree Job action for a job log.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class ViewJobLogAction implements IJobAction {

    private static final long serialVersionUID = 4354106279427926070L;

    @Override
    public String getFontIcon() {
        return "fa fa-indent";
    }
    
    @Override
    public IModel<String> getName() {
        return new ClassResourceModel(getClass(), "action.title.viewJobLog");
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
                jobStatus.getSuiteStatusSnapshot().getRoot().getJobId());
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        return true;
    }

    @Override
    public Component execute(JobStatusTreeNode jobStatus, String componentId) {
        ILogManager logManager = 
                jobStatus.getSuiteStatusSnapshot().getLogManager();
        
        return new LogViewerActionPanel(componentId, 
                new JefLogLinesReader(logManager, 
                      jobStatus.getSuiteStatusSnapshot().getRoot().getJobId(),
                      jobStatus.getJobId()));
    }
    
//    public IModel<String> getTooltip(final JobStatusTreeNode job) {
//        return StringResourceLoaderUtil.getStringModel(
//                this.getClass(), "action.title.viewJobLog");
//    }
}
