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
package com.norconex.jefmon.instance.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.tree.ISortableTreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.norconex.jef4.status.JobSuiteStatusSnapshot;
import com.norconex.jefmon.instance.JEFMonInstance;

public class JobTreeProvider 
        implements ISortableTreeProvider<JobStatusTreeNode, Void> {

    private static final long serialVersionUID = -7140490848913529419L;

    private final JEFMonInstance instance;

    public JobTreeProvider(final JEFMonInstance instance) {
        super();
        this.instance = instance;
    }

    @Override
    public void detach() {
        //Do nothing
    }

    @Override
    public Iterator<? extends JobStatusTreeNode> getChildren(
            JobStatusTreeNode jobStatusNode) {
        return jobStatusNode.getChildren().iterator();
    }

    @Override
    public Iterator<? extends JobStatusTreeNode> getRoots() {
        List<JobStatusTreeNode> roots = new ArrayList<JobStatusTreeNode>();
        Collection<JobSuiteStatusSnapshot> suitesStatuses =
                instance.getJobSuitesStatuses();
        for (JobSuiteStatusSnapshot suiteStatuses : suitesStatuses) {
            roots.add(new JobStatusTreeNode(
                    instance, suiteStatuses.getRoot().getJobId(), 
                    suiteStatuses.getRoot().getJobId(), true));
        }
        return roots.iterator();
    }

    @Override
    public boolean hasChildren(JobStatusTreeNode jobStatusNode) {
        return jobStatusNode.hasChildren();
    }

    @Override
    public IModel<JobStatusTreeNode> model(JobStatusTreeNode jobStatusNode) {
        return new Model<JobStatusTreeNode>(jobStatusNode);
    }

    @Override
    public ISortState<Void> getSortState() {
        return null;
    }
}
