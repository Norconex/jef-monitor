/* Copyright 2007-2015 Norconex Inc.
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
package com.norconex.jefmon.instance;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.SetModel;
import org.apache.wicket.util.time.Duration;

import com.norconex.jefmon.JEFMonPanel;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;
import com.norconex.jefmon.instance.tree.JobTreeProvider;
import com.norconex.jefmon.instance.tree.JobsTableTree;

@SuppressWarnings("nls")
public class InstancePanel extends JEFMonPanel {

    private static final long serialVersionUID = 6007545512735243708L;

    public static final String TREE_DIALOG_WRAPPER_ID = "treeDialog";
    public static final String TREE_DIALOG_ID = "dialog";

    public InstancePanel(String id) {
        super(id);
        
        WebMarkupContainer treeDialogWrapper = 
                new WebMarkupContainer(TREE_DIALOG_WRAPPER_ID);
        treeDialogWrapper.add(new Label(TREE_DIALOG_ID));
        treeDialogWrapper.setOutputMarkupId(true);
        add(treeDialogWrapper);
        
        add(new Label("name", getJEFMonConfig().getInstanceName()));
        
        final JobTreeProvider provider = 
                new JobTreeProvider(getJobSuitesStatusesMonitor());

        IModel<Set<JobStatusTreeNode>> state = new SetModel<JobStatusTreeNode>(
                new HashSet<JobStatusTreeNode>());
        final AbstractTree<JobStatusTreeNode> tree = createTree(provider, state);
        add(tree);

        add(new InstanceToolbar("toolbar", tree));

        add(new AbstractAjaxTimerBehavior(Duration.seconds(
                getJEFMonConfig().getDefaultRefreshInterval())) {
            private static final long serialVersionUID = -8417817690758833175L;
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                target.add(tree);
            }
        });
    }


    private AbstractTree<JobStatusTreeNode> createTree(JobTreeProvider provider,
            IModel<Set<JobStatusTreeNode>> state) {
        final JobsTableTree tree = new JobsTableTree(
                "treeTable", provider, Integer.MAX_VALUE, state);
        return tree;
    }
}
