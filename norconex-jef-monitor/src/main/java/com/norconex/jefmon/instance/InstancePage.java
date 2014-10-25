package com.norconex.jefmon.instance;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.jefmon.JEFMonPage;

public class InstancePage extends JEFMonPage {

    private static final long serialVersionUID = 1559009691638968793L;

    public InstancePage() {
        super();
    }
    public InstancePage(IModel<?> model) {
        super(model);
    }
    public InstancePage(PageParameters pageParameters) {
        super(pageParameters);
    }

    @Override
    protected IModel<String> getTitle() {
        return Model.of("JEF Jobs");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new InstancePanel("treePanel"));
    }

}
