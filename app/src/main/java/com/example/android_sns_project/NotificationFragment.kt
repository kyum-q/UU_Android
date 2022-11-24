package com.example.android_sns_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android_sns_project.databinding.FragmentCommentBinding
import com.example.android_sns_project.databinding.FragmentNotificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationFragment : Fragment() {
    private var binding: FragmentNotificationBinding? = null
    val db = Firebase.firestore
    private var nickname: String = ""
    private var userID: String = ""
    var auth : FirebaseAuth? = null
    lateinit var customLayout: View

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                               savedInstanceState: Bundle?): View? {

        auth = FirebaseAuth.getInstance()
        //auth?.currentUser?.email

        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val ID = arguments?.getString("id").toString()
        // 해당 레퍼런스 가져오기
        val md = db.collection("content").document(ID)

        md.get().addOnSuccessListener {
            userID = it["userId"].toString()
            val userInfo = db.collection("UserInfo").document(userID)

            //커스텀 레이아웃 내부 뷰 접근
            userInfo.get().addOnSuccessListener {
                nickname = it["nickname"].toString()
            }

            val commendMd = md.collection("comments")
            commendMd.get().addOnSuccessListener {
                for (d in it) {
                    var comment: HomeComment = HomeComment(context, d["commentID"].toString(), d["commentText"].toString())
                    //LienarLayout에 커스텀 레이아웃 추가
                    binding?.notificationLayout?.addView(comment.getLayout())
                }
            }
        }

        return binding?.root

    }
}