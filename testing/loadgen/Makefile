build:
	docker build -t orcaselite/loadgen .

run:
	docker run -e TARGET_URL=http://172.17.0.1:8080 -e RUNS=2 -p 8000:8000 orcaselite/loadgen

default:
	build