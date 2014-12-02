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

import java.util.Locale;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.norconex.commons.lang.time.DurationUtil;
import com.norconex.jef4.status.JobState;
import com.norconex.jefmon.JEFMonSession;

public class RemainingColumn extends AbstractJefColumn {

    private static final long serialVersionUID = -4129824825592867125L;

    public RemainingColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<JobStatusTreeNode>> item,
            String componentId, JobStatusTreeNode jobStatus) {

        JobState state = jobStatus.getState();
        
        if (state == null
                || state != JobState.RUNNING
                || jobStatus.getDuration().getStartTime() == null
                || jobStatus.getProgress() == 0d) {
            item.add(new Label(componentId));
            return;
        }
        double completion = jobStatus.getProgress();
        double elapsedTime =
                System.currentTimeMillis() 
                        - jobStatus.getDuration().getStartTime().getTime();
        long remaining = (long) (elapsedTime / completion - elapsedTime);
        Locale locale = JEFMonSession.getSession().getLocale();
        item.add(new Label(componentId,
                DurationUtil.formatLong(locale, remaining, 2)));
    }
    @Override
    public String getCssClass() {
        return "jef-tree-status";
    }
}
