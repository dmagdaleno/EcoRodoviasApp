package br.com.fiap.challenge.ecorodoviasapp.service

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
import android.util.Log
import android.widget.Toast
import br.com.fiap.challenge.ecorodoviasapp.NewIncidentActivity
import br.com.fiap.challenge.ecorodoviasapp.domain.Position
import br.com.fiap.challenge.ecorodoviasapp.domain.User
import br.com.fiap.challenge.ecorodoviasapp.domain.UserLocation
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_NAME
import br.com.fiap.challenge.ecorodoviasapp.util.USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.USER_NAME
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GPSService: Service() {

    companion object {
        private const val TAG = "GPSService"
    }

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager

    private val service: LocationService? = EcoviasService().create(LocationService::class.java)

    private lateinit var user: User

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        user = getUser()

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    val position = Position(latitude = it.latitude, longitude = it.longitude)
                    val userLocation = UserLocation(user = user, position = position)
                    if (service != null) {
                        save(service, userLocation)
                    }
                }
            }

            private fun save(service: LocationService, userLocation: UserLocation) {
                service.save(userLocation).enqueue(object: Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e(TAG, "Error saving location", t)
                    }

                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.d(TAG, "Location saved: $userLocation")
                    }
                })
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

    private fun getUser(): User {
        val uuid = getUserId()

        val name = getUserName()

        return User(id = uuid, name = name)
    }

    private fun getUserName() =
        getSharedPreferences(PREF_USER_NAME, Context.MODE_PRIVATE)
            .getString(USER_NAME, "") ?: ""

    private fun getUserId() =
        getSharedPreferences(PREF_USER_ID, Context.MODE_PRIVATE)
            .getString(USER_ID, "") ?: ""
}