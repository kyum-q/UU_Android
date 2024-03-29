package com.example.android_sns_project

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.example.android_sns_project.data.Content
import com.example.android_sns_project.data.UserInfo
import com.example.android_sns_project.databinding.ContentItemBinding
import com.example.android_sns_project.databinding.FragmentCommentBinding
import com.example.android_sns_project.databinding.FragmentOtherUserBinding
import com.example.android_sns_project.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class OtherUserFragment : Fragment() {
    var photoUri : Uri? = null
    var photoBitmap: Bitmap? = null
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    private var adapter: OtherUserFragment.UserFragmentAdapter? = null
    private val itemsCollectionRef = db.collection("content")
    private var binding: FragmentOtherUserBinding? = null
    var items = mutableListOf<Item>()

    var currentUserId : String? = null
    var auth : FirebaseAuth? = null

    val database = Firebase.database
    var roofRef2 = Firebase.database.reference
    var userInfo : UserInfo? = null
    var userId : String?= null

    var curruntNickname : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentOtherUserBinding.inflate(inflater, container, false)
        userId = arguments?.getString("email").toString()
        binding!!.recyclerView.layoutManager = GridLayoutManager(activity,3)
        adapter = UserFragmentAdapter()
        binding!!.recyclerView.adapter = adapter

        //로그인한 유저의 닉네임 받아오기
        db.collection("UserInfo")?.document(auth?.currentUser?.email!!)
            ?.addSnapshotListener{ snapshot, error ->
                userInfo = snapshot?.toObject(UserInfo::class.java)
                curruntNickname = userInfo?.nickname
            }

        db.collection("UserInfo")?.document(userId!!)
            ?.addSnapshotListener { snapshot, error ->
                // var items = mutableListOf<Item>()
                Log.d("follow", snapshot.toString())
                if (snapshot == null) return@addSnapshotListener
//                snapshot.toSet()
                //      CoroutineScope(Dispatchers.Main).launch {

                userInfo = snapshot.toObject(UserInfo::class.java)

                binding!!.followerCount.text = userInfo?.followerCount.toString()
                binding!!.followingCount.text = userInfo?.followingCount.toString()
                binding!!.nickName.text = userInfo?.nickname.toString()

            }
        //게시물 수
        db.collection("content")?.whereEqualTo("userId",userId)
            ?.addSnapshotListener { snapshot, error ->
                if (snapshot == null) return@addSnapshotListener
                binding!!.contentCount.text = snapshot.documents.size.toString()

            }
        //팔로우 버튼 이벤트
        binding!!.accountBtnFollow.setOnClickListener {
            follow()
        }
        //팔로잉 목록 이벤트
        binding!!.followingLinear.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("email",userId)
            findNavController().navigate(R.id.action_otherUserFragment_to_followingFragment,bundle)
        }
        //팔로워 목록 이벤트 (followers)
        binding!!.followerLinear.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("email",userId)
            findNavController().navigate(R.id.action_otherUserFragment_to_followerFragment2,bundle)
        }
        follwerUpdate()
        return binding?.root
    }

    fun follow(){
        var id :String? =null
        //버튼을 누른 사람의 email을 가져온다
        var user_db = db.collection("UserInfo")?.document(userId!!)
        if(userId == auth?.currentUser?.email) return

        user_db?.get()?.addOnSuccessListener {
                var user = it.toObject(UserInfo::class.java)
                Log.d("follow", "follow() ${user}")
            //팔로우 이벤트
                //이미 목록에 존재하면
                if (user?.followers?.containsKey(auth?.currentUser?.email)!!) {
                    user?.followerCount = user?.followerCount!! - 1
                    user?.followers!!.remove(auth?.currentUser?.email)
                    Log.d("follow", "--() ${user}")
                } else {
                    user?.followerCount = user?.followerCount!! + 1
                    Log.d("follow", "follow(email) ${auth?.currentUser?.email}")
                    user?.followers!![auth?.currentUser?.email!!] = true
                    Log.d("follow", "++() ${user}")

                    if(!userId.equals(auth?.currentUser?.email))
                        FcmPush.instance.sendMessage(userId!!, "님이 회원님을 팔로우했습니다","",
                            curruntNickname.toString())
                }
            user_db.set(user)
        }
        var current_user_db = db.collection("UserInfo")?.document(auth?.currentUser?.email!!)
        current_user_db?.get()?.addOnSuccessListener{
            var current_user = it.toObject(UserInfo::class.java)
            //팔로잉 이벤트
            if (current_user?.followings?.containsKey(userId)!!) {
                current_user?.followingCount = current_user?.followingCount!! - 1
                current_user?.followings!!.remove(userId)

            } else {
                current_user?.followingCount = current_user?.followingCount!! + 1
                Log.d("follow", "follow(email) ${auth?.currentUser?.email}")
                current_user?.followings!![userId!!] = true
            }
            current_user_db.set(current_user)
        }
        follwerUpdate()
    }

    fun follwerUpdate(){
        var user_db = db.collection("UserInfo")?.document(userId!!)
        Log.d("follow", "follow() ${userId}")
        user_db?.addSnapshotListener{snapshot, error ->
            if(snapshot == null) return@addSnapshotListener
            var user = snapshot.toObject(UserInfo::class.java)
            Log.d("follow", "follow() ${user}")
            //이미 목록에 존재하면
            if(user?.followerCount != null){
                binding!!.followerCount.text = user.followerCount.toString()
                //해당 유저를 팔로우 했다면 언팔로우 버튼으로
                if(user.followers.containsKey(auth?.currentUser?.email)){

                    binding!!.accountBtnFollow.text = "언팔로우"
                    //binding!!.accountBtnFollow.setBackgroundColor(context.resources.getColor(R.id.))
                    binding!!.accountBtnFollow.width = 130
                }else{
                    binding!!.accountBtnFollow.text = "팔로우"
                    binding!!.accountBtnFollow.width = 100
                }
            }
            if(user?.followingCount != null){
                binding!!.followingCount.text = user.followingCount.toString()
            }

        }
    }

    inner class MyViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    @SuppressLint("SuspiciousIndentation")
    inner class UserFragmentAdapter : RecyclerView.Adapter<MyViewHolder>(){
        private val itemsCollectionRef = db.collection("content")
        var contents : ArrayList<Content> = arrayListOf()
        var contentsID : ArrayList<String> = arrayListOf()
        var items : ArrayList<Item> = arrayListOf()
        var userId = arguments?.getString("email").toString()
        init {
            //content collect에 접근
            Log.d("usertest", userId)
            itemsCollectionRef?.whereEqualTo("userId",userId)
                ?.addSnapshotListener { snapshot, error ->
                    // var items = mutableListOf<Item>()
                    if(snapshot == null) return@addSnapshotListener

                    //      CoroutineScope(Dispatchers.Main).launch {
                    for(snapshot in snapshot.documents) {
                        if(!contents.contains(snapshot.toObject(Content::class.java))){
                            contents.add(snapshot.toObject(Content::class.java)!!)
                            contentsID.add(snapshot.id)
                            Log.d("TAG","전 ${contents.size}")

                        }

                        notifyDataSetChanged()
                        Log.d("TAG","후 ${contents.size}")
                    }

                    //   }
                    //adapter?.updateList(items)
                }

        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ContentItemBinding.inflate(inflater, parent, false)
            var width = resources.displayMetrics.widthPixels/3
            var imageView = ImageView(parent.context)
            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            /*경미 추가 부분 */
            imageView.setOnClickListener{
                Log.d("TAG","클릭 ${contents.size}")
            }
            /*경미 추가 부분 */
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
                        findNavController().navigate(com.example.android_sns_project.R.id.action_otherUserFragment_to_myContentFragment, bundle)
                    }
                }
            }

        }

        override fun getItemCount(): Int {
            return contents.size
        }


    }

}