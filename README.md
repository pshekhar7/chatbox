# Run below commands to start the application

1. After unzipping the file, `cd` to chatbox
2. The chatbox folder contains all project files along with docker related files.
3. To build applicaiton image, run
     `docker build -t chatbox:latest .`
4. To start both the application and the database, run
     `docker compose up`
5. Application is up and running on port 8080.


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

# Token based Authorization

1. The application supports token based authorization option.
2. This is driven by setting the flag `ENABLE_TOKEN_SECURITY` in docker compose file under application's environment.
3. By doing so, the login response returns a `sessionToken` response. 
4. Going forward, this token is required to be passed in header under `x-session-token` to successfully access resources.