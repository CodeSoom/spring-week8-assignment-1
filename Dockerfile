FROM openjdk:15

ENV APP_HOME=/home/app

WORKDIR $APP_HOME

COPY app/build/libs/app.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]