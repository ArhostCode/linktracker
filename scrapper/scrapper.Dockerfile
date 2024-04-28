FROM openjdk:21
WORKDIR /app
COPY target/scrapper.jar /app/scrapper.jar
EXPOSE 8080
EXPOSE 8000
CMD ["java", "-jar", "scrapper.jar"]
