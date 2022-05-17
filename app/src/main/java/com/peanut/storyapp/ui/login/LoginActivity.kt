package com.peanut.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.peanut.storyapp.R
import com.peanut.storyapp.data.Result
import com.peanut.storyapp.data.local.datastore.UserPreferences
import com.peanut.storyapp.databinding.ActivityLoginBinding
import com.peanut.storyapp.ui.main.MainActivity
import com.peanut.storyapp.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginModelFactory.getInstance(
            UserPreferences.getInstance(dataStore)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setMyButtonEnable()
        binding.edtTxtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.myButton.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    private fun setMyButtonEnable() {
        val resultEmail= binding.edtTxtEmail.text
        val resultPass = binding.edtTxtPassword.text
            binding.myButton.isEnabled =
                resultEmail != null && resultEmail.toString().isNotEmpty() && isEmailValid(resultEmail.toString())
                && resultPass != null && resultPass.toString().isNotEmpty() && resultPass.toString().length > 6
    }

    private fun isEmailValid(s: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.myButton ->{
                val resultEmail= binding.edtTxtEmail.text
                val resultPass = binding.edtTxtPassword.text
                val result = loginViewModel.loginUser(resultEmail.toString(), resultPass.toString())
                result.observe(this){
                    when (it) {
                        is Result.Loading ->{

                        }
                        is Result.Error ->{
                            /*val data = it.error
                            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()*/
                        }
                        is Result.Success ->{
                            val data = it.data
                            Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                            loginViewModel.saveToken(data.loginResult.token)
                            Log.d("LoginActivity", "Token: ${data.loginResult.token}")
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            R.id.btn_register ->{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}

