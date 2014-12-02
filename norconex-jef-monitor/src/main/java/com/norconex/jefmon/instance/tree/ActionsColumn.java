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

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import com.norconex.jefmon.JEFMonApplication;
import com.norconex.jefmon.instance.action.ActionsCell;
import com.norconex.jefmon.instance.action.IJobAction;

public class ActionsColumn extends AbstractJefColumn {

    private static final long serialVersionUID = 5647374372976301140L;
    
    private final WebMarkupContainer dialogWrapper;
    private final String dialogId;
    
    
    public ActionsColumn(IModel<String> displayModel, 
            WebMarkupContainer dialogWrapper, String dialogId) {
        super(displayModel);
        this.dialogId = dialogId;
        this.dialogWrapper = dialogWrapper;
    }

    @Override
    public void populateItem(
            Item<ICellPopulator<JobStatusTreeNode>> cellItem,
            String componentId,  JobStatusTreeNode jobStatusTreeNode) {
        
        List<IJobAction> actions = Arrays.asList(
                JEFMonApplication.get().getConfig().getJobActions());

        cellItem.add(new ActionsCell(componentId, 
                dialogWrapper, dialogId, jobStatusTreeNode, actions));
    }

}