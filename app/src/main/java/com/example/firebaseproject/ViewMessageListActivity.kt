package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitAll

class ViewMessageListActivity : AppCompatActivity() {
    private var adapter: MessageAdapter? = null
    private val uid = Firebase.auth.currentUser?.uid
    private val db: FirebaseFirestore = Firebase.firestore
    private val userRef = db.collection("user")
    private val messageRef = db.collection("message")
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.MessageRecycler) }
    private val back by lazy {findViewById<Button>(R.id.view_message_list_back)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_message_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MessageAdapter(this, emptyList())
        adapter?.setOnItemClickListener {
            val intent = Intent(this, ViewMessageActivity::class.java)
            intent.putExtra("docId", it.sendUser)
            startActivity(intent)
        }
        queryItem()
        recyclerView.adapter = adapter

        back.setOnClickListener{
            startActivity(Intent(this,ViewPostListActivity::class.java))
        }
    }

    private fun queryItem() {
        messageRef.whereEqualTo("receiveUser", uid).get().addOnSuccessListener {query->
            var userId : String
            var userName : String
            val items = mutableListOf<MessageItem>()
            for (doc in query) {
                userId = doc.getString("sendUser")?:""
                userRef.document(userId).get().addOnSuccessListener {
                    userName = it["name"].toString()
                    items.add(MessageItem(userName,doc))

                    if(items.size == query.size())
                        adapter?.updateList(items)
                }
            }
        }
    }
}