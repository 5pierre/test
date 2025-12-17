

winget install EclipseAdoptium.Temurin.21.JDK

./gradlew build

docker build -t rentalservice .

docker run -p 8000:8080 rentalservice

docker images

docker tag rentalservice 5pierre/rentalservice:v1

docker run


