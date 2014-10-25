package com.norconex.jefmon.instance.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.norconex.commons.lang.map.Properties;
import com.norconex.jef4.status.IJobStatus;
import com.norconex.jef4.status.JobDuration;
import com.norconex.jef4.status.JobState;
import com.norconex.jef4.status.JobSuiteStatusSnapshot;

public class JobStatusTreeNode implements IJobStatus, Serializable {

    private static final long serialVersionUID = -1114163221300422823L;

    private final IJobStatus jobStatus;
    private final JobSuiteStatusSnapshot suiteStatusSnapshot;
    private final boolean root;
    private final String uid;
    public JobStatusTreeNode(
            IJobStatus jobStatus, JobSuiteStatusSnapshot suiteStatuses) {
        this(jobStatus, suiteStatuses, false);
    }
    public JobStatusTreeNode(IJobStatus jobStatus, 
            JobSuiteStatusSnapshot suiteStatuses, boolean root) {
        super();
        this.jobStatus = jobStatus;
        this.root = root;
        this.suiteStatusSnapshot = suiteStatuses;
        this.uid = suiteStatuses.getRoot().getJobId()
                + "__" + jobStatus.getJobId();
    }
    public boolean isRoot() {
        return root;
    }
    public boolean hasChildren() {
        return !suiteStatusSnapshot.getChildren(jobStatus).isEmpty();
    }
    public List<JobStatusTreeNode> getChildren() {
        List<IJobStatus> statuses = suiteStatusSnapshot.getChildren(jobStatus);
        List<JobStatusTreeNode> nodes = new ArrayList<>(statuses.size());
        for (IJobStatus status : statuses) {
            nodes.add(new JobStatusTreeNode(status, suiteStatusSnapshot));
        }
        return nodes;
    }
    
    public JobSuiteStatusSnapshot getSuiteStatusSnapshot() {
        return suiteStatusSnapshot;
    }
    @Override
    public String getJobId() {
        return jobStatus.getJobId();
    }
    @Override
    public JobState getState() {
        return jobStatus.getState();
    }
    @Override
    public double getProgress() {
        return jobStatus.getProgress();
    }
    @Override
    public String getNote() {
        return jobStatus.getNote();
    }
    @Override
    public int getResumeAttempts() {
        return jobStatus.getResumeAttempts();
    }
    @Override
    public JobDuration getDuration() {
        return jobStatus.getDuration();
    }
    @Override
    public Date getLastActivity() {
        return jobStatus.getLastActivity();
    }
    @Override
    public Properties getProperties() {
        return jobStatus.getProperties();
    }
    @Override
    public boolean isStarted() {
        return jobStatus.isStarted();
    }
    @Override
    public boolean isResumed() {
        return jobStatus.isResumed();
    }
    @Override
    public boolean isAborted() {
        return jobStatus.isAborted();
    }
    @Override
    public boolean isStopped() {
        return jobStatus.isStopped();
    }
    @Override
    public boolean isStopping() {
        return jobStatus.isStopping();
    }
    @Override
    public boolean isCompleted() {
        return jobStatus.isCompleted();
    }
    @Override
    public boolean isPrematurlyEnded() {
        return jobStatus.isPrematurlyEnded();
    }
    @Override
    public boolean isRunning() {
        return jobStatus.isRunning();
    }
    @Override
    public boolean isState(JobState... states) {
        return jobStatus.isState(states);
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uid == null) ? 0 : uid.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JobStatusTreeNode other = (JobStatusTreeNode) obj;
        if (uid == null) {
            if (other.uid != null) {
                return false;
            }
        } else if (!uid.equals(other.uid)) {
            return false;
        }
        return true;
    }

    
    
    
    
}