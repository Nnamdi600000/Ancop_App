package com.codennamdi.ancop_admin.activities

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codennamdi.ancop_admin.R
import com.codennamdi.ancop_admin.databinding.ActivityMainBinding
import com.codennamdi.ancop_admin.firestore.FireStoreClass
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressDialog: Dialog
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForUserAuthentication()
        setUpClickListeners()
        mAuth = FirebaseAuth.getInstance()
    }

    private fun checkForUserAuthentication() {
        val currentAdminId = FireStoreClass().getCurrentUserID()
        if (currentAdminId.isNotBlank()) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun setUpClickListeners() {
        binding.textViewCreateOne.setOnClickListener {
            startActivity(Intent(this@MainActivity, AdminRegisterActivity::class.java))
        }

        binding.btnLoginAdmin.setOnClickListener {
            logInAdmin()
        }
    }

    private fun validateLoginFormAdmin(eMail: String, passWord: String): Boolean {
        return when {
            TextUtils.isEmpty(eMail) -> {
                showErrorSnackBar("Please enter an email")
                return false
            }
            TextUtils.isEmpty(passWord) -> {
                showErrorSnackBar("Please enter a password")
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun logInAdmin() {
        val eMail = binding.textFieldLoginEmail.text.toString().trim { it <= ' ' }
        val passWord = binding.textFieldLoginPassword.text.toString().trim { it <= ' ' }

        if (validateLoginFormAdmin(eMail, passWord)) {
            showProgressDialog("Please wait...")
            mAuth.signInWithEmailAndPassword(eMail, passWord)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        hideProgressDialog()
                        FireStoreClass().loadAdminData(this@MainActivity)
                    } else {
                        hideProgressDialog()
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun logInAdminSuccessFul() {
        Toast.makeText(this@MainActivity, "Logged in", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        finish()
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@MainActivity,
                R.color.red
            )
        )
        snackBar.show()
    }

    private fun showProgressDialog(text: String) {
        progressDialog = Dialog(this)

        progressDialog.setContentView(R.layout.activity_progress_bar)

        progressDialog.findViewById<TextView>(R.id.text_view_please_wait).text = text

        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.dismiss()
    }
}