package net.overclock.consultacep.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.overclock.consultacep.data.repository.CepRepository
import net.overclock.consultacep.data.validator.CepValidator

data class CepFormState(
    val isDataValid: Boolean = false,
    val isLoading: Boolean = false,
    val hasErrorLoading: Boolean = false,
    val endereco: Endereco? = null
)

class CepViewModel(
    private val cepRepository: CepRepository,
    private val cepValidator: CepValidator
) : ViewModel() {
    private val _formState = MutableLiveData(CepFormState())
    val formState: LiveData<CepFormState> = _formState

    fun buscarCep(cep: String) {
        if (_formState.value?.isDataValid != true || _formState.value?.isLoading == true) return

        _formState.value = _formState.value?.copy(
            isLoading = true,
            hasErrorLoading = false
        )
        viewModelScope.launch {
            try {
                val endereco = cepRepository.buscarCep(cep)
                _formState.value = _formState.value?.copy(
                    isLoading = false,
                    endereco = endereco
                )
            } catch (ex: Exception) {
                Log.e("CepViewModel", "Erro ao consultar o CEP", ex)
                _formState.value = _formState.value?.copy(
                    isLoading = false,
                    hasErrorLoading = true
                )
            }
        }
    }

    fun onCepChanged(cep: String) {
        _formState.value = _formState.value?.copy(
            isDataValid = cepValidator.verificarCep(cep)
        )
    }
}