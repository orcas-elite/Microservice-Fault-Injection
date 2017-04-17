build:
	docker build -t orcaselite/loadgen .

run:
	docker run -e TARGET_URL=http://172.17.0.1:8080 -e RUNS=1 orcaselite/loadgen

bash:
	docker run -it -e TARGET_URL=http://172.17.0.1:8080 -e RUNS=1 orcaselite/loadgen bash

default:
	build