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
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import com.norconex.commons.lang.ClassFinder;
import com.norconex.jefmon.home.HomePage;
import com.norconex.jefmon.instance.InstancePage;
import com.norconex.jefmon.instance.JEFMonInstance;
import com.norconex.jefmon.instance.action.IJobAction;
import com.norconex.jefmon.markup.html.image.JEFImages;
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

//    private static final String PARAM_LOCALES = "locales";

    //private transient List<JobSuite> jobSuites;
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
//    public synchronized void setJEFMonConfig(JEFMonConfig monitorConfig) {
//        this.monitorConfig = monitorConfig;
////        resetClassResolver();
//        jobSuites = null;
//    }

    public Locale[] getSupportedLocales() {
        return supportedLocales;
    }

    public List<IJobAction> getAllJobsActions() {
        return allJobsActions;
    }
    
//    public Collection<JobSuiteStatusSnapshot> getJobSuitesStatuses() {
//        return statusesMonitor.getJobSuitesStatuses();
////        return JEFMonUtil.createJobSuites(monitorConfig);
//    }

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
        mountJEFImagesRef(JEFImages.REF_JOB_BLANK);
        mountJEFImagesRef(JEFImages.REF_JOB_ERROR);
        mountJEFImagesRef(JEFImages.REF_JOB_OK);
        mountJEFImagesRef(JEFImages.REF_JOB_RUNNING);
        
        
        getMarkupSettings().setStripWicketTags(true);

        statusesMonitor.startMonitoring();

        
        
        
        // Supported Locales
//        initSupportedLocales();
//
//        try {
//            this.monitorConfig = ConfigurationDAO.loadConfig();
//        } catch (Exception e) {
//            // TODO handle better
//            throw new RuntimeException(e);
//        }

        // TODO hide wicket tags


//        if (monitorConfig != null) {
//            resetClassResolver();
//        }
    }

    private void mountJEFImagesRef(ResourceReference ref) {
        String fullName = ref.getName();
        String fileName = StringUtils.substringAfterLast(fullName, "/");
        
//        mountResource(fullName, 
//                new PackageResourceReference(JEFImages.class, fileName));
        getSharedResources().add(fullName, new PackageResourceReference(
                JEFImages.class, fileName).getResource());
        mountResource(fullName, new SharedResourceReference(fullName));
    }
    
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
    
//    private void resetClassResolver() {
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("About to reset class resolver.");
//        }
//        // try {
//        // Thread.currentThread().setContextClassLoader(
//        // monitorConfig.getClassloader());
//        // } catch (MalformedURLException e) {
//        // throw new RuntimeException(
//        // "Cannot resolve class loader.", e);
//        // }
//        getApplicationSettings().setClassResolver(new IClassResolver() {
//            DefaultClassResolver resolver = new DefaultClassResolver();
//
//            @Override
//            public Class<?> resolveClass(String classname)
//                    throws ClassNotFoundException {
//                Class<?> clazz = Class.forName(classname, true,
//                        monitorConfig.getClassloader());
//                if (clazz == null) {
//                    clazz = resolver.resolveClass(classname);
//                }
//                return clazz;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public Iterator<URL> getResources(String name) {
//                Iterator<URL> urls;
//                try {
//                    urls = IteratorUtils.asIterator(monitorConfig
//                            .getClassloader().getResources(name));
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException("Cannot reset class resolver.",
//                            e);
//                } catch (IOException e) {
//                    throw new RuntimeException("Cannot reset class resolver.",
//                            e);
//                }
//                // TODO should we always append instead???
//                if (urls == null || !urls.hasNext()) {
//                    urls = resolver.getResources(name);
//                }
//                return urls;
//            }
//
//            // TODO is this new wicket 6.0 method working?
//            @Override
//            public ClassLoader getClassLoader() {
//                return monitorConfig.getClassloader();
//            }
//        });
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("Done resetting class resolver.");
//        }
//    }

//    private void initSupportedLocales() {
//        String localesString = getServletContext().getInitParameter(
//                PARAM_LOCALES);
//        if (localesString == null) {
//            LOG.warn("No supported locales specified. "
//                    + "Defaulting to English");
//            this.supportedLocales = new Locale[] { Locale.ENGLISH };
//            return;
//        }
//        String[] localeStrings = localesString.split(",");
//        Locale[] locales = new Locale[localeStrings.length];
//        for (int i = 0; i < localeStrings.length; i++) {
//            locales[i] = LocaleUtils.toLocale(localeStrings[i].trim());
//        }
//        this.supportedLocales = locales;
//    }
}