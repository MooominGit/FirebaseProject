package com.example.firebaseproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot


data class MessageItem(val sendUser : String, val content : String) {
    constructor(sendUser: String,doc: QueryDocumentSnapshot) :
            this(sendUser, doc["content"].toString())
}

class MessageViewHolder(val view: View) : RecyclerView.ViewHolder(view)
class MessageAdapter(private val context: Context, private var items: List<MessageItem>) :
    RecyclerView.Adapter<MessageViewHolder>() {
    fun interface OnItemClickListener {
        fun onItemClick(item: MessageItem)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<MessageItem>) {
        items = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message_list, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = items[position]
        holder.view.findViewById<TextView>(R.id.message_list_name).text = "보낸 사람 : " + item.sendUser
        holder.view.findViewById<TextView>(R.id.message_list_content).text = item.content
        holder.view.setOnClickListener {
            itemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount() = items.size
}
