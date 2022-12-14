package com.codennamdi.ancopapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityRegisterBinding
import com.codennamdi.ancopapp.firebase.FireStoreClass
import com.codennamdi.ancopapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Register"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.textViewLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegisterActivityRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun validateForm(fullName: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(fullName) -> {
                showErrorSnackBar("Please enter a name")
                return false
            }

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                return false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                return false
            }

            else -> {
                true
            }
        }
    }

    private fun registerUser() {
        val fullName = binding.textFieldFullName.text.toString().trim { it <= ' ' }
        val email = binding.textFieldEmail.text.toString().trim { it <= ' ' }
        val password = binding.textFieldPassword.text.toString().trim { it <= ' ' }

        if (validateForm(fullName, email, password)) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid, fullName, registeredEmail)
                        FireStoreClass().registerUser(this@RegisterActivity, user)
                        // finish()
                    } else {
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        hideProgressDialog()
                    }
                }
        }
    }

    fun userRegisteredSuccessful() {
        Toast.makeText(this@RegisterActivity, "You have registered successfully", Toast.LENGTH_LONG)
            .show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}