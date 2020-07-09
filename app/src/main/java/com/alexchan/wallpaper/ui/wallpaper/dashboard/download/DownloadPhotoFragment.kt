package com.alexchan.wallpaper.ui.wallpaper.dashboard.download

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.databinding.FragmentDownloadPhotoBinding
import com.alexchan.wallpaper.util.TAG
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference


class DownloadPhotoFragment : BottomSheetDialogFragment() {

    private val READ_WRITE_REQUEST_CODE = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentDownloadPhotoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Bind data from bundle to layout data variable
        val userSelectedPhoto = DownloadPhotoFragmentArgs.fromBundle(requireArguments()).userSelectedPhoto
        binding.photoModel = userSelectedPhoto

        val username = userSelectedPhoto.user?.name
        val photoId = userSelectedPhoto.id
        val photoUrl = userSelectedPhoto.photoUrl.raw

        // Handle download photo button on click listener
        binding.downloadPhotoButton.setOnClickListener {downloadPhoto(username, photoId, photoUrl)}

        // Check if photo is downloaded
        // If it is, hide download button and
        // allow user to open downloaded photo in gallery
        if (file(username, photoId).exists()) {
            binding.downloadPhotoButton.visibility = View.GONE
            binding.openDownloadedPhotoButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {openDownloadedPhoto(username, photoId)}
            }
            binding.deleteDownloadedPhotoButton.apply {
                visibility = View.VISIBLE
                setOnClickListener {deleteDownloadedPhoto(username, photoId)}
            }
        }
        return binding.root
    }

    private fun file(username: String?, photoId: String?): File {
        val path = requireContext().getExternalFilesDir(null)
        return File(path, "$username-$photoId.jpg")
    }

    private fun downloadPhoto(username: String?, photoId: String?, photoUrl: String) {
        requestAppPermissions()

        val file = file(username, photoId)
        if (file.exists()) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.photo_downloaded, file), Toast.LENGTH_LONG).show()
        } else {
            DownloadAndSaveImageTask(requireContext(), username, photoId)
                .execute(photoUrl)
        }
        requireActivity().onBackPressed()
    }

    private fun openDownloadedPhoto(username: String?, photoId: String?) {
        val file = file(username, photoId)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        val uri = FileProvider.getUriForFile(requireContext(), "${this.requireContext().packageName}.provider", file)
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun deleteDownloadedPhoto(username: String?, photoId: String?) {
        if (file(username, photoId).exists()) {
            file(username, photoId).delete()
            requireActivity().onBackPressed()
        }
    }

    // Request App Permission
    private fun requestAppPermissions() {
        if (hasReadPermissions() && hasWritePermissions()) {
            return
        }

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), READ_WRITE_REQUEST_CODE
        ) // READ_WRITE_REQUEST_CODE
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    class DownloadAndSaveImageTask(context: Context, username: String?, photoId: String?) : AsyncTask<String, Unit, Unit>() {

        private var mContext: WeakReference<Context> = WeakReference(context)
        private var mUsername: String? = username
        private var mPhotoId: String? = photoId
        private var context: Context = context
        private lateinit var filePath: File

        override fun doInBackground(vararg params: String?) {
            val url = params[0]
            val requestOptions = RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

            mContext.get()?.let {
                val bitmap = Glide.with(it)
                    .asBitmap()
                    .load(url)
                    .apply(requestOptions)
                    .submit()
                    .get()

                try {
                    // Save image to external storage
                    val path = it.getExternalFilesDir(null)

                    // Create a file to save the image
                    val file = File(path, "$mUsername-$mPhotoId.jpg")
                    filePath = file

                    val out = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                    Log.i(TAG, "Image successfully saved!")
                } catch (e: Exception) {
                    Log.i(TAG, "Failed to save Image!")
                }
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(context, context.getString(R.string.photo_downloading), Toast.LENGTH_SHORT).show()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            Toast.makeText(context, context.getString(R.string.photo_download_successful, filePath), Toast.LENGTH_LONG).show()
        }
    }
}
