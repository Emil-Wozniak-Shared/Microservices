# Getting Started


## Requirement

- **Gradle**
- **Kotlin**
- **common project** in local .m2 repository 
- **config server project**
  - Environment variables
    * URI = web or local store of project properties
    * USER = store username credential
    * PASS = store password credential
- **Postgres**
  - database: *microservice*
  
### Project *common*

Go to [common](./common) project and execute publish to local repository

```bash
gradle publishToMavenLocal
```
or type `ctrl + ctrl`  and then `gradle addCommon`, it also works in other projects.

This will install **common** project to your local m2 repository.

### Database

- Postgresql
    - username: `ifzz`
    - password: `ifzz`
    - database: `microservice`
    - port:     `localhost:5432`

**IMPORTANT**
Execute [schema.sql](src/main/resources/schema.sql) file

You can use also [Docker](./docker/psql/docker-compose.yml) image, with few configurations
this will help you speed up start applications.

All projects can replace use custom database **username**, **password** and **port** by providing
environment variables **DB_PORT**, **DB_USER** and / or **DB_PASS**. I provided those to help
avoid conflicts on the ports.

## Project structure

![diagram](./assets/Microservices.svg)

## Endpoints

- **Eureka server**:  http://localhost:8010
- **Spring Gateway**: http://localhost:8180
- **Posts endpoint**: http://localhost:8020
- **Users endpoint**: http://localhost:8040
- **Customers endpoint**: http://localhost:8060

- error prone

```bash
echo '{"title": null ,"content": "validate test 5",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
```

- success

```bash
echo '{"title": "Test 13","content": "validate test 3",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
```

### Actuator

#### Gateway 

Request current list of services 
```http request
GET http://localhost:8180/actuator/gateway/routes
```

### User Security

The definition below solved the `PasswordEncoder` problem. See
[Handler.kt#L19-22](https://github.com/kensiprell/kotlin-spring-security/blob/master/src/main/kotlin/com/siprell/kotlinspringsecurity/SecurityConfiguration.kt#L19-22).

```kotlin
@Bean
fun passwordEncoder(): PasswordEncoder {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
```

#### Curl Commands

##### Request token

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

##### Request Users

```bash
curl -X GET \
  http://localhost:8180/users/api/users/ \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'cache-control: no-cache'
```

##### Create User

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

##### Refresh user service
```bash
curl localhost:8040/actuator/refresh -d {} -H "Content-Type: application/json"
```

##### Request all users through Gateway

```bash
curl -X GET \
  http://localhost:8180/users/api/users/ \
  -H 'Accept: application/json' \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache'
```
UserHandlerTest
##### Request token
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

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.4/spring-framework-reference/languages.html#coroutines)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## Properties Store

In my store I use pattern:
<pre>
📦 Project dir
┣ 📜 application.yml
┣ 📂 posts
┃ ┗ 📜 application.yml
┣ 📂 users
┃ ┗ 📜 application.yml
</pre>


## Folders description

Notice that Projects are not Modules

- **common**:
  - *PROJECT*
  - provides common classes for others projects
- **configserver**:
  - *PROJECT*
  - projects properties configuration project
- **contract**
  - *PROJECT*
  - OpenApi models and endpoints 
- **docker**:
  - *DIR*
  - docker images
- **gateway**:
  - *PROJECT*
  - projects gateway
- **gradle-addons**:
  - *DIR*
  - gradle common configurations
- **http**:
  - *DIR*
  - sample http requests
- **posts**:
  - *PROJECT*
  - sample api for users
- **src**:
  - *PROJECT*
  - main module and discovery server
- **users**:
  - *PROJECT*
  - user authentication and profile module

## OpenAPI 
I configure OpenAPi kotlin models and webflux endpoints **contract** project

To generate them use `gradle openApiGenerate`
