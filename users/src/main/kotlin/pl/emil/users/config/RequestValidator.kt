package pl.emil.users.config

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace
import org.springframework.validation.annotation.Validated

const val FIELD_REQUIRED = "field.required"

@Component
@Validated
class RequestValidator : SmartValidator {

    override fun supports(clazz: Class<*>): Boolean = RequestValidator::class.java.isAssignableFrom(clazz)

    override fun validate(target: Any, errors: Errors, vararg validationHints: Any?) {
        this.validate(target, errors)
        validationHints.forEach {
            rejectIfEmptyOrWhitespace(errors, it.toString(), FIELD_REQUIRED)
        }
    }

    /**
     * Don't use it since it will not handle javax annotations.
     */
    override fun validate(target: Any, errors: Errors) {

    }
}
