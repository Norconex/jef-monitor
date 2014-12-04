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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

import com.norconex.commons.lang.ClassFinder;
import com.norconex.jefmon.home.HomePage;
import com.norconex.jefmon.instance.InstancePage;
import com.norconex.jefmon.instance.JEFMonInstance;
import com.norconex.jefmon.instance.action.IJobAction;
import com.norconex.jefmon.settings.initial.InitialSetupPage;
import com.norconex.jefmon.settings.update.SettingsPage;
import com.norconex.jefmon.ws.JobSuiteProgressJsonPage;

/**
 * Application object for JEF web application.
 *
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public class JEFMonApplication extends WebApplication {
    static {
        System.setProperty("java.awt.headless", "true");
    }
    private static final Logger LOG = 
            LogManager.getLogger(JEFMonApplication.class);

    private final JEFMonInstance statusesMonitor;
    private final JEFMonConfig monitorConfig;
    private final Locale[] supportedLocales;
    private final List<IJobAction> allJobsActions = new ArrayList<>();

    public JEFMonApplication(JEFMonConfig config, Locale[] supportedLocales) {
        super();
        this.supportedLocales = supportedLocales;
        if (config == null) {
            this.monitorConfig = new JEFMonConfig();
        } else {
            this.monitorConfig = config;
        }
        this.statusesMonitor = new JEFMonInstance(this.monitorConfig);
        
    }

    @Override
    public Class<? extends JEFMonPage> getHomePage() {
        if (StringUtils.isNotBlank(monitorConfig.getInstanceName())) {
            return HomePage.class;
        }
        return InitialSetupPage.class;
    }

    public static JEFMonApplication get() {
        WebApplication application = WebApplication.get();
        if (application instanceof JEFMonApplication == false) {
            throw new WicketRuntimeException(
                    "The application attached to the current thread is not a "
                  + JEFMonApplication.class.getSimpleName());
        }
        return (JEFMonApplication) application;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new JEFMonSession(request);
    }

    public JEFMonConfig getConfig() {
        return monitorConfig;
    }

    public Locale[] getSupportedLocales() {
        return supportedLocales;
    }

    public List<IJobAction> getAllJobsActions() {
        return allJobsActions;
    }
    
    public JEFMonInstance getJobSuitesStatusesMonitor() {
        return statusesMonitor;
    }

    @Override
    protected void init() {

        initJobActions();

        
        // mountPage("/initialsetup", InitialSetupPage.class);
        mountPage("settings", SettingsPage.class);
//        mountPage("jobs/xml", JobProgressXmlPage.class);
        mountPage("suites/json", JobSuiteProgressJsonPage.class);
        mountPage("jobs", InstancePage.class);

        //Mount shared resources
//        mountJEFImagesRef(JEFImages.REF_JOB_BLANK);
//        mountJEFImagesRef(JEFImages.REF_JOB_ERROR);
//        mountJEFImagesRef(JEFImages.REF_JOB_OK);
//        mountJEFImagesRef(JEFImages.REF_JOB_RUNNING);
        
        
        getMarkupSettings().setStripWicketTags(true);

        statusesMonitor.startMonitoring();

    }

//    private void mountJEFImagesRef(ResourceReference ref) {
//        String fullName = ref.getName();
//        String fileName = StringUtils.substringAfterLast(fullName, "/");
//        
//        getSharedResources().add(fullName, new PackageResourceReference(
//                JEFImages.class, fileName).getResource());
//        mountResource(fullName, new SharedResourceReference(fullName));
//    }
    
    private void initJobActions() {
        List<String> classes = ClassFinder.findSubTypes(IJobAction.class);
        for (String className : classes) {
            IJobAction action;
            try {
                action = (IJobAction) ClassUtils.getClass(
                        className).newInstance();
                allJobsActions.add(action);
            } catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException e) {
                LOG.error("Cannot instantiate job action: " + className, e);
            }
        }
    }
    
    
    @Override
    protected void onDestroy() {
        statusesMonitor.stopMonitoring();
        super.onDestroy();
    }
    
}