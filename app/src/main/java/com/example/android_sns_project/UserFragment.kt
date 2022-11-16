package com.example.android_sns_project

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.Content
import com.example.android_sns_project.databinding.ContentItemBinding
import com.example.android_sns_project.databinding.FragmentUserBinding
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject


class UserFragment : Fragment() {
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    private var adapter: UserFragmentAdapter? = null
    private val itemsCollectionRef = db.collection("content")
    private var binding: FragmentUserBinding? = null
    var items = mutableListOf<Item>()
    //실시간 변경 데이터 추적
    private var snapshotListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)

        // recyclerview setup
        binding!!.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        adapter = UserFragmentAdapter()
//        adapter?.setOnItemClickListener {
//            //updateList()
//        }
//        binding!!.accountBtnFollow.setOnClickListener{
//            updateList()
//        }
        binding!!.recyclerView.adapter = adapter
       // updateList()
        return binding?.root
    }
    inner class MyViewHolder(val binding: ContentItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class UserFragmentAdapter : RecyclerView.Adapter<MyViewHolder>(){
        var contents : ArrayList<Content> = arrayListOf()
        var items = ArrayList<Item>()
        private val itemsCollectionRef = db.collection("content")
        init {
            //content collect에 접근
            itemsCollectionRef!!.addSnapshotListener {snapshot, error ->
                // var items = mutableListOf<Item>()
                if(snapshot == null) return@addSnapshotListener

                for(snapshot in snapshot.documents){
                    contents.add(snapshot.toObject(Content::class.java)!!)
                    for (content in contents) {
                        val ref = rootRef.child(content.imagePath.toString())

                        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                            if (it.isSuccessful) {
                                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                                //imgView?.setImageBitmap(bmp)
                                items.add(Item(content, bmp))

                            }
                        }
                    }
                }
                notifyDataSetChanged()


                //adapter?.updateList(items)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ContentItemBinding.inflate(inflater, parent, false)
            var width = resources.displayMetrics.widthPixels/3
            binding.imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            if(items.isEmpty()) return


            val item = items[position]
            holder.binding.imageView.setImageBitmap(item.bmp)
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }
    private fun updateList() {
//        //content collect에 접근
//        itemsCollectionRef.get().addOnSuccessListener {
//           // var items = mutableListOf<Item>()
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
//            adapter?.updateList(items)
//        }
    }


//    private fun getImage(path:String){
//        val token = path.split("gs://android-sns-youu.appspot.com/")
//        val ref = rootRef.child(token[1])
//
//        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
//            if (it.isSuccessful) {
//                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
//                val contentImage: ImageView = customLayout.findViewById<ImageView>(R.id.contentImage)
//                contentImage.setImageBitmap(bmp)
//            }
//        }
//    }
}