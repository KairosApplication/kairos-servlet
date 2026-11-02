FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN mvn -B package

FROM tomcat:11.0-jdk21-temurin
ENV TZ=America/Sao_Paulo JAVA_OPTS="-Duser.timezone=America/Sao_Paulo"
COPY --from=build /app/target/KairosApp-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
