FROM openjdk:17-jdk-slim

WORKDIR /meetup

COPY ./target/meetup-1.0-SNAPSHOT.jar /meetup/meetup-service.jar

ENTRYPOINT ["java", "-jar", "/meetup/meetup-service.jar"]