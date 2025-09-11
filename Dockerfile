# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# ---- Runtime Stage ----
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Configure timezone
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Install Python and pip
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        python3 \
        python3-pip \
        python3-venv \
        netcat-openbsd \
        ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Create Python virtual environment and install MCP time server
RUN python3 -m venv /opt/mcp-env && \
    /opt/mcp-env/bin/pip install --no-cache-dir --upgrade pip && \
    /opt/mcp-env/bin/pip install --no-cache-dir mcp-server-time

# Add the virtual environment to PATH
ENV PATH="/opt/mcp-env/bin:$PATH"

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Copy MCP config file
COPY src/main/resources/mcp-servers.json /app/mcp-servers.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
