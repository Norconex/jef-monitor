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
package com.norconex.jefmon;

import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.util.cookies.CookieUtils;

import com.norconex.commons.wicket.markup.html.i18n.SessionLocaleUtils;

/**
 * Subclass of WebSession to allow easy and typesafe
 * access to session properties.
 * @author Pascal Essiembre
 */
@SuppressWarnings("nls")
public final class JEFMonSession extends WebSession {

    /** Logging */
    private static final Log LOG = LogFactory.getLog(JEFMonSession.class);

    /** For serialisation. */
    private static final long serialVersionUID = -6545170315077101133L;

    /**
     * Constructor
     * @param request the request
     */
    protected JEFMonSession(final Request request) {
        super(request);
        initLocale();
    }

    /**
     * Get downcast application object for easy access by subclasses
     * 
     * @return The application
     */
    public JEFMonApplication getApp() {
        return (JEFMonApplication) getApplication();
    }

    public JEFMonConfig getConfig() {
        return getApp().getConfig();
    }

    public static JEFMonSession getSession() {
        return (JEFMonSession) get();
    }

    private void initLocale() {
        String cookieLocale = new CookieUtils()
                .load(SessionLocaleUtils.COOKIE_LOCALE_KEY);
        Locale[] locales = getApp().getSupportedLocales();
        Locale locale = null;
        if (StringUtils.isNotBlank(cookieLocale)) {
            locale = LocaleUtils.toLocale(cookieLocale);
        }
        if (!ArrayUtils.contains(locales, locale)) {
            locale = getLocale();
            if (!ArrayUtils.contains(locales, locale)
                    && locale.getCountry() != null) {
                locale = new Locale(locale.getLanguage(), locale.getCountry());
            }
            if (!ArrayUtils.contains(locales, locale)) {
                locale = new Locale(locale.getLanguage());
            }
            if (!ArrayUtils.contains(locales, locale)) {
                locale = locales[0];
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("User initial locale is:" + locale);
            }
        }
        setLocale(locale);
    }
}
