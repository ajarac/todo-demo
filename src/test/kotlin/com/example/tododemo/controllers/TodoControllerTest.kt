package com.example.tododemo.controllers

import com.example.tododemo.entities.Todo
import com.example.tododemo.entities.TodoMother
import com.example.tododemo.exceptions.ErrorResponse
import com.example.tododemo.exceptions.TodoNotFoundException
import com.example.tododemo.repository.TodoRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class TodoControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setUp() {
        todoRepository.deleteAll()
    }

    @Test
    fun `should return empty todos if DB is empty`() {
        val response = restTemplate
            .getForEntity("http://localhost:$port/todos", String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(objectMapper.writeValueAsString(emptyList<Todo>()), response.body)
    }

    @Test
    fun `should create a todo and return it`() {
        val todo = Todo(
            title = "title",
            description = "description",
            done = false
        )
        val response = restTemplate
            .postForEntity("http://localhost:$port/todos", todo, Todo::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(todo.title, response.body?.title)
        assertEquals(todo.description, response.body?.description)
        assertEquals(todo.done, response.body?.done)
    }

    @Test
    fun `should return a list of todos if DB is not empty`() {
        val todo = Todo(
            title = "title",
            description = "description",
            done = false
        )
        val response = restTemplate
            .postForEntity("http://localhost:$port/todos", todo, Todo::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = restTemplate
            .exchange(
                "http://localhost:$port/todos",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<Todo>>() {})

        assertEquals(HttpStatus.OK, response2.statusCode)

        // Map the JSON response to a List<Todo>
        val todos: List<Todo> = response2.body ?: emptyList()

        // Now you can assert the List<Todo>
        assertEquals(1, todos.size)
        assertEquals(todo.title, todos[0].title)
        assertEquals(todo.description, todos[0].description)
        assertFalse(todos[0].done)
    }

    @Test
    fun `should return todo not found if does not exist by id`() {
        val id = UUID.randomUUID()
        val expected = TodoNotFoundException(id)

        val response = restTemplate
            .getForEntity("http://localhost:$port/todos/$id", ErrorResponse::class.java)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals(expected.message, response.body?.message)
        assertEquals(HttpStatus.NOT_FOUND, response.body?.statusCode)
        assertEquals("https://www.todo.com/", response.body?.domain)
    }

    @Test
    fun `should return a todo by id`() {
        val todo = TodoMother.randomPending()
        val response = restTemplate
            .postForEntity("http://localhost:$port/todos", todo, Todo::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = restTemplate
            .getForEntity("http://localhost:$port/todos/${response.body?.id}", Todo::class.java)

        assertEquals(HttpStatus.OK, response2.statusCode)

        // Map the JSON response to a Todo
        val todo2: Todo? = response2.body

        // Now you can assert the Todo
        assertEquals(todo.title, todo2?.title)
        assertEquals(todo.description, todo2?.description)
        assertFalse(todo2?.done!!)
    }

    @Test
    fun `should complete a todo by id`() {
        val todo = TodoMother.randomPending()
        val response = restTemplate
            .postForEntity("http://localhost:$port/todos", todo, Todo::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = restTemplate
            .exchange(
                "http://localhost:$port/todos/${response.body?.id}/complete",
                HttpMethod.PUT,
                null,
                Todo::class.java
            )

        assertEquals(HttpStatus.OK, response2.statusCode)
        val todo2: Todo? = response2.body
        assertEquals(todo.title, todo2?.title)
        assertEquals(todo.description, todo2?.description)
        assertEquals(true, todo2?.done)
    }

    @Test
    fun `should delete a todo by id`() {
        val todo = TodoMother.randomPending()
        val response = restTemplate
            .postForEntity("http://localhost:$port/todos", todo, Todo::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)

        val response2 = restTemplate
            .exchange(
                "http://localhost:$port/todos/${response.body?.id}",
                HttpMethod.DELETE,
                null,
                Unit::class.java
            )

        assertEquals(HttpStatus.OK, response2.statusCode)

        val response3 = restTemplate
            .getForEntity("http://localhost:$port/todos/${response.body?.id}", ErrorResponse::class.java)
        assertEquals(HttpStatus.NOT_FOUND, response3.statusCode)
    }
}
