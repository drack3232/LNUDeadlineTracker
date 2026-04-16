# Етап 1: Збірка проекту за допомогою Gradle (Kotlin DSL)
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Копіюємо файли конфігурації
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x ./gradlew
COPY src ./src

# Збираємо jar-файл
RUN ./gradlew clean bootJar --no-daemon

# Етап 2: Запуск
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]