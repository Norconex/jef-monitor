package com.norconex.jefmon.ws;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONWriter;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.norconex.jef4.status.JobState;
import com.norconex.jefmon.JEFMonConfig;
import com.norconex.jefmon.JEFMonSession;
import com.norconex.jefmon.instances.InstancesManager;
import com.norconex.jefmon.instances.InstanceSummary;

@SuppressWarnings("nls")
public class JobSuiteProgressJsonPage extends WebPage {

    private static final long serialVersionUID = -1277433265344131627L;

    public JobSuiteProgressJsonPage() {
    }

    public JobSuiteProgressJsonPage(IModel<?> model) {
        super(model);
    }

    public JobSuiteProgressJsonPage(PageParameters pageParameters) {
        super(pageParameters);
    }

    @Override
    public MarkupType getMarkupType() {
        return new MarkupType("json", "application/json");
    }

    @Override
    public void renderPage() {
        JEFMonConfig monitorConfig =
                ((JEFMonSession) getSession()).getConfig();
        if (monitorConfig == null) {
            return;
        }
        try {
            Response response = getRequestCycle().getResponse();
            OutputStreamWriter out =
                    new OutputStreamWriter(response.getOutputStream());

            InstanceSummary instance = 
                    InstancesManager.createThisJefMonInstance();
            JSONWriter writer = new JSONWriter(out)
                .object()
                    .key("name").value(instance.getName())
                    .key("total").value(instance.getTotalRoots())
                    .key("statuses").object();
            for (JobState status : instance.getStatuses().keySet()) {
                MutableInt value = instance.getStatuses().get(status);
                if (status == null) {
                    writer.key("").value(value);
                } else {
                    writer.key(status.toString()).value(value);
                }
            }
            writer.endObject();
            writer.endObject();
            out.flush();
        } catch (IOException e) {
            throw new WicketRuntimeException("Cannot flush JSON response.", e);
        } catch (JSONException e) {
            throw new WicketRuntimeException("Cannot create JSON response.", e);
        }
    }

}
