package net.overclock.consultacep.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.overclock.consultacep.data.repository.CepRepository
import net.overclock.consultacep.data.validator.CepValidator


class CepViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CepViewModel::class.java)) {
            return CepViewModel(
                cepRepository = CepRepository(),
                cepValidator = CepValidator()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
