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

import javax.xml.ws.ProtocolException;

import soapmocks.generic.logging.Log;
import soapmocks.generic.logging.LogFactory;

/**
 * Used in SoapMocks to handle {@link Response} parts where files are not found
 * or other things go wrong. Sets Proxy delegation on creation.
 */
// ProtocolException because it is logged as debug in jaxws-rt
public final class ProxyDelegateException extends ProtocolException {

    private static final Log LOG = LogFactory.create(ProxyDelegateException.class);

    private static final long serialVersionUID = 1L;

    public ProxyDelegateException(Exception e) {
	super(e.getMessage());
	ProxyDelegator.toProxy();
    }

    public ProxyDelegateException(String message) {
	super(message);
	LOG.info(message);
	ProxyDelegator.toProxy();
    }

}
