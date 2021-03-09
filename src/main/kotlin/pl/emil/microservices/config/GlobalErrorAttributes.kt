package pl.emil.microservices.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest

class GlobalErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(request: WebRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val map = super.getErrorAttributes(
            request, options
        )
        map["status"] = BAD_REQUEST
        map["message"] = "username is required"
        return map
    }
}