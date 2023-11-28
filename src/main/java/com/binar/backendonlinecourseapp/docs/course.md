## COURSE API SPEC

## CREATE COURSE

Endpoint : POST /api/course/create

Request Header :

Authorization : Bearer 1231312312 (ROLE ADMIN)

Request Body :
```json
{
  "namaKelas": "dwadwadwa",
  "kategori": "web development",
  "kodeKelas": "dawda",
  "tipeKelas": "PREMIUM",
  "level": "ADVANCED",
  "harga": "150000",
  "materi": "course ini mempelajari tentang cara pembuatan web menggunakan javascript",
  "insertVideo": [
    {
      "judulVideo": "introduction java script",
      "linkVideo": "https://youtu.be/ixOd42SEUF0",
      "desc": "video introduction atau perkenalan kepada bahasa pemreogramman java",
      "isPremium": "false",
      "chapter": "1"
    },{
      "judulVideo": "dwaiodnowaid java script",
      "linkVideo": "https://youtu.be/DwTkyMJi890",
      "desc": "video diwadioawdiwaj kepada bahasa pemreogramman java",
      "isPremium": "true",
      "chapter": "1"
    }
  ]

}
```

Request Body (Success):
```json
{
  "data": {
    "namaKelas": "dwadwadwa",
    "kategori": "web development",
    "kodeKelas": "dawda",
    "harga": 150000,
    "materi": "course ini mempelajari tentang cara pembuatan web menggunakan javascript"
  },
  "message": "sucess create new course",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "course code already exists",
  "errors": true
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "course name already exists",
  "errors": true
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "category not found",
  "errors": true
}
```

## GET COURSE

Endpoint : GET /api/course/{page}

Request Header (OPTIONAL):

Authorization : Bearer 1231312312 (ROLE ADMIN)

Response Body (Success):
```json
{
  "data": [
    {
      "kodeKelas": "wdjs",
      "namaKelas": "web development using javascript",
      "kategori": "web development",
      "level": "ADVANCED",
      "harga": 150000,
      "teacher": "user",
      "tipeKelas": "PREMIUM"
    },
    {
      "kodeKelas": "dawda",
      "namaKelas": "dwadwadwa",
      "kategori": "web development",
      "level": "ADVANCED",
      "harga": 150000,
      "teacher": "user",
      "tipeKelas": "PREMIUM"
    }
  ],
  "message": "success get data",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "course data is null",
  "errors": true
}
```

## SEARCH COURSE

Endpoint : GET /api/course/search/{page}/{keyword with nama kelas or teacher}

Response Body (Success):
```json
{
  "data": [
    {
      "kodeKelas": "wdjs",
      "namaKelas": "web development using javascript",
      "kategori": "web development",
      "level": "ADVANCED",
      "harga": 150000,
      "teacher": "user",
      "tipeKelas": "PREMIUM"
    },
    {
      "kodeKelas": "dawda",
      "namaKelas": "dwadwadwa",
      "kategori": "web development",
      "level": "ADVANCED",
      "harga": 150000,
      "teacher": "user",
      "tipeKelas": "PREMIUM"
    }
  ],
  "message": "success get data",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "course not found",
  "errors": true
}
```

## HIT COURSE DETAIL

Endpoint : GET /api/course/get/{kodeKelas}

Request Header:

Authorization : Bearer 1231312312 (ROLE ADMIN)

Response Body (Success):
```json
{
  "data": {
    "kodeKelas": "wdjs",
    "namaKelas": "web development using javascript",
    "kategori": "web development",
    "level": "ADVANCED",
    "harga": 150000,
    "teacher": "user",
    "deskripsi": "course ini mempelajari tentang cara pembuatan web menggunakan javascript",
    "getVideoResponses": [
      {
        "videoCode": "be4ea2",
        "judulVideo": "introduction java script",
        "linkVideo": "https://youtu.be/ixOd42SEUF0",
        "chapter": "1",
        "premium": false
      },
      {
        "videoCode": "72617c",
        "judulVideo": "dwaiodnowaid java script",
        "linkVideo": "https://youtu.be/DwTkyMJi890",
        "chapter": "1",
        "premium": false
      }
    ]
  },
  "message": "successfully get data",
  "errors": false
}
```

Response Body (Failed 400) :
```json
{
  "data": null,
  "message": "fail hit data",
  "errors": true
}
```