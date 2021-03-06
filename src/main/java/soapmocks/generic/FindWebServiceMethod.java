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

import java.lang.annotation.Annotation;

import javax.jws.WebService;

import soapmocks.api.Constants;
import soapmocks.api.ProxyDelegateException;

public class FindWebServiceMethod {

    public static String get() {
	String methodName = null;
	StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
	for (StackTraceElement stackTraceElement : stackTraceElements) {
	    try {
		String className = stackTraceElement.getClassName();
		if(className.startsWith(Constants.SOAPMOCKS_SERVICES_PACKAGE)) {
		    Class<?> class1 = Class.forName(className);
		    Annotation[] annotations = class1.getAnnotations();
		    for (Annotation annotation : annotations) {
			if(annotation.annotationType().getCanonicalName().equals(WebService.class.getCanonicalName())) {
			    methodName = stackTraceElement.getMethodName();
			}
		    }
		}
	    } catch (ClassNotFoundException e) {
		continue;
	    }
    	}
	if(methodName==null) {
	    throw new ProxyDelegateException("Webservice method not found.");
	}
	return methodName;
    }
}
