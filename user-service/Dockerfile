FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY user-service/.mvn .mvn
COPY user-service/mvnw mvnw
COPY user-service/pom.xml pom.xml
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B \
    -Dmaven.artifact.threads=2 \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.timeout=60000 \
    -Dmaven.wagon.http.readTimeout=60000 \
    dependency:go-offline
COPY common /workspace/common
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B \
    -f /workspace/common/pom.xml \
    clean install \
    -DskipTests
COPY user-service/src /workspace/src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B \
    -Dmaven.artifact.threads=2 \
    -Dmaven.wagon.http.retryHandler.count=5 \
    -Dmaven.wagon.http.timeout=60000 \
    -Dmaven.wagon.http.readTimeout=60000 \
    clean package \
    -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/target/user-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]