# Use the official Maven image to create a build artifact
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn package -DskipTests

# Use the official OpenJDK image for the runtime
FROM eclipse-temurin:21-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the Maven build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 2001

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
