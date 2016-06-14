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

/**
 * {@link FaultCreator}
 */
public final class FaultCreator {

    /**
     * Creates a fault for error situations.
     * 
     * @param message to be added in Fault
     * @return Fault as String
     */
    public String createFault(String message) {
	String fault = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
		+ "	<SOAP-ENV:Header/>\n"
		+ "	<SOAP-ENV:Body>\n"
		+ "		<SOAP-ENV:Fault>\n"
		+ "			<faultcode>SOAP-ENV:Server</faultcode>\n"
		+ "			<faultstring>"
		+ message
		+ "</faultstring>\n"
		+ "			<faultactor>http://soap-mocks-lib.de/mocks</faultactor>\n"
		+ "			<detail></detail>\n"
		+ "		</SOAP-ENV:Fault>\n"
		+ "	</SOAP-ENV:Body>\n" + "</SOAP-ENV:Envelope>";
	return fault;
    }
    
}
