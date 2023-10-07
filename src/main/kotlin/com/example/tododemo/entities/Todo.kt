package com.example.tododemo.entities


import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table(name = "todos", schema = "public")
data class Todo(
    @Id val id: UUID? = null,
    val title: String,
    val description: String,
    var done: Boolean,
    @Column("created_at")
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @Column("updated_at")
    @LastModifiedDate
    val updatedAt: Instant = Instant.now()
)
