package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace
import org.springframework.validation.Validator
import pl.emil.microservices.model.user.User

const val FIELD_REQUIRED = "field.required"
const val FIELD_REQUIRED_MSG = "Field is required"
const val MIN_LENGTH = "field.min.length"
const val MIN_LENGTH_MSG = "The field must be at least [6] characters in length."

@Component
class RequestValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return RequestValidator::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val firstName = "firstName"
        rejectIfEmptyOrWhitespace(errors, firstName, FIELD_REQUIRED)
        rejectIfEmptyOrWhitespace(errors, "lastName", FIELD_REQUIRED)
        val request: User = target as User
        val minLength = arrayOf<Any>(Integer.valueOf(6))
        if (request.firstName != null && request.firstName!!.length < 6) {
            errors.rejectValue(firstName, MIN_LENGTH, minLength, MIN_LENGTH_MSG)
        } else if (request.firstName == null) {
            errors.rejectValue(firstName, FIELD_REQUIRED, minLength, FIELD_REQUIRED_MSG)
        }
    }
}