FROM openjdk:latest
EXPOSE 8081
ADD target/problem-management.jar problem-management.jar
ENTRYPOINT ["java","-jar","problem-management.jar"]