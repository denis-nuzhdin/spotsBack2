# Сборочный этап
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Копируем gradle wrapper и зависимости
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# Сборка jar-файла
RUN ./gradlew build --no-daemon

# Production image
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 