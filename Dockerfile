FROM gradle:jdk17-alpine as builder
RUN apk add --update --no-cache nodejs npm fontconfig libfreetype6
WORKDIR /build

COPY build.gradle /build/
COPY settings.gradle /build/
COPY . /build

RUN gradle build -x test

FROM openjdk:17-jdk-alpine as runner
COPY --from=builder /build/build/libs/nest-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
