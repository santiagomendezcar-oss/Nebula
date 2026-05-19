# Dockerfile Multi-stage optimizado
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Imagen final - Usar Eclipse Temurin (oficial)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar JAR
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Usar variable de puerto de Render
ENV PORT=8080

# Comando de entrada
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT} app.jar"]