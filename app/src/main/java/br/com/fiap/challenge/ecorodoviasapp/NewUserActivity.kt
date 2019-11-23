package br.com.fiap.challenge.ecorodoviasapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.challenge.ecorodoviasapp.util.*
import kotlinx.android.synthetic.main.activity_new_user.*
import java.util.*

class NewUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        userSaveButton.setOnClickListener {
            saveUserId()
            saveUserName()
            saveCarPlate()
            openDashboard()
        }
    }

    private fun saveUserId() {
        getSharedPreferences(PREF_USER_ID, Context.MODE_PRIVATE)
            .edit()
            .also {
                it.putString(USER_ID, UUID.randomUUID().toString())
            }
            .apply()
    }

    private fun saveUserName() {
        getSharedPreferences(PREF_USER_NAME, Context.MODE_PRIVATE)
            .edit()
            .also {
                it.putString(USER_NAME, userNameEditText.text.toString())
            }
            .apply()
    }

    private fun saveCarPlate() {
        getSharedPreferences(PREF_USER_CAR_PLATE, Context.MODE_PRIVATE)
            .edit()
            .also {
                it.putString(USER_CAR_PLATE, userCarPlateEditText.text.toString())
            }
            .apply()
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}
