FROM openjdk:alpine

MAINTAINER  Jan Strauss <os@over9000.eu>
WORKDIR /pcs

ADD target/pcs-1.0-SNAPSHOT.jar /pcs/pcs.jar
ADD cfg.yml /pcs/cfg.yml

CMD java -jar pcs.jar server cfg.yml

EXPOSE 9090
