# Microservice-Fault-Injection

## Overview

A framework to test resilience of microservice architectures using proxies to inject failures.

- jproxy is the injection proxy project
- pcs is the proxy control application
- proxy-commons contains classes for communication with and between jproxy and pcs
- testing/arch-gen contains generated test microservices
- testing/request-test is a simple http request test (can also be used for performance testing).
- testing/test-server is a simple http server, returning a hello.

More information about jproxy and pcs in the folders READMEs

TODO Examples