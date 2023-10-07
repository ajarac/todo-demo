package com.example.tododemo.controllers

import com.example.tododemo.dto.CreateTodoDTO
import com.example.tododemo.entities.Todo
import com.example.tododemo.services.TodoService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    companion object {
        private val log = LoggerFactory.getLogger(TodoController::class.java)
    }

    @GetMapping
    suspend fun findAll(): ResponseEntity<List<Todo>> {
        log.info("TodoController.findAll")
        val list = todoService.findAll()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    suspend fun findById(@PathVariable id: String): ResponseEntity<Todo> {
        log.info("TodoController.findById: id=$id")
        val todo = todoService.findById(UUID.fromString(id))
        log.info("TodoController.findById: todo=$todo")
        return ResponseEntity.ok(todo)
    }

    @PostMapping
    suspend fun create(@RequestBody createTodoDTO: CreateTodoDTO): ResponseEntity<Todo> {
        log.info("TodoController.create: createTodoDTO=$createTodoDTO")
        val todoCreated = todoService.create(createTodoDTO)
        log.info("TodoController.create: todoCreated=$todoCreated")
        return ResponseEntity<Todo>(todoCreated, HttpStatus.CREATED)
    }

    @PutMapping("/{id}/complete")
    suspend fun complete(@PathVariable id: String): ResponseEntity<Todo> {
        log.info("TodoController.complete: id=$id")
        val todoUpdated = todoService.complete(UUID.fromString(id))
        log.info("TodoController.complete: todoUpdated=$todoUpdated")
        return ResponseEntity.ok(todoUpdated)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String): ResponseEntity<Unit> {
        log.info("TodoController.delete: id=$id")
        todoService.delete(UUID.fromString(id))
        log.info("TodoController.delete: todo deleted")
        return ResponseEntity.ok().build()
    }
}
