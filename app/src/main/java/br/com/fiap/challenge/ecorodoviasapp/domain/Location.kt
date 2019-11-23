package br.com.fiap.challenge.ecorodoviasapp.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Location (
    val id: String?,
    val user: User,
    val position: Position,
    val timestamp: LocalDateTime?
)