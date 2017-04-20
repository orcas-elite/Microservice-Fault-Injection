# PCS

proxy control service..

## Build

`make`

## Run

`make run`

## Endpoints

```
GET     /proxy 			list available proxies
PUT     /proxy 			register/refresh new proxy
DELETE  /proxy/{id} 		delete a proxy by its id
GET     /proxy/{id} 		get a specific proxy
POST    /proxy/{id}/delay 	configure a delay action on the proxy with the given id
POST    /proxy/{id}/drop 	configure a drop action on the proxy with the given id
POST    /proxy/{id}/nlane 	configure a drop action on the proxy with the given id
POST    /proxy/{id}/metrics 	configure a drop action on the proxy with the given id
GET     /proxy/{id}/status 	retrieve the status information for the given proxy
GET     /tag/{tag}/{delay,drop,nlane} 	get delay, drop or nlane for all proxies with given tag
POST    /tag/{tag}/{delay,drop,nlane} 	configure delay, drop or nlane for all proxies with given tag
```

POST/PUT data should be `application/json`, successful responses will also be `application/json`

## Data

For the data format for the json payloads see the classes in `de.uni_stuttgart.informatik.rss.msinject.pcs.models`

## Example

```python
import requests

# add/refresh proxy
r = requests.put('localhost:9090/proxy', json={
'id':'wasd',
'uuid':'qqq',
'controlPort': 5781,
'proxyPort': 5501
})

# enable packet dropping
r = requests.post('localhost:9090/proxy/qqq/drop', json={
'enabled':True,
'probability':0.15
})

# list all registered proxies
r = requests.get('localhost:9090/proxy')

```
