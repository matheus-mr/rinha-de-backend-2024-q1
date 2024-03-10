FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /workspace/build

COPY . .

RUN --mount=type=cache,target=/root/.gradle chmod +x ./gradlew && ./gradlew bootBuildImage

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY --from=build /workspace/build/build/libs/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
