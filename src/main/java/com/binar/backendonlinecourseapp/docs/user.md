## USER API SPEC

## Register User

Endpoint : POST /api/users/register

Request Body :
```json
{
    "nama" : "broo",
    "email" : "broo@gmail.com",
    "telp" : "08123123123",
    "password" : "****"
}
```

Response Body (Success) :
```json
{
  "data": {
    "email": "edotank47@gmail.com"
  },
  "message": "success register, token already sent",
  "errors": false
}
```

Response Body (Failed) : 
```json
{
  "data": null,
  "message": "email/telp number invalid",
  "errors": true
}

```

## LOGIN USER

Endpoint : POST /api/users/login

Request Body :
```json
{
    "email" : "broo@gmail.com",
    "password" : "*****"
}
```

Response Body (Success) : 
```json
{
  "data": {
    "token": "1231231321"
  },
  "message": "Authentication successful",
  "errors": false
}
```

Response Body (Failed 401) :
```json
{
  "data": null,
  "message": "Authentication failed: No value present",
  "errors": true
}
``` 


Response Body (Failed 410) : 
```json
{
  "data": null,
  "message": "user account not activated yet",
  "errors": true
}
```

## GET USER

Endpoint : GET /api/users

Request Header :

Authorization : Bearer 1231312312

Response Body (Success):
```json
{
  "nama" : "broo",
  "email" : "broo@gmail.com",
  "telp" : "08123123123"
}
```

Response Body (Failed 410) :
```json
{
  
}
```

## UPDATE USER

Endpoint : PUT /api/users

Request Header : 

Authorization : Bearer 1231312312

Request Body :
```json
{
  "nama" : "broo",
  "email" : "broo@gmail.com",
  "telp" : "08123123123",
  "oldpassword" : "****",
  "newpassword" : "****"
}
```
Response Body (Success): 
```json
{
  "data": {
    "nama" : "broo",
    "email" : "broo@gmail.com",
    "telp" : "08123123123",
    "token" : "123132131"
  },
  "message": "success update data",
  "errors": false
}
```

Response Body (Failed 400) : 
```json
{
  "data": null,
  "message": "cant update data, wrong old password",
  "errors": true
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "email is already exists",
  "errors": true
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "telp is already exists",
  "errors": true
}
```

## CHECK OTP

Endpoint : POST /api/users/token/{TOKEN}/{EMAIL}

Response Body (Success):
```json
{
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlZG90YW5rNDdAZ21haWwuY29tIiwiZXhwIjoxNzAwOTAyODEzLCJpYXQiOjE3MDA4ODQ4MTN9.xmNozV4ayhMhqFFGVx9l3XpsJ7FUnxE4U3tAVB440B8Kvq7yUbZLGdS9Uu5BkBTGwvrdDKofU58p_Pbr4-Z_SA"
  },
  "message": "account active",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "token is invalid",
  "errors": true
}
```

## RESEND OTP

Endpoint : POST api/users/resend-token/{email}

Response Body (Success):
```json
{
  "data": {
    "email": "edotank47@gmail.com"
  },
  "message": "token already sent",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "email not found",
  "errors": true
}
```