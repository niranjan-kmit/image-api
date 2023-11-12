FROM node:latest
RUN apt-get update \
&& apt-get install default-jre -y \
&& apt-get install default-jdk -y
COPY target/image-api-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "image-api-0.0.1-SNAPSHOT.jar"]
