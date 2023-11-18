package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SendMessageActivity : AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private val db: FirebaseFirestore = Firebase.firestore
    private val messageRef = db.collection("message")
    private val sendMessage by lazy {findViewById<Button>(R.id.send_message_sendMessage)}
    private val messageContent by lazy {findViewById<EditText>(R.id.send_message_message)}
    private lateinit var postUserId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        sendMessage.setOnClickListener{
            saveMessage()
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }
    }

    fun saveMessage(){
        val message = messageContent.text.toString()
        val uid = user?.uid
        postUserId = intent.getStringExtra("postUserId")?:""
        val messageMap = hashMapOf(
            "content" to message,
            "sendUser" to uid,
            "receiveUser" to postUserId
        )
        messageRef.add(messageMap).addOnSuccessListener {
        }
    }
}