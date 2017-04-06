IMAGE=pcs

build:
	mvn package
	docker build -t orcaselite/$(IMAGE) .

jar:
	java -jar target/pcs.jar server cfg.yml

run:
	docker run -it -p 9090:9090 $(IMAGE)

bash:
	docker run -it -P $(IMAGE) bash

default:
	build