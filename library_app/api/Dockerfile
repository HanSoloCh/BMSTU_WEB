# ---------- СБОРКА ----------
FROM gradle:8.6-jdk17 AS build

WORKDIR /app

# 1. Сначала копируем только то, что нужно для настройки репозиториев
COPY settings.gradle.kts ./
COPY build.gradle.kts ./
COPY gradle gradle

# 2. Копируем код после — это важно для кеширования
COPY ./api ./api
COPY ./data ./data
COPY ./domain ./domain

# 3. Указываем, что можно использовать репозитории проекта
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx1g"

# 4. Собираем модуль api
RUN gradle :api:clean :api:shadowJ -x test --no-daemon --stacktrace

# ---------- РАНТАЙМ ----------
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app
COPY --from=build /app/api/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
