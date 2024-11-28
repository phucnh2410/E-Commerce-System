#Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn/ .mvn
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests


#Stage 2: Run the application
FROM openjdk:21-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/target/E-Commerce-System-0.0.1-SNAPSHOT.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]

#docker buildx build --platform linux/amd64 -t phucngohuynhdocker/sherlock-store:1.0 .
#docker-compose up
#docker push phucngohuynhdocker/sherlock-store:1.0
