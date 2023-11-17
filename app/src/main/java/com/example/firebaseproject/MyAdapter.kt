package com.example.firebaseproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.text.DecimalFormat

data class Item(val docId : String, val title: String, val price: Int, val forSale : Boolean, val id : String) {
    constructor(doc: QueryDocumentSnapshot) :
            this(doc.id, doc["title"].toString(), doc["price"].toString().toIntOrNull() ?: 0, doc["forSale"].toString().toBoolean(), doc["id"].toString())
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

class MyAdapter(private val context: Context, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>() {

    fun interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.post_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val decimalFormat = DecimalFormat("#,###")
        val item = items[position]
        holder.view.findViewById<TextView>(R.id.post_list_title).text = item.title
        holder.view.findViewById<TextView>(R.id.post_list_price).text = decimalFormat.format(item.price).toString() + "원"
        if (item.forSale.toString() == "true")
            holder.view.findViewById<TextView>(R.id.post_list_forSale).text = "판매여부 O"
        else
            holder.view.findViewById<TextView>(R.id.post_list_forSale).text = "판매여부 X"

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount() = items.size
}