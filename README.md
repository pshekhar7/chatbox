# Run below commands to start the application

1. After unzipping the file, `cd` to chatbox
2. The chatbox folder contains all project files along with docker related files.
2. To build applicaiton image, run
     `docker build -t chatbox:latest .`
3. To start both the application and the database, run
     `docker compose up`
4. Application is up and running on port 8080.


# APIs
1. Other than the APIs mentioned in the question document, this application also provisions `/send/text/group` API with below sample cURL command and responses.

```curl
curl --location 'http://localhost:8080/send/text/group' \
--header 'Content-Type: application/json' \
--data '{
    "from": "user1",
    "toList": [
        "user2",
        "user3",
        "user4"
    ],
    "text": "hello"
}'
```

Response:
```json
{
    "status": "success"
}
```