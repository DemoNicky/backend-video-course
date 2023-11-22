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
    "token": "1231231321"
  },
  "message": "success register",
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

Response Body (Failed) : 
```json
{
  "data": null,
  "message": "cant update data",
  "errors": true
}
```