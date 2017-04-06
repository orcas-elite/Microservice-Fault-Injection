build:
	mvn package
	docker build -t orcaselite/pcs .

jar:
	java -jar target/pcs-1.0-SNAPSHOT.jar server cfg.yml

run:
	docker run -it -p 9090:9090 orcaselite/pcs

bash:
	docker run -it -P orcaselite/pcs bash

default:
	build