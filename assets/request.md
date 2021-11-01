# Commandline Commands


## Error prone
```bash
echo '{"title": null ,"content": "validate test 5",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
```

- success

```bash
echo '{"title": "Test 13","content": "validate test 3",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
```

## Actuator

### Gateway

Request current list of services
```http request
GET http://localhost:8180/actuator/gateway/routes
```

## Request token

Curl
```bash
curl -X POST \
  http://localhost:8180/users/oauth/token \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{ "username": "John" }'
```
HTTPie

```bash
printf '{ "username": "John" }'| http  --follow --timeout 3600 POST 'http://localhost:8180/users/oauth/token'  Content-Type:'application/json'  Cache-Control:'no-cache'
```

## Request Users

```bash
curl -X GET \
  http://localhost:8180/users/api/users/ \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'cache-control: no-cache'
```

## Create User

```bash
curl --location --request POST 'http://localhost:8180/users/api/users/' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": null,
    "firstName": "John",
    "lastName": "Snow",
    "password": "123456",
    "email": "j.snow@test.pl",
}'
```

## Refresh user service
```bash
curl localhost:8040/actuator/refresh -d {} -H "Content-Type: application/json"
```

## Request all users through Gateway

```bash
curl -X GET \
  http://localhost:8180/users/api/users/ \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache'
```
UserHandlerTest
## Request token
Request body contains json with **username** which is required User object email value.

```bash
printf '{
    "id": null,
    "firstName": "John",
    "lastName": "Snow",
    "password": "123456",
    "email": "j.snow@test.pl",
    "karma": 80
}'| http  --follow --timeout 3600 POST 'http://localhost:8180/users/api/users/' \
 Content-Type:'application/json'
```
