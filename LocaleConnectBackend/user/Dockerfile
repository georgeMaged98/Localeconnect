FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests -f user/pom.xml

FROM openjdk:17-jdk-slim

WORKDIR /user

COPY --from=builder /app/user/target/user-1.0-SNAPSHOT.jar /user/user-service.jar

ENTRYPOINT ["java", "-jar", "/user/user-service.jar"]