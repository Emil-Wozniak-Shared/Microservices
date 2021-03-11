package pl.emil.microservices.web.handlers

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import pl.emil.microservices.web.validator.LocalValidator

@Component
class RequestValidator(private val validator: LocalValidator) : SmartValidator {
    override fun supports(clazz: Class<*>): Boolean = RequestValidator::class.java.isAssignableFrom(clazz)

    override fun validate(target: Any, errors: Errors, vararg validationHints: Any?) {
        this.validate(target, errors)
//        validationHints.forEach {
//            rejectIfEmptyOrWhitespace(errors, it.toString(), FIELD_REQUIRED)
//        }
    }

    override fun validate(target: Any, errors: Errors) {
        val validatorAdapter = SpringValidatorAdapter(validator)
        validatorAdapter.validate(target, errors)
//        rejectIfEmptyOrWhitespace(errors, "title", FIELD_REQUIRED)
//        rejectIfEmptyOrWhitespace(errors, "content", FIELD_REQUIRED)
    }
}