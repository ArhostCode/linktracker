FROM openjdk:21
WORKDIR /app
COPY target/bot.jar /app/bot.jar
CMD ["java", "-jar", "bot.jar"]
