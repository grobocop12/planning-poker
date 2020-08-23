package com.grobocop.springscrumpoker.controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.RequestDispatcher
import javax.servlet.http.HttpServletRequest

@Controller
class ExceptionController : ErrorController {

    @RequestMapping("/error")
    fun handleError(request: HttpServletRequest): String {
        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
        if (status != null) {
            val statusCode = Integer.valueOf(status.toString())
            if(statusCode== HttpStatus.NOT_FOUND.value()){
                return "error-404"
            }
        }
        return "error"
    }

    override fun getErrorPath(): String? {
        return null;
    }

}