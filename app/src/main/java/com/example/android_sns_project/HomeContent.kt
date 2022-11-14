package com.example.android_sns_project

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class HomeContent {
    lateinit var customLayout: View
    val rootRef = Firebase.storage.reference
    var i:Boolean = false
    @SuppressLint("SetTextI18n", "InflateParams")
    constructor(context: Context?, d: QueryDocumentSnapshot) {
        //추가할 커스텀 레이아웃 가져오기
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customLayout = layoutInflater.inflate(R.layout.home_content, null)

        //커스텀 레이아웃 내부 뷰 접근
        val userName: TextView = customLayout.findViewById<TextView>(R.id.userID)
        userName.text = "Kyum_q"
        val userName2: TextView = customLayout.findViewById<TextView>(R.id.userID2)
        userName2.text = "Kyum_q"
        val explain: TextView = customLayout.findViewById<TextView>(R.id.explain)
        explain.text = d["explain"].toString()
        val likeDescription: TextView = customLayout.findViewById<TextView>(R.id.likeDescription)
        likeDescription.text = "${d["likeCount"].toString()}명이 좋아합니다"

        val likeButton: Button = customLayout.findViewById<Button>(R.id.likeButton)
        likeButton.setOnClickListener {
            if (i == true){
                likeButton.setBackgroundColor(R.drawable.heart_click_icon)
                i = false
            }else {
                likeButton.setBackgroundColor(R.drawable.heart_icon)
                i = true
            }
            println("############################## clikeButton Click")
        }

        // 이미지 알아내기
        getImage(d["imagePath"].toString())
    }

    private fun getImage(path:String){
        val token = path.split("gs://android-sns-youu.appspot.com/")
        val ref = rootRef.child(token[1])

        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
            if (it.isSuccessful) {
                val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                //imgView?.setImageBitmap(bmp)
                val contentImage: ImageView = customLayout.findViewById<ImageView>(R.id.contentImage)
                contentImage.setImageBitmap(bmp)
            }
        }
    }

    public fun getLayout(): View {
        return customLayout
    }

}