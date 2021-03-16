
FROM openjdk:11.0.10-jre-slim-buster AS builder
COPY . .

CMD ["./gradlew", "assemble"]

FROM openjdk:11.0.10-jre-slim-buster
COPY --from=builder /app/build/libs/app.jar .
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
