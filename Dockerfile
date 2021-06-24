FROM openjdk:11-jre-slim-buster AS builder

COPY . .

CMD ["./gradlew", "app:assemble"]

FROM openjdk:11-jre-slim-buster

COPY --from=builder /app/build/libs/app.jar .

CMD ["java", "-jar", "-Dspring.profiles.active=stage", "app.jar"]
