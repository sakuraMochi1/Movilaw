package com.example.movilaw.repository

import com.example.movilaw.model.Abogado

interface AbogadosRepository {

    suspend fun getAbogados(): List<Abogado>

    suspend fun crearAbogado(
        nombreAbogado: String,
        rut: Long,
        numeroCaso: Int,
        nombreCliente: String
    )
}