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

import javax.jws.WebService;

import soapmocks.api.ProxyDelegateQuietException;

public class FindWebServiceMethod {

    public static String get() {
	String methodName = "unknownMethod";
	StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
	for (StackTraceElement stackTraceElement : stackTraceElements) {
	    try {
		Class<?> class1 = Class.forName(stackTraceElement.getClassName());
		if(class1.isAnnotationPresent(WebService.class)) {
		    methodName = stackTraceElement.getMethodName();
		}
	    } catch (ClassNotFoundException e) {
		throw new ProxyDelegateQuietException(e);
	    }
    	}
	return methodName;
    }
}
