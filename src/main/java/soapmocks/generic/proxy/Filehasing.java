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
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import soapmocks.api.ResponseIdentifier;

public class Filehasing {
    
    private static final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public String hash(byte[] xml1, ResponseIdentifier responseIdentifier) {
	return createHash(xml1, responseIdentifier);
    }

    private String createHash(byte[] xml1, ResponseIdentifier responseIdentifier) {
	try {
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(new ByteArrayInputStream(xml1));

	    if (responseIdentifier != null && responseIdentifier.getExcludes() != null) {
		String[] excludes = responseIdentifier.getExcludes();
		for (String exclude : excludes) {
		    deleteExclude(doc, exclude);
		}
	    }

	    return createHashFromDocument(doc);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void deleteExclude(Document doc, String exclude) {
	NodeList nodes = doc.getElementsByTagName(exclude);
	for (int i = 0; i < nodes.getLength(); i++) {
	    Element element = (Element) nodes.item(i);
	    element.getParentNode().removeChild(element);
	}
    }

    private String createHashFromDocument(Document doc) throws TransformerFactoryConfigurationError,
	    TransformerConfigurationException, TransformerException, NoSuchAlgorithmException, DocumentException, IOException {
	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	transformer.setOutputProperty(OutputKeys.INDENT, "no");
	
	StringWriter writer = new StringWriter();
	transformer.transform(new DOMSource(doc), new StreamResult(writer));
	String output = writer.getBuffer().toString();

	OutputFormat outputFormat = OutputFormat.createCompactFormat();
	outputFormat.setIndent(false);
	outputFormat.setSuppressDeclaration(true);
	org.dom4j.Document doc2 = DocumentHelper.parseText(output);
	
	StringWriter stringWriter = new StringWriter();
	XMLWriter xmlWriter = new XMLWriter( stringWriter, outputFormat );
	xmlWriter.write(doc2);
        
	String resultXml = stringWriter.toString();
	String hash = (new HexBinaryAdapter()).marshal(MessageDigest.getInstance("MD5").digest(resultXml.getBytes()))
		.toLowerCase();
	return hash;
    }

}
