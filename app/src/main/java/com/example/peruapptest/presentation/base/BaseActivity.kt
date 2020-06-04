package com.example.peruapptest.presentation.base

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.peruapptest.R
import com.example.peruapptest.util.Constants
import com.example.peruapptest.util.createImageFile
import com.example.peruapptest.util.verificarPermisos
import com.example.peruapptest.util.verificarPermisosExternalStorage
import java.io.File
import java.io.IOException

abstract class BaseActivity : AppCompatActivity() {

    val fileProvier = Constants.FILE_PROVIDER
    var uriExterno: Uri? = null
    var imagenActualUri: Uri? = null
    var photoURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        ocultarTeclado()
        inicializar()
    }

    private fun ocultarTeclado() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    abstract fun getLayout(): Int

    abstract fun inicializar()

    fun mostrarOpcionesObtenerFoto(): Boolean {
        val alertDialog = AlertDialog.Builder(this, R.style.dialog)
            .setTitle("SELECCIONAR")
            .setItems(R.array.select_documento, dialogListener)
            .create()
        alertDialog.show()
        return true
    }

    val dialogListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            0 -> permisos()
            //1 -> permisosExternalStorage()
        }
    }

    private fun permisos() {
        if(baseContext.verificarPermisos()) {
            activarCamara()
        } else {
            solicitudDePermisos()
        }
    }

    private fun solicitudDePermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return }
        requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.PERMISSION_CAMERA)
    }

    private fun permisosExternalStorage() {
        if(baseContext.verificarPermisosExternalStorage()) {
            activarGaleria()
        } else {
            solicitudDePermisosReadWhitePermission()
        }
    }

    private fun solicitudDePermisosReadWhitePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { return }
        requestPermissions(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE),
            Constants.PERMISSION_WRITE_READ_EXTERNAL_STORAGE
        )
    }

    fun activarGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE)
    }

    fun activarCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = baseContext.createImageFile()
                uriExterno = photoFile.absolutePath.toUri()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                    fileProvier,
                    photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    val resInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                }
                startActivityForResult(intent, Constants.CAMERA_REQUEST_CODE)
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    activarCamara()
                }
                return
            }
            Constants.PERMISSION_WRITE_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    activarGaleria()
                }
                return
            }
        }
    }

    fun deleteImage() {
        if (photoURI == null) return
        val image = File(photoURI?.path)
        if (image.exists())
            image.delete()

        if (imagenActualUri == null) return
        val imageExter = File(imagenActualUri?.path)
        if (imageExter.exists())
            imageExter.delete()

    }

}