cd proxy-commons
mvn clean install
cd ../JInjectProxy/proxy
./docker-build.sh
cd ../../pcs
make