package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ViewPostListActivity: AppCompatActivity() {
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val postRef = db.collection("post")
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.PostRecycler) }
    private val wrietPost by lazy { findViewById<Button>(R.id.writePost)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
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

        wrietPost.setOnClickListener{
            startActivity(
                Intent(this,WritePostActivity::class.java)
            )
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
}

