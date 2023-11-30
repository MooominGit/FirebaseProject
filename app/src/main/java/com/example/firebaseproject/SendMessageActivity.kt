package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SendMessageActivity : AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private val db: FirebaseFirestore = Firebase.firestore
    private val messageRef = db.collection("message")
    private val userRef = db.collection("user")
    private val sendMessage by lazy {findViewById<Button>(R.id.send_message_sendMessage)}
    private val messageContent by lazy {findViewById<EditText>(R.id.send_message_message)}
    private val back by lazy {findViewById<Button>(R.id.send_message_back)}
    private val name by lazy {findViewById<TextView>(R.id.send_message_name)}
    private lateinit var postUserId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_message)

        val docId = intent.getStringExtra("postUserId")?:""
        userRef.document(docId).get().addOnSuccessListener {
            name.text = "받을 사람 : " + it["name"].toString()
        }

        findViewById<FrameLayout>(R.id.frameLayout3).setOnClickListener{
            messageContent.requestFocus()
        }

        sendMessage.setOnClickListener{
            saveMessage()
        }

        back.setOnClickListener{
            onBackPressed()
        }
    }

    fun saveMessage(){
        if (messageContent.text.isEmpty()) {
            Snackbar.make(messageContent, "내용을 입력해주세요", Snackbar.LENGTH_SHORT).show()
            return
        }
        val message = messageContent.text.toString()
        val uid = user?.uid
        postUserId = intent.getStringExtra("postUserId")?:""
        val messageMap = hashMapOf(
            "content" to message,
            "sendUser" to uid,
            "receiveUser" to postUserId
        )
        messageRef.add(messageMap).addOnSuccessListener {
            startActivity(Intent(this,ViewPostListActivity::class.java))
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,"메시지를 보내는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}