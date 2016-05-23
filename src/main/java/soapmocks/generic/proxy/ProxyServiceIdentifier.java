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
package soapmocks.generic.proxy;

import soapmocks.api.RequestIdentifier;
import soapmocks.api.ResponseIdentifier;
import soapmocks.generic.SafeFilename;

public final class ProxyServiceIdentifier {

    private final RequestIdentifier requestIdentifier;
    private final ResponseIdentifier responseIdentifier;

    public ProxyServiceIdentifier(RequestIdentifier requestIdentifier,
	    ResponseIdentifier responseIdentifier) {
	this.requestIdentifier = requestIdentifier;
	this.responseIdentifier = responseIdentifier;
    }

    String[] getParameters() {
	return requestIdentifier.getParameters();
    }

    String getMethod() {
	return requestIdentifier.getMethod();
    }

    String generateFilename() {
	return generateFilename(null);
    }

    String generateFilename(String hash) {
	StringBuilder filename = new StringBuilder();
	filename.append(requestIdentifier.getMethod());
	for (String parameter : requestIdentifier.getParameters()) {
	    filename.append("-").append(parameter);
	}
	addHashIfNotNull(hash, filename);
	return new SafeFilename().make(filename.append(".xml").toString());
    }

    private void addHashIfNotNull(String hash, StringBuilder filename) {
	if (hash != null) {
	    filename.append(".resp-" + hash);
	}
    }

    ResponseIdentifier getResponseIdentifier() {
	return responseIdentifier;
    }
    
    
}
