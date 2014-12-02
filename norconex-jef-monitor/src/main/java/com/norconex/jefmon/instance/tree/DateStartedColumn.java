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

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.joda.time.format.DateTimeFormat;

import com.norconex.jef4.status.JobDuration;
import com.norconex.jefmon.JEFMonSession;

public class DateStartedColumn extends AbstractJefColumn {

    private static final long serialVersionUID = -2624662215293684093L;

    public DateStartedColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<JobStatusTreeNode>> item,
            String componentId, JobStatusTreeNode jobStatus) {
        
        JobDuration duration = jobStatus.getDuration();
        Date startDate = duration.getStartTime();
        item.add(new Label(componentId, getTimeString(startDate)));
    }
    @Override
    public String getCssClass() {
        return "jef-tree-status";
    }
    
    private String getTimeString(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        Locale locale = JEFMonSession.getSession().getLocale();
        String pattern = "yyyy-MMM-dd - H:mm:ss";
        return DateTimeFormat.forPattern(
                pattern).withLocale(locale).print(date.getTime()); 
    }
}
