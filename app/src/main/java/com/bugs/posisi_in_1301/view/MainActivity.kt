package com.bugs.posisi_in_1301.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bugs.posisi_in_1301.KampretActivity
import com.bugs.posisi_in_1301.R
import com.bugs.posisi_in_1301.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnOnline.setOnClickListener(this)
        binding.btnOffline.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            binding.btnOnline.id -> {
                val intent = Intent(this, OnlineActivity::class.java)
                startActivity(intent)
            }
            binding.btnOffline.id -> {
                val intent = Intent(this, ManageDataSampelActivity::class.java)
                startActivity(intent)
            }
        }
    }
}