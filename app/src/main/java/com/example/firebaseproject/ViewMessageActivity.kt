package com.example.firebaseproject

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewMessageActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val messageRef = db.collection("message")
    private val userRef = db.collection("user")
    private val back by lazy {findViewById<Button>(R.id.view_message_back)}
    private val messageContent by lazy {findViewById<TextView>(R.id.view_message_content)}
    private val sendUser by lazy {findViewById<TextView>(R.id.view_message_sendUser)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_message)

        val docId = intent.getStringExtra("docId")?:""
        getMessage(docId)

        back.setOnClickListener{
            onBackPressed()
        }
    }

    private fun getMessage(docId: String) {
        messageRef.document(docId).get().addOnSuccessListener {
            messageContent.text = it["content"].toString()
            userRef.document(it["sendUser"].toString()).get().addOnSuccessListener {
                sendUser.text = "보낸 사람 : " + it["name"].toString()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"메시지를 불러오는 데 실패했습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}