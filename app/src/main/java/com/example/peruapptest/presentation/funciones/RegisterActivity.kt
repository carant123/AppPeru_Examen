package com.example.peruapptest.presentation.funciones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.peruapptest.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_activity.*
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var reference : DatabaseReference

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        btn_register.setOnClickListener {

            var txt_username = username.text.toString()
            var txt_email = email.text.toString()
            var txt_password = password.text.toString()

            if (txt_username.isEmpty() ||
                txt_email.isEmpty() ||
                txt_email.isEmpty() ) {

                Toast.makeText(this@RegisterActivity, "Todos los campos son requeridos", Toast.LENGTH_SHORT)
                    .show()
            } else if (txt_password.length < 4) {
                Toast.makeText(
                    this@RegisterActivity,
                    "password debe tener al menos 4 digitos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                register(txt_username, txt_email, txt_password)
            }

        }

    }

    private fun register(username: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser!!
                    val userid = firebaseUser.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid)

                    val hashMap = HashMap<String, String>()
                    hashMap["id"] = userid
                    hashMap["username"] = username
                    hashMap["imageURL"] = "default"
                    hashMap["status"] = "offline"
                    hashMap["search"] = username.toLowerCase()
                    hashMap["password"] = password.toLowerCase()

                    reference.setValue(hashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            FirebaseAuth.getInstance().signOut()
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "No puedes registrar este email o password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}