FROM openjdk:latest
ARG projectname
ARG version
COPY target/*.jar /usr/app/
EXPOSE 81
ENTRYPOINT ["/bin/sh", "-c", "java -jar /usr/app/*.jar"]
