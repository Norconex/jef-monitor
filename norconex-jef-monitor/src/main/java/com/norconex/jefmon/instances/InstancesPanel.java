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
package com.norconex.jefmon.instances;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import com.norconex.commons.wicket.behaviors.CssClass;
import com.norconex.commons.wicket.behaviors.CssStyle;
import com.norconex.commons.wicket.behaviors.OnClickBehavior;
import com.norconex.commons.wicket.bootstrap.modal.BootstrapModalLauncher;
import com.norconex.commons.wicket.bootstrap.tooltip.BootstrapTooltip;
import com.norconex.jef4.status.JobState;
import com.norconex.jefmon.JEFMonPanel;

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

    private DataView<InstanceSummary> createDataView() {
        ListDataProvider<InstanceSummary> dataProvider =
                new ListDataProvider<InstanceSummary>(
                        InstancesManager.loadInstances());
        DataView<InstanceSummary> dataView = 
                new DataView<InstanceSummary>("instances", dataProvider) {
            private static final long serialVersionUID =
                    4462642058300231730L;
            @Override
            public void populateItem(final Item<InstanceSummary> item) {
                final InstanceSummary instance = item.getModelObject();
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
            protected Item<InstanceSummary> newItem(String id, int index,
                    final IModel<InstanceSummary> model) {
                Item<InstanceSummary> row = super.newItem(id, index, model);
                InstanceSummary instance = model.getObject();
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
            AjaxRequestTarget target, InstanceSummary instance);

    private ListView<JobState> createJobStateListView(
            final InstanceSummary instance) {
        return new ListView<JobState>("statuses", Arrays.asList(STATUSES)) {
            private static final long serialVersionUID = -716585245859081922L;
            @Override
            protected void populateItem(ListItem<JobState> item) {
                JobState status = item.getModelObject();
                MutableInt count = instance.getStatuses().get(status);
                if (count == null) {
                    count = new MutableInt(0);
                }
                String css = "";
                if (instance.isInvalid()) {
                    css = "fa fa-exclamation-circle nx-jef-status-error";
                } else if (status == null || count.getValue() == 0) {
                    css = "jef-tree-job-blank";
                } else {
                    switch (status) {
                    case COMPLETED:
                        css = "fa fa-check-circle nx-jef-status-ok";
                        break;
                    case RUNNING:
                        css = "fa fa-spinner fa-spin nx-jef-status-running";
                        break;
                    default:
                        css = "fa fa-exclamation-circle nx-jef-status-error";
                        break;
                    }
                }

                Label icon = new Label("statusIcon");
                icon.add(new CssClass(css));
                String countLabel = count.toString();
                if (instance.isInvalid()) {
                    icon.setVisible(false);
                    countLabel = StringUtils.EMPTY;
                }
                item.add(new Label("statusCount", countLabel));
                item.add(icon);
            }
        };
    }
}
