# # Use a Java 21 base image
# FROM openjdk:21-jdk-slim

# # Set working directory inside the container
# WORKDIR /app

# # Copy the built Spring Boot JAR file
# COPY target/EcommerceCRUD-1.0.jar app.jar

# # Expose the port the app runs on
# EXPOSE 8080

# # Run the jar file
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Step 1: Build the Java app using Maven
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set working directory inside the container
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project and skip tests to speed up
RUN mvn clean package -DskipTests

# Step 2: Run the app using OpenJDK
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port (change if your app uses a different one)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]

