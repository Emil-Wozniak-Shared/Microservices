package pl.emil.microservices.web.interceptor

import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import pl.emil.microservices.model.Post
import pl.emil.microservices.web.exception.RequestNonValidException
import reactor.core.publisher.Mono
import reactor.core.publisher.Mono.just



//        .doOnNext { body ->
//            val errors = BeanPropertyBindingResult(body, Post::class.java.name)
//            validator.validate(body as Any, errors)
//            if (errors.allErrors.isNotEmpty()) {
//                throw Exception(errors.allErrors.toString())
//            }
//        }
