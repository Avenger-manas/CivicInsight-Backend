# ===========================
# Stage 1: Build the JAR
# ===========================
FROM eclipse-temurin:17-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# 1️⃣ Copy Maven wrapper and pom.xml first
COPY mvnw .
COPY .mvn/ .mvn
COPY pom.xml .

# 2️⃣ Give executable permission to mvnw
RUN chmod +x mvnw

# 3️⃣ Download dependencies (cache optimization)
RUN ./mvnw dependency:go-offline -B

# 4️⃣ Copy the rest of the project source code
COPY src src

# 5️⃣ Build the JAR (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# ===========================
# Stage 2: Run the JAR
# ===========================
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# 1️⃣ Copy the JAR file from the build stage
COPY --from=builder /app/target/*.jar app.jar

# 2️⃣ Expose application port
EXPOSE 8080

# 3️⃣ Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]