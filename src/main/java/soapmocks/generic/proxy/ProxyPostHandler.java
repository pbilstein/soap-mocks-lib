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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import soapmocks.generic.FaultCreator;
import soapmocks.generic.logging.Log;
import soapmocks.generic.logging.LogFactory;

final class ProxyPostHandler {

    private static final Log LOG = LogFactory.create(ProxyPostHandler.class);
    private static final ProxyCounter COUNTER = new ProxyCounter();
    private final ProxyUrl proxyUrl;

    ProxyPostHandler(ProxyUrl proxyUrl) {
	this.proxyUrl = proxyUrl;
    }

    ProxyHandlerResult doPostInternal(String uri, HttpServletRequest req,
	    HttpServletResponse resp) throws IOException {
	long count = COUNTER.incrementAndGet();
	byte[] requestString = IOUtils.toByteArray(req.getInputStream());
	LOG.out("REQ-" + count + ": " + new String(requestString));

	long time = System.currentTimeMillis();
	ProxyResult proxyResult = sendPost(proxyUrl.proxyUrl(uri),
		new ProxyRequestHeaderCopier().mapHeaderFromRequest(req),
		requestString);
	time = System.currentTimeMillis() - time;

	LOG.out("RESP-" + count + ": " + new String(proxyResult.bodyDeflated));
	new ProxyResponseHeaderCopier().copyHeaderToResponse(resp, proxyResult);
	resp.setStatus(proxyResult.responseCode);
	IOUtils.write(proxyResult.body, resp.getOutputStream());
	new ProxyRecordHandler().handleProxyRecord(proxyResult);
	return new ProxyHandlerResult(time, !proxyResult.isGeneratedFault);
    }

    private ProxyResult sendPost(String url, Map<String, String> reqHeader,
	    byte[] body) {
	try {
	    final HttpURLConnection connection = (HttpURLConnection) new URL(
		    url).openConnection();
	    prepareRequest(reqHeader, body, connection);
	    final int responseCode = connection.getResponseCode();

	    InputStream inputStream = connection.getErrorStream();
	    if (inputStream == null) {
		inputStream = connection.getInputStream();
	    }
	    byte[] response = IOUtils.toByteArray(inputStream);

	    ProxyResult proxyResult = createProxyResult(connection,
		    responseCode, response);

	    return proxyResult;
	} catch (Exception e) {
	    String message = "Error calling target system as proxy: " + e.getMessage();
	    LOG.warn(message);
	    ProxyResult proxyResult = new ProxyResult();
	    proxyResult.responseCode = 500;
	    proxyResult.body = new FaultCreator().createFault(message).getBytes();
	    proxyResult.bodyDeflated = proxyResult.body;
	    proxyResult.isGeneratedFault = true;
	    return proxyResult;
	}
    }

    private void prepareRequest(Map<String, String> reqHeader, byte[] body,
	    final HttpURLConnection connection) throws ProtocolException,
	    IOException {
	connection.setDoOutput(true);
	connection.setRequestMethod("POST");
	for (final Entry<String, String> header : reqHeader.entrySet()) {
	    connection.setRequestProperty(header.getKey(), header.getValue());
	}
	final OutputStream outputStream = connection.getOutputStream();
	IOUtils.write(body, outputStream);
	outputStream.flush();
	outputStream.close();
    }

    private ProxyResult createProxyResult(HttpURLConnection connection,
	    int responseCode, byte[] response) throws IOException {
	ProxyResult proxyResult = new ProxyResult();
	proxyResult.responseCode = responseCode;
	proxyResult.header = connection.getHeaderFields();

	if (proxyResult.header != null
		&& proxyResult.header.containsKey("Content-Encoding")
		&& proxyResult.header.get("Content-Encoding").get(0)
			.equals("gzip")) {
	    proxyResult.bodyDeflated = IOUtils.toByteArray(new GZIPInputStream(
		    new ByteArrayInputStream(response)));
	} else {
	    proxyResult.bodyDeflated = response;
	}
	proxyResult.body = response;
	return proxyResult;
    }

}
