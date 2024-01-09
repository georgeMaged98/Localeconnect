FROM openjdk:17-jdk-slim

WORKDIR /user

COPY ./target/user-1.0-SNAPSHOT.jar /user/user-service.jar

ENTRYPOINT ["java", "-jar", "/user/user-service.jar"]
