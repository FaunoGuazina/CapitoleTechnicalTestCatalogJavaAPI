# -------- build --------
FROM public.ecr.aws/docker/library/maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY .mvn/ .mvn
RUN mvn -B -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -q -DskipTests clean package

# -------- runtime --------
FROM public.ecr.aws/docker/library/eclipse-temurin:21-jre
WORKDIR /app
RUN useradd -ms /bin/bash appuser
USER appuser
COPY --from=build /workspace/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]