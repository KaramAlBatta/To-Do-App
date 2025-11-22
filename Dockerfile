# Basis-Image mit Java (z.B. für Spring Boot)
FROM eclipse-temurin:17-jre

# Arbeitsverzeichnis setzen
WORKDIR /app

# Kopiere die Gradle-Files und das Quellverzeichnis
COPY . .

# Baue das Projekt als JAR (Produktiv-JAR wird in build/libs/ erzeugt)
RUN ./gradlew build -x test

# Exponiere den Standard-Port (Spring Boot: 8080)
EXPOSE 8080

# Starte die Anwendung
CMD ["java", "-jar", "build/libs/webtech-0.0.1-SNAPSHOT.jar"]
