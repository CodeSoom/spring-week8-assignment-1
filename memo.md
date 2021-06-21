
// build.gradle : 아스키독 데펜더시
asciidoctor {
    dependsOn(test)
}



// 아스키독 생성
./gradlew asciidoc

// 기존 아스키독 삭제
./gradlew clean asciidoc