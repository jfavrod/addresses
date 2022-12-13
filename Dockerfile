FROM openjdk:11-slim
EXPOSE 8080
WORKDIR /app

COPY target/addresses.jar /app/addresses.jar

CMD ["java", "-jar", "/app/addresses.jar"]