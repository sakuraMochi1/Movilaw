
package com.example.movilaw.viewmodel

import com.example.movilaw.model.Abogado
import com.example.movilaw.repository.AbogadosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AbogadosViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    // Repositorio falso para evitar conexiones con Xano
    private class RepositorioFalso : AbogadosRepository {

        val abogados = mutableListOf<Abogado>()
        var errorCarga: Exception? = null

        override suspend fun getAbogados(): List<Abogado> {
            errorCarga?.let { throw it }
            return abogados.toList()
        }

        override suspend fun crearAbogado(
            nombreAbogado: String,
            rut: Long,
            numeroCaso: Int,
            nombreCliente: String
        ) {
            abogados.add(
                Abogado(
                    id = abogados.size + 1,
                    created_at = 0L,
                    nombre_abogado = nombreAbogado,
                    rut = rut,
                    numero_caso = numeroCaso,
                    nombre_cliente = nombreCliente
                )
            )
        }
    }

    @Test
    fun `carga correctamente los abogados`() = runTest {

        // PREPARACION
        val abogado = Abogado(
            1, 0L, "Matias del Monte",
            150309803L, 1001, "Jesus Zapata"
        )

        val repositorio = RepositorioFalso()
        repositorio.abogados.add(abogado)

        // ACCION
        val viewModel = AbogadosViewModel(repositorio)
        advanceUntilIdle()

        // COMPROBACION
        assertEquals(listOf(abogado), viewModel.abogados.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `crea un abogado correctamente`() = runTest {

        // PREPARACION
        val repositorio = RepositorioFalso()
        val viewModel = AbogadosViewModel(repositorio)

        advanceUntilIdle()

        // ACCION
        viewModel.crearAbogado(
            nombreAbogado = "Carolina Edwards",
            rut = "204378890",
            numeroCaso = "4560",
            nombreCliente = "Valeria Acosta"
        )

        advanceUntilIdle()

        // COMPROBACION
        val esperado = Abogado(
            1, 0L, "Carolina Edwards",
            204378890L, 4560, "Valeria Acosta"
        )

        assertEquals(listOf(esperado), viewModel.abogados.value)
        assertEquals(
            "Abogado agregado correctamente.",
            viewModel.mensaje.value
        )
        assertFalse(viewModel.isSaving.value)
    }

    @Test
    fun `muestra un error cuando falla la carga`() = runTest {

        // PREPARACION
        val repositorio = RepositorioFalso()
        repositorio.errorCarga = Exception("Error de conexión")

        // ACCION
        val viewModel = AbogadosViewModel(repositorio)
        advanceUntilIdle()

        // COMPROBACION
        assertEquals("Error de conexión", viewModel.error.value)
        assertTrue(viewModel.abogados.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }
}
