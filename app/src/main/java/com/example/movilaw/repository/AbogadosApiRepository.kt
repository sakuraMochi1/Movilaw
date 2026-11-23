

package com.example.movilaw.repository

import com.example.movilaw.model.Abogado
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AbogadosApiRepository {

    suspend fun getAbogados(): List<Abogado> =
        withContext(Dispatchers.IO) {

            val url = URL(
                "https://x8ki-letl-twmt.n7.xano.io/api:5aziZbsy/movilaw_abogados"
            )

            val connection =
                (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 15000
                    readTimeout = 15000
                    setRequestProperty("Accept", "application/json")
                }

            try {
                val responseCode = connection.responseCode

                val response = if (responseCode in 200..299) {
                    connection.inputStream.bufferedReader().use {
                        it.readText()
                    }
                } else {
                    val error = connection.errorStream
                        ?.bufferedReader()
                        ?.use { it.readText() }

                    throw IllegalStateException(
                        "Error HTTP $responseCode${error?.let { ": $it" } ?: ""}"
                    )
                }

                val jsonArray = JSONArray(response)

                List(jsonArray.length()) { index ->
                    val item = jsonArray.getJSONObject(index)

                    Abogado(
                        id = item.getInt("id"),
                        created_at = item.getLong("created_at"),
                        nombre_abogado = item.getString("nombre_abogado"),
                        rut = item.getLong("rut"),
                        numero_caso = item.getInt("numero_caso"),
                        nombre_cliente = item.getString("nombre_cliente")
                    )
                }
            } finally {
                connection.disconnect()
            }
        }

    suspend fun crearAbogado(
        nombreAbogado: String,
        rut: Long,
        numeroCaso: Int,
        nombreCliente: String
    ) = withContext(Dispatchers.IO) {

        val url = URL(
            "https://x8ki-letl-twmt.n7.xano.io/api:5aziZbsy/movilaw_abogados"
        )

        val connection =
            (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 15000
                readTimeout = 15000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
            }

        try {
            val json = JSONObject().apply {
                put("nombre_abogado", nombreAbogado)
                put("rut", rut)
                put("numero_caso", numeroCaso)
                put("nombre_cliente", nombreCliente)
            }

            connection.outputStream.use { output ->
                output.write(json.toString().toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode

            if (responseCode !in 200..299) {
                val error = connection.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }

                throw IllegalStateException(
                    "Error al guardar: HTTP $responseCode ${error ?: ""}"
                )
            }

        } finally {
            connection.disconnect()
        }
    }
}
