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

import java.util.Arrays;
import java.util.List;

import soapmocks.generic.proxy.ProxyServiceIdentifier;

/**
 * A Thread-Local that ensures correct handling of proxy requests.
 */
public final class ProxyDelegator {

    private static final ThreadLocal<Boolean> IS_DELEGATED = new ThreadLocal<>();
    private static final ThreadLocal<ProxyServiceIdentifier> SERVICE_IDENTIFIER = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> HAS_SERVICE_IDENTIFIER = new ThreadLocal<>();

    /**
     * This enables the proxy delegation, if a proxy is available for the URI.
     * This will skip the current JaxWS service call and send to proxy.
     */
    public static void toProxy() {
	IS_DELEGATED.set(true);
    }

    /**
     * This enables the proxy delegation, if a proxy is available for the URI.
     * In addition it sets the service identifier used by proxy records.
     *
     * @param {@link RequestIdentifier} to identify a service request by method
     *        and some parameters.
     */
    public static void toProxy(RequestIdentifier requestIdentifier) {
	serviceIdentifier(requestIdentifier);
	toProxy();
    }

    /**
     * This enables the proxy delegation, if a proxy is available for the URI.
     * In addition it sets the service identifier used by proxy records.
     * ResponseIdentifier can be used to set response element excludes on hash
     * creation.
     *
     * @param {@link RequestIdentifier} to identify a service request by method
     *        and some parameters.
     * @param {@link ResponseIdentifier} responseIdentifier to be used for
     *        element excludes on hash creation.
     */
    public static void toProxy(RequestIdentifier requestIdentifier, ResponseIdentifier responseIdentifier) {
	serviceIdentifier(requestIdentifier, responseIdentifier);
	toProxy();
    }

    /**
     * Sets the service identifier used by proxy records.
     * 
     * @param {@link RequestIdentifier} to identify a service request by method
     *        and some parameters.
     * @param responseIdentifier
     *            to identify elements in response hash creation and response
     *            object creation
     */
    public static void serviceIdentifier(RequestIdentifier requestIdentifier, ResponseIdentifier responseIdentifier) {
	ProxyServiceIdentifier proxyServiceIdentifier = new ProxyServiceIdentifier(requestIdentifier, responseIdentifier);
	checkForceNoJaxWsMockForMethod(requestIdentifier);
	SERVICE_IDENTIFIER.set(proxyServiceIdentifier);
	HAS_SERVICE_IDENTIFIER.set(true);
    }

    private static void checkForceNoJaxWsMockForMethod(
	    RequestIdentifier requestIdentifier) {
	String forceNoMockMethodsWithComma = System.getProperty(Constants.SOAPMOCKS_JAXWS_NOMOCK_METHODS_FORCE);
	if(forceNoMockMethodsWithComma == null || forceNoMockMethodsWithComma.isEmpty()) {
	    return;
	}
	List<String> forceNoMockMethods = Arrays.asList(forceNoMockMethodsWithComma.split(","));
	if(forceNoMockMethods.contains(requestIdentifier.getMethod())) {
	    toProxy();
	}
    }

    /**
     * Sets the service identifier used by proxy records.
     * 
     * @param {@link RequestIdentifier} to identify a service request by method
     *        and some parameters.
     */
    public static void serviceIdentifier(RequestIdentifier requestIdentifier) {
	serviceIdentifier(requestIdentifier, null);
    }

    /**
     * @return true, if proxy delegation was enabled during current REQ-RESP.
     */
    public static boolean isDelegateToProxy() {
	Boolean isDelegated = IS_DELEGATED.get();
	return isDelegated != null ? isDelegated : false;
    }

    /**
     * @return boolean, true if a service identifier has been set for proxy
     *         records.
     */
    public static boolean hasServiceIdentifier() {
	Boolean isIdentified = HAS_SERVICE_IDENTIFIER.get();
	return isIdentified != null ? isIdentified : false;
    }

    /**
     * @return {@link ProxyServiceIdentifier}
     */
    public static ProxyServiceIdentifier getServiceIdentifier() {
	return SERVICE_IDENTIFIER.get();
    }

    /**
     * Reset the delegation.
     */
    public static void reset() {
	IS_DELEGATED.remove();
    }

    /**
     * Reset the delegation and remove the service identifier.
     */
    public static void restart() {
	reset();
	SERVICE_IDENTIFIER.remove();
	HAS_SERVICE_IDENTIFIER.remove();
    }

}
