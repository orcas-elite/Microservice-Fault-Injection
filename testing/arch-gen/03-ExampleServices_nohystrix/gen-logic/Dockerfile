FROM openjdk:alpine

EXPOSE 8080

COPY target/logic-nohystrix-1.2.5.jar /

CMD java -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=* -jar logic-nohystrix-1.2.5.jar
