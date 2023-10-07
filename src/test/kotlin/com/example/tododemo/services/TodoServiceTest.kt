package com.example.tododemo.services

import com.example.tododemo.dto.CreateTodoDTO
import com.example.tododemo.entities.Todo
import com.example.tododemo.entities.TodoMother
import com.example.tododemo.exceptions.TodoNotFoundException
import com.example.tododemo.repository.TodoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TodoServiceTest {

    private var todoRepository = mockk<TodoRepository>()
    private lateinit var todoService: TodoService

    @BeforeEach
    fun setUp() {
        todoService = TodoService(todoRepository)
    }

    @Test
    fun `should create a todo`() {
        val createTodoDTO = CreateTodoDTO("Test Todo", "Test Description")
        val todo = TodoMother.randomBy(createTodoDTO)
        every { todoRepository.save(any<Todo>()) } returns todo

        val createdTodo = todoService.create(createTodoDTO)

        verify(exactly = 1) { todoRepository.save(any<Todo>()) }
        assertEquals(createTodoDTO.title, createdTodo.title)
        assertEquals(createTodoDTO.description, createdTodo.description)
        assertFalse(createdTodo.done)
    }

    @Test
    fun `should complete a existing todo`() {
        val todo = TodoMother.randomPending()
        every { todoRepository.findById(any<UUID>()) } returns Optional.of(todo)
        every { todoRepository.save(any<Todo>()) } returns todo

        val completedTodo = todoService.complete(todo.id!!)

        verify(exactly = 1) { todoRepository.findById(any<UUID>()) }
        verify(exactly = 1) { todoRepository.save(any<Todo>()) }
        assertEquals(todo.title, completedTodo.title)
        assertEquals(todo.description, completedTodo.description)
        assertEquals(true, completedTodo.done)
    }

    @Test
    fun `should throw a todoNotFoundException if try to complete a todo which does not exist`() {
        val todoId = UUID.randomUUID()
        every { todoRepository.findById(any<UUID>()) } returns Optional.empty()

        try {
            todoService.complete(todoId)
        } catch (e: TodoNotFoundException) {
            assertEquals(e.message, TodoNotFoundException(todoId).message)
        }

        verify(exactly = 1) { todoRepository.findById(any<UUID>()) }
        verify(exactly = 0) { todoRepository.save(any<Todo>()) }
    }

    @Test
    fun `should delete a todo`() {
        val todoId = UUID.randomUUID()
        every { todoRepository.deleteById(any<UUID>()) } returns Unit

        todoService.delete(todoId)

        verify(exactly = 1) { todoRepository.deleteById(any<UUID>()) }
    }

    @Test
    fun `should find all return an empty list if there is not todos`() {
        every { todoRepository.findAll() } returns emptyList()

        val todos = todoService.findAll()

        verify(exactly = 1) { todoRepository.findAll() }
        assertEquals(0, todos.size)
    }

    @Test
    fun `should find all return some todos if they exist`() {
        val list = TodoMother.randomList()
        every { todoRepository.findAll() } returns list

        val todos = todoService.findAll()

        verify(exactly = 1) { todoRepository.findAll() }
        assertEquals(2, todos.size)
        list.forEachIndexed { index, it ->
            assertEquals(it.title, todos[index].title)
            assertEquals(it.description, todos[index].description)
            assertEquals(it.done, todos[index].done)
        }
    }

    @Test
    fun `should findById return a todoNotFoundException if todo does not exist`() {
        val todoId = UUID.randomUUID()
        every { todoRepository.findById(any<UUID>()) } returns Optional.empty()

        try {
            todoService.findById(todoId)
        } catch (e: TodoNotFoundException) {
            assertEquals(e.message, TodoNotFoundException(todoId).message)
        }

        verify(exactly = 1) { todoRepository.findById(any<UUID>()) }
    }

    @Test
    fun `should findById return a todo if exist`() {
        val todoId = UUID.randomUUID()
        val todo = TodoMother.random()
        every { todoRepository.findById(any<UUID>()) } returns Optional.of(todo)

        val foundTodo = todoService.findById(todoId)

        verify(exactly = 1) { todoRepository.findById(any<UUID>()) }
        assertEquals(todo.title, foundTodo.title)
        assertEquals(todo.description, foundTodo.description)
        assertEquals(todo.done, foundTodo.done)
    }
}
