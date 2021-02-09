# Moneylion Technical Assessment

## Usage
- To run Spring Boot application
```
mvn clean spring-boot:run
```

- To run unit tests
```
mvn clean test
```

## Curl commands for Windows
- To check whether a user has access to a certain feature
```
curl "localhost:8080/feature?email=tiewkeehui95@hotmail.com&featureName=CREATE%20USER"
```

- To add a new permission for a user-feature pair
```
curl -X POST -H "Content-Type: application/json" -d "{\"featureName\": \"CREATE USER\", \"email\": \"tiewkeehui95@hotmail.com\", \"enabled\": false}" "localhost:8080/feature" -i
```