package com.norconex.jefmon;

import java.io.File;
import java.io.Serializable;

import com.norconex.jefmon.instance.action.IJobAction;

public class JEFMonConfig implements Serializable {

    public static final int DEFAULT_REFRESH_INTERVAL = 5;

    private static final long serialVersionUID = -5553575452353106467L;

//    private JobSuiteFactoryConfig[] suiteFactoryConfigs;
    private IJobAction[] jobActions;
//    private File[] classpathFiles;
//    private transient ClassLoader classloader;
    private int defaultRefreshInterval = DEFAULT_REFRESH_INTERVAL;
    private String instanceName;
    private String[] remoteInstanceUrls;
    private File[] monitoredPaths;
    
    public JEFMonConfig() {
        super();
    }

//    public JobSuiteFactoryConfig[] getSuiteFactoryConfigs() {
//        return suiteFactoryConfigs;
//    }
//    public void setSuiteFactoryConfigs(
//            JobSuiteFactoryConfig[] suiteFactoryConfigs) {
//        this.suiteFactoryConfigs = suiteFactoryConfigs;
//    }
    public IJobAction[] getJobActions() {
        return jobActions;
    }
    public void setJobActions(IJobAction[] actionClasses) {
        this.jobActions = actionClasses;
    }
//    public File[] getClasspathFiles() {
//        return classpathFiles;
//    }
//    public void setClasspathFiles(File[] classpathFiles) {
//        this.classpathFiles = classpathFiles;
//        this.classloader = null;
//    }
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

    
    
    
//    @SuppressWarnings("nls")
//    public ClassLoader getClassloader() {
//        if (classloader == null) {
//            if (classpathFiles != null) {
//                URL[] urls = new URL[classpathFiles.length];
//                for (int i = 0; i < urls.length; i++) {
//                    try {
//                        urls[i] = classpathFiles[i].toURI().toURL();
//                    } catch (MalformedURLException e) {
//                        throw new WicketRuntimeException(
//                                "Invalid classpath URL/Location: "
//                                        + classpathFiles[i]);
//                    }
//                }
//                classloader = new URLClassLoader(
//                        urls, JEFMonConfig.class.getClassLoader());
//            } else {
//                classloader = JEFMonConfig.class.getClassLoader();
//            }
//        }
//        return classloader;
//    }

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
    
//    public void replaceWith(JEFMonConfig that) {
//        this.defaultRefreshInterval = that.defaultRefreshInterval;
//        this.instanceName = that.instanceName;
//        this.remoteInstanceUrls = that.remoteInstanceUrls;
//        this.monitoredPaths = that.monitoredPaths;
//    }
}
