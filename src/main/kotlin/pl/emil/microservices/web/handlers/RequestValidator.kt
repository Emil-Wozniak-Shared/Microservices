package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace
import org.springframework.validation.Validator
import pl.emil.microservices.model.Post
import pl.emil.microservices.model.user.User
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

const val FIELD_REQUIRED = "field.required"

@Component
class RequestValidator : SmartValidator {
    override fun supports(clazz: Class<*>): Boolean = RequestValidator::class.java.isAssignableFrom(clazz)

    override fun validate(target: Any, errors: Errors, vararg validationHints: Any?) {
        validationHints.forEach {
            rejectIfEmptyOrWhitespace(errors, it.toString(), FIELD_REQUIRED)
        }
    }

    override fun validate(target: Any, errors: Errors) {
        rejectIfEmptyOrWhitespace(errors, "title", FIELD_REQUIRED)
        rejectIfEmptyOrWhitespace(errors, "content", FIELD_REQUIRED)
    }
}