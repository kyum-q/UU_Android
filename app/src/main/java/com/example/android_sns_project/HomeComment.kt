package com.example.android_sns_project

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeComment {
    val db = Firebase.firestore
    val rootRef = Firebase.storage.reference
    lateinit var customLayout: View

    @SuppressLint("SetTextI18n", "InflateParams")
    constructor(context: Context?, AddCommentID:String, AddCommentText:String)  {

        //추가할 커스텀 레이아웃 가져오기
        val layoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customLayout = layoutInflater.inflate(R.layout.comment, null)

        val userInfo = db.collection("UserInfo").document(AddCommentID)

        //커스텀 레이아웃 내부 뷰 접근
        userInfo.get().addOnSuccessListener {
            val userID = it["nickname"].toString() //d["comment_id"].toString()
            val commentID: TextView = customLayout.findViewById<TextView>(R.id.comment_id)
            commentID.text =userID
        }

        val commentText: TextView = customLayout.findViewById<TextView>(R.id.comment_text)
        commentText.text = AddCommentText//d["comment"].toString()
    }

    public fun getLayout(): View {
        return customLayout
    }


}