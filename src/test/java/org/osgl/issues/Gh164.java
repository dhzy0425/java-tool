package org.osgl.issues;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2018 OSGL (Open Source General Library)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Before;
import org.junit.Test;
import org.osgl.$;
import org.osgl.TestBase;
import org.osgl.util.IO;
import org.osgl.util.S;

import java.io.StringWriter;
import java.nio.ByteBuffer;

public class Gh164 extends TestBase {

    private ByteBuffer buf;
    private String str;

    @Before
    public void prepare() {
        str = S.longUrlSafeRandom();
        buf = $.convert(str).toByteBuffer();
    }

    @Test
    public void test() {
        String s = IO.read(buf).toString();
        eq(str, s);
    }

    @Test
    public void test2() {
        StringWriter sw = new StringWriter();
        IO.write(buf).to(sw);
        eq(str, sw.toString());
    }

}
