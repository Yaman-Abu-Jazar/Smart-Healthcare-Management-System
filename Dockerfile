FROM openjdk:17-jdk-alpine
LABEL maintainer=exalt.com
WORKDIR /app
EXPOSE 8080
COPY target/healthcare-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]