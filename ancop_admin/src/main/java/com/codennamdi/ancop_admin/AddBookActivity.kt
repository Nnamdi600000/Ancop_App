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
import com.codennamdi.ancop_admin.databinding.ActivityAddBookBinding
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private var bookImageResultData: Uri? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                bookImageResultData = result.data?.data!!
                Log.e("Saved image", "$bookImageResultData")
                try {
                    Glide
                        .with(this@AddBookActivity)
                        .load(bookImageResultData)
                        .centerCrop()
                        .placeholder(R.drawable.profile_place_holder)
                        .into(binding.bookImageImageViewId)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBookImageBtnId.setOnClickListener {
            choosePhotoFromGallery()
        }

        binding.addBookBtn.setOnClickListener {
            addLeaderDetailsToFirebase()
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@AddBookActivity)
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
        AlertDialog.Builder(this@AddBookActivity)
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

    private fun validateEventDetailsField(): Boolean {
        return when {
            (bookImageResultData == null) -> {
                showErrorSnackBar("Please select an image from your gallery")
                return false
            }

            TextUtils.isEmpty(binding.textFieldBookTitleId.toString()) -> {
                showErrorSnackBar("Please enter a book title")
                return false
            }

            else -> {
                return true
            }
        }
    }

    private fun addLeaderDetailsToFirebase() {
        val bookTitle = binding.textFieldBookTitleId.text.toString().trim { it <= ' ' }

        if (validateEventDetailsField()) {
            showErrorSnackBarGreen("Added details")
        }
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this@AddBookActivity,
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
                this@AddBookActivity,
                R.color.green
            )
        )
        snackBar.show()
    }
}