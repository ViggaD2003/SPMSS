FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ARG GEMINI_KEY
ENV GEMINI_KEY=$GEMINI_KEY

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
