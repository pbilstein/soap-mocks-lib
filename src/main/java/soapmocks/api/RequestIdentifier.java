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
package soapmocks.api;

import soapmocks.generic.FindWebServiceMethod;

/**
 * An object to identify a service request by WebService calling method and some
 * parameters. This identifier will generate a filename by given parameters.
 * Example: Webservice method 'doSomething' and Identifier.with("blah",
 * "suelz"); will try to find a file named doSomething-blah-suelz.xml
 */
public final class RequestIdentifier {

    private final String method;
    private final String[] parameters;

    private RequestIdentifier(String method, String... parameters) {
	this.method = method;
	this.parameters = parameters;
    }

    /**
     * Identify the request by parameters from payload. The webservice method
     * will be retrieved automatically via stacktrace finding the calling class
     * with Webservice annotation.
     * <p>
     * 
     * @param parameters
     *            Parameter strings from request to identify unique request to a
     *            matching response
     * @return Identifier object
     */
    public static RequestIdentifier by(String... parameters) {
	return new RequestIdentifier(FindWebServiceMethod.get(), parameters);
    }

    /**
     * Identify the request by parameters from payload. The webservice method is
     * set manually.
     * <p>
     * 
     * @param method
     *            The calling webservice method so identify a response file.
     * @param parameters
     *            Parameter strings from request to identify unique request to a
     *            matching response
     * @return Identifier object
     */
    public static RequestIdentifier byManualMethod(String method, String... parameters) {
	return new RequestIdentifier(method, parameters);
    }

    public String getMethod() {
	return method;
    }

    public String[] getParameters() {
	return parameters;
    }

}
