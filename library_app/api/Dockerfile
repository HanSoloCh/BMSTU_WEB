FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

# Копируем локально собранный jar
COPY ./api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
