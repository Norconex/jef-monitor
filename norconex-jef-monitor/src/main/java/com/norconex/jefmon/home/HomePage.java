package com.norconex.jefmon.home;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.commons.wicket.markup.head.HeaderContributor;
import com.norconex.jefmon.JEFMonPage;
import com.norconex.jefmon.instances.InstancesPanel;
import com.norconex.jefmon.instances.JEFMonInstance;

/**
 * JEF Web Monitoring main overview page.
 */
@SuppressWarnings("nls")
public class HomePage extends JEFMonPage {

    private static final long serialVersionUID = 6007545512735243708L;

    private String instanceURL;

    public HomePage() {
        super();
    }
    public HomePage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected IModel<String> getTitle() {
        return new ResourceModel("title");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        
        add(new HomePageHeader("header"));
        add(new InstancesPanel("instances") {
            private static final long serialVersionUID = 8327373240621977294L;
            @Override
            protected void onInstanceClick(AjaxRequestTarget target,
                    JEFMonInstance instance) {
                instanceURL = instance.getUrl();
                String pageURL = instanceURL;
                if (instanceURL == null) {
                    pageURL = "/";
                } else if (!instanceURL.endsWith("/")) {
                    pageURL += "/";
                }
                pageURL += "jobs";
                target.appendJavaScript("$(\"#instanceJobs\").attr(\"src\", \""
                        + pageURL + "\")");
            }
        });
        setOutputMarkupId(true);
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        HeaderContributor.addJavascript(response, HomePage.class, 
                HomePage.class.getSimpleName() + ".js");
        HeaderContributor.addCss(response, HomePage.class, 
                HomePage.class.getSimpleName() + ".css");
    }
}
