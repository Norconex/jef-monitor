package com.norconex.jefmon.instance.action.impl;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;

import com.norconex.commons.lang.file.FileUtil;
import com.norconex.commons.lang.io.IInputStreamFilter;
import com.norconex.jef4.log.FileLogManager;
import com.norconex.jef4.log.ILogManager;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;

@SuppressWarnings("nls")
public class JefLogLinesReader extends LinesReader {

    private static final long serialVersionUID = 818718054537660673L;
//    private final ILogManager logManager;
//    private final String namespace;
//    private final String jobId;
    private final JobStatusTreeNode jobStatus;
    private final boolean filterOnJob;
    
//    public JefLogLinesReader(
//            ILogManager logManager, String namespace, String jobId) {
//        this.logManager = logManager;
//        this.jobId = jobId;
//        this.namespace = namespace;
//    }
    public JefLogLinesReader(JobStatusTreeNode jobStatus, boolean filterOnJob) {
        this.jobStatus = jobStatus;
        this.filterOnJob = filterOnJob;
//        this.logManager = logManager;
//        this.jobId = jobId;
//        this.namespace = namespace;
    }

    
    @Override
    public String[] readLines() throws IOException {
        if (jobStatus.getLogManager() instanceof FileLogManager) {
            return readFileManagerLines();
        }
        return null;
    }
    private String[] readFileManagerLines() throws IOException {
        ILogManager logManager = jobStatus.getLogManager();
        File file = ((FileLogManager) logManager).getLogFile(
                jobStatus.getSuiteId());
        String[] lines;
        if (LinesReader.MODE_HEAD.equalsIgnoreCase(getReadMode())) {
            lines = FileUtil.head(file, SystemUtils.FILE_ENCODING,
                    getLineQty(), true, new JobIdLinesFilter());
        } else {
            lines = FileUtil.tail(file, SystemUtils.FILE_ENCODING,
                    getLineQty(), true, new JobIdLinesFilter());
        }
        if (LinesReader.STYLE_COMPACT.equalsIgnoreCase(getLineStye())) {
            for (int i = 0; i < lines.length; i++) {
                lines[i] = lines[i].replaceFirst(".*?\\[.*?\\].*? - ", "");
            }
        }
        return lines;
    }
    
    private class JobIdLinesFilter implements IInputStreamFilter {
        public JobIdLinesFilter() {
            super();
        }
        @Override
        public boolean accept(String line) {
            if (filterOnJob) {
                return line.matches(".*?\\[" 
                        + Pattern.quote(jobStatus.getJobId()) + "\\].*? - .*$");
//                return line.startsWith(jobStatus.getJobId() + ":");
            }
            return true;
        }
    }
}
