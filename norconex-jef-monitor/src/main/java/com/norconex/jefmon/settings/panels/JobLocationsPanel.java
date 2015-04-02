/* Copyright 2007-2015 Norconex Inc.
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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

import com.norconex.commons.wicket.behaviors.OnClickBehavior;
import com.norconex.commons.wicket.bootstrap.filesystem.BootstrapFileSystemDialog;
import com.norconex.commons.wicket.bootstrap.modal.BootstrapModalLauncher;
import com.norconex.jefmon.JEFMonConfig;

/**
 * Panel for giving a name to a JEF installation.
 * 
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
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

        // --- Add File ---
        IOFileFilter validationFilter = new SuffixFileFilter(".index");
        FilenameFilter browseFilter = new OrFileFilter(
                DirectoryFileFilter.DIRECTORY, validationFilter);
        BootstrapFileSystemDialog fileDialog = new BootstrapFileSystemDialog(
                "addFileModal", new ResourceModel("location.dlg.file"), 
                browseFilter, true) {
            private static final long serialVersionUID = 831482258795791951L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, File[] files) {
                addFileToSelect(target, files);
            }
        };
        fileDialog.setSelectionValidator(validationFilter);
        add(fileDialog);
        WebMarkupContainer addFileButton = 
                new WebMarkupContainer("addFileButton");
        addFileButton.add(new BootstrapModalLauncher(fileDialog));
        formWrapper.add(addFileButton);

        // --- Add Folder ---
        BootstrapFileSystemDialog folderDialog = new BootstrapFileSystemDialog(
                "addFolderModal", new ResourceModel("location.dlg.dir"), 
                DirectoryFileFilter.DIRECTORY, true) {
            private static final long serialVersionUID = -6453512318897096749L;
            @Override
            protected void onSubmit(AjaxRequestTarget target, File[] files) {
                addFileToSelect(target, files);
            }
        };
        folderDialog.setSelectionValidator(DirectoryFileFilter.DIRECTORY);
        add(folderDialog);
        WebMarkupContainer addFolderButton = 
                new WebMarkupContainer("addFolderButton");
        addFolderButton.add(new BootstrapModalLauncher(folderDialog));
        formWrapper.add(addFolderButton);


    
    }

    @Override
    protected void applyState() {
        JEFMonConfig dirtyConfig = getDirtyConfig();
        dirtyConfig.setMonitoredPaths(locations.toArray(new File[]{}));
    }

    private void addFileToSelect(AjaxRequestTarget target, File[] files) {
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
                if (file.isDirectory()) {
                    icon = "nx-folder-icon";
                }
                buffer.insert(buffer.lastIndexOf("\">") + 1,
                        " class=\"" + icon + "\"");
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
