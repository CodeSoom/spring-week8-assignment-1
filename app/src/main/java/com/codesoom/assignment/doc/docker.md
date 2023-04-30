## 도커를 이용하여 배포하기

- openjdk 이미지 받기


docker pull openjdk:{version} //amd
docker pull arm64v8/openjdk:{version} // arm(m1)


- DockerFile 만들기

```Docker
FROM arm64v8/openjdk:15
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```


- Docker Build
```agsl
docker build -t kkad45/java{dockerhub ID }/{repository}:tag .{Docker 파일 위치} - f DockerFile

-f -file (특정 파일 위치를 가르킨다.  m1에서 도커파일이 있는 위치에서 빌드를 시도했는데 파일을 읽지 못해 해당 옵션 추가)
```
