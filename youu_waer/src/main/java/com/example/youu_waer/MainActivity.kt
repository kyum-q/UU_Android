package com.example.youu_waer

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.EXTRA_EVENT_ID
import com.example.youu_waer.databinding.ActivityMainBinding
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat.WearableExtender
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    val db = Firebase.firestore
    var auth: FirebaseAuth? = null
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        //auth?.currentUser?.email
        val myEmail = auth?.currentUser?.email.toString()
        var follwings: HashMap<String, Boolean> = HashMap()
        var myNickname = ""
        val userInfo = db.collection("UserInfo").document(myEmail)
        userInfo.get().addOnSuccessListener {
            follwings = it["followings"] as HashMap<String, Boolean>
            myNickname = it["nickname"].toString()
            println("################# followings : " + follwings)


            println("################# followings : " + follwings)

            // 1. 레퍼런스 가져오기
            val col = db.collection("content")

            // 2. 접근하기 (읽기, 쓰기)
            col.get().addOnSuccessListener {
                for (d in it) {
                    var f = follwings.containsKey(key = d["userId"].toString())
                    if (f) {
                        var content: HomeContent = HomeContent(this, d)
                        //LienarLayout에 커스텀 레이아웃 추가
                        binding?.scrollLayout?.addView(content.getLayout())


                        // like 클릭시 알림 띄우기 (좋아요 true 일때만)
                        content.getLikeButton().setOnClickListener {
                            if (!content.isLikeClick() && !content.getEmail()
                                    .equals(auth?.currentUser?.email)
                            ) {
                               
                            }
                            content.setLike()
                        }
                    }
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}