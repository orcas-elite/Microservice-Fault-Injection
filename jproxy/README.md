# JInjectProxy

## Overview

HTTP proxy supporting drop, delay (and more coming soon) for requests.
Written in java, based on jetty and jersey.

Each Proxy has an ID identifying its role in the system. Furthermore each proxy has a random UUID assigned at startup.


## Starting

Usage: "[control-port] [proxy-listen-port] [proxy-to] [proxy-id] [master-url] [influxdb-url]"
Example: "8089 8080 http://0.0.0.0:8080/ ProxyForDatabase http://0.0.0.0:8091/ http://172.17.0.2:8086/"

Drop and delay are disabled by default.


## Connection to Master

Proxy tries to send a hello message to [master-url] containint the JSON status message.


## Metrics

Proxy collects metrics:
- requestsServiced
- requestsDelayed
- requestsDropped

Metrics are periodically (1000ms) written to InfluxDb, if there is a database connection.

## Configuration

There are the following configurations
- Enable/Disable metrics (MsgCount, RequestTime, DroppedMsgs, DelayedMsgs, NLaneDelayedMsgs). By default enabled
- Drop, by default disabled
- Delay, by default disabled
- N-lane Bridge, by default disabled

The proxy can be controlled and configured using REST:


### REST Control Interface

#### GET http://localhost:8089/control/status
Get proxy status, example:

Output: "{"controlPort":8089, "proxyPort":8090,"proxyTag":"TestProxy","proxyUuid":"f6ab607c-b451-4da1-a2ca-039c87529cb2","proxyTarget":"http://0.0.0.0:8080/", "started":true, "pcsConnected":false, "requestsServiced":3,"requestsDelayed":0, "requestsDropped":0}"

#### GET/PUT http://localhost:8089/control/drop
Set drop configuration, enabled and drop probability (0.0 to 1.0). Example:

Input:
{
  "enabled": true,
  "probability": 0.74
}
Output:
200, Config json (same as input)


#### GET/PUT http://localhost:8089/control/delay
Set delay configuration, enabled, delay probability (0.0 to 1.0) and minimum and maximum delay (in milliseconds). Example: 

Input:
{
  "enabled": true,
  "probability": 0.74,
  "delayTimeDistribution": UniformRealDistribution
  "min": 500,
  "max": 800
}
Output:
200, Config json (same as input)


#### GET/PUT http://localhost:8089/control/nlane
Set n-lane bridge delay configuration, enabled, maximum active requests.

Input:
{
  "enabled": true,
  "maxActive": 10
}
Output:
200, Config json (same as input)


#### GET/PUT http://localhost:8089/control/metrics
Enables or disables metrics.

Input:
{
  "enabled": true
}
Output:
200, Config json (same as input)
