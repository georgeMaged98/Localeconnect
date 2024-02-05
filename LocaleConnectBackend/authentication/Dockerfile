FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} authentication.jar

ENTRYPOINT ["java", "-jar", "/authentication.jar"]

EXPOSE 8500