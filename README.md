# Getting Started

## Requirement

### Database

- Postgresql
    - username: `ifzz`
    - password: `ifzz`
    - database: `microservice`
    - port:     `localhost:5432`

**IMPORTANT**
Execute [schema.sql](src/main/resources/schema.sql) file

## Endpoints

- **Eureka server**:  http://localhost:8010
- **Spring Gateway**: http://localhost:8180
- **Posts endpoint**: http://localhost:8020
- **Users endpoint**: http://localhost:8040

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

```bash
curl -X POST \
  http://localhost:8180/users/oauth/token \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{ "username": "John" }'
```


##### Request Users

```bash
curl -X GET \
  http://localhost:8180/users/api/users/ \
  -H 'Authorization: Bearer <TOKEN>' \
  -H 'cache-control: no-cache'
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

```http request
POST /users/oauth/token HTTP/1.1
Host: localhost:8180
Content-Type: application/json
cache-control: no-cache
{
  "username": "e.wozniak@ifzz.pl"
}------WebKitFormBoundary7MA4YWxkTrZu0gW--
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

