# Etapa 1: Build do projeto usando Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

COPY --from=build /app/target/quarkus-app/ ./

EXPOSE 8080
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

CMD ["java", "-jar", "quarkus-run.jar"]
