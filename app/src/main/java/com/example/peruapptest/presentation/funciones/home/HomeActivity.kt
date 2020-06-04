package com.example.peruapptest.presentation.funciones.home

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peruapptest.AlarmReciever
import com.example.peruapptest.R
import com.example.peruapptest.presentation.adapter.PlaceAdapter
import com.example.peruapptest.presentation.model.Place
import com.example.peruapptest.data.room.AppDatabase
import com.example.peruapptest.presentation.funciones.LoginActivity
import com.example.peruapptest.presentation.funciones.localPlaces.LocalPlacesActivity
import com.example.peruapptest.presentation.funciones.photo.PhotoActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeActivity : AppCompatActivity(), HomeMVP.View {

    lateinit var mFusedLocationCliente : FusedLocationProviderClient
    lateinit var mDatabase : DatabaseReference
    lateinit var mDatabaseUsers : DatabaseReference
    lateinit var firebaseUser: FirebaseUser

    private val ACCESS_FINE_LOCATION_CODE = 101
    private val ACCESS_COARSE_LOCATION_CODE = 102

    lateinit var placeAdapter: PlaceAdapter
    lateinit var mPlaces : ArrayList<Place>

    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent : PendingIntent

    private val presenter: HomeMVP.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        inicializarToolbar()
        initializeRecycleview()
        inicializarVariables()
        inicializarVarFirebase()
        inicializarRoom()
        setupPermissions()
        activelocalizacion()
        scheduleAlarm()

    }

    private fun inicializarRoom() {
        presenter?.setView(this)
    }

    private fun inicializarVariables() {
        mPlaces = ArrayList()
        mFusedLocationCliente = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun inicializarToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
    }


    fun scheduleAlarm() {
        val time: Long = GregorianCalendar().timeInMillis +  60 * 1000
        val intentAlarm = Intent(this, AlarmReciever::class.java)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent = PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    private fun inicilizarFeed() {

        val fuser = FirebaseAuth.getInstance().currentUser
        //val query = FirebaseDatabase.getInstance().getReference("Feed").child(fuser!!.uid)
        val query = FirebaseDatabase.getInstance().getReference("Feed")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                mPlaces.clear()
                for (snapshot in dataSnapshot.children) {
                    val place = snapshot.getValue(Place::class.java)!!
                    mPlaces.add(place)
                }

                placeAdapter = PlaceAdapter(baseContext, mPlaces)
                recycler_view.adapter = placeAdapter

            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {

            }
        })

    }

    private fun inicializarVarFirebase() {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("Users")
        mDatabase = mDatabaseUsers.child(firebaseUser.uid)
        inicilizarFeed()
    }

    private fun initializeRecycleview() {
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(baseContext)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return true
        } else if(item.itemId == R.id.id_foto){
            startActivity(Intent(this, PhotoActivity::class.java))
            return true
        } else if(item.itemId == R.id.id_foto_local){
            startActivity(Intent(this, LocalPlacesActivity::class.java))
            return true
        }


        return false
    }

    private fun activelocalizacion() {

        mFusedLocationCliente.lastLocation.addOnSuccessListener { location: Location? ->

            ltlongi.text = "" + location?.latitude + " " + location?.longitude
            val anotherMap = mutableMapOf<String, Double>()
            anotherMap.put("latitud", location!!.latitude)
            anotherMap.put("longitude", location.longitude)
            mDatabase.child("localizacion").removeValue()
            mDatabase.child("localizacion").push().setValue(anotherMap)

        }

    }

    private fun setupPermissions() {
        val permissionFineLocation = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        val permissionCoarseLocation = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED && permissionFineLocation != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            ACCESS_FINE_LOCATION_CODE)

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            ACCESS_COARSE_LOCATION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            ACCESS_FINE_LOCATION_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG1", "Permission has been denied by user")
                } else {
                    Log.i("TAG2", "Permission has been granted by user")
                    activelocalizacion()
                }
            }
            ACCESS_COARSE_LOCATION_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG1_2", "Permission has been denied by user")
                } else {
                    Log.i("TAG2_2", "Permission has been granted by user")
                    activelocalizacion()
                }
            }
        }
    }

    fun status(status : String) {
        val hashMap = HashMap<String, Any>()
        hashMap["status"] = status
        mDatabase.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        activelocalizacion()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        activelocalizacion()
        status("offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmManager.cancel(pendingIntent);
    }

    override fun iniciarCargar() {
        TODO("Not yet implemented")
    }

    override fun finalizarCarga() {
        TODO("Not yet implemented")
    }

    override fun mostrarMensajeDeError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

    override fun mostrarMensajeExitoso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }

}
