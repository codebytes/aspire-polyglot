FROM maven:3.10.0-eclipse-temurin-21 AS build
WORKDIR /src
COPY pom.xml .
COPY src ./src
RUN mvn -q -B -DskipTests package

FROM eclipse-temurin:21.0.12.1_1-jre
WORKDIR /app
COPY --from=build /src/target/leaderboard.jar app.jar
COPY --from=build /src/target/agent/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
COPY entrypoint.sh /app/entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["sh", "/app/entrypoint.sh"]
