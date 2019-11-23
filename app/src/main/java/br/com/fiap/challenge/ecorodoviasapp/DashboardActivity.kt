package br.com.fiap.challenge.ecorodoviasapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_NAME
import br.com.fiap.challenge.ecorodoviasapp.util.USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.USER_NAME

class DashboardActivity : AppCompatActivity() {

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
    }

    private fun openUserForm() {
        startActivity(Intent(this, NewUserActivity::class.java))
    }
}
