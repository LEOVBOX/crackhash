# Используем базовый образ с Java (например, OpenJDK 17)
FROM eclipse-temurin:17-jdk-jammy

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл приложения в контейнер
COPY build/libs/crackhash-worker-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт, на котором работает приложение (например, 8080)
EXPOSE 8090

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]