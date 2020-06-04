package com.example.peruapptest.presentation.funciones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.peruapptest.R
import com.example.peruapptest.presentation.funciones.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_user_activity.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var firebaseUser : FirebaseUser

    override fun onStart() {
        super.onStart()

        FirebaseAuth.getInstance().currentUser?.let {
            firebaseUser = it
            irAHome()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_user_activity)

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {

            var txt_email : String = email.text.toString()
            var txt_password : String = password.text.toString()

            if ( txt_email!!.isEmpty() || txt_password!!.isEmpty() ) {
                mensajeFallido("Todos los campos son requeridos")
            } else {
                auth.signInWithEmailAndPassword(txt_email, txt_password)
                        .addOnCompleteListener {
                            if (it.isSuccessful()) {
                                irAHome()
                            } else {
                                mensajeFallido("Autenticacion Fallida!");
                            }
                        }
            }

        }

        forgot_password.setOnClickListener {
            irARegistro()
        }

    }

    private fun mensajeFallido(mensaje: String) {
        Toast.makeText(this@LoginActivity, mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun irARegistro() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun irAHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

}
