package com.codennamdi.ancop_admin.activities

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.codennamdi.ancop_admin.R
import com.codennamdi.ancop_admin.databinding.ActivityAddEventBinding
import com.codennamdi.ancop_admin.firestore.FireStoreClass
import com.codennamdi.ancop_admin.model.Event
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException

class AddEventActivity : AppCompatActivity() {
    private lateinit var progressDialog: Dialog
    private lateinit var binding: ActivityAddEventBinding
    private var eventImageResultData: Uri? = null
    private var eventImageUrl: String = ""

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                eventImageResultData = result.data?.data!!
                Log.e("Saved image", "$eventImageResultData")
                //uploadEventImageToStorage()
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
                showErrorSnackBarRed("Please select an image from your gallery")
                return false
            }

            TextUtils.isEmpty(eventDetails) -> {
                showErrorSnackBarRed("Please enter your event details")
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

    //Function to upload the event image to firebase storage.
    private fun uploadEventImageToStorage() {
        showProgressDialog(getString(R.string.please_wait))

        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "EVENT_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                    eventImageResultData
                )
            )

        eventImageResultData?.let {
            sRef.putFile(it).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase eImage url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                    Log.e(
                        "Downloaded eImage url", url.toString()
                    )
                    eventImageUrl = url.toString()
                    hideProgressDialog()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this@AddEventActivity, exception.message, Toast.LENGTH_LONG)
                    .show()
                hideProgressDialog()
            }
        }
    }

    private fun uploadEventDetails() {
        val event = Event(
            FireStoreClass().getCurrentUserID(),

        )
    }

    private fun showErrorSnackBarRed(message: String) {
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

    private fun showProgressDialog(text: String) {
        progressDialog = Dialog(this)

        progressDialog.setContentView(R.layout.activity_progress_dialog)

        progressDialog.findViewById<TextView>(R.id.text_view_please_wait).text = text

        progressDialog.show()
    }

    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    fun eventUploadedSuccessful() {
        hideProgressDialog()
        Toast.makeText(
            this@AddEventActivity,
            getString(R.string.upload_event_successful),
            Toast.LENGTH_LONG
        ).show()
        finish()
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }
}