package com.bugs.posisi_in_1301.view

import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bugs.posisi_in_1301.algorithm.Knn
import com.bugs.posisi_in_1301.databinding.ActivityOnlineBinding
import com.bugs.posisi_in_1301.model.DataMasukan
import com.bugs.posisi_in_1301.model.Sampel
import com.bugs.posisi_in_1301.receiver.OnlineWifiReceiver
import com.bugs.posisi_in_1301.tools.State
import com.bugs.posisi_in_1301.tools.WifiConstants
import com.bugs.posisi_in_1301.viewModel.SampelViewModel
import com.bugs.posisi_in_1301.viewModel.SampelViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class OnlineActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding : ActivityOnlineBinding

    private lateinit var viewModel : SampelViewModel

    val uiScope = CoroutineScope(Dispatchers.Main)

    private val listSampel = mutableListOf<Sampel>()

    private lateinit var wifiManager: WifiManager

    private lateinit var wifiReceiver: OnlineWifiReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, SampelViewModelFactory()) [SampelViewModel::class.java]

        binding.btnPosisi.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            binding.btnPosisi.id -> {
                scanWifi()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiReceiver = OnlineWifiReceiver(this, wifiManager)
        val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(wifiReceiver, filter)
    }

    override fun onStop() {
        unregisterReceiver(wifiReceiver)
        super.onStop()
    }

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

    private suspend fun susGetAllSampel() {
        viewModel.getAllSampel().collect { state ->
            when(state) {
                is State.Loading -> {
                    showToast("Loading")
                }

                is State.Success -> {
                    showToast("Sampel OK")
                    listSampel.addAll(state.data)
                }

                is State.Failed -> {
                    showToast(state.message)
                }
            }
        }
    }

    fun getPosition(masukan: DataMasukan) {
        val posisi = Knn(
            listSampel,
            masukan,
            WifiConstants.VALUE_OF_K
        ).resultPosisi()

        binding.tvPosisiValue.text = posisi
    }

}