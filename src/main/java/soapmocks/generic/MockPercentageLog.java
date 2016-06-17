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

import java.util.concurrent.atomic.AtomicLong;

final class MockPercentageLog {

    private static final AtomicLong COUNTER_PROXY = new AtomicLong();
    private static final AtomicLong COUNTER_PROXY_FAILED = new AtomicLong();
    private static final AtomicLong COUNTER_MOCK = new AtomicLong();

    String logMock() {
	return logPercentage(COUNTER_MOCK.incrementAndGet(),
		COUNTER_PROXY.get(), COUNTER_PROXY_FAILED.get());
    }

    String logProxy() {
	return logPercentage(COUNTER_MOCK.get(),
		COUNTER_PROXY.incrementAndGet(), COUNTER_PROXY_FAILED.get());
    }

    String logProxyFailed() {
	return logPercentage(COUNTER_MOCK.get(), COUNTER_PROXY.get(),
		COUNTER_PROXY_FAILED.incrementAndGet());
    }

    String logPercentage(long mocks, long proxy, long proxyFailed) {
	double percent = proxy == 0 && proxyFailed == 0 ? 100 : (mocks * 100) / (proxy + proxyFailed + mocks);
	return "Currently " + percent + "% MOCKS [Mocks: " + mocks
		+ " <> Proxy: " + proxy + " (" + proxyFailed + " failed)"+"]";
    }
}
