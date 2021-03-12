package pl.emil.posts.web.validator

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator
import org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace
import org.springframework.validation.annotation.Validated
import org.springframework.validation.beanvalidation.SpringValidatorAdapter
import pl.emil.posts.config.constant.FIELD_REQUIRED

@Component
@Validated
class RequestValidator(private val validator: LocalValidator) : SmartValidator {

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
        val validatorAdapter = SpringValidatorAdapter(validator)
        validatorAdapter.validate(target, errors)
    }
}