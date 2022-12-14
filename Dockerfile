FROM openjdk:11-slim as builder
WORKDIR /build

COPY .mvn /build/.mvn
COPY src /build/src
COPY mvnw /build/mvnw
COPY pom.xml /build/pom.xml

RUN ./mvnw install -DskipTests


FROM openjdk:11-slim
WORKDIR /app
EXPOSE 8080
COPY --from=builder /build/target/addresses.jar /app/addresses.jar

CMD ["java", "-jar", "/app/addresses.jar"]

