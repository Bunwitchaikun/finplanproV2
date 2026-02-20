# Stage 1: Build stage
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Railway uses dynamic PORT via env variable
EXPOSE ${PORT:-8083}

ENTRYPOINT ["java", "-Xmx400m", "-Xms200m", "-jar", "app.jar"]

