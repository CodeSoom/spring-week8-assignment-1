FROM openjdk:15 AS builder

COPY . .

CMD ["./gradlew", "app:assemble"]

FROM openjdk:15

COPY --from=builder /app/build/libs/app.jar .

CMD ["java", "-jar", "app.jar"]
