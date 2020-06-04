package com.example.peruapptest.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun Context.getRealPathFromUri(contentUri: Uri) : String {
    var cursor: Cursor? = null
    try {
        var proj : Array<String> = arrayOf(MediaStore.MediaColumns.DATA)
        cursor = contentResolver.query(contentUri, proj, null, null, null)
        var column_index : Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return column_index?.let { cursor?.getString(it) }!!
    } finally {
        if (cursor != null) {
            cursor.close()
        }
    }
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "PNG_" + timeStamp + "_"
    val storageDir = File(this.filesDir, "images")

    if (!storageDir.exists()) storageDir.mkdirs()

    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".png", /* suffix */
        storageDir      /* directory */
    )
    return image
}

@Throws(IOException::class)
fun Context.eliminarStorageImagenes() {
    val storageDir = File(this.filesDir, "images")
    if (storageDir.exists()) storageDir.delete()
}