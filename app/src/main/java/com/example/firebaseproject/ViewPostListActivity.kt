package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ViewPostListActivity: AppCompatActivity() {
    private var adapter: PostAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val postRef = db.collection("post")
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.PostRecycler) }
    private val wrietPost by lazy { findViewById<Button>(R.id.writePost)}
    private val receivedMessage by lazy { findViewById<Button>(R.id.receivedMessage)}
    private val forSale by lazy {findViewById<CheckBox>(R.id.view_post_list_forSale)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(this, emptyList())
        adapter?.setOnItemClickListener {
            val intent = if(Firebase.auth.currentUser?.uid == it.id)
                Intent(this, ModifyPostActivity::class.java)
            else
                Intent(this, ViewPostActivity::class.java)
            intent.putExtra("docId", it.docId)
            startActivity(intent)
        }
        queryItem()
        recyclerView.adapter = adapter

        forSale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (forSale.isChecked == true) queryItem()
            else queryNotSale()
        }

        wrietPost.setOnClickListener{
            startActivity(Intent(this,WritePostActivity::class.java))
        }

        receivedMessage.setOnClickListener{
            startActivity(Intent(this,ViewMessageListActivity::class.java))
        }
    }

    private fun queryItem() {
        postRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }

    private fun queryNotSale() {
        postRef.whereEqualTo("forSale", true).get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }
}

