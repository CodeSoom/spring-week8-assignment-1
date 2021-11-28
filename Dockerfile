FROM openjdk:15
COPY /app/build/libs/app.jar app.jar

CMD ["java", "-jar", "app.jar"]
EXPOSE 8080
