package com.alexchan.wallpaper.ui.wallpaper.dashboard.download

import android.Manifest
import android.content.Context
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
import com.alexchan.wallpaper.R
import com.alexchan.wallpaper.databinding.FragmentDownloadPhotoBinding
import com.alexchan.wallpaper.model.unsplash.Photo
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
        binding.photoModel = DownloadPhotoFragmentArgs.fromBundle(requireArguments()).userSelectedPhoto

        // Handle download photo button on click listener
        binding.downloadPhotoButton.setOnClickListener {downloadPhoto()}

        return binding.root
    }

    private fun downloadPhoto() {
        requestAppPermissions()

        val selectedPhoto = DownloadPhotoFragmentArgs.fromBundle(requireArguments())
        val path = requireContext().getExternalFilesDir(null)
        val file = File(path, "${selectedPhoto.userSelectedPhoto.user?.name}-${selectedPhoto.userSelectedPhoto.id}.jpg")

        if (file.exists()) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.photo_downloaded, file), Toast.LENGTH_LONG).show()
        } else {
            DownloadAndSaveImageTask(requireContext(), selectedPhoto.userSelectedPhoto)
                .execute(selectedPhoto.userSelectedPhoto.photoUrl.raw)
        }
        requireActivity().onBackPressed()
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

    class DownloadAndSaveImageTask(context: Context, userSelectedPhoto: Photo) : AsyncTask<String, Unit, Unit>() {

        private var mContext: WeakReference<Context> = WeakReference(context)
        private var mUserSelectedPhoto: Photo = userSelectedPhoto
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
                    val file = File(path, "${mUserSelectedPhoto.user?.name}-${mUserSelectedPhoto.id}.jpg")
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
