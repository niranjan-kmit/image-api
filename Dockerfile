FROM openjdk:8-jdk-alpine
MAINTAINER niranjan
COPY target/image-api-0.0.1-SNAPSHOT.jar image-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/image-api-0.0.1-SNAPSHOT.jar"]
