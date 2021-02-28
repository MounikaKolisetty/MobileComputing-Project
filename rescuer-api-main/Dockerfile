# Install maven for downloading dependencies
FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn clean install -Dmaven.test.skip=true

# install JRE
FROM openjdk:11.0.4-jre-slim
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/rescuer-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java", "-jar", "rescuer-0.0.1-SNAPSHOT.jar"]