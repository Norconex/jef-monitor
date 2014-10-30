package com.norconex.jefmon.instance.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.norconex.commons.lang.map.Properties;
import com.norconex.jef4.log.ILogManager;
import com.norconex.jef4.status.IJobStatus;
import com.norconex.jef4.status.JobDuration;
import com.norconex.jef4.status.JobState;
import com.norconex.jef4.status.JobSuiteStatusSnapshot;
import com.norconex.jefmon.instance.JEFMonInstance;

public class JobStatusTreeNode implements IJobStatus, Serializable {

    private static final long serialVersionUID = -1114163221300422823L;

    //TODO create a dynamic way to get statuses and snapshots so we can remove
    //the need for transient
//    private transient IJobStatus jobStatus;
//    private transient JobSuiteStatusSnapshot suiteStatusSnapshot;
    
    
    private final boolean root;
    private final String uid;
    private final String suiteId;
    private final String jobId;
    
    private final JEFMonInstance instance;
    
//    public JobStatusTreeNode(
//            IJobStatus jobStatus, JobSuiteStatusSnapshot suiteStatuses) {
//        this(jobStatus, suiteStatuses, false);
//    }
//    public JobStatusTreeNode(IJobStatus jobStatus, 
//            JobSuiteStatusSnapshot suiteStatuses, boolean root) {
//        super();
//        this.jobStatus = jobStatus;
//        this.root = root;
//        this.suiteStatusSnapshot = suiteStatuses;
//        this.uid = suiteStatuses.getRoot().getJobId()
//                + "__" + jobStatus.getJobId();
//    }

    
    public JobStatusTreeNode(JEFMonInstance instance, String suiteId, 
            String jobId, boolean root) {
        super();
        this.instance = instance;
        this.suiteId = suiteId;
        this.jobId = jobId;
//        this.jobStatus = jobStatus;
        this.root = root;
//        this.suiteStatusSnapshot = suiteStatuses;
        
//        this.uid = suiteStatuses.getRoot().getJobId()
//                + "__" + jobStatus.getJobId();
        this.uid = suiteId + "__" + jobId;
    }

    
    
    public boolean isRoot() {
        return root;
    }
    public boolean hasChildren() {
        JobSuiteStatusSnapshot snapshot = getSuiteStatusSnapshot();
        if (snapshot != null) {
            return !snapshot.getChildren(jobId).isEmpty();
        }
        return false;
    }
    public List<JobStatusTreeNode> getChildren() {
        JobSuiteStatusSnapshot snapshot = getSuiteStatusSnapshot();
        if (snapshot != null) {
            List<IJobStatus> statuses = snapshot.getChildren(jobId);
            List<JobStatusTreeNode> nodes = new ArrayList<>(statuses.size());
            for (IJobStatus status : statuses) {
                nodes.add(new JobStatusTreeNode(
                        instance, suiteId, status.getJobId(), false));
            }
            return nodes;
        }
        return new ArrayList<>();
    }
    
    public JobSuiteStatusSnapshot getSuiteStatusSnapshot() {
        return instance.getJobSuiteStatuses(suiteId);
    }
    public ILogManager getLogManager() {
        JobSuiteStatusSnapshot snapshot = getSuiteStatusSnapshot();
        if (snapshot != null) {
            return snapshot.getLogManager();
        }
        return null;
    }

    
    public String getSuiteId() {
        return suiteId;
    }
    @Override
    public String getJobId() {
        return jobId;
    }
    @Override
    public JobState getState() {
        return getJobStatus().getState();
    }
    @Override
    public double getProgress() {
        return getJobStatus().getProgress();
    }
    @Override
    public String getNote() {
        return getJobStatus().getNote();
    }
    @Override
    public int getResumeAttempts() {
        return getJobStatus().getResumeAttempts();
    }
    @Override
    public JobDuration getDuration() {
        return getJobStatus().getDuration();
    }
    @Override
    public Date getLastActivity() {
        return getJobStatus().getLastActivity();
    }
    @Override
    public Properties getProperties() {
        return getJobStatus().getProperties();
    }
    @Override
    public boolean isStarted() {
        return getJobStatus().isStarted();
    }
    @Override
    public boolean isResumed() {
        return getJobStatus().isResumed();
    }
    @Override
    public boolean isAborted() {
        return getJobStatus().isAborted();
    }
    @Override
    public boolean isStopped() {
        return getJobStatus().isStopped();
    }
    @Override
    public boolean isStopping() {
        return getJobStatus().isStopping();
    }
    @Override
    public boolean isCompleted() {
        return getJobStatus().isCompleted();
    }
    @Override
    public boolean isPrematurlyEnded() {
        return getJobStatus().isPrematurlyEnded();
    }
    @Override
    public boolean isRunning() {
        return getJobStatus().isRunning();
    }
    @Override
    public boolean isState(JobState... states) {
        return getJobStatus().isState(states);
    }
    
    private IJobStatus getJobStatus() {
        JobSuiteStatusSnapshot snapshot = getSuiteStatusSnapshot();
        if (snapshot != null) {
            return snapshot.getJobStatus(jobId);
        }
        return null;
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