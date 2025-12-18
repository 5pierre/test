
# Premier Service

# RentalService - Guide d’installation et lancement

## Partie 1️ : Lancer le projet sans Docker

### 1. Installer Java 21

- Télécharge JDK 21 avec powershell : winget install EclipseAdoptium.Temurin.21.JDK )

### 3. Vérifier la version de Java
powershell
java -version
### 4. Tester

cd C:\Users\plli\Desktop\cours\docker\ingnum\RentalService
.\gradlew build

java -jar build/libs/RentalService-0.0.1-SNAPSHOT.jar  

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


# Deuxième Service

# FirstnameService – Guide d’installation et lancement

## Partie 1 : Création du service PHP

### 1. Créer le dossier FirstnameService

Créer un nouveau dossier à la racine du projet :

```bash
mkdir FirstnameService
cd FirstnameService
````

---

### 2. Création du fichier PHP

Créer un fichier `index.php` qui retourne mon prénom via une requête HTTP GET :

```php
<?php
header("Content-Type: text/plain");
echo "Prénom : Jean";
```

---

## Partie 2 : Lancer le service avec Docker

### 1. Créer le Dockerfile

Dans le dossier `FirstnameService`, créer un fichier nommé `Dockerfile` :

```dockerfile
FROM php:8.2-apache

COPY index.php /var/www/html/index.php

EXPOSE 80
```

---

### 2. Construire l’image Docker

Se placer dans le dossier `FirstnameService` puis construire l’image Docker :

```bash
cd FirstnameService
docker build -t firstnameservice .
```

---

### 3. Tester le service avec Docker

Lancer le conteneur Docker :

```bash
docker run -p 8082:80 firstnameservice
```

Tester le service dans le navigateur à l’adresse suivante :

```
http://localhost:8082
```

Résultat attendu :

```
Prénom : Jean
```

---

### 4. Publier l’image sur Docker Hub

Connexion à Docker Hub :

```bash
docker login
```

Tag de l’image :

```bash
docker tag firstnameservice 5pierre/firstnameservice:latest
```

Publication de l’image :

```bash
docker push 5pierre/firstnameservice:latest
```

