# Етап 1: Збірка проекту за допомогою Gradle
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Копіюємо файли конфігурації Gradle та сам wrapper
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Даємо права на виконання скрипта
# (ти, як користувач Arch Linux, чудово знаєш навіщо тут chmod +x)
RUN chmod +x ./gradlew

# Копіюємо вихідний код
COPY src ./src

# Збираємо jar-файл (пропускаючи тести для швидкості)
RUN ./gradlew clean bootJar --no-daemon

# Етап 2: Запуск готового jar-файлу (чистий і легкий рантайм)
FROM eclipse-temurin:17-jre
WORKDIR /app
# Зверни увагу: у Gradle готовий файл лежить у build/libs/
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]