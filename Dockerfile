# Dockerfile Multi-stage optimizado
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Configurar codificación UTF-8
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Forzar codificación UTF-8 en Maven
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT} app.jar"]