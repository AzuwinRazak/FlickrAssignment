package com.azuwinrazak.flickrassignment.android.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import kotlinx.coroutines.Job
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun SaveMediaToStorage(bitmap: Bitmap?, context: Context, function: () -> Job) {
    val filename = "${System.currentTimeMillis()}.jpg"
    var fos: OutputStream? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
    }
    fos?.use {
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        //Toast.makeText(context , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
    }
}

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}