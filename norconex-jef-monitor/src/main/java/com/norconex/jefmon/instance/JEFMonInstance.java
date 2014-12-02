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
package com.norconex.jefmon.instance;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.wicket.util.lang.Objects;

import com.norconex.commons.lang.Sleeper;
import com.norconex.jef4.status.JobSuiteStatusSnapshot;
import com.norconex.jefmon.JEFMonConfig;

public class JEFMonInstance implements Serializable {

    private static final long serialVersionUID = 5607001500218702705L;

    private static final Logger LOG = LogManager.getLogger(
            JEFMonInstance.class);
    
    public static final long DEFAULT_SCAN_INTERVAL = 5 * 1000;
    
    private final Monitor monitor;

    public JEFMonInstance(JEFMonConfig config) {
        this(config, DEFAULT_SCAN_INTERVAL);
    }
    public JEFMonInstance(
            JEFMonConfig config, long scanInterval) {
        super();
        this.monitor = new Monitor(config, scanInterval);
    }

    public Collection<JobSuiteStatusSnapshot> getJobSuitesStatuses() {
        return monitor.getStatuses().values();
    }

    public JobSuiteStatusSnapshot getJobSuiteStatuses(String suiteId) {
        for (JobSuiteStatusSnapshot snapshot : getJobSuitesStatuses()) {
            if (Objects.equal(snapshot.getRoot().getJobId(), suiteId)) {
                return snapshot;
            }
        }
        return null;
    }

    
    public void startMonitoring() {
        new Thread(monitor).start();
    }

    public void stopMonitoring() {
        monitor.stopMe();
    }

    private static class Monitor implements Runnable, Serializable {

        private static final long serialVersionUID = 2775523547279413259L;
        
        private static final Map<File, JobSuiteStatusSnapshot> STATUSES =
                Collections.synchronizedMap(
                        new HashMap<File, JobSuiteStatusSnapshot>());
        private final JEFMonConfig cfg;
        private boolean running;
        private boolean stopme;
        private final long interval;
        
        public Monitor(JEFMonConfig cfg, long interval) {
            super();
            this.cfg = cfg;
            this.interval = interval;
        }

        private Map<File, JobSuiteStatusSnapshot> getStatuses() {
            return STATUSES;
        }
        
        @Override
        public void run() {
            if (running) {
                throw new IllegalStateException(
                        "JEFMonInstance already running.");
            }
            running = true;
            stopme = false;
            while (!stopme) {
                if (cfg.getMonitoredPaths() != null) {
                    syncIndexFiles();
                }
                Sleeper.sleepMillis(interval);
            }
            running = false;
        }
        
        public void stopMe() {
            stopme = true;
        }
        
        private void syncIndexFiles() {
            Set<File> files = new HashSet<>();
            for (File file : cfg.getMonitoredPaths()) {
                if (!file.exists()) {
                    continue;
                }
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    files.addAll(Arrays.asList(file.listFiles(
                            (FilenameFilter) new SuffixFileFilter(".index"))));
                }
            }
            // remove suite statuses those that no longer have an index file.
            STATUSES.keySet().retainAll(files);

            // add/replace suite statuses
            for (File file : files) {
                try {
                    STATUSES.put(file, 
                            JobSuiteStatusSnapshot.newSnapshot(file));
                } catch (IOException e) {
                    LOG.error("Cannot sync suite statuses for index file: "
                            + file, e);
                }
            }
        }
    }
}
