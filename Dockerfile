# Etapa 1: Build do projeto usando Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e constrói o projeto
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia toda a estrutura fast-jar
COPY --from=build /app/target/quarkus-app/ ./

# Porta padrão Quarkus
EXPOSE 8080

# Host para funcionar no Railway
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

# Comando correto para fast-jar
CMD ["java", "-jar", "quarkus-run.jar"]
