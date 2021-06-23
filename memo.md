
// build.gradle : 아스키독 데펜더시
asciidoctor {
    dependsOn(test)
}



// 아스키독 생성
./gradlew asciidoc

// 기존 아스키독 삭제
./gradlew clean asciidoc



./gradlew bootRun -> local tap 새로 만들어서 http localhost:8080


./gradlew assemble
-> 뭘 모아준다.
-> $ java -jar app/build/libs/app.jar
-> 실행하게 됨 -> idea . 없이도 서버가 구동되고, build.gradle 의존 없이도 서버실행이 된다.(의존관계를 하나씩 줄여가는 과정)

-> 문제: 자바 버전도 맞춰줘야하고,여러 환경에 대한 고려를 해줘야한다.
-> 최근에는 컨테이너 기술을 도입한다: docker
-> 컨테이너 단위로 실행
-> 리눅스 위에 도커를 올려 실행



docker ps : ls 느낌

하나의 이미진지는 여러개의 레잉어로 이루어져있다.


docker rm id번호 -> 삭제

java를 받아옴
% docker pull openjdk:15


% docker run -it --rm --name api-server -v $(pwd)/app/build/libs:/home/api-server -p 80:8080 openjdk:11 bash 
-- bash 라고 써준것은 안에서 실행할 명령어의 자리 -> 


껐다 키면 사라짐(h2 db는)
imseonghu@imseonghuui-MacBookPro spring-week8-assignment-1 % docker run -it --rm --name api-server -v $(pwd)/app/build/libs:/home/api-server -p 80:8080 openjdk:11 bash -c "java -jar /home/api-server/app.jar"


-d : background


docker run -d --name mariadb --network codesoom -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root1234 -e MYSQL_DATABASE=test mariadb --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci


db name을 test 로 지정함.


```` java
// 명명 규칙 인지하
implementation 'org.mariadb.jdbc:mariadb-java-client:2.7.2'

```
