ARG BUILD_IMAGE=public.ecr.aws/docker/library/maven:3.9-amazoncorretto-25
FROM ${BUILD_IMAGE} AS builder

WORKDIR /workspace

# Copy pom only first so dependencies layer can be cached
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy sources and build the project
COPY src ./src
RUN mvn clean package -DskipTests -B

###############################################
# 2. Runtime stage — Amazon Corretto 25 runtime
###############################################
FROM public.ecr.aws/amazoncorretto/amazoncorretto:25

WORKDIR /app

# Copy the built jar from builder stage
COPY --from=builder /workspace/target/*.jar ./app.jar

ENTRYPOINT ["java", "-Xms512m", "-Xmx1400m", "-jar", "/app/app.jar"]