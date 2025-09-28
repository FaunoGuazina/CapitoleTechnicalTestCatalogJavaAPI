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
COPY --from=build /workspace/target/*-SNAPSHOT.jar /app/app.jar
RUN chown -R appuser:appuser /app
USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]