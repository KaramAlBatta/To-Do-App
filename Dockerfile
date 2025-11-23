FROM eclipse-temurin:17-jre

WORKDIR /app

COPY . .

# Jetzt das Flag im Container setzen:
RUN chmod +x gradlew

RUN ./gradlew build -x test

EXPOSE 8080

CMD ["java", "-jar", "build/libs/webtech-0.0.1-SNAPSHOT.jar"]
