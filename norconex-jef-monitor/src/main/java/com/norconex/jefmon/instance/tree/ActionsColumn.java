/**
 * 
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
    
//    private final JobTreeProvider provider;
    
    private final WebMarkupContainer dialogWrapper;
    private final String dialogId;
    
    
    public ActionsColumn(
            IModel<String> displayModel, WebMarkupContainer dialogWrapper,// JobTreeProvider provider,
            String dialogId) {
        super(displayModel);
//        this.provider = provider;
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
                
        
//        cellItem.add(new ActionsCell( 
//                componentId, dialogId, jobStatusTreeNode, actions) {
//            @Override
//            protected void showActionDialog(AbstractDialog<?> dialog,
//                    AjaxRequestTarget target) {
//                ActionsColumn.this.showActionDialog(dialog, target);
//            }
//        });
    }

//    protected abstract void showActionDialog(
//            AbstractDialog<?> dialog, AjaxRequestTarget target);
    
//    /**
//     * @see com.norconex.jef.webmon.wicket.tree.AbstractJEFColumn
//     *      #newCell(java.lang.String,
//     *               com.norconex.jef.webmon.model.JobSuiteTreeNode)
//     */
//    public Component newCell(String id, final JobStatusTreeNode treenode) {
//        ActionsPanel actionsPanel = new ActionsPanel(id, treenode/*, modal*/);
//        return actionsPanel;
//    }
}