package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private var db: FirebaseFirestore = Firebase.firestore
    private var userRef = db.collection("user")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        findViewById<Button>(R.id.Back).setOnClickListener{
            startActivity(
                Intent(this,MainActivity::class.java)
            )
        }
        findViewById<Button>(R.id.signup).setOnClickListener{
            val userEmail = findViewById<EditText>(R.id.username)?.text.toString()
            val password = findViewById<EditText>(R.id.password)?.text.toString()
            val name = findViewById<EditText>(R.id.Name)?.text.toString()
            val birth = findViewById<EditText>(R.id.BirthDate)?.text.toString().toInt()
            doSignUp(userEmail,password,name,birth)
        }
    }
    private fun doSignUp(userEmail: String, password: String, name: String, birthdate: Int){
        Firebase.auth.createUserWithEmailAndPassword(userEmail,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user = Firebase.auth.currentUser
                    saveUserData(user?.uid, name, birthdate)
                    startActivity(
                        Intent(this,ViewPostListActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(userId: String?, name: String, birthdate: Int) {
        val userMap = hashMapOf(
            "name" to name,
            "birthdate" to birthdate
        )

        userRef.document(userId ?: "")
            .set(userMap)
            .addOnSuccessListener {  }.addOnFailureListener{}

        Toast.makeText(baseContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }
}