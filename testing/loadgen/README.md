# Loadgen

Simple loadgen container based on taurus.

## Build

make

## Usage

```bash
docker run -e TARGET_URL=http://my.frontend.url:8080 -e RUNS=5 orcaselite/loadgen
```

or 

```bash
make run
```

