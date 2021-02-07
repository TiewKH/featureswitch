# Moneylion Technical Assessment

### Curl commands for Windows
To check whether a user is enabled for a certain feature
```
curl "localhost:8080/feature?email=tiewkeehui95@hotmail.com&featureName=CREATE%20USER"
```

To add a new permission for a user-feature pair
```
curl -X POST -H "Content-Type: application/json" -d "{\"featureName\": \"CREATE USER\", \"email\": \"tiewkeehui95@hotmail.com\", \"enabled\": false}" "localhost:8080/feature" -i
```