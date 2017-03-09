/* Copyright 2007-2017 Norconex Inc.
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
package com.norconex.jefmon.settings.panels;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import com.norconex.commons.wicket.behaviors.OnClickBehavior;
import com.norconex.commons.wicket.markup.html.form.UpdatingTextField;
import com.norconex.jefmon.JEFMonConfig;

/**
 * Panel for giving a name to a JEF installation.
 * 
 * @author Pascal Essiembre
 */
public class JobLocationsPanel extends AbstractSettingsPanel {

    private static final long serialVersionUID = -6095982251770897729L;

    final WebMarkupContainer formWrapper = 
            new WebMarkupContainer("formWrapper");
    private final List<File> locations = new ArrayList<>();
    private final ListMultipleChoice<File> locationsSelect;
    private final WebMarkupContainer removeButton;
    
    public JobLocationsPanel(String id, JEFMonConfig dirtyConfig) {
        super(id, dirtyConfig);
        setOutputMarkupId(true);

        if (dirtyConfig.getMonitoredPaths() != null) {
            locations.addAll(Arrays.asList(dirtyConfig.getMonitoredPaths()));
        }
        formWrapper.setOutputMarkupId(true);
        add(formWrapper);
        
        // --- Locations Select ---
        locationsSelect = buildLocationsSelect("locations");
        formWrapper.add(locationsSelect);

        // --- Add ---
        final UpdatingTextField<String> addPathField = 
                new UpdatingTextField<>("addPath", new Model<String>());
        formWrapper.add(addPathField);
        WebMarkupContainer addButton = new WebMarkupContainer("addPathButton");
        addButton.add(new OnClickBehavior() {
            private static final long serialVersionUID = 3606300997668625245L;
            @Override
            protected void onClick(AjaxRequestTarget target) {
                String path = addPathField.getModelObject();
                if (StringUtils.isNotBlank(path)) {
                    addFileToSelect(target, new File(path));
                }
            }
        });
        formWrapper.add(addButton);
        
        // --- Remove ---
        removeButton = new WebMarkupContainer("removeButton");
        removeButton.setOutputMarkupId(true);
        removeButton.add(new OnClickBehavior() {
            private static final long serialVersionUID = 2072304873075922291L;
            @Override
            protected void onClick(AjaxRequestTarget target) {
                Collection<File> files = locationsSelect.getModelObject();
                locations.removeAll(files);
                locationsSelect.setChoices(locations);
                removeButton.setVisible(false);
                target.add(formWrapper);
            }
        });
        removeButton.setVisible(false);
        formWrapper.add(removeButton);
    }

    @Override
    protected void applyState() {
        JEFMonConfig dirtyConfig = getDirtyConfig();
        dirtyConfig.setMonitoredPaths(locations.toArray(new File[]{}));
    }

    private void addFileToSelect(AjaxRequestTarget target, File... files) {
        List<File> fileList = Arrays.asList(files);
        locations.removeAll(fileList);
        locations.addAll(fileList);
        locationsSelect.setChoices(locations);
        target.add(formWrapper);
        adjustButtonVisibility(target);
    }
    
    private ListMultipleChoice<File> buildLocationsSelect(String markupId) {
        final ListMultipleChoice<File> lc = new ListMultipleChoice<File>(
                markupId, new ListModel<File>(), locations) {
            private static final long serialVersionUID = 7754249758151817399L;
            @Override
            protected CharSequence getDefaultChoice(String selected) {
                return "";
            }
            @Override
            protected void appendOptionHtml(
                    AppendingStringBuffer buffer, File file,
                    int index, String selected) {
                super.appendOptionHtml(buffer, file, index, selected);
                if (file == null) {
                    return;
                }
                String icon = "nx-file-icon";
                String notFoundTitle = "";
                if (!file.exists()) {
                    icon = "nx-badpath-icon";
                    notFoundTitle = getString("location.notfound");
                } else if (file.isDirectory()) {
                    icon = "nx-folder-icon";
                } else if (!file.getName().endsWith(".index")) {
                    icon = "nx-badpath-icon";
                    notFoundTitle = getString("location.notindex");
                }
                buffer.insert(buffer.lastIndexOf("\">") + 1,
                        " class=\"" + icon + "\" title=\""
                      + StringEscapeUtils.escapeHtml4(notFoundTitle + 
                              file.getAbsolutePath()) +  "\"");
            }
        };
        lc.setMaxRows(7);
        lc.setOutputMarkupId(true);
        lc.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = -6508661554028761884L;
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                adjustButtonVisibility(target);
            }
        });
        
        return lc;
    }
    
    private void adjustButtonVisibility(AjaxRequestTarget target) {
        Collection<File> files = locationsSelect.getModelObject();
        removeButton.setVisible(files != null && !files.isEmpty());
        target.add(formWrapper);
    }

}
