package com.example.tododemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoDemoApplication

fun main(args: Array<String>) {
    runApplication<TodoDemoApplication>(*args)
}
