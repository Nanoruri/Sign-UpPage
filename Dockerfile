#Dockerfile
# Use an official OpenJDK runtime as a parent image
FROM  arm64v8/openjdk:11-jre-slim

LABEL maintainer="NanoRuri"

# Set the working directory in the container
WORKDIR /app

# Define a build-time variable for the JAR file
ARG JAR_FILE

# Copy the JAR file from the host to the container
COPY ${JAR_FILE} /app/springstudy.jar

# Copy the application.yml file from the host to the container
COPY springstudy/target/application.yml /app/application.yml

# Expose the port that the application will run on
EXPOSE 8082

# Run the JAR file with the application.yml configuration
ENTRYPOINT ["java", "-jar", "/app/springstudy.jar", "--spring.config.location=/app/application.yml"]