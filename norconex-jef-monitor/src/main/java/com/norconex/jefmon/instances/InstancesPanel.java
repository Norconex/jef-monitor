package com.norconex.jefmon.instances;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.time.Duration;

import com.norconex.commons.wicket.behaviors.CssClass;
import com.norconex.commons.wicket.behaviors.CssStyle;
import com.norconex.commons.wicket.behaviors.OnClickBehavior;
import com.norconex.commons.wicket.bootstrap.modal.BootstrapModalLauncher;
import com.norconex.commons.wicket.bootstrap.tooltip.BootstrapTooltip;
import com.norconex.commons.wicket.markup.html.image.CacheableImage;
import com.norconex.jef4.status.JobState;
import com.norconex.jefmon.JEFMonPanel;
import com.norconex.jefmon.markup.html.image.JEFImages;

public abstract class InstancesPanel extends JEFMonPanel {

    private static final long serialVersionUID = 5164879205325323841L;

    private static final JobState[] STATUSES = { JobState.COMPLETED,
            JobState.RUNNING, JobState.ABORTED, JobState.PREMATURE_TERMINATION,
            JobState.STOPPED, JobState.STOPPING, JobState.UNKNOWN, };

    private WebMarkupContainer instancesTable;

    public InstancesPanel(String id) {
        super(id);
    }

    public InstancesPanel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);

        instancesTable = new WebMarkupContainer("instancesTable") {
            private static final long serialVersionUID = -4821257755526779662L;
            @Override
            protected void onBeforeRender() {
                addOrReplace(createDataView());
                super.onBeforeRender();
            }
        };
        instancesTable.setOutputMarkupId(true);
        add(instancesTable);

        add(new AbstractAjaxTimerBehavior(Duration.seconds(
                getJEFMonConfig().getDefaultRefreshInterval())) {
            private static final long serialVersionUID = -4652378407644216362L;
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                target.add(instancesTable);
            }
        });
        
        AddInstanceDialog dialog = new AddInstanceDialog("addInstanceDialog") {
            private static final long serialVersionUID = 1405966469655005795L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, String url) {
                InstancesManager.addInstance(url);
                target.add(instancesTable);
            }
        };
        add(dialog);
        
        WebMarkupContainer addButton = new WebMarkupContainer("addInstance");
        addButton.add(new BootstrapModalLauncher(dialog));
        add(addButton);
    }

    private DataView<JEFMonInstance> createDataView() {
        ListDataProvider<JEFMonInstance> dataProvider =
                new ListDataProvider<JEFMonInstance>(
                        InstancesManager.loadInstances());
        DataView<JEFMonInstance> dataView = 
                new DataView<JEFMonInstance>("instances", dataProvider) {
            private static final long serialVersionUID =
                    4462642058300231730L;
            @Override
            public void populateItem(final Item<JEFMonInstance> item) {
                final JEFMonInstance instance = item.getModelObject();
                Label nameLabel = new Label("name", instance.getName());
                if (instance.isInvalid()) {
                    nameLabel.add(new CssStyle("font-weight: bold;"));
                }
                item.add(nameLabel);
                String url = instance.getUrl();
                if (url == null) {
                    url = "<" + getString("instance.this") + ">";
                }
                item.add(new Label("url", url));
                item.add(createJobStateListView(instance));

                String totalLabel;
                if (instance.isInvalid()) {
                    totalLabel = StringUtils.EMPTY;
                } else {
                    totalLabel = Integer.toString(instance.getTotalRoots());
                }
                item.add(new Label(
                        "status-total", totalLabel));


                AjaxLink<String> removeButton = new AjaxLink<String>("remove") {
                    private static final long serialVersionUID = 
                            -7913473337936429743L;
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        InstancesManager.removeInstance(instance.getUrl());
                        target.add(instancesTable);
                    }
                };
                removeButton.add(new BootstrapTooltip(
                        new ResourceModel("btn.remove")));
                if (instance.isThisInstance()) {
                    removeButton.setVisible(false);
                }
                item.add(removeButton);
            }
            @Override
            protected Item<JEFMonInstance> newItem(String id, int index,
                    final IModel<JEFMonInstance> model) {
                Item<JEFMonInstance> row = super.newItem(id, index, model);
                JEFMonInstance instance = model.getObject();
                if (instance.isInvalid()) {
                    row.add(new CssClass("danger"));
                    row.add(new CssStyle("cursor: default;"));
                    row.add(new BootstrapTooltip(getString(
                            "instance.error") + instance.getRemoteError()));
                } else {
                    row.add(new CssStyle("cursor: pointer;"));
                    row.add(new OnClickBehavior() {
                        private static final long serialVersionUID =
                                7484946666996050240L;
                        @Override
                        protected void onClick(AjaxRequestTarget target) {
                            onInstanceClick(target, model.getObject());
                        }
                    });
                }
                return row;
            }
        };
        return dataView;
    }

    protected abstract void onInstanceClick(
            AjaxRequestTarget target, JEFMonInstance instance);

    private ListView<JobState> createJobStateListView(
            final JEFMonInstance instance) {
        return new ListView<JobState>("statuses", Arrays.asList(STATUSES)) {
            private static final long serialVersionUID = -716585245859081922L;
            @Override
            protected void populateItem(ListItem<JobState> item) {
                JobState status = item.getModelObject();
                MutableInt count = instance.getStatuses().get(status);
                if (count == null) {
                    count = new MutableInt(0);
                }
                ResourceReference imgRef;
                if (instance.isInvalid()) {
                    imgRef = JEFImages.REF_JOB_ERROR;
                } else if (status == null || count.getValue() == 0) {
                    imgRef = JEFImages.REF_JOB_BLANK;
                } else {
                    switch (status) {
                    case COMPLETED:
                        imgRef = JEFImages.REF_JOB_OK;
                        break;
                    case RUNNING:
                        imgRef = JEFImages.REF_JOB_RUNNING;
                        break;
                    default:
                        imgRef = JEFImages.REF_JOB_ERROR;
                        break;
                    }
                }
                Image image = new CacheableImage("statusIcon", imgRef);
                String countLabel = count.toString();
                if (instance.isInvalid()) {
                    image.setVisible(false);
                    countLabel = StringUtils.EMPTY;
                }
                item.add(new Label("statusCount", countLabel));
                item.add(image);
            }
        };
    }
}
