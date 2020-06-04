package com.example.peruapptest.presentation.funciones.localPlaces

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peruapptest.R
import com.example.peruapptest.data.room.model.PlaceEntity
import com.example.peruapptest.presentation.adapter.LocalPlacesAdapter
import com.example.peruapptest.presentation.base.BaseActivity
import com.example.peruapptest.presentation.funciones.photo.PhotoMVP
import com.example.peruapptest.util.verificarSiExiste
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.photo_activity.*
import kotlinx.android.synthetic.main.places_local_activity.*
import kotlinx.android.synthetic.main.places_local_activity.progressBar
import org.koin.android.ext.android.inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LocalPlacesActivity: BaseActivity(), LocalPlacesMVP.View, LocalPlacesAdapter.eventoCompartir {

    private val presenter: LocalPlacesMVP.Presenter by inject()
    lateinit var adapter: LocalPlacesAdapter

    private var mStorageReference: StorageReference? = null
    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabaseUsers : DatabaseReference
    lateinit var mDatabaseFeed : DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    var userid: String = ""

    override fun getLayout(): Int = R.layout.places_local_activity

    override fun inicializar() {
        presenter?.setView(this)
        inicializarFirebase()
    }

    private fun inicializarFirebase() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        userid = firebaseUser.uid
        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users")
        mDatabaseFeed = FirebaseDatabase.getInstance().getReference("Feed")
        mDatabase = mDatabaseUsers.child(firebaseUser.uid)

        presenter?.obtenerListaPlacesLocales(firebaseUser.uid)
    }

    override fun mostrarPlacesListaLocal(lista: List<PlaceEntity>) {
        lista?.let { inicializarAdapter(it) }
    }

    private fun inicializarAdapter(listaItems:  List<PlaceEntity>) {
        adapter = LocalPlacesAdapter(this, listaItems)
        adapter?.listener = this
        recycler_view?.isNestedScrollingEnabled = false
        recycler_view?.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recycler_view?.adapter = adapter
        recycler_view?.hasFixedSize()
        recycler_view?.visibility = if(listaItems.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun compartir(entity: PlaceEntity) {
        compartirFotosLocales(entity)
    }

    override fun eliminar(entity: PlaceEntity) {
        eliminarFotoLocal(entity)
        presenter?.eliminarPlaceLocal(entity)
    }

    private fun eliminarFotoLocal(entity: PlaceEntity) {
        if (!(entity.imageURL.isNullOrEmpty())) {
            imagenActualUri = Uri.parse(entity.imageURL)
            var file = File(imagenActualUri?.path)
            if (file.exists())
                file.delete()
        }
    }

    private fun compartirFotosLocales(entity: PlaceEntity) {

        var descripcionLugar = entity.descripcionLugar.orEmpty()

            var urlUri = Uri.parse(entity.imageURL)
            urlUri?.let { it1 ->
                iniciarCargar()
                var direccionImagenChild =
                    mStorageReference?.child("Fotos")?.child(userid)?.child(descripcionLugar)

                direccionImagenChild?.putFile(it1)?.addOnSuccessListener { taskSnapshot ->

                    finalizarCarga()
                    Toast.makeText(this, "Foto a sido publicada", Toast.LENGTH_SHORT).show();

                    direccionImagenChild?.downloadUrl?.addOnSuccessListener {
                        entity.imageURL = it.toString()
                        guardarUrlFoto(entity)
                    }

                }?.addOnFailureListener {
                    finalizarCarga()
                    it.message?.let { it2 -> mensajeFallido(it2) }
                }
            }

    }

    private fun guardarUrlFoto(entity: PlaceEntity) {
        val hashMap = obtenerEstructura(entity)
        mDatabase.child("Imagen").push().setValue(hashMap)
        guardarFeed(hashMap)
    }

    private fun guardarFeed(url: HashMap<String, String>) {
        mDatabaseFeed.push().setValue(url)
        finish()
    }

    private fun obtenerEstructura(entity: PlaceEntity): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        hashMap["id"] = userid
        hashMap["descripcionLugar"] = entity.descripcionLugar.orEmpty()
        hashMap["imageURL"] = entity.imageURL.orEmpty()
        hashMap["createdAt"] = obtenerFechaActual()
        return hashMap
    }

    fun obtenerFechaActual() : String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val fecha: String = dateFormat.format(date)
        return fecha
    }

    private fun mensajeFallido(mensaje: String) {
        Toast.makeText(this@LocalPlacesActivity, mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun mostrarMensajeDeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarMensajeExitoso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarMensajePlaceLocalEliminar() {
        Toast.makeText(this, "Foto Eliminada", Toast.LENGTH_LONG).show()
        presenter?.obtenerListaPlacesLocales(firebaseUser.uid)
    }

    override fun iniciarCargar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun finalizarCarga() {
        progressBar.visibility = View.GONE
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