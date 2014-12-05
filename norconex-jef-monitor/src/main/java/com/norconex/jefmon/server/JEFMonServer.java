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
package com.norconex.jefmon.server;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.application.DefaultClassResolver;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import com.norconex.commons.lang.map.Properties;
import com.norconex.jefmon.JEFMonApplication;
import com.norconex.jefmon.JEFMonException;
import com.norconex.jefmon.model.ConfigurationDAO;

@SuppressWarnings("nls")
public class JEFMonServer {
    
    private static final Logger LOG =
            LogManager.getLogger(JEFMonServer.class);
    
    private static final String CONFIG_PROPERTIES = "config/setup.properties";

    private static final String JEFMON_MAPPING = "/*";
    
    private final Server server = new Server();
    private final JEFMonApplication app;
    private final int port;
    
    public JEFMonServer(int port, boolean https, Locale[] locales) {
        this.port = port;
        this.app = new JEFMonApplication(
                ConfigurationDAO.loadConfig(), locales);
        
        initRuntimeConfiguration();
        
        WebAppContext webappContext = buildWebappContext();
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { webappContext });
        server.setHandler(handlers);
        
        if (https) {
            initHttpsConnector();
        } else {
            initHttpConnector();
        }
    }
    
    /**
     * Set configuration type by looking for dev.properties file.
     * 
     * This method must be called very early.
     * 
     * The default behavior is to set as RuntimeConfigurationType.DEPLOYMENT 
     */
    public void initRuntimeConfiguration() {
        
        URL resource = new DefaultClassResolver().
                getClassLoader().getResource("dev.properties");
        if (resource == null) {
            app.setConfigurationType(RuntimeConfigurationType.DEPLOYMENT);
            return;
        }
        
        try
        {
            PropertiesConfiguration properties = 
                    new PropertiesConfiguration(resource);
            String value = properties.getString(
                    "wicket." + Application.CONFIGURATION, 
                    RuntimeConfigurationType.DEPLOYMENT.toString());
            app.setConfigurationType(
                    RuntimeConfigurationType.valueOf(
                            value.toString().toUpperCase()));
        } catch (ConfigurationException e) {
            throw new JEFMonException(
                    "Exception while reading dev.propeties.", e);
        }
    }

    private WebAppContext buildWebappContext() {
        
        WebAppContext webappContext = new WebAppContext();
        webappContext.setResourceBase("/");
        
        // Add Wicket filter
        WicketFilter filter = new WicketFilter(app);
        FilterHolder filterHolder = new FilterHolder(filter);
        filterHolder.setInitParameter(
                WicketFilter.FILTER_MAPPING_PARAM, JEFMON_MAPPING);
        filterHolder.setInitParameter("encoding", "UTF-8");
        filterHolder.setInitParameter("forceEncoding", "true");
        webappContext.addFilter(
                filterHolder, 
                JEFMON_MAPPING, 
                EnumSet.of(DispatcherType.REQUEST));
        
        // Add custom error message
        webappContext.setErrorHandler(new ErrorHandler() {
            protected void writeErrorPageBody(
                    HttpServletRequest request, 
                    Writer writer, 
                    int code, 
                    String message, 
                    boolean showStacks) throws IOException {
                String uri= request.getRequestURI();
                writeErrorPageMessage(request,writer,code,message,uri);
                if (showStacks)
                    writeErrorPageStacks(request,writer);
                writer.write("<hr><i><small>"
                        + "Norconex JEF Monitor</small></i><hr/>\n");
            }
        });
        
        return webappContext;
    }
    
    private void initHttpConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);
    }
    
    private void initHttpsConnector() {
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
        URL keystore = 
                JEFMonServer.class.getClassLoader().getResource("keystore");
        SslContextFactory sslContextFactory = 
                new SslContextFactory(keystore.toExternalForm());
        // TODO use jasypt to handle password
        sslContextFactory.setKeyStorePassword("vyU2Pk_Q-2");
        sslContextFactory.setKeyManagerPassword("vyU2Pk_Q-2");
        ServerConnector sslConnector = 
                new ServerConnector(server, 
                        new SslConnectionFactory(
                                sslContextFactory, "http/1.1"),
                        new HttpConnectionFactory(https));
        sslConnector.setPort(port);
        server.addConnector(sslConnector);
    }
    
    public void run() throws Exception {
        server.start();
        LOG.info("JEF Monitor server started.");
        server.join();
    }
    
    public void stop() throws Exception {
        server.stop();
        LOG.info("JEF Monitor server stopped.");
        server.join();
    }

    public static void main(String[] args) throws Exception {
        Properties props = getSetupProperties();

        String localesString = props.getString("locales");
        String[] localeStrings = localesString.split(",");
        Locale[] locales = new Locale[localeStrings.length];
        for (int i = 0; i < localeStrings.length; i++) {
            locales[i] = LocaleUtils.toLocale(localeStrings[i].trim());
        }
        
        final JEFMonServer server = new JEFMonServer(
                props.getInt("port", 80), 
                props.getBoolean("https", false),
                locales);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        server.run();
    }

    private static Properties getSetupProperties() throws IOException {
        File setupFile = new File(CONFIG_PROPERTIES);
        if (!setupFile.exists()) {
            System.err.println("Missing file " + CONFIG_PROPERTIES);
            System.exit(-1);
        }
        Properties props = new Properties();
        FileReader reader = new FileReader(setupFile);
        props.load(reader);
        reader.close();
        return props;
    }
}
