/* Copyright 2014-2015 Norconex Inc.
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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree.State;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.norconex.jef4.status.JobState;

public class JEFFolder extends Panel {
    private static final LinkStyleBehavior LINK_STYLE_CLASS = 
            new LinkStyleBehavior();
    private static final IconStyleBehavior ICON_STYLE_CLASS = 
            new IconStyleBehavior();

    private static final long serialVersionUID = 1L;

    private AbstractTree<JobStatusTreeNode> tree;

    public JEFFolder(String id, AbstractTree<JobStatusTreeNode> tree,
            IModel<JobStatusTreeNode> model) {
        super(id, model);

        MarkupContainer link = newLinkComponent("link", model);
        link.add(LINK_STYLE_CLASS);
        add(link);

        link.add(newLabelComponent("label", model));

        Label icon = new Label("icon");
        icon.add(ICON_STYLE_CLASS);
        link.add(icon);

        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    public IModel<JobStatusTreeNode> getModel() {
        return (IModel<JobStatusTreeNode>) getDefaultModel();
    }

    public JobStatusTreeNode getModelObject() {
        return getModel().getObject();
    }

    protected MarkupContainer newLinkComponent(String id,
            IModel<JobStatusTreeNode> model) {
        return new AjaxFallbackLink<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return JEFFolder.this.isClickable();
            }

            @Override
            public void onClick(AjaxRequestTarget target) {
                JEFFolder.this.onClick(target);
            }
        };
    }

    protected Component newLabelComponent(String id,
            IModel<JobStatusTreeNode> model) {
        return new Label(id, newLabelModel(model));
    }

    protected IModel<String> newLabelModel(IModel<JobStatusTreeNode> model) {
        return new Model<String>(model.getObject().getJobId());
    }

    protected String getLinkStyleClass() {
        return "";
    }

    protected String getIconStyleClass() {
        JobStatusTreeNode job = getModelObject();
        JobState state = job.getState();
        if (state == null) {
            return "jef-tree-job-blank";
        }
        switch (state) {
        case RUNNING:
            return "fa fa-spinner fa-spin jef-tree-job-running";
        case COMPLETED:
            return "fa fa-check-circle jef-tree-job-ok";
        case PREMATURE_TERMINATION:
            return "fa fa-exclamation-circle jef-tree-job-premature";
        case ABORTED:
            return "fa fa-exclamation-circle jef-tree-job-aborted";
        case STOPPED:
        case STOPPING:
            return "fa fa-stop jef-tree-job-stop";
        default:
            return "jef-tree-job-blank";
        }
    }

    protected boolean isSelected() {
        return false;
    }

    protected String getOtherStyleClass(JobStatusTreeNode t) {
        return "tree-folder-other";
    }

    protected String getClosedStyleClass() {
        return "tree-folder-closed";
    }

    protected String getOpenStyleClass() {
        return "tree-folder-open";
    }

    protected String getSelectedStyleClass() {
        return "selected";
    }

    protected boolean isClickable() {
        JobStatusTreeNode t = getModelObject();

        return tree.getProvider().hasChildren(t);
    }

    protected void onClick(AjaxRequestTarget target) {
        JobStatusTreeNode t = getModelObject();
        if (tree.getState(t) == State.EXPANDED) {
            tree.collapse(t);
        } else {
            tree.expand(t);
        }
    }

    private static class LinkStyleBehavior extends Behavior {
        private static final long serialVersionUID = 1L;

        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            JEFFolder parent = (JEFFolder) component.getParent();

            String styleClass = parent.getLinkStyleClass();
            if (styleClass != null) {
                tag.put("class", styleClass);
            }
        }
    }

    private static class IconStyleBehavior extends Behavior {
        private static final long serialVersionUID = 1L;

        @Override
        public void onComponentTag(Component component, ComponentTag tag) {
            JEFFolder parent = (JEFFolder) component.getParent().getParent();
            String styleClass = parent.getIconStyleClass();
            if (styleClass != null) {
                tag.put("class", styleClass);
            }
        }
    }
}