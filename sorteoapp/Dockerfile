# -----------------------
# Etapa 1: Compilación
# -----------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app/sorteoapp
COPY ./sorteoapp ./
RUN mvn clean package -DskipTests

# -----------------------
# Etapa 2: Ejecución
# -----------------------
FROM eclipse-temurin:21

WORKDIR /app
COPY --from=build /app/sorteoapp/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
