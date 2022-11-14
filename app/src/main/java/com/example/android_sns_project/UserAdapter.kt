package com.example.android_sns_project

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.databinding.ContentItemBinding
import com.example.android_sns_project.databinding.FragmentUserBinding
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Item(val id:String,val uid : String, val imagePath:String, val bmp:Bitmap){
    constructor(doc: QueryDocumentSnapshot, bmp : Bitmap) :
            this(doc.id,doc["uid"].toString(), doc["imagePath"].toString(), bmp)

}
class MyViewHolder(val binding: ContentItemBinding) : RecyclerView.ViewHolder(binding.root)

class UserAdapter (private val context: Context, private var items: List<Item>)
    : RecyclerView.Adapter<MyViewHolder>(){

    fun interface OnItemClickListener {
        fun onItemClick(student_id: String)
    }
    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    fun updateList(newList: List<Item>) {
        items = newList
        notifyDataSetChanged()
    }
    //• RecyclerView에서 Adapter에게 필요한 만큼의 ViewHolder 생성을 요청하여 ViewHolder를 받아 두고 있음
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContentItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }
    //리스트이 항목 내용이 변경되어야 할 때 항목에 해당하는
    //ViewHolder에 데이터를 채우도록 Adapter에게 요청
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       if(items.isEmpty()) return


            val item = items[position]

            holder.binding.imageView.setImageBitmap(item.bmp)

//        holder.binding. = item.uid
//        holder.binding.textView2.text = item.name
//        holder.binding.root.setOnClickListener {
//            AlertDialog.Builder(context).setMessage("You clicked ${student.name}.").show()
//        }
//        holder.binding.buttonRemove.setOnClickListener {
//            students.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//        }
    }

    override fun getItemCount(): Int {
      //  return students.size
        return items.size
    }
}