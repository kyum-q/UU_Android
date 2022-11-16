package com.example.android_sns_project

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.Content
import com.example.android_sns_project.databinding.ContentItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

data class Item(val uid : String, val imagePath:String, val bmp:Bitmap){
    constructor(content: Content, bmp : Bitmap) :
            this(content.uid.toString(), content.imagePath.toString(), bmp)

}
class MyViewHolder(val binding: ContentItemBinding) : RecyclerView.ViewHolder(binding.root)

class UserAdapter(private val context: Context)
//    : RecyclerView.Adapter<MyViewHolder>(){
//
//    var firestore : FirebaseFirestore? =null
//    val db = Firebase.firestore
//    val rootRef = Firebase.storage.reference
//
//    var items = ArrayList<Item>()
//    private val itemsCollectionRef = db.collection("content")
//    init {
//        //content collect에 접근
//        itemsCollectionRef!!.get().addOnSuccessListener {
//            // var items = mutableListOf<Item>()
//            for (doc in it) {
//                val ref = rootRef.child(doc["imagePath"].toString())
//
//                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
//                        //imgView?.setImageBitmap(bmp)
//                        items.add(Item(doc, bmp))
//
//                    }
//                }
//            }
//            notifyDataSetChanged()
//            //adapter?.updateList(items)
//        }
//    }
//    fun interface OnItemClickListener {
//        fun onItemClick(student_id: String)
//    }
//    private var itemClickListener: OnItemClickListener? = null
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        itemClickListener = listener
//    }
////    fun updateList(newList: List<Item>) {
////        items = newList
////        notifyDataSetChanged()
////    }
//    //• RecyclerView에서 Adapter에게 필요한 만큼의 ViewHolder 생성을 요청하여 ViewHolder를 받아 두고 있음
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ContentItemBinding.inflate(inflater, parent, false)
//        return MyViewHolder(binding)
//    }
//    //리스트이 항목 내용이 변경되어야 할 때 항목에 해당하는
//    //ViewHolder에 데이터를 채우도록 Adapter에게 요청
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//       if(items.isEmpty()) return
//
//
//            val item = items[position]
//            holder.binding.imageView.setImageBitmap(item.bmp)
//
//    }
//
//    override fun getItemCount(): Int {
//      //  return students.size
//        return items.size
//    }
//}