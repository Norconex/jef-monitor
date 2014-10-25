package com.norconex.jefmon.instance.action;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import com.norconex.jefmon.instance.tree.JobStatusTreeNode;

public interface IJobAction extends Serializable {

    IModel<String> getName();
    String getFontIcon();
//    IModel<String> getTooltip(JobStatusTreeNode job);
    boolean isVisible(JobStatusTreeNode job);
    Component execute(JobStatusTreeNode job, String componentId);
//    BootstrapAjaxModal execute(JobStatusTreeNode job, 
//            AjaxRequestTarget target, String dialogId);
}
