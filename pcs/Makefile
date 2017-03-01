IMAGE=pcs
TAG=latest

build:
	mvn package
	docker build -t $(IMAGE):$(TAG) .

run:
	docker run -it -p 9090:9090 $(IMAGE):$(TAG)

bash:
	docker run -it -P $(IMAGE):$(TAG) bash

default:
	build