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

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.norconex.commons.lang.time.DurationUtil;
import com.norconex.jef4.status.JobDuration;
import com.norconex.jefmon.JEFMonSession;

public class DurationColumn extends AbstractJefColumn {

    private static final long serialVersionUID = -4129824825592867125L;

    public DurationColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<JobStatusTreeNode>> item,
            String componentId, JobStatusTreeNode jobStatus) {

        JobDuration duration = jobStatus.getDuration();
        Date startDate = duration.getStartTime();
        Date endDate = duration.getEndTime();
        if (startDate == null) {
            item.add(new Label(componentId));
            return;
        }
        
        long started = startDate.getTime();
        long stopTime = System.currentTimeMillis();
        if (endDate != null) {
            stopTime = endDate.getTime(); 
        } else {
            stopTime = jobStatus.getLastActivity().getTime();
        }
        Locale locale = JEFMonSession.getSession().getLocale();
        item.add(new Label(componentId, 
                DurationUtil.formatLong(locale, stopTime - started, 2)));
    }
    @Override
    public String getCssClass() {
        return "jef-tree-status";
    }
}
