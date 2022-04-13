FROM openjdk:17
EXPOSE 8081 15772 5672 5772
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .

COPY ./src ./src
COPY ./pom.xml ./pom.xml

RUN chmod 755 /app/mvnw

RUN ./mvnw package -DskipTests

ENTRYPOINT ["java","-jar","target/history-service.jar"]