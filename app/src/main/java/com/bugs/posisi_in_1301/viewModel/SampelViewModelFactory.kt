package com.bugs.posisi_in_1301.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bugs.posisi_in_1301.repository.SampelRepository

class SampelViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SampelRepository::class.java)
            .newInstance(SampelRepository())
    }
}