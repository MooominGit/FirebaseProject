package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
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
    private val forSale by lazy {findViewById<CheckBox>(R.id.modify_post_forSale)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_post)

        val docId = intent.getStringExtra("docId")
        if (docId != null) getData(docId)

        back.setOnClickListener{
            onBackPressed()
            //startActivity(Intent(this,ViewPostListActivity::class.java))
        }

        modifyPost.setOnClickListener{
            if (docId != null) updateData(docId)
        }
    }

    fun getData(docId: String) {
        postRef.document(docId).get().addOnSuccessListener {
            val sale = it.getBoolean("forSale")
            title.text = it["title"].toString()
            price.text = it["price"].toString()
            content.text = it["content"].toString()
            forSale.isChecked = it.getBoolean("forSale")?:false
        }
    }

    fun updateData(docId: String){
        if (price.text.isEmpty()) {
            Snackbar.make(price, "가격을 입력해주세요", Snackbar.LENGTH_SHORT).show()
            return
        }
        postRef.document(docId).update("price",price.text.toString().toInt())
        postRef.document(docId).update("forSale",forSale.isChecked).addOnSuccessListener {
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }.addOnFailureListener{
            Toast.makeText(this,"글을 수정하는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
        
    }

}