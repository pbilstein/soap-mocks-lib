/*
Copyright 2016 Peter Bilstein

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package soapmocks.generic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestSafeFilename {

    SafeFilename safeFilename = new SafeFilename();
    private String filename;
    private String result;
    private String expectedResult;
    
    @Test
    public void assureThatWhitespaceIsReplaced() {
	filename = "getTest-blah-some param.xml";
	expectedResult = "getTest-blah-some_param.xml";
	runMakeAndAssertResult();
    }

    private void runMakeAndAssertResult() {
	result = safeFilename.make(filename);
	assertEquals(expectedResult,result);
    }
}
