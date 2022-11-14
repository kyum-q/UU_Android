package com.example.android_sns_project

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_sns_project.databinding.FragmentHomeBinding
import com.example.android_sns_project.databinding.FragmentUserBinding
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class UserFragment : Fragment() {
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    private var adapter: UserAdapter? = null
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
        // Inflate the layout for this fragment

        // recyclerview setup
        binding!!.recyclerView.layoutManager = GridLayoutManager(activity, 3)
        adapter = activity?.let { UserAdapter(it, emptyList()) }
        adapter?.setOnItemClickListener {
            //updateList()
        }
        binding!!.accountBtnFollow.setOnClickListener{
            updateList()
        }
        binding!!.recyclerView.adapter = adapter
        updateList()
        return binding?.root
    }

    private fun updateList() {
        //content collect에 접근
        itemsCollectionRef.get().addOnSuccessListener {
           // var items = mutableListOf<Item>()
            for (doc in it) {
                val token = doc["imagePath"].toString().split("gs://android-sns-youu.appspot.com/")
                val ref = rootRef.child(token[1])

                ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                        //imgView?.setImageBitmap(bmp)
                        items.add(Item(doc,bmp))

                    }

                }

            }

            adapter?.updateList(items)
        }
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