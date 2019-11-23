package br.com.fiap.challenge.ecorodoviasapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import br.com.fiap.challenge.ecorodoviasapp.domain.*
import br.com.fiap.challenge.ecorodoviasapp.service.EcoviasService
import br.com.fiap.challenge.ecorodoviasapp.service.IncidentService
import br.com.fiap.challenge.ecorodoviasapp.util.*
import kotlinx.android.synthetic.main.activity_new_incident.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewIncidentActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NewIncidentActivity"
    }

    private lateinit var locationListener: LocationListener
    private lateinit var locationManager: LocationManager
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_incident)

        getLocation()

        val uuid = getUserId()

        val name = getUserName()

        val carPlate = getUserCarPlate()

        incidentSaveButton.setOnClickListener {

            val incident = Incident (
                type = IncidentType.CONFIRMED,
                title = incidentTitleEditText.text.toString(),
                description = incidentDescriptionEditText.text.toString(),
                user = User(id = uuid, name = name, carPlate = carPlate),
                position = Position(latitude = latitude, longitude = longitude)
            )
            Log.d(TAG, "$incident")

            EcoviasService().create(IncidentService::class.java)?.let { service ->
                saveIncident(service, incident)
            }
        }
    }

    private fun saveIncident(service: IncidentService, incident: Incident) {
        service.saveIncident(incident).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Error saving incident", t)
            }

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.d(TAG, "Success: $response")
                val message = "Incidente cadastrado com sucesso"
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                finish()
            }

        })
    }

    private fun getUserId() =
        getSharedPreferences(PREF_USER_ID, Context.MODE_PRIVATE)
            .getString(USER_ID, "") ?: ""

    private fun getUserName() =
        getSharedPreferences(PREF_USER_NAME, Context.MODE_PRIVATE)
            .getString(USER_NAME, "") ?: ""

    private fun getUserCarPlate() =
        getSharedPreferences(PREF_USER_CAR_PLATE, Context.MODE_PRIVATE)
            .getString(USER_CAR_PLATE, "") ?: ""

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    locationManager.removeUpdates(this)
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
        val intervalInMillis: Long = 0
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
