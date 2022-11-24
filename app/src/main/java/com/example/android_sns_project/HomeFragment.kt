package com.example.android_sns_project

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationManager
import android.content.Intent
import android.util.JsonReader
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android_sns_project.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    val db = Firebase.firestore
    var auth : FirebaseAuth? = null
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()
        //auth?.currentUser?.email
        val myEmail = auth?.currentUser?.email.toString()
        var follwings: HashMap<String, Boolean>
        var myNickname = ""
        val userInfo = db.collection("UserInfo").document(myEmail)
        userInfo.get().addOnSuccessListener {
            follwings = it["followings"] as HashMap<String, Boolean>
            myNickname = it["nickname"].toString()
        }

        println("################# followings : ")

        // 1. 레퍼런스 가져오기
        val col = db.collection("content")

        // 2. 접근하기 (읽기, 쓰기)
        col.get().addOnSuccessListener {
            for(d in it){
                var content:HomeContent = HomeContent(context, d)
                //LienarLayout에 커스텀 레이아웃 추가
                binding?.scrollLayout?.addView(content.getLayout())

                // 댓글 창 클릭시 댓글 fragment로 이동
                content.getCommentButton().setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("id",content.getID())
                    findNavController().navigate(com.example.android_sns_project.R.id.action_homeFragment_to_commentFragment, bundle)
                }

                // 유저 사진 클릭시 유저 frament로 이동
                content.getUserImage().setOnClickListener {

                    val bundle = Bundle()
                    bundle.putString("email",content.getEmail())
                    if(content.getEmail().equals(auth?.currentUser?.email)){
                        findNavController().navigate(com.example.android_sns_project.R.id.action_homeFragment_to_userFragment, bundle)
                    }else{
                        findNavController().navigate(com.example.android_sns_project.R.id.action_homeFragment_to_otherUserFragment, bundle)
                    }
                }

                // like 클릭시 알림 띄우기 (좋아요 true 일때만)
                content.getLikeButton().setOnClickListener {
                    if(!content.isLikeClick()) {
                        //showNotification("kyum_q")  // 알림 시작
                        FcmPush.instance.sendMessage(content.getEmail(), myNickname+"님이 당신의 게시물을 좋아합니다","좋아요")
                    }
                    content.setLike()
                }
            }
        }

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        return binding?.root

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
            createNotificationChannel()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    private val channelID = "default"

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelID, "default channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "description text of this channel."
        val notificationManager = mainActivity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}