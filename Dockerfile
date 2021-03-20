FROM openjdk:15.0.2-slim-buster AS builder
COPY . .

CMD ["./gradlew", "bootJar"]

FROM openjdk:15.0.2-slim-buster
COPY --from=builder /app/build/libs/app.jar .
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
