FROM eclipse-temurin:17.0.8_7-jdk-alpine
#docker pull
ARG JAR_FILE=/target/media-holder-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]