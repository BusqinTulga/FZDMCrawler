FROM maven:3.9.11-eclipse-temurin-8 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar fzdm-crawler.jar
ENTRYPOINT ["java", "-jar", "fzdm-crawler.jar"]