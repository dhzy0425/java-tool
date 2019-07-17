package org.osgl.util;

/*-
 * #%L
 * Java Tool
 * %%
 * Copyright (C) 2014 - 2017 OSGL (Open Source General Library)
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

import org.junit.Test;
import org.osgl.TestBase;

/**
 * Test {@link Keyword}
 */
public class KeywordTest extends TestBase {

    private Keyword keyword;

    @Test
    public void testKeywordFromCamelCase() {
        verify("CamelCase");
    }

    @Test
    public void testKeywordFromSeparatedWordGroups() {
        verify("camel-case");
        verify("camel case");
        verify("Camel, Case");
        verify("camel:case");
        verify("CAMEL_CASE");
        verify("Camel.case");
    }

    @Test
    public void testCamelCaseWithSeparators() {
        keyword = Keyword.of("CamelCase and Separators");
        eq("camel-case-and-separators", keyword.dashed());
        eq("Camel-Case-And-Separators", keyword.httpHeader());
        eq("Camel Case And Separators", keyword.startCase());
        eq(C.listOf("camel", "case", "and", "separators"), keyword.tokens());
    }

    @Test
    public void testAllUpperCases() {
        keyword = Keyword.of("ALL_UPPERCASES");
        eq("all-uppercases", keyword.dashed());
        eq(C.listOf("all", "uppercases"), keyword.tokens());
    }

    @Test
    public void testUpperCases() {
        yes(Keyword.of("HTTPProtocol").matches("http-protocol"));
        yes(Keyword.of("HTTP-Protocol").matches("http-protocol"));
        yes(Keyword.of("HttpV1.1").matches("http-v-1.1"));
        yes(Keyword.of("HTTP v1.1").matches("http-v1.1"));
        yes(Keyword.of("H1").matches("h-1"));
        yes(Keyword.of("oldHTMLFile").matches("old-html-file"));
    }

    @Test
    public void testAcronyms() {
        verifyAcronym("FBZ", "fooBarZee");
        verifyAcronym("<5M", "<500m");
        verifyAcronym("HW!", "Hello World!");
    }

    private void verifyAcronym(String expected, String source) {
        eq(expected, Keyword.of(source).acronym());
    }

    private void verify(String s) {
        keyword = Keyword.of(s);
        eq("camel-case", keyword.dashed());
        eq(keyword.dashed(), keyword.hyphenated());
        eq(keyword.dashed(), keyword.kebabCase());
        eq("camel_case", keyword.underscore());
        eq(keyword.underscore(), keyword.snakeCase());
        eq("Camel case", keyword.readable());
        eq("CamelCase", keyword.camelCase());
        eq(keyword.camelCase(), keyword.pascalCase());
        eq(keyword.camelCase(), keyword.upperCamelCase());
        eq("CAMEL_CASE", keyword.constantName());
        eq("Camel-Case", keyword.httpHeader());
        eq("camelCase", keyword.javaVariable());
        eq(keyword.javaVariable(), keyword.lowerCamelCase());
        eq(C.listOf("camel", "case"), keyword.tokens());
        eq("camel.case", keyword.dotted());
        eq("CC", keyword.acronym());
    }

    @Test
    public void testDigits() {
        yes(Keyword.of("GH111").matches("gh111"));
        yes(Keyword.of("Gh111").matches("gh111"));
        no(Keyword.of("gH111").matches("gh111"));
    }

    @Test
    public void testX() {
        Keyword kw1 = Keyword.of("equalsTo");
        Keyword kw2 = Keyword.of("equals-to");
        yes(kw1.equals(kw2));

        eq(Keyword.of("Lt"), Keyword.of("lt"));
    }

}
