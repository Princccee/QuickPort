# Step 1: Use JDK image to run Spring Boot
FROM eclipse-temurin:17-jdk-jammy

# Step 2: Add a volume for temp files
VOLUME /tmp

# Step 3: Copy the JAR built from Gradle
COPY build/libs/deliveryapp-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Run the JAR
ENTRYPOINT ["java","-jar","/app.jar"]