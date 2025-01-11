FROM openjdk:17-oracle
COPY ./target/*.jar app.jar
COPY src/main/resources /app/resources
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
