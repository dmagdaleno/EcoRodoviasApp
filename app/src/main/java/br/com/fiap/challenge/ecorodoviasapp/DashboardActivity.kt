package br.com.fiap.challenge.ecorodoviasapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import br.com.fiap.challenge.ecorodoviasapp.service.GPSService
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_NAME
import br.com.fiap.challenge.ecorodoviasapp.util.USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.USER_NAME
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val uuid = getSharedPreferences(PREF_USER_ID, Context.MODE_PRIVATE).getString(USER_ID, "")
        Log.d("DashboardActivity", "UUID: $uuid")

        val name = getSharedPreferences(PREF_USER_NAME, Context.MODE_PRIVATE).getString(USER_NAME, "")
        Log.d("DashboardActivity", "Name: $name")

        if(uuid.isNullOrBlank() || name.isNullOrBlank()) {
            openUserForm()
        }

        dashboardUserName.text = name

        if(!hasToAskPermissions()) {
            enableButtons()
        }
    }

    private fun enableButtons() {
        startTrackingButton.setOnClickListener {
            startService(Intent(applicationContext, GPSService::class.java))
        }

        stopTrackingButton.setOnClickListener {
            stopService(Intent(applicationContext, GPSService::class.java))
        }

        newIncidentButton.setOnClickListener {
            startActivity(Intent(this, NewIncidentActivity::class.java))
        }
    }

    private fun openUserForm() {
        startActivity(Intent(this, NewUserActivity::class.java))
    }

    private fun hasToAskPermissions(): Boolean {
        if(Build.VERSION.SDK_INT >= 23 &&
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
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
            if(grantResults.all { it == PackageManager.PERMISSION_GRANTED }){
                enableButtons()
            } else {
                hasToAskPermissions()
            }
        }
    }
}
