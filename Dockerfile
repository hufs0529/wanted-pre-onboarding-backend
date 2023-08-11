# 빌드 스테이지
FROM maven:3.8.4 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package

# 실행 스테이지
FROM khipu/openjdk17-alpine

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
VOLUME /tmp
