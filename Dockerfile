FROM gradle:jdk17-alpine as builder
RUN apk add --update --no-cache nodejs npm
WORKDIR /build

COPY build.gradle /build/
COPY settings.gradle /build/
COPY . /build

RUN gradle build -x test

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/build/build/libs/nest-0.0.1-SNAPSHOT.jar"]
