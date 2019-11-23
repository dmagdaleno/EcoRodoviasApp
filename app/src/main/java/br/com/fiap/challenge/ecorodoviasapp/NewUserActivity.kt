package br.com.fiap.challenge.ecorodoviasapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.PREF_USER_NAME
import br.com.fiap.challenge.ecorodoviasapp.util.USER_ID
import br.com.fiap.challenge.ecorodoviasapp.util.USER_NAME
import kotlinx.android.synthetic.main.activity_new_user.*
import java.util.*

class NewUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        userSaveButton.setOnClickListener {
            saveUserId()
            saveUserName()
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

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }
}
