package com.codennamdi.ancop_admin

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.codennamdi.ancop_admin.databinding.ActivityAddEventBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding
    private var eventImageResultData: Uri? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                eventImageResultData = result.data?.data!!
                Log.e("Saved image", "$eventImageResultData")
                try {
                    Glide
                        .with(this@AddEventActivity)
                        .load(eventImageResultData)
                        .centerCrop()
                        .placeholder(R.drawable.profile_place_holder)
                        .into(binding.eventImageImageViewId)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addEventImageBtn.setOnClickListener {
            choosePhotoFromGallery()
        }

        binding.addEventDetailsBtn.setOnClickListener {
            addEventDetailsToFirebase()
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@AddEventActivity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val pickIntent =
                            Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            )
                        openGalleryLauncher.launch(pickIntent)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>,
                    token: PermissionToken
                ) {
                    displayRationalDialog()
                }
            }).onSameThread().check()
    }

    private fun displayRationalDialog() {
        AlertDialog.Builder(this@AddEventActivity)
            .setMessage("You can't access the gallery because you did not enable the permission, You can do that by heading to the app settings.")

            .setPositiveButton("GOTO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun validateEventDetailsField(eventDetails: String): Boolean {
        return when {
            (eventImageResultData == null) -> {
                showErrorSnackBar("Please select an image from your gallery")
                return false
            }

            TextUtils.isEmpty(eventDetails) -> {
                showErrorSnackBar("Please enter your event details")
                return false
            }

            else -> {
                return true
            }
        }
    }

    private fun addEventDetailsToFirebase() {
        val textEventDetails = binding.textFieldEventDetails.text.toString().trim { it <= ' ' }

        if (validateEventDetailsField(textEventDetails)) {
            showErrorSnackBarGreen("Added details")
        }
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AddEventActivity,
                R.color.red
            )
        )
        snackBar.show()
    }

    private fun showErrorSnackBarGreen(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AddEventActivity,
                R.color.green
            )
        )
        snackBar.show()
    }
}