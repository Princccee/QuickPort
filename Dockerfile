# ---------- Build stage ----------
FROM gradle:8.10.1-jdk17-jammy AS builder
WORKDIR /workspace

# Cache dependencies first (speed up rebuilds)
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew gradlew
RUN chmod +x gradlew
RUN ./gradlew dependencies || true

# Now copy the rest and build
COPY . .
RUN ./gradlew clean bootJar -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the fat jar from builder
COPY --from=builder /workspace/build/libs/*-SNAPSHOT.jar app.jar

# Tunables (optional)
ENV JAVA_OPTS=""

# Expose port (documentational)
EXPOSE 8080

# Run with a little heap friendliness for containers
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
