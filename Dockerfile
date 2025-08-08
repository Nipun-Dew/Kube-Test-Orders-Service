FROM maven:3.9.6-eclipse-temurin-21 AS build

COPY . /app

WORKDIR /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

EXPOSE 8080

COPY --from=build /app/target/*.jar orders.jar

ENTRYPOINT ["java", "-jar", "orders.jar"]
