package com.example.tododemo.entities

import com.example.tododemo.dto.CreateTodoDTO
import java.util.UUID

object TodoMother {

    fun randomList() = listOf(
        randomPending(),
        randomCompleted()
    )

    fun random() = Todo(
        id = UUID.randomUUID(),
        title = "title",
        description = "description",
        done = false
    )

    fun randomBy(createTodoDTO: CreateTodoDTO) = Todo(
        id = UUID.randomUUID(),
        title = createTodoDTO.title,
        description = createTodoDTO.description,
        done = false
    )

    fun randomPending() = Todo(
        id = UUID.randomUUID(),
        title = "title",
        description = "description",
        done = false
    )

    fun randomCompleted() = Todo(
        id = UUID.randomUUID(),
        title = "title",
        description = "description",
        done = true
    )
}
