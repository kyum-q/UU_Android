package com.example.android_sns_project

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
        binding!!.recyclerView.layoutManager = GridLayoutManager(activity,3)
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
    inner class MyViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    @SuppressLint("SuspiciousIndentation")
    inner class UserFragmentAdapter : RecyclerView.Adapter<MyViewHolder>(){
        private val itemsCollectionRef = db.collection("content")
        var contents : ArrayList<Content> = arrayListOf()
        var contentsID : ArrayList<String> = arrayListOf()
        var items : ArrayList<Item> = arrayListOf()

        init {
            //content collect에 접근
            itemsCollectionRef!!.addSnapshotListener {snapshot, error ->
                // var items = mutableListOf<Item>()
                if(snapshot == null) return@addSnapshotListener

          //      CoroutineScope(Dispatchers.Main).launch {
                    for(snapshot in snapshot.documents) {
                        contents.add(snapshot.toObject(Content::class.java)!!)
                        contentsID.add(snapshot.id)
                        Log.d("TAG","전 ${contents.size}")

                        notifyDataSetChanged()
                        Log.d("TAG","후 ${contents.size}")
                    }

             //   }
                //adapter?.updateList(items)
            }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding =ContentItemBinding.inflate(inflater, parent, false)
            var width = resources.displayMetrics.widthPixels/3
            var imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)

            return MyViewHolder(imageView)
            Log.d("TAG","onCreateViewHolder ${contents.size}")
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            if(contents.isEmpty()) return
            var imageView =(holder as MyViewHolder).imageView
            Log.d("TAG","onBindViewHolder ${contents.size}")
          //  for (content in contents) {
                val ref = rootRef.child(contents[position].imagePath.toString())

                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                        imageView.setImageBitmap(bmp)
                        //imgView?.setImageBitmap(bmp)
                       // items?.add(Item(content, bmp))

                        imageView.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("id",contentsID[position])
                            findNavController().navigate(com.example.android_sns_project.R.id.action_userFragment_to_myContentFragment, bundle)
                        }
                    }
                }
          //  }
          //  val item = items[position]
            //imageView.setImageBitmap(item.bmp)
        }

        override fun getItemCount(): Int {
            return contents.size
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