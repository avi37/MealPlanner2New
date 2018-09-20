package com.example.admin.mealplanner2new.Fragments

import android.Manifest
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin.mealplanner2new.R
import kotlinx.android.synthetic.main.fragment_first_photo_upload.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.hasPermissions
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Environment
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.PermissionRequest
import android.os.Environment.DIRECTORY_PICTURES
import android.os.SystemClock
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.content.Intent
import android.graphics.Bitmap
import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.NotificationCompat.getExtras
import com.bumptech.glide.Glide
import com.example.admin.mealplanner2new.Common.RetrofitClient
import com.example.admin.mealplanner2new.Models.ResCommon
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


class FirstPhotoUploadFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    val REQUEST_IMAGE_CAPTURE = 1
    var mCurrentPhotoPath: String? = null
    val IMAGE_DIRECTORY = "HealthBotics"
    val FILE_NAME = "profile"
    lateinit var uploadImageToServer: UploadImageToServer
    private val BASE_URL = "http://code-fuel.in/healthbotics/api/auth/"

    companion object {
        const val RC_WRITE_STORAGE = 108
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {


    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uploadImageToServer = uploadImage(BASE_URL)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_first_photo_upload, container, false)!!
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSelectFile?.setOnClickListener {

            requestPermission()

        }


        btnUpload?.setOnClickListener {

            if (mCurrentPhotoPath != null) {

                val file = File(mCurrentPhotoPath)
                val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
                val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestBody)
                val filename = RequestBody.create(MediaType.parse("text/plain"), file.name)
                val firstTime = RequestBody.create(MediaType.parse("text/plain"), "1")


            }


        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    @AfterPermissionGranted(RC_WRITE_STORAGE)
    private fun requestPermission() {
        val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(activity, *perms)) {
            // Already have permission, do the thing
            dispatchTakePictureIntent()
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    PermissionRequest.Builder(this, RC_WRITE_STORAGE, *perms)
                            .setRationale("Storage permission required")
                            .setPositiveButtonText("OK")
                            .setNegativeButtonText("Cancel")
                            .setTheme(R.style.AppTheme)
                            .build())
        }
    }


    private fun getOutputMediaFile(): File? {

        val mediaStorageDir = File(
                Environment.getExternalStorageDirectory(), IMAGE_DIRECTORY)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY, "failed to create directory")
                return null
            }
        }

        var mediaFile: File? = null
        try {
            mediaFile = File.createTempFile(SystemClock.elapsedRealtime().toString().plus(FILE_NAME), ".jpg", mediaStorageDir)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        mCurrentPhotoPath = mediaFile?.absolutePath
        return mediaFile
    }


    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete()
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {

            var photoFile: File? = null
            try {
                photoFile = getOutputMediaFile()
            } catch (ex: IOException) {


            }

            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity,
                        "com.example.admin.mealplanner2new.fileprovider",
                        photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (mCurrentPhotoPath != null) {

                galleryAddPic()
                setPic()
            }


        }
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        activity.sendBroadcast(mediaScanIntent)
    }


    private fun setPic() {

        Glide.with(activity).load(BitmapFactory.decodeFile(mCurrentPhotoPath)).into(ivProfile)


    }

    interface UploadImageToServer {
        @Multipart
        @POST("member/uploadimageapi/")
        fun uploadFile(@Part file: MultipartBody.Part, @Part("file") name: RequestBody, @Part("status") status: RequestBody,
                       @Part("type") type: RequestBody): Call<ResCommon>
    }

    internal fun uploadImage(baseurl: String): UploadImageToServer {
        return RetrofitClient.getClient(baseurl).create(UploadImageToServer::class.java)
    }

}