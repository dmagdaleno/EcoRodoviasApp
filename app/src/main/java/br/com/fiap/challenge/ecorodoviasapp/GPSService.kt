package br.com.fiap.challenge.ecorodoviasapp

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings

class GPSService: Service() {

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    val newIntent = Intent("location_update").apply {
                        putExtra("coordinates", "${it.longitude} ${it.latitude}")
                    }
                    sendBroadcast(newIntent)
                }
            }

            override fun onStatusChanged(s: String?, p1: Int, bundle: Bundle?) { }

            override fun onProviderEnabled(s: String?) { }

            override fun onProviderDisabled(provider: String?) {
                val newIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(newIntent)
            }
        }

        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val intervalInMillis: Long = 3000
        val intervalInMeters = 0.0f

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            intervalInMillis, intervalInMeters,
            locationListener)


        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            intervalInMillis, intervalInMeters,
            locationListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(::locationManager.isInitialized){
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener)
        }
    }
}