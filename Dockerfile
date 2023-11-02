FROM gradle:jdk17-alpine as builder
RUN apk add --update --no-cache nodejs npm
WORKDIR /build

COPY build.gradle /build/
COPY  settings.gradle /build/

COPY . /build
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

FROM openjdk:17-jdk-alpine as runner
COPY --from=builder /build/build/libs/nest-0.0.1-SNAPSHOT.jar app.jar

RUN pwd
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]