package br.com.fiap.challenge.ecorodoviasapp

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_CODE = 100
    }

    private lateinit var broadcastReceiver: BroadcastReceiver

    override fun onResume() {
        super.onResume()

        if(!::broadcastReceiver.isInitialized) {
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                    textView.append("\n ${getCoordinates()}")
                }
            }
        }

        registerReceiver(broadcastReceiver, IntentFilter("location_update"))
    }

    private fun getCoordinates() = intent.extras?.get("coordinates") ?: ""

    override fun onDestroy() {
        super.onDestroy()

        if(::broadcastReceiver.isInitialized) {
            unregisterReceiver(broadcastReceiver)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if(!hasToAskPermissions()) {
            enableButtons()
        }
    }

    private fun hasToAskPermissions(): Boolean {
        if(Build.VERSION.SDK_INT >= 23 &&
            checkPermission(ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
            checkPermission(ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
            requestPermissions(permissions, LOCATION_PERMISSION_CODE)

            return true
        }

        return false
    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION_PERMISSION_CODE){
            if(grantResults.all { it == PERMISSION_GRANTED }){
                enableButtons()
            } else {
                hasToAskPermissions()
            }
        }
    }

    private fun enableButtons() {
        buttonStart.setOnClickListener {
            startService(Intent(applicationContext, GPSService::class.java))
        }

        buttonStop.setOnClickListener {
            stopService(Intent(applicationContext, GPSService::class.java))
        }
    }
}
