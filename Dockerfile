FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/e-commerce-api-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]