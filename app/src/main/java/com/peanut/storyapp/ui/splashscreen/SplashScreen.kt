package com.peanut.storyapp.ui.splashscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.peanut.storyapp.R
import com.peanut.storyapp.data.local.datastore.UserPreferences
import com.peanut.storyapp.ui.login.LoginActivity
import com.peanut.storyapp.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class SplashScreen : AppCompatActivity() {
    private val splashScreenViewModel: SplashScreenViewModel by viewModels {
        SplashScreenViewModel.SplashModelFactory.getInstance(
            UserPreferences.getInstance(dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        isTokenAvailable()
    }

    private fun isTokenAvailable() {
        splashScreenViewModel.getToken().observe(this){
            if (it == ""){
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}