FROM maven:3.6-amazoncorretto-11 as build

COPY . /app/

RUN cd /app && mvn clean install

FROM openjdk:11-jre-slim

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]
