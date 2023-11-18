FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

# Copy the application source code to the container
COPY . /app

# Build the application
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the build stage to the runtime `image
COPY --from=builder /app/target/*.jar /app/chatbox.jar

# Expose port 8080 for the Spring Boot application
EXPOSE 8080

CMD ["java", "-jar", "/app/chatbox.jar"]