package com.norconex.jefmon.instances;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;

import com.norconex.jef4.status.JobState;

public class InstanceSummary implements Serializable {

    private static final long serialVersionUID = 932346719888464820L;

    private final String url;
    private String name;
    private int totalRoots;
    private final Map<JobState, MutableInt> statuses = new HashMap<>();
    private String remoteError;

    public InstanceSummary(String url) {
        super();
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalRoots() {
        return totalRoots;
    }

    public void setTotalRoots(int totalRoots) {
        this.totalRoots = totalRoots;
    }

    public Map<JobState, MutableInt> getStatuses() {
        return statuses;
    }

    public String getRemoteError() {
        return remoteError;
    }
    public void setRemoteError(String remoteError) {
        this.remoteError = remoteError;
    }

    public boolean isInvalid() {
        return StringUtils.isNotBlank(remoteError);
    }
    public boolean isThisInstance() {
        return url == null;
    }
}
