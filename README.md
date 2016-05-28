# soap-mocks-lib
[![Build Status](https://travis-ci.org/pbilstein/soap-mocks-lib.svg?branch=master)](https://travis-ci.org/pbilstein/soap-mocks-lib)

A jar that can be put into a war to easily create an application for SOAP service mocking and proxying with following features:

* Static file mocking configuration via properties, no code needed
  * Simple default response xml
  * Conditional response xml based on request contains
* Use @WebService annotated JaxWS services and implement mocks via powerful soapmocks.api
  * Identify fitting response by simple RequestIdentifier API
  * Supports automatic response recording via Proxy API
