package com.example.tododemo.exceptions

import com.example.tododemo.entities.MyConfig
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(val message: String, val statusCode: HttpStatus, val domain: String)

@ControllerAdvice
class CatchExceptions(private val myConfig: MyConfig) {

    companion object {
        private val log = LoggerFactory.getLogger(CatchExceptions::class.java)
    }

    @ExceptionHandler
    fun handleException(todoNotFoundException: TodoNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("CatchExceptions.handleException: todoNotFoundException=$todoNotFoundException")
        val errorResponse = ErrorResponse(
            message = todoNotFoundException.message!!,
            statusCode = HttpStatus.NOT_FOUND,
            domain = myConfig.myDomain
        )
        log.warn("CatchExceptions.handleException: errorResponse=$errorResponse")
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
}
