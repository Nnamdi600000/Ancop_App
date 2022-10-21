package com.codennamdi.ancopapp.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Login"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        setUpClickListeners()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun setUpClickListeners() {
        binding.textViewCreateOne.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginActivityLogin.setOnClickListener {
            loginRegisteredUser()
        }
    }

    private fun validateLoginForm(fullName: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(fullName) -> {
                showErrorSnackBar("Please enter an email")
                return false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                return false
            }

            else -> {
                return true
            }
        }
    }

    private fun loginRegisteredUser() {
        val email = binding.textFieldLoginEmail.text.toString().trim { it <= ' ' }
        val password = binding.textFieldLoginPassword.text.toString().trim { it <= ' ' }

        if (validateLoginForm(email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        hideProgressDialog()
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Logged in", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        hideProgressDialog()
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}