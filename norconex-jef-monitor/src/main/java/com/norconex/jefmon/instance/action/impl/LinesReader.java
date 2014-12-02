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
package com.norconex.jefmon.instance.action.impl;

import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("nls")
public abstract class LinesReader implements Serializable {

    private static final long serialVersionUID = 8421366237808671573L;

    public static final String MODE_HEAD = "head";
    public static final String MODE_TAIL = "tail";
    public static final String STYLE_FULL = "full";
    public static final String STYLE_COMPACT = "compact";

    private String readMode = MODE_TAIL;
    private String lineStye = STYLE_FULL;
    private int lineQty = 10;

    abstract public String[] readLines() throws IOException;

    public String getReadMode() {
        return readMode;
    }
    public void setReadMode(String readMode) {
        this.readMode = readMode;
    }

    public String getLineStye() {
        return lineStye;
    }
    public void setLineStye(String lineStye) {
        this.lineStye = lineStye;
    }

    public int getLineQty() {
        return lineQty;
    }
    public void setLineQty(int lineQty) {
        this.lineQty = lineQty;
    }
}
