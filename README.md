# Order API Testing Guide

This guide provides instructions for testing the Order API endpoints and Kafka event triggers using cURL.

---

# Order API Testing Guide

This guide provides instructions for testing the Order API endpoints and Kafka event triggers using cURL.

---

## Prerequisites

- **Base URL:** `http://localhost:8080/api/v1/orders`
- **Authentication:** Use a JWT token in the `Authorization` header (e.g., `Bearer YOUR_JWT_TOKEN`).
- **Kafka Topic:** `order-events`
- **Kafka Dead Letter Queue:** `orders-events-dlq`
- **Kafka Broker:** Ensure Kafka is running and accessible at `localhost:9092`.

---

## Testing API Endpoints

### 1. Create an Order

**cURL Command:**
```bash
curl -X POST "http://localhost:8080/api/v1/orders" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"orderId": "123", "customerId": "cust123", "amount": 100.00, "vatAmount": 10.00, "status": "PENDING"}'
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `CREATE` event published to Kafka topic `order-events`.

---

### 2. Get an Order

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/v1/orders/123" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
- **Status Code:** `200 OK` if order exists, `404 Not Found` otherwise.

---

### 3. Update an Order

**cURL Command:**
```bash
curl -X PUT "http://localhost:8080/api/v1/orders/123" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"orderId": "123", "customerId": "cust123", "amount": 150.00, "vatAmount": 15.00, "status": "COMPLETED"}'
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `UPDATE` event published to Kafka topic `order-events`.

---

### 4. Delete an Order

**cURL Command:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/orders/123" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `DELETE` event published to Kafka topic `order-events`.

---

## Testing Kafka Events

### Verify Kafka Events

Use the Kafka console consumer to verify events:

**Command:**
```bash
kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic order-events \
  --from-beginning
```

**Expected Output:**
- Events should match the API actions (e.g., `CREATE`, `UPDATE`, `DELETE`).

Example event payload:
```json
{
  "eventType": "CREATE",
  "order": {
    "orderId": "123",
    "customerId": "cust123",
    "amount": 100.00,
    "vatAmount": 10.00,
    "status": "PENDING"
  }
}
```

---

## Troubleshooting

- **404 Not Found:** Ensure the correct endpoint path is used.
- **400 Bad Request:** Check the payload format and ensure required fields are present.
- **401 Unauthorized:** Ensure `Authorization: Bearer YOUR_JWT_TOKEN` header is included.
- **Kafka Issues:** Verify Kafka broker is running and the topics `order-events` and `orders-events-dlq` exist.

---

## Next Steps

1. Test the endpoints using the provided cURL commands.
2. Verify Kafka events using the Kafka console consumer.
3. Document any issues or findings in this guide.

---

Happy Testing!

---

## Testing API Endpoints

### 1. Create an Order

**cURL Command:**
```bash
curl -X POST "http://localhost:8080/api/v1/orders" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"orderId": "123", "customerId": "cust123", "amount": 100.00, "vatAmount": 10.00, "status": "PENDING"}'
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `CREATE` event published to Kafka topic `order-events`.

---

### 2. Get an Order

**cURL Command:**
```bash
curl -X GET "http://localhost:8080/api/v1/orders/123" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
- **Status Code:** `200 OK` if order exists, `404 Not Found` otherwise.

---

### 3. Update an Order

**cURL Command:**
```bash
curl -X PUT "http://localhost:8080/api/v1/orders/123" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"orderId": "123", "customerId": "cust123", "amount": 150.00, "vatAmount": 15.00, "status": "COMPLETED"}'
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `UPDATE` event published to Kafka topic `order-events`.

---

### 4. Delete an Order

**cURL Command:**
```bash
curl -X DELETE "http://localhost:8080/api/v1/orders/123" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response:**
- **Status Code:** `202 Accepted`
- **Event Triggered:** `DELETE` event published to Kafka topic `order-events`.

---

## Testing Kafka Events

### Verify Kafka Events

Use the Kafka console consumer to verify events:

**Command:**
```bash
kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic order-events \
  --from-beginning
```

**Expected Output:**
- Events should match the API actions (e.g., `CREATE`, `UPDATE`, `DELETE`).

Example event payload:
```json
{
  "eventType": "CREATE",
  "order": {
    "orderId": "123",
    "customerId": "cust123",
    "amount": 100.00,
    "vatAmount": 10.00,
    "status": "PENDING"
  }
}
```

---

## Notes

- Replace `YOUR_JWT_TOKEN` with an actual JWT token if authentication is required.
- Ensure Kafka is running and accessible at `localhost:9092`.
- For testing, you can use placeholder values for `orderId`, `customerId`, etc.

---

## Troubleshooting

- **404 Not Found:** Ensure the correct endpoint path is used.
- **400 Bad Request:** Check the payload format and ensure required fields are present.
- **Kafka Issues:** Verify Kafka broker is running and the topic `order-events` exists.

---

## Next Steps

1. Update the `Base URL` and `Authentication` sections with your specific configuration.
2. Confirm the Kafka topic name if it differs from `order-events`.
3. Test the endpoints and verify Kafka events using the provided cURL commands and Kafka consumer.

---

Happy Testing!
