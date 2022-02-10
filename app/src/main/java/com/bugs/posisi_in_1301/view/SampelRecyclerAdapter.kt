package com.bugs.posisi_in_1301.view

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugs.posisi_in_1301.databinding.ItemSampelBinding
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.viewModel.SampelViewModel

class SampelRecyclerAdapter(
    private val activity: ManageDataSampelActivity,
    private val listSampel: List<Sampel>
) : RecyclerView.Adapter<SampelRecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding : ItemSampelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sampel: Sampel){
            binding.tvLabel.text = sampel.label.toString()
            binding.tvAp1Level.text = sampel.ap1.toString()
            binding.tvAp2Level.text = sampel.ap2.toString()
            binding.tvAp3Level.text = sampel.ap3.toString()
            binding.tvCreationTime.text = DateUtils.getRelativeTimeSpanString(sampel.creation!!)

            binding.btnDelete.setOnClickListener {
                activity.deleteData(sampel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSampelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSampel[position])
    }

    override fun getItemCount(): Int = listSampel.size
}