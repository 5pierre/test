
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

## Partie 3 : Communication entre microservices avec Docker Compose

### 1. Architecture de l'application

L'application est composée de deux microservices communicant via HTTP :
- **rental-service** : Service Java (Spring Boot) sur le port 8080
- **firstname-service** : Service PHP (Apache) sur le port 80
- **microservices-network** : Réseau Docker Compose permettant la communication entre les services

### 2. Fichier Docker Compose

Créer un fichier `docker-compose.yaml` à la racine du projet avec 2 services et 1 réseau :

```yaml
version: '3.8'

services:
  firstname-service:
    build:
      context: ./firstname-service
      dockerfile: Dockerfile
    container_name: firstname-service
    ports:
      - "8081:80"
    networks:
      - microservices-network
    restart: unless-stopped

  rental-service:
    build:
      context: ./rental-service
      dockerfile: Dockerfile
    container_name: rental-service
    ports:
      - "8080:8080"
    networks:
      - microservices-network
    depends_on:
      - firstname-service
    restart: unless-stopped

networks:
  microservices-network:
    driver: bridge
```

### 3. Modifications du code Java et Configuration de l'URL du service PHP


Dans `rental-service/src/main/resources/application.properties` :

```properties
server.port=8080
spring.application.name=RentalService
firstname.service.url=http://firstname-service/firstname
```



### 4. Service PHP avec endpoint /firstname

Mise à jour de `firstname-service/index.php` :

```php
<?php

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

if ($path === '/firstname' || $path === '/firstname/') {
    header('Content-Type: application/json');
    echo json_encode(['firstname' => 'Jean']);
    exit;
}

header('Content-Type: text/plain');
echo "firstname-service is running";
```

### 5. Lancement et test avec Docker Compose

#### 5.1. Construire et démarrer les conteneurs

```bash
cd c:\Users\pllie\OneDrive\Documents\Efrei_2023-2025\Cours-L3\docker\exo docker\ingnum

# Construire le JAR de rental-service
cd rental-service
.\gradlew build
cd ..

# Lancer Docker Compose
docker-compose up --build
```

#### 5.2. Tester avec le navigateur

- **Service rental-service** : http://localhost:8080/bonjour
  - Résultat attendu : `bonjour Jean`
  
- **Service firstname-service** (direct) : http://localhost:8081
  - Résultat attendu : `firstname-service is running`
  
- **Service firstname-service** (endpoint) : http://localhost:8081/firstname
  - Résultat attendu : `{"firstname":"Jean"}`

### 6. Arrêter les conteneurs

```bash
docker-compose down

# Supprimer aussi les volumes si nécessaire
docker-compose down -v
```

### 7. Images Docker Hub

- **rental-service** : [5pierre/rentalservice:v1](https://hub.docker.com/r/5pierre/rentalservice)
- **firstname-service** : [5pierre/firstnameservice:latest](https://hub.docker.com/r/5pierre/firstnameservice)


