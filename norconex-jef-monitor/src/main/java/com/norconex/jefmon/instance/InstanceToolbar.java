package com.norconex.jefmon.instance;

import java.util.Iterator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.markup.html.panel.Panel;

import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonPanel;
import com.norconex.jefmon.instance.tree.JobStatusTreeNode;
import com.norconex.jefmon.settings.panels.IdentityPanel;
import com.norconex.jefmon.settings.panels.JobActionsPanel;
import com.norconex.jefmon.settings.panels.JobLocationsPanel;
import com.norconex.jefmon.settings.update.SettingsPage;

@SuppressWarnings("nls")
public class InstanceToolbar extends JEFMonPanel {

    private static final long serialVersionUID = 8688539156220717257L;

    public InstanceToolbar(String id, final AbstractTree<JobStatusTreeNode> tree) {
        super(id);

        add(new AjaxLink<String>("expandAll") {
            private static final long serialVersionUID = 2456949869983267747L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<? extends JobStatusTreeNode> it = 
                        tree.getProvider().getRoots();
                while (it.hasNext()) {
                    expand(it.next());
                }
                target.add(tree);
            }
            private void expand(JobStatusTreeNode node) {
                tree.expand(node);
                for (JobStatusTreeNode childNode : node.getChildren()) {
                    expand(childNode);
                }
            }
        });
        
        add(new AjaxLink<String>("collapseAll") {
            private static final long serialVersionUID = 4973372951011393621L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                Iterator<? extends JobStatusTreeNode> it = 
                        tree.getProvider().getRoots();
                while (it.hasNext()) {
                    collapse(it.next());
                }
                target.add(tree);
            }
            private void collapse(JobStatusTreeNode node) {
                tree.collapse(node);
                for (JobStatusTreeNode childNode : node.getChildren()) {
                    collapse(childNode);
                }
            }
        });

        add(new AjaxLink<String>("name") {
            private static final long serialVersionUID = 6166025920271559843L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new SettingsPage() {
                    private static final long serialVersionUID = 
                            -8965379629384587120L;
                    @Override
                    protected Panel createConfigPanel(
                            String markupId, JEFMonConfig dirtyConfig) {
                        return new IdentityPanel(markupId, dirtyConfig);
                    }
                });
            }
        });
        add(new AjaxLink<String>("jobs") {
            private static final long serialVersionUID = -3653237035032195947L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new SettingsPage() {
                    private static final long serialVersionUID = 
                            -4324388727949696916L;
                    @Override
                    protected Panel createConfigPanel(
                            String markupId, JEFMonConfig dirtyConfig) {
                        return new JobLocationsPanel(markupId, dirtyConfig);
                    }
                });
            }
        });
        add(new AjaxLink<String>("actions") {
            private static final long serialVersionUID = 2197324271681886459L;
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new SettingsPage() {
                    private static final long serialVersionUID = 
                            -8773340012426346828L;
                    @Override
                    protected Panel createConfigPanel(
                            String markupId, JEFMonConfig dirtyConfig) {
                        return new JobActionsPanel(markupId, dirtyConfig);
                    }
                });
            }
        });
    }
}
