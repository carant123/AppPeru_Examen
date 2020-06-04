package com.example.peruapptest.util

import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.peruapptest.R
import java.io.File

fun ImageView.cargarImagen(ruta: Any, placeholer: Int = -1, error: Int = -1) {
    var glide = Glide.with(context)
    if (placeholer != -1 && error != -1) {
        glide = glide.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_upload_image)
                .error(R.drawable.ic_upload_image))
    }

    if(ruta is Uri || ruta is String || ruta is File){
        glide.load(ruta)
            .override(100, 200)
            .thumbnail(0.1f)
            .into(this)
    }

}

fun File.verificarSiExiste() : Boolean {

    var fileExists = this.exists()
    if(fileExists){
        Log.d("FileStatus: ","Archivo Existe")
        val fileSize = this.length()
        val sizeInMb = fileSize / (1024.0 * 1024)
        val sizeInMbStr = "%.2f".format(sizeInMb)

        Log.d("FileSize: ", "Size=${sizeInMbStr}MB")
    } else {
        Log.d("FileStatus: ","Archivo no Existe")
    }
    return fileExists

}



