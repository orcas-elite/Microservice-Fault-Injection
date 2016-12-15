FROM openjdk:alpine

RUN mkdir -p /opt/jproxy
ADD target/proxy-0.0.1-SNAPSHOT.jar /opt/jproxy/proxy.jar
#RUN chmod a+x /opt/jproxy/proxy.jar

EXPOSE 8088 8081

CMD ["java -jar /opt/jproxy/proxy.jar 8088 8081 http://0.0.0.0:8080/"]
