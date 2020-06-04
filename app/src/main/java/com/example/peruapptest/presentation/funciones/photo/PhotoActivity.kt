package com.example.peruapptest.presentation.funciones.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.example.peruapptest.R
import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.presentation.base.BaseActivity
import com.example.peruapptest.util.Constants
import com.example.peruapptest.util.cargarImagen
import com.example.peruapptest.util.getRealPathFromUri
import com.example.peruapptest.util.verificarSiExiste
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.photo_activity.*
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoActivity : BaseActivity(), PhotoMVP.View {

    private var mStorageReference: StorageReference? = null
    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabaseUsers : DatabaseReference
    lateinit var mDatabaseFeed : DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    var userid: String = ""

    private val presenter: PhotoMVP.Presenter by inject()

    override fun getLayout(): Int = R.layout.photo_activity

    override fun inicializar() {
        inicializarPresenter()
        inicializarFirebase()
        inicializarEventos()
    }

    private fun inicializarPresenter() {
        presenter?.setView(this)
    }

    private fun inicializarFirebase() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        userid = firebaseUser.uid
        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users")
        mDatabaseFeed = FirebaseDatabase.getInstance().getReference("Feed")
        mDatabase = mDatabaseUsers.child(firebaseUser.uid)
    }

    private fun inicializarEventos() {
        bt_foto.setOnClickListener {
            mostrarOpcionesObtenerFoto()
        }

        bt_enviar.setOnClickListener {
            if(tv_descripcion_place.text.isNotEmpty()){
                guardarImagen()
            } else {
                Toast.makeText(this, "Escriba una descripcion", Toast.LENGTH_LONG).show()
            }
        }
        bt_eliminar.setOnClickListener {
            eliminarFoto()
        }
    }

    private fun eliminarFoto() {
        var image = tv_url.text.toString()
        if (!(image.isNullOrEmpty())) {
            imagenActualUri = Uri.parse(image)
            var file = File(imagenActualUri?.path)
            if (file.exists())
                file.delete()
            deleteImage()
            tv_url.text = ""
            iv_foto.cargarImagen("", R.drawable.ic_upload_image, R.drawable.ic_upload_image)
        }
    }

    private fun guardarImagen() {
        photoURI?.let { it ->
            guardarLocalPlace(it.toString())
        }
    }

    fun obtenerPlace(url: String) : PlaceEntity {
        var place = PlaceEntity()
        place.idFirebase = userid
        place.descripcionLugar = tv_descripcion_place.text.toString()
        place.imageURL = url
        place.createdAt = obtenerFechaActual()
        return place
    }

    private fun guardarLocalPlace(downloadUri: String) {
        presenter?.guardarRoom(obtenerPlace(downloadUri))
    }

    fun obtenerFechaActual() : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val fecha: String = dateFormat.format(date)
        return fecha
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            Constants.CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        deleteImage()
                        tv_url.text = uriExterno?.path.toString()

                        photoURI?.let {

                            var uriFile = it

                            if (Build.VERSION.SDK_INT < 28) {

                                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uriFile)
                                iv_foto.visibility = View.VISIBLE
                                iv_foto.setImageBitmap(bitmap)

                            } else {

                                var file = File(uriFile.path.toString())
                                file.verificarSiExiste()

                                uriFile.let { it1 ->
                                    iv_foto.visibility = View.VISIBLE
                                    iv_foto.cargarImagen(it1,
                                        R.drawable.ic_upload_image,
                                        R.drawable.ic_upload_image
                                    )
                                }

                            }

                        }
                    } catch (ex: Exception) {

                    }

                }
            }
            Constants.GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        deleteImage()
                        var selectedImage : Uri? = data?.data
                        var mPhotoFile = Compressor(this).compressToFile(File(selectedImage?.let { baseContext.getRealPathFromUri(it) }))
                        tv_url.text = mPhotoFile?.path.toString()

                        var uriFile = mPhotoFile.toUri()

                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                this.contentResolver,
                                uriFile
                            )
                            iv_foto.visibility = View.VISIBLE
                            iv_foto.setImageBitmap(bitmap)

                        } else {

                            var file = File(uriFile.path.toString())
                            file.verificarSiExiste()

                            uriFile.let {
                                iv_foto.visibility = View.VISIBLE
                                iv_foto.cargarImagen(it.toString(),
                                    R.drawable.ic_upload_image,
                                    R.drawable.ic_upload_image
                                )
                            }

                        }


                    } catch (ex: Exception) {

                    }
                }
            }

        }
    }

    override fun iniciarCargar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun finalizarCarga() {
        progressBar.visibility = View.GONE
    }

    override fun mostrarMensajeDeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarMensajeExitoso(mensaje: String) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        presenter.setView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.setView(null)
    }


}