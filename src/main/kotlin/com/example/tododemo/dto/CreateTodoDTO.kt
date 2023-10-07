package com.example.tododemo.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateTodoDTO(
    @NotEmpty
    @Size(min = 3, max = 50)
    val title: String,
    @NotEmpty
    @Size(min = 3, max = 500)
    val description: String
)

