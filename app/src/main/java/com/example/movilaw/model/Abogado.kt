package com.example.movilaw.model

data class Abogado(
    val id: Int,
    val created_at: Long,
    val nombre_abogado: String,
    val rut: Long,
    val numero_caso: Int,
    val nombre_cliente: String
)