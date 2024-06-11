
## 베이스 이미지로 Java 17버전이 포함된 Docker 이미지를 사용
FROM bellsoft/liberica-openjdk-alpine:17

## Gradle을 사용해 빌드를 실행하는 명령어
CMD ["./gradlew", "clean", "build"]

VOLUME /tmp

## Gradle로 빌드한 jar 파일의 위치를 변수로 설정
ARG JAR_FILE=build/libs/*.jar

## JAR_FILE 변수에 지정된 파일을 app.jar라는 이름으로 컨테이너에 추가
COPY ${JAR_FILE} app.jar

## 컨테이너가 사용할 포트를 설정, 이 경우에는 8080 포트를 사용
EXPOSE 8080

## 컨테이너가 실행될 때 기본적으로 실행될 명령어를 설정
ENTRYPOINT ["java","-jar","/app.jar"]