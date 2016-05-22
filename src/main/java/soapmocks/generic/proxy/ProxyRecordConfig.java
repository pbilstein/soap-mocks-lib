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

import java.io.File;

import soapmocks.api.Constants;

public final class ProxyRecordConfig {

    public static String getProxyTraceDir() {
	String proxyRecordDir = System
		.getProperty(Constants.SOAPMOCKS_PROXYRECORD_DIR_SYSTEM_PROP);
	if (proxyRecordDir != null && !proxyRecordDir.isEmpty()) {
	    return proxyRecordDir + File.separatorChar;
	}
	return "target" + File.separatorChar + "proxyrecord" + File.separatorChar;
    }
    
    public static String getProxyTraceBaseDir() {
	String proxyRecordBaseDir = System.getProperty("basedir");
	return proxyRecordBaseDir != null ? proxyRecordBaseDir
		+ File.separatorChar : "";
    }

    public static String getProxyTraceAbsoluteDir() {
	return getProxyTraceBaseDir() + getProxyTraceDir();
    }
}
