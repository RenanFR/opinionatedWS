# Use the official maven/Java 8 image to create a build artifact.
# https://hub.docker.com/_/maven
FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.
WORKDIR /opinionatedWS
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests
RUN apk update && apk --no-cache add curl
# Use AdoptOpenJDK for base image.
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk8:jdk8u202-b08-alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /opinionatedWS/target/opinionatedWS-*.jar /opinionatedWS.jar
# Run the web service on container startup.
CMD ["curl.sh"]
CMD ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=develop","-Dserver.port=${PORT}","-jar","/opinionatedWS.jar"]