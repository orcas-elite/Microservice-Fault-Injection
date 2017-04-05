cd proxy-commons
mvn clean install
cd ../jproxy
./docker-build.sh
cd ../pcs
make