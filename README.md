# Spring Boot 3 application with Kafka

This project demonstrates a Spring Boot application with Kafka integration. It consists of two modules:
- `parent-pom`: Contains the parent POM that manages dependency versions
- `dispatch-service`: Contains the Spring Boot application that integrates with Kafka

## Building and Running the Application

### Step 1: Build the Maven modules
```bash
# Build the parent-pom module
cd parent-pom
./mvnw clean install

# Build the dispatch-service module
cd ../dispatch-service
./mvnw clean package
```
ZZZ
### Step 2: Build and run Docker services
```bash
# Return to the project root
cd ..

# Build Docker images and start services
docker compose build
docker compose up -d
```

### Step 3: Stop the services
```bash
docker compose down
```

## Project Structure

- `parent-pom/`: Parent POM module that manages dependency versions
- `dispatch-service/`: Spring Boot application that integrates with Kafka
- `compose.yaml`: Docker Compose configuration for running the application with Kafka

## Health Verification

After starting the services, you can verify the health of the application by checking:
- Kafka UI: http://localhost:8081
- Dispatch Service: http://localhost:8080/actuator/health
