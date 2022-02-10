package com.bugs.posisi_in_1301.viewModel

import androidx.lifecycle.ViewModel
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.repository.SampelRepository

class SampelViewModel(private val repository: SampelRepository) : ViewModel() {

    fun getAllSampel() = repository.getAllSampel()

    fun deleteSampel(sampel: Sampel) = repository.deleteSampel(sampel)

    fun addSampel(sampel: Sampel) = repository.addSampel(sampel)

}