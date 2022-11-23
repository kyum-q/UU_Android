package com.example.android_sns_project

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class HomeContent {
    lateinit var customLayout: View
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    var id:String = ""
    var userID:String = "kyun_q"
    var emailInfo:String = "kyun_q"
    var likeClick: Boolean = false
    private var commentButton: ImageButton
    private var likeButton: ImageButton
    var userImageButton: ImageButton
    val likeDescription: TextView
    @SuppressLint("SetTextI18n", "InflateParams")
    constructor(context: Context?, d: DocumentSnapshot)  {

        id = d.id
        emailInfo = d["userId"].toString()

        //추가할 커스텀 레이아웃 가져오기
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customLayout = layoutInflater.inflate(R.layout.home_content, null)

        val userInfo = db.collection("UserInfo").document(emailInfo)

        //커스텀 레이아웃 내부 뷰 접근
        userInfo.get().addOnSuccessListener {
            val userName: TextView = customLayout.findViewById<TextView>(R.id.userID)
            userName.text = it["nickname"].toString()
            val userName2: TextView = customLayout.findViewById<TextView>(R.id.userID2)
            userName2.text = it["nickname"].toString()
        }

        val explain: TextView = customLayout.findViewById<TextView>(R.id.explain)
        explain.text = d["explain"].toString()
        likeDescription = customLayout.findViewById<TextView>(R.id.likeDescription)
        likeDescription.text = "${d["likeCount"].toString()}명이 좋아합니다"

        likeButton = customLayout.findViewById<ImageButton>(R.id.likeButton)
        userImageButton = customLayout.findViewById<ImageButton>(R.id.home_profile)
//        userImage.setOnClickListener{
//            MainActivity().changeFragment( d["email"].toString())
//
//        }

        val likeMd = db.collection("content").document(id).collection("likes")
        likeMd.get().addOnSuccessListener {
            for (like in it)
                if(like.id == userID) {
                    likeButton.setImageResource(R.drawable.heart_click_icon)
                    likeClick = true
                }
        }
        commentButton = customLayout.findViewById<ImageButton>(R.id.commentButton)

        // 이미지 알아내기
        getImage(d["imagePath"].toString())
    }

    private fun getImage(path:String){
            val ref = rootRef.child(path)
            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
                if (it.isSuccessful) {
                    val bmp = BitmapFactory.decodeByteArray(it.result, 0, it.result!!.size)
                    val contentImage: ImageView =
                        customLayout.findViewById<ImageView>(R.id.contentImage)
                    contentImage.setImageBitmap(bmp)
                }
        }
    }

    fun setLike() {
        if (likeClick == false) {
            likeButton.setImageResource(R.drawable.heart_click_icon)
            setLikeCount(likeDescription, 1)
            likeClick = true
        } else {
            setLikeCount(likeDescription, -1)
            likeButton.setImageResource(R.drawable.heart_icon)
            likeClick = false
        }
    }

    public fun getLayout(): View {
        return customLayout
    }

    public fun getCommentButton(): ImageButton {
        return commentButton
    }

    public fun getLikeButton(): ImageButton {
        return likeButton
    }

    public fun getUserImage() :ImageView {
        return userImageButton
    }

    public fun getID() : String {
        return id
    }

    public fun getEmail() : String {
        return emailInfo
    }

    public fun isLikeClick() : Boolean {
        return likeClick
    }

     private fun setLikeCount(likeDescription: TextView, changeCount:Int) {
        // 해당 레퍼런스 가져오기
        val md = db.collection("content").document(id)
        md.get().addOnSuccessListener {
            var likeCount =  Integer.parseInt(it["likeCount"].toString()) + changeCount
            var likeMap = hashMapOf(
                "likeCount" to likeCount
            )
            md.update(likeMap as Map<String, Any>)

            val likeMd = md.collection("likes")
            likeMd.get().addOnSuccessListener {
            if(changeCount>0) {
                var userMap = hashMapOf(
                    "likeUid" to userID
                )
                likeMd.document(userID).set(userMap)
            }
            else likeMd.document(userID).delete()
            }
            likeMd.get().addOnFailureListener {

            }
            likeDescription.text = "${likeCount}명이 좋아합니다"}
    }

}