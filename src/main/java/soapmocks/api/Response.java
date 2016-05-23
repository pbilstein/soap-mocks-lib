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

/**
 * An API class to create JaxWS response objects from XML files identified by
 * request information. This class supports the automatic creation of a proxy
 * response into a file using request identifier information.
 */
public final class Response {

    private final ResponseFile responseFile;

    /**
     * Uses soapmocks.files.basedir to find files.
     */
    public Response() {
	this.responseFile = new ResponseFile();
    }

    /**
     * @param baseDir
     *            for finding the files. Relative to soapmocks.files.basedir or
     *            classpath
     */
    public Response(String baseDir) {
	this.responseFile = new ResponseFile(baseDir);
    }

    /**
     * Create the response object using all information given. If nothing was
     * found for method and parameter, it will try to find a default file. If
     * this fails, proxy delegation jumps in if configured. As element for
     * response the classname of classForT starting with lower case will be
     * taken.
     * <p>
     * <p>
     * Supports Service Identifier, so when proxy delegation jumps in, it will
     * be able to create a record.
     * <p>
     * 
     * @param classForResponseType
     *            The type of the response object.
     * @param requestIdentifier
     *            {@link RequestIdentifier} to identify a request and find its
     *            matching response.
     * @return RESPONSE_TYPE Object to return in WebService.
     */
    public <RESPONSE_TYPE> RESPONSE_TYPE using(
	    Class<RESPONSE_TYPE> classForResponseType,
	    RequestIdentifier requestIdentifier) {
	return using(classForResponseType, null, requestIdentifier);
    }

    /**
     * Create the response object using all information given. If nothing was
     * found for method and parameter, it will try to find a default file when
     * ResponseIdentifer is set to DefaultResponse.TRUE. If this fails,
     * proxy delegation jumps in if configured.
     * <p>
     * <p>
     * Supports Service Identifier, so when proxy delegation jumps in, it will
     * be able to create a record.
     * <p>
     * 
     * @param classForResponseType
     *            The type of the response object.
     * @param responseIdentifier
     *            The element in the response file representing the response
     *            object and more information in response for proxy and object
     *            creation.
     * @param requestIdentifier
     *            {@link RequestIdentifier} to identify a request and find its
     *            matching response.
     * @return Object to return in WebService.
     */
    public <RESPONSE_TYPE> RESPONSE_TYPE using(
	    Class<RESPONSE_TYPE> classForResponseType,
	    ResponseIdentifier responseIdentifier,
	    RequestIdentifier requestIdentifier) {
	ProxyDelegator.serviceIdentifier(requestIdentifier, responseIdentifier);
	String filename = new ResponseCreatorFileFinder()
		.findFileFromMethodsAndParameter(
			responseFile.baseDir(),
			responseIdentifier != null ? responseIdentifier
				.getDefaultResponse() : null, requestIdentifier);
	if (filename == null) {
	    throw new ProxyDelegateQuietException("No response file found");
	}
	return responseFile.using(filename, responseIdentifier,
		classForResponseType);
    }

}
