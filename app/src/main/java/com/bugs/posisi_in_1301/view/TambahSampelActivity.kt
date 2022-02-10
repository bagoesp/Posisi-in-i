package com.bugs.posisi_in_1301.view

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bugs.posisi_in_1301.R
import com.bugs.posisi_in_1301.databinding.ActivityTambahSampelBinding
import com.bugs.posisi_in_1301.model.DataMasukan
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.receiver.WifiReceiver
import com.bugs.posisi_in_1301.tools.State
import com.bugs.posisi_in_1301.tools.WifiConstants
import com.bugs.posisi_in_1301.viewModel.SampelViewModel
import com.bugs.posisi_in_1301.viewModel.SampelViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TambahSampelActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityTambahSampelBinding

    // wifi utility
    lateinit var wifiManager: WifiManager
    lateinit var wifiReceiver: WifiReceiver

    // view model
    private lateinit var viewModel : SampelViewModel

    // coroutine scope
    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahSampelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // viewModel
        viewModel = ViewModelProvider(this, SampelViewModelFactory())[SampelViewModel::class.java]

        // adapter ac label
        val labelAdapter = ArrayAdapter(this, R.layout.ac_label_item, WifiConstants.label_items)
        binding.acLabel.setAdapter(labelAdapter)

        // button on click listener
        binding.btnScan.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.btnBackTambah.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            binding.btnScan.id -> {
                scanWifi()
            }

            binding.btnSave.id -> {
                simpanData()
            }

            binding.btnBackTambah.id -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        // initialize wifi utility
        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiReceiver = WifiReceiver(this, wifiManager)

        // wifi scan result intent filter
        val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

        // register wifi receiver with intent filter
        registerReceiver(wifiReceiver, filter)
    }

    override fun onStop() {
        unregisterReceiver(wifiReceiver)
        super.onStop()
    }

    /*
        Function untuk melakukan scanning jaringan wifi dan mengambil data RSS
     */

    private fun scanWifi() {
        // check wifi state
        if (checkWifiState()) {
            // check permission coarse for >= marsmallow
            if (WifiConstants.DEVICE_BUILD_VERSION >= WifiConstants.M_BUILD_VERSION) {
                val selfPermission = ContextCompat.checkSelfPermission(this, WifiConstants.PERMISSION_COARSE)
                if (selfPermission != PackageManager.PERMISSION_GRANTED) {
                    // don't have permission, request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            WifiConstants.PERMISSION_COARSE,
                            WifiConstants.PERMISSION_FINE,
                            WifiConstants.PERMISSION_WIFI,
                            WifiConstants.PERMISSION_NETWORK
                        ),
                        WifiConstants.MY_REQUEST_CODE
                    )
                } else {
                    doScan()
                }
            } else {
                doScan()
            }
        }
    }

    private fun checkWifiState() : Boolean {
        val wifiState = wifiManager.wifiState
        return when (wifiState) {
            WifiManager.WIFI_STATE_DISABLED -> {
                showToast("Wifi disabled")
                false
            }
            WifiManager.WIFI_STATE_ENABLED -> {
                true
            }
            else -> false
        }
    }

    private fun doScan() {
        wifiManager.startScan()
    }

    // on result request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WifiConstants.MY_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted do scan
                    scanWifi()
                } else {
                    // permission not granted
                    showToast("Permission tidak diijinkan")
                }
            }
        }
    }

    // receive wifi scan result data list and show into the edit text
    fun showRSS(list: List<ScanResult>) {
        for (ap in list) {
            when (ap.SSID) {
                WifiConstants.SSID_AP1 -> {
                    binding.etAp1.setText(ap.level.toString())
                }

                WifiConstants.SSID_AP2 -> {
                    binding.etAp2.setText(ap.level.toString())
                }

                WifiConstants.SSID_AP3 -> {
                    binding.etAp3.setText(ap.level.toString())
                }
            }
        }
    }

    /*
        Function untuk menyimpan data ke cloud database
     */

    private fun simpanData() {
        if (checkForm()) {
            uiScope.launch {
                susAddSampel(ambilData())
            }
        }
        else showToast("Lengkapi form")
    }

    // suspen coroutine function to add data to the firebase
    private suspend fun susAddSampel(sampel: Sampel) {
        viewModel.addSampel(sampel).collect { state ->
            when(state) {
                is State.Loading -> {
                    showToast("Mengupload sampel")
                }

                is State.Success -> {
                    showToast("Sampel telah disimpan")
                    clearForm()
                }

                is State.Failed -> {
                    showToast("Sampel gagal diupload")
                }
            }
        }
    }

    // check form ap and label
    private fun checkForm() : Boolean {
        // check all form
        return checkAp1() && checkAp2() && checkAp3() && checkLabel()
    }

    // check ap1
    private fun checkAp1() : Boolean {
        return if (binding.etAp1.text.isNullOrEmpty()) {
            binding.tilAp1.isErrorEnabled = true
            binding.tilAp1.error = "Access point 1 tidak terdeteksi"
            false
        } else {
            binding.tilAp1.isErrorEnabled = false
            true
        }
    }

    // check ap2
    private fun checkAp2() : Boolean {
        return if (binding.etAp2.text.isNullOrEmpty()) {
            binding.tilAp2.isErrorEnabled = true
            binding.tilAp2.error = "Access point 2 tidak terdeteksi"
            false
        } else {
            binding.tilAp2.isErrorEnabled = false
            true
        }
    }

    // check ap3
    private fun checkAp3() : Boolean {
        return if (binding.etAp1.text.isNullOrEmpty()) {
            binding.tilAp3.isErrorEnabled = true
            binding.tilAp3.error = "Access point 3 tidak terdeteksi"
            false
        } else {
            binding.tilAp3.isErrorEnabled = false
            true
        }
    }

    // check label
    private fun checkLabel() : Boolean {
        return if (binding.acLabel.text.isNullOrEmpty()) {
            binding.tilLabel.isErrorEnabled = true
            binding.tilLabel.error = "Label lokasi data sampel kosong"
            false
        } else {
            binding.tilLabel.isErrorEnabled = false
            true
        }
    }

    // ambil data
    private fun ambilData() : Sampel {
        val label = binding.acLabel.text.toString()
        val ap1 = binding.etAp1.text.toString().toInt()
        val ap2 = binding.etAp2.text.toString().toInt()
        val ap3 = binding.etAp3.text.toString().toInt()
        val creation = System.currentTimeMillis()

        return Sampel(label, ap1, ap2, ap3, creation)
    }

    // clear form
    private fun clearForm() {
        binding.etAp1.text!!.clear()
        binding.etAp2.text!!.clear()
        binding.etAp3.text!!.clear()
        binding.acLabel.text!!.clear()
    }


}