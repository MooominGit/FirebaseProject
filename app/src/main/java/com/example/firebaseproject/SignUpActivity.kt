package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private var db: FirebaseFirestore = Firebase.firestore
    private var userRef = db.collection("user")
    private val UserEmail by lazy {findViewById<EditText>(R.id.username)}
    private val Password by lazy {findViewById<EditText>(R.id.password)}
    private val Name by lazy {findViewById<EditText>(R.id.Name)}
    private val Birth by lazy {findViewById<EditText>(R.id.BirthDate)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        findViewById<Button>(R.id.Back).setOnClickListener{
            startActivity(
                Intent(this,MainActivity::class.java)
            )
        }
        findViewById<Button>(R.id.signup).setOnClickListener{
            val userEmail = UserEmail.text.toString()
            val password = Password.text.toString()
            val name = Name.text.toString()
            val birth = Birth.text.toString()
            doSignUp(userEmail,password,name,birth)
        }
    }
    private fun doSignUp(userEmail: String, password: String, name: String, birthdate: String){
        if (userEmail.isEmpty()) {
            Snackbar.make(UserEmail, "유효하지 않은 이메일 형식입니다.", Snackbar.LENGTH_SHORT).show()
            return
        }
        else if (password.isEmpty() || password.length < 6) {
            Snackbar.make(Password, "비밀번호는 6글자 이상이어야 합니다.", Snackbar.LENGTH_SHORT).show()
            return
        }
        else if (name.isEmpty()) {
            Snackbar.make(Name, "이름을 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            return
        }
        else if (birthdate.isEmpty()) {
            Snackbar.make(Birth, "유효하지 않은 생년월일입니다.", Snackbar.LENGTH_SHORT).show()
            return
        }
        Firebase.auth.createUserWithEmailAndPassword(userEmail,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user = Firebase.auth.currentUser
                    saveUserData(user?.uid, name, birthdate)
                    startActivity(Intent(this,ViewPostListActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(userId: String?, name: String, birthdate: String) {
        val userMap = hashMapOf(
            "name" to name,
            "birthdate" to birthdate.toInt()
        )
        userRef.document(userId ?: "")
            .set(userMap)
            .addOnSuccessListener {  }.addOnFailureListener{}

        Toast.makeText(baseContext, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
    }
}