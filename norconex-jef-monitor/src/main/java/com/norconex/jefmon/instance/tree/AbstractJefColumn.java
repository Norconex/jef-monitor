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

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public abstract class AbstractJefColumn extends AbstractColumn<JobStatusTreeNode, Void> {
    private static final long serialVersionUID = -4245784740172933614L;

    public AbstractJefColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public final void populateItem(Item<ICellPopulator<JobStatusTreeNode>> cellItem,
            String componentId, IModel<JobStatusTreeNode> rowModel) {
        populateItem(cellItem, componentId, rowModel.getObject());
    }
    public abstract void populateItem(Item<ICellPopulator<JobStatusTreeNode>> cellItem,
            String componentId, JobStatusTreeNode jobStatusTreeNode);
}
