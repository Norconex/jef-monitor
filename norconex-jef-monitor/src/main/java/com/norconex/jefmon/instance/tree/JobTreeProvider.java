package com.norconex.jefmon.instance.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.norconex.jef4.status.JobSuiteStatusSnapshot;
import com.norconex.jefmon.instance.JEFMonInstance;

public class JobTreeProvider 
        implements ISortableTreeProvider<JobStatusTreeNode, Void> {

    private static final long serialVersionUID = -7140490848913529419L;

    private final JEFMonInstance instance;

//    private transient List<IJobAction> jobActions;

    public JobTreeProvider(final JEFMonInstance instance) {
        super();
        this.instance = instance;
    }

    @Override
    public void detach() {
        //Do nothing
    }

    @Override
    public Iterator<? extends JobStatusTreeNode> getChildren(
            JobStatusTreeNode jobStatusNode) {
        return jobStatusNode.getChildren().iterator();
    }

    @Override
    public Iterator<? extends JobStatusTreeNode> getRoots() {
        List<JobStatusTreeNode> roots = new ArrayList<JobStatusTreeNode>();
        Collection<JobSuiteStatusSnapshot> suitesStatuses =
                instance.getJobSuitesStatuses();
//        List< JobSuite> suites = getJobSuites();
//        List<JobSuite> suites = JEFMonApplication.get().getJobSuites();
        for (JobSuiteStatusSnapshot suiteStatuses : suitesStatuses) {
            roots.add(new JobStatusTreeNode(
                    instance, suiteStatuses.getRoot().getJobId(), 
                    suiteStatuses.getRoot().getJobId(), true));
        }
//        if (suites != null) {
//            for (JobSuite suite : suites) {
//                roots.add(new JobStatusTreeNode(suite.getRootJob(), suite, true));
//            }
//        }
        return roots.iterator();
    }

    @Override
    public boolean hasChildren(JobStatusTreeNode jobStatusNode) {
        return jobStatusNode.hasChildren();
    }

    @Override
    public IModel<JobStatusTreeNode> model(JobStatusTreeNode jobStatusNode) {
        return new Model<JobStatusTreeNode>(jobStatusNode);
    }

//    public List<IJobAction> getJobActions() {
//        if (monitorConfig == null) {
//            return null;
//        }
//        if (jobActions != null) {
//            return jobActions;
//        }
//        String[] actionClasses = monitorConfig.getActionClasses();
//        jobActions = new ArrayList<IJobAction>(actionClasses.length);
//        for (String actionClass : actionClasses) {
//            jobActions.add((IJobAction) JEFMonUtil.newInstance(
//                    monitorConfig.getClassloader(), actionClass));
//        }
//        return jobActions;
//    }

//    private List<JobSuite> getJobSuites() {
//        if (monitorConfig == null) {
//            return null;
//        }
//        if (jobSuites != null) {
//            return jobSuites;
//        }
//        return JEFMonUtil.createJobSuites(monitorConfig);
//    }

    @Override
    public ISortState<Void> getSortState() {
        return null;
    }
}
