FROM openjdk:21
WORKDIR /app
COPY target/scrapper.jar /app/scrapper.jar
CMD ["java", "-jar", "scrapper.jar"]
