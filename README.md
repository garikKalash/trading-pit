# trading-pit

Microservice is created to support client/conversion APIs and simulate requests to external services doing retryable actions.

Tech-stack
- Java 17
- Postgres
- Flyway
- Swagger 3
- Spring boot
- Junit

Deployment
- Heroku
- Github


## Implementation notices
-  Since we received API calls directly from the callers `userAgent` and `ip` properties I removed from the payload and catch them internally from the request and headers param. IDK how much you like it guys but it decreases the payload size and helps us to avoid of manual setup of `IP` property