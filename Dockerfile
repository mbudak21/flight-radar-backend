FROM maven:3.9-eclipse-temurin-21-alpine AS Builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=Builder /app/target/*.jar /app/application.jar
CMD ["java", "-jar", "application.jar"]