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

import java.math.BigDecimal;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.norconex.commons.wicket.bootstrap.progress.BootstrapProgressBar;
import com.norconex.jef4.status.JobState;

public class ProgressColumn extends AbstractJefColumn {

    private static final long serialVersionUID = -4129824825592867125L;

    public ProgressColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<JobStatusTreeNode>> item,
            String markupId, JobStatusTreeNode jobStatus) {
        
        JobState state = jobStatus.getState();
        
        if (state == null) {
            item.add(new Label(markupId));
            return;
        }
        final int percent = BigDecimal.valueOf(
                jobStatus.getProgress()).movePointRight(2).intValue();

        BootstrapProgressBar bar = new BootstrapProgressBar(markupId);
        bar.setPercent(jobStatus.getProgress());
        if (state == JobState.ABORTED 
                || state == JobState.PREMATURE_TERMINATION) {
            bar.setBarCssClass("progress-bar-danger");
        } else if (percent > 100) {
            bar.setBarCssClass("progress-bar-warning");
        } else if (state == JobState.COMPLETED) {
            bar.setBarCssClass("progress-bar-success");
        } else if (state == JobState.STOPPING) {
            bar.setBarCssClass("progress-bar-info");
        } else if (state == JobState.STOPPED) {
            bar.setBarCssClass("progress-bar-info");
        } else if (state == JobState.RUNNING) {
            bar.setBarCssClass("progress-bar-striped active");
        }
        item.add(bar);
    }
    @Override
    public String getCssClass() {
        return "jef-tree-progress";
    }
}
