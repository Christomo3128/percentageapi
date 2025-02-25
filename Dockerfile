# Imagen base de Java 21
FROM openjdk:21-jdk-slim

# Configurar directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR al contenedor
COPY build/libs/percentageapi-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación
EXPOSE 8083

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]