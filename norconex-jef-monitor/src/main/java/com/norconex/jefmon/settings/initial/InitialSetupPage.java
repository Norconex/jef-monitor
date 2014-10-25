package com.norconex.jefmon.settings.initial;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonHeaderPanel;
import com.norconex.jefmon.JEFMonPage;
import com.norconex.jefmon.home.HomePage;
import com.norconex.jefmon.settings.panels.IdentityPanel;
import com.norconex.jefmon.settings.panels.JobActionsPanel;
import com.norconex.jefmon.settings.panels.JobLocationsPanel;

/**
 * JEF Web Monitoring setup page.
 */
@SuppressWarnings("nls")
public class InitialSetupPage extends JEFMonPage {

    private static final long serialVersionUID = -5929479500979931196L;

    private final CircularFifoQueue<IStep> steps =
            new CircularFifoQueue<IStep>();

    private float initialStepCount;
    private Component currentStep;
    private final JEFMonConfig dirtyConfig = new JEFMonConfig();

    public InitialSetupPage() {
        super();
    }
    public InitialSetupPage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected IModel<String> getTitle() {
        return new ResourceModel("settings.title");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new JEFMonHeaderPanel("header"));
        
        steps.addAll(buildSteps());
        currentStep = steps.remove().createStep("step");
        initialStepCount = steps.size();

        final Form<Void> setupForm = new Form<Void>("form") {
            private static final long serialVersionUID = 6003112987882939981L;

            @Override
            protected void onBeforeRender() {
                addOrReplace(currentStep);
                super.onBeforeRender();
            }
        };
        setupForm.setOutputMarkupId(true);
        add(setupForm);

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        setupForm.add(feedback);

        final AjaxButton continueButton = new AjaxButton("continue") {
            private static final long serialVersionUID = 1L;
            @Override
            protected void onAfterSubmit(
                    AjaxRequestTarget target, Form<?> form) {
                boolean done = true;
                if (!steps.isEmpty()) {
                    IStep step = steps.remove();
                    if (step != null) {
                        done = false;
                        currentStep = step.createStep("step");
                        target.add(form);
//                        target.add(feedback);
                        // target.add(progressBar);
                    }
                }
                if (done) {
                    setResponsePage(HomePage.class);
                }
            }
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        };
        continueButton.add(new Label("continueLabel", new Model<String>() {
            private static final long serialVersionUID = -7480334927080818811L;

            @Override
            public String getObject() {
                if (!isStarted()) {
                    return getString("btn.start");
                }
                return getString("btn.continue");
            }
        }));
        setupForm.add(continueButton);
        setupForm.setDefaultButton(continueButton);
    }

    private boolean isStarted() {
        return steps.size() != initialStepCount;
    }

    private List<IStep> buildSteps() {
        return Arrays.asList(new IStep() {
            private static final long serialVersionUID = -769448771139201001L;
            @Override
            public Component createStep(String id) {
                return new IntroPanel(id);
            }
        }, new IStep() {
            private static final long serialVersionUID = -7526554774061290757L;
            @Override
            public Component createStep(String id) {
                return new IdentityPanel(id, dirtyConfig);
            }
        }, new IStep() {
            private static final long serialVersionUID = 2545401458451581290L;
            @Override
            public Component createStep(String id) {
                return new JobLocationsPanel(id, dirtyConfig);
            }
        }, new IStep() {
            private static final long serialVersionUID = -4380602772462992401L;
            @Override
            public Component createStep(String id) {
                return new JobActionsPanel(id, dirtyConfig);
            }
        }, new IStep() {
            private static final long serialVersionUID = -769448771139201001L;
            @Override
            public Component createStep(String id) {
                return new ConclusionPanel(id, dirtyConfig);
            }
        });
    }

    interface IStep extends Serializable {
        Component createStep(String id);
    }

}
