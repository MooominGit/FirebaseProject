package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Application.Execution.Thread.Frame
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

        findViewById<FrameLayout>(R.id.frameLayout2).setOnClickListener{
            editContent.requestFocus()
        }

        writeButton.setOnClickListener{
            addItem()
        }

         backButton.setOnClickListener{
            onBackPressed()
        }
    }

    private fun addItem() {
        val title = editTitle.text.toString()
        val uid = Firebase.auth.currentUser?.uid
        if (title.isEmpty()) {
            Snackbar.make(editTitle, "제목을 입력해주세요", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (editPrice.text.isEmpty()){
            Snackbar.make(editContent, "가격을 입력해주세요", Snackbar.LENGTH_SHORT).show()
            return
        }
        val content = editContent.text.toString()
        if (content.isEmpty()) {
            Snackbar.make(editContent, "내용을 입력해주세요", Snackbar.LENGTH_SHORT).show()
            return
        }
        val itemMap = hashMapOf(
            "id" to uid,
            "title" to title,
            "content" to content,
            "price" to editPrice.text.toString().toInt(),
            "forSale" to true
        )
        postRef.add(itemMap).addOnSuccessListener {
            startActivity(Intent(this,ViewPostListActivity::class.java))
            finish()
        }
            .addOnFailureListener{
                Toast.makeText(this,"글 등록에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
    }

}