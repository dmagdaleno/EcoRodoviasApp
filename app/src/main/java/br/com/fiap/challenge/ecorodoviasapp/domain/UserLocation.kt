package br.com.fiap.challenge.ecorodoviasapp.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserLocation (
    val id: String? = null,
    val user: User,
    val position: Position,
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    val timestamp: LocalDateTime? = null
)