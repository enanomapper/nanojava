FROM maven:3-jdk-8-alpine as maven

WORKDIR /tmp

COPY pom.xml .

RUN mvn verify clean --fail-never

COPY src/ ./src/

RUN mvn package -DskipTests=true 


FROM openjdk:8-jre-alpine

LABEL maintainer "Ammar Ammar <ammar257ammar@gmail.com>"

RUN apk update && apk add bash

COPY --from=maven /tmp/target/nanojava-1.2.0-SNAPSHOT-jar-with-dependencies.jar /app/nanojava.jar

ENTRYPOINT ["java","-jar","/app/nanojava.jar"]
CMD ["-h"]
