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
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import soapmocks.api.ResponseIdentifier;
import soapmocks.generic.logging.Log;
import soapmocks.generic.logging.LogFactory;

public class Filehasing {
    
    private static final Log LOG = LogFactory.create(ProxyPostHandler.class);
    
    private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public String hash(byte[] xml1, ResponseIdentifier responseIdentifier) {
	return createHash(xml1, responseIdentifier);
    }

    private String createHash(byte[] xml1, ResponseIdentifier responseIdentifier) {
	try {
	    return createHashFromDocument(xml1, responseIdentifier);
	} catch (Exception e) {
	    LOG.info("Hashing failed: " + e.getMessage());
	    return "nohash";
	}
    }


    private String createHashFromDocument(byte[] xml1, ResponseIdentifier responseIdentifier) throws TransformerFactoryConfigurationError,
	    TransformerConfigurationException, TransformerException, NoSuchAlgorithmException, DocumentException, IOException {
	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	transformer.setOutputProperty(OutputKeys.INDENT, "no");
	
	StringWriter writer = new StringWriter();
	transformer.transform(new StreamSource(new ByteArrayInputStream(xml1)), new StreamResult(writer));
	String output = writer.getBuffer().toString();

	OutputFormat outputFormat = OutputFormat.createCompactFormat();
	outputFormat.setIndent(false);
	outputFormat.setSuppressDeclaration(true);
	org.dom4j.Document doc2 = DocumentHelper.parseText(output);

	if (responseIdentifier != null && responseIdentifier.getExcludes() != null) {
		String[] excludes = responseIdentifier.getExcludes();
		for (String exclude : excludes) {
		    @SuppressWarnings("unchecked")
		    List<Node> selectNodes = (List<Node>)doc2.selectNodes("//*[local-name()='"+exclude+"']");
		    if(selectNodes.size()>0) {
			Node node = (Node) selectNodes.get(0);
			node.detach();
		    }
		}
	    }
	
	StringWriter stringWriter = new StringWriter();
	XMLWriter xmlWriter = new XMLWriter( stringWriter, outputFormat );
	xmlWriter.write(doc2);
        
	String resultXml = stringWriter.toString();
	System.out.println(resultXml);
	String hash = (new HexBinaryAdapter()).marshal(MessageDigest.getInstance("MD5").digest(resultXml.getBytes()))
		.toLowerCase();
	return hash;
    }

}
