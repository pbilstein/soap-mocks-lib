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

/**
 * Containing all necessary information to be used after proxy handling.
 */
public final class ProxyHandlerResult {

    private final long tookTimeMillis;
    private final boolean successful;
    
    public ProxyHandlerResult(long tookTimeMillis, boolean successful) {
	this.tookTimeMillis = tookTimeMillis;
	this.successful = successful;
    }

    public long getTookTimeMillis() {
	return tookTimeMillis;
    }

    public boolean isSuccessful() {
	return successful;
    }
}
