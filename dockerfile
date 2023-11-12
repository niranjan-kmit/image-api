FROM node:latest
RUN apt-get update \
&& apt-get install default-jre -y \
&& apt-get install default-jdk -y \
WORKDIR /usr/app
COPY target/image-api-0.0.1-SNAPSHOT.jar /usr/app/
ENTRYPOINT ["java", "-jar", "image-api-0.0.1-SNAPSHOT.jar"]

