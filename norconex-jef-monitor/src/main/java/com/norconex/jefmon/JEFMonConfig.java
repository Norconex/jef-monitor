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
package com.norconex.jefmon;

import java.io.File;
import java.io.Serializable;

import com.norconex.jefmon.instance.action.IJobAction;

public class JEFMonConfig implements Serializable {

    public static final int DEFAULT_REFRESH_INTERVAL = 5;

    private static final long serialVersionUID = -5553575452353106467L;

    private IJobAction[] jobActions;
    private int defaultRefreshInterval = DEFAULT_REFRESH_INTERVAL;
    private String instanceName;
    private String[] remoteInstanceUrls;
    private File[] monitoredPaths;
    
    public JEFMonConfig() {
        super();
    }

    public IJobAction[] getJobActions() {
        return jobActions;
    }
    public void setJobActions(IJobAction[] actionClasses) {
        this.jobActions = actionClasses;
    }
    public String getInstanceName() {
        return instanceName;
    }
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
    public String[] getRemoteInstanceUrls() {
        return remoteInstanceUrls;
    }
    public void setRemoteInstanceUrls(String[] remoteInstances) {
        this.remoteInstanceUrls = remoteInstances;
    }

    public File[] getMonitoredPaths() {
        return monitoredPaths;
    }

    public void setMonitoredPaths(File[] monitoredLocations) {
        this.monitoredPaths = monitoredLocations;
    }

    public int getDefaultRefreshInterval() {
        return defaultRefreshInterval;
    }

    public void setDefaultRefreshInterval(int defaultRefreshInterval) {
        this.defaultRefreshInterval = defaultRefreshInterval;
    }
}
