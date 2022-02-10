package com.bugs.posisi_in_1301.view

import android.net.wifi.ScanResult
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bugs.posisi_in_1301.model.DataMasukan
import com.bugs.posisi_in_1301.tools.WifiConstants

open class BaseActivity : AppCompatActivity() {

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}