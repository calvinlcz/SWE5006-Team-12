# Use OpenJDK 17 as the base image (JHipster supports Java 17)
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR from the Maven target directory
COPY target/*.jar app.jar

# Expose the application port (default is 8080)
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-Dspring.profiles.active=prod", "-Dserver.address=0.0.0.0", "-jar", "app.jar"]