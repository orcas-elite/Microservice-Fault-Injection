FROM openjdk:alpine

MAINTAINER  Jan Strauss <os@over9000.eu>
WORKDIR /pcs

ADD target/pcs-1.0-SNAPSHOT.jar /pcs/pcs.jar

CMD java -Ddw.server.applicationConnectors[0].port=9090 -jar pcs.jar server

EXPOSE 9090
