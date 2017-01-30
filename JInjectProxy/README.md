# Proxy

## Overview

HTTP proxy supporting drop, delay (and more coming soon) for requests.
Written in java, based on jetty and jersey.

Main project is in "proxy" folder.
request-test is a simple http request test (can also be used for performance testing).
test-server is a simple http server, returning a hello.


## Starting

Usage: "[control-port] [proxy-listen-port] [proxy-to] [proxy-id] [master-url] [influxdb-url]"
Example: "8088 8081 http://0.0.0.0:8080/ http://0.0.0.0:8089/ ProxyForDatabase  http://0.0.0.0:8080/ http://172.17.0.2:8086"

Drop and delay are disabled by default.

## REST Control Interface

The proxy can be controlled using REST:

#### GET http://localhost:8088/control/status
Get proxy status, example:

Output: "{"proxy":"started"}"

#### POST http://localhost:8088/control/set/drop
Set drop configuration, enabled and drop probability (0.0 to 1.0). Example:

Input:
{
  enabled: true,
  probability: 0.74
}
Output:
200 "Success"


#### POST http://localhost:8088/control/set/delay
Set delay configuration, enabled, delay probability (0.0 to 1.0) and minimum and maximum delay (in milliseconds). Example: 

Input:
{
  enabled: true,
  probability: 0.74,
  min: 500,
  max: 800
}
Output:
200 "Success"

