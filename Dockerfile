FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/MediSched-0.0.1-SNAPSHOT.jar /app/MediSched.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "MediSched.jar"]