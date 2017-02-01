# PCS

proxy control service..

## Build

`make`

## Run

`make run`

## Endpoints

```
GET     /proxy 			list available proxies
PUT     /proxy 			register new proxy
DELETE  /proxy/{id} 		delete a proxy by its id
GET     /proxy/{id} 		get a specific proxy
POST    /proxy/{id}/delay 	configure a delay action on the proxy with the given id
POST    /proxy/{id}/drop 	configure a drop action on the proxy with the given id
POST    /proxy/{id}/nlane 	configure a drop action on the proxy with the given id
POST    /proxy/{id}/metrics 	configure a drop action on the proxy with the given id
GET     /proxy/{id}/status 	retrieve the status information for the given proxy
```

POST/PUT data should be `application/json`, successful responses will also be `application/json`

## Data

For the data format for the json payloads see the classes in `de.uni_stuttgart.informatik.rss.msinject.pcs.models`