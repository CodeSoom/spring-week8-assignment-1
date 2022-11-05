FROM openjdk:15

CMD ["./gradlew", "clean", "bootJar"]

ARG JAR_FILE=app/build/libs/app-2022.11.jar

COPY ${JAR_FILE} shop.jar

ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-jar","/shop.jar"]
