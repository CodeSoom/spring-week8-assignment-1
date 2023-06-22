FROM openjdk:11
ARG JAR_FILE_PATH=app/build/libs/app.jar
COPY ${JAR_FILE_PATH} app.jar
ENV ACTIVE_PROFILE default
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "app.jar"]
