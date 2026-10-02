# RideLink - Backend Microservices for a Ride-Sharing Platform

**Module:** IT3130 - Application Development  
**Assessment:** Group Assignment (30%)  

---

## 1. System Architecture & Service Decomposition

RideLink is designed as a modular, decentralized microservices backend for an on-demand ride-hailing system. Each microservice is independently deployable and strictly maintains its own database boundary using dedicated H2 file databases.

```
+---------------------------------------------------------------------------------------+
|                                    RideLink System                                    |
+---------------------------------------------------------------------------------------+

  [ Postman / Swagger UI / Client ]
                 |
                 +-------------------> Account Service (Port 8081)
                 |                     - Own DB: accountdb (H2)
                 |                     - JWT Issuance, Auth, Roles, Status
                 |
                 +-------------------> Driver & Vehicle Service (Port 8082)
                 |                     - Own DB: driverdb (H2)
                 |                     - Vehicle Profiles, Availability, GPS Location
                 |                     ^
                 |                     | (1) Sync REST: GET /api/drivers/eligible
                 |                     |                PATCH /api/drivers/{id}/availability
                 |                     |
                 +-------------------> Ride Management Service (Port 8083)
                 |                     - Own DB: ridedb (H2)
                 |                     - Ride Requests, Lifecycle State Machine
                 |                     |
                 |                     | (2) Sync REST: POST /api/fares/calculate
                 |                     v
                 +-------------------> Fare & Payment Service (Port 8084)
                                       - Own DB: faredb (H2)
                                       - Fare Formula, Simulated Payment, Receipts
```

---

## 2. Microservice Ownership & Responsibilities

| # | Microservice | Port | Database | Primary Owner | Responsibilities |
|---|---|---|---|---|---|
| **1** | **Account Service** | `8081` | H2 (`accountdb`) | **Member 1** | Passenger & Driver registration, BCrypt password hashing, JWT token issuance, Role authorization (`PASSENGER`, `DRIVER`, `ADMIN`), Profile view/update, Account status management. |
| **2** | **Driver & Vehicle Service** | `8082` | H2 (`driverdb`) | **Member 2** | Driver operational details, Vehicle specifications, Availability status (`AVAILABLE`, `UNAVAILABLE`, `ON_RIDE`), Service area & simulated coordinates, Eligible driver discovery. |
| **3** | **Ride Management Service** | `8083` | H2 (`ridedb`) | **Member 3** | Ride request creation, Dynamic driver assignment, Strict lifecycle state machine (`REQUESTED` -> `ASSIGNED` -> `ACCEPTED` -> `IN_PROGRESS` -> `COMPLETED` / `CANCELLED`), Interservice orchestration. |
| **4** | **Fare & Payment Service** | `8084` | H2 (`faredb`) | **Member 4** | Transparent formula-based fare estimation, Final fare calculation, Simulated payment processing (`SUCCESS` / `FAILED`), Digital receipt generation and retrieval. |

---

## 3. Interservice Communication

The architecture employs context-appropriate synchronous REST APIs with strong DTO contracts and resilient error handling:

1. **Ride Service -> Driver Service**:
   - `GET http://localhost:8082/api/drivers/eligible?serviceArea={area}`: Retrieves available drivers within the requested area.
   - `PATCH http://localhost:8082/api/drivers/{id}/availability?status=ON_RIDE`: Reserves driver upon assignment.
   - `PATCH http://localhost:8082/api/drivers/{id}/availability?status=AVAILABLE`: Frees driver upon completion/cancellation.

2. **Ride Service -> Fare Service**:
   - `POST http://localhost:8084/api/fares/calculate`: Calculates final fare when ride transitions to `COMPLETED`.

---

## 4. Minimum Functional Workflows Demonstrated

1. **Account and Access**:
   - Register passenger (`/api/accounts/register`)
   - Register driver (`/api/accounts/register`)
   - Login and receive JWT access token (`/api/accounts/login`)
   - View/update profile (`/api/accounts/profile/{id}`)

2. **Driver Preparation**:
   - Register vehicle & operational details (`POST /api/drivers`)
   - Update availability status to `AVAILABLE` (`PATCH /api/drivers/{id}/availability`)
   - Update simulated location & service area (`PATCH /api/drivers/{id}/location`)

3. **Fare Estimation**:
   - Request pre-ride fare estimate based on distance (`POST /api/fares/estimate`)
   - Formula: `Total = Base Fare (200 LKR) + (Distance * 80 LKR/km)`

4. **Ride Request & Driver Assignment**:
   - Passenger creates ride request (`POST /api/rides/request`)
   - System orchestrates with Driver Service, discovers eligible driver in "Colombo", assigns driver, transitions status to `ASSIGNED`.

5. **Ride Lifecycle State Machine**:
   - Driver accepts ride (`POST /api/rides/{id}/accept`) -> status: `ACCEPTED`
   - Driver starts ride (`POST /api/rides/{id}/start`) -> status: `IN_PROGRESS`

6. **Completion and Payment**:
   - Driver completes ride (`POST /api/rides/{id}/complete`) -> triggers Fare Service calculation, status: `COMPLETED`.
   - Record simulated payment (`POST /api/fares/payments`) -> status: `SUCCESS`.
   - Retrieve full receipt (`GET /api/fares/receipts/{rideId}`).

7. **Negative Scenarios (Failures & Edge Cases)**:
   - **Negative Scenario 1 (No Driver Available)**: Requesting a ride in an area with zero available drivers (e.g. "Galle") returns a clean 404/Empty result.
   - **Negative Scenario 2 (Invalid State Transition)**: Attempting an illegal transition (e.g., completing a ride that was never started, or accepting a cancelled ride) returns `400 Bad Request` with state violation details.
   - **Negative Scenario 3 (Failed Simulated Payment)**: Simulating an invalid transaction or card decline returns `PaymentStatus.FAILED` with a documented rejection reason.

---

## 5. Quick Start & Execution

### Prerequisites
- Java 17 or higher
- Apache Maven 3.8+

### Build All Services
```bash
mvn clean test
```

### Start Services (Option A: Helper Script)
```bash
chmod +x start-all.sh stop-all.sh
./start-all.sh
```

### Start Services (Option B: Individual Terminals)
```bash
# Terminal 1 - Account Service (8081)
cd account-service && mvn spring-boot:run

# Terminal 2 - Driver Service (8082)
cd driver-service && mvn spring-boot:run

# Terminal 3 - Ride Management Service (8083)
cd ride-service && mvn spring-boot:run

# Terminal 4 - Fare & Payment Service (8084)
cd fare-service && mvn spring-boot:run
```

---

## 6. Swagger UI / OpenAPI Endpoints

| Microservice | Port | Swagger UI URL |
|---|---|---|
| Account Service | `8081` | [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) |
| Driver Service | `8082` | [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) |
| Ride Management Service | `8083` | [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html) |
| Fare & Payment Service | `8084` | [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html) |

---

## 7. Postman Collection
Import the files from `postman/` into Postman:
- `RideLink.postman_collection.json`
- `RideLink.postman_environment.json`
All 7 workflows can be executed sequentially or individually.
