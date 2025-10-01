# Stage 1: build con Maven y JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copiamos pom.xml primero para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Ahora copiamos el código fuente
COPY src ./src

# Compilamos el proyecto (skip tests para acelerar)
RUN mvn -B clean package -DskipTests -Dmaven.test.skip=true

# Stage 2: runtime con JDK 21
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]