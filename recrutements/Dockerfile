# Utiliser l'image de base Java (version compatible avec votre application)
FROM openjdk:11-jdk-slim as build

# Indiquer le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar compilé dans le conteneur
COPY target/recrutements-0.0.1-SNAPSHOT.jar recrutement-service.jar

# Exécuter l'application
ENTRYPOINT ["java","-jar","/app/recrutement-service.jar"]
