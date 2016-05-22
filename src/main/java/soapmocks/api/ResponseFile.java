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

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import soapmocks.generic.logging.Log;
import soapmocks.generic.logging.LogFactory;

/**
 * An API class to create JaxWS response objects from XML files identified by
 * the filename.
 */
public class ResponseFile {

    private static final Log LOG = LogFactory.create(Response.class);

    private final String baseDir;

    /**
     * Uses soapmocks.files.basedir to find files.
     */
    public ResponseFile() {
	baseDir = "";
    }

    /**
     * @param baseDir
     *            for finding the files. Relative to soapmocks.files.basedir or
     *            classpath
     */
    public ResponseFile(String baseDir) {
	this.baseDir = baseDir;
    }

    /**
     * Will create a response object using the xml file (the SOAP response). As
     * base element the RESPONSE_TYPE will be used with the first char to
     * lower-case.
     * 
     * @param xmlfile
     *            is the filename of the response to be used.
     * @param classForResponseType
     * @return RESPONSE_TYPE object instance
     */
    public <RESPONSE_TYPE> RESPONSE_TYPE using(String xmlfile, Class<RESPONSE_TYPE> classForResponseType) {
	return using(xmlfile, null, classForResponseType);
    }

    /**
     * Will create a response object using the xml file (the SOAP response). As
     * base element the RESPONSE_TYPE will be used with the first char to
     * lower-case.
     * 
     * @param xmlfile
     *            is the filename of the response to be used.
     * @param responseIdentifier
     * @param classForResponseType
     * @return RESPONSE_TYPE object instance
     */
    public <RESPONSE_TYPE> RESPONSE_TYPE using(String xmlfile, ResponseIdentifier responseIdentifierObject,
	    Class<RESPONSE_TYPE> classForResponseType) {
	String responseIdentifier = null;
	if (responseIdentifierObject == null || responseIdentifierObject.getElementResponse() == null) {
	    String simpleName = classForResponseType.getSimpleName();
	    responseIdentifier = Character.toLowerCase(simpleName.charAt(0))
		    + (simpleName.length() > 1 ? simpleName.substring(1) : "");
	} else {
	    responseIdentifier = responseIdentifierObject.getElementResponse();
	}
	try {
	    xmlfile = baseDir + xmlfile;
	    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	    InputStream fileInputStream = new ResponseCreatorFileFinder().getFile(xmlfile);
	    failIfStreamNotFound(xmlfile, fileInputStream);
	    StreamSource streamSource = new StreamSource(fileInputStream);
	    XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(streamSource);
	    boolean found = iterateXmlStreamToElement(responseIdentifier, xmlStreamReader);
	    throwExceptionIfNotFound(xmlfile, responseIdentifier, found);
	    JAXBContext jc = JAXBContext.newInstance(classForResponseType);
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    JAXBElement<RESPONSE_TYPE> jaxbElement = unmarshaller.unmarshal(xmlStreamReader, classForResponseType);
	    xmlStreamReader.close();
	    fileInputStream.close();
	    LOG.out("JaxWS ResponseFile: " + xmlfile);
	    return jaxbElement.getValue();
	} catch (Exception e) {
	    ProxyDelegator.toProxy();
	    throw new ProxyDelegateQuietException(e);
	}
    }

    private boolean iterateXmlStreamToElement(String fromElement, XMLStreamReader xsr) throws XMLStreamException {
	boolean found = false;
	while (xsr.hasNext()) {
	    xsr.next();
	    if (xsr.isStartElement() && xsr.getLocalName().equals(fromElement)) {
		found = true;
		break;
	    }
	}
	return found;
    }

    protected String baseDir() {
	return baseDir;
    }

    private void throwExceptionIfNotFound(String xmlfile, String fromElement, boolean found) {
	if (!found) {
	    throw new ProxyDelegateQuietException(fromElement + " element not found in " + xmlfile);
	}
    }

    private void failIfStreamNotFound(String file, InputStream fileInputStream) throws FileNotFoundException {
	if (fileInputStream == null) {
	    throw new ProxyDelegateQuietException(file + " not found.");
	}
    }

}
