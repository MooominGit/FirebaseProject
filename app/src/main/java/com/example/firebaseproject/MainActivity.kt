package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class MainActivity : AppCompatActivity() {
    private val userEmail by lazy {findViewById<EditText>(R.id.username)}
    private val password by lazy {findViewById<EditText>(R.id.password)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.signin)?.setOnClickListener {
            var email = userEmail.text.toString()
            var pass = password.text.toString()
            doLogin(email, pass)
        }
        findViewById<Button>(R.id.signup).setOnClickListener{
            startActivity(
                Intent(this,SignUpActivity::class.java)
            )
        }
    }
    // 로그인
    private fun doLogin(email: String, pass: String) {
        if (userEmail.text.toString().isEmpty()) {
            Snackbar.make(userEmail, "유효하지 않은 이메일 형식입니다.", Snackbar.LENGTH_SHORT).show()
            return
        }
        else if (password.text.toString().isEmpty() || password.text.length < 6){
            Snackbar.make(userEmail, "비밀번호는 6글자 이상이어야 합니다", Snackbar.LENGTH_SHORT).show()
            return
        }
        Firebase.auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    startActivity(Intent(this,ViewPostListActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity","signInWithEmail",it.exception)
                    Toast.makeText(this, "가입되어있지 않은 계정입니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}