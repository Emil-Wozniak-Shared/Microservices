# Getting Started

## Endpoints

- error prone
```bash
echo '{"title": null ,"content": "validate test 5",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
```

- success
```bash
echo '{"title": "Test 13","content": "validate test 3",  "status": "DRAFT"}' | http POST localhost:8080/api/posts
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

