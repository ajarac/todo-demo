package com.example.tododemo.services

import com.example.tododemo.dto.CreateTodoDTO
import com.example.tododemo.entities.Todo
import com.example.tododemo.exceptions.TodoNotFoundException
import com.example.tododemo.repository.TodoRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class TodoService(private val todoRepository: TodoRepository) {
    companion object {
        private val log = LoggerFactory.getLogger(TodoService::class.java)
    }

    fun create(createTodoDTO: CreateTodoDTO): Todo {
        log.info("TodoService.create: createTodoDTO=$createTodoDTO")
        val todo = Todo(
            title = createTodoDTO.title,
            description = createTodoDTO.description,
            done = false
        )
        log.info("TodoService.create: todo=$todo")
        return todoRepository.save(todo)
    }

    fun complete(id: UUID): Todo {
        log.info("TodoService.complete: id=$id")
        val todo = findById(id)
        todo.done = true

        log.info("TodoService.complete: todo=$todo")
        return todoRepository.save(todo)
    }

    fun delete(id: UUID) {
        log.info("TodoService.delete: id=$id")
        todoRepository.deleteById(id)
    }

    fun findAll(): List<Todo> {
        log.info("TodoService.findAll")
        return todoRepository.findAll().toList()
    }

    fun findById(id: UUID): Todo {
        log.info("TodoService.findById: id=$id")
        val todoOptional = todoRepository.findById(id)
        log.info("TodoService.findById: todo is present=${todoOptional.isPresent}")
        if (todoOptional.isEmpty) {
            throw TodoNotFoundException(id)
        }
        log.info("TodoService.findById: todo=${todoOptional.get()}")
        return todoOptional.get()
    }
}

