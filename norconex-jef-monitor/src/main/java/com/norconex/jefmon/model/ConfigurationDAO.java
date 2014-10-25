package com.norconex.jefmon.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.norconex.commons.lang.config.ConfigurationUtil;
import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonException;
import com.norconex.jefmon.instance.action.IJobAction;

@SuppressWarnings({ "nls" })
public final class ConfigurationDAO {

    public static final File CONFIG_FILE =
            new File("./config/jefconfig.xml").getAbsoluteFile();

    /** Logging */
    private static final Logger LOG =
            LogManager.getLogger(ConfigurationDAO.class);

    public static JEFMonConfig loadConfig() {
//            throws FileNotFoundException,
//                   ConfigurationException,
//                   MalformedURLException,
//                   ClassNotFoundException {
        if (!CONFIG_FILE.exists()) {
            LOG.info("JEF Monitor configuration file not "
                    + "found: \"" + CONFIG_FILE
                    + "\". Assuming first time access of the application. ");
            return null;
        }
        if (!CONFIG_FILE.isFile()) {
            LOG.info("JEF Monitor configuration path exists but does not "
                    + "appear to be a file: \"" + CONFIG_FILE
                    + "\".  This will likely cause startup issues.  Make sure "
                    + "the path is clear for the config file to be created.");
            return null;
        }
        JEFMonConfig config = new JEFMonConfig();
        LOG.info("Loading JEF Monitor configuration file: " + CONFIG_FILE);
        try {
            XMLConfiguration xml = new XMLConfiguration(CONFIG_FILE);
            ConfigurationUtil.disableDelimiterParsing(xml);
            config.setInstanceName(xml.getString("instance-name", "[?]"));
            
            LOG.debug("Loading remote instances...");
            config.setRemoteInstanceUrls(loadRemoteUrls(xml));
            LOG.debug("Loading monitored paths...");
            config.setMonitoredPaths(loadMonitoredPaths(xml));
            LOG.debug("Loading default refresh interval...");
            config.setDefaultRefreshInterval(loadDefaultRefreshInterval(xml));
            LOG.debug("Loading job actions...");
            config.setJobActions(loadJobActions(xml));
            LOG.info("JEF Monitor configuration file loaded.");
        } catch (Exception e) {
            throw new JEFMonException(
                    "Cannot load JEF Monitor configuration.", e);
        }


//        LOG.debug("Loading ClassPathFiles...");
//        config.setClasspathFiles(loadClasspath(xml));
//        LOG.debug("Loading job suites...");
//        config.setSuiteFactoryConfigs(
//                loadJobSuites(xml, config.getClassloader()));
//        LOG.debug("Loading action classes...");
//        config.setActionClasses(loadJobActions(xml, config.getClassloader()));
        return config;
    }


    public static void saveConfig(JEFMonConfig config) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Saving JEF config to: " + CONFIG_FILE);
        }

        if (!CONFIG_FILE.exists()) {
            File configDir = new File(
                    FilenameUtils.getFullPath(CONFIG_FILE.getAbsolutePath()));
            if (!configDir.exists()) {
                LOG.debug("Creating JEF Monitor config directory for: "
                        + CONFIG_FILE);
                configDir.mkdirs();
            }
        }

//        TransformerFactory.newInstance().newTransformer(null).t
        OutputStream out = new FileOutputStream(CONFIG_FILE);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter xml = factory.createXMLStreamWriter(out);
            xml.writeStartDocument();
            xml.writeStartElement("jefmon-config");

            xml.writeStartElement("instance-name");
            xml.writeCharacters(config.getInstanceName());
            xml.writeEndElement();

            xml.writeStartElement("default-refresh-interval");
            xml.writeCharacters(
                    Integer.toString(config.getDefaultRefreshInterval()));
            xml.writeEndElement();

            saveRemoteUrls(xml, config.getRemoteInstanceUrls());
            saveMonitoredPaths(xml, config.getMonitoredPaths());
            saveJobActions(xml, config.getJobActions());
            
            xml.writeEndElement();
            xml.writeEndDocument();
            xml.flush();
            xml.close();
        } catch (XMLStreamException e) {
            throw new IOException("Cannot save as XML.", e);
        }

        
//        saveClasspath(out, config.getClasspathFiles());
//        saveJobSuites(out, config.getSuiteFactoryConfigs());
//        saveJobActions(out, config.getActionClasses());
        out.close();
    }

    private static void saveJobActions(XMLStreamWriter xml, 
            IJobAction[] jobActions) throws XMLStreamException {
        if (jobActions == null) {
            return;
        }
        xml.writeStartElement("job-actions");
        for (IJobAction action : jobActions) {
            xml.writeStartElement("action");
            xml.writeCharacters(action.getClass().getName());
            xml.writeEndElement();
        }
        xml.writeEndElement();
    }
    
    private static void saveMonitoredPaths(XMLStreamWriter xml, 
            File[] monitoredPaths) throws XMLStreamException {
        if (monitoredPaths == null) {
            return;
        }
        xml.writeStartElement("monitored-paths");
        for (File path : monitoredPaths) {
            xml.writeStartElement("path");
            xml.writeCharacters(path.getAbsolutePath());
            xml.writeEndElement();
        }
        xml.writeEndElement();
    }
    private static IJobAction[] loadJobActions(XMLConfiguration xml)
            throws InstantiationException, 
                    IllegalAccessException, ClassNotFoundException {
        List<HierarchicalConfiguration> nodes =
                xml.configurationsAt("job-actions.action");
        List<IJobAction> actions = new ArrayList<IJobAction>();
        for (HierarchicalConfiguration node : nodes) {
            String actionClass = node.getString("");
            IJobAction action = 
                    (IJobAction) Class.forName(actionClass).newInstance();
            actions.add(action);
        }
        return actions.toArray(new IJobAction[]{});
    }
    private static File[] loadMonitoredPaths(XMLConfiguration xml) {
        List<HierarchicalConfiguration> nodes =
                xml.configurationsAt("monitored-paths.path");
        List<File> paths = new ArrayList<File>();
        for (HierarchicalConfiguration node : nodes) {
            paths.add(new File(node.getString("")));
        }
        return paths.toArray(new File[]{});
    }
    
    private static void saveRemoteUrls(
            XMLStreamWriter xml, String[] remoteUrls)
                    throws XMLStreamException {
        if (remoteUrls == null) {
            return;
        }
        xml.writeStartElement("remote-instances");
        for (String url : remoteUrls) {
            xml.writeStartElement("url");
            xml.writeCharacters(url);
            xml.writeEndElement();
        }
        xml.writeEndElement();
    }
    private static String[] loadRemoteUrls(XMLConfiguration xml) {
        List<HierarchicalConfiguration> nodes =
                xml.configurationsAt("remote-instances.url");
        List<String> urls = new ArrayList<String>();
        for (HierarchicalConfiguration node : nodes) {
            urls.add(node.getString(""));
        }
        return urls.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
    }

//    private static void saveClasspath(
//            PrintWriter out, File[] classpathFiles) {
//        if (classpathFiles == null) {
//            return;
//        }
//        out.println("  <classpath>");
//        for (File file : classpathFiles) {
//            out.print("    <path>");
//            out.print(xmlEsc(file.getAbsolutePath()));
//            out.println("</path>");
//        }
//        out.println("  </classpath>");
//    }
//
//    private static File[] loadClasspath(XMLConfiguration xml) {
//        List<HierarchicalConfiguration> nodes =
//                xml.configurationsAt("classpath.path");
//        List<File> files = new ArrayList<File>();
//        for (HierarchicalConfiguration node : nodes) {
//            files.add(new File(node.getString("")));
//        }
//        return files.toArray(new File[]{});
//    }
//
//    private static void saveJobSuites(
//            PrintWriter out, JobSuiteFactoryConfig[] factoryConfigs) {
//        if (factoryConfigs == null) {
//            return;
//        }
//        out.println("  <job-suites>");
//        for (int i = 0; i < factoryConfigs.length; i++) {
//            JobSuiteFactoryConfig factoryConfig = factoryConfigs[i];
//            out.print("    <suite factory=\"");
//            out.print(xmlEsc(factoryConfig.getFactoryClass()));
//            out.println("\">");
//            Map<String, String> props = factoryConfig.getProperties();
//            for (String key : props.keySet()) {
//                String value = props.get(key);
//                out.print("      <attribute name=\"");
//                out.print(xmlEsc(key));
//                out.print("\">");
//                out.print(xmlEsc(value));
//                out.println("</attribute>");
//            }
//            out.println("    </suite>");
//        }
//        out.println("  </job-suites>");
//    }
//
//    private static JobSuiteFactoryConfig[] loadJobSuites(
//            XMLConfiguration xml, ClassLoader classloader)
//            throws ClassNotFoundException {
//        List<HierarchicalConfiguration> nodes =
//                xml.configurationsAt("job-suites.suite");
//        if (LOG.isDebugEnabled()) {
//            if (nodes != null) {
//                LOG.debug("Found " + nodes.size() + " job suites");
//            } else {
//                LOG.debug("Found no job suites");
//            }
//        }
//        List<JobSuiteFactoryConfig> suiteConfigs =
//                new ArrayList<JobSuiteFactoryConfig>();
//        for (HierarchicalConfiguration node : nodes) {
//            String factoryClass = node.getString("[@factory]");
//            JobSuiteFactoryConfig suiteConfig =
//                    new JobSuiteFactoryConfig(factoryClass);
//            Map<String, String> properties = suiteConfig.getProperties();
//            List<HierarchicalConfiguration> attributes =
//                    node.configurationsAt("attribute");
//            for (HierarchicalConfiguration attribute : attributes) {
//                properties.put(
//                        attribute.getString("[@name]"),
//                        attribute.getString(""));
//            }
//            suiteConfigs.add(suiteConfig);
//        }
//        return suiteConfigs.toArray(new JobSuiteFactoryConfig[]{});
//    }
//
//    private static void saveJobActions(
//            PrintWriter out, String[] jobActions) {
//        if (jobActions == null) {
//            return;
//        }
//        out.println("  <job-actions>");
//        for (int i = 0; i < jobActions.length; i++) {
//            String action = jobActions[i];
//            out.print("    <action class=\"");
//            out.print(xmlEsc(action));
//            out.println("\"/>");
//        }
//        out.println("  </job-actions>");
//    }
//
//    private static String[] loadJobActions(
//            XMLConfiguration xml, ClassLoader classloader)
//            throws ClassNotFoundException {
//        List<HierarchicalConfiguration> nodes =
//                xml.configurationsAt("job-actions.action");
//        List<String> actions = new ArrayList<String>();
//        for (HierarchicalConfiguration node : nodes) {
//            String actionClass = node.getString("[@class]");
//            actions.add(actionClass);
//        }
//        return actions.toArray(new String[]{});
//    }

    private static int loadDefaultRefreshInterval(XMLConfiguration xml) {
        return xml.getInt("default-refresh-interval", //$NON-NLS-1$
                JEFMonConfig.DEFAULT_REFRESH_INTERVAL);
    }


//
//    private static String xmlEsc(String value) {
//        if (value != null) {
//            return StringEscapeUtils.escapeXml(value);
//        }
//        return StringUtils.EMPTY;
//    }
}
