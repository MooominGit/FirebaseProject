package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ModifyPostActivity : AppCompatActivity(){
    private val db: FirebaseFirestore = Firebase.firestore
    private val postRef = db.collection("post")
    private val back by lazy {findViewById<Button>(R.id.modify_post_back)}
    private val modifyPost by lazy {findViewById<Button>(R.id.modify_post_modifyPost)}
    private val title by lazy {findViewById<TextView>(R.id.modify_post_title)}
    private val content by lazy {findViewById<TextView>(R.id.modify_post_content)}
    private val price by lazy {findViewById<TextView>(R.id.modify_post_price)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_post)

        val docId = intent.getStringExtra("docId")
        if (docId != null) getData(docId)

        back.setOnClickListener{
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }

        modifyPost.setOnClickListener{
            if (docId != null) updateData(docId)
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }
    }

    fun getData(docId: String) {
        postRef.document(docId).get().addOnSuccessListener {
            title.text = it["title"].toString()
            price.text = it["price"].toString()
            content.text = it["content"].toString()
        }
    }

    fun updateData(docId: String){
        val titleText = title.text.toString()
        val priceText = price.text.toString()
        val contentText = content.text.toString()
        postRef.document(docId).update("title",titleText)
        postRef.document(docId).update("price",priceText)
        postRef.document(docId).update("content",contentText)
    }

}