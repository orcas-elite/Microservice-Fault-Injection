build:
	docker build -t orcaselite/loadgen .

run:
	docker run -e TARGET_URL=http://my.frontend.url:8080 orcaselite/loadgen

bash:
	docker run -it -e TARGET_URL=http://my.frontend.url:8080 orcaselite/loadgen bash

default:
	build