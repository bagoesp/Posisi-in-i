package com.bugs.posisi_in_1301.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.bugs.posisi_in_1301.model.DataMasukan
import com.bugs.posisi_in_1301.tools.Constants
import com.bugs.posisi_in_1301.tools.WifiConstants
import com.bugs.posisi_in_1301.view.OnlineActivity

class OnlineWifiReceiver(
    private val activity: OnlineActivity,
    private val wifiManager: WifiManager
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val ok = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION == action
        if (ok) {
            Log.d(Constants.TAG_RECEIVER, "Receiving wifi scan result")
            val scanResult = wifiManager.scanResults
            val scanResultFiltered = scanResult.filter {
                (it.SSID == WifiConstants.SSID_AP1) || (it.SSID == WifiConstants.SSID_AP2) || (it.SSID == WifiConstants.SSID_AP3)
            }

            val masukan = DataMasukan()
            for ( ap in scanResult) {
                when (ap.SSID) {
                    WifiConstants.SSID_AP1 -> {
                        masukan.ap1 = ap.level
                    }
                    WifiConstants.SSID_AP2 -> {
                        masukan.ap2 = ap.level
                    }
                    WifiConstants.SSID_AP3 -> {
                        masukan.ap3 = ap.level
                    }

                }
            }

            // kirim data ke activity
            activity.getPosition(masukan)
        } else {
            Log.d(Constants.TAG_RECEIVER, "Not receive wifi scan result")
        }
    }
}