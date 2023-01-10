package com.codennamdi.ancop_admin.activities

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codennamdi.ancop_admin.R
import com.codennamdi.ancop_admin.databinding.ActivityAdminRegisterBinding
import com.codennamdi.ancop_admin.firestore.FireStoreClass
import com.codennamdi.ancop_admin.model.UserAdmin
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdminRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRegisterBinding
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpOnClickListener()
    }

    private fun setUpOnClickListener() {
        binding.btnRegisterAdmin.setOnClickListener {
            registerAdmin()
        }
    }

    private fun validateRegisterForm(fullName: String, eMail: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(fullName) -> {
                showErrorSnackBar("Please enter your full name")
                return false
            }
            TextUtils.isEmpty(eMail) -> {
                showErrorSnackBar("Please enter your email")
                return false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter your password")
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun registerAdmin() {
        val fullName = binding.textFieldFullName.text.toString().trim { it <= ' ' }
        val eMail = binding.textFieldEmail.text.toString().trim { it <= ' ' }
        val password = binding.textFieldPassword.text.toString().trim { it <= ' ' }

        if (validateRegisterForm(fullName, eMail, password)) {
            showProgressDialog("Please wait...")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(eMail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val fireBaseUser: FirebaseUser = task.result!!.user!!
                        val registeredEmail = fireBaseUser.email!!
                        val user = UserAdmin(fireBaseUser.uid, fullName, registeredEmail)
                        FireStoreClass().registerAdminUser(this@AdminRegisterActivity, user)
                    } else {
                        Toast.makeText(
                            this@AdminRegisterActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun adminRegisteredSuccessful() {
        Toast.makeText(
            this@AdminRegisterActivity,
            "Admin Registered Successfully",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AdminRegisterActivity,
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