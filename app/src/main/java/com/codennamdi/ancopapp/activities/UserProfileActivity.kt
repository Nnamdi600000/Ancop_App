package com.codennamdi.ancopapp.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.codennamdi.ancopapp.R
import com.codennamdi.ancopapp.databinding.ActivityUserProfileBinding
import com.codennamdi.ancopapp.firebase.FireStoreClass
import com.codennamdi.ancopapp.models.User
import com.codennamdi.ancopapp.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class UserProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private var profileImageResultData: Uri? = null
    private lateinit var userDetails: User
    private var profileImageUrl: String = ""

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                profileImageResultData = result.data?.data!!
                Log.e("Saved image", "$profileImageResultData")
                try {
                    Glide
                        .with(this@UserProfileActivity)
                        .load(profileImageResultData)
                        .centerCrop()
                        .placeholder(R.drawable.profile_place_holder)
                        .into(binding.ivProfileImageId)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private var getAction =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val profileImageFromCamera = it!!.data!!.extras!!.get("data") as Bitmap
                profileImageResultData = saveImageToInternalStorage(profileImageFromCamera)
                Log.e("Saved Image : ", "Path :: $profileImageResultData")
                saveCameraPhotoToStorage()

                try {
                    Glide
                        .with(this@UserProfileActivity)
                        .load(profileImageUrl)
                        .centerCrop()
                        .placeholder(R.drawable.profile_place_holder)
                        .into(binding.ivProfileImageId)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

    private fun saveCameraPhotoToStorage() {
        showProgressDialog(getString(R.string.please_wait))
        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                    profileImageResultData
                )
            )
        profileImageResultData?.let {
            sRef.putFile(it).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase image url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                    Log.e(
                        "Downloaded image url", url.toString()
                    )
                    profileImageUrl = url.toString()
                    hideProgressDialog()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this@UserProfileActivity, exception.message, Toast.LENGTH_LONG)
                    .show()
                hideProgressDialog()
            }
        }
    }


    companion object {
        private const val IMAGE_DIRECTORY = "AncopAppImages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.profile)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        setUpClickListeners()

        FireStoreClass().loadUserData(this@UserProfileActivity)
    }

    private fun setUpClickListeners() {
        binding.addNewProfileImageId.setOnClickListener {
            showActionDialog()
        }

        binding.saveUpdatedProfileDetailsBtn.setOnClickListener {
            if (profileImageResultData != null) {
                uploadUserImage()
            } else {
                showProgressDialog(getString(R.string.please_wait))
                updateUserProfileDetails()
            }
        }
    }

    private fun showActionDialog() {
        val actionDialog = AlertDialog.Builder(this)
        actionDialog.setTitle("Choose")
        val actionItems =
            arrayOf("Select from Gallery", "Use phone camera")

        actionDialog.setItems(actionItems) { _, which ->
            when (which) {
                0 -> {
                    choosePhotoFromGallery()
                }

                1 -> {
                    launchTheCameraApp()
                }
            }
        }
        actionDialog.show()
    }

    private fun displayRationalDialog() {
        AlertDialog.Builder(this@UserProfileActivity)
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

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@UserProfileActivity)
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

    private fun launchTheCameraApp() {
        Dexter.withContext(this@UserProfileActivity)
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        getAction.launch(intent)
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

    //Function for saving image as bitmap to device.
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    fun setUserDataInUi(user: User) {
        userDetails = user

        Glide
            .with(this@UserProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.profile_place_holder)
            .into(binding.ivProfileImageId)

        binding.textFieldUsernameProfile.setText(user.name)
        binding.textFieldEmailProfile.setText(user.email)

        if (user.mobileNumber != 0L && user.churchPosition != "") {
            binding.textFieldMobileNumberProfile.setText(user.mobileNumber.toString())
            binding.textFieldChurchPositionProfile.setText(user.churchPosition)
        }
    }

    private fun uploadUserImage() {
        showProgressDialog(getString(R.string.please_wait))
        val sRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
                    profileImageResultData
                )
            )
        profileImageResultData?.let {
            sRef.putFile(it).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase image url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                    Log.e(
                        "Downloaded image url", url.toString()
                    )
                    profileImageUrl = url.toString()

                    updateUserProfileDetails()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this@UserProfileActivity, exception.message, Toast.LENGTH_LONG)
                    .show()
                hideProgressDialog()
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        var anyChangesMade = false

        if (profileImageUrl.isNotEmpty() && profileImageUrl != userDetails.image) {
            userHashMap[Constants.IMAGE] = profileImageUrl
            anyChangesMade = true
        }

        if (binding.textFieldUsernameProfile.text.toString() != userDetails.name) {
            userHashMap[Constants.NAME] = binding.textFieldUsernameProfile.text.toString()
            anyChangesMade = true
        }

        if (binding.textFieldMobileNumberProfile.text.toString() != userDetails.mobileNumber.toString()) {
            userHashMap[Constants.MOBILE_NUMBER] =
                binding.textFieldMobileNumberProfile.text.toString().toLong()
            anyChangesMade = true
        }

        if (binding.textFieldChurchPositionProfile.text.toString() != userDetails.churchPosition) {
            userHashMap[Constants.CHURCH_POSITION] =
                binding.textFieldChurchPositionProfile.text.toString()
            anyChangesMade = true
        }

        if (anyChangesMade)
            FireStoreClass().updateUserProfileData(this, userHashMap)
        hideProgressDialog()
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    fun profileUpdateSuccess() {
        hideProgressDialog()
        finish()
    }
}