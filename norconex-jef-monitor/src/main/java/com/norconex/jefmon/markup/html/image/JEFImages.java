package com.norconex.jefmon.markup.html.image;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;

import com.norconex.commons.wicket.markup.html.image.CacheableImage;

public final class JEFImages {

    public static final ResourceReference REF_JOB_BLANK = 
            new SharedResourceReference("/images/job-blank.png");
    public static final ResourceReference REF_JOB_ERROR = 
            new SharedResourceReference("/images/job-error.gif");
    public static final ResourceReference REF_JOB_OK = 
            new SharedResourceReference("/images/job-ok.gif");
    public static final ResourceReference REF_JOB_RUNNING = 
            new SharedResourceReference("/images/job-running.gif");
    
    private JEFImages() {
        super();
    }

    public static Image jobBlank(String markupId) {
        return new CacheableImage(markupId, REF_JOB_BLANK);
    }
    public static Image jobError(String markupId) {
        return new CacheableImage(markupId, REF_JOB_ERROR);
    }
    public static Image jobOK(String markupId) {
        return new CacheableImage(markupId, REF_JOB_OK);
    }
    public static Image jobRunning(String markupId) {
        return new CacheableImage(markupId, REF_JOB_RUNNING);
    }
}
