package com.bugs.posisi_in_1301.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.bugs.posisi_in_1301.tools.Constants
import com.bugs.posisi_in_1301.tools.WifiConstants
import com.bugs.posisi_in_1301.view.TambahSampelActivity

class WifiReceiver(
    private val activity: TambahSampelActivity,
    private val wifiManager: WifiManager
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val ok = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == action
        if (ok) {
            Log.d(Constants.TAG_RECEIVER, "Receiving wifi scan result")
            val scanResult = wifiManager.scanResults
            val scanResultFiltered = scanResult.filter {
                (it.SSID == WifiConstants.SSID_AP1) ||
                (it.SSID == WifiConstants.SSID_AP2) ||
                (it.SSID == WifiConstants.SSID_AP3)
            }
            // kirim data ke activity
            activity.showRSS(scanResultFiltered)
        } else {
            Log.d(Constants.TAG_RECEIVER, "Not receive wifi scan result")
        }
    }
}