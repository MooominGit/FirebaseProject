package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WritePostActivity: AppCompatActivity() {
    private val db: FirebaseFirestore = Firebase.firestore
    private val postRef = db.collection("post")
    private val writeButton by lazy {findViewById<Button>(R.id.modify_post_modifyPost)}
    private val backButton by lazy {findViewById<Button>(R.id.modify_post_back)}
    private val editTitle by lazy {findViewById<EditText>(R.id.modify_post_title)}
    private val editPrice by lazy {findViewById<EditText>(R.id.modify_post_price)}
    private val editContent by lazy {findViewById<EditText>(R.id.modify_post_content)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)

        writeButton.setOnClickListener{
            addItem()
            startActivity(
                Intent(this,ViewPostListActivity::class.java)
            )
        }

         backButton.setOnClickListener{
            startActivity(
                Intent(this,ViewPostListActivity::class.java)
            )
        }
    }

    private fun addItem() {
        val title = editTitle.text.toString()
        val uid = Firebase.auth.currentUser?.uid
        if (title.isEmpty()) {
            Snackbar.make(editTitle, "Input name!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val price = editPrice.text.toString().toInt()
        val content = editContent.text.toString()
        if (content.isEmpty()) {
            Snackbar.make(editContent, "Input Content", Snackbar.LENGTH_SHORT).show()
            return
        }
        val itemMap = hashMapOf(
            "id" to uid,
            "title" to title,
            "content" to content,
            "price" to price,
            "forSale" to true
        )
        postRef.add(itemMap).addOnSuccessListener {  }
            .addOnFailureListener{}
    }

}