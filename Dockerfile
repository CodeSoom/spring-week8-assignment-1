# syntax=docker/dockerfile:1
FROM openjdk:11 AS builder

COPY  . .

RUN ["./gradlew", "assemble"]

FROM openjdk:11

COPY --from=builder /app/build/libs/app.jar .

ENV SPRING_PROFILES_ACTIVE=mariadb
CMD ["java","-jar","app.jar"]
