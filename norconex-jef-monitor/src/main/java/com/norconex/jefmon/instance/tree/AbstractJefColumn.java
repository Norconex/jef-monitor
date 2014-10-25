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
