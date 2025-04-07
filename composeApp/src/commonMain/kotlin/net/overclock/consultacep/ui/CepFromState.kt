package net.overclock.consultacep.ui

import net.overclock.consultacep.data.model.Endereco
data class CepFormState(
    val cep: String = "",
    val isDataValid: Boolean = false,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val endereco: Endereco = Endereco()
)