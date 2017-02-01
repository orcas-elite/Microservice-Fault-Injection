IMAGE=pcs
TAG=latest

build:
	mvn package
	docker build -t $(IMAGE):$(TAG) .

run:
	docker run -it -P $(IMAGE):$(TAG)

bash:
	docker run -it -P $(IMAGE):$(TAG) bash

default:
	build