package com.norconex.jefmon.instance.action.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;

import com.norconex.commons.wicket.bootstrap.form.BootstrapSelect;
import com.norconex.commons.wicket.markup.html.panel.CssPanel;

public class LogViewerActionPanel extends CssPanel {

    private static final long serialVersionUID = -7099355429414346698L;
    private final LinesReader linesReader;
    private final WebMarkupContainer logLinesWrapper;

    public LogViewerActionPanel(String id, LinesReader linesReader) {
        super(id);
        this.linesReader = linesReader;

        this.logLinesWrapper = new WebMarkupContainer("logLinesWrapper") {
                private static final long serialVersionUID = 
                        -8051862574434648486L;
            protected void onBeforeRender() {
                try {
                addOrReplace(createLogLines());
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                super.onBeforeRender();
            };
        };
        this.logLinesWrapper.setOutputMarkupId(true);
        Form<Void> form = new Form<Void>("form");
        add(form);
        
        form.add(createReadDirectionDropDown());
        form.add(createLineQtyDropDown());
        form.add(createLineStyleDropDown());
        
        form.add(new AjaxButton("refresh") {
            private static final long serialVersionUID = 
                    3578370723919187057L;
            @Override
            protected void onSubmit(
                    AjaxRequestTarget target, Form<?> theform) {
                refreshLines(target);
            }
        });
        add(logLinesWrapper);

    }
    
    private LinesReader getLinesReader() {
        return linesReader;
    }
    
    private DropDownChoice<String> createReadDirectionDropDown() {
        List<String> choices = Arrays.asList(
                LinesReader.MODE_TAIL, LinesReader.MODE_HEAD);
        final DropDownChoice<String> dd = new DropDownChoice<String>(
                "readDirection", new Model<String>(LinesReader.MODE_TAIL), 
                choices, new IChoiceRenderer<String>() {
            private static final long serialVersionUID = 4651210594612011298L;
            @Override
            public Object getDisplayValue(String object) {
                return getString("viewer.readDirection." + object) ;
            }
            @Override
            public String getIdValue(String object, int index) {
                return object;
            }
        });
        // Add Ajax Behaviour...
        dd.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 4577648689377807050L;
            protected void onUpdate(AjaxRequestTarget target) {
                linesReader.setReadMode(dd.getInput());
                refreshLines(target);
            }
        });
        dd.add(new BootstrapSelect());
        return dd;
    }

    private DropDownChoice<String> createLineStyleDropDown() {
        List<String> choices = Arrays.asList(
                LinesReader.STYLE_FULL, LinesReader.STYLE_COMPACT);
        final DropDownChoice<String> dd = new DropDownChoice<String>(
                "lineStyle", new Model<String>(LinesReader.STYLE_FULL), choices, 
                        new IChoiceRenderer<String>() {
            private static final long serialVersionUID = 4651210594612011298L;
            @Override
            public Object getDisplayValue(String object) {
                return getString("viewer.lineStyle." + object) ;
            }
            @Override
            public String getIdValue(String object, int index) {
                return object;
            }
        });
        // Add Ajax Behaviour...
        dd.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 7626315421462420718L;
            protected void onUpdate(AjaxRequestTarget target) {
                linesReader.setLineStye(dd.getInput());
                refreshLines(target);
            }
        });
        dd.add(new BootstrapSelect());
        return dd;
    }
    
    private Component createLogLines() {
        String text;
        try {
            text = StringUtils.join(
                    getLinesReader().readLines(), "\n");
        } catch (IOException e) {
            text = "Could not read log. Error is: " + e.getLocalizedMessage();
        }
        Label lines = new Label("logLines", text);
        return lines;
    }
    
    private DropDownChoice<Integer> createLineQtyDropDown() {
        final List<Integer> choices = Arrays.asList(10, 20, 50, 100);
        final DropDownChoice<Integer> lineQtyDDC = new DropDownChoice<Integer>(
                "lineQty", new Model<Integer>(10), choices, 
                        new IChoiceRenderer<Integer>() {
            private static final long serialVersionUID = 6925230811025968952L;
            @Override
            public Object getDisplayValue(Integer object) {
                return object.toString();
            }
            @Override
            public String getIdValue(Integer object, int index) {
                return object.toString();
            }
        });
        // Add Ajax Behaviour...
        lineQtyDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            private static final long serialVersionUID = 3867662005721342005L;
            protected void onUpdate(AjaxRequestTarget target) {
                linesReader.setLineQty(Integer.parseInt(lineQtyDDC.getInput()));
                refreshLines(target);
            }
        });
        lineQtyDDC.add(new BootstrapSelect());
        return lineQtyDDC;
    }
    
    private void refreshLines(AjaxRequestTarget target) {
        target.add(logLinesWrapper);
        target.appendJavaScript("resizeLogViewerLines();");
    }
}
