/* Copyright 2007-2017 Norconex Inc.
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
            xml.setDelimiterParsingDisabled(true);
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
            try {
                IJobAction action = 
                        (IJobAction) Class.forName(actionClass).newInstance();
                actions.add(action);
            } catch (ClassNotFoundException e) {
                LOG.error("Could not load action: " + actionClass, e);
            }
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

    private static int loadDefaultRefreshInterval(XMLConfiguration xml) {
        return xml.getInt("default-refresh-interval", //$NON-NLS-1$
                JEFMonConfig.DEFAULT_REFRESH_INTERVAL);
    }

}
