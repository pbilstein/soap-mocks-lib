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
package soapmocks.generic.servlet;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.MDC;

@WebFilter(urlPatterns={"/*"})
public class CounterFilter implements Filter {

    private static final String MDC_COUNTER_KEY = "counter";

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private static final ThreadLocal<Long> CURRENT_COUNT = new ThreadLocal<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {
	try {
	    long count = COUNTER.incrementAndGet();
	    CURRENT_COUNT.set(count);
	    MDC.put(MDC_COUNTER_KEY, count(count));
	    chain.doFilter(request, response);
	} finally {
	    CURRENT_COUNT.remove();
	    MDC.remove(MDC_COUNTER_KEY);
	}
    }

    @Override
    public void destroy() {
    }

    public static long currentCount() {
	Long currentCount = CURRENT_COUNT.get();
	return currentCount != null ? currentCount : -1;
    }

    public static boolean hasCurrentCount() {
	Long currentCount = CURRENT_COUNT.get();
	return currentCount != null && currentCount > -1;
    }
    
    private String count(long count) {
	return "[" + count + "] ";
    }

}
