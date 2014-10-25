package com.norconex.jefmon.instance;

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.SetModel;
import org.apache.wicket.util.time.Duration;

import com.norconex.jefmon.JEFMonPanel;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;
import com.norconex.jefmon.instance.tree.JobTreeProvider;
import com.norconex.jefmon.instance.tree.JobsTableTree;

/**
 */
@SuppressWarnings("nls")
public class InstancePanel extends JEFMonPanel {

    private static final long serialVersionUID = 6007545512735243708L;

    public static final String TREE_DIALOG_WRAPPER_ID = "treeDialog";
    public static final String TREE_DIALOG_ID = "dialog";

    private final WebMarkupContainer treeDialogWrapper;
//    private AbstractDialog<?> treeDialog;

    public InstancePanel(String id) {
        super(id);
        
        treeDialogWrapper = new WebMarkupContainer(TREE_DIALOG_WRAPPER_ID);
        treeDialogWrapper.add(new Label(TREE_DIALOG_ID));
        treeDialogWrapper.setOutputMarkupId(true);
        add(treeDialogWrapper);
        
        add(new Label("name", getJEFMonConfig().getInstanceName()));
        
        final JobTreeProvider provider = 
                new JobTreeProvider(getJobSuitesStatusesMonitor());

        IModel<Set<JobStatusTreeNode>> state = new SetModel<JobStatusTreeNode>(
                new HashSet<JobStatusTreeNode>());
        final AbstractTree<JobStatusTreeNode> tree = createTree(provider, state);
        add(tree);

        add(new InstanceToolbar("toolbar", tree));

        add(new AbstractAjaxTimerBehavior(Duration.seconds(
                getJEFMonConfig().getDefaultRefreshInterval())) {
            private static final long serialVersionUID = -8417817690758833175L;
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                target.add(tree);
            }
        });


//        {
//            private static final long serialVersionUID = -7502441018116229735L;
//            @Override
//            protected void onBeforeRender() {
//                if (get(TREE_DIALOG_ID) != null) {
//                    remove(TREE_DIALOG_ID);
//                }
//                if (treeDialog == null) {
//                    add(new Label(TREE_DIALOG_ID));
//                } else {
//                    add(treeDialog);
//                }
//                super.onBeforeRender();
//            }
//        };
    }


    private AbstractTree<JobStatusTreeNode> createTree(JobTreeProvider provider,
            IModel<Set<JobStatusTreeNode>> state) {
//        List<IColumn<JobStatusTreeNode, Void>> columns = createColumns(provider);
        final JobsTableTree tree = new JobsTableTree(
                "treeTable", provider, Integer.MAX_VALUE, state,
                treeDialogWrapper);
//        {
//            private static final long serialVersionUID = -7115420393079158433L;
//
//            @Override
//            protected void onInitialize() {
//                super.onInitialize();
//                getTable().add(new CssClass("table table-condensed small"));
//            }
//            
//            @Override
//            protected Component newContentComponent(String id,
//                    IModel<JobStatusTreeNode> model) {
//                return InstancePanel.this.newContentComponent(id, this, model);
//            }
//
////            @Override
////            protected Item<JobStatusTreeNode> newRowItem(String id, int index,
////                    IModel<JobStatusTreeNode> model) {
////                Item<JobStatusTreeNode> item = new OddEvenItem<JobStatusTreeNode>(id, index, model);
////                item.add(new Title(createTooltipContent(model.getObject())));
////                return item;
////            }
//        };
//        String selector = JQueryWidget.getSelector(tree);
//        tree.add(new TooltipBehavior(selector + " tr",
//                JQueryBehaviorUtil.createContentOption())
//                .setOption("track", true));
////                .setOption("show", "{effect: \"slideDown\", delay: 500 }"));
        return tree;
    }

//    private Component newContentComponent(String id,
//            final AbstractTree<JobStatusTreeNode> tree, IModel<JobStatusTreeNode> model) {
//        return new Folder<JobStatusTreeNode>(id, tree, model) {
//            private static final long serialVersionUID = 77423020059851645L;
//            @Override
//            protected Component newLabelComponent(
//                    String labelId, IModel<JobStatusTreeNode> labelModel) {
//
//                Label label = new Label(labelId, "&nbsp;"
//                      +  "<span class=\"jef-tree-junction-label\">"
////                      + StringEscapeUtils.escapeXml(labelModel.getObject().getId())
//                      + "</span>");
//                label.setEscapeModelStrings(false);
//                return label;
////                return new Label(id, model.getObject().getId());
//            }
////            protected String getStyleClass() {
////                JobStatusTreeNode job = getModelObject();
////                IJobStatus progress = job.getStatus();
////                if (progress == null || progress.getStatus() == null) {
////                    return "jef-tree-job-blank";
////                }
////                switch (progress.getStatus()) {
////                case RUNNING:
////                    return "jef-tree-job-running";
////                case COMPLETED:
////                    return "jef-tree-job-ok";
////                case PREMATURE_TERMINATION:
////                    return "jef-tree-job-premature";
////                case ABORTED:
////                    return "jef-tree-job-aborted";
////                case STOPPED:
////                case STOPPING:
////                    return "jef-tree-job-stop";
////                default:
////                    return "jef-tree-job-blank";
////                }
////            }
//        };
//    }
//
//    private List<IColumn<JobStatusTreeNode, Void>> createColumns(
//            JobTreeProvider provider) {
//        List<IColumn<JobStatusTreeNode, Void>> columns =
//                new ArrayList<IColumn<JobStatusTreeNode, Void>>();
//        columns.add(new TreeColumn<JobStatusTreeNode, Void>(
//                new ResourceModel("col.job")));
//        columns.add(new ProgressColumn(new ResourceModel("col.progress")));
//        columns.add(new StatusColumn(new ResourceModel("col.status")));
//        columns.add(new DateStartedColumn(new ResourceModel("col.started")));
//        columns.add(new DurationColumn(new ResourceModel("col.duration")));
//        columns.add(new RemainingColumn(new ResourceModel("col.remaining")));
//        columns.add(new PropertyColumn<JobStatusTreeNode, Void>(
//                new ResourceModel("col.note"), "status.note"));
////        columns.add(new ActionsColumn(
////                new ResourceModel("col.actions"), provider, TREE_DIALOG_ID) {
////            private static final long serialVersionUID = -7537848328949979087L;
////
////            @Override
////            protected void showActionDialog(final AbstractDialog<?> dialog,
////                    AjaxRequestTarget target) {
////                if (treeDialog != null) {
////                    treeDialog.detach();
////                }
////
////                dialog.setOutputMarkupId(true);
////
////                treeDialog = dialog;
////                target.add(treeDialogWrapper);
////            }
////        });
//        return columns;
//    }


//    private String createTooltipContent(JobStatusTreeNode job) {
////        IJobContext context = job.getContext();
////        IJobStatus status = job.getStatus();
//        StringBuilder b = new StringBuilder();
////        b.append("<div class=\"jef-tree-tooltip\"><h3>");
////        b.append(job.getId());
////        b.append("</h3>");
////        b.append("<table>");
////        appendTipRow(b, getString("tooltip.desc"), context.getDescription());
//
////        String type = null;
////        if (job.isAsyncGroup()) {
////            type = getString("tooltip.type.async");
////        } else if (job.isSyncGroup()) {
////            type = getString("tooltip.type.sync");
////        } else {
////            type = getString("tooltip.type.job");
////        }
////        appendTipRow(b, getString("tooltip.type"), type);
//
////        if (status != null) {
////            String startTime = null;
////            if (status.getStartTime() != null) {
////                startTime = DateTimeFormat.fullDateTime().withLocale(
////                        getLocale()).print(status.getStartTime().getTime());
////            } else {
////                startTime = getString("tooltip.notstarted");
////            }
////            appendTipRow(b, getString("tooltip.started"), startTime);
////            String endTime = null;
////            if (status.getEndTime() != null) {
////                endTime = DateTimeFormat.fullDateTime().withLocale(
////                        getLocale()).print(status.getEndTime().getTime());
////            } else if (status.getStatus() == JobState.RUNNING) {
////                endTime = getString("tooltip.running");
////            } else {
////                startTime = getString("tooltip.notended");
////            }
////            appendTipRow(b, getString("tooltip.ended"), endTime);
////            appendTipRow(b, getString("tooltip.elapsed"),
////                    DurationUtil.formatLong(
////                            getLocale(), status.getElapsedTime()));
////        } else {
//            appendTipRow(b, getString("tooltip.started"),
//                    getString("tooltip.notstarted"));
////        }
//        return b.toString();
//
//    }
//    private void appendTipRow(StringBuilder b, String key, String value) {
//        b.append("<tr><td class=\"label\">").append(key)
//                .append("</td><td class=\"value\">")
//                .append(value).append("</td></tr>");
//    }
}
