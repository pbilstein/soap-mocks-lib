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
 * An API class to help identify the response by a hash and excluded elements.
 */
public class ResponseIdentifier {

    private final String elementResponse;
    private final String[] elementExcludes;

    private ResponseIdentifier(String elementResponse, String... elementExcludes) {
	this.elementResponse = elementResponse;
	this.elementExcludes = elementExcludes;
    }

    public String[] getExcludes() {
	return elementExcludes;
    }

    public String getElementResponse() {
	return elementResponse;
    }

    public static Builder with() {
	return new Builder();
    }

    /**
     * Identify the response payload by hash, but the hash will exclude some
     * element (e.g. session elements)
     */
    public static class Builder {
	private String elementResponse;
	private String[] elementExcludes;

	/**
	 * The element of the response object, can be ommitted when response
	 * type in JaxWs is already the same name than the type.
	 */
	public ResponseIdentifier.Builder elementResponse(String elementResponse) {
	    this.elementResponse = elementResponse;
	    return this;
	}

	/**
	 * The elements that shall be excluded during response hash creation.
	 */
	public ResponseIdentifier.Builder elementExcludes(String... elementExcludes) {
	    this.elementExcludes = elementExcludes;
	    return this;
	}

	/**
	 * Build the object.
	 * 
	 * @return {@link ResponseIdentifier} instance.
	 */
	public ResponseIdentifier build() {
	    return new ResponseIdentifier(elementResponse, elementExcludes);
	}
    }
}
