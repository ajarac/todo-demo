package com.example.tododemo.services

import com.example.tododemo.dto.CreateTodoDTO
import com.example.tododemo.entities.Todo
import com.example.tododemo.repository.TodoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TodoServiceTest {

    private var todoRepository = mockk<TodoRepository>()
    private lateinit var todoService: TodoService

    @BeforeEach
    fun setUp() {
        todoService = TodoService(todoRepository)
    }

    @Test
    fun testCreateTodo() {
        val createTodoDTO = CreateTodoDTO("Test Todo", "Test Description")
        val todo = Todo(
            title = createTodoDTO.title,
            description = createTodoDTO.description,
            done = false
        )
        every { todoRepository.save(any<Todo>()) } returns todo

        val createdTodo = todoService.create(createTodoDTO)

        verify(exactly = 1) { todoRepository.save(any<Todo>()) }
        assertEquals(createTodoDTO.title, createdTodo.title)
        assertEquals(createTodoDTO.description, createdTodo.description)
        assertFalse(createdTodo.done)
    }
}
