/* Copyright 2007-2014 Norconex Inc.
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
