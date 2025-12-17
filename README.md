
# RentalService - Guide d’installation et lancement

## Partie 1️ : Lancer le projet sans Docker

### 1. Installer Java 21

- Télécharge JDK 21 avec powershell : winget install EclipseAdoptium.Temurin.21.JDK )

### 3. Vérifier la version de Java
powershell
java -version
### 4. Tester

cd C:\Users\marin\Desktop\cours\docker\ingnum\RentalService
.\gradlew build
dir build\libs
& "C:\Program Files\Eclipse Adoptium\jdk-21\bin\java.exe" -jar build\libs\RentalService-0.0.1-SNAPSHOT.jar
- tester dans le navigateur : http://localhost:8080/bonjour

## Partie 2: Lancer le projet avec Docker

### 1. Créer le Dockerfile

- Dans le dossier `RentalService`, crée un fichier nommé `Dockerfile` :
dockerfile
FROM eclipse-temurin:21-jre-jammy

VOLUME /tmp

EXPOSE 8080

ADD ./build/libs/RentalService-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
### 2. Tester le programme avec Docker

docker build -t rentalservice .
- Veuiller arrêter le serveur précédent et lancer l'application avec Docker :
  
  docker run -p 8080:8080 rentalservice
  
  Regerder dans le navigateur : http://localhost:8080/bonjour

### 3. Construire l'image avec Docker

docker login
docker images
docker tag rentalservice 5pierre/rentalservice:v1
docker push 5pierre/rentalservice:v1
