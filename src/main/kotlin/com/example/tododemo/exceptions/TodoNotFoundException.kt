package com.example.tododemo.exceptions

import java.util.*

class TodoNotFoundException(uuid: UUID) : RuntimeException("Todo with id $uuid not found")
