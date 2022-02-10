package com.bugs.posisi_in_1301.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugs.posisi_in_1301.databinding.ActivityManageDataSampelBinding
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.tools.State
import com.bugs.posisi_in_1301.viewModel.SampelViewModel
import com.bugs.posisi_in_1301.viewModel.SampelViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ManageDataSampelActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding : ActivityManageDataSampelBinding

    lateinit var viewModel : SampelViewModel

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageDataSampelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, SampelViewModelFactory())[SampelViewModel::class.java]

        binding.rvSampel.layoutManager = LinearLayoutManager(this)

        binding.btnAdd.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun susGetAllSampel() {
        viewModel.getAllSampel().collect { state ->
            when(state) {
                is State.Loading -> {
                    showToast("Loading")
                }

                is State.Success -> {
                    showToast("Success")
                    val adapter = SampelRecyclerAdapter(this, state.data)
                    binding.rvSampel.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                is State.Failed -> {
                    showToast(state.message)
                }
            }
        }
    }

    suspend fun susDeleteSampel(sampel: Sampel) {
        viewModel.deleteSampel(sampel).collect { state ->
            when(state) {
                is State.Loading -> {
                    showToast("Loading")
                }

                is State.Success -> {
                    susGetAllSampel()
                }

                is State.Failed -> {
                    showToast(state.message)
                }
            }
        }
    }

    fun deleteData(sampel: Sampel) {
        uiScope.launch {
            susDeleteSampel(sampel)
        }
    }

    init {
        uiScope.launch {
            susGetAllSampel()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            binding.btnAdd.id -> {
                val intent = Intent(this, TambahSampelActivity::class.java)
                startActivity(intent)
            }
        }
    }
}