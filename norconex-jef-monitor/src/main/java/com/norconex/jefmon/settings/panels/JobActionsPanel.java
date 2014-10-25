package com.norconex.jefmon.settings.panels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;

import com.norconex.commons.wicket.behaviors.CssClass;
import com.norconex.jefmon.JEFMonApplication;
import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.instance.action.IJobAction;
import com.norconex.jefmon.instance.action.impl.ViewJobLogAction;
import com.norconex.jefmon.instance.action.impl.ViewJobSuiteLogAction;

/**
 * @author Pascal Essiembre
 *
 */
@SuppressWarnings("nls")
public class JobActionsPanel extends AbstractSettingsPanel {

    private static final long serialVersionUID = 8241614025215145851L;

    private static final String ID_AVAILABLE_ACTIONS = "nx-available-actions";
    private static final String ID_SELECTED_ACTIONS = "nx-selected-actions";
    
    private final IJobAction[] defaultActions = new IJobAction[]{
            new ViewJobSuiteLogAction(),
            new ViewJobLogAction()
    };
    
    private final Map<String, IJobAction> allActions = new HashMap<>();
    
    private final List<IJobAction> availableActions = new ArrayList<>();
    private final List<IJobAction> selectedActions = new ArrayList<>();

    public JobActionsPanel(String id, JEFMonConfig dirtyConfig) {
        super(id, dirtyConfig);
        setOutputMarkupId(true);

        loadAllJobActions();

        // If we are configuring for the first time, configure default ones for
        // the user.
        if (JEFMonApplication.get().getConfig().getInstanceName() == null) {
            dirtyConfig.setJobActions(defaultActions);
        }

        availableActions.addAll(allActions.values());
        if (ArrayUtils.isNotEmpty(dirtyConfig.getJobActions())) {
            remove(availableActions, dirtyConfig.getJobActions());
            selectedActions.addAll(Arrays.asList(dirtyConfig.getJobActions()));
        }
        
        //--- Available actions ---
        ListView<IJobAction> avaiableActionsList = 
                new ListView<IJobAction>(
                        "avaiableActions", Model.ofList(availableActions)) {
            private static final long serialVersionUID = 823045777818571044L;
            @Override
            protected void populateItem(ListItem<IJobAction> item) {
                populateActionItem(item);
            }
        };
        add(avaiableActionsList);
        
        //--- Selected actions ---
        ListView<IJobAction> selectedActionsList = 
                new ListView<IJobAction>(
                        "selectedActions", Model.ofList(selectedActions)) {
            private static final long serialVersionUID = 386919939647830956L;
            @Override
            protected void populateItem(ListItem<IJobAction> item) {
                populateActionItem(item);
            }
        };
        add(selectedActionsList);
        
        add(new AbstractDefaultAjaxBehavior() {
            private static final long serialVersionUID = -2113526159749269348L;
            @Override
            protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = 
                        RequestCycle.get().getRequest().getRequestParameters();
                moveAction(
                        params.getParameterValue("class").toString(), 
                        params.getParameterValue("target").toString(), 
                        params.getParameterValue("newindex").toInt());
            }
            
            @Override
            public void renderHead(
                    Component component, IHeaderResponse response) {
                super.renderHead(component, response);
//                String componentMarkupId = component.getMarkupId();
                String callbackUrl = getCallbackUrl().toString();
                 
                Map<String, Object> map = new HashMap<>();
                map.put("callbackUrl", callbackUrl);
//                map.put( "args", "Your Arguments Here" );
//                map.put("componentMarkupId", componentMarkupId);
                
                PackageTextTemplate ptt = new PackageTextTemplate(
                        JobActionsPanel.class, "JobActionsPanel.js" );
                OnDomReadyHeaderItem onDomReadyHeaderItem = 
                        OnDomReadyHeaderItem.forScript(ptt.asString(map));
                response.render(onDomReadyHeaderItem);
                IOUtils.closeQuietly(ptt);
            }
            
        });
    }

    @Override
    protected void applyState() {
        getDirtyConfig().setJobActions(
                selectedActions.toArray(new IJobAction[]{}));
    }


    private void populateActionItem(ListItem<IJobAction> item) {
        IJobAction action = item.getModelObject();
        item.add(new AttributeModifier(
                "data-action", action.getClass().getName()));
        item.add(new Label("actionCss").add(
                new CssClass(action.getFontIcon())));
        item.add(new Label("actionName", action.getName()));
    }
        
        
    private void remove(List<IJobAction> target, IJobAction... removeme) {
        List<IJobAction> matches = new ArrayList<>();
        for (IJobAction removeAction : removeme) {
            for (IJobAction targetAction : target) {
                if (removeAction.getClass().equals(targetAction.getClass())) {
                    matches.add(targetAction);
                }
            }
        }
        target.removeAll(matches);
    }

    private void moveAction(
            String actionClass, String receipient, int index) {
        IJobAction action = allActions.get(actionClass);
        remove(availableActions, action);
        remove(selectedActions, action);

        if (ID_AVAILABLE_ACTIONS.equals(receipient)) {
            availableActions.add(index, action);
        } else if (ID_SELECTED_ACTIONS.equals(receipient)) {
            selectedActions.add(index, action);
        }
    }
    


    private void loadAllJobActions() {
        for (IJobAction action : getApp().getAllJobsActions()) {
            allActions.put(action.getClass().getName(), action);
        }
    }

}
