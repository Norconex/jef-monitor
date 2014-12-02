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
