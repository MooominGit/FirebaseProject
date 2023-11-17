package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewPostActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val postRef = db.collection("post")
    private val userRef = db.collection("user")
    private val back by lazy {findViewById<Button>(R.id.view_post_back)}
    private val sendMessage by lazy {findViewById<Button>(R.id.view_post_sendMessage)}
    private val name by lazy {findViewById<TextView>(R.id.view_post_name)}
    private val title by lazy {findViewById<TextView>(R.id.view_post_title)}
    private val content by lazy {findViewById<TextView>(R.id.view_post_content)}
    private val price by lazy {findViewById<TextView>(R.id.view_post_price)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)
        val docId = intent.getStringExtra("docId")
        if (docId != null) {
            getData(docId)
        }

        back.setOnClickListener{
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }

        sendMessage.setOnClickListener{

        }
    }

    private fun getData(docId: String) {
        postRef.document(docId).get().addOnSuccessListener {
            title.text = it["title"].toString()
            price.text = it["price"].toString()
            content.text = it["content"].toString()
            userRef.document(it["id"].toString()).get().addOnSuccessListener {
                name.text = it["name"].toString()
            }
        }
    }


}