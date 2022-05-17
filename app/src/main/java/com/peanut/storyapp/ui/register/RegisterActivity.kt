package com.peanut.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.peanut.storyapp.R
import com.peanut.storyapp.data.Result
import com.peanut.storyapp.databinding.ActivityRegisterBinding
import com.peanut.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        RegisterViewModel.RegisterViewModelFactory.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
    }

    private fun setMyButtonEnable() {
         val resultName = binding.edtTxtName.text
         val resultEmail= binding.edtTxtEmail.text
         val resultPass = binding.edtTxtPassword.text
        binding.myButton.isEnabled = resultName != null && resultName.toString().isNotEmpty() &&
            resultEmail != null && resultEmail.toString().isNotEmpty() && isEmailValid(resultEmail.toString())
                    && resultPass != null && resultPass.toString().isNotEmpty() && resultPass.toString().length > 6
    }

    private fun isEmailValid(s: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(s).matches()
    }

    override fun onClick(p0: View?) {
        val resultName = binding.edtTxtName.text
        val resultEmail= binding.edtTxtEmail.text
        val resultPass = binding.edtTxtPassword.text
        val result = registerViewModel.registerUser(resultName.toString(), resultEmail.toString(), resultPass.toString())
        result.observe(this){
            when (it) {
                is Result.Loading -> {

                }
                is Result.Error -> {
                    val error = it.error
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }

                is Result.Success -> {
                    val message = it.data.message
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                 /*   val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)*/
                }

            }
        }
    }
}