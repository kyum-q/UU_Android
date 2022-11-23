package com.example.android_sns_project

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import com.example.android_sns_project.data.UserInfo
import com.example.android_sns_project.data.UserList
import com.example.android_sns_project.databinding.ContentItemBinding
import com.example.android_sns_project.databinding.FragmentUserBinding
import com.example.android_sns_project.databinding.UserListItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class FollowingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    private var adapter: FollowingFragment.userListAdapter? = null
    private val itemsCollectionRef = db.collection("content")
    private var binding: FragmentUserBinding? = null

    var auth: FirebaseAuth? = null
    var currentEmail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        // recyclerview setup

        currentEmail = arguments?.getString("email")

        binding!!.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = userListAdapter()
        binding!!.recyclerView.adapter = adapter
        return binding?.root
    }

    inner class MyViewHolder(val binding: UserListItemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SuspiciousIndentation")
    inner class userListAdapter : RecyclerView.Adapter<MyViewHolder>() {

        private val itemsCollectionRef = db.collection("content")
        var userInfos: ArrayList<UserInfo> = arrayListOf()
        var currentUserInfo : UserInfo? = null;
        var followingList :  ArrayList<UserInfo> = arrayListOf()

        init {
            db.collection("UserInfo").addSnapshotListener{snapshot, error ->
                if (snapshot == null) return@addSnapshotListener
                //전체 유저 정보 받아오기
                for(snapshot in snapshot.documents) {
                    var userInfo = snapshot.toObject(UserInfo::class.java)
                    if(!userInfos.contains(userInfo!!)){
                        userInfos.add(userInfo)
                    }
                }
                userInfos.forEach {
                    if(it.email ==currentEmail ){
                        //현재 유저의 팔로잉 리스트
                        currentUserInfo = it
                        }
                    }
                //팔로잉 리스트에 userInfo 담기
                currentUserInfo?.followings?.map{(key, value) ->
                    userInfos.forEach{
                        if(it.email == key){
                            Log.d("followList","followingList${it}" )
                            followingList.add(it)
                        }
                    }
                }
                notifyDataSetChanged()
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: UserListItemBinding = UserListItemBinding.inflate(inflater, parent, false)
            return MyViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            Log.d("followList",followingList[position].email.toString() )
            val item = followingList[position]
            holder.binding.nickName.text= item.nickname

        }

        override fun getItemCount(): Int {
            return followingList.size
        }
    }
}