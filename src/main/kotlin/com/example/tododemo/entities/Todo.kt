package com.example.tododemo.entities


import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table
data class Todo(
    @Id val id: UUID? = null,
    val title: String,
    val description: String,
    var done: Boolean,
    @CreatedDate
    val created: Instant = Instant.now(),
    @LastModifiedDate
    val updated: Instant = Instant.now()
)
