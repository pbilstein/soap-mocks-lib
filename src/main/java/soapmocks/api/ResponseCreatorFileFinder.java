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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import soapmocks.generic.SafeFilename;
import soapmocks.generic.logging.Log;
import soapmocks.generic.logging.LogFactory;

final class ResponseCreatorFileFinder {

    private static final Log LOG = LogFactory.create(ResponseCreatorFileFinder.class);
    private final SafeFilename safeFilename = new SafeFilename();;

    String findFileFromMethodsAndParameter(String basedir, DefaultResponse defaultResponse,
	    RequestIdentifier requestIdentifier) {
	final String method = requestIdentifier.getMethod();
	final String[] parameters = requestIdentifier.getParameters();
	String filename = method;
	for (String parameter : parameters) {
	    filename += "-" + parameter;
	}
	filename += ".xml";
	
	filename = "/" + safeFilename.make(filename);
	
	InputStream fileInputStream = getFile(basedir + filename);

	if (fileInputStream == null) {
	    if (DefaultResponse.TRUE == defaultResponse) {
		filename = "/" + safeFilename.make(method + "-default.xml");
	    } else {
		throw new ProxyDelegateQuietException(filename + " not found");
	    }
	} else {
	    closeQuietly(fileInputStream);
	}

	fileInputStream = getFile(basedir + filename);
	closeFileOrFailIfNotFound(filename, fileInputStream);

	return filename;
    }

    InputStream getFile(String filename) {
	String basedir = System.getProperty(Constants.SOAPMOCKS_FILES_BASEDIR_SYSTEM_PROP);
	if (basedir != null) {
	    String absoluteFilename = basedir + filename;
	    File file = new File(absoluteFilename);
	    if (file.exists() && file.isFile()) {
		return fileInputStream(file);
	    } else {
		return null;
	    }
	}
	return getClass().getResourceAsStream(filename);
    }

    private InputStream fileInputStream(File file) {
	try {
	    return new FileInputStream(file);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private void closeFileOrFailIfNotFound(String filename, InputStream fileInputStream) {
	if (fileInputStream == null) {
	    throw new ProxyDelegateQuietException(filename + " not found");
	} else {
	    closeQuietly(fileInputStream);
	}
    }

    private void closeQuietly(InputStream fileInputStream) {
	try {
	    fileInputStream.close();
	} catch (IOException e) {
	    LOG.error(e);
	}
    }

}
