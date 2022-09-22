FROM openjdk:17-alpine
COPY ./target/SENG4500-A2-1.0-SNAPSHOT.jar /usr/local/lib/A1.jar
EXPOSE 5000