### Instruction to Run Application
Step 1: Build the application
From the project root (where your pom.xml is): mvn clean package

Step 2: Run the application: mvn spring-boot:run

Step 3: test api using this curl :
curl --location 'http://localhost:8080/api/initiate-session' \
--header 'Content-Type: application/json' \
--data '{
"stationId": "123e4567-e89b-12d3-a456-426614174000",
"driverTokenId": "validDriverToken123456",
"callBackURL": "http://localhost:8080/callback"
}'

Step 4: Access the DB at: http://localhost:8080/h2-console
add JDBC URL: jdbc:h2:mem:testdb

#### Another way :

Step 1 :Open IntelliJ IDEA.

Step 2 :Open your project folder (the one containing pom.xml).

Step 3 :IntelliJ will auto-detect it's a Maven project and start indexing.

Step 4 :Run the Application : From the Main Class
In the src/main/java/... folder, locate the class with main method

Step 5 :Right-click on this class → Run 'YourAppName.main()'

Step 6: test api using this curl :
curl --location 'http://localhost:8080/api/initiate-session' \
--header 'Content-Type: application/json' \
--data '{
"stationId": "123e4567-e89b-12d3-a456-426614174000",
"driverTokenId": "validDriverToken123456",
"callBackURL": "http://localhost:8080/callback"
}'

Step 7: Access the DB at: http://localhost:8080/h2-console
add JDBC URL: jdbc:h2:mem:testdb

### Problem Summary
A driver initiates a charging session at a station.

The API immediately acknowledges the request and asynchronously processes authorization using an internal service.

The result of this authorization is sent to the client's callback URL.

If the internal service does not respond in time, the result is marked as unknown.

I used BlockingQueue to simulate asynchronous messaging between the API and the internal authorization service.


### Key Components

#### 1. API Endpoint: /initiate-session
* Accepts a SessionRequest JSON payload:

  {
  "station_id": "UUID",
  "driver_token": "driverToken123",
  "callback_url": "http://localhost:8080/callback"
  }
* Validates input: UUID, driver token (20–80 chars with allowed characters), and a valid HTTP/HTTPS URL.
* Returns an immediate SessionResponse:

  {
  "status": "accepted",
  "message": "Request is being processed asynchronously. The result will be sent to the provided callback URL."
  }
* Asynchronously adds the request to a BlockingQueue for background processing.

#### 2. BlockingQueue Implementation:
* I used a LinkedBlockingQueue<SessionRequest>.

* A background worker thread continuously takes items from the queue and processes them.
* As enhancement, I can add Kafka for asynchronous communication

#### 3. Authorization Service:
* The Authorization Service is responsible for processing driver charging session requests and determining whether they are authorized to proceed with charging.
* Here I have made assumption that we will be verifying if customer is allowed or not allowed by checking prefix of driverTokenId

#### 4. Data Persistence
* Stored decision results in an H2 in-memory database using Spring Data JPA.

* This allows us to debug and retrieve past decisions if needed.

#### 5. callback API :
The /callback endpoint is used to receive the result of an asynchronous authorization decision for a charging session.

**Purpose:**
When a charging session request is initiated by a driver (via /initiate-session), the server processes it asynchronously. Once the decision (e.g., allowed or denied) is made, the result is posted to the callback URL that the client had provided.
To test this mechanism during development, I implement /callback in same application.

**Reason for adding :**

Simulates a real client system that will receive the decision.

Helps verify end-to-end flow from request → async processing → response delivery.

Logs are added to confirm the payload and ensure it's routed correctly.

### Technologies Used

* Java 17

* Spring Boot

* Spring Web

* Spring Data JPA

* H2 Database

* Lombok

* BlockingQueue for asynchronous simulation
