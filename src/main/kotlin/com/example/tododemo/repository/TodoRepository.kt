package com.example.tododemo.repository


import com.example.tododemo.entities.Todo
import org.springframework.data.repository.CrudRepository
import java.util.*

interface TodoRepository : CrudRepository<Todo, UUID>
