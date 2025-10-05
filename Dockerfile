# Etapa de compilación
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app/sorteoapp
COPY ./sorteoapp ./   # Copia solo la carpeta del proyecto
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/sorteoapp/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
