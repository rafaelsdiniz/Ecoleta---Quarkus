# Etapa 1: Build do projeto usando Maven
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências antes de copiar o código-fonte
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o restante do código e constrói o projeto
COPY src ./src
RUN mvn package -DskipTests

# Etapa 2: Imagem final para rodar a aplicação
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copia o .jar gerado da etapa anterior
COPY --from=build /app/target/quarkus-app/lib/ /app/lib/
COPY --from=build /app/target/quarkus-app/*.jar /app/
COPY --from=build /app/target/quarkus-app/app/ /app/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /app/quarkus/

EXPOSE 8080
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

ENTRYPOINT ["java", "-jar", "/app/quarkus-run.jar"]
