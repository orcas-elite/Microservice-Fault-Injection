FROM openjdk:alpine

MAINTAINER  Jonas Grunert <public@jonas-grunert.de>
WORKDIR /jproxy

ADD target/proxy-0.0.1-SNAPSHOT.jar /jproxy/proxy.jar

EXPOSE 8088 8081

CMD ["java","-jar","./proxy.jar","8088","8081","http://0.0.0.0:8080/"]
