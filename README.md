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

```bash
curl http://localhost:8040/authenticate/encoder
```

returns "class org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" or "Disabled, depending on the bean status.

```bash
curl http://localhost:8040/authenticate/hello
```

returns "Hello!"

```bash
curl -u user:password http://localhost:8040/authenticate/message
```

returns "Hello, user!"

```bash
curl -u admin:password http://localhost:8040/authenticate/message
```

returns "Hello, admin!"

```bash
curl -u user:password http://localhost:8040/authenticate/users/
```

returns the user details for "user"

```bash
curl -u user:password http://localhost:8040/authenticate/users/admin
```

returns "Access Denied"

```bash
curl -u admin:password http://localhost:8040/authenticate/users/user
```

returns the user details for "user"

```bash
curl -u admin:password http://localhost:8040/authenticate/users/admin
```

returns the user details for "admin"

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.3/gradle-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.4/spring-framework-reference/languages.html#coroutines)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

