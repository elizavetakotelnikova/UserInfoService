# API-documentation

## owner

### Create

**route:** /owner

**Method:** POST

**request:**

```json
{
  "birthday": "2004-02-03",
  "username": "username",
  "password": "password"
}
```

**response:**

```json
{
  "id": 234
}
```



### FindByID

**route:** /owner/{ownerId}

**Method:** GET

**path Parameters**

* {userID}

**request:**
```json
{
  "id": 234
}
```

response:

```json
{
  "id": 234,
  "birthday": "2004-02-03",
  "cats" : []
}
```

### Find

**route:** /owners/find

**Method:** GET

**path Parameters**

* birthday=date

**request:** none

**response:**

```json
{
    "117": {
      "birthday": "2005-02-01",
      "cats": [
        "2",
        "116"
      ]
    },
    "04056053-5d96-4069-94c3-4b3281ef32a0": {
        ...
    }
}
```

### Update

**route:** /owner/{ownerId}

**Method:** PUT

**path Parameters**

* {ownerId}

**request:**

```json
{
  "id": "234",
  "birthday" : "2004-02-03",
  "cats": [
    "5"
  ]
}
```

**response:** none

### Delete

**route:** /owner/{ownerId}

**Method:** DELETE

**path Parameters**

* {ownerId}

**request:** none

**response:** none

## Cat
### FindByID

**route:** /cat

**Method:** POST

**path Parameters**

* none

**request:** none

response:

```json
{
  "ownerId": "234",
  "name": "Tina",
  "birthday": "2017-04-08",
  "breed": "British shorthair",
  "friendsId": [],
  "color" : "GREY"
}
```

### UPDATE

**route:** /cat/{catId}

**Method:** PUT

**path Parameters**

* {catId}

**request:** 
```json
{
  "id": "1",
  "ownerId": "234",
  "name": "Tina",
  "birthday": "2017-04-08",
  "breed": "British shorthair",
  "friendsId": [],
  "color" : "GREY"
}
```

response:
```json
{ "id": "1" }
```

### FindByID

**route:** /cat/{catId}

**Method:** GET

**path Parameters**

* {catId}

**request:** none

response:

```json
{
  "id": "1",
  "ownerId": "234",
  "name": "Tina",
  "birthday": "2017-04-08",
  "breed": "British shorthair",
  "friendsId": [],
  "color" : "GREY"
}
```

### Find

**route:** /cats

**Method:** GET

**path Parameters**

* date = date
* ownerId = long
* color = enum value
* breed = string
* name = string

**request:** none

**response:**

```json
{
  "1": {
    "ownerId": 234,
    "birthday": "2017-04-08",
    "breed" : "British shorthair",
    "name": "Tina",
    "friendsId": [],
    "color": "GREY"
  },
  "04056053-5d96-4069-94c3-4b3281ef32a0": {
    ...
  }
}
```

### Delete

**route:** /cat/{catId}

**Method:** DELETE

**path Parameters**

* {catId}

**request:** none

**response:** none