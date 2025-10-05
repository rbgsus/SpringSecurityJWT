# -----------------------
# Etapa 1: Compilación
# -----------------------
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app/sorteoapp

# Copia solo la carpeta del proyecto (donde está el pom.xml)
COPY ./sorteoapp ./

# Ejecuta Maven para compilar y empaquetar el proyecto
RUN mvn clean package -DskipTests

# -----------------------
# Etapa 2: Ejecución
# -----------------------
FROM eclipse-temurin:21

# Directorio de trabajo para ejecutar el .jar
WORKDIR /app

# Copia el .jar compilado desde la etapa de build
COPY --from=build /app/sorteoapp/target/*.jar app.jar

# Expone el puerto 8080 (Render lo necesita)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
