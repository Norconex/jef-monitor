package com.norconex.jefmon.instance.tree;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import com.norconex.jef4.status.JobState;

public class StatusColumn extends AbstractJefColumn {

    private static final long serialVersionUID = -4129824825592867125L;

    public StatusColumn(IModel<String> displayModel) {
        super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<JobStatusTreeNode>> item,
            String componentId, JobStatusTreeNode job) {
        
        JobState state = job.getState();
        if (state == null) {
            item.add(new Label(componentId));
            return;
        }
        String label = "status." + state;
        item.add(new Label(componentId, new ResourceModel(label)));
    }
    @Override
    public String getCssClass() {
        return "jef-tree-status";
    }
}
