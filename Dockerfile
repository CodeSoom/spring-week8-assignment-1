# 우리 프로젝트를 빌드를 하기 위해서만 사용되는 이미지입니다.
FROM openjdk:11 AS builder
# 현재 폴더에서 컨테이너의 현재 폴더로 복사합니다.
COPY . .
# 명령어를 실행합니다.
CMD ["./gradlew", "assemble"]

# 이 이미지가 우리가 실제로 실행시킬 이미지입니다.
FROM openjdk:11
# 위의 빌드 이미지로부터 만들어진 jar 파일을 복사합니다.
COPY --from=builder /app/build/libs/app.jar .
# 서버를 실행시키는 명령어입니다.
CMD ["java", "-jar", "app.jar"]
